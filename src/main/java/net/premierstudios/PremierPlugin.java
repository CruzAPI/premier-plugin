package net.premierstudios;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.premierstudios.command.BlackmarketCommand;
import net.premierstudios.command.MarketplaceCommand;
import net.premierstudios.command.SellCommand;
import net.premierstudios.command.TransactionsCommand;
import net.premierstudios.config.DatabaseConfig;
import net.premierstudios.listener.*;
import net.premierstudios.repository.MarketItemRepository;
import net.premierstudios.repository.MarketTransactionRepository;
import net.premierstudios.service.*;
import net.premierstudios.task.BlackmarketRefreshTask;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Getter
public class PremierPlugin extends JavaPlugin
{
	public static final PremierPlugin INSTANCE = new PremierPlugin();
	
	private Economy economy;
	private Permission permission;
	
	private MarketManager marketManager;
	private MarketTransactionLogger marketTransactionLogger;
	private MessageBundleLoader messageBundleLoader;
	private PropertiesLoader propertiesLoader;
	private ResourceCopier resourceCopier;
	
	private DatabaseConfig databaseConfig;
	
	private PlayerListener playerListener;
	
	private MongoClient mongoClient;
	
	private MarketItemRepository marketItemRepository;
	private MarketTransactionRepository marketTransactionRepository;
	
	@Override
	public void onEnable()
	{
		economy = getProvider(Economy.class);
		permission = getProvider(Permission.class);
		
		marketManager = new MarketManager(this);
		marketTransactionLogger = new MarketTransactionLogger(this);
		messageBundleLoader = new MessageBundleLoader(this);
		propertiesLoader = new PropertiesLoader(this);
		resourceCopier = new ResourceCopier(this);
		
		resourceCopier.copyDir("inventory", false);
		resourceCopier.copyDir("message", false);
		resourceCopier.copyFile("database.yml", false);
		
		databaseConfig = new DatabaseConfig(this);
		
		mongoClient = MongoClients.create(new ConnectionString(databaseConfig.getMongoUrl()));
		
		marketItemRepository = new MarketItemRepository(this);
		marketTransactionRepository = new MarketTransactionRepository(this);
		
		marketManager.setMarketItems(marketItemRepository.getAll());
		marketTransactionLogger.setMarketTransactions(marketTransactionRepository.getAll());
		
		registerCommands();
		registerListeners();
		
		scheduleTasks();
	}
	
	@Override
	public void onDisable()
	{
		marketItemRepository.saveAll(marketManager.getMarketItemList());
		marketTransactionRepository.saveAll(marketTransactionLogger.getMarketTransactions());
	}
	
	@NotNull
	private <T> T getProvider(Class<T> type)
	{
		return Objects.requireNonNull(getServer().getServicesManager().getRegistration(type).getProvider());
	}
	
	private void registerCommands()
	{
		registerBasicCommand("blackmarket", new BlackmarketCommand(this));
		registerBasicCommand("marketplace", new MarketplaceCommand(this));
		registerBasicCommand("sell", new SellCommand(this));
		registerBasicCommand("transactions", new TransactionsCommand(this));
	}
	
	private void registerBasicCommand(String label, BasicCommand basicCommand, String... aliases)
	{
		registerBasicCommand(label, Arrays.asList(aliases), basicCommand);
	}
	
	private void registerBasicCommand(String label, Collection<String> aliases, BasicCommand basicCommand)
	{
		LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
		
		manager.registerEventHandler(LifecycleEvents.COMMANDS, event ->
		{
			event.registrar().register(label, aliases, basicCommand);
		});
	}
	
	private void registerListeners()
	{
		registerListener(new BlackmarketInventoryListener(this));
		registerListener(new MarketplaceInventoryListener(this));
		registerListener(playerListener = new PlayerListener(this));
		registerListener(new PremierInventoryListener(this));
		registerListener(new PremierPlayerListener(this));
		registerListener(new WorldListener(this));
	}
	
	private void registerListener(Listener listener)
	{
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	private void scheduleTasks()
	{
		new BlackmarketRefreshTask(this).runTaskTimer(this, 30L * 20L, 60L * 60L * 20L);
	}
}
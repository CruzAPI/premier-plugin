package net.premierstudios;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.premierstudios.command.BlackmarketCommand;
import net.premierstudios.command.MarketplaceCommand;
import net.premierstudios.command.SellCommand;
import net.premierstudios.command.TestCommand;
import net.premierstudios.listener.PlayerListener;
import net.premierstudios.listener.PremierInventoryListener;
import net.premierstudios.listener.PremierPlayerListener;
import net.premierstudios.service.MessageBundleLoader;
import net.premierstudios.service.PropertiesLoader;
import net.premierstudios.service.ResourceCopier;
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
	
	private MessageBundleLoader messageBundleLoader;
	private PropertiesLoader propertiesLoader;
	private ResourceCopier resourceCopier;
	
	private PlayerListener playerListener;
	
	@Override
	public void onEnable()
	{
		economy = getProvider(Economy.class);
		permission = getProvider(Permission.class);
		
		messageBundleLoader = new MessageBundleLoader(this);
		propertiesLoader = new PropertiesLoader(this);
		resourceCopier = new ResourceCopier(this);
		
		resourceCopier.copyResources("inventory", true);
		resourceCopier.copyResources("message", true);
		
		registerCommands();
		registerListeners();
	}
	
	@NotNull
	private <T> T getProvider(Class<T> type)
	{
		return Objects.requireNonNull(getServer().getServicesManager().getRegistration(type).getProvider());
	}
	
	private void registerCommands()
	{
		registerBasicCommand("test", new TestCommand(this));
		registerBasicCommand("sell", new SellCommand(this));
		registerBasicCommand("marketplace", new MarketplaceCommand(this));
		registerBasicCommand("blackmarket", new BlackmarketCommand(this));
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
		registerListener(playerListener = new PlayerListener(this));
		registerListener(new PremierInventoryListener(this));
		registerListener(new PremierPlayerListener(this));
	}
	
	private void registerListener(Listener listener)
	{
		getServer().getPluginManager().registerEvents(listener, this);
	}
}
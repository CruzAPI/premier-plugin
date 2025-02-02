package net.premierstudios.inventory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.premierstudios.PremierPlugin;
import net.premierstudios.config.InventoryConfig;
import net.premierstudios.config.ItemConfig;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public abstract class PremierInventory<I extends PremierInventory<I>>
{
	protected final PremierPlayer premierPlayer;
	
	@Delegate
	protected final Inventory inventory;
	
	protected Map<String, ItemConfig> itemsConfigByKey;
	
	public PremierInventory(PremierPlayer premierPlayer)
	{
		this.premierPlayer = premierPlayer;
		
		InventoryView currentInventoryView = premierPlayer.getOpenInventory();
		Inventory currentInventory = currentInventoryView.getTopInventory();
		
		if(canReuse(currentInventory))
		{
			String legacyTitle = LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(getInventoryConfig().getTitle()));
			currentInventoryView.setTitle(legacyTitle);
			this.inventory = currentInventory;
		}
		else
		{
			this.inventory = getInventoryConfig().createInventory(premierPlayer);
		}
		
		this.itemsConfigByKey = getInventoryConfig().getItemConfigsByKey();;
	}
	
	public void reset()
	{
		for(Map.Entry<String, ItemConfig> entry : itemsConfigByKey.entrySet())
		{
			String key = entry.getKey();
			ItemConfig itemConfig = entry.getValue();
			
			for(int slot : itemConfig.getSlots())
			{
				inventory.setItem(slot, itemConfig.getTranslatedItemStack(premierPlayer));
			}
		}
	}
	
	public void open()
	{
		if(premierPlayer.getOpenInventory().getTopInventory() != inventory)
		{
			Bukkit.broadcastMessage("open");
			premierPlayer.openInventory(inventory);
		}
		
		premierPlayer.setPremierInventory(this);
	}
	
	public void close()
	{
		if(premierPlayer.getPremierInventory() == this)
		{
			Bukkit.broadcastMessage("close");
			premierPlayer.closeInventory();
			premierPlayer.setPremierInventory(null);
		}
	}
	
	public abstract InventoryConfig getInventoryConfig();
	public abstract Map<String, Icon<I>> getIconsByName();
	
	public final Icon<I> getIcon(String name)
	{
		return getIconsByName().get(name);
	}
	
	public abstract I self();
	
	public final PremierPlugin getPremierPlugin()
	{
		return premierPlayer.getPremierPlugin();
	}
	
	private boolean canReuse(Inventory inventory)
	{
		InventoryType type = getInventoryConfig().getInventoryType();
		int size = getInventoryConfig().getSize();
		
		return type == null && inventory.getSize() == size || type == inventory.getType();
	}
}

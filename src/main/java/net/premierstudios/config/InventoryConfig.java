package net.premierstudios.config;

import com.google.common.base.Preconditions;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static net.premierstudios.util.PremierNamespacedKey.ICON_NAME;
import static org.bukkit.persistence.PersistentDataType.STRING;

public interface InventoryConfig extends Config
{
	default String getTitle()
	{
		return getYamlConfiguration().getString("title");
	}
	
	default int getSize()
	{
		return getYamlConfiguration().getInt("size");
	}
	
	default InventoryType getInventoryType()
	{
		return getYamlConfiguration().getObject("type", InventoryType.class);
	}
	
	default Map<String, ItemStack> getItemsByKey()
	{
		ConfigurationSection section = getYamlConfiguration().getConfigurationSection("items");
		
		if(section == null)
		{
			return Collections.emptyMap();
		}
		
		Map<String, ItemStack> items = new HashMap<>();
		
		for(String key : section.getKeys(false))
		{
			ItemStack item = section.getItemStack("itemstack", ItemStack.empty());
			ItemMeta meta = item.getItemMeta();
			
			if(meta != null)
			{
				meta.getPersistentDataContainer().set(ICON_NAME, STRING, key);
				item.setItemMeta(meta);
			}
			
			items.put(key, item);
		}
		
		return items;
	}
	
	default Map<Integer, ItemStack> getItemsBySlot()
	{
		ConfigurationSection section = getYamlConfiguration().getConfigurationSection("items");
		
		if(section == null)
		{
			return Collections.emptyMap();
		}
		
		Map<String, ItemStack> itemsByKey = getItemsByKey();
		Map<Integer, ItemStack> itemsBySlot = new HashMap<>();
		
		for(String key : section.getKeys(false))
		{
			for(Integer slot : section.getIntegerList("slots"))
			{
				if(slot != null)
				{
					itemsBySlot.put(slot, itemsByKey.getOrDefault(key, ItemStack.empty()));
				}
			}
		}
		
		return itemsBySlot;
	}
	
	default Inventory createInventory(PremierPlayer premierPlayer)
	{
		String title = Preconditions.checkNotNull(getTitle());
		InventoryType type = getInventoryType();
		int size = getSize();
		
		if(type != null)
		{
			return premierPlayer.createInventory(type, title);
		}
		
		return premierPlayer.createInventory(size, title);
	}
}

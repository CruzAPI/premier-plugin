package net.premierstudios.config;

import com.google.common.base.Preconditions;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.bukkit.persistence.PersistentDataType.STRING;

public interface InventoryConfig extends Config
{
	NamespacedKey getIconNamespacedKey();
	
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
		String typeName = getYamlConfiguration().getString("type");
		return typeName == null ? null : InventoryType.valueOf(typeName);
	}
	
	default Map<String, ItemConfig> getItemConfigsByKey()
	{
		ConfigurationSection section = getYamlConfiguration().getConfigurationSection("items");
		
		if(section == null)
		{
			return Collections.emptyMap();
		}
		
		Map<String, ItemConfig> itemConfigsByKey = new LinkedHashMap<>();
		
		for(String key : section.getKeys(false))
		{
			ItemStack item = section.getItemStack(key + ".itemstack", ItemStack.empty());
			ItemMeta meta = item.getItemMeta();
			
			if(meta != null)
			{
				meta.getPersistentDataContainer().set(getIconNamespacedKey(), STRING, key);
				item.setItemMeta(meta);
			}
			
			String bundleBaseName =  section.getString(key + ".bundle", "message/message");
			String nameTranslationKey = section.getString(key + ".name-translation-key");
			String loreTranslationKey = section.getString(key + ".lore-translation-key");
			int[] slots = section.getIntegerList(key + ".slots").stream().mapToInt(Integer::intValue).toArray();
			
			itemConfigsByKey.put(key, new ItemConfig(key, item, bundleBaseName, nameTranslationKey, loreTranslationKey, slots));
		}
		
		return itemConfigsByKey;
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

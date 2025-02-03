package net.premierstudios.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.premierstudios.PremierPlugin;
import net.premierstudios.inventory.BlackmarketInventory;
import net.premierstudios.inventory.MarketplaceInventory;
import org.bson.Document;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Base64;
import java.util.UUID;

import static net.premierstudios.util.PremierNamespacedKey.*;
import static org.bukkit.persistence.PersistentDataType.STRING;

@Getter
@Setter
@AllArgsConstructor
public class MarketItem
{
	private final UUID uniqueId;
	private final UUID sellerUniqueId;
	private final ItemStack originalItemStack;
	private final ItemStack itemStack;
	private final double price;
	
	private boolean blackmarket;
	
	public MarketItem(UUID sellerUniqueId, ItemStack itemStack, double price)
	{
		this(UUID.randomUUID(), sellerUniqueId, itemStack.clone(), itemStack, price, false);
		
		ItemMeta meta = itemStack.getItemMeta();
		meta.getPersistentDataContainer().set(MARKET_ITEM_UUID, STRING, uniqueId.toString());
		meta.getPersistentDataContainer().set(MARKETPLACE_ICON_NAME, STRING, MarketplaceInventory.MarketplaceIcon.MARKET_ITEM.key());
		meta.getPersistentDataContainer().set(BLACKMARKET_ICON_NAME, STRING, BlackmarketInventory.BlackmarketIcon.MARKET_ITEM.key());
		itemStack.setItemMeta(meta);
	}
	
	public OfflinePlayer getSeller()
	{
		return PremierPlugin.INSTANCE.getServer().getOfflinePlayer(sellerUniqueId);
	}
	
	public ItemStack getItemStack()
	{
		return itemStack.clone();
	}
	
	public double getBlackmarketPrice()
	{
		return price * 0.5D;
	}
}

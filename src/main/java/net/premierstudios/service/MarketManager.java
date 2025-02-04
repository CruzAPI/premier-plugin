package net.premierstudios.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.premierstudios.PremierPlugin;
import net.premierstudios.event.BlackmarketRefreshEvent;
import net.premierstudios.event.MarketItemUpdateEvent;
import net.premierstudios.market.MarketItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static net.premierstudios.util.PremierNamespacedKey.MARKET_ITEM_UUID;
import static org.bukkit.persistence.PersistentDataType.STRING;

@RequiredArgsConstructor
public class MarketManager
{
	@Getter
	private List<MarketItem> marketItemList = new ArrayList<>();
	private Map<UUID, MarketItem> marketItemByUniqueId = new HashMap<>();
	
	private final PremierPlugin premierPlugin;
	
	public List<MarketItem> getMarketplaceItems()
	{
		return marketItemList.stream().filter(not(MarketItem::isBlackmarket)).toList();
	}
	
	public List<MarketItem> getBlackmarketItems()
	{
		return marketItemList.stream().filter(MarketItem::isBlackmarket).toList();
	}
	
	public MarketItem getMarketItem(ItemStack item)
	{
		if(item == null)
		{
			return null;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return null;
		}
		
		String uuidString = meta.getPersistentDataContainer().get(MARKET_ITEM_UUID, STRING);
		
		if(uuidString == null)
		{
			return null;
		}
		
		return marketItemByUniqueId.get(UUID.fromString(uuidString));
	}
	
	public void addMarketItem(MarketItem marketItem)
	{
		if(marketItemByUniqueId.containsKey(marketItem.getUniqueId()))
		{
			return;
		}
		
		marketItemByUniqueId.put(marketItem.getUniqueId(), marketItem);
		marketItemList.add(marketItem);
		
		premierPlugin.getServer().getPluginManager().callEvent(new MarketItemUpdateEvent(marketItem));
	}
	
	public void removeMarketItem(MarketItem marketItem)
	{
		marketItemByUniqueId.remove(marketItem.getUniqueId());
		marketItemList.remove(marketItem);
		
		premierPlugin.getServer().getPluginManager().callEvent(new MarketItemUpdateEvent(marketItem));
	}
	
	public int refreshBlackmarket()
	{
		List<MarketItem> list = new ArrayList<>(marketItemList);
		Collections.shuffle(list);
		
		int blackmarketCount = 0;
		
		for(int i = 0; i < list.size(); i++)
		{
			MarketItem marketItem = list.get(i);
			boolean blackmarket = i < marketItemList.size() / 5;
			marketItem.setBlackmarket(blackmarket);
			
			if(blackmarket)
			{
				blackmarketCount++;
			}
		}
		
		premierPlugin.getLogger().info("Blackmarket was refreshed with " + blackmarketCount + " random items from Marketplace.");
		premierPlugin.getServer().getPluginManager().callEvent(new BlackmarketRefreshEvent());
		
		return blackmarketCount;
	}
	
	public void setMarketItems(Collection<MarketItem> marketItems)
	{
		marketItemList = new ArrayList<>(marketItems);
		marketItemByUniqueId = marketItems.stream().collect(Collectors.toMap(MarketItem::getUniqueId, Function.identity()));
	}
}

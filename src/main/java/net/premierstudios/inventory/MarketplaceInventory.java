package net.premierstudios.inventory;

import net.milkbowl.vault.economy.Economy;
import net.premierstudios.PremierPlugin;
import net.premierstudios.config.InventoryConfig;
import net.premierstudios.config.ItemConfig;
import net.premierstudios.market.MarketItem;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.premierstudios.config.InventoryConfigEnum.MARKETPLACE;
import static net.premierstudios.message.PremierMessage.*;

public class MarketplaceInventory extends PremierInventory<MarketplaceInventory>
{
	public static final Map<String, Icon<MarketplaceInventory>> ICON_BY_NAME =
			Arrays.stream(MarketplaceIcon.values())
					.collect(Collectors.toUnmodifiableMap(Enum::name, Function.identity()));
	
	public MarketplaceInventory(PremierPlayer premierPlayer)
	{
		super(premierPlayer);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		ItemConfig marketItemConfig = itemsConfigByKey.get(MarketplaceIcon.MARKET_ITEM.key());
		
		if(marketItemConfig != null)
		{
			int[] slots = marketItemConfig.getSlots();
			List<MarketItem> marketplaceItems = getPremierPlugin().getMarketManager().getMarketplaceItems();
			
			for(int i = 0; i < slots.length && i < marketplaceItems.size(); i++)
			{
				int slot = slots[i];
				MarketItem marketItem = marketplaceItems.get(i);
				Context ctx = new Context().marketItem(marketItem);
				inventory.setItem(slot, marketItemConfig.getTranslatedItemStack(premierPlayer, marketItem.getItemStack(), ctx));
			}
		}
	}
	
	@Override
	public InventoryConfig getInventoryConfig()
	{
		return MARKETPLACE;
	}
	
	@Override
	public Map<String, Icon<MarketplaceInventory>> getIconsByName()
	{
		return ICON_BY_NAME;
	}
	
	@Override
	public MarketplaceInventory self()
	{
		return this;
	}
	
	public enum MarketplaceIcon implements Icon<MarketplaceInventory>
	{
		BLACKMARKET
		{
			@Override
			public void onCurrentItemClick(MarketplaceInventory marketplaceInventory, InventoryClickEvent event)
			{
				if(event.getClick() != ClickType.LEFT)
				{
					return;
				}
				
				PremierPlayer premierPlayer = marketplaceInventory.getPremierPlayer();
				premierPlayer.openPremierInventory(new BlackmarketInventory(premierPlayer));
			}
		},
		MARKET_ITEM
		{
			@Override
			public void onCurrentItemClick(MarketplaceInventory marketplaceInventory, InventoryClickEvent event)
			{
				if(event.getClick() != ClickType.LEFT)
				{
					return;
				}
				
				PremierPlugin premierPlugin = marketplaceInventory.getPremierPlugin();
				PremierPlayer premierPlayer = marketplaceInventory.getPremierPlayer();
				Player player = premierPlayer.getPlayer();
				Economy economy = premierPlugin.getEconomy();
				
				ItemStack currentItem = event.getCurrentItem();
				MarketItem marketItem = premierPlugin.getMarketManager().getMarketItem(currentItem);
				
				if(marketItem == null)
				{
					return;
				}
				
				if(marketplaceInventory.getPremierPlayer().getUniqueId().equals(marketItem.getSellerUniqueId()))
				{
					marketplaceInventory.close();
					marketplaceInventory.getPremierPlayer().sendMessage(SELF_PURCHASE);
					return;
				}
				
				if(!economy.has(player, marketItem.getPrice()))
				{
					marketplaceInventory.getPremierPlayer().sendMessage(INSUFFICIENT_BALANCE);
					return;
				}
				
				premierPlayer.openPremierInventory(new ConfirmationInventory(premierPlayer)
				{
					@Override
					public void confirm()
					{
						this.close();
						
						Context ctx = new Context().player(player);
						
						MarketItem marketItem = premierPlugin.getMarketManager().getMarketItem(currentItem);
						
						if(marketItem == null)
						{
							marketplaceInventory.getPremierPlayer().sendMessage(ITEM_NOT_FOR_SALE_ANYMORE, ctx);
							return;
						}
						
						ctx.marketItem(marketItem);
						
						final double salePrice = marketItem.getPrice();
						final double purchasePrice = marketItem.getPrice();
						
						ctx.salePrice(salePrice).purchasePrice(purchasePrice);
						
						if(marketplaceInventory.getPremierPlayer().getUniqueId().equals(marketItem.getSellerUniqueId()))
						{
							marketplaceInventory.close();
							marketplaceInventory.getPremierPlayer().sendMessage(SELF_PURCHASE, ctx);
							return;
						}
						
						if(!economy.has(player, purchasePrice))
						{
							marketplaceInventory.close();
							marketplaceInventory.getPremierPlayer().sendMessage(INSUFFICIENT_BALANCE, ctx);
							return;
						}
						
						OfflinePlayer seller = marketItem.getSeller();
						PremierPlayer sellerPremierPlayer = Optional.ofNullable(seller.getPlayer()).map(premierPlugin.getPlayerListener()::get).orElse(null);
						
						economy.withdrawPlayer(player, purchasePrice);
						economy.depositPlayer(seller, salePrice);
						
						player.getInventory().addItem(marketItem.getOriginalItemStack());
						
						premierPlugin.getMarketManager().removeMarketItem(marketItem);
						premierPlugin.getMarketTransactionLogger().logTransaction(marketItem, premierPlayer, salePrice, purchasePrice);
						
						premierPlayer.sendMessage(PURCHASE_SUCCESSFUL, ctx);
						
						if(sellerPremierPlayer != null)
						{
							sellerPremierPlayer.sendMessage(SALE_COMPLETE, ctx);
						}
					}
				});
			}
		}
	}
}

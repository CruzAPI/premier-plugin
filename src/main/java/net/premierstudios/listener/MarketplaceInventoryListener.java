package net.premierstudios.listener;

import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.event.BlackmarketRefreshEvent;
import net.premierstudios.event.MarketItemUpdateEvent;
import net.premierstudios.inventory.MarketplaceInventory;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class MarketplaceInventoryListener implements Listener
{
	private final PremierPlugin premierPlugin;
	
	@EventHandler
	public void onBlackmarketRefresh(BlackmarketRefreshEvent event)
	{
		for(PremierPlayer premierPlayer : premierPlugin.getPlayerListener().getAll())
		{
			if(premierPlayer.getPremierInventory() instanceof MarketplaceInventory marketplaceInventory)
			{
				marketplaceInventory.reset();
			}
		}
	}
	
	@EventHandler
	public void onMarketItemUpdateEvent(MarketItemUpdateEvent event)
	{
		for(PremierPlayer premierPlayer : premierPlugin.getPlayerListener().getAll())
		{
			if(premierPlayer.getPremierInventory() instanceof MarketplaceInventory marketplaceInventory)
			{
				marketplaceInventory.reset();
			}
		}
	}
}

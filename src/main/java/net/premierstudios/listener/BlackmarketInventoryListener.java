package net.premierstudios.listener;

import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.event.BlackmarketRefreshEvent;
import net.premierstudios.event.MarketItemUpdateEvent;
import net.premierstudios.inventory.BlackmarketInventory;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class BlackmarketInventoryListener implements Listener
{
	private final PremierPlugin premierPlugin;
	
	@EventHandler
	public void onBlackmarketRefresh(BlackmarketRefreshEvent event)
	{
		for(PremierPlayer premierPlayer : premierPlugin.getPlayerListener().getAll())
		{
			if(premierPlayer.getPremierInventory() instanceof BlackmarketInventory blackmarketInventory)
			{
				blackmarketInventory.reset();
			}
		}
	}
	
	@EventHandler
	public void onMarketItemUpdateEvent(MarketItemUpdateEvent event)
	{
		for(PremierPlayer premierPlayer : premierPlugin.getPlayerListener().getAll())
		{
			if(premierPlayer.getPremierInventory() instanceof BlackmarketInventory blackmarketInventory)
			{
				blackmarketInventory.reset();
			}
		}
	}
}

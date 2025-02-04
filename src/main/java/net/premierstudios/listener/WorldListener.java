package net.premierstudios.listener;

import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

@RequiredArgsConstructor
public class WorldListener implements Listener
{
	private final PremierPlugin premierPlugin;
	
	@EventHandler
	public void onSave(WorldSaveEvent event)
	{
		if(premierPlugin.getServer().getWorlds().getFirst() != event.getWorld())
		{
			return;
		}
		
		premierPlugin.getMarketItemRepository().saveAll(premierPlugin.getMarketManager().getMarketItemList());
		premierPlugin.getMarketTransactionRepository().saveAll(premierPlugin.getMarketTransactionLogger().getMarketTransactions());
	}
}

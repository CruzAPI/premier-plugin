package net.premierstudios.listener;

import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

@RequiredArgsConstructor
public class PremierPlayerListener implements Listener
{
	private final PremierPlugin premierPlugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void closePremierInventory(InventoryCloseEvent event)
	{
		if(!(event.getPlayer() instanceof Player player))
		{
			return;
		}
		
		PremierPlayer premierPlayer = premierPlugin.getPlayerListener().get(player);
		premierPlayer.setPremierInventory(null);
	}
}

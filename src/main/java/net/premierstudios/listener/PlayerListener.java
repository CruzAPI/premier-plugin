package net.premierstudios.listener;

import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerListener implements Listener
{
	private final PremierPlugin premierPlugin;
	
	private final Map<UUID, PremierPlayer> premierPlayers = new HashMap<>();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		premierPlayers.put(player.getUniqueId(), new PremierPlayer(premierPlugin, player));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		premierPlayers.remove(player.getUniqueId());
	}
	
	public PremierPlayer get(Player player)
	{
		return get(player.getUniqueId());
	}
	
	public PremierPlayer get(UUID uniqueId)
	{
		return premierPlayers.get(uniqueId);
	}
}

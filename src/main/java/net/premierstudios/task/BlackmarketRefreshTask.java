package net.premierstudios.task;

import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class BlackmarketRefreshTask extends BukkitRunnable
{
	private final PremierPlugin premierPlugin;
	
	@Override
	public void run()
	{
		premierPlugin.getLogger().info("Auto refreshing Blackmarket...");
		premierPlugin.getMarketManager().refreshBlackmarket();
	}
}

package net.premierstudios.inventory;

import net.premierstudios.config.InventoryConfig;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static net.premierstudios.config.InventoryConfigEnum.BLACKMARKET;

public class BlackmarketInventory extends PremierInventory
{
	public BlackmarketInventory(PremierPlayer premierPlayer)
	{
		super(premierPlayer);
	}
	
	@Override
	public void reset()
	{
		getInventoryConfig().getItemsBySlot().forEach(this::setItem);
	}
	
	@Override
	public InventoryConfig getInventoryConfig()
	{
		return BLACKMARKET;
	}
}

package net.premierstudios.inventory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import net.premierstudios.config.InventoryConfig;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
@Getter
public abstract class PremierInventory
{
	protected final PremierPlayer premierPlayer;
	
	@Delegate
	protected final Inventory inventory;
	
	public PremierInventory(PremierPlayer premierPlayer)
	{
		this.premierPlayer = premierPlayer;
		this.inventory = getInventoryConfig().createInventory(premierPlayer);
	}
	
	public abstract void reset();
	
	public void close()
	{
		if(premierPlayer.getPremierInventory() == this)
		{
			premierPlayer.closeInventory();
		}
	}
	
	public abstract InventoryConfig getInventoryConfig();
}

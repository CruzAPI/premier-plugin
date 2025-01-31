package net.premierstudios.inventory;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.premierstudios.config.InventoryConfig;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static net.premierstudios.config.InventoryConfigEnum.MARKETPLACE;

public class MarketplaceInventory extends PremierInventory
{
	public MarketplaceInventory(PremierPlayer premierPlayer)
	{
		super(premierPlayer);
	}
	
	@Override
	@SneakyThrows
	public void reset()
	{
		getInventoryConfig().getItemsBySlot().forEach(this::setItem);
	}
	
	@Override
	public InventoryConfig getInventoryConfig()
	{
		return MARKETPLACE;
	}
}

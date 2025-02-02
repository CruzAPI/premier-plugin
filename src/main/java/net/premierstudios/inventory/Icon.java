package net.premierstudios.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Icon<I extends PremierInventory>
{
	int ordinal();
	String name();
	
	default String key()
	{
		return name();
	}
	
	default void onCurrentItemClick(I premierInventory, InventoryClickEvent event)
	{
	
	}
}

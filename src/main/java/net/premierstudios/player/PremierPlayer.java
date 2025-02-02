package net.premierstudios.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;
import net.kyori.adventure.text.Component;
import net.premierstudios.PremierPlugin;
import net.premierstudios.i18n.Messageable;
import net.premierstudios.inventory.PremierInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Locale;

@RequiredArgsConstructor
@Getter
@Setter
public class PremierPlayer implements Messageable
{
	private final PremierPlugin premierPlugin;
	
	private PremierInventory<?> premierInventory;
	
	@Delegate
	private final Player player;
	
	public void openPremierInventory(PremierInventory<?> premierInventory)
	{
		premierInventory.reset();
		premierInventory.open();
	}
	
	public Inventory createInventory(InventoryType type, String title)
	{
		return premierPlugin.getServer().createInventory(player, type, Component.text(title));
	}
	
	public Inventory createInventory(InventoryType type, Component title)
	{
		return premierPlugin.getServer().createInventory(player, type, title);
	}
	
	public Inventory createInventory(int size, String title)
	{
		return premierPlugin.getServer().createInventory(player, size, Component.text(title));
	}
	
	public Inventory createInventory(int size, Component title)
	{
		return premierPlugin.getServer().createInventory(player, size, title);
	}
	
	@Override
	public Locale getLocale()
	{
		return player.locale();
	}
}

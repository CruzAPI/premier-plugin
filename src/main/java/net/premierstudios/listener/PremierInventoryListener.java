package net.premierstudios.listener;

import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.inventory.PremierInventory;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

import static org.bukkit.persistence.PersistentDataType.STRING;

@RequiredArgsConstructor
public class PremierInventoryListener implements Listener
{
	private final PremierPlugin premierPlugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void cancelInventoryClickByDefault(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player))
		{
			return;
		}
		
		PremierPlayer premierPlayer = premierPlugin.getPlayerListener().get(player);
		
		if(premierPlayer.getPremierInventory() == null)
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCurrentItem(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player))
		{
			return;
		}
		
		PremierPlayer premierPlayer = premierPlugin.getPlayerListener().get(player);
		
		PremierInventory<?> premierInventory = premierPlayer.getPremierInventory();
		
		if(premierInventory == null)
		{
			return;
		}
		
		onCurrentItemHelper(premierInventory, event);
	}

	private <I extends PremierInventory<I>> void onCurrentItemHelper(PremierInventory<I> premierInventory, InventoryClickEvent event)
	{
		Optional.ofNullable(event.getCurrentItem())
				.map(ItemStack::getItemMeta)
				.map(ItemMeta::getPersistentDataContainer)
				.map(container -> container.get(premierInventory.getInventoryConfig().getIconNamespacedKey(), STRING))
				.map(premierInventory::getIcon)
				.ifPresent(icon -> icon.onCurrentItemClick(premierInventory.self(), event));
	}
}

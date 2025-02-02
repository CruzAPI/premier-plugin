package net.premierstudios.inventory;

import net.premierstudios.config.InventoryConfig;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.premierstudios.config.InventoryConfigEnum.CONFIRMATION;

public abstract class ConfirmationInventory extends PremierInventory<ConfirmationInventory>
{
	public static final Map<String, Icon<ConfirmationInventory>> ICON_BY_NAME =
			Arrays.stream(ConfirmationIcon.values())
					.collect(Collectors.toUnmodifiableMap(ConfirmationIcon::name, Function.identity()));
	
	public ConfirmationInventory(PremierPlayer premierPlayer)
	{
		super(premierPlayer);
	}
	
	@Override
	public InventoryConfig getInventoryConfig()
	{
		return CONFIRMATION;
	}
	
	@Override
	public Map<String, Icon<ConfirmationInventory>> getIconsByName()
	{
		return ICON_BY_NAME;
	}
	
	@Override
	public ConfirmationInventory self()
	{
		return this;
	}
	
	public abstract void confirm();
	
	public void cancel()
	{
		close();
		premierPlayer.sendMessage("Operation cancelled.");
	}
	
	public enum ConfirmationIcon implements Icon<ConfirmationInventory>
	{
		CONFIRM
		{
			@Override
			public void onCurrentItemClick(ConfirmationInventory confirmationInventory, InventoryClickEvent event)
			{
				if(event.getClick() == ClickType.LEFT)
				{
					confirmationInventory.confirm();
				}
			}
		},
		
		CANCEL
		{
			@Override
			public void onCurrentItemClick(ConfirmationInventory confirmationInventory, InventoryClickEvent event)
			{
				if(event.getClick() == ClickType.LEFT)
				{
					confirmationInventory.cancel();
				}
			}
		},
	}
}

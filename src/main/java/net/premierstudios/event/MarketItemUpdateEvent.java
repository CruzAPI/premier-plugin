package net.premierstudios.event;

import lombok.RequiredArgsConstructor;
import net.premierstudios.market.MarketItem;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class MarketItemUpdateEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final MarketItem marketItem;
	
	public static HandlerList getHandlerList()
	{
		return HANDLER;
	}
	
	@Override
	@NotNull
	public HandlerList getHandlers()
	{
		return HANDLER;
	}
	
	public boolean isBlackmarket()
	{
		return marketItem.isBlackmarket();
	}
}

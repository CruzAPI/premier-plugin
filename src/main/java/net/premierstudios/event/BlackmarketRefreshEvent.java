package net.premierstudios.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BlackmarketRefreshEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
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
}

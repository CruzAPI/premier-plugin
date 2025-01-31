package net.premierstudios.i18n;

import net.kyori.adventure.text.Component;

import java.util.Locale;

public interface Messageable
{
	void sendMessage(Component component);
	
	default <C> void sendMessage(TranslatableMessage<C> translatableMessage)
	{
		sendMessage(translatableMessage, (C) null);
	}
	
	default <C> void sendMessage(TranslatableMessage<C> translatableMessage, C context)
	{
		sendMessage(translatableMessage.translate(getLocale(), context));
	}
	
	default <C> void sendMessage(boolean flag, TranslatableMessage<C> translatableMessage)
	{
		sendMessage(flag, translatableMessage, null);
	}
	
	default <C> void sendMessage(boolean flag, TranslatableMessage<C> translatableMessage, C context)
	{
		if(flag)
		{
			sendMessage(translatableMessage, context);
		}
	}
	
	default <C> void sendMessage(boolean flag, TranslatableMessageContext<C> message)
	{
		if(flag)
		{
			sendMessage(message);
		}
	}
	
	default <C> void sendMessage(TranslatableMessageContext<C> message)
	{
		sendMessage(message.getTranslatableMessage(), message.getContext());
	}
	
	Locale getLocale();
}

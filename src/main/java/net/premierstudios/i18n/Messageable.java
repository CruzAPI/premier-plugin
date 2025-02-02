package net.premierstudios.i18n;

import net.kyori.adventure.text.Component;

import java.util.Locale;

public interface Messageable
{
	void sendMessage(Component component);
	
	default void sendMessage(TranslatableMessage translatableMessage)
	{
		sendMessage(translatableMessage, null);
	}
	
	default void sendMessage(TranslatableMessage translatableMessage, MessageContext messageContext)
	{
		sendMessage(translatableMessage.translate(getLocale(), messageContext));
	}
	
	default void sendMessage(boolean flag, TranslatableMessage translatableMessage)
	{
		sendMessage(flag, translatableMessage, null);
	}
	
	default void sendMessage(boolean flag, TranslatableMessage translatableMessage, MessageContext messageContext)
	{
		if(flag)
		{
			sendMessage(translatableMessage, messageContext);
		}
	}
	
	default void sendMessage(boolean flag, TranslatableMessageContext message)
	{
		if(flag)
		{
			sendMessage(message);
		}
	}
	
	default void sendMessage(TranslatableMessageContext message)
	{
		sendMessage(message.getTranslatableMessage(), message.getMessageContext());
	}
	
	Locale getLocale();
}

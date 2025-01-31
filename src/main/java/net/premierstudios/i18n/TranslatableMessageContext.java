package net.premierstudios.i18n;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Getter
public class TranslatableMessageContext<C>
{
	private final TranslatableMessage<C> translatableMessage;
	private final C context;
	
	public Component translate(Messageable messageable)
	{
		return translatableMessage.translate(messageable, context);
	}
	
	public Component translate(Locale locale)
	{
		return translatableMessage.translate(locale, context);
	}
	
	public List<Component> translateLines(Messageable messageable)
	{
		return translatableMessage.translateLines(messageable, context);
	}
	
	public List<Component> translateLines(Locale locale)
	{
		return translatableMessage.translateLines(locale, context);
	}
	
	public String translatePlain(Messageable messageable)
	{
		return translatableMessage.translatePlain(messageable, context);
	}
	
	public String translatePlain(Locale locale)
	{
		return translatableMessage.translatePlain(locale, context);
	}
	
	public String translateLegacy(Messageable messageable)
	{
		return translatableMessage.translateLegacy(messageable, context);
	}
	
	public String translateLegacy(Locale locale)
	{
		return translatableMessage.translateLegacy(locale, context);
	}
}

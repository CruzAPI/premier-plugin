package net.premierstudios.i18n;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Getter
public class TranslatableMessageContext
{
	private final TranslatableMessage translatableMessage;
	private final MessageContext messageContext;
	
	public Component translate(Messageable messageable)
	{
		return translatableMessage.translate(messageable, messageContext);
	}
	
	public Component translate(Locale locale)
	{
		return translatableMessage.translate(locale, messageContext);
	}
	
	public List<Component> translateLines(Messageable messageable)
	{
		return translatableMessage.translateLines(messageable, messageContext);
	}
	
	public List<Component> translateLines(Locale locale)
	{
		return translatableMessage.translateLines(locale, messageContext);
	}
	
	public String translatePlain(Messageable messageable)
	{
		return translatableMessage.translatePlain(messageable, messageContext);
	}
	
	public String translatePlain(Locale locale)
	{
		return translatableMessage.translatePlain(locale, messageContext);
	}
	
	public String translateLegacy(Messageable messageable)
	{
		return translatableMessage.translateLegacy(messageable, messageContext);
	}
	
	public String translateLegacy(Locale locale)
	{
		return translatableMessage.translateLegacy(locale, messageContext);
	}
}

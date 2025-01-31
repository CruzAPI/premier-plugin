package net.premierstudios.i18n;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.premierstudios.functional.ContextBuilder;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;

import static java.util.Collections.singletonList;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public interface TranslatableMessage<C>
{
	Locale DEFAULT_LOCALE = Locale.US;
	Set<Locale> SUPPORTED_LOCALES = Set.of
	(
		DEFAULT_LOCALE,
		Locale.forLanguageTag("pt-BR")
	);
	
	static Locale getSupportedLocaleOrDefault(Locale locale)
	{
		return locale != null && SUPPORTED_LOCALES.contains(locale) ? locale : DEFAULT_LOCALE;
	}
	
	String getKey();
	MessageBundle getMessageBundle(Locale locale);
	TagResolver[] translateArguments(Locale locale, C context);
	Plugin getPlugin();
	
	String name();
	C newContext();
	
	default TranslatableMessageContext<C> withContext(ContextBuilder<C> contextBuilder)
	{
		return withContext(contextBuilder.build(newContext()));
	}
	
	default TranslatableMessageContext<C> withContext(C context)
	{
		return new TranslatableMessageContext<>(this, context);
	}
	
	default TranslatableMessageContext<C> withoutContext()
	{
		return withContext((C) null);
	}
	
	default String translatePlain(Messageable messageable, C context)
	{
		return translatePlain(messageable.getLocale(), context);
	}
	
	default String translatePlain(Locale locale, C context)
	{
		locale = getSupportedLocaleOrDefault(locale);
		
		try
		{
			return translatePlain(getMessageBundle(locale), context);
		}
		catch(Exception e)
		{
			if(DEFAULT_LOCALE.equals(locale))
			{
				getPlugin().getLogger().log(Level.SEVERE, "Failed to translate plain message to default locale! locale=" + locale + " key=" + getKey() + " name=" + name(), e);
				return PlainTextComponentSerializer.plainText().serialize(fallback());
			}
			
			getPlugin().getLogger().log(Level.WARNING, "Failed to translate plain message! Plugin will try to translate it to default locale as fallback. locale=" + locale + " key=" + getKey() + " name=" + name(), e);
			return translatePlain(DEFAULT_LOCALE, context);
		}
	}
	
	private String translatePlain(MessageBundle messageBundle, C context)
	{
		return PlainTextComponentSerializer.plainText().serialize(translate(messageBundle, context));
	}
	
	default String translateLegacy(Messageable messageable, C context)
	{
		return translateLegacy(messageable.getLocale(), context);
	}
	
	default String translateLegacy(Locale locale, C context)
	{
		locale = getSupportedLocaleOrDefault(locale);
		
		try
		{
			return translateLegacy(getMessageBundle(locale), context);
		}
		catch(Exception e)
		{
			if(DEFAULT_LOCALE.equals(locale))
			{
				getPlugin().getLogger().log(Level.SEVERE, "Failed to translate legacy message to default locale! locale=" + locale + " key=" + getKey() + " name=" + name(), e);
				return LegacyComponentSerializer.legacySection().serialize(fallback());
			}
			
			getPlugin().getLogger().log(Level.WARNING, "Failed to translate legacy message! Plugin will try to translate it to default locale as fallback. locale=" + locale + " key=" + getKey() + " name=" + name(), e);
			return translateLegacy(DEFAULT_LOCALE, context);
		}
	}
	
	private String translateLegacy(MessageBundle messageBundle, C context)
	{
		return LegacyComponentSerializer.legacySection().serialize(translate(messageBundle, context));
	}
	
	default Component translate(Messageable messageable, C context)
	{
		return translate(messageable.getLocale(), context);
	}
	
	default Component translate(Locale locale, C context)
	{
		locale = getSupportedLocaleOrDefault(locale);
		
		try
		{
			return translate(getMessageBundle(locale), context);
		}
		catch(Exception e)
		{
			if(DEFAULT_LOCALE.equals(locale))
			{
				getPlugin().getLogger().log(Level.SEVERE, "Failed to translate message to default locale! locale=" + locale + " key=" + getKey() + " name=" + name(), e);
				return fallback();
			}
			
			getPlugin().getLogger().log(Level.WARNING, "Failed to translate message! Plugin will try to translate it to default locale as fallback. locale=" + locale + " key=" + getKey() + " name=" + name(), e);
			return translate(DEFAULT_LOCALE, context);
		}
	}
	
	private Component translate(MessageBundle messageBundle, C context)
	{
		return translate(messageBundle, translateArguments(messageBundle.getLocale(), context));
	}
	
	private Component translate(MessageBundle messageBundle, TagResolver... tagResolvers)
	{
		String input = messageBundle.getString(getKey());
		return deserialize(input, tagResolvers);
	}
	
	default List<Component> translateLines(Messageable messageable, C context)
	{
		return translateLines(messageable.getLocale(), context);
	}
	
	default List<Component> translateLines(Locale locale, C context)
	{
		locale = getSupportedLocaleOrDefault(locale);
		
		try
		{
			return translateLines(getMessageBundle(locale), context);
		}
		catch(Exception e)
		{
			if(DEFAULT_LOCALE.equals(locale))
			{
				getPlugin().getLogger().log(Level.SEVERE, "Failed to translate message lines to default locale! locale=" + locale + " key=" + getKey() + " name=" + name(), e);
				return singletonList(fallback());
			}
			
			getPlugin().getLogger().log(Level.WARNING, "Failed to translate message lines! Plugin will try to translate it to default locale as fallback. locale=" + locale + " key=" + getKey() + " name=" + name(), e);
			return translateLines(DEFAULT_LOCALE, context);
		}
	}
	
	private List<Component> translateLines(MessageBundle messageBundle, C context)
	{
		return translateLines(messageBundle, translateArguments(messageBundle.getLocale(), context));
	}
	
	private List<Component> translateLines(MessageBundle messageBundle, TagResolver... tagResolvers)
	{
		final String input = messageBundle.getString(getKey());
		final String[] inputLines = input.split("<br>");
		final List<Component> components = new ArrayList<>();
		
		for(String inputLine : inputLines)
		{
			components.add(deserialize(inputLine, tagResolvers));
		}
		
		return components;
	}
	
	private Component deserialize(String input, TagResolver... tagResolvers)
	{
		Preconditions.checkNotNull(input, "input is null");
		
		return MiniMessage.miniMessage()
				.deserialize(input, tagResolvers)
				.applyFallbackStyle(TextDecoration.ITALIC.withState(false), NamedTextColor.WHITE);
	}
	
	default Component fallback()
	{
		return Component.text("Failed to translate message: " + name()).color(RED);
	}
}

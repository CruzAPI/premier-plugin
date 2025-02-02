package net.premierstudios.i18n;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.premierstudios.PremierPlugin;

import java.util.Locale;
import java.util.Set;

@RequiredArgsConstructor
public class MessageTranslator
{
	public static final Locale DEFAULT_LOCALE = Locale.US;
	public static final Set<Locale> SUPPORTED_LOCALES = Set.of
	(
		DEFAULT_LOCALE,
		Locale.forLanguageTag("pt-BR")
	);
	
	private final PremierPlugin premierPlugin;
	
	public static Locale getSupportedLocaleOrDefault(Locale locale)
	{
		return locale != null && SUPPORTED_LOCALES.contains(locale) ? locale : DEFAULT_LOCALE;
	}
	
	public Component translate(String bundleName, Locale locale, String key, TagResolver[] tagResolvers)
	{
		return translate(premierPlugin.getMessageBundleLoader().getBundle(bundleName, locale), key, tagResolvers);
	}
	
	public Component translate(MessageBundle bundle, String key, TagResolver[] tagResolvers)
	{
		return deserialize(bundle.getString(key), tagResolvers);
	}
	
	private Component deserialize(String input, TagResolver[] tagResolvers)
	{
		Preconditions.checkNotNull(input, "input is null");
		
		return MiniMessage.miniMessage()
				.deserialize(input, tagResolvers)
				.applyFallbackStyle(TextDecoration.ITALIC.withState(false), NamedTextColor.WHITE);
	}
}

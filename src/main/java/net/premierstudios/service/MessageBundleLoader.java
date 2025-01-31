package net.premierstudios.service;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.i18n.MessageBundle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import static net.premierstudios.i18n.TranslatableMessage.DEFAULT_LOCALE;
import static net.premierstudios.i18n.TranslatableMessage.getSupportedLocaleOrDefault;

@RequiredArgsConstructor
public class MessageBundleLoader
{
	private final PremierPlugin premierPlugin;
	
	private final Map<String, MessageBundle> messageBundleMap = new HashMap<>();
	
	public MessageBundle getBundle(String baseName, Locale locale)
	{
		return getSupportedBundle(baseName, getSupportedLocaleOrDefault(locale));
	}
	
	private MessageBundle getSupportedBundle(String baseName, Locale locale)
	{
		final String fullName = getFullName(baseName, locale);
		
		return messageBundleMap.computeIfAbsent(fullName, k ->
		{
			try
			{
				return loadBundle(baseName, locale);
			}
			catch(IOException e)
			{
				if(DEFAULT_LOCALE.equals(locale))
				{
					premierPlugin.getLogger().log(Level.SEVERE, "Failed to load " + fullName);
					throw new RuntimeException(e);
				}
				
				premierPlugin.getLogger().log(Level.WARNING, "Failed to load " + fullName + ". Plugin will try to load for default locale as fallback.", e);
				return getBundle(baseName, DEFAULT_LOCALE);
			}
		});
	}
	
	private MessageBundle loadBundle(final String baseName, final Locale locale) throws IOException
	{
		final String fullName = getFullName(baseName, locale);
		
		Properties properties = premierPlugin.getPropertiesLoader().loadProperties(fullName);
		return new MessageBundle(premierPlugin, baseName, locale, fullName, properties);
	}
	
	private String getFullName(String baseName, Locale locale)
	{
		Preconditions.checkNotNull(baseName, "baseName is null");
		
		return baseName + "_" + locale.toString() + ".properties";
	}
}

package net.premierstudios.i18n;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import org.bukkit.plugin.Plugin;

import java.util.Locale;

@RequiredArgsConstructor
@Getter
public class AnonymousMessage implements TranslatableMessage
{
	private final PremierPlugin premierPlugin = PremierPlugin.INSTANCE;
	
	private final String bundleBaseName;
	private final String key;
	
	@Override
	public MessageBundle getMessageBundle(Locale locale)
	{
		return premierPlugin.getMessageBundleLoader().getBundle(bundleBaseName, locale);
	}
	
	@Override
	public Plugin getPlugin()
	{
		return premierPlugin;
	}
	
	@Override
	public String name()
	{
		return key;
	}
}

package net.premierstudios.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.premierstudios.PremierPlugin;
import net.premierstudios.i18n.MessageBundle;
import net.premierstudios.i18n.TranslatableMessage;
import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.function.BiFunction;

@RequiredArgsConstructor
@Getter
public enum PremierMessage implements TranslatableMessage<PremierMessage.Context>
{
	TEST("test"),
	
	;
	
	private final String key;
	private final BiFunction<Locale, Context, TagResolver[]> translateArgumentsBiFunction;
	
	PremierMessage(String key)
	{
		this(key, (locale, context) -> new TagResolver[0]);
	}
	
	@Override
	public MessageBundle getMessageBundle(Locale locale)
	{
		return PremierPlugin.INSTANCE.getMessageBundleLoader().getBundle("message/message", locale);
	}
	
	@Override
	public TagResolver[] translateArguments(Locale locale, Context context)
	{
		return translateArgumentsBiFunction.apply(locale, context);
	}
	
	@Override
	public Plugin getPlugin()
	{
		return PremierPlugin.INSTANCE;
	}
	
	@Override
	public Context newContext()
	{
		return new Context();
	}
	
	public static class Context
	{
	
	}
}

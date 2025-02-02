package net.premierstudios.message;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.premierstudios.PremierPlugin;
import net.premierstudios.i18n.MessageBundle;
import net.premierstudios.i18n.MessageContext;
import net.premierstudios.i18n.MessageUtil;
import net.premierstudios.i18n.TranslatableMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.kyori.adventure.text.minimessage.tag.resolver.Formatter.number;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed;
import static net.premierstudios.i18n.MessageUtil.displayName;

@RequiredArgsConstructor
@Getter
public enum PremierMessage implements TranslatableMessage
{
	TEST("test"),
	
	;
	
	private final String key;
	
	@Override
	public MessageBundle getMessageBundle(Locale locale)
	{
		return PremierPlugin.INSTANCE.getMessageBundleLoader().getBundle("message/message", locale);
	}
	
	@Override
	public Plugin getPlugin()
	{
		return PremierPlugin.INSTANCE;
	}
	
	@Builder
	public static class Context extends MessageContext
	{
		private Player player;
		private Double originalPrice;
		private Double price;
		private OfflinePlayer seller;
		
		@Override
		public TagResolver[] translateArguments(Locale locale)
		{
			List<TagResolver> tagResolverList = new ArrayList<>();
			
			if(locale != null)
			{
				tagResolverList.add(parsed("locale", locale.toLanguageTag()));
			}
			
			if(player != null)
			{
				tagResolverList.add(component("player", player.displayName()));
			}
			
			if(seller != null)
			{
				tagResolverList.add(component("seller", displayName(seller)));
			}
			
			if(originalPrice != null)
			{
				tagResolverList.add(number("original_price", originalPrice));
			}
			
			if(price != null)
			{
				tagResolverList.add(number("price", price));
			}
			
			return tagResolverList.toArray(TagResolver[]::new);
		}
	}
}

package net.premierstudios.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.premierstudios.PremierPlugin;
import net.premierstudios.i18n.MessageBundle;
import net.premierstudios.i18n.MessageContext;
import net.premierstudios.i18n.TranslatableMessage;
import net.premierstudios.market.MarketItem;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.minimessage.tag.resolver.Formatter.number;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed;
import static net.premierstudios.i18n.MessageUtil.displayName;

@RequiredArgsConstructor
@Getter
public enum PremierMessage implements TranslatableMessage
{
	INSUFFICIENT_BALANCE("insufficient-balance"),
	SELF_PURCHASE("self-purchase"),
	ITEM_NOT_FOR_SALE_ANYMORE("item-not-for-sale-anymore"),
	PURCHASE_SUCCESSFUL("purchase-successful"),
	SALE_COMPLETE("sale-complete"),
	OPERATION_CANCELLED("operation-cancelled"),
	
	COMMAND_SELL_INVALID_PRICE("command.sell.invalid-price"),
	COMMAND_SELL_POSITIVE_PRICE("command.sell.positive-price"),
	COMMAND_SELL_HOLD_ITEM("command.sell.hold-item"),
	COMMAND_SELL_ITEM_LISTED("command.sell.item-listed"),
	COMMAND_SELL_USAGE("command.sell.usage"),
	
	COMMAND_MARKETPLACE_USAGE("command.marketplace.usage"),
	
	COMMAND_BLACKMARKET_USAGE("command.blackmarket.usage"),
	COMMAND_BLACKMARKET_REFRESH("command.blackmarket.refresh"),
	COMMAND_BLACKMARKET_INSUFFICIENT("command.blackmarket.insufficient"),
	
	COMMAND_TRANSACTIONS_EMPTY("command.transactions.empty"),
	COMMAND_TRANSACTIONS_HEADER("command.transactions.header"),
	COMMAND_TRANSACTIONS_SOLD_ROW("command.transactions.sold-row"),
	COMMAND_TRANSACTIONS_BOUGHT_ROW("command.transactions.bought-row"),
	COMMAND_TRANSACTIONS_USAGE("command.transactions.usage"),
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
	
	@Getter
	@Setter
	@Accessors(fluent = true, chain = true)
	public static class Context extends MessageContext
	{
		private Player player;
		private Double price;
		private Double blackmarketPrice;
		private Double salePrice;
		private Double purchasePrice;
		private OfflinePlayer buyer;
		private OfflinePlayer seller;
		private Material material;
		private Integer count;
		private TemporalAccessor date;
		
		public Context marketItem(MarketItem marketItem)
		{
			return seller(marketItem.getSeller())
					.price(marketItem.getPrice())
					.blackmarketPrice(marketItem.getBlackmarketPrice())
					.material(marketItem.getItemStack().getType())
					.count(marketItem.getItemStack().getAmount());
		}
		
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
			
			if(buyer != null)
			{
				tagResolverList.add(component("buyer", displayName(buyer)));
			}
			
			if(price != null)
			{
				tagResolverList.add(number("price", price));
			}
			
			if(blackmarketPrice != null)
			{
				tagResolverList.add(number("blackmarket_price", blackmarketPrice));
			}
			
			if(salePrice != null)
			{
				tagResolverList.add(number("sale_price", salePrice));
			}
			
			if(purchasePrice != null)
			{
				tagResolverList.add(number("purchase_price", purchasePrice));
			}
			
			if(count != null)
			{
				tagResolverList.add(number("count", count));
			}
			
			if(material != null)
			{
				tagResolverList.add(component("material", translatable(material.translationKey())));
			}
			
			if(date != null)
			{
				tagResolverList.add(Formatter.date("date", date));
			}
			
			return tagResolverList.toArray(TagResolver[]::new);
		}
	}
}

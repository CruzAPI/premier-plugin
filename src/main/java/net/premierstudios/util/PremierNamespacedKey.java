package net.premierstudios.util;

import lombok.experimental.UtilityClass;
import net.premierstudios.PremierPlugin;
import org.bukkit.NamespacedKey;

@UtilityClass
public class PremierNamespacedKey
{
	public static final NamespacedKey MARKETPLACE_ICON_NAME = new NamespacedKey(PremierPlugin.INSTANCE, "marketplace_icon_name");
	public static final NamespacedKey BLACKMARKET_ICON_NAME = new NamespacedKey(PremierPlugin.INSTANCE, "blackmarket_icon_name");
	public static final NamespacedKey CONFIRMATION_ICON_NAME = new NamespacedKey(PremierPlugin.INSTANCE, "confirmation_icon_name");
	public static final NamespacedKey MARKET_ITEM_UUID = new NamespacedKey(PremierPlugin.INSTANCE, "market_item_uuid");
}

package net.premierstudios.util;

import lombok.experimental.UtilityClass;
import net.premierstudios.PremierPlugin;
import org.bukkit.NamespacedKey;

@UtilityClass
public class PremierNamespacedKey
{
	public static final NamespacedKey ICON_NAME = new NamespacedKey(PremierPlugin.INSTANCE, "icon_name");
}

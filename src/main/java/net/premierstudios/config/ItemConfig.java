package net.premierstudios.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.premierstudios.i18n.AnonymousMessage;
import net.premierstudios.i18n.MessageContext;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
@Getter
@ToString
public class ItemConfig
{
	private final String key;
	private final ItemStack itemStack;
	private final String bundleBaseName;
	private final String nameTranslationKey;
	private final String loreTranslationKey;
	private final int[] slots;
	
	public ItemStack getTranslatedItemStack(PremierPlayer premierPlayer)
	{
		return getTranslatedItemStack(premierPlayer, itemStack);
	}
	
	public ItemStack getTranslatedItemStack(PremierPlayer premierPlayer, MessageContext messageContext)
	{
		return getTranslatedItemStack(premierPlayer, itemStack, messageContext);
	}
	
	public ItemStack getTranslatedItemStack(PremierPlayer premierPlayer, final ItemStack originalItem)
	{
		return getTranslatedItemStack(premierPlayer, originalItem, null);
	}
	
	public ItemStack getTranslatedItemStack(PremierPlayer premierPlayer, final ItemStack originalItem, MessageContext messageContext)
	{
		ItemStack item = originalItem.clone();
		ItemMeta meta = item.getItemMeta();
		
		if(meta != null && bundleBaseName != null)
		{
			if(nameTranslationKey != null)
			{
				meta.displayName(new AnonymousMessage(bundleBaseName, nameTranslationKey).translate(premierPlayer, messageContext));
			}
			
			if(loreTranslationKey != null)
			{
				meta.lore(new AnonymousMessage(bundleBaseName, loreTranslationKey).translateLines(premierPlayer, messageContext));
			}
			
			item.setItemMeta(meta);
		}
		
		return item;
	}
}

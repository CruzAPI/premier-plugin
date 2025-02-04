package net.premierstudios.util;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;

@UtilityClass
public class ItemStackUtil
{
	public static String serializeItemStackBase64(ItemStack itemStack)
	{
		return Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
	}
	
	public static ItemStack deserializeItemStackBase64(String data)
	{
		return ItemStack.deserializeBytes(Base64.getDecoder().decode(data));
	}
}

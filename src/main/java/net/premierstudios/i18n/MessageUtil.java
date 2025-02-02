package net.premierstudios.i18n;

import com.google.common.base.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.format.TextDecoration;
import net.premierstudios.player.PremierPlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

public class MessageUtil
{
	public static final String UNKNOWN = "???";
	public static final String COLON = ":";
	
	public static String capitalizeAndConcatColon(String word)
	{
		return WordUtils.capitalize(word).concat(COLON);
	}
	
	public static Component usageRequiredArg(Object arg)
	{
		return text("<")
				.append(argToComponent(arg))
				.append(text(">"));
	}
	
	public static Component usageRequiredVarArgs(Object arg)
	{
		return text("<")
				.append(argToComponent(arg))
				.append(text("...>"));
	}
	
	public static Component usageOptionalArg(Object arg)
	{
		return text("[")
				.append(argToComponent(arg))
				.append(text("]"));
	}
	
	public static Component usageOptionalVarArgs(Object arg)
	{
		return text("[")
				.append(argToComponent(arg))
				.append(text("...]"));
	}
	
	public static Component inParentheses(Object arg)
	{
		return text("(")
				.append(argToComponent(arg))
				.append(text(")"));
	}
	
	public static Component argToComponent(Object arg, String prefix, String suffix)
	{
		return text(prefix)
				.append(argToComponent(arg))
				.append(text(suffix));
	}
	
	public static Optional<Object> getArgument(Object[] args, int index)
	{
		try
		{
			return Optional.ofNullable(args[index]);
		}
		catch(Exception e)
		{
			return Optional.empty();
		}
	}
	
	public static Component argToComponent(Object arg, Locale locale)
	{
		if(arg instanceof TranslatableMessage translatableMessage)
		{
			return translatableMessage.translate(locale);
		}
		else if(arg instanceof TranslatableMessageContext translatableMessageContext)
		{
			return translatableMessageContext.translate(locale);
		}
		
		return argToComponent(arg);
	}
	
	public static Component decimalToComponent(Object decimal, DecimalFormat decimalFormat)
	{
		return text(decimalFormat.format(decimal));
	}
	
	public static Component decimalToComponent(Object decimal, String pattern, Locale locale)
	{
		return decimalToComponent(decimal, new DecimalFormat(pattern, new DecimalFormatSymbols(locale)));
	}
	
	public static Component argToComponent(Object arg)
	{
		return arg instanceof Component component ? component : text(arg.toString());
	}
	
	public static String joinMessage(String[] args)
	{
		StringBuilder msg = new StringBuilder();
		
		for(String arg : args)
		{
			msg.append(arg).append(" ");
		}
		
		return msg.toString().trim();
	}
	
	public static String intToRoman(int num)
	{
		if(num <= 0 || num > 3999)
		{
			throw new IllegalArgumentException("The number must be between 1 and 3999");
		}
		
		String[] thousands = { "", "M", "MM", "MMM" };
		String[] hundreds = { "", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM" };
		String[] tens = { "", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC" };
		String[] units = { "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX" };
		
		return thousands[num / 1000] + hundreds[(num % 1000) / 100] + tens[(num % 100) / 10] + units[num % 10];
	}
	
	public static Component toPercentage(Object arg, String pattern, Locale locale)
	{
		if(arg instanceof Float f)
		{
			return toPercentage(f.floatValue(), pattern, locale);
		}
		
		return argToComponent(arg);
	}
	
	public static Component toPercentage(float value, String pattern, Locale locale)
	{
		float percentage = value * 100.0F;
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setRoundingMode(RoundingMode.DOWN);
		return text(decimalFormat.format(percentage) + "%");
	}
	
	public static Component getDecrescentPercentageProgressBar(int percentage)
	{
		percentage = Math.max(0, Math.min(100, percentage));
		return getPercentageProgressBar(100 - percentage);
	}
	
	public static Component getPercentageProgressBar(int percentage)
	{
		percentage = Math.max(0, Math.min(100, percentage));
		int remainingPercentage = 100 - percentage;
		
		Component done = Component.text(Strings.repeat("|", percentage)).color(NamedTextColor.GREEN);
		Component remaining = Component.text(Strings.repeat("|", remainingPercentage)).color(NamedTextColor.DARK_GRAY);
		
		return done.append(remaining).decoration(TextDecoration.ITALIC, false);
	}
	
	public static Component playerName(Object arg)
	{
		return arg instanceof OfflinePlayer offlinePlayer
				? text(Optional.ofNullable(offlinePlayer.getName()).orElse(UNKNOWN))
				: argToComponent(arg);
	}
	
	public static Component displayName(Object arg)
	{
		if(arg == null)
		{
			return Component.text(UNKNOWN);
		}
		
		if(arg instanceof PremierPlayer premierPlayer)
		{
			return premierPlayer.displayName();
		}
		
		return arg instanceof OfflinePlayer offlinePlayer
				? getOfflinePlayerDisplayName(offlinePlayer)
				: argToComponent(arg);
	}
	
	public static Component getOfflinePlayerDisplayName(OfflinePlayer offlinePlayer)
	{
		return getOfflinePlayerDisplayName(offlinePlayer, DARK_GRAY);
	}
	
	public static Component getOfflinePlayerDisplayName(OfflinePlayer offlinePlayer, StyleBuilderApplicable style)
	{
		Player player = offlinePlayer.getPlayer();
		
		return player != null
				? player.displayName()
				: text(Optional.ofNullable(offlinePlayer.getName()).orElse(UNKNOWN)).applyFallbackStyle(style);
	}
}

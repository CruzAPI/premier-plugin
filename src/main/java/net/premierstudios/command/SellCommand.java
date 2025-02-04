package net.premierstudios.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.market.MarketItem;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static net.premierstudios.message.PremierMessage.*;

@RequiredArgsConstructor
public class SellCommand implements BasicCommand
{
	private final PremierPlugin premierPlugin;
	
	@Override
	public void execute(CommandSourceStack commandSourceStack, String[] args)
	{
		if(!(commandSourceStack.getSender() instanceof Player player))
		{
			return;
		}
		
		PremierPlayer premierPlayer = premierPlugin.getPlayerListener().get(player);
		
		if(args.length == 1)
		{
			final double price;
			
			try
			{
				price = Double.parseDouble(args[0]);
			}
			catch(NumberFormatException e)
			{
				premierPlayer.sendMessage(COMMAND_SELL_INVALID_PRICE);
				return;
			}
			
			if(price <= 0.0D)
			{
				premierPlayer.sendMessage(COMMAND_SELL_POSITIVE_PRICE);
				return;
			}
			
			ItemStack itemToSell = player.getEquipment().getItemInMainHand();
			
			if(itemToSell.isEmpty())
			{
				premierPlayer.sendMessage(COMMAND_SELL_HOLD_ITEM);
				return;
			}
			
			MarketItem marketItem = new MarketItem(player.getUniqueId(), itemToSell.clone(), price);
			
			premierPlugin.getMarketManager().addMarketItem(marketItem);
			
			itemToSell.setAmount(0);
			premierPlayer.sendMessage(COMMAND_SELL_ITEM_LISTED);
		}
		else
		{
			premierPlayer.sendMessage(COMMAND_SELL_USAGE);
		}
	}
	
	@Override
	@NotNull
	public String permission()
	{
		return "marketplace.sell";
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player && premierPlugin.getPermission().has(sender, permission());
	}
}

package net.premierstudios.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.market.MarketItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
		
		if(args.length == 1)
		{
			final double price;
			
			try
			{
				price = Double.parseDouble(args[0]);
			}
			catch(NumberFormatException e)
			{
				player.sendMessage("Invalid price format.");
				return;
			}
			
			if(price <= 0.0D)
			{
				player.sendMessage("Price must be positive.");
				return;
			}
			
			ItemStack itemToSell = player.getEquipment().getItemInMainHand();
			
			if(itemToSell.isEmpty())
			{
				player.sendMessage("You need to hold an item.");
				return;
			}
			
			MarketItem marketItem = new MarketItem(player.getUniqueId(), itemToSell.clone(), price);
			
			premierPlugin.getMarketManager().addMarketItem(marketItem);
			
			itemToSell.setAmount(0);
			player.sendMessage("Your item has been added to marketplace.");
		}
		else
		{
			player.sendMessage("Usage: /sell <price>");
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

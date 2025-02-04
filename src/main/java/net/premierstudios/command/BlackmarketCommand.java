package net.premierstudios.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.inventory.BlackmarketInventory;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.premierstudios.message.PremierMessage.*;

@RequiredArgsConstructor
public class BlackmarketCommand implements BasicCommand
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
		
		if(args.length == 0)
		{
			premierPlayer.openPremierInventory(new BlackmarketInventory(premierPlayer));
		}
		else if(args.length == 1 && args[0].equalsIgnoreCase("refresh"))
		{
			if(!premierPlugin.getPermission().has(player, "marketplace.blackmarket.refresh"))
			{
				premierPlayer.sendMessage(COMMAND_BLACKMARKET_USAGE);
				return;
			}
			
			int blackmarketCount = premierPlugin.getMarketManager().refreshBlackmarket();
			
			Context ctx = new Context().count(blackmarketCount);
			
			if(blackmarketCount > 0)
			{
				premierPlayer.sendMessage(COMMAND_BLACKMARKET_REFRESH, ctx);
			}
			else
			{
				premierPlayer.sendMessage(COMMAND_BLACKMARKET_INSUFFICIENT, ctx);
			}
		}
		else
		{
			premierPlayer.sendMessage(COMMAND_BLACKMARKET_USAGE);
		}
	}
	
	@Override
	@NotNull
	public String permission()
	{
		return "marketplace.blackmarket";
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player && premierPlugin.getPermission().has(sender, permission());
	}
}

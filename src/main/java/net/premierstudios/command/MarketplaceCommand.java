package net.premierstudios.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.inventory.MarketplaceInventory;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.premierstudios.message.PremierMessage.COMMAND_MARKETPLACE_USAGE;

@RequiredArgsConstructor
public class MarketplaceCommand implements BasicCommand
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
			premierPlayer.openPremierInventory(new MarketplaceInventory(premierPlayer));
		}
		else
		{
			premierPlayer.sendMessage(COMMAND_MARKETPLACE_USAGE);
		}
	}
	
	@Override
	@NotNull
	public String permission()
	{
		return "marketplace.view";
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player && premierPlugin.getPermission().has(sender, permission());
	}
}

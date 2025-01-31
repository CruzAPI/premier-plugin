package net.premierstudios.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.message.PremierMessage;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class TestCommand implements BasicCommand
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
		premierPlayer.sendMessage(PremierMessage.TEST);
	}
}

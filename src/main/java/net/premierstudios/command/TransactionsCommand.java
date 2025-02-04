package net.premierstudios.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.market.MarketTransaction;
import net.premierstudios.message.PremierMessage;
import net.premierstudios.player.PremierPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static net.premierstudios.message.PremierMessage.*;

@RequiredArgsConstructor
public class TransactionsCommand implements BasicCommand
{
	private final PremierPlugin premierPlugin;
	
	private final Set<UUID> playersInExecution = new HashSet<>();
	
	@Override
	public void execute(CommandSourceStack commandSourceStack, String[] args)
	{
		if(!(commandSourceStack.getSender() instanceof Player player))
		{
			return;
		}
		
		PremierPlayer premierPlayer = premierPlugin.getPlayerListener().get(player);
		
		PremierMessage.Context ctx = new PremierMessage.Context().player(player);
		
		if(args.length == 0)
		{
			if(!playersInExecution.add(player.getUniqueId()))
			{
				return;
			}
			
			premierPlugin.getServer().getScheduler().runTaskAsynchronously(premierPlugin, () ->
			{
				try
				{
					List<MarketTransaction> marketTransactions = premierPlugin.getMarketTransactionLogger().getTransactionsFromUser(premierPlayer);
					
					if(marketTransactions.isEmpty())
					{
						premierPlayer.sendMessage(COMMAND_TRANSACTIONS_EMPTY, ctx);
						return;
					}
					
					premierPlayer.sendMessage(COMMAND_TRANSACTIONS_HEADER, ctx.count(marketTransactions.size()));
					
					for(MarketTransaction marketTransaction : marketTransactions)
					{
						boolean sale = marketTransaction.getSellerUniqueId().equals(player.getUniqueId());
						
						ctx.seller(premierPlugin.getServer().getOfflinePlayer(marketTransaction.getSellerUniqueId()));
						ctx.buyer(premierPlugin.getServer().getOfflinePlayer(marketTransaction.getBuyerUniqueId()));
						ctx.salePrice(marketTransaction.getSalePrice());
						ctx.purchasePrice(marketTransaction.getPurchasePrice());
						ctx.material(marketTransaction.getOriginalItemStack().getType());
						ctx.count(marketTransaction.getOriginalItemStack().getAmount());
						ctx.date(marketTransaction.getCreationDate());
						
						premierPlayer.sendMessage(sale ? COMMAND_TRANSACTIONS_SOLD_ROW : COMMAND_TRANSACTIONS_BOUGHT_ROW, ctx);
					}
				}
				finally
				{
					playersInExecution.remove(player.getUniqueId());
				}
			});
		}
		else
		{
			premierPlayer.sendMessage(COMMAND_TRANSACTIONS_USAGE, ctx);
		}
	}
	
	@Override
	@NotNull
	public String permission()
	{
		return "marketplace.transactions";
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player && premierPlugin.getPermission().has(sender, permission());
	}
}

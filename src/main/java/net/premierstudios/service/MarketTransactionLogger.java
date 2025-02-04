package net.premierstudios.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.premierstudios.PremierPlugin;
import net.premierstudios.market.MarketItem;
import net.premierstudios.market.MarketTransaction;
import net.premierstudios.player.PremierPlayer;

import java.util.*;

import static java.util.Comparator.comparing;

@RequiredArgsConstructor
@Getter
public class MarketTransactionLogger
{
	private final PremierPlugin premierPlugin;
	
	@Setter
	private Set<MarketTransaction> marketTransactions = new LinkedHashSet<>();
	
	public void logTransaction(MarketItem marketItem, PremierPlayer buyer, double salePrice, double purchasePrice)
	{
		logTransaction(new MarketTransaction(marketItem, buyer.getUniqueId(), salePrice, purchasePrice));
	}
	
	public void logTransaction(MarketTransaction marketTransaction)
	{
		marketTransactions.add(marketTransaction);
	}
	
	public List<MarketTransaction> getTransactionsFromUser(PremierPlayer premierPlayer)
	{
		return getTransactionsFromUser(premierPlayer.getUniqueId());
	}
	
	public List<MarketTransaction> getTransactionsFromUser(UUID userUniqueId)
	{
		return marketTransactions.stream()
				.filter(marketTransaction -> marketTransaction.isUserInvolved(userUniqueId))
				.sorted(comparing(MarketTransaction::getCreationDate).reversed())
				.toList();
	}
}

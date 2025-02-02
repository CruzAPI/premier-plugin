package net.premierstudios.service;

import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import net.premierstudios.market.MarketItem;
import net.premierstudios.market.MarketTransaction;
import net.premierstudios.player.PremierPlayer;

import java.util.LinkedHashSet;
import java.util.Set;

@RequiredArgsConstructor
public class MarketTransactionLogger
{
	private final PremierPlugin premierPlugin;
	
	public Set<MarketTransaction> unsavedMarketTransactions = new LinkedHashSet<>();
	
	public void logTransaction(MarketItem marketItem, PremierPlayer buyer)
	{
		logTransaction(new MarketTransaction(marketItem, buyer.getUniqueId()));
	}
	
	public void logTransaction(MarketTransaction marketTransaction)
	{
		unsavedMarketTransactions.add(marketTransaction);
	}
}

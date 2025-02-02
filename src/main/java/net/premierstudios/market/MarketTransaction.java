package net.premierstudios.market;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MarketTransaction
{
	private final UUID uniqueId;
	private final UUID sellerUniqueId;
	private final UUID buyerUniqueId;
	private final ItemStack originalItemStack;
	private final double price;
	private final double blackmarketPrice;
	private final boolean blackmarket;
	private final LocalDateTime creationDate;
	
	public MarketTransaction(MarketItem marketItem, UUID buyerUniqueId)
	{
		this(marketItem.getSellerUniqueId(),
				buyerUniqueId,
				marketItem.getOriginalItemStack(),
				marketItem.getPrice(),
				marketItem.getBlackmarketPrice(),
				marketItem.isBlackmarket());
	}
	
	public MarketTransaction(UUID sellerUniqueId,
			UUID buyerUniqueId,
			ItemStack originalItemStack,
			double price,
			double blackmarketPrice,
			boolean blackmarket)
	{
		this.uniqueId = UUID.randomUUID();
		this.sellerUniqueId = sellerUniqueId;
		this.buyerUniqueId = buyerUniqueId;
		this.originalItemStack = originalItemStack;
		this.price = price;
		this.blackmarketPrice = blackmarketPrice;
		this.blackmarket = blackmarket;
		this.creationDate = LocalDateTime.now();
	}
}

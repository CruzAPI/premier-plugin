package net.premierstudios.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MarketTransaction
{
	private final UUID uniqueId;
	private final UUID sellerUniqueId;
	private final UUID buyerUniqueId;
	private final ItemStack originalItemStack;
	private final double price;
	private final double blackmarketPrice;
	private final double salePrice;
	private final double purchasePrice;
	private final boolean blackmarket;
	private final LocalDateTime creationDate;
	
	public MarketTransaction(MarketItem marketItem, UUID buyerUniqueId, double salePrice, double purchasePrice)
	{
		this(UUID.randomUUID(),
				marketItem.getSellerUniqueId(),
				buyerUniqueId,
				marketItem.getOriginalItemStack(),
				marketItem.getPrice(),
				marketItem.getBlackmarketPrice(),
				salePrice,
				purchasePrice,
				marketItem.isBlackmarket(),
				LocalDateTime.now());
	}
	
	public boolean isUserInvolved(UUID userUniqueId)
	{
		return userUniqueId.equals(sellerUniqueId) || userUniqueId.equals(buyerUniqueId);
	}
}

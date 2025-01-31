package net.premierstudios;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class MarketItem
{
	private final UUID seller;
	private final ItemStack itemStack;
	private final double price;
}

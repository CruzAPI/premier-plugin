package net.premierstudios.repository;

import com.mongodb.client.MongoCollection;
import net.premierstudios.PremierPlugin;
import net.premierstudios.market.MarketItem;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.premierstudios.util.ItemStackUtil.deserializeItemStackBase64;
import static net.premierstudios.util.ItemStackUtil.serializeItemStackBase64;

public class MarketItemRepository
{
	private final PremierPlugin premierPlugin;
	private final MongoCollection<Document> marketItemCollection;
	
	public MarketItemRepository(PremierPlugin premierPlugin)
	{
		this.premierPlugin = premierPlugin;
		this.marketItemCollection = premierPlugin.getMongoClient().getDatabase("premier").getCollection("market");
	}
	
	public void saveAll(List<MarketItem> marketItemList)
	{
		long deleteCount = marketItemCollection.deleteMany(new Document()).getDeletedCount();
		premierPlugin.getLogger().info("Deleted " + deleteCount + " market items from DB.");
		
		if (!marketItemList.isEmpty())
		{
			List<Document> documents = marketItemList.stream()
					.map(this::marketItemToDocument)
					.toList();
			
			marketItemCollection.insertMany(documents);
			premierPlugin.getLogger().info("Saved " + documents.size() + " market items to DB.");
		}
	}
	
	public List<MarketItem> getAll()
	{
		List<MarketItem> marketItemList = marketItemCollection.find().map(this::documentToMarketItem).into(new ArrayList<>());
		premierPlugin.getLogger().info("Returning " + marketItemList.size() + " market items from DB.");
		return marketItemList;
	}
	
	private Document marketItemToDocument(MarketItem item)
	{
		return new Document("uniqueId", item.getUniqueId().toString())
				.append("sellerUniqueId", item.getSellerUniqueId().toString())
				.append("originalItemStack", serializeItemStackBase64(item.getOriginalItemStack()))
				.append("itemStack", serializeItemStackBase64(item.getItemStack()))
				.append("price", item.getPrice())
				.append("blackmarket", item.isBlackmarket());
	}
	
	private MarketItem documentToMarketItem(Document doc)
	{
		UUID uniqueId = UUID.fromString(doc.getString("uniqueId"));
		UUID sellerUniqueId = UUID.fromString(doc.getString("sellerUniqueId"));
		ItemStack originalItemStack = deserializeItemStackBase64(doc.getString("originalItemStack"));
		ItemStack itemStack = deserializeItemStackBase64(doc.getString("itemStack"));
		double price = doc.getDouble("price");
		boolean blackmarket = doc.getBoolean("blackmarket");
		
		return new MarketItem(uniqueId, sellerUniqueId, originalItemStack, itemStack, price, blackmarket);
	}
}

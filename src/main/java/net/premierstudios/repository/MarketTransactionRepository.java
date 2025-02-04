package net.premierstudios.repository;

import com.mongodb.client.MongoCollection;
import net.premierstudios.PremierPlugin;
import net.premierstudios.market.MarketTransaction;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static net.premierstudios.util.ItemStackUtil.deserializeItemStackBase64;
import static net.premierstudios.util.ItemStackUtil.serializeItemStackBase64;

public class MarketTransactionRepository
{
	private final PremierPlugin premierPlugin;
	private final MongoCollection<Document> marketTransactionCollection;
	
	public MarketTransactionRepository(PremierPlugin premierPlugin)
	{
		this.premierPlugin = premierPlugin;
		this.marketTransactionCollection = premierPlugin.getMongoClient().getDatabase("premier").getCollection("marketTransactions");
	}
	
	public void saveAll(Collection<MarketTransaction> marketTransactions)
	{
		long deleteCount = marketTransactionCollection.deleteMany(new Document()).getDeletedCount();
		premierPlugin.getLogger().info("Deleted " + deleteCount + " market transactions from DB.");
		
		if (!marketTransactions.isEmpty())
		{
			List<Document> documents = marketTransactions.stream()
					.map(this::marketTransactionToDocument)
					.toList();
			
			marketTransactionCollection.insertMany(documents);
			premierPlugin.getLogger().info("Saved " + documents.size() + " market transactions to DB.");
		}
	}
	
	public Set<MarketTransaction> getAll()
	{
		Set<MarketTransaction> marketTransactions = marketTransactionCollection.find().map(this::documentToMarketTransaction).into(new HashSet<>());
		premierPlugin.getLogger().info("Returning " + marketTransactions.size() + " market transactions from DB.");
		return marketTransactions;
	}
	
	private Document marketTransactionToDocument(MarketTransaction transaction)
	{
		return new Document()
				.append("uniqueId", transaction.getUniqueId().toString())
				.append("sellerUniqueId", transaction.getSellerUniqueId().toString())
				.append("buyerUniqueId", transaction.getBuyerUniqueId().toString())
				.append("originalItemStack", serializeItemStackBase64(transaction.getOriginalItemStack()))
				.append("price", transaction.getPrice())
				.append("blackmarketPrice", transaction.getBlackmarketPrice())
				.append("salePrice", transaction.getSalePrice())
				.append("purchasePrice", transaction.getPurchasePrice())
				.append("blackmarket", transaction.isBlackmarket())
				.append("creationDate", transaction.getCreationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
	}
	
	private MarketTransaction documentToMarketTransaction(Document doc)
	{
		UUID uniqueId = UUID.fromString(doc.getString("uniqueId"));
		UUID sellerUniqueId = UUID.fromString(doc.getString("sellerUniqueId"));
		UUID buyerUniqueId = UUID.fromString(doc.getString("buyerUniqueId"));
		ItemStack originalItemStack = deserializeItemStackBase64(doc.getString("originalItemStack"));
		double price = doc.getDouble("price");
		double blackmarketPrice = doc.getDouble("blackmarketPrice");
		double salePrice = doc.getDouble("salePrice");
		double purchasePrice = doc.getDouble("purchasePrice");
		boolean blackmarket = doc.getBoolean("blackmarket");
		LocalDateTime creationDate = LocalDateTime.parse(doc.getString("creationDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		return new MarketTransaction(uniqueId, sellerUniqueId, buyerUniqueId, originalItemStack, price, blackmarketPrice, salePrice, purchasePrice, blackmarket, creationDate);
	}
}

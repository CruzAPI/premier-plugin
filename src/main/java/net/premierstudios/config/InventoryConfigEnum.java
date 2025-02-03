package net.premierstudios.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static net.premierstudios.util.PremierNamespacedKey.*;

@RequiredArgsConstructor
@Getter
public enum InventoryConfigEnum implements InventoryConfig
{
	MARKETPLACE(new File(PremierPlugin.INSTANCE.getDataFolder(), "inventory/marketplace.yml"), MARKETPLACE_ICON_NAME),
	BLACKMARKET(new File(PremierPlugin.INSTANCE.getDataFolder(), "inventory/blackmarket.yml"), BLACKMARKET_ICON_NAME),
	CONFIRMATION(new File(PremierPlugin.INSTANCE.getDataFolder(), "inventory/confirmation.yml"), CONFIRMATION_ICON_NAME),
	;
	
	private final File file;
	private final NamespacedKey iconNamespacedKey;
	
	private YamlConfiguration yamlConfiguration;
	
	public final YamlConfiguration getYamlConfiguration()
	{
		return yamlConfiguration == null ? yamlConfiguration = reload() : yamlConfiguration;
	}
}

package net.premierstudios.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.premierstudios.PremierPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@RequiredArgsConstructor
@Getter
public enum InventoryConfigEnum implements InventoryConfig
{
	MARKETPLACE(new File(PremierPlugin.INSTANCE.getDataFolder(), "inventory/marketplace.yml")),
	BLACKMARKET(new File(PremierPlugin.INSTANCE.getDataFolder(), "inventory/blackmarket.yml")),
	;
	
	private final File file;
	
	private YamlConfiguration yamlConfiguration;
	
	public final YamlConfiguration getYamlConfiguration()
	{
		return yamlConfiguration == null ? yamlConfiguration = reload() : yamlConfiguration;
	}
	
	@Override
	public final YamlConfiguration reload()
	{
		return YamlConfiguration.loadConfiguration(file);
	}
}

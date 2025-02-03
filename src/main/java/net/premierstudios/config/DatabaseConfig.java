package net.premierstudios.config;

import lombok.Getter;
import net.premierstudios.PremierPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

@Getter
public class DatabaseConfig implements Config
{
	private final PremierPlugin premierPlugin;
	private final File file;
	
	public DatabaseConfig(PremierPlugin premierPlugin)
	{
		this.premierPlugin = premierPlugin;
		
		this.file = new File(premierPlugin.getDataFolder(), "database.yml");
	}
	
	public String getHost()
	{
		return Objects.requireNonNull(getYamlConfiguration().getString("host"));
	}
	
	public int getPort()
	{
		if(!getYamlConfiguration().isInt("port"))
		{
			throw new NullPointerException("port is null");
		}
		
		return getYamlConfiguration().getInt("port");
	}
	
	public String getUser()
	{
		return Objects.requireNonNull(getYamlConfiguration().getString("user"));
	}
	
	public String getPassword()
	{
		return Objects.requireNonNull(getYamlConfiguration().getString("password"));
	}
	
	public String getDatabase()
	{
		return Objects.requireNonNull(getYamlConfiguration().getString("database"));
	}
	
	public String getAuthSource()
	{
		return Objects.requireNonNull(getYamlConfiguration().getString("authSource"));
	}
	
	private YamlConfiguration yamlConfiguration;
	
	public final YamlConfiguration getYamlConfiguration()
	{
		return yamlConfiguration == null ? yamlConfiguration = reload() : yamlConfiguration;
	}
	
	public String getMongoUrl()
	{
		return "mongodb://" + getUser() + ":" + getPassword() + "@" + getHost() + ":" + getPort() + "/" + getDatabase() + "?authSource=" + getAuthSource();
	}
}


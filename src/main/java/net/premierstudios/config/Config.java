package net.premierstudios.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public interface Config
{
	File getFile();
	YamlConfiguration getYamlConfiguration();
	
	default YamlConfiguration reload()
	{
		return YamlConfiguration.loadConfiguration(getFile());
	}
}

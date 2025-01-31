package net.premierstudios.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public interface Config
{
	File getFile();
	YamlConfiguration getYamlConfiguration();
	YamlConfiguration reload();
}

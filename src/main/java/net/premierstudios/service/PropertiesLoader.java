package net.premierstudios.service;

import lombok.RequiredArgsConstructor;
import net.premierstudios.PremierPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@RequiredArgsConstructor
public class PropertiesLoader
{
	private final PremierPlugin premierPlugin;
	
	public Properties loadProperties(String fileName) throws IOException
	{
		return loadProperties(new File(premierPlugin.getDataFolder(), fileName));
	}
	
	public Properties loadProperties(File file) throws IOException
	{
		Properties properties = new Properties();
		
		if(!file.exists())
		{
			throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
		}
		
		try(FileInputStream input = new FileInputStream(file))
		{
			properties.load(input);
			premierPlugin.getLogger().info("Properties loaded: " + file.getAbsolutePath());
			
			return properties;
		}
	}
}

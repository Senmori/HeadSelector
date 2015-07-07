package net.senmori.headselector.config;

import java.io.File;

import net.senmori.headselector.HeadSelector;
import net.senmori.headselector.util.FileUtil;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;


public class MainConfig 
{

	public static void init() 
	{
		if(!HeadSelector.getInstance().getDataFolder().exists())
		{
			HeadSelector.getInstance().getDataFolder().mkdirs();
		}
		HeadSelector.configFile = new File(HeadSelector.getInstance().getDataFolder(), "config.yml");
		if(!HeadSelector.configFile.exists())
		{
			HeadSelector.configFile.getParentFile().mkdirs();
			FileUtil.copy(HeadSelector.getInstance().getResource("config.yml"), HeadSelector.configFile);
		}
		HeadSelector.pluginConfig = YamlConfiguration.loadConfiguration(HeadSelector.configFile);
		HeadSelector.log.warning("Loaded config.yml");
		loadConfig();
	}

	private static void loadConfig() 
	{
		
	}

}

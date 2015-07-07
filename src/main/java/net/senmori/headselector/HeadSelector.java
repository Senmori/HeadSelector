package net.senmori.headselector;

import java.io.File;
import java.util.logging.Logger;

import net.senmori.headselector.config.CacheConfig;
import net.senmori.headselector.config.MainConfig;
import net.senmori.headselector.listener.PlayerListener;
import net.senmori.headselector.sqlite.SQLite;
import net.senmori.headselector.sqlite.pools.ConnectionPool;
import net.senmori.headselector.util.LogHandler;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class HeadSelector extends JavaPlugin
{
	public static Logger log;
	
	// pdf values
	public static String name;
	public static String author;
	public PluginDescriptionFile pdf;
	// instance var
	private static HeadSelector instance;
	
	// config files and FileConfiguration
	public static File configFile;
	public static File cacheFile;
	public static FileConfiguration pluginConfig;
	public static FileConfiguration cacheConfig;
	
	//SQL Stuff
	SQLite sqlInstance;
	
	
	// Config
	
	@Override
	public void onEnable()
	{
		pdf = getDescription();
		name = pdf.getName();
		author = pdf.getAuthors().get(0);
		
		log = getLogger();
		
		// SQLite stuff
		sqlInstance = new SQLite(getInstance());
		
		// Listener(s)
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		LogHandler.info("Loaded listeners");
		
		// Config
		MainConfig.init();
		
		instance = this;
	}
	
	@Override
	public void onDisable()
	{
		// save configs
		instance = null;
	}
	
	public static HeadSelector getInstance()
	{
		return instance;
	}
}

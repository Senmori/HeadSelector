package net.senmori.headselector.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.senmori.headselector.HeadSelector;
import net.senmori.headselector.util.FileUtil;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class CacheConfig
{
	// map for storing heads and values
	private static HashMap<String,String> entries = new HashMap<String,String>();
	
	// URLs
	private static String mainCollectionURL = "http://heads.freshcoal.com/maincollection.php";
	private static String easterURL = "http://heads.freshcoal.com/heads.php?query=easter";
	private static String christmasURL = "http://heads.freshcoal.com/heads.php?query=christmas";
	private static String blankQueryURL = "http://heads.freshcoal.com/heads.php?query=";
	
	// load tabs from heads.freshcoal
	private static HashMap<Integer,String> tabs = new HashMap<Integer,String>();

	public static void init()
	{
		HeadSelector.cacheFile = new File(HeadSelector.getInstance().getDataFolder(), "cache.yml");
		if(!HeadSelector.cacheFile.exists())
		{
			FileUtil.copy(HeadSelector.getInstance().getResource("cache.yml"), HeadSelector.cacheFile);
		}
		HeadSelector.cacheConfig = YamlConfiguration.loadConfiguration(HeadSelector.cacheFile);
		HeadSelector.log.warning("Loaded cache.yml");
		
		// load tabs
		tabs.put(1, "Food");
		tabs.put(2, "Devices");
		tabs.put(3, "Misc");
		tabs.put(4, "Alphabet");
		tabs.put(5, "Interior");
		tabs.put(6, "Color");
		tabs.put(7, "Blocks");
		tabs.put(8, "Mobs");
		tabs.put(9, "Games");
		tabs.put(10, "Characters");
		tabs.put(11, "Pokemon");
		
		loadConfig();
	}


	private static void loadConfig() 
	{
		Document doc = null;
		try 
		{
			doc = Jsoup.connect(mainCollectionURL).get();
		} catch (IOException e)
		{
			e.printStackTrace(); // is the site down?
		}
		
		int total = 0;
		int input = 0;
		// Grab name/value based on tabs and reparse
		for(int count : tabs.keySet())
		{
			entries.clear();
			Document tabDoc = Jsoup.parse(doc.select("div#tabs-" + count).toString());
			for(Element el : tabDoc.getElementsByTag("img"))
			{
				total++;
				String json = extractJson(el);
				String name = extractName(json);
				String base64 = extractTextureValue(json);
				
				entries.put(name, base64);
				HeadSelector.cacheConfig.set(name, base64);
				HeadSelector.log.warning(name + ":" + base64);
				input++;
			}
			HeadSelector.log.warning("Input " + entries.size() + " into " + HeadSelector.cacheFile.getName());
			if(total > 0) break;
		}
	}
	
	
	public static void save()
	{
		
	}
	
	
	private static String extractName(String json)
	{
		return json.toString().substring(json.toString().indexOf("Name:")+11, json.toString().indexOf("}")-6);
	}
	
	private static String extractTextureValue(String json)
	{
		return json.toString().substring(json.toString().indexOf("Value:")+12, json.toString().lastIndexOf("]")-7);
	}
	
	private static String extractJson(Element e)
	{
		return e.toString().substring(e.toString().indexOf("{"), e.toString().lastIndexOf("}")+1);
	}
}

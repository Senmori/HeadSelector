package net.senmori.headselector.managers;

import java.util.ArrayList;
import java.util.List;

import net.senmori.headselector.util.CustomHead;

public class HeadManager 
{
	private List<CustomHead> customHeads;
	public HeadManager() 
	{
		customHeads = new ArrayList<CustomHead>();
	}
	
	
	
	private List<CustomHead> getRegisteredHeads()
	{
		return customHeads;
	}

}

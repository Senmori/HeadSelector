package net.senmori.headselector.util;

import org.json.simple.JSONObject;

public class CustomHead 
{

	private String displayName;
	private String base64;
	private String category;
	
	public CustomHead(String displayName, String base64, String category) 
	{
		this.displayName = displayName;
		this.base64 = base64;
		this.category = category;
	}
	
	public CustomHead(String displayName, String base64)
	{
		this.displayName = displayName;
		this.base64 = base64;
		this.category = "Submitted";
	}

	public String getDisplayName() 
	{
		return displayName;
	}

	public String getBase64() 
	{
		return base64;
	}

	public String getCategory() 
	{
		return category;
	}
	
	public String toJSONString()
	{
		// {displayName:base64}
		StringBuffer sb = new StringBuffer();
		
		sb.append("{");
		
		sb.append(JSONObject.escape("\"" + getDisplayName() + "\""));
		sb.append(":");
		sb.append(JSONObject.escape("\"" + getBase64() + "\""));
		sb.append("}");
		
		return sb.toString();
	}

}

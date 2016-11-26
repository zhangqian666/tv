package com.iptv.common.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.iptv.common.ParseUtil;

public class JsonUtil
{
	public static String parseString(JSONObject jo, String name)
    {
		return parseString(jo, name, "");
    }
	
	public static String parseString(JSONObject jo, String name, String defaultValue)
    {
		if (jo.has(name))
		{
			try
	        {
				return jo.getString(name);
	        }
	        catch (JSONException e)
	        {
	            LogUtils.error("JSONException:" + e.toString());
	        }
		}
		
		return defaultValue;
    }
	
	public static int parseInt(JSONObject jo, String name)
    {
		return parseInt(jo, name, 0);
    }
	
	public static int parseInt(JSONObject jo, String name, int defaultValue)
    {
		if (jo.has(name))
		{
			try
	        {
				return ParseUtil.parseInt(jo.getString(name), defaultValue);
	        }
	        catch (JSONException e)
	        {
	            LogUtils.error("JSONException:" + e.toString());
	        }
		}
		
		return defaultValue;
    }
	
	public static boolean parseBoolean(JSONObject jo, String name)
    {
		return parseBoolean(jo, name, false);
    }
	
	public static boolean parseBoolean(JSONObject jo, String name, boolean defaultValue)
    {
		if (jo.has(name))
		{
			try
	        {
				return ParseUtil.parseBoolean(jo.getString(name), defaultValue);
	        }
	        catch (JSONException e)
	        {
	            LogUtils.error("JSONException:" + e.toString());
	        }
		}
		
		return defaultValue;
    }
	
	public static double parseDouble(JSONObject jo, String name)
    {
		return parseDouble(jo, name, 0.0);
    }
	
	public static double parseDouble(JSONObject jo, String name, double defaultValue)
    {
		if (jo.has(name))
		{
			try
	        {
				return ParseUtil.parseDouble(jo.getString(name), defaultValue);
	        }
	        catch (JSONException e)
	        {
	            LogUtils.error("JSONException:" + e.toString());
	        }
		}
		
		return defaultValue;
    }
	
}

package com.iptv.common.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iptv.common.utils.JsonUtil;
import com.iptv.common.utils.LogUtils;

import android.text.TextUtils;

/**
 * getLiveChannel
 */
public class LiveChannelListApi
{
	public static EnumType.Platform platform = null;
    public static ArrayList<LiveChannel> getLiveChannel(String liveChannels)
    {
    	JSONObject jo = null;
        try
        {
        	ArrayList<LiveChannel> lstLiveChannel = null;
        	
        	jo = new JSONObject(liveChannels);
        	if (!TextUtils.isEmpty(jo.optString("totalcount")))
    		{
        		int totalcount = JsonUtil.parseInt(jo, "totalcount");
        		if (totalcount == 0)
        			return lstLiveChannel;
        		
        		lstLiveChannel = new ArrayList<LiveChannel>();
        		JSONArray channels = jo.getJSONArray("channels");
	            for (int i = 0; i < channels.length(); i++)
	            {
	            	JSONObject item = channels.getJSONObject(i);
	            	
	            	LiveChannel channel = new LiveChannel();
	            	channel.BeginTime = JsonUtil.parseInt(item, "BeginTime");
	            	
	            		channel.ChannelID = JsonUtil.parseString(item, "ChannelID");
	            	//LogUtils.debug("JSON中platfrom:"+JsonUtil.parseString(item, "Platform"));
	            	
	            	// 补充平台信息
	            	channel.platform= platform;
	            	channel.ChannelLogoURL = JsonUtil.parseString(item, "ChannelLogoURL");
	            	channel.ChannelName = JsonUtil.parseString(item, "ChannelName");
	            	channel.ChannelPurchased = JsonUtil.parseBoolean(item, "ChannelPurchased");
	            	channel.ChannelSDP = JsonUtil.parseString(item, "ChannelSDP");
	            	channel.ChannelType = JsonUtil.parseInt(item, "ChannelType");
	            	channel.ChannelURL = JsonUtil.parseString(item, "ChannelURL");
	            	channel.Interval = JsonUtil.parseInt(item, "Interval");
	            	channel.Lasting = JsonUtil.parseInt(item, "Lasting");
	            	channel.PositionX = JsonUtil.parseInt(item, "PositionX");
	            	channel.PositionY = JsonUtil.parseInt(item, "PositionY");
	            	channel.TimeShift = JsonUtil.parseBoolean(item, "TimeShift");
	            	channel.TimeShiftURL = JsonUtil.parseString(item, "TimeShiftURL");
	            	channel.UserChannelID = JsonUtil.parseInt(item, "UserChannelID");
	            	
	            	
	            	
	            	lstLiveChannel.add(channel);
	            }
    		}
            
            return lstLiveChannel;
        }
        catch (JSONException e)
        {
            LogUtils.error("getLiveChannel exception." + e.toString());
        }
        finally
        {
            if (jo != null)
            {
            	jo = null;
            }
        }
        return null;
    }
    
    public static HashMap<String, LiveChannel> getMapLiveChannel(String liveChannels)
    {
    	HashMap<String, LiveChannel> mapLiveChannel = new HashMap<String, LiveChannel>();
    	
    	JSONObject jo = null;
        try
        {
     
        	jo = new JSONObject(liveChannels);
        	if (!TextUtils.isEmpty(jo.optString("totalcount")))
    		{
        		int totalcount = JsonUtil.parseInt(jo, "totalcount");
        		if (totalcount == 0)
        			return mapLiveChannel;
        		
        		JSONArray channels = jo.getJSONArray("channels");
	            for (int i = 0; i < channels.length(); i++)
	            {
	            	JSONObject item = channels.getJSONObject(i);
	            	LiveChannel channel = new LiveChannel();
	            	channel.BeginTime = JsonUtil.parseInt(item, "BeginTime");
	            	channel.ChannelID = JsonUtil.parseString(item, "ChannelID");
	            	channel.ChannelLogoURL = JsonUtil.parseString(item, "ChannelLogoURL");
	            	channel.ChannelName = JsonUtil.parseString(item, "ChannelName");
	            	channel.ChannelPurchased = JsonUtil.parseBoolean(item, "ChannelPurchased");
	            	channel.ChannelSDP = JsonUtil.parseString(item, "ChannelSDP");
	            	channel.ChannelType = JsonUtil.parseInt(item, "ChannelType");
	            	channel.ChannelURL = JsonUtil.parseString(item, "ChannelURL");
	            	channel.Interval = JsonUtil.parseInt(item, "Interval");
	            	channel.Lasting = JsonUtil.parseInt(item, "Lasting");
	            	channel.PositionX = JsonUtil.parseInt(item, "PositionX");
	            	channel.PositionY = JsonUtil.parseInt(item, "PositionY");
	            	channel.TimeShift = JsonUtil.parseBoolean(item, "TimeShift");
	            	channel.TimeShiftURL = JsonUtil.parseString(item, "TimeShiftURL");
	            	channel.UserChannelID = JsonUtil.parseInt(item, "UserChannelID");
	            	mapLiveChannel.put(channel.ChannelID, channel);
	            }
    		}
            
            return mapLiveChannel;
        }
        catch (JSONException e)
        {
            LogUtils.error("getMapLiveChannel exception." + e.toString());
        }
        finally
        {
            if (jo != null)
            {
            	jo = null;
            }
        }
        return null;
    }

	public EnumType.Platform getPlatform() {
		return platform;
	}

	public void setPlatform(EnumType.Platform platform) {
		this.platform = platform;
	}

}

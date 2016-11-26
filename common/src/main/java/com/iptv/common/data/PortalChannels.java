package com.iptv.common.data;

import java.util.ArrayList;

public class PortalChannels {
	public ArrayList<PortalLiveChannel> lstPortalLiveChannel;
	public ArrayList<PortalLiveType> lstPortalLiveType;
	
	public PortalChannels()
	{
		lstPortalLiveChannel = new ArrayList<PortalLiveChannel>();
		lstPortalLiveType = new ArrayList<PortalLiveType>();
	}
}

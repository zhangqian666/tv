package com.iptv.common.data;

import java.util.ArrayList;

public class LiveChannelBill extends LiveChannel {
	public ArrayList<ProgBill> lstProgBill;
	
	public EnumType.LayoutType layout_type;
	
	public LiveChannelBill()
	{
		lstProgBill = new ArrayList<ProgBill>();
		
		layout_type = EnumType.LayoutType.UNKNOW;
	}
	
	public LiveChannelBill(LiveChannel channel)
	{
		lstProgBill = new ArrayList<ProgBill>();
		layout_type = EnumType.LayoutType.UNKNOW;
		
		this.Channel = channel.Channel;
		this.PositionY = channel.PositionY;
		this.PositionX = channel.PositionX;
		this.Interval = channel.Interval;
		this.ChannelPurchased = channel.ChannelPurchased;
		this.Lasting = channel.Lasting;
		this.ChannelSDP = channel.ChannelSDP;
		this.UserChannelID = channel.UserChannelID;
		this.TimeShift = channel.TimeShift;
		this.ChannelName = channel.ChannelName;
		this.BeginTime = channel.BeginTime;
		this.ChannelURL = channel.ChannelURL;
		this.ChannelLogoURL = channel.ChannelLogoURL;
		this.ChannelID = channel.ChannelID;
		this.TimeShiftURL = channel.TimeShiftURL;
		this.ChannelType = channel.ChannelType;
	}
	
}

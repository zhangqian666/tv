package com.iptv.common.data;

import com.iptv.common.utils.AtvUtils;
import com.iptv.common.utils.LogUtils;

public class LiveChannel {
	/*
 	totalcount 频道数量
	UserChannelID 用户定义频道
	TimeShift 是否有时移
	ChannelName 频道名称		CHANNELNAME
	ChannelURL 播放路径
	ChannelID 频道ID
	TimeShiftURL 时移地址
	ChannelType 频道类型		CHANNELTYPE
	 */
	public String Channel;
	
	public EnumType.Platform platform;
	
	public int PositionY;
	public int PositionX;
	public int Interval;
	public boolean ChannelPurchased;
	public int Lasting;
	public String ChannelSDP;
	public int UserChannelID;
	public boolean TimeShift;
	public String ChannelName;		// CHANNELNAME
	public int BeginTime;
	public String ChannelURL;
	public String ChannelLogoURL;	// PICS_PATH
	public String ChannelID;			// CHANNELID
	//"TimeShiftURL":"rtsp://10.0.3.74/PLTV/88888889/224/3221225824/10000100000000060000000000000402_0.smil?rrsip=10.0.3.72,rrsip=10.0.3.73&icpid=SSPID&accounttype=1&limitflux=-1&limitdur=-1&accountinfo=ncmTjvHn0Gq659YMM0SeY2njRUv6ZtZARGoDJRsj1868u9ncc2SXnuRM5NNpu+AgatytIXZgspYwQr5atHPshxqRtfvCahCf1kf7LTQL34KB99HkgEP2qOMcdOcecnghVdbRRk7caCyBn37Le/WtU4kFTbj4rCw14DRUTelHCiM=:20140327091716,gdtest,172.25.41.243,20140327091716,00000001000000050000000000000055,1FE03EA403BD4806EE8E30C347A5EFCE,-1,0,3,-1,,7,,,,4,END",
	public String TimeShiftURL;
	public int ChannelType;			// CHANNELTYPE
	public int TimeShiftLength;
	
	public LiveChannel() {
		
	}
	
	public LiveChannel(String paramString) {
		this.Channel = paramString;
		this.platform = AtvUtils.GetPlatform(this.Channel, "Platform");
			this.ChannelID = AtvUtils.GetItem(this.Channel, "ChannelID");
		// LogUtils.debug("platform:"+this.platform + ", ChannelID:"+this.ChannelID+"; ChannelIDZTE"+this.ChannelIDZTE); 
		this.ChannelName = AtvUtils.GetItem(this.Channel, "ChannelName");
		this.UserChannelID = AtvUtils.GetItemInt(this.Channel, "UserChannelID");
		this.ChannelURL = AtvUtils.GetItem(this.Channel, "ChannelURL");
		this.TimeShift = AtvUtils.GetItemInt(this.Channel, "TimeShift") == 1 ? true : false;
		this.ChannelSDP = AtvUtils.GetItem(this.Channel, "ChannelSDP");
		this.TimeShiftURL = AtvUtils.GetItem(this.Channel, "TimeShiftURL");
		this.PositionX = AtvUtils.GetItemInt(this.Channel, "PositionX");
		this.PositionY = AtvUtils.GetItemInt(this.Channel, "PositionY");
		this.BeginTime = AtvUtils.GetItemInt(this.Channel, "BeginTime");
		this.Interval = AtvUtils.GetItemInt(this.Channel, "Interval");
		this.Lasting = AtvUtils.GetItemInt(this.Channel, "Lasting");
		this.ChannelType = AtvUtils.GetItemInt(this.Channel, "ChannelType");
		this.ChannelPurchased = AtvUtils.GetItemInt(this.Channel, "ChannelPurchased") == 1 ? true : false;
		this.ChannelLogoURL = AtvUtils.GetItem(this.Channel, "ChannelLogoURL");
		this.TimeShiftLength = AtvUtils.GetItemInt(this.Channel, "TimeShiftLength");
	}
	
}

package com.iptv.common.data;

public class ChannelDetail {
	/*
	 	CHANNELID Integer 频道编号
		CHANNELNAME String 频道名称
		PICS_PATH String 频道图片路径
		ISNVOD Integer 是否是轮播： 	1：支持；0：不支持
		ISPLTV Integer 是否支持时移：	1：支持；0：不支持
		PLTVSTATUS Integer 时移状态：	1：激活；0：去激活
		ISTVOD Integer 是否支持录播：	1：支持；0：不支持
		TVODSTATUS Integer 录播状态：	1：激活；0：去激活
		CHANNELINDEX Integer 频道顺序号
		CHANNELTYPE Integer 频道类型：	1：视频；2：⾳音频；3：页⾯面频道
		ISSUBSCRIBED Integer 是否已订购：	1：订购；0：未订购
		POSTERPATHS HashMap 多海报信息，此字段为HashMap，其中key值为海报位置，为String型，value值为该位置的多张海报，value值类型为String数组类型。数组下标即为该位置海报的顺序。
		DEFINITION Integer 高清标识		1：高清；2：标清
	 */
	public int CHANNELID;
	public String CHANNELNAME;
	public String PICS_PATH;
	public int ISNVOD;
	public int ISPLTV;
	public int PLTVSTATUS;
	public int ISTVOD;
	public int TVODSTATUS;
	public int CHANNELINDEX;
	public int CHANNELTYPE;
	public int ISSUBSCRIBED;
	public int DEFINITION;
	
}

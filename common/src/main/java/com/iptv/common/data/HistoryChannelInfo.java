package com.iptv.common.data;

import java.io.Serializable;

public class HistoryChannelInfo extends VodDetailInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2286399559976279390L;
	
	// sqllite主键ID
	public int id;
	// 频道ID(当为剧集或合集时，此为剧集或合集ID。否则与vid相等)
	public String channelid;
	// 最近观看记录里的sub title
	public String subtitle;
	// 播放时间点
	public long playposition;
	// 创建时间
	public long ctime;
}

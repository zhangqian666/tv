package com.iptv.common.data;

import java.util.ArrayList;


public class LiveType {
	/*
		title string 分类名称		typeName
		bgimg string 背景图片		bgimg
		type_id int 直播分类ID		typeId
	 */
	public int typeId;
	public String typeIdZte;
	public String title;
	public String bg;
	
	public ArrayList<LiveChannel> lstLiveChannel;
//	public ArrayList<Integer> lstChannelIds;
	public ArrayList<String> lstChannelIds;
	
	public LiveType()
	{
		lstLiveChannel = new ArrayList<LiveChannel>();
//		lstChannelIds = new ArrayList<Integer>();
		lstChannelIds=new ArrayList<String>();
	}
	
}

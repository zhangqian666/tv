package com.iptv.common.data;

import java.util.ArrayList;


/**
 * 点播一级分类
 */
public class VodTop {
	/*
	 	type_id String vod分类的ID 类似：10000100000000090000000000002306
		bgimg String 背景图片
		title String 名称
	 */
	public int id;
	public int seq;
	public EnumType.ContentType content_type;
	public EnumType.SubContentType subContentType;
	public EnumType.LayoutType layout_type;
	public String title;
	public String type_id;
	public String icon;
	public String bg;
	public String color;
	public EnumType.BackGroundType bgType;
	public ArrayList<String> imgs;
	
}

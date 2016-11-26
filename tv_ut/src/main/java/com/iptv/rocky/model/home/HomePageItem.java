package com.iptv.rocky.model.home;

import java.util.ArrayList;

import com.iptv.common.data.EnumType;

public class HomePageItem {
    public int id;
    public String contentid;
    public EnumType.SubContentType subcontenttype;
	public EnumType.ContentType contenttype;
	public EnumType.LayoutType layouttype;
	public EnumType.ItemType type;
	public EnumType.MyHotelSubContentType myHotelSubContentType;
	public EnumType.Platform platform;
	public double widthSpan;
	public double heightSpan;
	public String title;   //标题
	public String icon;
	public String background;
	public EnumType.BackGroundType backgroundType;
	public ArrayList<String> images;
	public String columnCode;
	public String contentCode;
	
}

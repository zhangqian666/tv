package com.iptv.rocky.model.vodmovielist;

import java.util.ArrayList;

import com.iptv.common.data.EnumType;

public class VodMovieListPageItem {
    public int id;
    public String contentid;
	public EnumType.ContentType contenttype;
	public EnumType.LayoutType layouttype;
	public EnumType.ItemType type;
	public double widthSpan;
	public double heightSpan;
	public String title;   	//标题
	public String bill;   	//节目单
	public String icon;
	public String background;
	public ArrayList<String> images;
	
}

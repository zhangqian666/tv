package com.iptv.rocky.model.vodsec;

import java.util.ArrayList;

import com.iptv.rocky.model.home.HomePageItem;

public class VodSecLayoutItem {
	
	public ArrayList<HomePageItem> items;
	
	public String title;
    public String id;
	
	public VodSecLayoutItem()
	{
		items = new ArrayList<HomePageItem>();
	}
}

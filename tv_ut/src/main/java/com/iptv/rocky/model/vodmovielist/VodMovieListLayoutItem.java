package com.iptv.rocky.model.vodmovielist;

import java.util.ArrayList;

public class VodMovieListLayoutItem {
	
	public ArrayList<VodMovieListPageItem> items;
	
	public String title;
    public String id;
	
	public VodMovieListLayoutItem()
	{
		items = new ArrayList<VodMovieListPageItem>();
	}
}

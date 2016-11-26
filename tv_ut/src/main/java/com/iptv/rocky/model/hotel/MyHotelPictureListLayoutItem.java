package com.iptv.rocky.model.hotel;

import java.util.ArrayList;

public class MyHotelPictureListLayoutItem {
	
	public ArrayList<MyHotelPictureListPageItem> items;
	
	public String title;
    public String id;
	
	public MyHotelPictureListLayoutItem()
	{
		items = new ArrayList<MyHotelPictureListPageItem>();
	}
}

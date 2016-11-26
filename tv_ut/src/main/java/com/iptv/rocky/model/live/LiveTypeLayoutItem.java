package com.iptv.rocky.model.live;

import java.util.ArrayList;

import com.iptv.rocky.base.BaseTabItemData;

public class LiveTypeLayoutItem extends BaseTabItemData {
	
	public ArrayList<LiveTypePageItem> items;
	
	public LiveTypeLayoutItem()
	{
		items = new ArrayList<LiveTypePageItem>();
	}
}

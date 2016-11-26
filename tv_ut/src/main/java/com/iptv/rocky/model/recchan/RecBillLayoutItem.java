package com.iptv.rocky.model.recchan;

import java.util.ArrayList;

import com.iptv.rocky.base.BaseTabItemData;

public class RecBillLayoutItem extends BaseTabItemData {
	
	public ArrayList<RecBillPageItem> items;
	
	public RecBillLayoutItem()
	{
		super();
		
		items = new ArrayList<RecBillPageItem>();
	}
	
	public RecBillLayoutItem(String title)
	{
		super(title);
		
		items = new ArrayList<RecBillPageItem>();
	}
}

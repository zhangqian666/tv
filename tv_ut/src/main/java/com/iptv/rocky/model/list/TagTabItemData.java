package com.iptv.rocky.model.list;

import com.iptv.rocky.base.BaseTabItemData;

public class TagTabItemData extends BaseTabItemData {
	
	public TagDimension dimension;
	
	public String selectedTitle = "";
	
	public TagTabItemData(String title, TagDimension dimension) {
		this.title = title;
		this.dimension = dimension;
	}
	
}

package com.iptv.rocky.model.vodsec;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodSec;
import com.iptv.rocky.model.home.HomeItemData;
import com.iptv.rocky.model.home.HomePageItem;
import com.iptv.rocky.model.home.HomeVodSecCategoryData;

public class VodSecLayoutFactory {

	/**
	 * 本地数据
	*/
	public static HomeItemData createLiveData(HomePageItem page) {
		if (page.type.equals(EnumType.ItemType.COMMON)) {
			return initData(new HomeVodSecCategoryData(), page);
//		} else if (page.type.equals(EnumType.ItemType.BACKIMAGE)) {
//			return initData(new LiveTypeBackImageData(), page);
		}
		return null;
	}
	
	/**
	 * 直播
	*/
	public static HomeItemData createLiveData(VodSec info) {
		HomeItemData itemData = createItemData(info);
		double height = createItemHeight(info.layout_type);
		double width = createItemWidth(info.layout_type);
		return initData(itemData, info, width, height);
	}
	
	private static HomeItemData createItemData(VodSec info) {
		return new HomeVodSecCategoryData();
	}
	
	private static double createItemWidth(EnumType.LayoutType layout_type) {
		if (layout_type.equals(EnumType.LayoutType.ROW)) {
			return 1.3;
		} else if(layout_type.equals(EnumType.LayoutType.COL)) {
			return 2.0;
		} else if(layout_type.equals(EnumType.LayoutType.NORMAL)) {
			return 1.0;
		} else if(layout_type.equals(EnumType.LayoutType.UNKNOW)) {
			return 1.0;
		}
		return 1.0;
	}
	
	private static double createItemHeight(EnumType.LayoutType layout_type) {
		if (layout_type.equals(EnumType.LayoutType.ROW)) {
			return 2.0;
		} else if(layout_type.equals(EnumType.LayoutType.COL)) {
			return 1.0;
		} else if(layout_type.equals(EnumType.LayoutType.NORMAL)) {
			return 1.0;
		} else if(layout_type.equals(EnumType.LayoutType.UNKNOW)) {
			return 1.0;
		}
		return 1.0;
	}
	
	private static HomeItemData initData(HomeItemData data, HomePageItem page) {
        data.id = page.id;
        data.pageItem = page;
        data.widthSpan = page.widthSpan;
        data.heightSpan = page.heightSpan;
        data.initSpecialData();
        return data;
    }
	
    private static HomeItemData initData(HomeItemData data, VodSec info, double widthSpan, double heightSpan) {
        data.id = info.id;
        data.pageItem = new HomePageItem();
        data.pageItem.id = info.id;
        data.pageItem.contentid = info.type_id;
        data.pageItem.contenttype = info.content_type;
        data.pageItem.layouttype = info.layout_type;
        data.pageItem.subcontenttype = info.sub_content_type;
        data.pageItem.background = info.bg;
        data.pageItem.title = info.title;
        data.pageItem.heightSpan = heightSpan;
        data.pageItem.widthSpan = widthSpan;
        data.widthSpan = widthSpan;
        data.heightSpan = heightSpan;
        data.initSpecialData();
        return data;
    }

}

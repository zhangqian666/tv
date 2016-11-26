package com.iptv.rocky.model.hotel;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.MyHotelPicture;

public class MyHotelPictureListLayoutFactory {
	
	/**
	 * 本地数据
	*/
	public static MyHotelPictureListItemData createMyHotelPictureData(MyHotelPictureListPageItem page) {
		if (page.type.equals(EnumType.ItemType.COMMON)) {
			return initData(new MyHotelPictureListCommonData(), page);
//		} else if (page.type.equals(EnumType.ItemType.BACKIMAGE)) {
//			return initData(new LiveTypeBackImageData(), page);
		}
		return null;
	}
	
	public static MyHotelPictureListItemData createMyHotelPictureData(MyHotelPicture info) {
		MyHotelPictureListItemData itemData = createItemData(info);
		double height = createItemHeight(EnumType.LayoutType.UNKNOW);
		double width = createItemWidth(EnumType.LayoutType.UNKNOW);
		return initData(itemData, info, width, height);
	}
	
	private static MyHotelPictureListItemData createItemData(MyHotelPicture info) {
		return new MyHotelPictureListCommonData();
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
	
	private static MyHotelPictureListItemData initData(MyHotelPictureListItemData data, MyHotelPictureListPageItem page) {
        data.id = page.id;
        data.pageItem = page;
        data.widthSpan = page.widthSpan;
        data.heightSpan = page.heightSpan;
        return data;
    }
	
    private static MyHotelPictureListItemData initData(MyHotelPictureListItemData data, MyHotelPicture info, double widthSpan, double heightSpan) {
        //data.id = info.VODID;
        data.pageItem = new MyHotelPictureListPageItem();
        //data.pageItem.id = info.VODID;
        //data.pageItem.contentid = info.VODID + "";
        //data.pageItem.contenttype = info.content_type;
        //data.pageItem.layouttype = info.layout_type;
        data.pageItem.background = info.picPath;
        data.pageItem.title = info.title;
        data.pageItem.heightSpan = heightSpan;
        data.pageItem.widthSpan = widthSpan;
        data.widthSpan = widthSpan;
        data.heightSpan = heightSpan;
        data.initSpecialData(info);
        return data;
    }

}

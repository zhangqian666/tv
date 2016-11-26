package com.iptv.rocky.model.vodmovielist;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.CommonUtils;

public class VodMovieListLayoutFactory {
	
	/**
	 * 本地数据
	*/
	public static VodMovieListItemData createVodData(VodMovieListPageItem page) {
		if (page.type.equals(EnumType.ItemType.COMMON)) {
			return initData(new VodMovieListCommonData(), page);
//		} else if (page.type.equals(EnumType.ItemType.BACKIMAGE)) {
//			return initData(new LiveTypeBackImageData(), page);
		}
		return null;
	}
	
	public static VodMovieListItemData createVodData(VodChannel info) {
		VodMovieListItemData itemData = createItemData(info);
		double height = createItemHeight(EnumType.LayoutType.UNKNOW);
		double width = createItemWidth(EnumType.LayoutType.UNKNOW);
		return initData(itemData, info, width, height);
	}
	
	private static VodMovieListItemData createItemData(VodChannel info) {
		return new VodMovieListCommonData();
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
	
	private static VodMovieListItemData initData(VodMovieListItemData data, VodMovieListPageItem page) {
        data.id = page.id;
        data.pageItem = page;
        data.widthSpan = page.widthSpan;
        data.heightSpan = page.heightSpan;
        return data;
    }
	
    private static VodMovieListItemData initData(VodMovieListItemData data, VodChannel info, double widthSpan, double heightSpan) {
        data.id = CommonUtils.parseInt(info.VODID);
        data.pageItem = new VodMovieListPageItem();
        data.pageItem.id =  CommonUtils.parseInt(info.VODID);
        data.pageItem.contentid = info.VODID + "";
        data.pageItem.contenttype = info.content_type;
        //data.pageItem.layouttype = info.layout_type;
        data.pageItem.icon = info.PICPATH;
        data.pageItem.title = info.VODNAME;
        data.pageItem.heightSpan = heightSpan;
        data.pageItem.widthSpan = widthSpan;
        data.widthSpan = widthSpan;
        data.heightSpan = heightSpan;
        data.initSpecialData(info);
        return data;
    }

}

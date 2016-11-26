package com.iptv.rocky.model.recchan;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.RecBill;

public class RecBillLayoutFactory {
	
	/**
	 * 本地数据
	*/
	public static RecBillItemData createBillData(RecBillPageItem page) {
		if (page.type.equals(EnumType.ItemType.COMMON)) {
			return initData(new RecBillCommonData(), page);
//		} else if (page.type.equals(EnumType.ItemType.BACKIMAGE)) {
//			return initData(new LiveTypeBackImageData(), page);
		}
		return null;
	}
	
	public static RecBillItemData createBillData(RecBill info) {
		RecBillItemData itemData = createItemData(info);
		double height = createItemHeight(EnumType.LayoutType.UNKNOW);
		double width = createItemWidth(EnumType.LayoutType.UNKNOW);
		return initData(itemData, info, width, height);
	}
	
	private static RecBillItemData createItemData(RecBill info) {
		return new RecBillCommonData();
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
	
	private static RecBillItemData initData(RecBillItemData data, RecBillPageItem page) {
        data.id = page.id;
        data.pageItem = page;
        data.widthSpan = page.widthSpan;
        data.heightSpan = page.heightSpan;
        return data;
    }
	
    private static RecBillItemData initData(RecBillItemData data, RecBill info, double widthSpan, double heightSpan) {
        data.id = info.programId;
        data.pageItem = new RecBillPageItem();
        data.pageItem.id = info.programId;
        data.pageItem.contentid = info.programId + "";
        //data.pageItem.contenttype = info.content_type;
        //data.pageItem.layouttype = info.layout_type;
        //data.pageItem.icon = info.PICPATH;
        data.pageItem.title = info.beginTime.substring(0, 5) + " " + info.title;
        data.pageItem.heightSpan = heightSpan;
        data.pageItem.widthSpan = widthSpan;
        data.widthSpan = widthSpan;
        data.heightSpan = heightSpan;
        data.initSpecialData(info);
        return data;
    }

}

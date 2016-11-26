package com.iptv.rocky.model.live;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.data.ProgBill;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.utils.TimeUtil;

public class LiveTypeLayoutFactory {
	
	/**
	 * 本地数据
	*/
	public static LiveTypeItemData createLiveData(LiveTypePageItem page) {
		if (page.type.equals(EnumType.ItemType.COMMON)) {
			return initData(new LiveTypeCommonData(), page);
//		} else if (page.type.equals(EnumType.ItemType.BACKIMAGE)) {
//			return initData(new LiveTypeBackImageData(), page);
		}
		return null;
	}
	
	/**
	 * 直播
	*/
	public static LiveTypeItemData createLiveData(LiveChannelBill info) {
		LiveTypeItemData itemData = createItemData(info);
		double height = createItemHeight(info.layout_type);
		double width = createItemWidth(info.layout_type);
		return initData(itemData, info, width, height);
	}
	
	private static LiveTypeItemData createItemData(LiveChannelBill info) {
		return new LiveTypeCommonData();
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
	
	private static LiveTypeItemData initData(LiveTypeItemData data, LiveTypePageItem page) {
        data.id = page.id;
        data.pageItem = page;
        data.widthSpan = page.widthSpan;
        data.heightSpan = page.heightSpan;
        data.initSpecialData();
        return data;
    }
	
    private static LiveTypeItemData initData(LiveTypeItemData data, LiveChannelBill info, double widthSpan, double heightSpan) {
        data.id = info.UserChannelID;
        data.pageItem = new LiveTypePageItem();
        data.pageItem.id = info.UserChannelID;
        
        // 此处需要判断使用哪个，userchannelId或channelId;
        //data.pageItem.contentid = info.UserChannelID + "";
        data.pageItem.contentid = info.ChannelID;
        
//        data.pageItem.contenttype = info.content_type;
        data.pageItem.layouttype = info.layout_type;
//        LogUtils.info("----- data.pageItem.icon = info.ChannelLogoUR");
        data.pageItem.icon = info.ChannelLogoURL;
        data.pageItem.title = info.ChannelName;
        if (info.lstProgBill != null && info.lstProgBill.size() > 0)
        {
        	int curSeconds = TimeUtil.getSeconds(TimeUtil.getTimeShort());
        	for (ProgBill bill : info.lstProgBill)
        	{
        		if (curSeconds < TimeUtil.getSeconds(bill.beginTime))
        		{
        			data.pageItem.bill = bill.beginTime.substring(0, 5) + " " + bill.title;
        			break;
        		}
        	}
        }
        data.pageItem.heightSpan = heightSpan;
        data.pageItem.widthSpan = widthSpan;
        data.widthSpan = widthSpan;
        data.heightSpan = heightSpan;
        data.initSpecialData();
        return data;
    }
    
}

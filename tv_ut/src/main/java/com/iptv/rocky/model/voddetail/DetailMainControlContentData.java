package com.iptv.rocky.model.voddetail;

import com.iptv.common.data.VodDetailInfo;

public class DetailMainControlContentData {
	public int backgroundResId;
	public int imageResId;
	public String text;
	public float textSize;
	public boolean isMarquee;
	public Type type;  //详情页主控制内容的类型
	public SelectType selectType;
	public VodDetailInfo vodDetailObj;
	
	public enum Type {
		PLAY,
		SELECTOR,	// 选集
		CHASE,		// 追剧
		STORE,		// 收藏
		TRY, 		// 试看
		BUY;		// 购买
	}
	
	public enum SelectType {
		// 数字选集
		NUMBER,
		// gallery选集
		NUMBER_NO;
	}
}

package com.iptv.common.data;


public class PortalLiveType extends LiveType {
	/*
	 	id int 推荐位编号
		layout_type int 位置类型：
		content_type int 当推荐位为内容时	0：VOD；1：电视剧；2：频道；
		category_type int 	0：VOD；1：电视剧；2：专题；3：直播分类；4：直播频道；5：回看；6：收视记录；
	 */
	
	public int id;
	public EnumType.ContentType content_type;
	public EnumType.LayoutType layout_type;
	public EnumType.BackGroundType backGroundType;
	public String icon;
	public int seq;
	
}

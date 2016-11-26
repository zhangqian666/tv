package com.iptv.common.data;

import java.util.ArrayList;

public class PortalHome {
	public int id;
	public String baseId;
	public String content_id;
	public String parentId;//父级ID；栏目id
	public String programId;//节目id
	public EnumType.ContentType content_type;
	public EnumType.SubContentType subContentType;
	public EnumType.LayoutType layout_type;
	public EnumType.BackGroundType bgType;
	public String bg;
	public EnumType.Platform platform;
	public int color;
	public String title;
	public String icon;
	public int seq;
	public ArrayList<String> imgs;

}

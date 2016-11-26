package com.iptv.common.data;

public enum StbType {
	IPTV_STB("IPTV用机顶盒"), 
	DASHBOARD_STB("公告信息用机顶盒"),
	RESTRANT_STB("餐厅用机顶盒");
	
	private final String type;

	private StbType(String type){
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public static StbType createType(String type) {
		switch (type) {
		case "IPTV_STB":
			return StbType.IPTV_STB;
		case "DASHBOARD_STB":
			return StbType.DASHBOARD_STB;
		default:
			return StbType.IPTV_STB;
		}
	}
}

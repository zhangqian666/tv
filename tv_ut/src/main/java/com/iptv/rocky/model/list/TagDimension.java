package com.iptv.rocky.model.list;

public enum TagDimension {
	CATALOG,
	AREA,
	YEAR,
	UNKNOW;
	
	public String toString() {
		switch(this) {
		case CATALOG:
			return "catalog";
		case AREA:
			return "area";
		case YEAR:
			return "year";
		default:
			return "unknow";
		}
	}
}

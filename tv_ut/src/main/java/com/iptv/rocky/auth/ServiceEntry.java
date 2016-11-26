package com.iptv.rocky.auth;

import com.iptv.common.utils.AtvUtils;

public class ServiceEntry {
	public String ServiceEntry;

	public String URL;
	public int HotKey;
	public String Desc;

	public ServiceEntry(String paramString) {
		this.ServiceEntry = paramString;
		this.URL = AtvUtils.GetItem(paramString, "URL");
		this.HotKey = AtvUtils.GetItemInt(paramString, "HotKey");
		this.Desc = AtvUtils.GetItem(paramString, "Desc");
	}
}

package com.iptv.rocky.model.home;

/**
 * 获取系统信息
 *
 */
public class SystemInfo {

	private String stbId;
	private String mac;
	private String androidVersion;
	private String hardVersion;
	private String hardInfo;
	
	
	public String getStbId() {
		return stbId;
	}
	public void setStbId(String stbId) {
		this.stbId = stbId;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getAndroidVersion() {
		return androidVersion;
	}
	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}
	public String getHardVersion() {
		return hardVersion;
	}
	public void setHardVersion(String hardVersion) {
		this.hardVersion = hardVersion;
	}
	public String getHardInfo() {
		return hardInfo;
	}
	public void setHardInfo(String hardInfo) {
		this.hardInfo = hardInfo;
	}
	
	
}

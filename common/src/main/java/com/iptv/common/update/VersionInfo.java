package com.iptv.common.update;

import java.io.IOException;

import android.util.JsonReader;
import android.util.JsonToken;

public class VersionInfo {
	private boolean isupdate;
	private String update_log;
	private String update_url;
	private String version_name;
	private int version_code;
	private String size;
	private int mode;
	
	public boolean isUpdate() {
		return isupdate;
	}
	public void setIsUpdate(boolean isupdate) {
		this.isupdate = isupdate;
	}
	public String getUpdateLog() {
		return update_log;
	}
	public void setUpdateLog(String update_log) {
		this.update_log = update_log;
	}
	public String getUpdateUrl() {
		return update_url;
	}
	public void setUpdateUrl(String update_url) {
		this.update_url = update_url;
	}
	public String getVersionName() {
		return version_name;
	}
	public void setVersionName(String version_name) {
		this.version_name = version_name;
	}
	public int getVersionCode() {
		return version_code;
	}
	public void setVersionCode(int version_code) {
		this.version_code = version_code;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	// json 格式
	public static VersionInfo build(JsonReader reader) throws IOException {
		if (reader.peek().equals(JsonToken.NULL)) {
			reader.nextNull();
			return null;
		}
		VersionInfo versionInfo = new VersionInfo();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if ("isupdate".equals(name)) {
				versionInfo.setIsUpdate(reader.nextBoolean());
			} else if ("update_log".equals(name)) {
				versionInfo.setUpdateLog(reader.nextString());
			} else if ("update_url".equals(name)) {
				versionInfo.setUpdateUrl(reader.nextString());
			} else if ("version_name".equals(name)) {
				versionInfo.setVersionName(reader.nextString());
			} else if ("version_code".equals(name)) {
				versionInfo.setVersionCode(reader.nextInt());
			} else if ("size".equals(name)) {
				versionInfo.setSize(reader.nextString());
			} else if ("mode".equals(name)) {
				versionInfo.setMode(reader.nextInt());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return versionInfo;
	}
	
}

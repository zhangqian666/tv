package com.iptv.rocky.view.update;

import android.content.Context;
import android.os.Build;

import com.iptv.rocky.model.TvApplication;

public class DeviceInfo {
	private String deviceId;
	private String deviceType;
	private String osv;
	private String sv;
	private int versionCode;
	
	public DeviceInfo(Context context) {
		this.deviceId = TvApplication.mDeviceID;
		this.deviceType = Build.MANUFACTURER + "|" + Build.MODEL + "|" + Build.DEVICE;
		this.osv = Build.VERSION.RELEASE;
		this.sv = TvApplication.mAppVersionName;
		this.versionCode = TvApplication.mAppVersionCode;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public String getOsv() {
		return osv;
	}

	public String getSv() {
		return sv;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

}

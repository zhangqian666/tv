package com.iptv.rocky;

import com.iptv.common.data.EnumType.StbOrientation;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.SystemInfo;
import com.skyworth.sys.param.SkParam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.SystemProperties;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

/**
 * 创维机顶盒部分操作
 * 
 *
 */
public class SkyworthOptions {

	public SkyworthOptions(){
	}
	
	public SkyworthOptions(WindowManager windowManager, Context context){
		this.windowManager = windowManager;
		this.mContext = context;
	}
	
	private Context mContext;
	private WindowManager windowManager;
	
	public void downloadBootVideo(){
		
	}
	
	
	/**
	 * 获取系统信息
	 * 2016-08-14
	 */
	public SystemInfo getSystemInfos(){
		
		String stbId = SystemProperties.get("ro.serialno");
		String mac = SystemProperties.get("ro.mac");
		
		String androidVersion = SystemProperties.get("ro.build.version.release");
		Log.i("安卓版本信息:", androidVersion);
		
		String hardVersion = SystemProperties.get("ro.build.hard");
		Log.i("硬件版本信息:", hardVersion);
		
		String hardInfo = SystemProperties.get("ro.build.hard");
		Log.i("硬件信息:", hardInfo);
		
		SystemInfo info = new SystemInfo();
		info.setStbId(stbId);
		info.setMac(mac);
		info.setAndroidVersion(androidVersion);
		info.setHardVersion(hardVersion);
		info.setHardInfo(hardInfo);
		return info;
	}
	
	
	/**
	 * 修改机顶盒的旋转方向。
	 * @param stbOrientation
	 */
	public void changeRotation(Activity mContext, StbOrientation stbOrientation){
		// 开始处理机顶盒旋转角度
		LogUtils.debug("应该设置的角度："+stbOrientation);
		Display display = windowManager.getDefaultDisplay();
		TvApplication.rotation =display.getRotation();
		switch (stbOrientation) {
		case Horizonal:
			if(TvApplication.rotation != Surface.ROTATION_0){
				mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 		LogUtils.debug("设置为横向0度");
		 		onReboot();
			}else{
				Log.d(this.toString(), "已经是0度，不重启");
			}
			break;
		case Virtical270:
			LogUtils.debug("当前角度:"+TvApplication.rotation);
			if(TvApplication.rotation != Surface.ROTATION_270){
				mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
			}else{
				Log.d(this.toString(), "已经是270度，不重启");
			}
			break;
		case Virtical90:
			if(TvApplication.rotation != Surface.ROTATION_90){
				mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}else{
				Log.d(this.toString(), "已经是90度，不重启");
			}
			break;
		default:
			break;
		}
		// 结束处理机顶盒旋转角度
	}
	
	
	/**
	 * 修改Ntp服务器信息
	 */
	public void changNtpServer(){
		if(TvApplication.mChannel.equals("SKYWORTH")){
			if(!SkParam.getParam("skyworth.params.sys.ntp_server").equals("10.0.3.104")){
				LogUtils.debug("NTP地址尚未设置");
				SkParam.setParam("skyworth.params.sys.ntp_server", "10.0.3.104");
				SkParam.setParam("skyworth.params.sys.ntp_server2", "10.0.3.105");
			}else{
				LogUtils.debug("NTP地址已经设置");
			}
		}else{
			
		}
	}
	
	
	/**
	 * 强制重启机顶盒
	 * 
	 */
	public void onReboot()
	  {
	    Intent reboot = new Intent(Intent.ACTION_REBOOT);
	    reboot.putExtra("nowait", 1);
	    reboot.putExtra("interval", 1);
	    reboot.putExtra("window", 0);
	    mContext.sendBroadcast(reboot);
	}

	public WindowManager getWindowManager() {
		return windowManager;
	}

	public void setWindowManager(WindowManager windowManager) {
		this.windowManager = windowManager;
	}
}

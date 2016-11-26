package com.iptv.rocky;

import android.os.SystemProperties;
import android.view.Display;
import android.view.Surface;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;

/**
 * 修改屏幕的旋转方向
 * @author Xiaoqiang
 *
 */
public class ScreenOrientation {

	/**
	 * 修改 
	 * @param rotation
	 */
	public void change(int rotation){
		
		switch(rotation){
		case 0:
			 SystemProperties.set("persist.sys.screenorientation", "landscape");
			 SystemProperties.set("persist.sys.isrotation.portrait", "270");
			break;
		case 1:	
			 SystemProperties.set("persist.sys.screenorientation", "landscape");
			 SystemProperties.set("persist.sys.isrotation.portrait", "270");
			break;
		case 2:
			 SystemProperties.set("persist.sys.screenorientation", "landscape");
			 SystemProperties.set("persist.sys.isrotation.portrait", "270");
			break;
		case 3:
			 SystemProperties.set("persist.sys.screenorientation", "landscape");
			 SystemProperties.set("persist.sys.isrotation.portrait", "270");
			break;
		default:
 			break;
		}
	}
}

package com.iptv.common.api;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CommonPlayer {

	private static final String TAG = "CommonPlayer";
	private static String playUrl = null;		// 播放
	private static String timeShifted = null;	// 保存直播的时移地址
	private static String maker ="FIBERHOME";
	
	static {
		Log.d(TAG, "loadLibaray--CommonPlayer_jni");
		System.loadLibrary("CommonPlayer_jni");
	}

	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			iCallback.Notify(msg.what, msg.arg1, msg.arg2, (String) msg.obj);
		}
	};

	private Context mContext;

	public CommonPlayer(Context ctx) {
		mContext = ctx;
	}

	/**
	 * 底层消息发送至上层，实现notify完成消息处理
	 *
	 * @param what
	 * @param arg1
	 * @param arg2
	 */
	public static void postEventFromNative(int what, int arg1, int arg2,
			String str) {
		Log.d(TAG, "what = " + what);
		// iCallback.Notify(what, arg1, arg2, str);
		Message message = handler.obtainMessage(what, arg1, arg2, str);
		handler.sendMessage(message);
	}

	/**
	 * 
	 * @param url
	 *            设置播放地址
	 * @param f
	 *            表示播放地址是直播还是点播 0:直播 1:点播 2:播放时移地址
	 */
	public void SetDataSource(String url, int f) {
		Log.d(TAG, "url = " + url + " ,f---value:" + f);
		if (f == 0) {
			String[] urls = url.split(";");
			if (urls.length == 2) {
				playUrl = urls[0];
				timeShifted = urls[1];
			} else {
				playUrl = urls[0];
			}
			if(maker ==null){
				playUrl += "&third_mplay_type=LiveTV";
			}else if(maker.equals("FIBERHOME")){
				
			}
		} else if (f == 1) {
			playUrl = url;
			if(maker ==null){
				playUrl += "&third_mplay_type=VOD";
			}else if(maker.equals("FIBERHOME")){
				
			}
			
		} else if (f == 2) {
			playUrl = url;
			if(maker ==null){
				playUrl += "&third_mplay_type=TSTV";
			}else if(maker.equals("FIBERHOME")){
				
			}
			
		}

		Log.d(TAG, "playUrl = " + playUrl);
		native_SetDataSource(playUrl, f);
		if (f == 0 && timeShifted != null) {
			native_SetTstvUrl(timeShifted);
		}
	}

	public boolean Open() {
		Log.d(TAG, "Open");
		int ret = native_Open();
		if (ret == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 释放资源
	 */
	public void release() {
		Log.d(TAG, "release");
		native_release();
	}

	/**
	 * 开始播放
	 */
	public boolean Start() {
		Log.d(TAG, "Start");
		int ret = native_Start();
		if (ret == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 暂停
	 */
	public void pause() {
		Log.d(TAG, "pause");
		native_pause();
	}

	/**
	 * 停止播放
	 */
	public void Stop() {
		Log.d(TAG, "Stop");
		native_Stop();
	}

	/**
	 * 定位
	 * 
	 * @param timestamp
	 */
	public void Seek(int timestamp) {
		Log.d(TAG, "Seek timestamp = " + timestamp);
		native_Seek(timestamp);
	}

	/**
	 * 直播快退,播放时移地址。 点播，直接调用native_Scale
	 * 
	 * @param speed
	 */
	public void Scale(float speed) {
		Log.d(TAG, "Scale--->in speed value:" + speed);
		// if (timeShifted != null && !timeShifted.equals(" ") && speed < 0) {
		// Log.d(TAG, "timeShifted:" + timeShifted);
		// // Stop();
		// // release();
		// this.Stop();
		// this.clearVideo();
		// this.release();
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// this.setKeepLastFrame(0);
		// this.SetDataSource(timeShifted, 2);
		//
		// this.Open();
		// this.Start();
		//
		// timeShifted = null;
		//
		// Scale(-2);
		// } else {
		native_Scale(speed);
		// }
	}

	/**
	 * 恢复播放
	 */
	public void Resume() {
		Log.d(TAG, "Resume");
		native_Resume();
	}

	/**
	 * 跳到片尾
	 */
	public void gotoEnd() {
		Log.d(TAG, "gotoEnd");
		native_gotoEnd();
	}

	/**
	 * 跳到片头
	 */
	public void gotoStart() {
		Log.d(TAG, "gotoStart");
		native_gotoStart();
	}

	public void setKeepLastFrame(int nKeepLastFrame) {
		Log.d(TAG, "setKeepLastFrame nKeepLastFrame = " + nKeepLastFrame);
		native_setKeepLastFrame(nKeepLastFrame);
	}

	public void SetWindow(int left, int top, int width, int height) {
		Log.d(TAG, "SetWindow left=" + left + ",top = " + top + ", width = "
				+ width + ", height = " + height);
		native_SetWindow(left, top, width, height);
	}

	public void clearVideo() {
		Log.d(TAG, "clearVideo ");
		native_clearVideo();
	}

	/**
	 * 视频时长
	 * 
	 * @return s
	 */
	public int getMediaDuration() {
		int ret = native_getMediaDuration();
		Log.d(TAG, "getMediaDuration ret = " + ret);
		return ret;
	}

	/**
	 * 当前播放时间
	 * 
	 * @return ms
	 */
	public int GetCurrentPlaybackTimePos() {
		int ret = native_GetCurrentPlaybackTimePos();
		Log.d(TAG, "GetCurrentPlaybackTimePos ret = " + ret);
		return ret;
	}

	private static ICallback iCallback;

	public interface ICallback {
		public void Notify(int what, int arg1, int arg2, String str);
	}

	public void RegisterCallBack(ICallback cb) {
		CommonPlayer.iCallback = cb;
	}

	private native void native_SetDataSource(String url, int f);

	private native int native_Open();

	private native void native_release();

	private native int native_Start();

	private native void native_Stop();

	private native void native_Seek(int timestamp);

	private native void native_Scale(float speed);

	private native void native_pause();

	private native void native_Resume();

	private native void native_gotoEnd();

	private native void native_gotoStart();

	private native void native_setKeepLastFrame(int nKeepLastFrame);

	private native void native_SetWindow(int left, int top, int width,
			int height);

	// private native void native_RegisterCallBack(ICallback cb);

	private native void native_clearVideo();

	private native int native_getMediaDuration();

	private native int native_GetCurrentPlaybackTimePos();

	private native void native_SetTstvUrl(String url);

}

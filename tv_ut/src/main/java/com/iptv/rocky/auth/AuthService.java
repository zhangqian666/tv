package com.iptv.rocky.auth;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;

import com.ikantech.support.listener.YiHttpResponseListener;
import com.ikantech.support.net.YiHttpRequest;
import com.ikantech.support.proxy.YiHandlerProxy;
import com.ikantech.support.proxy.YiHandlerProxy.YiHandlerProxiable;
import com.ikantech.support.service.YiLocalService;
import com.ikantech.support.utils.YiLog;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.LiveChannel;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;

public abstract class AuthService extends YiLocalService implements
		YiHttpResponseListener, YiHandlerProxiable {
	public static final String ACTION_AUTH_SUCCEED = "com.xiaoqiang.android.auth.action.AUTH_SUCCEED";
	public static final String ACTION_AUTH_START = "com.xiaoqiang.android.auth.action.AUTH_START";
	public static final String ACTION_AUTH_ERROR = "com.xiaoqiang.android.auth.action.AUTH_ERROR";

	private static final int MSG_RE_AUTH = 0x01;
	private static final int MSG_RE_AUTH_DELAY = 2000;
	public static final int HTTP_TIME_OUT = 60000;
	public static final int ONE_MINUTE = 60000;

	private YiHandlerProxy mHandlerProxy;

	protected boolean mIsAuthed;
	
	// 内网
	
	protected static String mPreAuthUrl =  TvApplication.authUrl; 
	
	protected static String mAuthUrl =  TvApplication.authUrl; 
	protected static String mAuthBaseUrl = TvApplication.mAuthBaseUrl;
	protected static String mUserId = null;
	protected static String mPasswd = null;
	
	//protected static String mSTBID = "001002990050001100500050123456ff";
	//protected static String mSTBID_pre = "00100299005000110050";
	protected static String mSTBID_pre = "00000100000820102PBA";

	protected Map<String, String> mParams;
	protected ArrayList<LiveChannel> mChannels;
	protected ArrayList<ServiceEntry> mServiceEntries;

	protected AuthServiceBinder mServiceBinder;
	
	//add epgserver ip start -- by saint 0509
	protected String mEPGServerIP;
	//add epgserver ip end

	@Override
	public void onCreate() {
		super.onCreate();
		mParams = new HashMap<String, String>();
		mChannels = new ArrayList<LiveChannel>();
		mServiceEntries = new ArrayList<ServiceEntry>();

		mHandlerProxy = new YiHandlerProxy(this, this);

		mIsAuthed = false;

		mServiceBinder = new AuthServiceBinder(this);
		
		//add epgserver ip start -- by saint 0509
		mEPGServerIP = null;
		//end

		YiLog.getInstance().i("AuthService onCreate");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mServiceBinder;
	}

	public abstract void reAuth();

	public abstract void startAuth();
	
	public abstract void startNormalPlatformAuth();

	public abstract void startAuth(long delay);

	public abstract void reHeartBeat();

	public abstract void startHeartBeat(boolean immediately);

	protected void broadcast(String action, Map<String, String> params) {
		if (ACTION_AUTH_SUCCEED.equals(action)) {
			mIsAuthed = true;
		}
		Intent intent = new Intent(action);
		if (params != null) {
			Set<String> keys = params.keySet();
			for (String string : keys) {
				intent.putExtra(string, params.get(string));
			}
		}
		sendBroadcast(intent);
	}

	protected String getEPGSessionId() {
		return mParams.get("JSESSIONID");
	}
	
	protected String getEPGADDR() {
		return mParams.get("EPGADDR");
	}
	
	protected String getEPGServerIP() {
		//return mParams.get("EPGServerIP");
		//add epgserver ip start -- by saint 0509
		if (mEPGServerIP == null) {
			String epgdomain = mParams.get("EPGDomain");
			//add you code here
			// mEPGServerIP = you code
			if (!TextUtils.isEmpty(epgdomain)) {
				Pattern pattern = Pattern.compile("(http://[^/]+)");
				Matcher m = pattern.matcher(epgdomain);
				while (m.find()) {
					mEPGServerIP = m.group(1);
				}
			}
		}
		return mEPGServerIP;
	}
	
	protected String getEPGPort() {
		return mParams.get("EPGPort");
	}

	protected String getEPGDomain() {
		return mParams.get("EPGDomain");
	}

	protected String getEPGDomainBackup() {
		return mParams.get("EPGDomainBackup");
	}

	protected String getUserToken() {
		return mParams.get("UserToken");
	}

	protected String getManagementDomain() {
		return mParams.get("ManagementDomain");
	}

	protected String getManagementDomainBackup() {
		return mParams.get("ManagementDomainBackup");
	}

	protected String getUpgradeDomain() {
		return mParams.get("UpgradeDomain");
	}

	protected String getUpgradeDomainBackup() {
		return mParams.get("UpgradeDomainBackup");
	}

	protected String getNTPDomain() {
		return mParams.get("NTPDomain");
	}

	protected String getNTPDomainBackup() {
		return mParams.get("NTPDomainBackup");
	}
	protected String getEPGGroupNMB() {
		return mParams.get("EPGGroupNMB");
	}

	protected String getUserGroupNMB() {
		return mParams.get("UserGroupNMB");
	}

	protected String getChannels() {
		JSONObject jsonObject = new JSONObject();
		try {
			JSONArray array = new JSONArray();
			if (mChannels != null && mChannels.size() > 0) {
				jsonObject.put("totalcount", mChannels.size());
				for (LiveChannel channel : mChannels) {
					JSONObject object = new JSONObject();
					object.put("BeginTime", channel.BeginTime);
					// LogUtils.debug("channelId:"+channel.ChannelID+" ;channelIdZTE:"+channel.ChannelIDZTE);
//					object.put(name, value)
					object.put("ChannelID", channel.ChannelID);
					object.put("ChannelLogoURL", channel.ChannelLogoURL);
					object.put("ChannelName", channel.ChannelName);
					object.put("ChannelPurchased", channel.ChannelPurchased);
					object.put("ChannelSDP", channel.ChannelSDP);
					object.put("ChannelType", channel.ChannelType);
					object.put("ChannelURL", channel.ChannelURL);
					object.put("Interval", channel.Interval);
					object.put("Lasting", channel.Lasting);
					object.put("PositionX", channel.PositionX);
					object.put("PositionY", channel.PositionY);
					object.put("TimeShift", channel.TimeShift);
					object.put("TimeShiftURL", channel.TimeShiftURL);
					object.put("UserChannelID", channel.UserChannelID);
					array.put(object);
				}
				jsonObject.put("channels", array);
			} else {
				jsonObject.put("totalcount", 0);
			}
		} catch (Exception e) {
		}
		return jsonObject.toString();
	}
	
	protected String getServiceEntries() {
		JSONObject jsonObject = new JSONObject();
		try {
			JSONArray array = new JSONArray();
			if (mServiceEntries != null && mServiceEntries.size() > 0) {
				jsonObject.put("totalcount", mServiceEntries.size());
				for (ServiceEntry serviceEntry : mServiceEntries) {
					JSONObject object = new JSONObject();

					object.put("URL", serviceEntry.URL);
					object.put("HotKey", serviceEntry.HotKey);
					object.put("Desc", serviceEntry.Desc);
					
					array.put(object);
				}
				jsonObject.put("service_entries", array);
			} else {
				jsonObject.put("totalcount", 0);
			}
		} catch (Exception e) {
		}
		return jsonObject.toString();
	}

	public static class AuthServiceBinder extends IAuthService.Stub {
		protected WeakReference<AuthService> mServiceHandler;

		public AuthServiceBinder(AuthService s) {
			mServiceHandler = new WeakReference<AuthService>(s);
		}

		public void execute(Runnable runnable) {
			YiLocalService s = mServiceHandler.get();
			if (s != null) {
				s.getExecutor().execute(runnable);
			}
		}

		public void executeDelayed(Runnable runnable, long delayMillis) {
			YiLocalService s = mServiceHandler.get();
			if (s != null) {
				s.getExecutor().execute(runnable, delayMillis);
			}
		}

		public void execute(YiHttpRequest request,
				YiHttpResponseListener listener) {
			YiLog.getInstance().i("http request %s", request.getUrl());
			execute(new HttpRunnable(request, listener));
		}

		public void executeDelayed(YiHttpRequest request,
				YiHttpResponseListener listener, long delayMillis) {
			executeDelayed(new HttpRunnable(request, listener), delayMillis);
		}

		@Override
		public void startAuth() {
			if (mServiceHandler.get() != null) {
				mServiceHandler.get().startAuth();
			}
		}
		
		@Override
		public void startNormalPlatformAuth() {
			if (mServiceHandler.get() != null) {
				mServiceHandler.get().startNormalPlatformAuth();
			}
		}

		@Override
		public String getEPGSessionId() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getEPGSessionId();
			}
			return null;
		}
		
		@Override
		public String getEPGADDR() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getEPGADDR();
			}
			return null;
		}
		
		@Override
		public String getEPGServerIP() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getEPGServerIP();
			}
			return null;
		}
		
		@Override
		public String getEPGPort() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getEPGPort();
			}
			return null;
		}
		
		@Override
		public String getEPGDomain() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getEPGDomain();
			}
			return null;
		}

		@Override
		public String getEPGDomainBackup() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getEPGDomainBackup();
			}
			return null;
		}

		@Override
		public String getUserToken() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getUserToken();
			}
			return null;
		}

		@Override
		public String getManagementDomain() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getManagementDomain();
			}
			return null;
		}

		@Override
		public String getManagementDomainBackup() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getManagementDomainBackup();
			}
			return null;
		}

		@Override
		public String getUpgradeDomain() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getUpgradeDomain();
			}
			return null;
		}

		@Override
		public String getUpgradeDomainBackup() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getUpgradeDomainBackup();
			}
			return null;
		}

		@Override
		public String getNTPDomain() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getNTPDomain();
			}
			return null;
		}

		@Override
		public String getNTPDomainBackup() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getNTPDomainBackup();
			}
			return null;
		}

		@Override
		public String getEPGGroupNMB() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getEPGGroupNMB();
			}
			return null;
		}

		@Override
		public String getUserGroupNMB() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getUserGroupNMB();
			}
			return null;
		}

		@Override
		public String getChannels() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getChannels();
			}
			return null;
		}
		
		@Override
		public String getServiceEntries() throws RemoteException {
			if (mServiceHandler.get() != null) {
				return mServiceHandler.get().getServiceEntries();
			}
			return null;
		}
	}

	protected boolean isNetConnected() {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			YiLog.getInstance().e(e, "check net work failed.");
		}
		return false;
	}

	public static String getAuthUrl() {
		return mAuthUrl;
	}

	public static void setAuthUrl(String mAuthUrl) {
		AuthService.mAuthUrl = mAuthUrl;
	}

	public static String getAuthBaseUrl() {
		return mAuthBaseUrl;
	}

	public static void setAuthBaseUrl(String mAuthBaseUrl) {
		AuthService.mAuthBaseUrl = mAuthBaseUrl;
	}

//	public static String getUserId() {
//		return mUserId;
//	}

	public static void setUserId(String mUserId) {
		AuthService.mUserId = mUserId;
	}

	public static String getPasswd() {
		return mPasswd;
	}

	public static void setPasswd(String mPasswd) {
		AuthService.mPasswd = mPasswd;
	}

	public static String getSTBID() {
		return mSTBID_pre + AuthUtils.getMacAddress().replace(":", "").toLowerCase();
		//return mSTBID_pre + "0050123456ff";
	}

//	public static void setSTBID(String mSTBID) {
//		AuthService.mSTBID_pre = mSTBID;
//	}

	public class NativeNetworkBroadcastReceiver extends BroadcastReceiver {
		State wifiState = null;
		State mobileState = null;

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
				// 获取手机的连接服务管理器，这里是连接管理器类
				ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
				mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

				if (wifiState != null && mobileState != null
						&& State.CONNECTED != wifiState
						&& State.CONNECTED == mobileState) {
					YiLog.getInstance().i("net 手机网络连接成功！");
					getHandler().removeMessages(MSG_RE_AUTH);
					getHandler().sendEmptyMessageDelayed(MSG_RE_AUTH, MSG_RE_AUTH_DELAY);
				} else if (wifiState != null && mobileState != null
						&& State.CONNECTED == wifiState
						&& State.CONNECTED != mobileState) {
					YiLog.getInstance().i("net 无线网络连接成功！");
					getHandler().removeMessages(MSG_RE_AUTH);
					getHandler().sendEmptyMessageDelayed(MSG_RE_AUTH, MSG_RE_AUTH_DELAY);
				} else if (wifiState != null && mobileState != null
						&& State.CONNECTED != wifiState
						&& State.CONNECTED != mobileState) {
					YiLog.getInstance().i("net 手机没有任何网络...");
				}
			}
		}
	}

	@Override
	public void processHandlerMessage(Message msg) {
		switch (msg.what) {
		case MSG_RE_AUTH:
			reAuth();
			break;

		default:
			break;
		}
	}

	@Override
	public Handler getHandler() {
		return mHandlerProxy.getHandler();
	}

	public static String getmPreAuthUrl() {
		return mPreAuthUrl;
	}

	public static void setmPreAuthUrl(String mPreAuthUrl) {
		AuthService.mPreAuthUrl = mPreAuthUrl;
	}
}

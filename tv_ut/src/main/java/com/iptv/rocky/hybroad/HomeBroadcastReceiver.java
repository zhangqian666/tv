package com.iptv.rocky.hybroad;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.net.wifi.WifiManager;
import android.os.SystemProperties;
import android.util.Log;

public class HomeBroadcastReceiver extends BroadcastReceiver {
	
	private ConnectivityManager mConnectivity;
	private static final String TAG = "HomeBroadcastReceiver";
	private Context mContext;
	private EthernetManager mEthManager;
	private WifiManager mWifiManager;
    private ConnectivityManager connMgr;
    private NetworkInfo ethInfo;
    private boolean linkupFlag = false;
    
    private static final String ACTION_RELOGINIPTV ="com.virgintelecom.action.RELOGINIPTV";
    private static final String ACTION_HOME ="com.virgintelecom.action.HOME";
    
    final String SYSTEM_DIALOG_REASON_KEY = "reason";
    final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    
    final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    
	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		int ret = -1;
		int message = -1;
		int mWifiCheck = 0;
		String mNectType = null;
		String mConnectType = null;
        
		String action = intent.getAction();	
		//mNectType = "eth0";//getNetTypeValue("Network.AccessType");//Cable,Wifi
		//mConnectType = "dhcp";//getConnectTypeValue("ConnectType");
	  
		mConnectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
		mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager.isWifiEnabled())
	        mNectType = "Wifi";
	    else
	        mNectType = "eth0";
	        			
		Log.d(TAG, "mExitIptvReceiver Action :" + action);
		Log.d(TAG, "mNectType is = " + mNectType);
		
		if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
			
			LogUtils.error("收到HOME键信息");
			String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
			LogUtils.error("reason "+reason);
			broadcast(ACTION_HOME, null);
			
		}else if ("android.net.ethernet.ETHERNET_STATE_CHANGE".equals(action) || "android.net.pppoe.PPPOE_STATE_CHANGE".equals(action)
				|| "android.net.wifi.WIFI_STATE_CHANGED".equals(action) || "android.net.wifi.STATE_CHANGE".equals(action)) {  
			/*if("Wifi".equals(mNectType)){
				Log.d(TAG, "mNectType is wifi");
				if("android.net.wifi.WIFI_STATE_CHANGED".equals(action)){
					Log.d(TAG, "wifi progress one set...");
					SystemProperties.set("sys.IPTVPROGRESS_FLAG", "1");
					SystemProperties.set("sys.IPTVPROGRESS_STATUS", "true");
				} else if("android.net.wifi.STATE_CHANGE".equals(action)){
					mWifiCheck++;
					NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
					if(info == null){
						Log.d(TAG, "NetworkInfo is null...");
					}
					if (info.getDetailedState() == NetworkInfo.DetailedState.VERIFYING_POOR_LINK || info.getDetailedState() == NetworkInfo.DetailedState.CAPTIVE_PORTAL_CHECK
							|| info.isConnected()){
						Log.d(TAG, "wifi connect is OK");	
						SystemProperties.set("sys.IPTVPROGRESS_FLAG", "2");
						SystemProperties.set("sys.IPTVPROGRESS_STATUS", "true");
					} else {
						Log.d(TAG, "wifi connect is failed");	
						if(mWifiCheck < 2)
							return;
						SystemProperties.set("sys.IPTVPROGRESS_FLAG", "2");
			    		SystemProperties.set("sys.IPTVPROGRESS_STATUS", "false");
					}
				}	else {
					Log.d(TAG, "wifi other message...");
				}						
			} else {
			    mEthManager = (EthernetManager)context.getSystemService(Context.ETHERNET_SERVICE);
			    mConnectType = mEthManager.getEthernetMode();		
			    Log.d(TAG, "mConnectType = " + mConnectType);				    						
				if(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE.equals(mConnectType) && "android.net.pppoe.PPPOE_STATE_CHANGE".equals(action)){
					message = intent.getIntExtra(PppoeManager.EXTRA_PPPOE_STATE, ret);  
				}else {
					message = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, ret); 
				}
				
				Log.d(TAG, "message = " + message);			
				switch (message) {
				case EthernetManager.EVENT_PHY_LINK_UP:
					Log.d(TAG, "EthernetManager.EVENT_PHY_LINK_UP");				
					SystemProperties.set("sys.IPTVPROGRESS_FLAG", "1");
					SystemProperties.set("sys.IPTVPROGRESS_STATUS", "true");
					linkupFlag = true;
					break;
						
				case EthernetManager.EVENT_PHY_LINK_DOWN:
					Log.d(TAG, "wait for 1s start...");
					try {
						Thread.sleep(1000);
					}catch (Exception ex) {
						Log.e(TAG, "Thread sleep error");
					}
					Log.d(TAG, "wait for 1s end...");
					SystemProperties.set("sys.IPTVPROGRESS_FLAG", "1");
					SystemProperties.set("sys.IPTVPROGRESS_STATUS", "false");
					break;
				
				//case IPTVConstant.EVENT_PPPOE_CONNECT_SUCCESSED:
				case EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED:
				case EthernetManager.EVENT_STATIC_CONNECT_SUCCESSED:
				    connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
				    ethInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
					Log.d(TAG, "wait for 2s start...");
					try {
						Thread.sleep(2000);
					}catch (Exception ex) {
						Log.e(TAG, "Thread sleep error");
					}
					Log.d(TAG, "wait for 2s end...");			
					SystemProperties.set("sys.IPTVPROGRESS_FLAG", "2");
					SystemProperties.set("sys.IPTVPROGRESS_STATUS", "true");
					
					LogUtils.debug("TvApplication.doNetState  "+TvApplication.doNetState);
					if(TvApplication.doNetState){
						if(TvApplication.loginIptv){
							if(TvApplication.shouldLoginDate != null){
								LogUtils.error("应该加载时间"+TvApplication.shouldLoginDate.getTime().toString() + "当前时间:"+Calendar.getInstance().getTime().toString());
								if(TvApplication.shouldLoginDate.before(Calendar.getInstance())){
									LogUtils.debug("已经超期，发起重新注册请求");
									//broadcast(ACTION_RELOGINIPTV, null);
								}else{
									LogUtils.debug("尚未超期，暂不处理");
								}
							}else{
								LogUtils.info("时间为空");
							}
						}
					}
    				break;
				
				case PppoeManager.EVENT_CONNECT_FAILED:
				case EthernetManager.EVENT_DHCP_CONNECT_FAILED:
				case EthernetManager.EVENT_STATIC_CONNECT_FAILED:
				    Log.d(TAG, "EVENT_STATIC_CONNECT_FAILED");
					if(EthernetManager.EVENT_STATIC_CONNECT_FAILED == message && "PPPoE".equals(mConnectType))
						return;
					SystemProperties.set("sys.IPTVPROGRESS_FLAG", "2");
					SystemProperties.set("sys.IPTVPROGRESS_STATUS", "false");
					break;
				
				default:
					Log.d(TAG, "Receiver Message  :" + message + " ,No Handler Method.");
					return;
				}
			}*/
		}
	}	
		
	/**
     * 自己增加，主要是解决  mAuthServiceBinder 的绑定，只有绑定成功后，才可以进行认证
     *
     */
	protected void broadcast(String action, Map<String, String> params) {
		Intent intent = new Intent(action);
		if (params != null) {
			Set<String> keys = params.keySet();
			for (String string : keys) {
				intent.putExtra(string, params.get(string));
			}
		}
		this.mContext.sendBroadcast(intent);
	}
}
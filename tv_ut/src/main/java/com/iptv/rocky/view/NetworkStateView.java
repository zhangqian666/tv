package com.iptv.rocky.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;

public class NetworkStateView extends ScaleAsyncImageView {
	
	private Context mContext;
	private boolean mIsWifi;
	//private EthernetManager mEthManager;
	private WifiManager mWifiManager;
	
	private int ret = -1;
	private int message = -1;
	private String mNectType = null;
	private String mConnectType = null;
	
	private BroadcastReceiver broadCast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			setStateTip();
		}
	};
	
	private BroadcastReceiver wifiBroadCast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			setWifiSignalLevel();
		}
	};

	public NetworkStateView(Context context) {
		this(context, null, 0);
	}
	
	public NetworkStateView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public NetworkStateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		this.mContext = context;
		setStateTip();
	}
	
	public void start() {
		try {
			IntentFilter intent = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
			mContext.registerReceiver(broadCast, intent);
			if (mIsWifi) {
				IntentFilter wifiIntent = new IntentFilter(WifiManager.RSSI_CHANGED_ACTION);
				mContext.registerReceiver(wifiBroadCast, wifiIntent);
			}
		} catch (Exception e) {
			LogUtils.e("NetworkStateView", "registerReceiver error.");
		}
	}
	
	public void stop() {
		try {
			mContext.unregisterReceiver(broadCast);
			if (mIsWifi) {
				mContext.unregisterReceiver(wifiBroadCast);
			}
		} catch (Exception e) {
			LogUtils.e("NetworkStateView", "unregisterReceiver error.");
		}
	}
	
	private void setStateTip() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager.isWifiEnabled()){
			mNectType = "Wifi"; 
		}else{ 
			mNectType = "eth0";
		}
		LogUtils.error("无线网络状态 "+mNectType);
        
        
        if (manager != null) {
            //NetworkInfo info = manager.getActiveNetworkInfo();
            //if (info != null) {
                if (mNectType == "eth0") {
                    setImageResource(R.drawable.tv_status_et_connect);
                    return;
                } else if (mNectType == "Wifi") {
                    mIsWifi = true;
                    setWifiSignalLevel();
                    return;
                }
            //}
        }
        setImageResource(R.drawable.tv_status_et_disconnect);
    }

    private void setWifiSignalLevel() {
        int wifiLevel = -1;
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            wifiLevel = WifiManager.calculateSignalLevel(info.getRssi(), 5);
        }
        if (wifiLevel >= 0) {
            setImageResource(mContext.getResources().getIdentifier("tv_status_wifi" + wifiLevel,
                    "drawable", mContext.getPackageName()));
        } else {
            setImageResource(R.drawable.tv_status_et_disconnect);
        }
    }
}

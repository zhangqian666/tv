package com.iptv.rocky.model.voddetail;

import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.data.StoreChannelInfo;
import com.iptv.rocky.hwdata.local.VodHistoryLocalFactory;
import com.iptv.rocky.hwdata.local.VodStoreLocalFactory;

import android.content.Context;


public class DetailLocalHelper {
	private static DetailLocalHelper instance;
	
	private static VodStoreLocalFactory storeFactory;
	private static VodHistoryLocalFactory historyFactory;
	
	private DetailLocalHelper(Context context) {
		if (storeFactory == null) {
			storeFactory = new VodStoreLocalFactory(context);
		}
		if (historyFactory == null) {
			historyFactory = new VodHistoryLocalFactory(context);
		}
	}
	
	public static DetailLocalHelper instance(Context context) {
		if (instance != null) {
			return instance; 
		}
		return new DetailLocalHelper(context);
	}
	
	public boolean isStored(String property, String val) {
		StoreChannelInfo info = storeFactory.findByProperty(property, val);
		return info == null ? false : true;
	}
	
	public void store(StoreChannelInfo storeInfo) {
		storeFactory.saveStore(storeInfo);
	}
	
	public void unStore(String channelId) {
		storeFactory.deleteByChannelId(channelId);
	}
	
	public HistoryChannelInfo getLastPlay(String channelId) {
		HistoryChannelInfo info = historyFactory.getHistoryById(channelId);
		if (info == null || info.playposition == 0) {
			return null;
		}
		return info;
	}

}

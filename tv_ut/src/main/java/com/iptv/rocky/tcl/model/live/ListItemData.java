package com.iptv.rocky.tcl.model.live;

import android.content.Context;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.data.LiveChannelBill;
import com.iptv.rocky.base.BaseListItemData;
import com.iptv.rocky.tcl.view.live.ListItemView;
import com.iptv.rocky.R;

public class ListItemData extends BaseListItemData {
	
	public LiveChannelBill mLiveChannelBill;
	private ListItemView mContentView;
	//是否是正在播放
	private boolean playing;
	
/*	public void initView(LiveChannelBill info) {
		Log.d("ListItemData","initView");
		mLiveChannelBill = info;
		mContentView.initView(mLiveChannelBill);
	}*/
	
	public void initView(LiveChannelBill info,boolean playing) {
		mLiveChannelBill = info;
		this.playing = playing;
		mContentView.initView(mLiveChannelBill,playing);
	}
	
	public void setViewEnlarge() {
		mContentView.setViewEnlarge();
	}
	
	public void setViewNormal() {
		mContentView.setViewNormal();
	}
	
	public void setViewNotFocus() {
		mContentView.setViewNotFocus();
	}
	
	public void destroy() {
		mContentView.destroy();
	}
	
	@Override
	public View getView(Context context) {
		//Log.d("ListItemData","GetView");
		if (mContentView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = (ListItemView)inflater.inflate(R.layout.live_listitem, null);
		}
		return mContentView;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
		mContentView.playing = playing;
	}

}

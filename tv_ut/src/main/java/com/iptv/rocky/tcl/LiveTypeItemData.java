package com.iptv.rocky.tcl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.data.PortalLiveType;
import com.iptv.rocky.base.BaseListItemData;
import com.iptv.rocky.R;

public class LiveTypeItemData extends BaseListItemData {
	
	public PortalLiveType mPortalLiveType;

	private LiveTypeItemView mContentView;
	
	public void initView(PortalLiveType info) {
		mPortalLiveType = info;
		mContentView.initView(info);
	}
	
	public void setViewEnlarge() {
		mContentView.setViewEnlarge();
	}
	
	public void setViewNormal() {
		mContentView.setViewNormal();
	}
	
	public void destroy() {
		mContentView.destroy();
	}
	
	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = (LiveTypeItemView)inflater.inflate(R.layout.livetype_listitem, null);
		}
		return mContentView;
	}

}

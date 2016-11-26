package com.iptv.rocky.model.recchan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.data.RecChan;
import com.iptv.rocky.base.BaseListItemData;
import com.iptv.rocky.view.recchan.ListItemView;
import com.iptv.rocky.R;

public class ListItemData extends BaseListItemData {
	
	public RecChan mRecChan;

	private ListItemView mContentView;
	
	public void initView(RecChan info) {
		mRecChan = info;
		mContentView.initView(mRecChan);
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
		if (mContentView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = (ListItemView)inflater.inflate(R.layout.recchan_listitem, null);
		}
		return mContentView;
	}

}

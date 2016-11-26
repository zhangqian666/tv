package com.iptv.rocky.model.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.view.home.HomeAnimView;
import com.iptv.rocky.R;

public class HomeAnimData extends HomeItemData {

	private HomeAnimView mContentView;
	
	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = (HomeAnimView)inflater.inflate(R.layout.home_page_item_anim, null);
			mContentView.initView(this);
		}
		return mContentView;
	}
	
	@Override
	public void onOwnerFocusChange(boolean hasFocus) {
		mContentView.onOwnerFocusChange(hasFocus);
	}
}

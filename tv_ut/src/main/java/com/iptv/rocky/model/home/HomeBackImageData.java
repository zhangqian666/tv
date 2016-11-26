package com.iptv.rocky.model.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.view.home.HomeBackImageView;
import com.iptv.rocky.R;

public class HomeBackImageData extends HomeItemData {

	private HomeBackImageView mContentView;
	
	@Override
	public View getView(Context context) {
		HomeBackImageView view = mContentView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			view = (HomeBackImageView)inflater.inflate(R.layout.home_page_item_backimage, null);
			view.initView(this);
			mContentView = view;
		}
		return view;
	}

	@Override
	public void onOwnerFocusChange(boolean hasFocus) {
		mContentView.onOwnerFocusChange(hasFocus);
	}

}

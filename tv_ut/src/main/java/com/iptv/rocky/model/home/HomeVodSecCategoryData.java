package com.iptv.rocky.model.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.view.home.HomeVodSecCategoryView;
import com.iptv.rocky.R;

public class HomeVodSecCategoryData extends HomeItemData {
	
	private HomeVodSecCategoryView mContentView;

	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = (HomeVodSecCategoryView)inflater.inflate(R.layout.home_page_item_vodseccategory, null);
			mContentView.initView(this);
		}
		return mContentView;
	}

}

package com.iptv.rocky.model.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.view.home.HomeCategoryView;
import com.iptv.rocky.R;

public class HomeCategoryData extends HomeItemData {
	
	private HomeCategoryView mContentView;

	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = (HomeCategoryView) inflater.inflate(R.layout.home_page_item_category, null);
			mContentView.initView(this);
		}
		return mContentView;
	}

}

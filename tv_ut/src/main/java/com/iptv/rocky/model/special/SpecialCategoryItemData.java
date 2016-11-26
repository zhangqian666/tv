package com.iptv.rocky.model.special;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.data.SpecialCategoryObj;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.R;
import com.iptv.rocky.SpecialDetailActivity;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.view.special.SpecialCategoryItemView;

public class SpecialCategoryItemData extends BaseMetroItemData {
	
	public SpecialCategoryObj categoryItem;
	
	private SpecialCategoryItemView mContentView;

	public SpecialCategoryItemData() {
		widthSpan = 1.5;
		heightSpan = 1.0;
	}
	
	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			mContentView = (SpecialCategoryItemView)LayoutInflater.from(context).inflate(R.layout.special_category_item, null);
			mContentView.initView(this);
		}
		return mContentView;
	}
	
	@Override
	public void onClick(Context context) {
		Intent intent = new Intent(context, SpecialDetailActivity.class);
		intent.putExtra(Constants.cSpecialDetailExtra, categoryItem.getId());
		intent.putExtra(Constants.cSpecialDetailPlatformExtra, categoryItem.getPlatform().toString());
		context.startActivity(intent);
	}

	@Override
	public void onOwnerFocusChange(boolean hasFocus) {
		if (hasFocus) {
			mContentView.startMarquee();
		} else {
			mContentView.stopMarquee();
		}
	}
}

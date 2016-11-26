package com.iptv.rocky.model.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.view.home.HomeCommonView;
import com.iptv.rocky.R;

public class HomeCommonData extends HomeItemData {
	
	private HomeCommonView mContentView;
	
	public boolean isFontBlod = false;
	
	@Override
	public View getView(Context context) {
        HomeCommonView view = mContentView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = (HomeCommonView) inflater.inflate(R.layout.home_page_item_common, null);
            view.initView(this);
            mContentView = view;
        }
        return view;
    }

	@Override
	public void initSpecialData() {
		if (super.id == 403 || super.id == 404 
				|| super.id == 701 || super.id == 702 || super.id == 703) {
			isFontBlod = true;
		}
	}
	
}

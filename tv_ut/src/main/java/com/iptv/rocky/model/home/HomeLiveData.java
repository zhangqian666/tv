package com.iptv.rocky.model.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.view.home.HomeLiveView;
import com.iptv.rocky.R;

public class HomeLiveData extends HomeItemData {
	
	private HomeLiveView mContentView;
	
	public boolean isFontBlod = false;
	
	@Override
	public View getView(Context context) {
		HomeLiveView view = mContentView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = (HomeLiveView) inflater.inflate(R.layout.home_page_item_live, null);
            view.initView(this);
            mContentView = view;
        }
        return view;
    }

	@Override
	public void initSpecialData() {
		if (super.id == 403 || super.id == 404) {
			isFontBlod = true;
		}
	}
	
}

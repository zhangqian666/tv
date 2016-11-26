package com.iptv.rocky.model.live;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.view.live.LiveTypeCommonView;
import com.iptv.rocky.R;

public class LiveTypeCommonData extends LiveTypeItemData {
	
	private LiveTypeCommonView mContentView;
	
	public boolean isFontBlod = false;
	
	@Override
	public View getView(Context context) {
		LiveTypeCommonView view = mContentView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = (LiveTypeCommonView) inflater.inflate(R.layout.livetype_page_item_common, null);
            view.initView(this);
            mContentView = view;
        }
        return view;
    }

	@Override
	public void initSpecialData() {
	}
	
}

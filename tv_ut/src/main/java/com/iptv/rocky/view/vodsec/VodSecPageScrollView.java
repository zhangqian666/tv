package com.iptv.rocky.view.vodsec;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.common.data.VodSec;
import com.iptv.rocky.model.home.HomeItemData;
import com.iptv.rocky.model.home.HomePageItem;
import com.iptv.rocky.model.vodsec.VodSecLayoutFactory;
import com.iptv.rocky.model.vodsec.VodSecLayoutItem;
import com.iptv.rocky.view.TvHorizontalScrollView;
import com.iptv.rocky.view.home.HomeMetroView;

public class VodSecPageScrollView extends TvHorizontalScrollView {
	
	private HomeMetroView mMetroView;

	public VodSecPageScrollView(Context context) {
		this(context, null, 0);
	}
	
	public VodSecPageScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public VodSecPageScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setWillNotDraw(true);
    	setSmoothScrollingEnabled(true);
	}
	
	public void createView(VodSecLayoutItem item) {
		mMetroView = new HomeMetroView(getContext());
		for (int i = 0, j = item.items.size(); i < j; i++) {
			HomePageItem page = item.items.get(i);
			HomeItemData data = VodSecLayoutFactory.createLiveData(page);
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
		addView(mMetroView);
	}
	
	public void createView(ArrayList<VodSec> infos, boolean isAutoFocus) {
		if (mMetroView == null) {
			mMetroView = new HomeMetroView(getContext());
			mMetroView.isAutoFocus = isAutoFocus;
			addView(mMetroView);
		} else {
			mMetroView.isAutoFocus = isAutoFocus;
			mMetroView.clear();
			mMetroView.removeAllViewsInLayout();
		}
		
		for (int i = 0, j = infos.size(); i < j; i++) {
			HomeItemData data = VodSecLayoutFactory.createLiveData(infos.get(i));
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
	}
	
	public void destroy() {
		if (mMetroView != null) {
			mMetroView.destroy();
		}
	}

}

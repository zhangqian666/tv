package com.iptv.rocky.view.myHotel;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.common.data.MyHotelSec;
import com.iptv.rocky.model.home.HomeItemData;
import com.iptv.rocky.model.home.HomePageItem;
import com.iptv.rocky.model.hotel.MyHotelSecLayoutFactory;
import com.iptv.rocky.model.hotel.MyHotelSecLayoutItem;
import com.iptv.rocky.view.TvHorizontalScrollView;
import com.iptv.rocky.view.home.HomeMetroView;

public class MyHotelSecPageScrollView extends TvHorizontalScrollView {
	
	private HomeMetroView mMetroView;

	public MyHotelSecPageScrollView(Context context) {
		this(context, null, 0);
	}
	
	public MyHotelSecPageScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MyHotelSecPageScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setWillNotDraw(true);
    	setSmoothScrollingEnabled(true);
	}
	
/*	public void createView(VodSecLayoutItem item) {
		mMetroView = new HomeMetroView(getContext());
		for (int i = 0, j = item.items.size(); i < j; i++) {
			HomePageItem page = item.items.get(i);
			HomeItemData data = VodSecLayoutFactory.createLiveData(page);
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
		addView(mMetroView);
	}*/
	
	
	public void createView(MyHotelSecLayoutItem item) {
		mMetroView = new HomeMetroView(getContext());
		for (int i = 0, j = item.items.size(); i < j; i++) {
			HomePageItem page = item.items.get(i);
			HomeItemData data = MyHotelSecLayoutFactory.createLiveData(page);
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
		addView(mMetroView);
	}
	
/*	public void createView(ArrayList<VodSec> infos, boolean isAutoFocus) {
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
	}*/
	
	public void createView(ArrayList<MyHotelSec> infos, boolean isAutoFocus) {
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
			HomeItemData data = MyHotelSecLayoutFactory.createLiveData(infos.get(i));
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

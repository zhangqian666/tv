package com.iptv.rocky.view.live;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.common.data.LiveChannelBill;
import com.iptv.rocky.model.live.LiveTypeItemData;
import com.iptv.rocky.model.live.LiveTypeLayoutFactory;
import com.iptv.rocky.model.live.LiveTypeLayoutItem;
import com.iptv.rocky.model.live.LiveTypePageItem;
import com.iptv.rocky.view.TvHorizontalScrollView;

public class LiveTypePageScrollView extends TvHorizontalScrollView {
	
	private LiveTypeMetroView mMetroView;

	public LiveTypePageScrollView(Context context) {
		this(context, null, 0);
	}
	
	public LiveTypePageScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public LiveTypePageScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setWillNotDraw(true);
    	setSmoothScrollingEnabled(true);
	}
	
	public void createView(LiveTypeLayoutItem item) {
		mMetroView = new LiveTypeMetroView(getContext());
		for (int i = 0, j = item.items.size(); i < j; i++) {
			LiveTypePageItem page = item.items.get(i);
			LiveTypeItemData data = LiveTypeLayoutFactory.createLiveData(page);
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
		addView(mMetroView);
	}
	
	public void createView(ArrayList<LiveChannelBill> infos, boolean isAutoFocus) {
		if (mMetroView == null) {
			mMetroView = new LiveTypeMetroView(getContext());
			mMetroView.isAutoFocus = isAutoFocus;
			addView(mMetroView);
		} else {
			mMetroView.isAutoFocus = isAutoFocus;
			mMetroView.clear();
			mMetroView.removeAllViewsInLayout();
		}
		
		for (int i = 0, j = infos.size(); i < j; i++) {
			LiveTypeItemData data = LiveTypeLayoutFactory.createLiveData(infos.get(i));
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

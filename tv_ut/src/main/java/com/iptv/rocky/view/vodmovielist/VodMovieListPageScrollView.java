package com.iptv.rocky.view.vodmovielist;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.common.data.VodChannel;
import com.iptv.rocky.model.vodmovielist.VodMovieListItemData;
import com.iptv.rocky.model.vodmovielist.VodMovieListLayoutFactory;
import com.iptv.rocky.model.vodmovielist.VodMovieListLayoutItem;
import com.iptv.rocky.model.vodmovielist.VodMovieListPageItem;
import com.iptv.rocky.view.TvHorizontalScrollView;

public class VodMovieListPageScrollView extends TvHorizontalScrollView {
	
	private VodMovieListMetroView mMetroView;

	public VodMovieListPageScrollView(Context context) {
		this(context, null, 0);
	}
	
	public VodMovieListPageScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public VodMovieListPageScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setWillNotDraw(true);
    	setSmoothScrollingEnabled(true);
	}
	
	public void createView(VodMovieListLayoutItem item) {
		mMetroView = new VodMovieListMetroView(getContext());
		for (int i = 0, j = item.items.size(); i < j; i++) {
			VodMovieListPageItem page = item.items.get(i);
			VodMovieListItemData data = VodMovieListLayoutFactory.createVodData(page);
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
		addView(mMetroView);
	}
	
	public void createView(ArrayList<VodChannel> infos, boolean isAutoFocus) {
		if (mMetroView == null) {
			mMetroView = new VodMovieListMetroView(getContext());
			mMetroView.isAutoFocus = isAutoFocus;
			addView(mMetroView);
		} else {
			mMetroView.isAutoFocus = isAutoFocus;
			mMetroView.clear();
			mMetroView.removeAllViewsInLayout();
		}
		
		for (int i = 0, j = infos.size(); i < j; i++) {
			VodMovieListItemData data = VodMovieListLayoutFactory.createVodData(infos.get(i));
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

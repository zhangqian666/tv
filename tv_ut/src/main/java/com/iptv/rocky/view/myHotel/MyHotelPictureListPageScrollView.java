package com.iptv.rocky.view.myHotel;

import java.util.ArrayList;
import android.content.Context;
import android.util.AttributeSet;
import com.iptv.common.data.MyHotelPicture;
import com.iptv.common.data.MyHotelPictureList;
import com.iptv.rocky.model.hotel.MyHotelPictureListItemData;
import com.iptv.rocky.model.hotel.MyHotelPictureListLayoutFactory;
import com.iptv.rocky.model.hotel.MyHotelPictureListLayoutItem;
import com.iptv.rocky.model.hotel.MyHotelPictureListPageItem;
import com.iptv.rocky.view.TvHorizontalScrollView;

public class MyHotelPictureListPageScrollView extends TvHorizontalScrollView {
	
	private MyHotelPictureListMetroView mMetroView;

	public MyHotelPictureListPageScrollView(Context context) {
		this(context, null, 0);
	}
	
	public MyHotelPictureListPageScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MyHotelPictureListPageScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setWillNotDraw(true);
    	setSmoothScrollingEnabled(true);
	}
	
	public void createView(MyHotelPictureListLayoutItem item) {
		mMetroView = new MyHotelPictureListMetroView(getContext());
		/*for (int i = 0, j = item..size(); i < j; i++) {
			MyHotelPictureListPageItem page = item.items.get(i);
			MyHotelPictureListItemData data = MyHotelPictureListLayoutFactory.createMyHotelPictureData(page);
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}*/
		addView(mMetroView);
	}
	
	public void createView(MyHotelPictureListLayoutItem infos, boolean isAutoFocus) {
		/*if (mMetroView == null) {
			mMetroView = new MyHotelPictureListMetroView(getContext());
			mMetroView.isAutoFocus = isAutoFocus;
			addView(mMetroView);
		} else {
			mMetroView.isAutoFocus = isAutoFocus;
			mMetroView.clear();
			mMetroView.removeAllViewsInLayout();
		}
		
		for (int i = 0, j = infos.size(); i < j; i++) {
			MyHotelPictureListItemData data = MyHotelPictureListLayoutFactory.createMyHotelPictureData(infos.get(i));
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}*/
	}
	
	public void destroy() {
		if (mMetroView != null) {
			mMetroView.destroy();
		}
	}

}

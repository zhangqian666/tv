package com.iptv.rocky.view.home;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.MyHotelTop;
import com.iptv.common.data.PortalChannels;
import com.iptv.common.data.PortalHome;
import com.iptv.common.data.PortalLiveType;
import com.iptv.common.data.VodTop;
import com.iptv.common.utils.DataReloadUtil;
import com.iptv.rocky.R;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.HomeItemData;
import com.iptv.rocky.model.home.HomeLayoutFactory;
import com.iptv.rocky.model.home.HomeLayoutItem;
import com.iptv.rocky.model.home.HomePageItem;
import com.iptv.rocky.view.TvHorizontalScrollView;

public class HomePageScrollView extends TvHorizontalScrollView {
	
	private HomeMetroView mMetroView;

	public HomePageScrollView(Context context) {
		this(context, null, 0);
	}
	
	public HomePageScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HomePageScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setWillNotDraw(true);
    	setSmoothScrollingEnabled(true);
	}
	
	public void createView(HomeLayoutItem item) {
		mMetroView = new HomeMetroView(getContext());
		for (int i = 0; i < item.items.size(); i++) {
			HomePageItem page = item.items.get(i);
			HomeItemData data = HomeLayoutFactory.createHomeData(page);
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
		addView(mMetroView);
	}
	
	public void createView(ArrayList<PortalHome> infos, boolean isAutoFocus) {
		if (mMetroView == null) {
			mMetroView = new HomeMetroView(getContext());
			mMetroView.isAutoFocus = isAutoFocus;
			addView(mMetroView);
		} else {
			mMetroView.isAutoFocus = isAutoFocus;
			mMetroView.clear();
			mMetroView.removeAllViewsInLayout();
		}
		
		for (int i = 0; i < infos.size(); i++) {
			HomeItemData data = HomeLayoutFactory.createHomeData(infos.get(i));
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
	}
	
	public void createLiveView(PortalChannels liveChannels, boolean isAutoFocus) {
		if (mMetroView == null) {
			mMetroView = new HomeMetroView(getContext());
			mMetroView.isAutoFocus = isAutoFocus;
			addView(mMetroView);
		} else {
			mMetroView.isAutoFocus = isAutoFocus;
			mMetroView.clear();
			mMetroView.removeAllViewsInLayout();
		};
		
		int i = 0;
		for (i = 0; i < liveChannels.lstPortalLiveChannel.size(); i++) {
			HomeItemData data = HomeLayoutFactory.createHomeData(liveChannels.lstPortalLiveChannel.get(i));
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
		for (i = 0; i < liveChannels.lstPortalLiveType.size(); i++) {
			HomeItemData data = HomeLayoutFactory.createHomeData(liveChannels.lstPortalLiveType.get(i));
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
		
		//ADD
		if (TvApplication.shoulLoginIptv) {
			PortalHome info = new PortalHome();
			info.icon = "assets://icon/rec.png";
			info.title = getContext().getString(R.string.home_lookback);
			info.content_type = EnumType.ContentType.UNKNOW;
			info.layout_type = EnumType.LayoutType.UNKNOW;
            info.id = DataReloadUtil.HomeRecChanViewID;
			HomeItemData data = HomeLayoutFactory.createHomeData(info);
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
	}
	
	public void createVodView(ArrayList<VodTop> infos, boolean isAutoFocus) {
		if (mMetroView == null) {
			mMetroView = new HomeMetroView(getContext());
			mMetroView.isAutoFocus = isAutoFocus;
			addView(mMetroView);
		} else {
			mMetroView.isAutoFocus = isAutoFocus;
			mMetroView.clear();
			mMetroView.removeAllViewsInLayout();
		}
		
		for (int i = 0; i < infos.size(); i++) {
			HomeItemData data = HomeLayoutFactory.createHomeData(infos.get(i));
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
	}
	
	/**
	 *  Create My Hotel's View
	 */
	public void createMyHotelView(ArrayList<MyHotelTop> infos, boolean isAutoFocus) {
		if (mMetroView == null) {
			mMetroView = new HomeMetroView(getContext());
			mMetroView.isAutoFocus = isAutoFocus;
			addView(mMetroView);
		} else {
			mMetroView.isAutoFocus = isAutoFocus;
			mMetroView.clear();
			mMetroView.removeAllViewsInLayout();
		}
		
		for (int i = 0; i < infos.size(); i++) {
			HomeItemData data = HomeLayoutFactory.createMyHotelData(infos.get(i));
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

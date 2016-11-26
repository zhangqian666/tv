package com.iptv.rocky.view.home;

import java.util.ArrayList;

import com.iptv.common.data.MyHotelTop;
import com.iptv.common.data.PortalChannels;
import com.iptv.common.data.PortalHome;
import com.iptv.common.data.VodTop;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.home.HomeLayoutItem;
import com.iptv.rocky.model.home.HomeViewPagerAdapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class HomeViewPager extends ViewPager {
	
	private ArrayList<HomePageScrollView> mPages;
	
	public HomeViewPager(Context context) {
		this(context, null);
	}

    public HomeViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
    
    public void createView(ArrayList<HomeLayoutItem> items) {
    	int size = items.size();
    	mPages = new ArrayList<HomePageScrollView>(size);
	    for (int i = 0; i < size; i++) {
	    	HomePageScrollView page = new HomePageScrollView(getContext());
	    	page.createView(items.get(i));
	    	mPages.add(page);
	    }
	    setAdapter(new HomeViewPagerAdapter((BaseActivity)getContext(), mPages));
    }
    
    public void createView(int index, ArrayList<PortalHome> infos, boolean isAutoFocus) {
    	HomePageScrollView frame = mPages.get(index);
    	frame.createView(infos, isAutoFocus);
    }
    
    public void createLiveView(int index, PortalChannels liveChannels, boolean isAutoFocus) {
    	HomePageScrollView frame = mPages.get(index);
    	frame.createLiveView(liveChannels, isAutoFocus);
    }
    
    public void createVodView(int index, ArrayList<VodTop> infos, boolean isAutoFocus) {
    	HomePageScrollView frame = mPages.get(index);
    	frame.createVodView(infos, isAutoFocus);
    }
    
    // Create MyHotelTop View
    public void createMyHotelView(int index, ArrayList<MyHotelTop> infos, boolean isAutoFocus) {
    	HomePageScrollView frame = mPages.get(index);
    	frame.createMyHotelView(infos, isAutoFocus);
    }
    
    // Create MyHotelTop View
    public void createTravelView(int index, ArrayList<MyHotelTop> infos, boolean isAutoFocus) {
    	HomePageScrollView frame = mPages.get(index);
    	frame.createMyHotelView(infos, isAutoFocus);
    }
    
    
    public void destroy() {
    	if (mPages != null) {
    		for (int i = 0, j = mPages.size(); i < j; i++) {
    			mPages.get(i).destroy();
    		}
    	}
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
    	if (super.dispatchKeyEvent(event)) {
    		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT 
				|| event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					return true;
				}
    		}
    	}
    	return false;
    }
}

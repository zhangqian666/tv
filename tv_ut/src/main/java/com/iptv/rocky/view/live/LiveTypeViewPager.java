package com.iptv.rocky.view.live;

import java.util.ArrayList;

import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.live.LiveTypeLayoutItem;
import com.iptv.rocky.model.live.LiveTypeViewPagerAdapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class LiveTypeViewPager extends ViewPager {
	
	private ArrayList<LiveTypePageScrollView> mPages;
	
	public LiveTypeViewPager(Context context) {
		this(context, null);
	}

    public LiveTypeViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
    
    public void createView(ArrayList<LiveTypeLayoutItem> items) {
    	int size = items.size();
    	mPages = new ArrayList<LiveTypePageScrollView>(size);
	    for (int i = 0; i < size; i++) {
	    	LiveTypePageScrollView page = new LiveTypePageScrollView(getContext());
	    	page.createView(items.get(i));
	    	mPages.add(page);
	    }
	    setAdapter(new LiveTypeViewPagerAdapter((BaseActivity)getContext(), mPages));
    }
    
    public void createView(int index, ArrayList<LiveChannelBill> infos, boolean isAutoFocus) {
    	LiveTypePageScrollView frame = mPages.get(index);
    	frame.createView(infos, isAutoFocus);
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

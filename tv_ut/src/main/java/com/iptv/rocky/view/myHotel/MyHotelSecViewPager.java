package com.iptv.rocky.view.myHotel;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.iptv.common.data.MyHotelSec;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.hotel.MyHotelSecLayoutItem;
import com.iptv.rocky.model.hotel.MyHotelSecViewPagerAdapter;

public class MyHotelSecViewPager extends ViewPager {
	
	private ArrayList<MyHotelSecPageScrollView> mPages;
	
	public MyHotelSecViewPager(Context context) {
		this(context, null);
	}

    public MyHotelSecViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
    
    public void createView(ArrayList<MyHotelSecLayoutItem> items) {
    	int size = items.size();
    	mPages = new ArrayList<MyHotelSecPageScrollView>(size);
	    for (int i = 0; i < size; i++) {
	    	MyHotelSecPageScrollView page = new MyHotelSecPageScrollView(getContext());
	    	page.createView(items.get(i));
	    	mPages.add(page);
	    }
	    setAdapter(new MyHotelSecViewPagerAdapter((BaseActivity)getContext(), mPages));
    }
    
    public void createView(int index, ArrayList<MyHotelSec> infos, boolean isAutoFocus) {
    	MyHotelSecPageScrollView frame = mPages.get(index);
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

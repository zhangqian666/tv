package com.iptv.rocky.view.vodsec;

import java.util.ArrayList;

import com.iptv.common.data.VodSec;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.vodsec.VodSecLayoutItem;
import com.iptv.rocky.model.vodsec.VodSecViewPagerAdapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class VodSecViewPager extends ViewPager {
	
	private ArrayList<VodSecPageScrollView> mPages;
	
	public VodSecViewPager(Context context) {
		this(context, null);
	}

    public VodSecViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
    
    public void createView(ArrayList<VodSecLayoutItem> items) {
    	int size = items.size();
    	mPages = new ArrayList<VodSecPageScrollView>(size);
	    for (int i = 0; i < size; i++) {
	    	VodSecPageScrollView page = new VodSecPageScrollView(getContext());
	    	page.createView(items.get(i));
	    	mPages.add(page);
	    }
	    setAdapter(new VodSecViewPagerAdapter((BaseActivity)getContext(), mPages));
    }
    
    public void createView(int index, ArrayList<VodSec> infos, boolean isAutoFocus) {
    	VodSecPageScrollView frame = mPages.get(index);
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

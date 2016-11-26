package com.iptv.rocky.view.vodmovielist;

import java.util.ArrayList;

import com.iptv.common.data.VodChannel;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.vodmovielist.VodMovieListLayoutItem;
import com.iptv.rocky.model.vodmovielist.VodMovieListViewPagerAdapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class VodMovieListViewPager extends ViewPager {
	
	private ArrayList<VodMovieListPageScrollView> mPages;
	
	public VodMovieListViewPager(Context context) {
		this(context, null);
	}

    public VodMovieListViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
    
    public void createView(ArrayList<VodMovieListLayoutItem> items) {
    	int size = items.size();
    	mPages = new ArrayList<VodMovieListPageScrollView>(size);
	    for (int i = 0; i < size; i++) {
	    	VodMovieListPageScrollView page = new VodMovieListPageScrollView(getContext());
	    	page.createView(items.get(i));
	    	mPages.add(page);
	    }
	    setAdapter(new VodMovieListViewPagerAdapter((BaseActivity)getContext(), mPages));
    }
    
    public void createView(int index, ArrayList<VodChannel> infos, boolean isAutoFocus) {
    	VodMovieListPageScrollView frame = mPages.get(index);
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

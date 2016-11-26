package com.iptv.rocky.view.recchan;

import java.util.ArrayList;

import com.iptv.common.data.RecBill;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.recchan.RecBillLayoutItem;
import com.iptv.rocky.model.recchan.RecBillViewPagerAdapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class RecBillViewPager extends ViewPager {
	
	private ArrayList<RecBillPageScrollView> mPages;
	
	private OnRecBillPageScrollChangeListener onRecBillPageScrollChangeListener;
	
	public void setOnRecBillPageScrollChangeListener(OnRecBillPageScrollChangeListener onRecBillPageScrollChangeListener) {
		this.onRecBillPageScrollChangeListener = onRecBillPageScrollChangeListener;
	}
	
	public interface OnRecBillPageScrollChangeListener {
		public abstract void onItemScrolled(int nCount);
	}
	
	public RecBillViewPager(Context context) {
		this(context, null);
	}

    public RecBillViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    
	    setPadding((int)(TvApplication.sTvLeftMargin * 2.2), 0, 0, 0);
	}
    
    public void createView(ArrayList<RecBillLayoutItem> items) {
    	int size = items.size();
    	mPages = new ArrayList<RecBillPageScrollView>(size);
	    for (int i = 0; i < size; i++) {
	    	RecBillPageScrollView page = new RecBillPageScrollView(getContext());
	    	page.createView(items.get(i), onRecBillPageScrollChangeListener);
	    	mPages.add(page);
	    }
	    setAdapter(new RecBillViewPagerAdapter((BaseActivity)getContext(), mPages));
    }
    
    public boolean createView(int index, ArrayList<RecBill> infos, boolean isAutoFocus) {
    	if (index == mPages.size()) {
    		return false;
    	}
    	RecBillPageScrollView frame = mPages.get(index);
    	frame.createView(infos, isAutoFocus);
    	return true;
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
    
    @Override
	public boolean arrowScroll(int direction) {
    	return false;
    }
    
    public RecBillPageScrollView getChildView(int index) {
    	return mPages.get(index);
    }
    
}

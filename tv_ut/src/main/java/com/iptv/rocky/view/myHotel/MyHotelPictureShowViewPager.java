package com.iptv.rocky.view.myHotel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.iptv.common.data.MyHotelPicture;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.hotel.MyHotelPictureListLayoutItem;
import com.iptv.rocky.model.hotel.MyHotelPictureShowViewPagerAdapter;

public class MyHotelPictureShowViewPager extends ViewPager {
	
	
	
	private List<ImageView> mPages;
	
	public MyHotelPictureShowViewPager(Context context) {
		this(context, null);
	}

    public MyHotelPictureShowViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
    
    public void createView(ArrayList<MyHotelPictureListLayoutItem> items) {
    	int size = items.size();
    	mPages = new ArrayList<ImageView>(size);
	    for (int i = 0; i < size; i++) {
	    	ImageView page = new ImageView(getContext());
	    	//page.createView(items.get(i));
	    	mPages.add(page);
	    }
	    setAdapter(new MyHotelPictureShowViewPagerAdapter((BaseActivity) getContext(), mPages));
    }
    
    public void createView(int index, ArrayList<MyHotelPicture> infos, boolean isAutoFocus) {
    	//MyHotelPictureListPageScrollView frame = mPages.get(index);
    	//frame.createView(infos, isAutoFocus);
    }
    
    public void destroy() {
    	/*if (mPages != null) {
    		for (int i = 0, j = mPages.size(); i < j; i++) {
    			mPages.get(i).destroy();
    		}
    	}*/
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

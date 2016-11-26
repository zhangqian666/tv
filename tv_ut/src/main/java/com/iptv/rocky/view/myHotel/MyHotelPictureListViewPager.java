package com.iptv.rocky.view.myHotel;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.iptv.rocky.model.hotel.MyHotelPictureListLayoutItem;

public class MyHotelPictureListViewPager extends ViewPager {
	
	private ImageView[] mImageViews;  
	
	//private ArrayList<MyHotelPictureListPageScrollView> mPages;
	
	public MyHotelPictureListViewPager(Context context) {
		this(context, null);
	}

    public MyHotelPictureListViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
    
    public void createView(ArrayList<MyHotelPictureListLayoutItem> items) {
    	int size = items.size();
    	//mPages = new ArrayList<MyHotelPictureListPageScrollView>(size);
	    for (int i = 0; i < size; i++) {
	    	MyHotelPictureListPageScrollView page = new MyHotelPictureListPageScrollView(getContext());
	    	page.createView(items.get(i));
	    	//mPages.add(page);
	    }
	    //setAdapter(new MyHotelPictureListViewPagerAdapter((BaseActivity)getContext(), mPages));
    }
    
    public void createView(int index, ArrayList<MyHotelPictureListLayoutItem>  infos, boolean isAutoFocus) {
    	int size = infos.size();
    	 for (int i = 0; i < size; i++) {
 	    	MyHotelPictureListPageScrollView page = new MyHotelPictureListPageScrollView(getContext());
 	    	page.createView(infos.get(i));
 	    	//mPages.add(page);
 	    }
 	    //setAdapter(new MyHotelPictureListViewPagerAdapter((BaseActivity)getContext(), mPages));
    	
    	//MyHotelPictureListPageScrollView frame = mPages.get(index);
    	//frame.createView(infos.get(index), isAutoFocus);
    }
    
    public void destroy() {
    	//if (mPages != null) {
    		//for (int i = 0, j = mPages.size(); i < j; i++) {
    			//mPages.get(i).destroy();
    		//}
    	//}
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

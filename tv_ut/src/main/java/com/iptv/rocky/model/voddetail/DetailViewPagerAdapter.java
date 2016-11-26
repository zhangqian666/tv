package com.iptv.rocky.model.voddetail;

import java.util.List;

import com.iptv.common.utils.LogUtils;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class DetailViewPagerAdapter extends PagerAdapter {
	
	private List<View> list;
	
	public DetailViewPagerAdapter(List<View> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = list.get(position);
		((ViewPager)container).removeView(view);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (position > list.size() - 1) {
	        LogUtils.w("HomeViewPager", "instantiateItem failed: " + position);
	        return null;
        }
		View localView = (View)list.get(position);
		if (localView.getParent() != null) {
			((ViewGroup)localView.getParent()).removeView(localView);
			((ViewPager) container).addView(localView, 0);
		} else {
			((ViewPager)container).addView(localView, 0);
		}
	    return localView;
	}
	
}

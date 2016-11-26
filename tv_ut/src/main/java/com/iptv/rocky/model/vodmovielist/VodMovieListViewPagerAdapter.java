package com.iptv.rocky.model.vodmovielist;

import java.util.List;

import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.view.vodmovielist.VodMovieListPageScrollView;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class VodMovieListViewPagerAdapter extends PagerAdapter {
	
	private BaseActivity mActivity;
	public List<VodMovieListPageScrollView> mList;

    public VodMovieListViewPagerAdapter(BaseActivity activity, List<VodMovieListPageScrollView> list) {
    	mActivity = activity;
    	mList = list;
    }

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		VodMovieListPageScrollView view = mList.get(position);
		ViewPager pager = (ViewPager)container;
    	pager.addView(view);
    	mActivity.handleMessage();
	    return view;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		VodMovieListPageScrollView page = mList.get(position);
		ViewPager pager = (ViewPager)container;
		pager.removeView(page);
	}

	@Override
	public void finishUpdate(View container) {
	}

	public final void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader) {
    }

    public final Parcelable saveState() {
    	return null;
    }

    public final void startUpdate(View paramView) {
    }

}

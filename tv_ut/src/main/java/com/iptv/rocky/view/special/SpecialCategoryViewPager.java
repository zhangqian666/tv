package com.iptv.rocky.view.special;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.iptv.common.data.SpecialCategoryObj;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.special.SpecialCategoryViewPagerAdapter;

public class SpecialCategoryViewPager extends ViewPager {

	private ArrayList<SpecialCategoryMetroView> mPages;

	private SpecialCategoryViewPagerAdapter mAdapter;
	
	public SpecialCategoryViewPager(Context context) {
		this(context, null);
	}

	public SpecialCategoryViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setOffscreenPageLimit(2);
		
		setPadding(TvApplication.sTvChannelUnit / 2, 0, 0, 0);
	}

	public void createView(ArrayList<SpecialCategoryObj> datas) {
		int pageNumber = Constants.cSpecialCategoryNumber;
		int size = datas.size();
		int page = (size / pageNumber) + (size % pageNumber > 0 ? 1 : 0);

		mPages = new ArrayList<SpecialCategoryMetroView>(page);

		for (int i = 0; i < page; i++) {
			SpecialCategoryMetroView metroView = new SpecialCategoryMetroView(getContext());
			mPages.add(metroView);
		}
		
		if (mAdapter == null) {
			mAdapter = new SpecialCategoryViewPagerAdapter(mPages, datas);
			setAdapter(mAdapter);
		} else {
			mAdapter.setmViews(mPages);
			mAdapter.setmDatas(datas);
			mAdapter.notifyDataSetChanged();
		}
//		setAdapter(new SpecialCategoryViewPagerAdapter(mPages));
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

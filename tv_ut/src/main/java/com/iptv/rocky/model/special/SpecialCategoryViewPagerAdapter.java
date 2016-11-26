package com.iptv.rocky.model.special;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.iptv.common.data.SpecialCategoryObj;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.view.special.SpecialCategoryMetroView;

public class SpecialCategoryViewPagerAdapter extends PagerAdapter {

	private List<SpecialCategoryMetroView> mViews;
	private List<SpecialCategoryObj> mDatas;

	public SpecialCategoryViewPagerAdapter(List<SpecialCategoryMetroView> views, List<SpecialCategoryObj> datas) {
		this.mViews = views;
		this.mDatas = datas;
	}

	@Override
	public int getCount() {
		int pageNumber = Constants.cSpecialCategoryNumber;
		int size = mDatas.size();
		int page = (size / pageNumber) + (size % pageNumber > 0 ? 1 : 0);
		return page;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		SpecialCategoryMetroView metroView = mViews.get(position);
		if (metroView.getChildCount() <= 0) {
			int pageNumber = Constants.cSpecialCategoryNumber;
			int size = mDatas.size();
			
			for (int i = 0; i < pageNumber; i++) {
				int index = position * pageNumber + i;
				if (index >= size) {
					break;
				}

				SpecialCategoryItemData itemData = new SpecialCategoryItemData();
				itemData.categoryItem = mDatas.get(index);
				metroView.addMetroItem(itemData);
			}
		}
		
		ViewPager pager = (ViewPager) container;
		pager.addView(metroView);
		return metroView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		LogUtils.i("info", "position--->>" + position);
		((ViewPager) container).removeViewInLayout((View) object);
	}

	public void setmViews(List<SpecialCategoryMetroView> mViews) {
		this.mViews = mViews;
	}

	public void setmDatas(List<SpecialCategoryObj> mDatas) {
		this.mDatas = mDatas;
	}
}

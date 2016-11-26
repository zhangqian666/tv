package com.iptv.rocky.model.special;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.iptv.common.data.SpecialCategoryObj;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.view.special.SpecialCategoryMetroView;

public class SpecialCategoryAdapter extends BaseAdapter {
	
	private ArrayList<SpecialCategoryObj> mDatas;
	private ArrayList<SpecialCategoryMetroView> mViews;
	
	public SpecialCategoryAdapter(ArrayList<SpecialCategoryObj> datas, ArrayList<SpecialCategoryMetroView> metroViews) {
		mDatas = datas;
		mViews = metroViews;
	}

	@Override
	public int getCount() {
		return mViews.size();
	}

	@Override
	public Object getItem(int position) {
		return mViews.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SpecialCategoryMetroView metroView = mViews.get(position);
		
		if (metroView.getChildCount() <= 0) {
			int size = mDatas.size();
			int pageNumber = Constants.cSpecialCategoryNumber;
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
		metroView.isAutoFocus = false;
		
		return metroView;
	}

	public void setmDatas(ArrayList<SpecialCategoryObj> mDatas) {
		this.mDatas = mDatas;
	}

	public void setmViews(ArrayList<SpecialCategoryMetroView> mViews) {
		this.mViews = mViews;
	}
	
}

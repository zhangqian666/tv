package com.iptv.rocky.model.vodthd;

import java.util.ArrayList;

import com.iptv.common.data.VodSec;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.view.vodthd.SpecialCategoryMetroView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class VodThdAdapter extends BaseAdapter {
	
	private ArrayList<VodSec> mDatas;
	private ArrayList<SpecialCategoryMetroView> mViews;
	private String mColumnCode;
	
	public VodThdAdapter(ArrayList<VodSec> datas, ArrayList<SpecialCategoryMetroView> metroViews,String columnCode) {
		mDatas = datas;
		mViews = metroViews;
		mColumnCode=columnCode;
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
				VodThdItemData itemData = new VodThdItemData();
				itemData.categoryItem = mDatas.get(index);
				itemData.mColumnCode=mColumnCode;
				metroView.addMetroItem(itemData);
			}
		}
		metroView.isAutoFocus = false;
		
		return metroView;
	}
}

package com.iptv.rocky.model.detail;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.rocky.view.detail.SelectNotNumberGallery;
import com.iptv.rocky.view.detail.SelectNotNumberMetroView;

public class SelectNotNumberAdapter extends BaseAdapter {

	private List<VodChannel> mDatas;
	private ArrayList<SelectNotNumberMetroView> mViews;
	private VodDetailInfo obj;

	public SelectNotNumberAdapter(VodDetailInfo obj,
			ArrayList<SelectNotNumberMetroView> metroViews) {
		mDatas = obj.SUBVODIDLIST;
		mViews = metroViews;
		this.obj = obj;
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
		SelectNotNumberMetroView metroView = mViews.get(position);

		if (metroView.getChildCount() <= 0) {
			int size = mDatas.size();
			int pageNumber = SelectNotNumberGallery.sPageNumber;
			for (int i = 0; i < pageNumber; i++) {
				int index = position * pageNumber + i;
				if (index >= size) {
					break;
				}
//				SelectItemData itemData = new SelectItemData(1.0, 0.6);
//				itemData.setVodDetailObj(obj);
//				itemData.setData(mDatas.get(index));
//				metroView.addMetroItem(itemData);
			}
		}
		metroView.isAutoFocus = false;

		return metroView;
	}

}

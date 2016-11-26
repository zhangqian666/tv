package com.iptv.rocky.model.special;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.iptv.common.data.SpecialItemObj;

public class SpecialListAdapter extends BaseAdapter {

	private Context mContext;
	private List<SpecialItemObj> mDatas;
	private SpecialListItemData.onScrollListener mScrollListener;
	
	public SpecialListAdapter(Context context, List<SpecialItemObj> datas, SpecialListItemData.onScrollListener scrollListener) {
		this.mContext = context;
		this.mDatas = datas;
		this.mScrollListener = scrollListener;
	}
	
	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SpecialListItemData holder = null;
		if (convertView == null) {
			holder = new SpecialListItemData();
			convertView = holder.getView(mContext);
			holder.setOnScrollListener(mScrollListener);
			convertView.setTag(holder);
		} else {
			holder = (SpecialListItemData)convertView.getTag();
			holder.destroy();
		}
		SpecialItemObj data = mDatas.get(position);
		holder.setView(data);
		
		return convertView;
	}
}

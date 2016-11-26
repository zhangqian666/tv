package com.iptv.rocky.model.vodsearch;

import java.util.ArrayList;

import com.iptv.common.data.VodChannel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SearchAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<VodChannel> mDatas;

	public SearchAdapter(Context context, ArrayList<VodChannel> datas) {
		this.mContext = context;
        this.mDatas = datas;
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
		SearchItemData holder = null;
		if (convertView == null) {
			holder = new SearchItemData();
			convertView = holder.getView(mContext);
			convertView.setTag(holder);
		} else {
			holder = (SearchItemData)convertView.getTag();
			holder.destroy();
		}
		VodChannel data = mDatas.get(position);
		holder.initView(data);
		
		return convertView;
	}
}

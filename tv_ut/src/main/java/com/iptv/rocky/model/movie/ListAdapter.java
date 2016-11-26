package com.iptv.rocky.model.movie;

import java.util.ArrayList;

import com.iptv.common.data.VodChannel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<VodChannel> mDatas;
	private String mColumnCode;
	
	public ListAdapter(Context context, ArrayList<VodChannel> datas,String columnCode) {
		this.mContext = context;
		this.mDatas = datas;
		this.mColumnCode=columnCode;
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
		ListItemData holder = null;
		if (convertView == null) {
			holder = new ListItemData();
			convertView = holder.getView(mContext);
			convertView.setTag(holder);
		} else {
			holder = (ListItemData)convertView.getTag();
			holder.destroy();
		}
		VodChannel data = mDatas.get(position);
		holder.initView(data,mColumnCode);
		
		return convertView;
	}
}

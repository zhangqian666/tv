package com.iptv.rocky.model.voddetail;

import java.util.ArrayList;

import com.iptv.common.data.VodChannel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DetailSimilarListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<VodChannel> mDatas;
	
	public DetailSimilarListAdapter(Context context, ArrayList<VodChannel> datas) {
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
		DetailSimilarListItemData holder = null;
		if (convertView == null) {
			holder = new DetailSimilarListItemData();
			convertView = holder.getView(mContext);
			convertView.setTag(holder);
		} else {
			holder = (DetailSimilarListItemData)convertView.getTag();
			holder.destroy();
		}
		VodChannel data = mDatas.get(position);
		holder.initView(data);
		
		return convertView;
	}
}

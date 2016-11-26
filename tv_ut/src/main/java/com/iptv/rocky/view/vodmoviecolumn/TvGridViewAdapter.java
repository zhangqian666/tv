package com.iptv.rocky.view.vodmoviecolumn;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.iptv.common.data.VodChannel;
import com.iptv.rocky.model.vodmoviecolumn.VodMovieColumnItemData;

public class TvGridViewAdapter extends BaseAdapter {
	
	private Context context;
	
	private ArrayList<VodChannel> sBaseInfos;
	
	private String mColumnCode;
	
	public TvGridViewAdapter(Context context, ArrayList<VodChannel> sBaseInfos,String columnCode) {
		this.context = context;
		this.sBaseInfos = sBaseInfos;
		this.mColumnCode=columnCode;
	}

	@Override
	public int getCount() {
		return sBaseInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return sBaseInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VodMovieColumnItemData holder = null;
		if (convertView == null) {
			holder = new VodMovieColumnItemData();
			convertView = holder.getView(context);
			convertView.setTag(holder);
		} else {
			holder = (VodMovieColumnItemData)convertView.getTag();
			holder.destroy();
		}
		VodChannel info = sBaseInfos.get(position);
		holder.setData(info,mColumnCode);
		return convertView;
	}

}

package com.iptv.rocky.model.recchan;

import java.util.ArrayList;

import com.iptv.common.data.RecChan;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<RecChan> mDatas;
	
	public ListAdapter(Context context, ArrayList<RecChan> datas) {
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
		ListItemData holder = null;
		if (convertView == null) {
			holder = new ListItemData();
			convertView = holder.getView(mContext);
			convertView.setTag(holder);
		} else {
			holder = (ListItemData)convertView.getTag();
			holder.destroy();
		}
		
		
/*			holder = new ListItemData();
			convertView = holder.getView(mContext);
			//convertView.setTag(holder);
*/		
		
		RecChan data = mDatas.get(position);
		holder.initView(data);
		
		return convertView;
	}
	
}

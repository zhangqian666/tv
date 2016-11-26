package com.iptv.rocky.tcl.model.live;

import java.util.ArrayList;

import com.iptv.common.data.LiveChannelBill;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<LiveChannelBill> mDatas;
	
    // 正在播放的用户自定义频道号
//    private int currentPlayUserChannelId;
	  private String currentPlayUserChannelId;
    // 正在播放的频道分类
    private int currentPlayLiveType;
    // 正在播放的频道的position，用于手工滚动定位
    private int currentPlayingPosition;
	
	public ListAdapter(Context context, ArrayList<LiveChannelBill> datas) {
		//Log.d("ListAdapter", "初始化");
		this.mContext = context;
		this.mDatas = datas;
	}
	
	@Override
	public int getCount() {
		//Log.d("ListAdapter","初始化，首先获取数量，得到绘制次数，GetCount:"+mDatas.size());
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		//Log.d("ListAdapter","getItem");
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		//Log.d("ListAdapter","getItemId");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//Log.d("ListAdapter", "currentPlayUserChannelId:"+currentPlayUserChannelId);
		//Log.d("ListAdapter", "currentPlayLiveType:"+currentPlayLiveType);
		ListItemData holder = null;
		if (convertView == null) {
			holder = new ListItemData();
			convertView = holder.getView(mContext);
			convertView.setTag(holder);
		} else {
			holder = (ListItemData)convertView.getTag();
			holder.destroy();
		}
		LiveChannelBill data = mDatas.get(position);
		//holder.initView(data);
		if(data.ChannelID.equals(currentPlayUserChannelId)){
			holder.initView(data,true);
			currentPlayingPosition =position;
		}else{
			holder.initView(data,false);
		}
		// Log.d("ListAdapter", "开始绘制 getView: position:"+position);
		// Log.d("ListAdapter", "convertView is playing:"+((ListItemData)convertView.getTag()).isPlaying());
		
		return convertView;
	}

	public String getCurrentPlayUserChannelId() {
		return currentPlayUserChannelId;
	}

	public void setCurrentPlayUserChannelId(String currentPlayUserChannelId) {
		this.currentPlayUserChannelId = currentPlayUserChannelId;
	}

	public int getCurrentPlayLiveType() {
		return currentPlayLiveType;
	}

	public void setCurrentPlayLiveType(int currentPlayLiveType) {
		this.currentPlayLiveType = currentPlayLiveType;
	}

	public int getCurrentPlayingPosition() {
		return currentPlayingPosition;
	}

	public void setCurrentPlayingPosition(int currentPlayingPosition) {
		this.currentPlayingPosition = currentPlayingPosition;
	}
	
}

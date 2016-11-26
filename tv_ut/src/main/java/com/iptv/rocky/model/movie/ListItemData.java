package com.iptv.rocky.model.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.data.VodChannel;
import com.iptv.rocky.base.BaseListItemData;
import com.iptv.rocky.view.movie.ListItemView;
import com.iptv.rocky.R;

public class ListItemData extends BaseListItemData {
	
	public VodChannel mVodInfo;

	private ListItemView mContentView;
	
	public void initView(VodChannel vodInfo,String columnCode) {
		mVodInfo = vodInfo;
		mContentView.initView(vodInfo,columnCode);
	}
	
	public void destroy() {
		mContentView.destroy();
	}
	
	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = (ListItemView)inflater.inflate(R.layout.movie_item_common, null);
		}
		return mContentView;
	}

}

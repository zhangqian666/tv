package com.iptv.rocky.model.voddetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.data.VodChannel;
import com.iptv.rocky.base.BaseListItemData;
import com.iptv.rocky.view.voddetail.DetailSimilarListItemView;
import com.iptv.rocky.R;

public class DetailSimilarListItemData extends BaseListItemData {
	
	public VodChannel mVodInfo;

	private DetailSimilarListItemView mContentView;
	
	public void initView(VodChannel vodInfo) {
		mVodInfo = vodInfo;
		mContentView.initView(vodInfo);
	}
	
	public void destroy() {
		mContentView.destroy();
	}
	
	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = (DetailSimilarListItemView)inflater.inflate(R.layout.vod_detail_similar_list_item, null);
		}
		return mContentView;
	}

}

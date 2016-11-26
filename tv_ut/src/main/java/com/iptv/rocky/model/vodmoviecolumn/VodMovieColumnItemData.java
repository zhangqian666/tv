package com.iptv.rocky.model.vodmoviecolumn;

import com.iptv.common.data.VodChannel;
import com.iptv.rocky.R;
import com.iptv.rocky.base.BaseListItemData;
import com.iptv.rocky.view.vodmoviecolumn.VodMovieColumnGridItemView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class VodMovieColumnItemData extends BaseListItemData {
	
	private VodMovieColumnGridItemView mContentView;
	
	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			mContentView = (VodMovieColumnGridItemView) LayoutInflater.from(context).inflate(R.layout.vod_movie_column_grid_item, null);
		}
		return mContentView;
	}
	
	public void setData(VodChannel baseInfo,String columnCode) {
		mContentView.fillViewData(baseInfo,columnCode);
	}
	
	public void destroy() {
		if (mContentView != null) {
			mContentView.destroy();
		}
	}
	
}

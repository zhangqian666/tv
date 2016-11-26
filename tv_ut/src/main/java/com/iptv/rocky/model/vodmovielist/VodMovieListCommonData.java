package com.iptv.rocky.model.vodmovielist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.view.vodmovielist.VodMovieListCommonView;
import com.iptv.rocky.R;

public class VodMovieListCommonData extends VodMovieListItemData {
	
	private VodMovieListCommonView mContentView;
	
	public boolean isFontBlod = false;
	
	@Override
	public View getView(Context context) {
		VodMovieListCommonView view = mContentView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = (VodMovieListCommonView) inflater.inflate(R.layout.vod_movie_list_page_item_common, null);
            view.initView(this);
            mContentView = view;
        }
        return view;
    }
	
}

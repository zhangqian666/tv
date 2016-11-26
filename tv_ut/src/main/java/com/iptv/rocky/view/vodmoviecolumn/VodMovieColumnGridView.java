package com.iptv.rocky.view.vodmoviecolumn;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.common.data.VodChannel;
import com.iptv.rocky.view.TvGridView;

public class VodMovieColumnGridView extends TvGridView {
	
	private TvGridViewAdapter adapter;
	
	public VodMovieColumnGridView(Context context) {
		this(context, null);
	}

	public VodMovieColumnGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public VodMovieColumnGridView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void createView(ArrayList<VodChannel> gridViewData,String columnCode){
		if (adapter == null) {
			adapter = new TvGridViewAdapter(getContext(), gridViewData,columnCode);
			setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
			mDataChanged = false;
		}
	}

	@Override
	protected void loadDatas() {
		if (callback != null) {
			callback.loadDatas();
		}
	}

	private CallBack callback;
	
	public interface CallBack{
		void loadDatas();
	}
	
	public void setCallback(CallBack callback) {
		this.callback = callback;
	}
	
}

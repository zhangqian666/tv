package com.iptv.rocky.view.voddetail;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.data.VodDetailInfo;
import com.iptv.rocky.model.voddetail.DetailViewPagerAdapter;
import com.iptv.rocky.R;

public class DetailViewPager extends ViewPager {
	
	private Context mContext;
	private LayoutInflater mInflater;
	
	private DetailMainView mainView;
	private DetailSimilarView similarView;

	public DetailViewPager(Context context) {
		this(context, null);
	}
	
	public DetailViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		mInflater = ((LayoutInflater)mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE));
	}
	
	public void destory() {
		if (mainView != null) {
			mainView.destory();
		}
		if (similarView != null) {
			similarView.destory();
		}
	}
	
	public void restart() {
		if (mainView != null) {
			mainView.restart();
		}
	}
	
	public void createView(VodDetailInfo info) {
		List<View> list = new ArrayList<View>(2);
		
		mainView = (DetailMainView) mInflater.inflate(R.layout.vod_detail_main, null, false);
		mainView.initView(info);
		
		//similarView = (DetailSimilarView) mInflater.inflate(R.layout.vod_detail_similar, null, false);
		// load similar data
		//similarView.load(VodChannelDetailActivity.channelId);
		
		list.add(0, mainView);
		//list.add(1, similarView);
		setAdapter(new DetailViewPagerAdapter(list));
	}
}

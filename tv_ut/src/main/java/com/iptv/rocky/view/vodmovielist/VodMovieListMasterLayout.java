package com.iptv.rocky.view.vodmovielist;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.xml.VodChannelFactory;
import com.iptv.rocky.model.vodmovielist.VodMovieListLayoutItem;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TitleView;
import com.iptv.rocky.R;

public class VodMovieListMasterLayout extends RelativeLayout
	implements ViewPager.OnPageChangeListener {
	
	private VodMovieListViewPager mPageView;
	
	private VodChannelFactory mVodChannelFactory;
	
	private String mTypeId;
	private String mName;
	private TitleView mTitleView;
	
	public VodMovieListMasterLayout(Context context) {
		this(context, null, 0);
	}
	
	public VodMovieListMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public VodMovieListMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mVodChannelFactory = new VodChannelFactory();
		mVodChannelFactory.setHttpEventHandler(liveLayoutHandler);
		mVodChannelFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
	}
	
	public void destroy() {
		mPageView.destroy();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTitleView = (TitleView) findViewById(R.id.vod_movie_list_title);
		mPageView = (VodMovieListViewPager) findViewById(R.id.vod_movie_list_viewpage);
		mPageView.setOnPageChangeListener(this);
	}
	
	public void createView(EnumType.Platform platform, String id, String name)
	{
		mTypeId = id;
		mName = name;
		mTitleView.setText(name);
		mVodChannelFactory.DownloadDatas(platform.toString(), id, 60, 0);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}
	
	@Override
	public void onPageSelected(int arg0) {
	}

	@Override
	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {
		return mPageView.requestFocus(direction, previouslyFocusedRect);
	}
	
	private HttpEventHandler<ArrayList<VodChannel>> liveLayoutHandler = new HttpEventHandler<ArrayList<VodChannel>>() {
		
		@Override
		public void HttpSucessHandler(ArrayList<VodChannel> result) {
			VodMovieListLayoutItem item = new VodMovieListLayoutItem();
			item.id = mTypeId;
			item.title = mName;
			ArrayList<VodMovieListLayoutItem> items = new ArrayList<VodMovieListLayoutItem>();
			items.add(item);
			mPageView.createView(items);
			mPageView.createView(0, result, true);
			mPageView.setCurrentItem(0);
		}
		
		@Override
		public void HttpFailHandler() {
			TvUtils.processHttpFail(getContext());
		}
	};
	
}

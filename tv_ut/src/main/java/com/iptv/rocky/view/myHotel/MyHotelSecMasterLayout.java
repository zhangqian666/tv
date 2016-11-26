package com.iptv.rocky.view.myHotel;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.MyHotelSec;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.xml.MyHotelSecFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.hotel.MyHotelSecLayoutItem;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TitleView;

public class MyHotelSecMasterLayout extends RelativeLayout implements ViewPager.OnPageChangeListener {
	
	private MyHotelSecViewPager mPageView;
	
	private String mTypeId;
	private String mName;
	private TitleView mTitleView;
	
	private MyHotelSecFactory mMyHotelSecFactory;
	
	public MyHotelSecMasterLayout(Context context) {
		this(context, null, 0);
	}
	
	public MyHotelSecMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MyHotelSecMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mMyHotelSecFactory = new MyHotelSecFactory();
		mMyHotelSecFactory.setHttpEventHandler(myhotelsecLayoutHandler);
		mMyHotelSecFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
	}
	
	public void destroy() {
		mPageView.destroy();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mTitleView = (TitleView) findViewById(R.id.vod_titleview);
		mPageView = (MyHotelSecViewPager)findViewById(R.id.live_viewpage);
		
		mPageView.setOnPageChangeListener(this);
	}
	
	public void createView(String id, String name)
	{
		mTypeId = id;
		mName = name;
		
		mTitleView.setText(name);
		
		mMyHotelSecFactory.DownloadDatas(TvApplication.account,TvApplication.language,id);
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
	
	private HttpEventHandler<ArrayList<MyHotelSec>> myhotelsecLayoutHandler = new HttpEventHandler<ArrayList<MyHotelSec>>() {
		
		@Override
		public void HttpSucessHandler(ArrayList<MyHotelSec> result) {
			MyHotelSecLayoutItem item = new MyHotelSecLayoutItem();
			item.id = mTypeId;
			item.title = mName;
			ArrayList<MyHotelSecLayoutItem> items = new ArrayList<MyHotelSecLayoutItem>();
			items.add(item);
			mPageView.createView(items);

			mPageView.createView(0, result, true);
			
			mPageView.setCurrentItem(0);
			
			//requestChildFocus(mPageView, mPageView.findFocus());
		}
		
		@Override
		public void HttpFailHandler() {
			TvUtils.processHttpFail(getContext());
		}
	};
	
}

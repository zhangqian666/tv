package com.iptv.rocky.view.myHotel;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.MyHotelPicture;
import com.iptv.common.data.MyHotelPictureList;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.xml.MyHotelPictureFactory;
import com.iptv.rocky.model.hotel.MyHotelPictureListLayoutItem;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TitleView;
import com.iptv.rocky.R;

public class MyHotelPictureListMasterLayout extends RelativeLayout implements OnPageChangeListener {
	
	//private MyHotelPictureListViewPager mPageView;
	private MyHotelPictureFactory mMyHotelPictureFactory;
	
	private ViewPager viewPager;  
	
	private String mTypeId;
	//private String mName;
	//private TitleView mTitleView;
	
	public MyHotelPictureListMasterLayout(Context context) {
		this(context, null, 0);
	}
	
	public MyHotelPictureListMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MyHotelPictureListMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mMyHotelPictureFactory = new MyHotelPictureFactory();
		mMyHotelPictureFactory.setHttpEventHandler(liveLayoutHandler);
		mMyHotelPictureFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		
		
	}
	
	public void destroy() {
		//mPageView.destroy();
		
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		//viewPager.setAdapter(new PictureAdapter());
		//viewPager.setOnPageChangeListener(this);
		
		
		//mTitleView = (TitleView) findViewById(R.id.my_hotel_picture_list_title);
		//mPageView = (MyHotelPictureListViewPager) findViewById(R.id.my_hotel_picture_list_viewpage);
		
		//mPageView.setOnPageChangeListener(this);
	}
	
	public void createView(String id, String name)
	{
		mTypeId = id;
		//mName = name;
		//mTitleView.setText(name);
		mMyHotelPictureFactory.DownloadDatas(id, 60, 0);
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

/*	@Override
	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {
		return mPageView.requestFocus(direction, previouslyFocusedRect);
	}*/
	
	private HttpEventHandler<ArrayList<MyHotelPicture>> liveLayoutHandler = new HttpEventHandler<ArrayList<MyHotelPicture>>() {
		
		@Override
		public void HttpSucessHandler(ArrayList<MyHotelPicture> result) {
			MyHotelPictureListLayoutItem item = new MyHotelPictureListLayoutItem();
			item.id = mTypeId;
			//item.title = mName;
			//LogUtils.info("Result 大小："+result.size());
			ArrayList<MyHotelPictureListLayoutItem> items = new ArrayList<MyHotelPictureListLayoutItem>();
			items.add(item);
			//mPageView.createView(items);
			//mPageView.createView(0, items, true);
			//mPageView.setCurrentItem(0);
			
			
			viewPager.setCurrentItem(items.size()*100);
		}
		
		@Override
		public void HttpFailHandler() {
			TvUtils.processHttpFail(getContext());
		}
	};
	
}

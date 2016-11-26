package com.iptv.rocky.view.vodsec;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.VodSec;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.xml.VodSecFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.vodsec.VodSecLayoutItem;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TitleView;
import com.iptv.rocky.R;

public class VodSecMasterLayout extends RelativeLayout implements ViewPager.OnPageChangeListener {
	
	private VodSecViewPager mPageView;
	
	private String mTypeId;
	private String mName;
	private TitleView mTitleView;
	
	private VodSecFactory mVodSecFactory;
	
	public VodSecMasterLayout(Context context) {
		this(context, null, 0);
	}
	
	public VodSecMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public VodSecMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mVodSecFactory = new VodSecFactory();
		mVodSecFactory.setHttpEventHandler(vodsecLayoutHandler);
		mVodSecFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
	}
	
	public void destroy() {
		mPageView.destroy();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mTitleView = (TitleView) findViewById(R.id.vod_titleview);
		mPageView = (VodSecViewPager)findViewById(R.id.live_viewpage);
		mPageView.setOnPageChangeListener(this);
	}
	
	public void createView(String id, String name)
	{
		mTypeId = id;
		mName = name;
		mTitleView.setText(name);
		mVodSecFactory.DownloadDatas(TvApplication.account,TvApplication.language,id);
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
	
	private HttpEventHandler<ArrayList<VodSec>> vodsecLayoutHandler = 
			new HttpEventHandler<ArrayList<VodSec>>() {
		
		@Override
		public void HttpSucessHandler(ArrayList<VodSec> result) {
			VodSecLayoutItem item = new VodSecLayoutItem();
			item.id = mTypeId;
			item.title = mName;
			ArrayList<VodSecLayoutItem> items = new ArrayList<VodSecLayoutItem>();
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

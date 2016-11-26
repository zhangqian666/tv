package com.iptv.rocky.view.live;

import java.util.ArrayList;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.data.PortalLiveType;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.json.AllChannelProgBilJsonlFactory;
import com.iptv.rocky.hwdata.local.LiveTypeLocalFactory;
import com.iptv.rocky.hwdata.xml.AllChannelProgBillFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.live.LiveTypeLayoutItem;
import com.iptv.rocky.view.TabView;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * 直播的列表页面
 */
public class LiveMasterLayout extends RelativeLayout implements ViewPager.OnPageChangeListener, TabView.SelectChangedListener {
	
	private TabView mTabView;
	private LiveTypeViewPager mPageView;
	private ProgressBar mProgressBar;

	private ArrayList<PortalLiveType> mLstLiveType;
	private ArrayList<LiveTypeLayoutItem> mLstLiveTypeLayout;
	
	private int mTypeId;
	
	public LiveMasterLayout(Context context) {
		this(context, null, 0);
	}
	
	public LiveMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public LiveMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void destroy() {
		mPageView.destroy();
	}
	
	public void resume() {
		mTabView.autoFocusStart();
	}
	
	public void pause() {
		mTabView.autoFocusStop();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mTabView = (TabView) findViewById(R.id.tv_tablayout);
		mPageView = (LiveTypeViewPager) findViewById(R.id.live_viewpage);
		mProgressBar = (ProgressBar) findViewById(R.id.tv_progressbar);
		mTabView.setSelectChanngeListener(this);
		mPageView.setOnPageChangeListener(this);
	}
	
	public void createView(int id) {
		LogUtils.debug("送进来的ID："+id);
		mTypeId = id;
		
		init();
	}

	@Override
	public void onChange(int index) {
		if (mPageView.getCurrentItem() != index) {
			mPageView.setCurrentItem(index);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		mTabView.tabSelected(arg0);
	}

	@Override
	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {
		return mPageView.requestFocus(direction, previouslyFocusedRect);
	}
	
	public void init() {
		LiveTypeLocalFactory liveTypeLocalFactory = new LiveTypeLocalFactory(getContext());
		mLstLiveType = liveTypeLocalFactory.getLiveType();
		mLstLiveTypeLayout = new ArrayList<LiveTypeLayoutItem>();
		LogUtils.debug("mLstLiveType size:"+mLstLiveType.size());
		for (PortalLiveType livetype : mLstLiveType) {
			LiveTypeLayoutItem layout = new LiveTypeLayoutItem();
			layout.id = livetype.typeId;
//			LogUtils.info("livetype.typeId===="+livetype.typeId);
			layout.title = livetype.title;
//			LogUtils.info("livetype.title===="+livetype.title);
			mLstLiveTypeLayout.add(layout);
		}
		
		mTabView.createView(mLstLiveTypeLayout);
		mPageView.createView(mLstLiveTypeLayout);
		
		new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
            	for (int i = 0; i < mLstLiveTypeLayout.size(); i++)	{
        			mPageView.createView(i, getFromDb(i+1), mTypeId-1 == i ? true : false);
        		}
        		mPageView.setCurrentItem(mTypeId-1);
        		
        		requestChildFocus(mPageView, mPageView.findFocus());
        		
            	mProgressBar.setVisibility(View.GONE);
            }
        }, 3000);
	}
	
	private ArrayList<LiveChannelBill> getFromDb(int nTypeId) {
		ArrayList<LiveChannelBill> result = new ArrayList<LiveChannelBill>();
		
		for (PortalLiveType liveType : mLstLiveType)
		{
//			LogUtils.debug("liveType.typeId:"+liveType.typeId+"  ;nTypeId:"+nTypeId+";  liveType.lstChannelIds:"+liveType.lstChannelIds.size());
			if (liveType.typeId == nTypeId)
			{
				for (String channelid : liveType.lstChannelIds)
				{
					
					if (TvApplication.platform==EnumType.Platform.ZTE) {
						if (AllChannelProgBilJsonlFactory.mapLiveChannelBillZte != null) {
							if (AllChannelProgBilJsonlFactory.mapLiveChannelBillZte.containsKey(channelid)) {
								LiveChannelBill bill = AllChannelProgBilJsonlFactory.mapLiveChannelBillZte.get(channelid);
								bill.ChannelLogoURL = String.format("assets://icon/live_%s.jpg", channelid);
								result.add(bill);
							} else {
								LogUtils.error("节目单频道列表中不包含："+ channelid+",新建一个");
							}
						} else {
							LogUtils.error("mapLiveChannelBillZte 为 null");
						}
					}else{
						if (AllChannelProgBillFactory.mapLiveChannelBill != null) {
							if (AllChannelProgBillFactory.mapLiveChannelBill.containsKey(channelid)) {
								LiveChannelBill bill = AllChannelProgBillFactory.mapLiveChannelBill.get(channelid);
								//bill.ChannelName = HomeActivity.mapLiveChannel.get(channelid).ChannelName;
								bill.ChannelLogoURL = String.format("assets://icon/live_%s.jpg", channelid);
								result.add(bill);
							} else {
								//LogUtils.error("节目单频道列表中不包含："+ channelid+",新建一个");
							}
						} else {
							//LogUtils.error("mapLiveChannelBill 为 null");
						}
					}
					
				}
				break;
			}
		}
		return result;
	}
	
}

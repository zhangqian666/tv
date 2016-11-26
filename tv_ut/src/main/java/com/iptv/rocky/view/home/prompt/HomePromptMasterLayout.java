package com.iptv.rocky.view.home.prompt;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.HomePrompt;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.HomePromptJsonFactory;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TabView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class HomePromptMasterLayout extends RelativeLayout
	implements ViewPager.OnPageChangeListener, TabView.SelectChangedListener {
	
	
	//电子公告的信息
	private HomePromptViewFlipper mHomePromptViewFlipper;
	// 读取配置文件布局首页
	private HomePromptJsonFactory homePromptJsonFactory;
	private List<HomePrompt> mHomePromptList;
	
	private ImageLoadingListener bgImageLoadingListener;
	
	// 启动屏幕保护
	private Handler handlerRefreshDashboard = new Handler();
	private Handler handlerChangePage = new Handler();
	
	public HomePromptMasterLayout(Context context) {
		this(context, null, 0);
	}
	
	public HomePromptMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HomePromptMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void destroy() {
		//mPageView.destroy();
	}
	
	public void resume() {
		//mTabView.autoFocusStart();
	}
	
	public void pause() {
		//mTabView.autoFocusStop();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mHomePromptViewFlipper = (HomePromptViewFlipper)findViewById(R.id.dashboard_viewpage);
	}
	
	public void downloadDatas() {
		homePromptJsonFactory = new HomePromptJsonFactory();
		homePromptJsonFactory.setHttpEventHandler(homePromptHandler);
		homePromptJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		homePromptJsonFactory.DownloadDatas();
		LogUtils.debug("开始下载数据！");
	}

	@Override
	public void onChange(int index) {
		LogUtils.error("OnChange:"+index);
		/*if (mPageView.getCurrentItem() != index) {
			mPageView.setCurrentItem(index);
		}*/
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		LogUtils.error("onPageSelected : arg0:"+ arg0);
		//mTabView.tabSelected(arg0);
	}

/*	@Override
	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {
		return mPageView.requestFocus(direction, previouslyFocusedRect);
	}*/

	
	private HttpEventHandler<List<HomePrompt>> homePromptHandler = new HttpEventHandler<List<HomePrompt>>() {
		@Override
		public void HttpSucessHandler(List<HomePrompt> result) {
			
			mHomePromptList = result;
			
			mHomePromptViewFlipper.setInfos(mHomePromptList);
			
			mHomePromptViewFlipper.setAdapter(mHomePromptViewFlipper.adapter);
	    	mHomePromptViewFlipper.refreshDrawableState();
			
	    	/*if(mHomePromptList.size()>1){
		    	handlerRefreshDashboard.removeCallbacks(runnableRefreshDashboardInfo);
		    	
		    	handlerRefreshDashboard.postDelayed(runnableRefreshDashboardInfo, 60000);
		    	handlerChangePage.postDelayed(runnableChangePage, 10000);
	    	}else{
	    		LogUtils.debug("只有一张图片");
	    	}*/
	    	

		}

		@Override
		public void HttpFailHandler() {
			TvUtils.processHttpFail(getContext());
		}
	};
	
	/**
	 *  定时更换信息
	 */
	Runnable runnableRefreshDashboardInfo = new Runnable() {
		@Override
		public void run() {
			LogUtils.debug("定时更新数据");
			//stopFlipping();
			homePromptJsonFactory.DownloadDatas();
		}
	};
	
	
	/**
	 *  定时更换信息
	 */
	Runnable runnableChangePage = new Runnable() {
		@Override
		public void run() {
			handlerChangePage.removeCallbacks(runnableChangePage);
			
			mHomePromptViewFlipper.showNext();
			handlerChangePage.postDelayed(runnableChangePage, 10000);
		}
	};
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		
		//mHomePromptViewFlipper.showNext();
		
		/*switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if(mBtnEnglish.isFocused()){
					mBtnChinese.requestFocus();
				}
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if(mBtnChinese.isFocused()){
					mBtnEnglish.requestFocus();
				}
			case KeyEvent.KEYCODE_DPAD_CENTER:
				
				break;
			default:
				break;
		}*/
		return true;
	}
	
}

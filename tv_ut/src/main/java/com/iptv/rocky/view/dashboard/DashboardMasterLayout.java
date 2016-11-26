package com.iptv.rocky.view.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.DashboardInfo;
import com.iptv.common.data.DashboardPicture;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.DashboardJsonFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TabView;

/************************************
 * 
 * @author 赵小强
 * 2016-02-12
 * 
 ************************************/
public class DashboardMasterLayout extends RelativeLayout implements ViewPager.OnPageChangeListener, TabView.SelectChangedListener {
	
	//电子公告的信息
	private DashboardViewFlipper mDashboardViewFlipper; 
	
	// private DashboardVideoView mDashboardVideoView;
 
	// 读取配置文件布局首页
	// private DashboardJsonFactory dashboardFactory;
	private ArrayList<DashboardInfo> infos;
	

	private Handler handlerChangePage = new Handler();
	
	public DashboardMasterLayout(Context context) {
		this(context, null, 0);
	}
	
	public DashboardMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public DashboardMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void destroy(){
	}
	
	public void resume(){
	}
	
	public void pause(){
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// 图片显示
		mDashboardViewFlipper = (DashboardViewFlipper)findViewById(R.id.dashboard_viewpage);
		// 视频显示
		//mDashboardVideoView = (DashboardVideoView) findViewById(R.id.dashboard_video_play_view);
	}
	
	/**
	 * 下载公告信息
	 *//*
	public void DownloadDatas() {
		dashboardFactory = new DashboardJsonFactory();
		dashboardFactory.setHttpEventHandler(dashBoardHandler);
		dashboardFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		dashboardFactory.DownloadDatas();
	}*/

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
		
	}
	
	private HttpEventHandler<ArrayList<DashboardInfo>> dashBoardHandler = new HttpEventHandler<ArrayList<DashboardInfo>>() {
		@Override
		public void HttpSucessHandler(ArrayList<DashboardInfo> result) {
			
			int size = result.size();
			LogUtils.debug("公告数据的尺寸:"+size);
	    	infos = result;
	    	
	    	
	    	List<DashboardPicture> pictureListOutput = new ArrayList<DashboardPicture>();
	    	
	    	List<String> videoUrls = new ArrayList<String>();
	    	
	    	String type = null;
	    	
	    	for(DashboardInfo info:infos){
	    		LogUtils.debug("消息类型:"+info.getType()+ " ,内容消息类型:"+info.getContentType());
	    		if(info.getContentType() != null){
	    			if(info.getContentType().equals("PICTURE")){
		    			List<DashboardPicture> pictures = info.getImage();
			    		if(pictures != null){
			    			for(DashboardPicture picture:pictures){
			    				LogUtils.debug("图片:"+picture.getBackgroundImage());
				    			pictureListOutput.add(picture);
				    			type = "PICTURE";
				    		}
			    		}
		    		}else if(info.getContentType().equals("VIDEO")){
		    			videoUrls.add(info.getPlayUrl());
		    			type = "VIDEO";
		    		}
	    		}
	    	}
	    	
	    	if(infos.size()>0){
	    		if(type.equals("PICTURE")){
	    			//mDashboardVideoView.setVisibility(View.GONE);
	    			mDashboardViewFlipper.setVisibility(View.VISIBLE);
			    	mDashboardViewFlipper.setInfos(pictureListOutput);
			    	//handlerRefreshDashboard.removeCallbacks(runnableRefreshDashboardInfo);
			    	mDashboardViewFlipper.setAdapter(mDashboardViewFlipper.adapter);
			    	mDashboardViewFlipper.refreshDrawableState();
			    	//handlerRefreshDashboard.postDelayed(runnableRefreshDashboardInfo, 60000);
			    	handlerChangePage.postDelayed(runnableChangePage, 10000);
			    	TvApplication.status ="SIGNAGE_PICTURE";
			    	
		    	}else if(type.equals("VIDEO")){
		    		
		    		mDashboardViewFlipper.setVisibility(View.GONE);
		    		/*mDashboardVideoView.setPlayUrlList(videoUrls);
		    		mDashboardVideoView.setVisibility(View.VISIBLE);
		    		mDashboardVideoView.beginPlay();*/
		    		TvApplication.status ="SIGNAGE_VIDEO";
		    	}
	    	}else{
	    		//做没有信息的提示，或者就什么都不显示，一般不应该出现这个，因为每个点都得配置最起码一个默认任务
	    		LogUtils.error("服务器没有这个点的公告数据。数据尺寸为0");
	    		//mDashboardVideoView.setVisibility(View.GONE);
	    		mDashboardViewFlipper.setInfos(new ArrayList<DashboardPicture>());
    			mDashboardViewFlipper.setVisibility(View.GONE);
	    		//handlerRefreshDashboard.postDelayed(runnableRefreshDashboardInfo, 60000);
	    		TvApplication.status ="SIGNAGE_NO_DATA";
	    	}
		}

		@Override
		public void HttpFailHandler() {
			TvUtils.processHttpFail(getContext());
		}
	};
	
	/**
	 * 
	 */
	public void beginShowPictures(){
		
		List<DashboardPicture> pictureListOutput = new ArrayList<DashboardPicture>();
    	List<String> videoUrls = new ArrayList<String>();
    	String type = null;
    	
    	for(DashboardInfo info:infos){
    		LogUtils.debug("消息类型:"+info.getType()+ " ,内容消息类型:"+info.getContentType());
    		if(info.getContentType() != null){
    			
    			List<DashboardPicture> pictures = info.getImage();
	    		if(pictures != null){
	    			for(DashboardPicture picture:pictures){
	    				LogUtils.debug("图片:"+picture.getBackgroundImage());
		    			pictureListOutput.add(picture);
		    			type = "PICTURE";
		    		}
	    		}
	    		
    		}
    	}
		
		if(infos.size()>0){
    			mDashboardViewFlipper.setVisibility(View.VISIBLE);
		    	mDashboardViewFlipper.setInfos(pictureListOutput);
		    	mDashboardViewFlipper.setAdapter(mDashboardViewFlipper.adapter);
		    	mDashboardViewFlipper.refreshDrawableState();
		    	handlerChangePage.postDelayed(runnableChangePage, 10000);
		    	TvApplication.status ="SIGNAGE_PICTURE";
		    
    	}else{
    		//做没有信息的提示，或者就什么都不显示，一般不应该出现这个，因为每个点都得配置最起码一个默认任务
    		LogUtils.error("服务器没有这个点的公告数据。数据尺寸为0");
    		mDashboardViewFlipper.setInfos(new ArrayList<DashboardPicture>());
			mDashboardViewFlipper.setVisibility(View.GONE);
    		TvApplication.status ="SIGNAGE_NO_DATA";
    	}
	}
	
	
	/**
	 *  定时更换信息
	 */
	Runnable runnableChangePage = new Runnable() {
		@Override
		public void run() {
			handlerChangePage.removeCallbacks(runnableChangePage);
			LogUtils.debug("更换图片:当前位置:"+mDashboardViewFlipper.getPositionForView(DashboardMasterLayout.this));
			
			mDashboardViewFlipper.showNext();
			handlerChangePage.postDelayed(runnableChangePage, 10000);
		}
	};

	public ArrayList<DashboardInfo> getInfos() {
		return infos;
	}

	public void setInfos(ArrayList<DashboardInfo> infos) {
		this.infos = infos;
	}
	
}

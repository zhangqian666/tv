package com.iptv.rocky.tcl.view.live;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.Advertisement;
import com.iptv.common.data.AdvertisementImageInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.json.AdvertisementFactory;
import com.iptv.rocky.hwdata.json.WelcomeInfoFactory;

public class AdvertisementView extends Fragment{

	private AdvertisementFactory mAdvertisementFactory;
	
	
	//数字频道切换的处理
	private Handler handlerChangeImage = new Handler();
	
	
	/** Fragment第一次附属于Activity时调用,在onCreate之前调用 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//mStartPayOrderedPackageListener = (StartPayOrderedPackageListener)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mAdvertisementFactory = new AdvertisementFactory();
		mAdvertisementFactory.setHttpEventHandler(advertisementHandler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.vod_play_advertisement,container, false);

		Bundle bundle = getArguments();
		
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdvertisementFactory.DownloadDatas();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	/*@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mPager = (ViewPager) this.findViewById(R.id.screen_protect_view_pager);
		
		mAdvertisementFactory = new AdvertisementFactory();
		mAdvertisementFactory.setHttpEventHandler(advertisementHandler);
		mAdvertisementFactory.DownloadDatas();
	}	*/
	
	
	/**
	 * 接收屏保数据
	 */
	private HttpEventHandler<Advertisement> advertisementHandler = new HttpEventHandler<Advertisement>() {

		@Override
		public void HttpSucessHandler(Advertisement result) {
	
			int i=0;
			for(AdvertisementImageInfo info: result.getImages()){
				if(info != null){

				}
				i++;
			} 
	        
		}

		@Override
		public void HttpFailHandler() {
			LogUtils.error("下载屏保数据错误");
		}
	};
	
	/*private Runnable runnableChangePage = new Runnable() {
		@Override
		public void run() {
			if(bottomImageList.size()>0){
				if(bottomImageList.size()>1){
					if(currentItem <(bottomImageList.size()-1)){
						currentItem +=1;
					}else{
						currentItem =0;
					}
					mPager.setCurrentItem(currentItem);
					handlerChangeImage.removeCallbacks(runnableChangePage);
					if(infoList.get(currentItem).duration == 0){
						handlerChangeImage.postDelayed(runnableChangePage, 10000);
					}else{
						handlerChangeImage.postDelayed(runnableChangePage, infoList.get(currentItem).duration*1000);
					}
				}
			}else{
				
			}
		}
	};*/
	
	
	
}

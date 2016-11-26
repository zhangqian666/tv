package com.iptv.rocky.view.home.prompt;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.common.data.HomePrompt;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


/**
 * 公告信息的具体显示页，本页为图片类的
 *
 */
public class HomePromptPictureView extends RelativeLayout implements ImageLoadingListener{

	
	// 定时退出开机提示
	private Handler handlerExitPage = new Handler();
    private ImageLoader bgImageLoader;
    private HomePrompt mHomePrompt;
    
    private TextView timeDispText;
    private int timeLeft;
    
	public HomePromptPictureView(Context context) {
		super(context);
	}

	public void createView(HomePrompt mHomePrompt) {
		
		this.mHomePrompt = mHomePrompt;
		
		timeLeft = mHomePrompt.getDuration();
		
        bgImageLoader = ImageLoader.getInstance();
		bgImageLoader.loadImage(mHomePrompt.getBackgroundImage(), HomePromptPictureView.this);
		
/*		RelativeLayout.LayoutParams  layoutParam = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		layoutParam.setMargins(1400, 950, 0, 0);
		timeDispText = new TextView(HomePromptPictureView.this.getContext());
		timeDispText.setTextColor(getResources().getColor(R.color.white));
		timeDispText.setTextSize(40);
		timeDispText.setText(Integer.toString(timeLeft));
		
		timeDispText.setGravity(Gravity.CENTER);
		HomePromptPictureView.this.addView(timeDispText,layoutParam);*/
	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}	

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		
		
	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		LogUtils.error("图片下载完毕");
		this.setBackground(new BitmapDrawable(this.getResources(),arg2));
		/*if(handlerExitPage.hasCallbacks(runnableRefreshTimeInfo)){
			handlerExitPage.removeCallbacks(runnableRefreshTimeInfo);
		}
		handlerExitPage.postDelayed(runnableRefreshTimeInfo, 1000);*/
				
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		
	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {

	}
	
	
	/**
	 *  定时更换信息
	 */
	Runnable runnableRefreshTimeInfo = new Runnable() {
		@Override
		public void run() {
			LogUtils.debug("定时更新数据");
			//stopFlipping();
			if(timeLeft>0){
				timeDispText.setText(Integer.toString(timeLeft));
				timeLeft -= 1;
			}else{
				handlerExitPage.removeCallbacks(runnableRefreshTimeInfo);
			}
		}
	};
}

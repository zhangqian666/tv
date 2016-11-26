package com.iptv.rocky.view.splash;

import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.GuestInfoToClient;
import com.iptv.common.data.WelcomeInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.WelcomeInfoFactory;
import com.iptv.rocky.model.TvApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class WelcomeFragment extends Fragment{

    //private static final String ACTION_STARTLOGINHOTELSERVER ="com.virgintelecom.action.STARTLOGINHOTELSERVER";
	
	private Button mBtnChinese;
	private Button mBtnEnglish;
	private Button mBtnEnter;
	private ImageView chooseLanguageIv_chAndEn;
	private ImageView chooseLanguageIv;
	
	private View mMain;
	private ImageView welcomeText;
	private ProgressBar mProgressBar;
	
	private ChooseLanguageListener mStartLoginHotelServerListener;
	private WelcomeInfoFactory welcomeInfoFactory;
	
	private WelcomeInfo welcomeInfo;
	
	private BitmapDrawable bitmapChinese;
	private BitmapDrawable bitmapEnglish;
	
	
	private ImageLoadingListener bgImageLoadingListener;
	

	private ImageView promptImage; //促销提示
	private boolean showPrompt =false;
	
	private TextView guestNameTextView;
	private TextView welcomeTextBodyTextView;
	
	private String textChinese;
	private String textEnglish;
	private String formatCn = "尊敬的 %s:";
	private String formatEn ="Dear %s,";
	
	 /** Fragment第一次附属于Activity时调用,在onCreate之前调用 */  
    @Override  
    public void onAttach(Activity activity)
    {  
        super.onAttach(activity);  
        mStartLoginHotelServerListener = (ChooseLanguageListener) activity;   
    }  

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		welcomeInfoFactory = new WelcomeInfoFactory();
        welcomeInfoFactory.setHttpEventHandler(welcomeHandler);
        welcomeInfoFactory.DownloadDatas();
    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,   Bundle savedInstanceState){
		
		welcomeInfo = new WelcomeInfo();
		
		mMain = inflater.inflate(R.layout.fragment_choose_language, container,false);
		
		mMain.setVisibility(View.INVISIBLE);
		
		//backgroundImage = (AsyncImageView) mMain.findViewById(R.id.welcomeImageBottom);
		
		mProgressBar = (ProgressBar) mMain.findViewById(R.id.tv_progressbar);
		mProgressBar.setVisibility(View.GONE);
		
		welcomeText = (ImageView) mMain.findViewById(R.id.welcome_text);
		promptImage = (ImageView) mMain.findViewById(R.id.promptImageBottom);
		guestNameTextView = (TextView) mMain.findViewById(R.id.welcome_text_guestname);
		welcomeTextBodyTextView = (TextView) mMain.findViewById(R.id.welcome_text_body);
		
		textChinese ="欢迎您选择并下榻石家庄希尔顿酒店，我们将竭诚为您服务并为您带来真正难忘的入住体验。\n祝您和家人心想事成，一切顺利！\n\n刘浩然\n石家庄希尔顿酒店总经理 ";
		textEnglish ="On behalf of all team members of Hilton Shijiazhuang, I just express my warm welcome to your stay. We will try our best to provide you the best service to give you the perfect experience at Hilton Shijiazhuang. Hope everything goes well as you wish.\n\nAdrian Liu\nGeneral Manager Hilton Shijiazhuang";

		LogUtils.error("欢迎界面内添加顾客信息，顾客信息大小："+TvApplication.guests.size());
		chooseLanguageIv_chAndEn=(ImageView) mMain.findViewById(R.id.chooseLanguageIv_chAndEn);
		chooseLanguageIv=(ImageView) mMain.findViewById(R.id.chooseLanguageIv);
		
		mBtnChinese = (Button) mMain.findViewById(R.id.chinese);
		mBtnChinese.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mProgressBar.setVisibility(View.VISIBLE);
				updateLange(Locale.SIMPLIFIED_CHINESE);
				TvApplication.language = IPTVUriUtils.SIMPLIFIED_CHINESE_CODE;
				
				LogUtils.error("showPrompt :"+showPrompt);
				
				if(showPrompt){
					promptImage.setVisibility(View.VISIBLE);
				}else{
					mStartLoginHotelServerListener.chooseLanguage(1);
				}
			}
		});
	
		mBtnChinese.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					//Animation fade_out = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_out_10);
					//ceoImage.startAnimation(fade_out);
					
					
					if(TvApplication.guests.size()>0){
						welcomeText.setVisibility(View.GONE);
						welcomeTextBodyTextView.setVisibility(View.VISIBLE);
						guestNameTextView.setVisibility(View.VISIBLE);
						String textName = "";
						welcomeTextBodyTextView.setText(welcomeInfo.getWelcomeChineseText());
						welcomeTextBodyTextView.setTextSize((int)mMain.getResources().getDimension(R.dimen.p_45));
						
						for(GuestInfoToClient  guest:TvApplication.guests ){
							textName += guest.getName();
							if(guest.getSex() ==0){
								textName +="先生 ";
							}else if(guest.getSex() ==1){
								textName +="女士 ";
							}
						}
						guestNameTextView.setText(String.format(formatCn, textName));
					}else{
						// String textName = "";
						// textName ="宾客";
						welcomeText.setVisibility(View.VISIBLE);
						
						welcomeTextBodyTextView.setVisibility(View.GONE);
						guestNameTextView.setVisibility(View.GONE);
						
						//LogUtils.error("topImageChinese是否为空?"+(topImageChinese == null));
						if(bitmapChinese != null){
							welcomeText.setImageDrawable(bitmapChinese);
						}else{
							//welcomeText.setBackgroundResource(R.drawable.zhici_cn);
						}
					}

					//welcomeText.setBackgroundResource(R.drawable.zhici_cn);
					//Animation left_in_animation = AnimationUtils.loadAnimation(v, R.anim.left_in);
					//welcomeText.startAnimation(left_in_animation);
					
					//Animation fade_in = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_in_10);
					//ceoImage.startAnimation(fade_in);
				} else {
					//Animation left_out_animation = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.left_out);
					//welcomeText.startAnimation(left_out_animation);
				}
			}
		});
		
		mBtnEnglish = (Button) mMain.findViewById(R.id.english);
		mBtnEnglish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				mProgressBar.setVisibility(View.VISIBLE);
				updateLange(Locale.ENGLISH);
				TvApplication.language = IPTVUriUtils.ENGLISH_CODE;
				
				LogUtils.error("showPrompt :"+showPrompt);
				if(showPrompt){
					promptImage.setVisibility(View.VISIBLE);
				}else{
					mStartLoginHotelServerListener.chooseLanguage(1);
				}
			}
		});

		mBtnEnglish.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					//Animation fade_out = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_out_10);
					//ceoImage.startAnimation(fade_out);
					//LogUtils.error("topImageEnglish是否为空?"+(topImageEnglish == null));
					
					
					
					
					if(TvApplication.guests.size()>0){
						welcomeText.setVisibility(View.GONE);
						welcomeTextBodyTextView.setVisibility(View.VISIBLE);
						guestNameTextView.setVisibility(View.VISIBLE);
						String textName = "";
						welcomeTextBodyTextView.setText(welcomeInfo.getWelcomeEnglishText());
						welcomeTextBodyTextView.setTextSize((int)mMain.getResources().getDimension(R.dimen.p_45));
						
						for(GuestInfoToClient  guest:TvApplication.guests ){
							
							if(guest.getSex() ==0){
								textName +="Mr. ";
							}else if(guest.getSex() ==1){
								textName +="Ms. ";
							}
							textName += guest.getName();
						}
						guestNameTextView.setText(String.format(formatEn, textName));
					}else{
						welcomeText.setVisibility(View.VISIBLE);
						welcomeTextBodyTextView.setVisibility(View.GONE);
						guestNameTextView.setVisibility(View.GONE);
						
						//LogUtils.error("topImageChinese是否为空?"+(topImageChinese == null));
						if(bitmapChinese != null){
							welcomeText.setImageDrawable(bitmapEnglish);
							//welcomeText.setImageDrawable(topImageEnglish.getDrawable());
						}else{
							//welcomeText.setBackgroundResource(R.drawable.zhici_cn);
						}
					}
					
					
					//welcomeText.setBackgroundResource(R.drawable.zhici_en);
					//Animation left_in_animation = AnimationUtils.loadAnimation(this, R.anim.left_in);
					//welcomeText.startAnimation(left_in_animation);
					
					//Animation fade_in = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_in_10);
					//ceoImage.startAnimation(fade_in);
				} else {
					//Animation left_out_animation = AnimationUtils.loadAnimation(this, R.anim.left_out);
					//welcomeText.startAnimation(left_out_animation);
				}
			}
		});
		
		mBtnEnter = (Button) mMain.findViewById(R.id.enter);
		mBtnEnter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mProgressBar.setVisibility(View.VISIBLE);
				updateLange(Locale.SIMPLIFIED_CHINESE);
				TvApplication.language = IPTVUriUtils.SIMPLIFIED_CHINESE_CODE;
				
				LogUtils.error("showPrompt :"+showPrompt);
				
				if(showPrompt){
					promptImage.setVisibility(View.VISIBLE);
				}else{
					mStartLoginHotelServerListener.chooseLanguage(1);
				}
			}
		});

		mBtnEnter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					//Animation fade_out = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_out_10);
					//ceoImage.startAnimation(fade_out);
					
					
					if(TvApplication.guests.size()>0){
						welcomeText.setVisibility(View.GONE);
						welcomeTextBodyTextView.setVisibility(View.VISIBLE);
						guestNameTextView.setVisibility(View.VISIBLE);
						String textName = "";
						welcomeTextBodyTextView.setText(welcomeInfo.getWelcomeChineseText());
						LogUtils.error("welcomeInfo.getWelcomeChineseText()----->  "+welcomeInfo.getWelcomeChineseText());
						welcomeTextBodyTextView.setTextSize((int)mMain.getResources().getDimension(R.dimen.p_45));
						
						for(GuestInfoToClient  guest:TvApplication.guests ){
							textName += guest.getName();
							if(guest.getSex() ==0){
								textName +="先生 ";
							}else if(guest.getSex() ==1){
								textName +="女士 ";
							}
						}
						guestNameTextView.setText(String.format(formatCn, textName));
					}else{
						// String textName = "";
						// textName ="宾客";
						welcomeText.setVisibility(View.VISIBLE);
						
						welcomeTextBodyTextView.setVisibility(View.GONE);
						guestNameTextView.setVisibility(View.GONE);
						
						//LogUtils.error("topImageChinese是否为空?"+(topImageChinese == null));
						if(bitmapChinese != null){
							welcomeText.setImageDrawable(bitmapChinese);
						}else{
							//welcomeText.setBackgroundResource(R.drawable.zhici_cn);
						}
					}

					//welcomeText.setBackgroundResource(R.drawable.zhici_cn);
					//Animation left_in_animation = AnimationUtils.loadAnimation(v, R.anim.left_in);
					//welcomeText.startAnimation(left_in_animation);
					
					//Animation fade_in = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_in_10);
					//ceoImage.startAnimation(fade_in);
				} else {
					//Animation left_out_animation = AnimationUtils.loadAnimation(this, R.anim.left_out);
					//welcomeText.startAnimation(left_out_animation);
				}
			}
		});
		
		if (!TvApplication.showEnglish) {
			mBtnChinese.setVisibility(View.GONE);
			mBtnEnglish.setVisibility(View.GONE);
			chooseLanguageIv_chAndEn.setVisibility(View.GONE);
			chooseLanguageIv.setVisibility(View.VISIBLE);
			mBtnEnter.setVisibility(View.VISIBLE);
			mBtnEnter.requestFocus();
		}else{
			mBtnChinese.setVisibility(View.VISIBLE);
			mBtnEnglish.setVisibility(View.VISIBLE);
			chooseLanguageIv_chAndEn.setVisibility(View.VISIBLE);
			chooseLanguageIv.setVisibility(View.GONE);
			mBtnEnter.setVisibility(View.GONE);
			mBtnChinese.requestFocus();
		}
		
		
		//add 播放背景音乐
		//BackgroundMusic.getInstance(this).playBackgroundMusic("music/background.mp3", true);
		return mMain;
	}
	
	public void initFocus(){
		mBtnChinese.requestFocus();
	}
	
	private void updateLange(Locale locale) {    	
		 Resources res = getResources();
		 Configuration config = res.getConfiguration();
		 config.locale = locale;
		 DisplayMetrics dm = res.getDisplayMetrics();
		 res.updateConfiguration(config, dm);
   }
   
	/**
	 * 欢迎
	 */
	private HttpEventHandler<WelcomeInfo> welcomeHandler = new HttpEventHandler<WelcomeInfo>() {
		
		@Override
		public void HttpSucessHandler(WelcomeInfo result) {
			
			welcomeInfo = result;
			if(mBtnChinese.isFocused()){
				welcomeTextBodyTextView.setText(welcomeInfo.getWelcomeChineseText());
			}else if(mBtnEnglish.isFocused()){
				welcomeTextBodyTextView.setText(welcomeInfo.getWelcomeEnglishText());
			}else if(mBtnEnter.isFocused()){
				welcomeTextBodyTextView.setText(welcomeInfo.getWelcomeChineseText());
				welcomeTextBodyTextView.setTextSize((int)mMain.getResources().getDimension(R.dimen.p_45));
			}

			bgImageLoadingListener = new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
					mMain.setBackground(new BitmapDrawable(mMain.getResources() ,bitmap));
					mMain.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
				}
			};
			
			ImageLoader bgImageLoader = ImageLoader.getInstance();
			bgImageLoader.loadImage(welcomeInfo.getBackgroundImage(), bgImageLoadingListener);
			
			
			ImageLoadingListener frontCHmageLoadingListener = new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
					bitmapChinese = new BitmapDrawable(mMain.getResources() ,bitmap);
					welcomeText.setImageDrawable(bitmapChinese);
					LogUtils.error("中文图片下载成功");
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
				}
			};
			
			
			ImageLoadingListener frontEnglishImageLoadingListener = new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {

				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
					LogUtils.error("英文图片下载成功");
					bitmapEnglish = new BitmapDrawable(mMain.getResources() ,bitmap);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					
				}
			};
			
			ImageLoader frontChineseImageLoader = ImageLoader.getInstance();
			frontChineseImageLoader.loadImage(welcomeInfo.getTopImageChinese(), frontCHmageLoadingListener);
			
			ImageLoader frontEnglishImageLoader = ImageLoader.getInstance();
			frontEnglishImageLoader.loadImage(welcomeInfo.getTopImageEnglish(), frontEnglishImageLoadingListener);
		}
		
		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
		}
	};


	@Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }
    
    /*************************************************************************** 
     * Acitivity要实现这个接口，这样Fragment和Activity就可以共享事件触发的资源了 
     * 
     ***************************************************************************/  
    public interface ChooseLanguageListener   
    {  
        public void chooseLanguage(int index);   
    }  
    
    
	    
	    
/*		public boolean onKeyDown(int keyCode, KeyEvent event)
		{
			switch(keyCode) {
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
			}
			return true;
		}*/
	
}

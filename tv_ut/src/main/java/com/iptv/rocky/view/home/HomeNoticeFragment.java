package com.iptv.rocky.view.home;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.HomePrompt;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.json.HomePromptJsonFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.home.prompt.HomePromptMasterLayout;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 用于选择语言后，提示阶段性的促销信息，或酒店的促销信息
 *
 */
public class HomeNoticeFragment extends Fragment{

	
	private View mMain;
	private NoticeFinishShowListener mNoticeFinishShowListener;
	//private HomePromptJsonFactory mHomePromptJsonFactory;


	
	private HomePromptMasterLayout mHomePromptMasterLayout;

	
	 /** Fragment第一次附属于Activity时调用,在onCreate之前调用 */  
    @Override  
    public void onAttach(Activity activity)
    {  
        super.onAttach(activity);  
        mNoticeFinishShowListener = (NoticeFinishShowListener) activity;   
    }  

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        TvApplication.status = "HOMEPROMPT";
	    TvApplication.position="HOME_PROMPT_FRAGMENT";
    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,   Bundle savedInstanceState){
		
		mMain = inflater.inflate(R.layout.fragment_home_notice, container,false);
		mHomePromptMasterLayout = (HomePromptMasterLayout) mMain.findViewById(R.id.home_notice_fragment);
		
        mHomePromptMasterLayout.downloadDatas();
		
		//add 播放背景音乐
		//BackgroundMusic.getInstance(this).playBackgroundMusic("music/background.mp3", true);
		return mMain;
	}

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
    
   
    public interface NoticeFinishShowListener   
    {  
        public void finishShow();   
    }  
	    
	    
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{	
		mNoticeFinishShowListener.finishShow();
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

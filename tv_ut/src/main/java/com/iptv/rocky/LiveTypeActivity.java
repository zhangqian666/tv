package com.iptv.rocky;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.iptv.common.data.EnumType;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.StatusBarView;
import com.iptv.rocky.view.live.LiveMasterLayout;

public class LiveTypeActivity extends BaseActivity {
	private LiveMasterLayout mMasterLayout;
	private ScreenShot mScreenShot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live);
		StatusBarView sbView = (StatusBarView) findViewById(R.id.tv_status_bar);
		sbView.setSearchVisibility(View.GONE);
		initView();
	}
	
	private void initView()
	{
		mMasterLayout = (LiveMasterLayout) findViewById(R.id.live_main_view);
		if(TvApplication.hasBackImage){
			mMasterLayout.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
		}
		Intent intent = getIntent();
		if (intent != null) {
			String value = intent.getStringExtra(Constants.cListIdExtra);
			LogUtils.info("value------->"+value);
				mMasterLayout.createView(CommonUtils.parseInt(value));
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mMasterLayout != null) {
			mMasterLayout.resume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mMasterLayout != null) {
			mMasterLayout.pause();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMasterLayout != null) {
			mMasterLayout.destroy();
		}
	}

    @Override
    protected void onStart() {
        super.onStart();
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		View v = getCurrentFocus();
		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
			
			break;
		case KeyEvent.KEYCODE_1:
			
			break;
		case KeyEvent.KEYCODE_2:
			
			break;
		case KeyEvent.KEYCODE_3:
			
			break;
		case KeyEvent.KEYCODE_4:
			
			break;
		case KeyEvent.KEYCODE_5:
			
			break;
		case KeyEvent.KEYCODE_6:
			
			break;
		case KeyEvent.KEYCODE_7:
			
			break;
		case KeyEvent.KEYCODE_8:
			
			break;	
		case KeyEvent.KEYCODE_9:
			
			break;		
		
		case KeyEvent.KEYCODE_ENTER:
			
			break;
		case KeyEvent.KEYCODE_BACK:
			
			
			break;
		case KeyEvent.KEYCODE_HOME:
			break;
		case 17:
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			///this.mMasterLayout.displayPictureTitle();
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			//this.mMasterLayout.hidePictureTitle();
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			
//			this.mMasterLayout.preImage();
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
//			this.mMasterLayout.nextImage();
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			break;
		case KeyEvent.KEYCODE_VOLUME_UP:
			/*mScreenShot = new ScreenShot();
			Bitmap bmp = mScreenShot.myShot(this);
			try {
				mScreenShot.saveToSD(bmp, "/mnt/sdcard/pictures/", "livetype.png");
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			break;	
		case 82:
			break;
		case 253:
			break;
		case 303:
    		break;
		case 328: 	// channel +
			
			break;
		case 331:	// channel -
			
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}

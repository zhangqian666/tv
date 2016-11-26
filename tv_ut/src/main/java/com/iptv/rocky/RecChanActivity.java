package com.iptv.rocky;

import com.iptv.common.data.RecChan;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.recchan.RecChanMasterLayout;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class RecChanActivity extends BaseActivity {

	private RecChanMasterLayout mMasterLayout;
	private ScreenShot mScreenShot;
	public static RecChan mRecChan;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recchan);

        mMasterLayout = (RecChanMasterLayout) findViewById(R.id.recchan_layout);
        if(TvApplication.hasBackImage){
			mMasterLayout.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
		}
        mMasterLayout.createView();
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
				mScreenShot.saveToSD(bmp, "/mnt/sdcard/pictures/", "recchan.png");
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

    @Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mMasterLayout.destroy();
	}

}

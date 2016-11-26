package com.iptv.rocky;

import com.iptv.common.utils.Constants;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.myHotel.MyHotelSecMasterLayout;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;

public class MyHotelSecActivity extends BaseActivity {
	
	private MyHotelSecMasterLayout mMasterLayout;
	private String id;
	private String name;
//    private ScreenShot mScreenShot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_hotel_sec);
		
		mMasterLayout = (MyHotelSecMasterLayout)findViewById(R.id.myhotelsec_main_view);
		 if(TvApplication.hasBackImage){
	        	mMasterLayout.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
			}
		
		Intent intent = getIntent();
		if (intent != null) {
			id = intent.getStringExtra(Constants.cListIdExtra);
			name = intent.getStringExtra(Constants.cListNameExtra);
			mMasterLayout.createView(id, name);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
    	
    	
		switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				break;
			
			case KeyEvent.KEYCODE_PROG_RED:
				
				break;
			case KeyEvent.KEYCODE_PROG_GREEN:
				
				break;
			case KeyEvent.KEYCODE_PROG_YELLOW:
				
				break;
			case KeyEvent.KEYCODE_PROG_BLUE:
				
				break;
			case KeyEvent.KEYCODE_F2:	// 红键：到直播频道
				break;
				
			case KeyEvent.KEYCODE_F1:	// 绿键：回看
				
				return true;
			case KeyEvent.KEYCODE_F3:	// 黄健：点播
				
				return true;
			case KeyEvent.KEYCODE_F4:	// 蓝键：酒店宣传
				
				return true;
			case KeyEvent.KEYCODE_F7:	// 本地：调取盒子厂商的本地资源管理
				return true;
			case KeyEvent.KEYCODE_MENU:	// 菜单：操作帮助
				return true;
			case KeyEvent.KEYCODE_HOME:	// 重新加载
				return true;
			case KeyEvent.KEYCODE_VOLUME_UP:
				/*LogUtils.debug("----------->KEYCODE_VOLUME_UP");
				mScreenShot = new ScreenShot();
				Bitmap bmp = mScreenShot.myShot(this);
				try {
					//mScreenShot.saveToSD(bmp, "/mnt/sdcard/pictures/", "home.png");
					String fileName=TimeUtil.getStringToday()+"home.png";
					mScreenShot.saveToSD(bmp, "/mnt/sda/sda1/", fileName.trim());///mnt/sda/sda1/ 此路径为sub插入U盘的路径
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				break;
			default:
				break;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (mMasterLayout != null) {
			mMasterLayout.destroy();
		}
	}
}	
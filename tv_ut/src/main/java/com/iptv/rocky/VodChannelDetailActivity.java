package com.iptv.rocky;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.EnumType.Platform;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.splash.AlertDialogFragment;
import com.iptv.rocky.view.voddetail.DetailMasterView;

public class VodChannelDetailActivity extends BaseActivity{

	public static final String ACTION_OPEN_ORDER_FRAGMENT = "com.rocky.android.vod.action.openorderfragment";
	private DetailMasterView masterView;
	// 定义 Fragment
	//private VodDetailOrderByPasswordFragment orderByPasswordFragment;
	//private VodDetailChoosePackageFragment vodDetailChoosePackageFragment;
	//private VodDetailChoosePayMethodFragment vodDetailChoosePayMethodFragment;
	//private AlipayFragment alipayFragment;
	//private WeixinFragment weixinFragment;
	private static final String ROOT_PASSWORD="171515121618";
	private int keyTimes=0;
	private String rootCode="";
	private Platform platform;
	private String channelId;
	private String columnCode;
	private String columnCode_price;
	private String contentCode;
	private boolean needReload;

	private AlertDialogFragment alertDialogFragment;

	// 启动屏幕保护
	private Handler handlerShowScreenSaveFragment = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vod_detail);

		masterView = (DetailMasterView) findViewById(R.id.detail_master);
		if(TvApplication.hasBackImage){
			masterView.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
		}

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_OPEN_ORDER_FRAGMENT);

		registerReceiver(vodDetailBroadcastReceiver, intentFilter);

		needReload = false;
		Intent intent = getIntent();
		if (intent != null) {
			if (TvApplication.platform == EnumType.Platform.ZTE) {
				columnCode =intent.getStringExtra(Constants.CDETAIL_COLUMNCODE_EXTRA);//"0L0203" ;//
				contentCode =intent.getStringExtra(Constants.CDETAIL_CONTENTCODE_EXTRA);//"00000020010000519986" ;//
				columnCode_price=intent.getStringExtra(Constants.COLUMNCODE_EXTRA_PRICE);
				platform = EnumType.Platform.createPlatform(intent.getStringExtra(Constants.cPlatformExtra));
				LogUtils.debug("columnCode:"+columnCode+" contentCode"+contentCode+" platform:"+platform); 
				if (columnCode != null) {
					load1(platform,columnCode,contentCode);
				}
			}else{

				channelId = intent.getStringExtra(Constants.cDetailIdExtra);
				columnCode_price=intent.getStringExtra(Constants.COLUMNCODE_EXTRA_PRICE);
				platform = EnumType.Platform.createPlatform(intent.getStringExtra(Constants.cPlatformExtra));
				LogUtils.debug("channelId:"+channelId+" platform:"+platform); 
				if (channelId != null) {
					load(platform,channelId,columnCode_price);
				}
			}
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.error("keyCode----------->"+keyCode);
		keyTimes++;
		if (keyTimes>6) {
			keyTimes=0;
			rootCode="";
		}
		rootCode+=keyCode;

		
		//if(handlerShowScreenSaveFragment.hasCallbacks(runnableShowScreenProtect)){
			//handlerShowScreenSaveFragment.removeCallbacks(runnableShowScreenProtect);
		//}
		if(TvApplication.useScreenProtect){
			handlerShowScreenSaveFragment.postDelayed(runnableShowScreenProtect, 180000);
		}
		
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
		case 17:
			rootCode="";
			rootCode="17";
			break;
			
		case 18:
			if (rootCode.equals(ROOT_PASSWORD)) {
				if (platform==EnumType.Platform.ZTE) {
					LogUtils.error("columnCode:"+columnCode+" contentCode"+contentCode+" platform:"+platform);
					showAlertDialog("中兴平台", "        栏目Code："+columnCode+"；内容Code："+contentCode);
				}else if (platform==EnumType.Platform.DEVHUAWEI||platform==EnumType.Platform.HUAWEI) {
					LogUtils.error("channelId:"+channelId+" platform:"+platform);
					showAlertDialog("华为平台","        节目ID："+channelId);
				}
				
			}
			rootCode="";
			break;
		case KeyEvent.KEYCODE_ENTER:

			break;
		case KeyEvent.KEYCODE_BACK:
			if (getCurrentFocus() != null) {


			} else {


			}
			break;
		case KeyEvent.KEYCODE_HOME:
			break;
		
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
		case 82:
			break;
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:

			break;	
		case KeyEvent.KEYCODE_CHANNEL_UP:

			break;
		case KeyEvent.KEYCODE_CHANNEL_DOWN:

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


	private BroadcastReceiver vodDetailBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			new Handler(VodChannelDetailActivity.this.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					String action = intent.getAction();

					if (action.equals(ACTION_OPEN_ORDER_FRAGMENT)) {

						//						FragmentManager fm = getFragmentManager();  
						//				        FragmentTransaction transaction = fm.beginTransaction();  
						//				        
						//				        vodDetailChoosePackageFragment = new VodDetailChoosePackageFragment();
						//				        vodDetailChoosePackageFragment.setArguments(intent.getExtras());
						//				        transaction.add(R.id.detail_master, vodDetailChoosePackageFragment);
						//				        transaction.commit();
					}
					else{
						LogUtils.error("splash 收到不明事件:"+action);
					}
				}
			});
		}
	};

	public void load(EnumType.Platform platform,String channelId,String columnCode_price) {
		masterView.load(platform,channelId,columnCode_price);
	}


	public void load1(EnumType.Platform platform,String columnCode,String contentCode) {
		masterView.load1(platform,columnCode,contentCode);
	}


	@Override
	protected void onResume() {
		LogUtils.info("Resume");
		super.onResume();
		if(needReload){
			masterView.reLoad(platform, channelId,columnCode_price);
		}else{
			needReload=true;
		}
		masterView.resume();
		TvApplication.status="FREE";
		TvApplication.position="VOD_DETAIL";

		//10分钟后显示
		//		if((!TvApplication.status.equals("BEGIN")) && (!TvApplication.status.equals("STARTUP"))){
		//			if(handlerShowScreenSaveFragment.hasCallbacks(runnableShowScreenProtect)){
		//				handlerShowScreenSaveFragment.removeCallbacks(runnableShowScreenProtect);
		//			}
		//			handlerShowScreenSaveFragment.postDelayed(runnableShowScreenProtect, 120000);
		//		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		//if(handlerShowScreenSaveFragment.hasCallbacks(runnableShowScreenProtect)){
		//	handlerShowScreenSaveFragment.removeCallbacks(runnableShowScreenProtect);
		//}
		masterView.pause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(vodDetailBroadcastReceiver);
		super.onDestroy();
		masterView.destory();
	}

	//调取微信支付
	//	public void loadWeixinFragment(){
	//		FragmentManager fm = getFragmentManager();  
	//        FragmentTransaction transaction = fm.beginTransaction();  
	//        orderByPasswordFragment = new VodDetailOrderByPasswordFragment();
	//        transaction.add(R.id.detail_master, weixinFragment);
	//        transaction.commit();
	//	}

	/**
	 * 调取支付宝支付
	 */
	//	public void loadAlipayFragment(){
	//		FragmentManager fm = getFragmentManager();  
	//        FragmentTransaction transaction = fm.beginTransaction();  
	//        orderByPasswordFragment = new VodDetailOrderByPasswordFragment();
	//        transaction.add(R.id.detail_master, alipayFragment);
	//        transaction.commit();
	//	}

	// 启动屏保
	Runnable runnableShowScreenProtect = new Runnable() {
		@Override
		public void run() {
			LogUtils.error("runner内，开始加载屏幕保护");
			if(TvApplication.screenProtectType.equals("PICTURE")){
				Intent intent = new Intent(VodChannelDetailActivity.this,ScreenProtectActivity.class);    
				startActivity(intent);
			}else if(TvApplication.screenProtectType.equals("VIDEO")){
				Intent intent = new Intent(VodChannelDetailActivity.this,ScreenProtectVideoActivity.class);    
				startActivity(intent);
			}
		}    
	};

	/**
	 * 提示框  目前只为显示节目信息
	 * @param title
	 * @param message
	 */
	private void showAlertDialog(String title,String message) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment fragment = getFragmentManager().findFragmentByTag("basicDialog");
		if (null != fragment) {
			ft.remove(fragment);
		}

		alertDialogFragment = new AlertDialogFragment();

		alertDialogFragment.setTitle(title);
		alertDialogFragment.setMessage(message);
		alertDialogFragment.show(ft, "basicDialog");

	}
}

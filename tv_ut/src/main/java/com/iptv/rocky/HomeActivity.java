package com.iptv.rocky;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.IptvAccountInitResult;
import com.iptv.common.data.LiveChannel;
import com.iptv.common.data.LiveChannelListApi;
import com.iptv.common.data.StbType;
import com.iptv.common.update.UpdateSharedPreferencesFactory;
import com.iptv.common.update.VersionInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.auth.AuthService.AuthServiceBinder;
import com.iptv.rocky.auth.CUAuthService;
import com.iptv.rocky.auth.CUZTEAuthService;
import com.iptv.rocky.auth.PingExchangeManager;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.IptvAccountInitFactory;
import com.iptv.rocky.hwdata.local.AAALiveChannelsLocalFactory;
import com.iptv.rocky.hwdata.local.VodHistoryLocalFactory;
import com.iptv.rocky.hwdata.local.VodStoreLocalFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.rabbitmq.ReceiverService;
import com.iptv.rocky.utils.BackgroundMusic;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.home.HomeMasterLayout;
import com.iptv.rocky.view.home.HomeNoticeFragment;
import com.iptv.rocky.view.home.HomeNoticeFragment.NoticeFinishShowListener;
import com.iptv.rocky.view.splash.AlertDialogFragment;
import com.iptv.rocky.view.splash.HotelServerPingAlertDialogFragment;
import com.iptv.rocky.view.splash.LoginErrorAlertDialogFragment;
import com.iptv.rocky.view.splash.ScrollingMessageFragment;
import com.iptv.rocky.view.splash.WelcomeFragment;
import com.iptv.rocky.view.splash.WelcomeFragment.ChooseLanguageListener;
import com.iptv.rocky.view.update.AppUpdateManager;
import com.iptv.rocky.view.update.DeviceInfo;
import com.iptv.rocky.view.update.UpdateConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeActivity extends BaseActivity 
	implements ImageLoadingListener,NoticeFinishShowListener,ChooseLanguageListener{
	
	private HomeMasterLayout mMasterLayout;
	private Context mContext;
	
	public static HashMap<String, LiveChannel> mapLiveChannel;
	
	public PingExchangeManager pingExchangeManager;
	
	private ImageView imageView;
	private AnimationDrawable loadingAnimation;
	
	// Splash 移植过来开始
    private static final String TAG = "HomeActivity";
	public static final String ACTION_TVAPP_BIND_SUCCESS = "com.xiaoqiang.android.auth.action.ACTION_TVAPP_BIND_SUCCESS";
    public static final String ACTION_TCL_INIT_SUCCESS = "com.xiaoqiang.android.auth.action.ACTION_TCL_INIT_SUCCESS";
    public static final String ACTION_START_AUTH = "com.rocky.android.auth.action.AUTH_START";
    private static final String ACTION_RELOGINIPTV ="com.virgintelecom.action.RELOGINIPTV";
    private static final String ACTION_STARTLOGINHOTELSERVER ="com.virgintelecom.action.STARTLOGINHOTELSERVER";
    @SuppressWarnings("unused")
	private static final String ACTION_PUBLISHAMQPMESSAGE ="com.virgintelecom.iptv.PUBLISHAMQPINFO";
    // 将接收到的滚动消息显示出来
	private static final String ACTION_PUBLISHSERVERNOTICE = "com.virgintelecom.iptv.RABBITMQ.PUBLISH.SERVERNOTICE";
	// 让机顶盒开始连接rabbitmq服务器
	private static final String ACTION_START_CONNECT_RABBITMQ = "com.virgintelecom.iptv.START_CONNECT_RABBIT";

	@SuppressWarnings("unused")
	private com.iptv.rocky.hwdata.json.CheckUpdateFactory checkUpdateFactory;
	
	private AAALiveChannelsLocalFactory aaaLiveChannelsLocalFactory;
	private IptvAccountInitFactory iptvAccountInitFactory;
	private WelcomeFragment welcomeFragment;
//	private WelcomeFragment2 welcomeFragmentBaodingYoufa;
	private HomeNoticeFragment mHomeNoticeFragment;
	private AlertDialogFragment alertDialogFragment;
	private ScrollingMessageFragment scrollingMessageFragment;
	
	private ServiceConnection mConnection = null;
	@SuppressWarnings("unused")
	private ServiceConnection mBootImageConnection = null;
	
    private boolean authServiceBindSuccess;
	private UpdateSharedPreferencesFactory sharedFactory;
    private Intent receiverService;
	private int preVersionCode;
	private long preLaunchTime;
	private int updateFlag;
    @SuppressWarnings("unused")
	private boolean bloginSuccess =false;
	private boolean started= false;
	private ConnectivityManager connMgr;
    private NetworkInfo ethInfo;
    @SuppressWarnings("unused")
	private boolean linkupFlag = false;
	private Handler handlerPingHotelServer = new Handler();
	
	// 滚动条隐藏
	private Handler handlerHideScrollMessageFragment = new Handler();
	
	// 滚动条隐藏
	@SuppressWarnings("unused")
	private Handler handlerDownloadBootVideo = new Handler();
	
	// 启动屏幕保护
	private Handler handlerShowScreenSaveFragment = new Handler();
    private int pingExchangeTimes = 0;
    private int pingExchangeMaxTime = 30;
    @SuppressWarnings("unused")
	private boolean exchangeConnected = false;
    @SuppressWarnings("unused")
	private boolean networkConnected =false;
    private VodHistoryLocalFactory mHistoryFactory;
    private VodStoreLocalFactory vodStoreLocalFactory;
    private ImageLoader bgImageLoader;
    private boolean hasBackground;
    
    @SuppressWarnings("unused")
	private String backUrl;
	// Splash 移植过来结束
    @SuppressWarnings("unused")
	private AudioManager am;
    
    @SuppressWarnings("unused")
	private ScreenShot mScreenShot;
    private BootImageService mBootImageService;

    // 机顶盒开机图片处理
    @SuppressWarnings("unused")
	private ImageLoader bootImageLoader;
    private SkyworthOptions mSkyworthOptions;
    
    private UtInfo mUtInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		
		am= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		imageView = (ImageView) findViewById(R.id.splash_screen);
		loadingAnimation = (AnimationDrawable) imageView.getBackground();
		
		LogUtils.error("===================== HOME 启动 ==================");
		mContext= this;
		mSkyworthOptions = new SkyworthOptions(getWindowManager(),mContext);
		
		
		//if(TvApplication.mChannel.equals("UTSTAR")){
			mUtInfo = new UtInfo();
			TvApplication.stbId = mUtInfo.getStbId();
			LogUtils.debug("提取到的stbid:"+mUtInfo.getStbId());
//		}
//		else if(TvApplication.mChannel.equals("SKYWORTH")){
//			SystemInfo info = mSkyworthOptions.getSystemInfos();
//			TvApplication.stbId = info.getStbId();
//		}
		widthAndHeight();
		
		// SplashActivity 移植开始
        if(TvApplication.stbId== null || TvApplication.stbId.length()==0){
        	LogUtils.error("STBID 为空");
        }else{
        }
        
        TvApplication.status = "STARTUP";
	    clearHistoryAndFavorate();
		
		TvApplication.shouldLoginDate = Calendar.getInstance();
		fillLoginUrl();
		
		// checkUpdateFactory = new com.iptv.rocky.hwdata.json.CheckUpdateFactory();
		// checkUpdateFactory.setHttpEventHandler(checkUpdateHandler);
		
		aaaLiveChannelsLocalFactory = new AAALiveChannelsLocalFactory(this);
		
		ServerConnect();
		installListeners();

		sharedFactory = new UpdateSharedPreferencesFactory(this);
		preVersionCode = sharedFactory.getInt(UpdateConstant.SHARED_KEY_VERTION_CODE, 0);
		long curtime = System.currentTimeMillis();
		preLaunchTime = sharedFactory.getLong(UpdateConstant.SHARED_KEY_LAUNCH_TIME, curtime);
		updateFlag = sharedFactory.getInt(UpdateConstant.SHARED_KEY_UPDATE, UpdateConstant.SHARED_VALUE_NO_UPDATE);
	    
	    LogUtils.debug("应急，采取延迟的方式来保证连接");
	    
	    connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    ethInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
	    
	    if (handlerPingHotelServer != null) {
	    	handlerPingHotelServer.postDelayed(runnableConnectHotelServer, 3000);
		}
	    LogUtils.error("开始建立RabbitService");
	    receiverService = new Intent(HomeActivity.this, ReceiverService.class);
	    startService(receiverService);
	    
	    
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			loadingAnimation.start();
		}
	}
	
	private void initView()
	{
		// 从  HomeMasterLayout 类中 开始看推荐页下载数据
		startHome();
	}
	
	/**
	 * 开始显示欢迎界面
	 */
	private void beginShowWelcomeFragment(){
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		LogUtils.debug("创建 new welcome的fragment");
//		if(TvApplication.hasSpecialWelcome){
//			welcomeFragmentBaodingYoufa = new WelcomeFragment2();
//			if(welcomeFragmentBaodingYoufa.isAdded()){
//				fragmentTransaction.show(welcomeFragmentBaodingYoufa);
//			}else{
//				fragmentTransaction.add(R.id.home_main_view, welcomeFragmentBaodingYoufa);
//			}
//		}else{
			welcomeFragment = new WelcomeFragment();
			if(welcomeFragment.isAdded()){
				fragmentTransaction.show(welcomeFragment);
			}else{
				fragmentTransaction.add(R.id.home_main_view, welcomeFragment);
			}
//		}

		
		
		fragmentTransaction.commit();
	}
	
	@Override
	protected void onDestroy() {
		LogUtils.debug("onDestroy");
		
		super.onDestroy();
		
		if (mMasterLayout != null) {
			mMasterLayout.destroy();
		}
	}
	
	private void startHome() {
		imageView.setVisibility(View.GONE);
		mMasterLayout = (HomeMasterLayout) findViewById(R.id.home_main_view);
		if(hasBackground){
			mMasterLayout.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
		}
		findViewById(R.id.main).setVisibility(View.VISIBLE);
		LogUtils.error("开始启动 Home");
		new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
            	LogUtils.error("mMasterLayout 是否为空?"+(mMasterLayout == null));
        		if (mMasterLayout != null) {
        			mMasterLayout.DownloadDatas();
        		}
            }
        }, 1000);

		handleMessage();
	}
	
	/**
	 * 启动信息公告
	 */
	private void startDashboard() {
		imageView.setVisibility(View.GONE);
		mMasterLayout = (HomeMasterLayout) findViewById(R.id.home_main_view);
	
		LogUtils.error("启动Dashboard, mMasterLayout 是否为空?"+ (mMasterLayout == null));
		if (mMasterLayout != null) {
			mMasterLayout.DownloadDatas();
		}
		handleMessage();
	}
	
	@Override
	protected void onResume() {
		LogUtils.error("Home,重新加载，ONRESUME");
		super.onResume();
		TvApplication.status="FREE";
		TvApplication.position="HOME_ACTIVITY";
		LogUtils.error("authServiceBindSuccess:"+authServiceBindSuccess);
		if (authServiceBindSuccess) {
			if (mMasterLayout != null) {
				mMasterLayout.resume();
			}
			handleMessage();
		}
		//10分钟后显示
		//LogUtils.error("开始加载屏幕保护:status:"+TvApplication.status);
		if((!TvApplication.status.equals("BEGIN")) && (!TvApplication.status.equals("STARTUP"))){
			//if(handlerShowScreenSaveFragment.hasCallbacks(runnableShowScreenProtect)){
			//	handlerShowScreenSaveFragment.removeCallbacks(runnableShowScreenProtect);
			//}
			if(TvApplication.useScreenProtect){
				handlerShowScreenSaveFragment.postDelayed(runnableShowScreenProtect, 180000);
			}
		}
		//LogUtils.error("");
		//add 播放背景音乐
		//BackgroundMusic.getInstance(this).playBackgroundMusic("music/background_ex.mp3", true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtils.error("HOME PAUSE");
		
		//if(handlerShowScreenSaveFragment.hasCallbacks(runnableShowScreenProtect)){
			handlerShowScreenSaveFragment.removeCallbacks(runnableShowScreenProtect);
		//}
		if (mMasterLayout != null) {
			mMasterLayout.pause();
		}
		//quiteBackgroundMusic();
	}
	
	@SuppressWarnings("unused")
	private void quiteBackgroundMusic() {
		if (BackgroundMusic.getInstance(this).isBackgroundMusicPlaying()) {
			BackgroundMusic.getInstance(this).stopBackgroundMusic();
		}
	}
	
	@Override
	public void onBackPressed() {
//		LogUtils.debug("ActivityStack.size():"+ActivityStack.size());
// 		if (ActivityStack.size() == 1 && 
//			ActivityStack.getFront().getClass().equals(this.getClass())) {
// 			Dialog alertDialog = new AlertDialog.Builder(this)
// 					.setTitle(getString(R.string.dialog_title))
// 					.setMessage(getString(R.string.dialog_tips))
// 					//.setIcon(R.drawable.tv_logo)
// 					.setNegativeButton(getString(R.string.dialog_confirm), 
// 							new DialogInterface.OnClickListener() {
// 						@Override 
// 						public void onClick(DialogInterface dialog, int which) {
// 							quiteBackgroundMusic();
// 							TvUtils.doubleClickQuitApp(HomeActivity.this);
// 						}
// 					})
// 					.setPositiveButton(getString(R.string.dialog_cancel), 
// 							new DialogInterface.OnClickListener() {
// 						@Override 
// 						public void onClick(DialogInterface dialog, int which) {
// 							dialog.dismiss();
// 						}
// 					})
// 					.create(); 
// 				alertDialog.show();
//		} else {
//			ActivityStack.popCurrent();
//		}
	}

    @Override
    protected void onStart() {
        super.onStart();
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
    	if((!TvApplication.status.equals("BEGIN")) && (!TvApplication.status.equals("STARTUP")) && (!TvApplication.status.equals("HOMEPROMPT"))){
			//if(handlerShowScreenSaveFragment.hasCallbacks(runnableShowScreenProtect)){
				handlerShowScreenSaveFragment.removeCallbacks(runnableShowScreenProtect);
			//}
			if(TvApplication.useScreenProtect){
				handlerShowScreenSaveFragment.postDelayed(runnableShowScreenProtect, 180000);
			}
		}
    	LogUtils.debug("按键:"+keyCode);
    	if(TvApplication.status.equals("HOMEPROMPT")){
    		mHomeNoticeFragment.onKeyDown(keyCode, event);
    		//finishShow();
    		return false;
    	}
    	
		switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_DPAD_LEFT:
//				if(TvApplication.hasSpecialWelcome){
//					if(welcomeFragmentBaodingYoufa.isVisible()){
//						
//					}else{
//						if (mMasterLayout != null && mMasterLayout.mProgressBar.getVisibility() == View.VISIBLE) return true;
//					}
//				}else{
					if(welcomeFragment!=null&&welcomeFragment.isVisible()){
						
					}else{
						if (mMasterLayout != null && mMasterLayout.mProgressBar.getVisibility() == View.VISIBLE) return true;
//					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				
//				if(TvApplication.hasSpecialWelcome){
//					if(welcomeFragmentBaodingYoufa.isVisible()){
//						
//					}else{
//						if (mMasterLayout != null && mMasterLayout.mProgressBar.getVisibility() == View.VISIBLE) return true;
//					}
//				}else{
					if(welcomeFragment!=null&&welcomeFragment.isVisible()){
						
					}else{
						if (mMasterLayout != null && mMasterLayout.mProgressBar.getVisibility() == View.VISIBLE) return true;
					}
//				}
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				
//				if(TvApplication.hasSpecialWelcome){
//					if(welcomeFragmentBaodingYoufa.isVisible()){
//						
//					}else{
//						if (mMasterLayout != null && mMasterLayout.mProgressBar.getVisibility() == View.VISIBLE) return true;
//					}
//				}else{
					if(welcomeFragment!=null&&welcomeFragment.isVisible()){
						
					}else{
						if (mMasterLayout != null && mMasterLayout.mProgressBar.getVisibility() == View.VISIBLE) return true;
					}
//				}
				break;
			case KeyEvent.KEYCODE_PROG_RED:
				LogUtils.error("红键");
				if (mMasterLayout != null && mMasterLayout.mProgressBar.getVisibility() == View.GONE) {
					mMasterLayout.mTabView.tabUnSelect(mMasterLayout.mTabView.getSelectedIndex());
					mMasterLayout.mTabView.tabSelected(1);
					mMasterLayout.mTabView.getChildView(1).requestFocus();
				}
				break;
			case KeyEvent.KEYCODE_PROG_GREEN:
				LogUtils.error("绿键");
				if (mMasterLayout != null && mMasterLayout.mProgressBar.getVisibility() == View.GONE) {
					Intent intent = new Intent(this, RecChanActivity.class);
					startActivity(intent);
				}
				break;
			case KeyEvent.KEYCODE_PROG_YELLOW:
				LogUtils.error("黄键");
				if (mMasterLayout != null && mMasterLayout.mProgressBar.getVisibility() == View.GONE) {
					mMasterLayout.mTabView.tabUnSelect(mMasterLayout.mTabView.getSelectedIndex());
					mMasterLayout.mTabView.tabSelected(2);
					mMasterLayout.mTabView.getChildView(2).requestFocus();
				}
				break;
			case KeyEvent.KEYCODE_PROG_BLUE:
				LogUtils.error("蓝键");
				if (mMasterLayout != null
					&& mMasterLayout.mProgressBar.getVisibility() == View.GONE) {
					mMasterLayout.mTabView.tabUnSelect(mMasterLayout.mTabView.getSelectedIndex());
					mMasterLayout.mTabView.tabSelected(3);
					mMasterLayout.mTabView.getChildView(3).requestFocus();
				}
				break;
			case KeyEvent.KEYCODE_F2:	// 红键：到直播频道
			{
				if (mMasterLayout != null
					&& mMasterLayout.mProgressBar.getVisibility() == View.GONE) {
					mMasterLayout.mTabView.tabUnSelect(mMasterLayout.mTabView.getSelectedIndex());
					mMasterLayout.mTabView.tabSelected(1);
					mMasterLayout.mTabView.getChildView(1).requestFocus();
					//mMasterLayout.mTabView.onGlobalFocusChanged(mMasterLayout.mTabView.getChildView(mMasterLayout.mTabView.getSelectedIndex()), mMasterLayout.mTabView.getChildView(i));
				}
			}
				return true;
				
			case KeyEvent.KEYCODE_F1:	// 绿键：回看
				//if (TvApplication.hotelColumnEnabled.getReview().equals("1")) {
					Intent intent = new Intent(this, RecChanActivity.class);
		            startActivity(intent);
				//}
				return true;
			case KeyEvent.KEYCODE_F3:	// 黄健：点播
				//if (TvApplication.hotelColumnEnabled.getVod().equals("1")) {
					
					if (mMasterLayout != null
						&& mMasterLayout.mProgressBar.getVisibility() == View.GONE) {
						mMasterLayout.mTabView.tabUnSelect(mMasterLayout.mTabView.getSelectedIndex());
						mMasterLayout.mTabView.tabSelected(3);
						mMasterLayout.mTabView.getChildView(3).requestFocus();
					}
				//}
				return true;
			case KeyEvent.KEYCODE_F4:	// 蓝键：酒店宣传
				//if (TvApplication.hotelColumnEnabled.getHotelIntroduce().equals("1")) {
					if (mMasterLayout != null
						&& mMasterLayout.mProgressBar.getVisibility() == View.GONE) {
						mMasterLayout.mTabView.tabUnSelect(mMasterLayout.mTabView.getSelectedIndex());
						mMasterLayout.mTabView.tabSelected(3);
						mMasterLayout.mTabView.getChildView(3).requestFocus();
					}
				//}
				return true;
			case KeyEvent.KEYCODE_F7:	// 本地：调取盒子厂商的本地资源管理
				
				startActivity("com.hybroad.personalallsec");
				
				return true;
			case KeyEvent.KEYCODE_MENU:	// 菜单：操作帮助
				return true;
			case KeyEvent.KEYCODE_HOME:	// 重新加载
				
				
				//mMasterLayout.removeAllViews();
				//mMasterLayout.DownloadDatas();
				return true;
			case KeyEvent.KEYCODE_VOLUME_UP:
			/*	LogUtils.debug("----------->KEYCODE_VOLUME_UP");
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
    
    
    private void startActivity(String packageName)
    {
        // 根据包名启动应用
        PackageManager pkmanager = mContext.getPackageManager();
        
        // 根据包名获取 启动该应用的意图
        Intent openInten = pkmanager.getLaunchIntentForPackage(packageName);
        
        if(openInten == null)
        {
            Toast toast = Toast.makeText(mContext, "应用不存在", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        
        openInten.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        
        // 根据 意图查找 可以启动的activities
        List<ResolveInfo> acts = pkmanager.queryIntentActivities(openInten, 0);
        
        // 存在，就打开此应用
        if (acts.size() > 0)
        {
            mContext.startActivity(openInten);
            //overridePendingTransition(R.anim.alpha_launcher_show, R.anim.alpha_launcher_hide);
        }
    }
    
    /**
     * Splash 移植过来
     */
    /**
     * Splash 移植过来
     */
	private void fillLoginUrl()
	{
		LogUtils.error("调取fillLoginUrl IPTVUriUtils.HotelHost:"+IPTVUriUtils.HotelHost +";  IPTVUriUtils.hotelEpgHost:"+IPTVUriUtils.hotelEpgHost);
		IPTVUriUtils.clear();
		IPTVUriUtils.HotelHost=String.format("%s%s",IPTVUriUtils.hotelEpgHost, IPTVUriUtils.HotelHost);
		//IPTVUriUtils.HotelHost=IPTVUriUtils.hotelEpgHost
		//IPTVUriUtils.hotelLoginHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.hotelLoginHost);
		IPTVUriUtils.IptvAccountInitHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.IptvAccountInitHost);
		//IPTVUriUtils.hotelColumnEnabledJsonHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.hotelColumnEnabledJsonHost);
		//IPTVUriUtils.CheckUpdataHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.CheckUpdataHost);
		IPTVUriUtils.CheckIptvUpDateHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.CheckIptvUpDateHost);
		IPTVUriUtils.ChannelListPublic = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.ChannelListPublic);
		IPTVUriUtils.PortalHomeHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.PortalHomeHost);
		IPTVUriUtils.VodTopHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.VodTopHost);
		IPTVUriUtils.VodSecHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.VodSecHost);
		IPTVUriUtils.HotelProgramListHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.HotelProgramListHost);
		//IPTVUriUtils.VodDetailInfoHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.VodDetailInfoHost);
		IPTVUriUtils.myHotelTopLevelHost =  String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.myHotelTopLevelHost);
		IPTVUriUtils.myHotelSecHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.myHotelSecHost);
		IPTVUriUtils.myHotelPictureListHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.myHotelPictureListHost);
		IPTVUriUtils.PortalChannelsHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.PortalChannelsHost);
		IPTVUriUtils.HotelProgramDetailHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.HotelProgramDetailHost);
		IPTVUriUtils.HotelProgramDetaiJsonlHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.HotelProgramDetaiJsonlHost);
		
		IPTVUriUtils.HotelPlayUrlHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.HotelPlayUrlHost);
		IPTVUriUtils.LiveChannelPlayRecordReportHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.LiveChannelPlayRecordReportHost);
		IPTVUriUtils.vodDetailCheckOrderStatusHost =String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.vodDetailCheckOrderStatusHost);
		IPTVUriUtils.orderVodConfirmPasswordHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.orderVodConfirmPasswordHost);
		IPTVUriUtils.orderOneDayPackageHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.orderOneDayPackageHost);
		IPTVUriUtils.screenProtect =String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.screenProtect);
		IPTVUriUtils.videoScreenProtect=String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.videoScreenProtect);
		IPTVUriUtils.welcomeInfo =String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.welcomeInfo);
		IPTVUriUtils.orderVodHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.orderVodHost);
		IPTVUriUtils.dashboardHost =  String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.dashboardHost);
		IPTVUriUtils.homePromptHost =  String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.homePromptHost);
	}

	/**
	 * 检查版本信息的情况
	 */
	@SuppressWarnings("unused")
	private HttpEventHandler<VersionInfo> checkUpdateHandler = new HttpEventHandler<VersionInfo>() {
		@Override
		public void HttpSucessHandler(VersionInfo result) {
			int curVersionCode = TvApplication.mAppVersionCode;
			
			LogUtils.error("当前版本:"+curVersionCode+ " 是否需要升级？"+result.isUpdate());
			
			// 没有更新
			if (!result.isUpdate()) {
				LogUtils.info("不需要升级");
				if (updateFlag == UpdateConstant.SHARED_VALUE_NO_UPDATE 
						&& curVersionCode == result.getVersionCode()) {
					sharedFactory.putInt(UpdateConstant.SHARED_KEY_UPDATE, UpdateConstant.SHARED_VALUE_UPDATED);
				}
				LogUtils.info("不需要升级，开始加载Home 信息,先加载welcome信息");
				broadcast(ACTION_START_AUTH, null);
				
			}else{
				LogUtils.info("需要升级 curVersionCode"+curVersionCode+"  result.getVersionCode()"+result.getVersionCode());
				LogUtils.info("当前版本是否小于升级版本"+(curVersionCode < result.getVersionCode()));
				if (curVersionCode >= result.getVersionCode()) {
					sharedFactory.putInt(UpdateConstant.SHARED_KEY_UPDATE, UpdateConstant.SHARED_VALUE_NO_UPDATE);
				
					broadcast(ACTION_START_AUTH, null);
					LogUtils.info("当前版本大于等于升级版本,因此不升级，开始加载界面信息，先加载welcome信息");
					
				}else{
					int mode = result.getMode();
					LogUtils.debug("升级版本大于当前版本，版本升级模式 "+mode);
					if (mode == UpdateConstant.UPDATE_1_TIME) {
						if (preVersionCode < result.getVersionCode()) {
							showUpdateDialog(result);
							sharedFactory.putInt(UpdateConstant.SHARED_KEY_VERTION_CODE, result.getVersionCode());
							return;
						}
					}else if (mode == UpdateConstant.UPDATE_EVERYDAT) {
						long curTime = System.currentTimeMillis();
						if (curTime - preLaunchTime > UpdateConstant.ONE_DAY_MILLIS) {
							showUpdateDialog(result);
							// 存储启动时间
							sharedFactory.putLong(UpdateConstant.SHARED_KEY_LAUNCH_TIME, curTime);
							return;
						}
					}else if (mode == UpdateConstant.UPDATE_ALWAYS || mode == UpdateConstant.UPDATE_FORCE) {
						showUpdateDialog(result);
						return;
					}
				}
			}
		}

		private void showUpdateDialog(VersionInfo result) {
			AppUpdateManager updateMgr = new AppUpdateManager(HomeActivity.this, result);
			updateMgr.showUpdate();
		}
		
		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			TvUtils.processHttpFail(HomeActivity.this);
		}
	};
	
	/**
	 * 登录信息
	 */
	private HttpEventHandler<IptvAccountInitResult> loginHandler = new HttpEventHandler<IptvAccountInitResult>() {
		@Override
		public void HttpSucessHandler(IptvAccountInitResult result) {
			// 成功
			LogUtils.error("返回状态:"+result.getResult());
			if (result.getResult() == 200) {
				LogUtils.error("机顶盒类型:"+result.getStbType());
				
				TvApplication.platform = EnumType.Platform.createPlatform(result.getIptvPlatform());
				AAALiveChannelsLocalFactory.platform = TvApplication.platform;
				// 初始化平台地址信息
				TvApplication.initPlatformAuthUrl();
				if(result.getStbType() == StbType.DASHBOARD_STB){
					TvApplication.account = result.getUserId();
					TvApplication.passwd = result.getPassword();
					TvApplication.hotelId = result.getHotelId();
					TvApplication.stbType = result.getStbType();
					TvApplication.roomId = result.getRoomId();
					TvApplication.status = "LOGIN";
				    TvApplication.position="LOGIN_ACTIVITY";
				    
				    
				    // 开始修改机顶盒的平台信息
					mSkyworthOptions.changNtpServer();
					// 结束修改机顶盒的平台信息
					
					
					// 开始处理机顶盒旋转角度
					mSkyworthOptions.changeRotation(HomeActivity.this, result.getStbOrientation());
					
					//changeRotation(result.getStbOrientation());
					// 结束处理机顶盒旋转角度
				    
					if(TvApplication.platform==EnumType.Platform.RUNHUAWEI || TvApplication.platform==EnumType.Platform.DEVHUAWEI){
						Intent authIntent = new Intent(getApplicationContext(), CUAuthService.class);
						bindService(authIntent, mConnection, Context.BIND_AUTO_CREATE);
					}else if(TvApplication.platform==EnumType.Platform.ZTE){
						Intent authIntent = new Intent(getApplicationContext(), CUZTEAuthService.class);
						bindService(authIntent, mConnection, Context.BIND_AUTO_CREATE);
					}else{
						LogUtils.error("平台数据platform错误"+TvApplication.platform+"，无法做注册绑定");
					}
				    if(result.getStbType() == null){
						result.setStbType(StbType.DASHBOARD_STB);
					}
					TvApplication.stbType = result.getStbType();
				    startDashboard();
				    
				}else if(result.getStbType().equals(StbType.IPTV_STB)){
					TvApplication.account = result.getUserId();
					TvApplication.passwd = result.getPassword();
					TvApplication.hotelId = result.getHotelId();
					TvApplication.roomId = result.getRoomId();
					TvApplication.shoulLoginIptv = result.isLoginIptv();
					TvApplication.billingType = result.getBillingTypeOfHotelGuest();
					TvApplication.perMonthPrice = result.getPerMonthPrice();
					TvApplication.payForDaylyPayVod =result.isPayForDaylyPayVod();
					TvApplication.perDayPrice = result.getPerDayPrice();
					TvApplication.daylyPayVodPrice = result.getDaylyPayVodPrice();
					TvApplication.payForPayPerVod = result.isPayForPayPerVod();
					TvApplication.perVodPrice = result.getPerVodPrice();
					TvApplication.welcomeId = result.getWelcomeId();
					TvApplication.useScreenProtect = result.isHasScreenProtect();
					TvApplication.screenProtectType = result.getScreenProtectType();
					TvApplication.screenProtectId = result.getScreenProtectId();
					TvApplication.groupId = result.getGroupId();
					TvApplication.bootImage = result.getBootImage();
					TvApplication.bootVideo = result.getBootVideo();
					TvApplication.stbOrientation =result.getStbOrientation();
					TvApplication.showEnglish=result.getShowEnglish();
					
					if(result.getStbType() == null){
						result.setStbType(StbType.IPTV_STB);
					}
					
					if(TvApplication.platform==EnumType.Platform.DEVHUAWEI || TvApplication.platform==EnumType.Platform.RUNHUAWEI){
						Intent authIntent = new Intent(getApplicationContext(), CUAuthService.class);
						bindService(authIntent, mConnection, Context.BIND_AUTO_CREATE);
					}else if(TvApplication.platform==EnumType.Platform.ZTE){
						LogUtils.info("中兴3A鉴权开始");
						Intent authIntent = new Intent(getApplicationContext(), CUZTEAuthService.class);
						bindService(authIntent, mConnection, Context.BIND_AUTO_CREATE);
					}else{
						LogUtils.error("平台数据platform错误"+TvApplication.platform+"，无法做注册绑定");
					}
					
					TvApplication.stbType = result.getStbType();
				    
					TvApplication.homeShowPromptNotice = result.isShowHomeNoticePrompt();
					LogUtils.debug("是否显示促销信息："+TvApplication.homeShowPromptNotice);
					
				    LogUtils.error("登陆数据成功， 开始下载栏目控制");
				    TvApplication.status = "LOGIN";
				    TvApplication.position="LOGIN_ACTIVITY";
				    TvApplication.hasSpecialWelcome = result.isHasSpecialWelcome();
				    
					if(result.isHasBackground()){//增加检查是否已经在本地存储
						bgImageLoader = ImageLoader.getInstance();
						bgImageLoader.loadImage(result.getBackGround(), HomeActivity.this);
						backUrl=result.getBackGround();
					}
					if(TvApplication.status!=null){
						if((!TvApplication.status.equals("STARTUP")) || (!TvApplication.status.equals("BEGIN"))){
							//if(handlerShowScreenSaveFragment.hasCallbacks(runnableShowScreenProtect)){
							//	handlerShowScreenSaveFragment.removeCallbacks(runnableShowScreenProtect);
							//}
							handlerShowScreenSaveFragment.postDelayed(runnableShowScreenProtect, 180000);
						}
					}
					
					// 通用，关闭音量调节
				    //am.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0); //tempVolume:音量绝对值  
					//int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC); 
					//LogUtils.error("设置后音量"+currentVolume);
					
					if(result.getGuests() != null){
						if(result.getGuests().size()>0){
							TvApplication.guests.addAll(result.getGuests());
						}
					}
					
					// 开始修改机顶盒的平台信息
					mSkyworthOptions.changNtpServer();
					// 结束修改机顶盒的平台信息
					
					
					// 开始处理机顶盒旋转角度
					mSkyworthOptions.changeRotation(HomeActivity.this,result.getStbOrientation());
					// 结束处理机顶盒旋转角度
					
					// 临时关闭更新。
					//checkUpdate();
					
					broadcast(ACTION_START_AUTH, null);
					
				}else{
					LogUtils.error("机顶盒类型为:"+result.getStbType());
				}
			}else if(result.getResult() == 500 || result.getResult() == 503){
				loadingAnimation.stop();
				imageView.setVisibility(View.GONE);
				
				showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getErrorInfo());
				TvApplication.status = "STARTUP";
				
			}else if(result.getResult() == 600){
				loadingAnimation.stop();
				imageView.setVisibility(View.GONE);
				
				showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getErrorInfo());
				TvApplication.status = "STARTUP";
				
			}else if(result.getResult() == 609 || result.getResult() == 610){
				loadingAnimation.stop();
				imageView.setVisibility(View.GONE);
				
				showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getErrorInfo());
				TvApplication.status = "STARTUP";	
			}else{
				loadingAnimation.stop();
				imageView.setVisibility(View.GONE);
				
				showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getErrorInfo());
				TvApplication.status = "STARTUP";
			}
			
			//通知RABBITMQ开始连接服务器
			Log.d(HomeActivity.class.getName(), "发出开始连接rabbitmq服务器事件");
			Intent intent = new Intent(ACTION_START_CONNECT_RABBITMQ);
			sendBroadcast(intent); 
			
			Intent iUpdate = new Intent("SK_SYSTEM_UPGRADE_CHECK");
			sendBroadcast(iUpdate);
			LogUtils.error("发送升级通知");
		}

		private void showUpdateDialog(VersionInfo result) {
			AppUpdateManager updateMgr = new AppUpdateManager(HomeActivity.this, result);
			updateMgr.showUpdate();
		}
		
		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			//TvUtils.processHttpFail(HomeActivity.this);
			showAlertDialog("酒店服务器错误","酒店服务器无法连接，请联系系统管理员进行处理。");
		}
	};
	
	private void ServerConnect()
	{
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				LogUtils.debug("绑定结果成功");
				IPTVUriUtils.mAuthServiceBinder = (AuthServiceBinder) service;
				authServiceBindSuccess = true;
			}
		};
	}
	
	/**
     * 自己增加，主要是解决  mAuthServiceBinder 的绑定，只有绑定成功后，才可以进行认证
     *
     */
	protected void broadcast(String action, Map<String, String> params) {
		Intent intent = new Intent(action);
		if (params != null) {
			Set<String> keys = params.keySet();
			for (String string : keys) {
				intent.putExtra(string, params.get(string));
			}
		}
		sendBroadcast(intent);
	}
	
	private void installListeners() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CUAuthService.ACTION_AUTH_ERROR);
		intentFilter.addAction(CUAuthService.ACTION_AUTH_START);
		intentFilter.addAction(CUAuthService.ACTION_AUTH_SUCCEED);
		intentFilter.addAction(ACTION_START_AUTH);
		intentFilter.addAction(ACTION_TCL_INIT_SUCCESS);
		intentFilter.addAction(ACTION_RELOGINIPTV);
		intentFilter.addAction(ACTION_STARTLOGINHOTELSERVER);
		intentFilter.addAction(ACTION_PUBLISHSERVERNOTICE);

		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(mAuthBroadcastReceiver, intentFilter);
	}
	
	// 认证通过，继续处理
	private void startNoLoginToIptv() {
		// 认证通过，先保存用户名和密码
		//sharedFactory.putString(UpdateConstant.SHARED_KEY_ACCOUNT, TvApplication.account);
		//sharedFactory.putString(UpdateConstant.SHARED_KEY_PASSWD, TvApplication.passwd);
		
		if (TvApplication.platform==EnumType.Platform.RUNHUAWEI || TvApplication.platform==EnumType.Platform.DEVHUAWEI) {
			try {
				if (IPTVUriUtils.mAuthServiceBinder == null) {
					LogUtils.debug("IPTVUriUtils.mAuthServiceBinder is null");
				} else {
					if (IPTVUriUtils.mAuthServiceBinder.getEPGDomain() == null) {
						LogUtils.debug("IPTVUriUtils.mAuthServiceBinder.getEPGDomain is null");
					} else {
						if (TvApplication.useProxy) {
							IPTVUriUtils.EpgHost = IPTVUriUtils.mAuthServiceBinder.getEPGDomain().substring(0, IPTVUriUtils.mAuthServiceBinder.getEPGDomain().indexOf("/EPG/"));
							LogUtils.error("/EPG EpgHost---->"+IPTVUriUtils.EpgHost);
							IPTVUriUtils.EpgHost = "http://110.249.173.111:33200";
						}else{
							IPTVUriUtils.EpgHost = IPTVUriUtils.mAuthServiceBinder.getEPGDomain().substring(0, IPTVUriUtils.mAuthServiceBinder.getEPGDomain().indexOf("/EPG/"));
						}
						
					
					}
				}
				
				//fillUrl();
			} catch (RemoteException e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			}
		}else if(TvApplication.platform==EnumType.Platform.ZTE){
			try {
				if (IPTVUriUtils.mAuthServiceBinder == null) {
					LogUtils.debug("IPTVUriUtils.mAuthServiceBinder is null");
				} else {
					if (IPTVUriUtils.mAuthServiceBinder.getEPGDomain() == null) {
						LogUtils.debug("IPTVUriUtils.mAuthServiceBinder.getEPGDomain is null");
					} else {
						LogUtils.info("IPTVUriUtils.EpgHost------>"+IPTVUriUtils.mAuthServiceBinder.getEPGDomain());
						IPTVUriUtils.EpgHost = IPTVUriUtils.mAuthServiceBinder.getEPGDomain().substring(0, IPTVUriUtils.mAuthServiceBinder.getEPGDomain().indexOf("/iptvepg/"));
					}
				}
				
				//fillUrl();
			} catch (RemoteException e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			}
		}
		

		//imageView.setVisibility(View.GONE);
	
		DeviceInfo info = new DeviceInfo(HomeActivity.this);
		LogUtils.info("准备下载更新信息  hotelId:" +TvApplication.hotelId);
		//临时关闭升级，直接启动
		// checkUpdateFactory.DownloadDatas(TvApplication.mChannel, info.getDeviceId(), info.getDeviceType(), info.getOsv(), info.getVersionCode(),"HUAWEI",TvApplication.hotelId,"skyworth");
		
		// 临时直接加载开始
		LogUtils.error("临时直接加载开始");
		//initView();
		
		// 临时直接加载结束
		
		handleMessage();
		
		/*if(handlerNoChannelInfo != null){
			handlerNoChannelInfo.removeCallbacks(runnableDisplayNoChannel);
			handlerNoChannelInfo.postDelayed(runnableDisplayNoChannel, 3000);
		}*/
	}
	
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
	
	/**
	 * 隐藏
	 *
	 */
	private void hideAlertDialog() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(alertDialogFragment);
		fragmentTransaction.commit();
    }
	
	
	private void showLoginErrorAlertDialog(String title,String message) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag("basicDialog");
        if (null != fragment) {
            ft.remove(fragment);
        }
 
        LoginErrorAlertDialogFragment dialogFragment = new LoginErrorAlertDialogFragment();
       
        dialogFragment.setTitle(title);
        dialogFragment.setMessage(message);
        dialogFragment.show(ft, "basicDialog");
    }
	
	private BroadcastReceiver mAuthBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			new Handler(HomeActivity.this.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					
					int ret = -1;
					int message = -1;
					String mConnectType = null;
					
					String action = intent.getAction();
					LogUtils.error("收到的消息:"+action);
					if (action.equals(CUAuthService.ACTION_AUTH_START)) {
						// Toast.makeText(HomeActivity.this, getResources().getString(R.string.authing), Toast.LENGTH_LONG).show();
					} else if (action.equals(CUAuthService.ACTION_AUTH_SUCCEED)) {
						
						// Toast.makeText(HomeActivity.this, getResources().getString(R.string.authing_success), Toast.LENGTH_LONG).show();
						
						bloginSuccess = true;
						LogUtils.debug("started:"+started);
						
						// Begin Load Welcome Fragment;
						//loadingAnimation.stop();
						//imageView.setVisibility(View.GONE);
						
						
						if(!started){
							LogUtils.debug("第一次: start");
							start();
							
						}else{
							LogUtils.debug("再次开始");
							reStart();
						}
					} else if (action.equals(CUAuthService.ACTION_AUTH_ERROR)) {
						
						String errorCode = intent.getStringExtra("errorCode");  
						String reason = intent.getStringExtra("reason");
						LogUtils.info("提取到的错误代码 "+errorCode+  "  reason"+ reason);
						Toast.makeText(HomeActivity.this, getResources().getString(R.string.authing_fail), Toast.LENGTH_LONG).show();
						showLoginErrorAlertDialog(errorCode,reason);
						bloginSuccess = false;
					}
					else if(action.equals(ACTION_START_AUTH))
					{
						LogUtils.debug("开始发起注册");
						if (authServiceBindSuccess) {
							TvApplication.startAuth();
						}else{
							broadcast(ACTION_START_AUTH, null);
						}
					}else if(action.equals(ACTION_RELOGINIPTV)){
						LogUtils.info("收到重新注册iptv信息，发起注册");
						
						broadcast(ACTION_START_AUTH, null);
					}
					else if(action.equals(ACTION_PUBLISHSERVERNOTICE))
					{	
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						
						Bundle bundle =intent.getExtras();
						String scrollmessage = bundle.getString("MESSAGE");
						
						LogUtils.debug("收到要发布消息, 传递的消息:"+scrollmessage);
						
						Bundle newBundle = new Bundle();
						long duration=0;
						try {
							JSONObject json =new JSONObject(scrollmessage);
							duration=json.getLong("duration");
							newBundle.putString("MESSAGE", json.getString("text"));
						} catch (JSONException e) {
						LogUtils.info("解析推送Json失败》》》【"+e.getMessage()+"】");
						}
					
						LogUtils.debug("scrollingMessageFragment 是否为空?"+(scrollingMessageFragment == null));
						if(scrollingMessageFragment == null){
							scrollingMessageFragment = new ScrollingMessageFragment();
							scrollingMessageFragment.setArguments(newBundle);
							fragmentTransaction.add(R.id.home_main_view, scrollingMessageFragment);
							fragmentTransaction.commit();
							handlerHideScrollMessageFragment.removeCallbacks(runnableHideScrollMessageFragment);
							handlerHideScrollMessageFragment.postDelayed(runnableHideScrollMessageFragment, duration*60000);
//							handlerHideScrollMessageFragment.postDelayed(runnableHideScrollMessageFragment, 10000);
						}else{
							if(scrollingMessageFragment.isAdded()){
								if(scrollingMessageFragment.isVisible()){
									try {
										JSONObject json =new JSONObject(scrollmessage);
										duration=json.getLong("duration");
										scrollingMessageFragment.showMessage( json.getString("text"));
									} catch (JSONException e) {
									LogUtils.info("解析推送Json失败》》》【"+e.getMessage()+"】");
									}
								}else{
									LogUtils.error("先显示，再设置要显示的信息:"+scrollmessage);
									fragmentTransaction.show(scrollingMessageFragment);
									try {
										JSONObject json =new JSONObject(scrollmessage);
										duration=json.getLong("duration");
										scrollingMessageFragment.showMessage( json.getString("text"));
									} catch (JSONException e) {
									LogUtils.info("解析推送Json失败》》》【"+e.getMessage()+"】");
									}
								}
								fragmentTransaction.commit();
								handlerHideScrollMessageFragment.removeCallbacks(runnableHideScrollMessageFragment);
								handlerHideScrollMessageFragment.postDelayed(runnableHideScrollMessageFragment, duration*60000);
							}else{
								scrollingMessageFragment = new ScrollingMessageFragment();
								scrollingMessageFragment.setArguments(newBundle);
								fragmentTransaction.add(R.id.home_main_view, scrollingMessageFragment);
								fragmentTransaction.commit();
								handlerHideScrollMessageFragment.removeCallbacks(runnableHideScrollMessageFragment);
								handlerHideScrollMessageFragment.postDelayed(runnableHideScrollMessageFragment, duration*60000);
							}
						}
					}

					else{
						LogUtils.error("splash 收到不明事件:"+action);
					}
				}
			});
		}
	};
	
	// 认证通过，继续处理
	private void reStart() {
		try {
			aaaLiveChannelsLocalFactory.updateHomeInfos(LiveChannelListApi.getLiveChannel(IPTVUriUtils.mAuthServiceBinder.getChannels()));
		} catch (RemoteException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
		//imageView.setVisibility(View.GONE);		
		handleMessage();
	}
	
	/**
	 * Welcome 选择语言后，开始加载
	 */
	@Override
	public void chooseLanguage(int index) {
		
		if(TvApplication.homeShowPromptNotice){
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			LogUtils.debug("关闭 welcome fragment ，然后开始下载数据，后面加一个等待符");
			
			if(TvApplication.hasSpecialWelcome){
//				fragmentTransaction.remove(welcomeFragmentBaodingYoufa);
//			}else{
				fragmentTransaction.remove(welcomeFragment);
			}
			
			mHomeNoticeFragment = new HomeNoticeFragment();
			fragmentTransaction.add(R.id.home_main_view, mHomeNoticeFragment);
			fragmentTransaction.commit();
	        TvApplication.status = "HOMEPROMPT";
		}else{
//			if(TvApplication.hasSpecialWelcome){
//				FragmentManager fragmentManager = getFragmentManager();
//				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//				fragmentTransaction.remove(welcomeFragmentBaodingYoufa);
//				fragmentTransaction.commit();
//			}else{
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.remove(welcomeFragment);
				fragmentTransaction.commit();
//			}
			
			
			LogUtils.error("不显示开机公告，点击选择语言，开始登录IPTV");
			if (TvApplication.shoulLoginIptv) {
				//broadcast(ACTION_START_AUTH, null);
				initView();
			} else {
				startNoLoginToIptv();
			}
		}
	}
	
	
	/**
	 *  认证通过，继续处理
	 */
	private void start() {
		// 认证通过，先保存用户名和密码
		try {
			if (IPTVUriUtils.mAuthServiceBinder == null) {
				LogUtils.debug("IPTVUriUtils.mAuthServiceBinder is null");
			} else {
//				if (IPTVUriUtils.mAuthServiceBinder.getEPGDomain() == null) {
//					LogUtils.debug("IPTVUriUtils.mAuthServiceBinder.getEPGDomain is null");
//				} else {
					if(TvApplication.platform==EnumType.Platform.ZTE){
						
//						IPTVUriUtils.EpgHost = IPTVUriUtils.mAuthServiceBinder.getEPGDomain().substring(0, IPTVUriUtils.mAuthServiceBinder.getEPGDomain().indexOf("/iptvepg/"));
						LogUtils.info("******************临时写死获取epg地址**************************");
						IPTVUriUtils.EpgHost="http://"+CUZTEAuthService.IP_PORT;
					}else if (TvApplication.platform == EnumType.Platform.RUNHUAWEI || TvApplication.platform==EnumType.Platform.DEVHUAWEI) {
						if (TvApplication.useProxy) {
							IPTVUriUtils.EpgHost = IPTVUriUtils.mAuthServiceBinder.getEPGDomain().substring(0, IPTVUriUtils.mAuthServiceBinder.getEPGDomain().indexOf("/EPG/"));
							LogUtils.error("/EPG EpgHost---->"+IPTVUriUtils.EpgHost);
							IPTVUriUtils.EpgHost = "http://110.249.173.111:33200";
						}else{
							IPTVUriUtils.EpgHost = IPTVUriUtils.mAuthServiceBinder.getEPGDomain().substring(0, IPTVUriUtils.mAuthServiceBinder.getEPGDomain().indexOf("/EPG/"));
						}
					}
				}
//			}
			//LogUtils.debug("start epgHost:"+IPTVUriUtils.EpgHost);
			//IPTVUriUtils.EpgHost = IPTVUriUtils.mAuthServiceBinder.getEPGADDR();
			welcomefillUrl();
			
			//将下载的直播数据存到数据库中
			aaaLiveChannelsLocalFactory.dropTable();
			aaaLiveChannelsLocalFactory.createDB();
			LiveChannelListApi.platform = TvApplication.platform ;
			
			ArrayList<LiveChannel> channelList = LiveChannelListApi.getLiveChannel(IPTVUriUtils.mAuthServiceBinder.getChannels());
			aaaLiveChannelsLocalFactory.insertHomeInfos(channelList);
			//LogUtils.info("结束保存AAA下载的直播数据到本地服务器");
		} catch (RemoteException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
		
		DeviceInfo info = new DeviceInfo(HomeActivity.this);
		LogUtils.debug("开始下载更新数据 hotelId:"+TvApplication.hotelId);
		// checkUpdateFactory.DownloadDatas(TvApplication.mChannel, info.getDeviceId(), info.getDeviceType(), info.getOsv(), info.getSv(),"HUAWEI",TvApplication.hotelId,"HUAWEI");
		// 临时直接加载开始
		LogUtils.error("临时直接加载开始");
		
		// 临时直接加载结束
		handleMessage();
		started = true;
		// Begin Load Welcome Fragment;
		loadingAnimation.stop();
		imageView.setVisibility(View.GONE);
		
		beginShowWelcomeFragment();
		
	}
	
	
	private void welcomefillUrl()
	{
		
		IPTVUriUtils.clear();
		
		if(TvApplication.platform == EnumType.Platform.DEVHUAWEI || TvApplication.platform == EnumType.Platform.RUNHUAWEI){
			IPTVUriUtils.Host = String.format("%s%s", IPTVUriUtils.EpgHost, IPTVUriUtils.huaweiHost);
		}else if(TvApplication.platform == EnumType.Platform.ZTE){
			IPTVUriUtils.Host = String.format("%s%s%s", IPTVUriUtils.EpgHost, IPTVUriUtils.zteHost,CUZTEAuthService.FRAMECODE+"/");
		}
		
		IPTVUriUtils.HotelHost = String.format("%s%s",IPTVUriUtils.hotelEpgHost, IPTVUriUtils.HotelHost);
		IPTVUriUtils.CheckIptvUpDateHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.CheckIptvUpDateHost);
		IPTVUriUtils.ChannelListHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.ChannelListHost);
		IPTVUriUtils.ChannelListPublic = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.ChannelListPublic);
		IPTVUriUtils.ChannelListByTypeIdHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.ChannelListByTypeIdHost);
		IPTVUriUtils.PortalHomeHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.PortalHomeHost);
		IPTVUriUtils.PortalChannelsHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.PortalChannelsHost);
		IPTVUriUtils.ChannelTypeHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.ChannelTypeHost);
		IPTVUriUtils.ChannelInfoHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.ChannelInfoHost);
		IPTVUriUtils.ProgBillHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.ProgBillHost);
		IPTVUriUtils.AllChannelProgBillHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.AllChannelProgBillHost);
		IPTVUriUtils.VodTriggerPlayUrlHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.VodTriggerPlayUrlHost);
		IPTVUriUtils.VodTopHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.VodTopHost);
		IPTVUriUtils.VodSecHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.VodSecHost);
		IPTVUriUtils.VodThdHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.VodThdHost);
		IPTVUriUtils.VodThdHostJSON = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.VodThdHostJSON);
		IPTVUriUtils.VodChannelListHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.VodChannelListHost);
		IPTVUriUtils.HotelProgramListHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.HotelProgramListHost);
		IPTVUriUtils.VodDetailInfoHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.VodDetailInfoHost);
		IPTVUriUtils.VodDetailInfoHostJson = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.VodDetailInfoHostJson);
		IPTVUriUtils.VodPlayRecordReportHost = String.format("%s%s", IPTVUriUtils.HotelHost,IPTVUriUtils.VodPlayRecordReportHost);
		IPTVUriUtils.LiveChannelPlayRecordReportHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.LiveChannelPlayRecordReportHost);
		IPTVUriUtils.vodDetailCheckOrderStatusHost =String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.vodDetailCheckOrderStatusHost);
		IPTVUriUtils.HotelProgramDetailHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.HotelProgramDetailHost);
		IPTVUriUtils.HotelProgramDetaiJsonlHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.HotelProgramDetaiJsonlHost);
		IPTVUriUtils.orderVodHost =String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.orderVodHost);
		IPTVUriUtils.orderVodConfirmPasswordHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.orderVodConfirmPasswordHost);
		IPTVUriUtils.HotelPlayUrlHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.HotelPlayUrlHost);
		IPTVUriUtils.orderOneDayPackageHost =String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.orderOneDayPackageHost);
		IPTVUriUtils.VodSearchHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.VodSearchHost);
		IPTVUriUtils.RecChanHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.RecChanHost);
		IPTVUriUtils.RecBillHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.RecBillHost);
		IPTVUriUtils.RecTriggerPlayUrlHost = String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.RecTriggerPlayUrlHost);
		//ZTE  PLATFORM
		IPTVUriUtils.AllChannelProgBillHostZTE=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.AllChannelProgBillHostZTE);
		IPTVUriUtils.VodAuthJson=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.VodAuthJson);
		IPTVUriUtils.vodGetPlayUrlJson=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.vodGetPlayUrlJson);
		IPTVUriUtils.getSitcomList=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.getSitcomList);
		IPTVUriUtils.VodSearchByCode=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.VodSearchByCode);
		IPTVUriUtils.vodGetVodTailinfo=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.vodGetVodTailinfo);
		IPTVUriUtils.vodGetVodListByTypeIdZTE=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.vodGetVodListByTypeIdZTE);
		IPTVUriUtils.getReBillUrl=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.getReBillUrl);
		IPTVUriUtils.getChannelList=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.getChannelList);
		IPTVUriUtils.getChanBill=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.getChanBill);
		IPTVUriUtils.zte_getCategoriesThirdJson=String.format("%s%s", IPTVUriUtils.Host, IPTVUriUtils.zte_getCategoriesThirdJson);
		
		//My Hotel's first level
		IPTVUriUtils.myHotelTopLevelHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.myHotelTopLevelHost);
		IPTVUriUtils.myHotelSecHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.myHotelSecHost);
		IPTVUriUtils.myHotelPictureListHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.myHotelPictureListHost);
		IPTVUriUtils.screenProtect =  String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.screenProtect);
		IPTVUriUtils.videoScreenProtect=String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.videoScreenProtect);
		IPTVUriUtils.welcomeInfo 	= String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.welcomeInfo);
		IPTVUriUtils.dashboardHost 	= String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.dashboardHost);
		IPTVUriUtils.homePromptHost = String.format("%s%s", IPTVUriUtils.HotelHost, IPTVUriUtils.homePromptHost);
		
		
	}
	//Splash 移植结束
	
	// 隐藏滚动条
	Runnable runnableHideScrollMessageFragment = new Runnable() {
		@Override
		public void run() {
			if(scrollingMessageFragment != null){
				if(scrollingMessageFragment.isAdded()){
					if(scrollingMessageFragment.isVisible()){
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction.hide(scrollingMessageFragment);
						fragmentTransaction.commit();
						handlerHideScrollMessageFragment.removeCallbacks(runnableHideScrollMessageFragment);
					}
				}
			}
		}
	};
	
	/**
	 *  要做的工作，连接酒店服务器
	 */
	Runnable runnableConnectHotelServer = new Runnable() {
		@Override
		public void run() {
			
			//先检查到服务器网络是否通
			LogUtils.debug("TvApplication.hotelServiceIpAddress:"+TvApplication.hotelServiceIpAddress);
			if(pingHotelServer(TvApplication.hotelServiceIpAddress)){
			    LogUtils.debug("Ping 服务器成功");
				TvApplication.stateDoingLoginHotelServer =true;
				iptvAccountInitFactory = new IptvAccountInitFactory();
				iptvAccountInitFactory.setHttpEventHandler(loginHandler);
				iptvAccountInitFactory.DownloadDatas(TvApplication.stbId, TvApplication.mAppVersionCode,TvApplication.mAppVersionName);
			   
			}else{
				
				if(pingExchangeTimes < pingExchangeMaxTime){
					if (handlerPingHotelServer != null) {
						handlerPingHotelServer.removeCallbacks(runnableConnectHotelServer);
				    	handlerPingHotelServer.postDelayed(runnableConnectHotelServer, 3000);
					}
				}else{
					loadingAnimation.stop();
					imageView.setVisibility(View.INVISIBLE);
	            	
	                FragmentTransaction ft = getFragmentManager().beginTransaction();
	                Fragment fragment = getFragmentManager().findFragmentByTag("basicDialog");
	                if (null != fragment) {
	                    ft.remove(fragment);
	                }
	         
	                HotelServerPingAlertDialogFragment dialogFragment = new HotelServerPingAlertDialogFragment();
	               
	                dialogFragment.setTitle("网络错误  错误代码 ：550");
//	                dialogFragment.setMessage(String.format("IPTV服务器%s暂时无法联通！\n请联系客房服务人员检查网络！", TvApplication.hotelServiceIpAddress));
	                dialogFragment.setMessage("        IPTV服务器暂时无法联通！\n        请联系客房服务人员检查网络！");
	                dialogFragment.setCancelable(false);
	                dialogFragment.show(ft, "basicDialog");
	                pingExchangeTimes = 0;
				}
			}
		}
	};
	
	
	/**
	 * 下载启动视频文件
	 */
	Runnable runnableDownloadBootVideo = new Runnable() {
		
		private BootVideoService mBootVideoService = new BootVideoService();
		
			@Override
			public void run() {
				
				String fileName = TvApplication.bootVideo.substring(TvApplication.bootVideo.lastIndexOf("/")+1,TvApplication.bootVideo.length());
				
				
				String url =String.format("%s/hotel/%s", TvApplication.hotelEpgHost,TvApplication.bootVideo);
				LogUtils.error("开机视频名称:"+fileName+" url:"+url);
				LogUtil.d("开机视频名称:"+fileName+" url:"+url);
				
				String filepath ="/data/hilton/bootrocky.mp4";
//				XUtil.DownLoadFile(url, filepath,new MyCallBack<File>(){  
//		            @Override  
//		            public void onSuccess(File result) {  
//		                super.onSuccess(result);  
//		                LogUtils.debug("成功下载开机视频");
//		            
//		                result.setReadable(true, false);
//		                result.setExecutable(true, false);
//		                result.setWritable(true, false);
//		            }  
//		  
//		            @Override  
//		            public void onError(Throwable ex, boolean isOnCallback) {  
//		                super.onError(ex, isOnCallback);  
//		                LogUtils.error("未能下载开机视频");
//		            }  
//		        });  
				
				
//				File fileDirectory = new File("/data/hilton/");
//				if(fileDirectory.exists()){
//					LogUtils.debug("/data/hilton/目录存在");
//					
//					File file1 = new File("/data/hilton/"+fileName);
//					if(file1.exists()){
//						LogUtils.error("下载前，开机视频文件已经存在");
//					}else{
//						LogUtils.debug("下载前，开机视频文件不存在，开始下载开机视频");
//						mBootVideoService.downfile(TvApplication.hotelEpgHost+TvApplication.bootVideo, "/data/hilton/", fileName);
//					}
//					
//				}else{
//					LogUtils.debug("/data/hilton/目录不存在");
//					
//					if(fileDirectory.mkdirs()){
//						File file1 = new File("/data/hilton/"+fileName);
//						if(file1.exists()){
//							LogUtils.error("下载前，开机视频文件已经存在");
//						}else{
//							LogUtils.debug("下载前，开机视频文件不存在，开始下载开机视频");
//							mBootVideoService.downfile(TvApplication.hotelEpgHost+TvApplication.bootVideo, "/data/hilton/", fileName);
//						}
//					}
//				}
//				
//				File file1 = new File("/data/hilton/"+fileName);
//				if(file1.exists()){
//					LogUtils.error("文件已经存在");
//				}else{
//					LogUtils.debug("文件不存在，开始下载开机视频");
//					mBootVideoService.downfile(TvApplication.hotelEpgHost+TvApplication.bootVideo, "/data/hilton/", fileName);
//				}
				//read(fileName);
				
				
				
				
			}
			
			
		    /**
		     * @author chenzheng_java 
		     * 读取刚才用户保存的内容
		     */
//		    private void read(String fileName) {
//		        try {
//		            FileInputStream inputStream = openFileInput(fileName);
//		            byte[] bytes = new byte[1024];
//		            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//		            while (inputStream.read(bytes) != -1) {
//		                arrayOutputStream.write(bytes, 0, bytes.length);
//		            }
//		            inputStream.close();
//		            arrayOutputStream.close();
//		            String content = new String(arrayOutputStream.toByteArray());
//		            //showTextView.setText(content);
//
//		        } catch (FileNotFoundException e) {
//		            e.printStackTrace();
//		        } catch (IOException e) {
//		            e.printStackTrace();
//		        }
//
//		    }
		    
//		    
//		    private void save(String filePath,String fileName,String content) {
//
//		        //String content = editText.getText().toString();
//		        try {
//		            /* 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的，
//		             * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的
//		             *   public abstract FileOutputStream openFileOutput(String name, int mode)
//		             *   throws FileNotFoundException;
//		             * openFileOutput(String name, int mode);
//		             * 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名
//		             *          该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt
//		             * 第二个参数，代表文件的操作模式
//		             *             MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖
//		             *             MODE_APPEND  私有   重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件
//		             *             MODE_WORLD_READABLE 公用  可读
//		             *             MODE_WORLD_WRITEABLE 公用 可读写
//		             *  */
//		            FileOutputStream outputStream = openFileOutput(filePath+fileName,
//		                    Activity.MODE_PRIVATE);
//		            outputStream.write(content.getBytes());
//		            outputStream.flush();
//		            outputStream.close();
//		           
//		        } catch (FileNotFoundException e) {
//		            e.printStackTrace();
//		        } catch (IOException e) {
//		            e.printStackTrace();
//		        }
//
//		    }
		};
	

	// 启动屏保
	Runnable runnableShowScreenProtect = new Runnable() {
		@Override
		public void run() {
			LogUtils.error("runner内，开始加载屏幕保护");
			if(TvApplication.useScreenProtect){
				if(TvApplication.screenProtectType.equals("PICTURE")){
					Intent intent = new Intent(HomeActivity.this,ScreenProtectActivity.class);    
		            startActivity(intent);
				}else if(TvApplication.screenProtectType.equals("VIDEO")){
					Intent intent = new Intent(HomeActivity.this,ScreenProtectVideoActivity.class);    
		            startActivity(intent);
				}
			}
		}    
	};
	
	
	
	public void doReconnect(){
		LogUtils.debug("响应OK按钮。");
		loadingAnimation.start();
		imageView.setVisibility(View.VISIBLE);
		LogUtils.debug("应急，采取延迟的方式来保证连接");
	    if (handlerPingHotelServer != null) {
	    	handlerPingHotelServer.removeCallbacks(runnableConnectHotelServer);
	    	handlerPingHotelServer.postDelayed(runnableConnectHotelServer, 100);
		}
	}
	
	/**
	 * 供告警框使用，重新注册到酒店服务器。
	 */
	public void doLoginHotelServer(){
		iptvAccountInitFactory = new IptvAccountInitFactory();
		iptvAccountInitFactory.setHttpEventHandler(loginHandler);
		iptvAccountInitFactory.DownloadDatas(TvApplication.stbId, TvApplication.mAppVersionCode,TvApplication.mAppVersionName);
		hideAlertDialog();
	}
	
	
	/**
	 * 通过 Ping 检查服务器是否可用
	 */
	private boolean pingHotelServer(String serverAddress){
		 // 增加先检查服务器是否通，目的是确认楼道交换机端口已经起来了
		String pingNum ="2";
		String m_strForNetAddress=serverAddress;
		boolean result =false;
		Process p;
		
		pingExchangeTimes ++;
		
		try {
			p = Runtime.getRuntime().exec("/system/bin/ping -c "+ pingNum + " " + m_strForNetAddress);
			int status = p.waitFor(); 
	        if (status == 0) {  
	            result=true; 
	        }else { 
	            result=false; 
	        }
	        String lost = new String();  
	        String delay = new String();  
	        BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
	         
	        String str = new String();  

	        //读出所有信息并显示          
	        String tv_PingInfo="";
	        while((str=buf.readLine())!=null){  
	            str = str + "\r\n";
	            // LogUtils.debug("输出:"+str);
	            // tv_PingInfo.append(str);
	        }
		} catch (IOException e) {

			e.printStackTrace();
		} 
		catch (InterruptedException e) {

			e.printStackTrace();
		}
		return result;
	}
	
	private void clearHistoryAndFavorate(){
		LogUtils.debug("清除点播播放记录和收藏记录");
		mHistoryFactory = new VodHistoryLocalFactory(this);
		mHistoryFactory.deletedRecords();
		vodStoreLocalFactory = new VodStoreLocalFactory(this);
		vodStoreLocalFactory.deletedRecords();
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		
	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		
		LogUtils.error("壁纸下载成功");
		TvApplication.hasBackImage =true;
		TvApplication.backImage = arg2;
		hasBackground =true;
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		
	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		
	}

	/**
	 * 营销信息发布完毕提示。
	 */
	@Override
	public void finishShow() {
		
		LogUtils.debug("关闭 促销信息，然后开始下载数据，后面加一个等待符");
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(mHomeNoticeFragment);
		fragmentTransaction.commit();
		
		if (TvApplication.shoulLoginIptv) {
			//broadcast(ACTION_START_AUTH, null);
			initView();
		} else {
			startNoLoginToIptv();
		}
	}
	
	/**
	 * 开机图片下载监听
	 * 2016-06-14
	 */
	ImageLoadingListener bootImageLoadingListener = new ImageLoadingListener() {
		
		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			LogUtils.debug("开机图片开始下载");
		}
		
		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			LogUtils.debug("开机图片下载失败："+arg0);
//			FragmentManager fragmentManager = getFragmentManager();
//			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			LogUtils.debug("创建 new welcome的fragment");
//			welcomeFragment = new WelcomeFragment();
//			fragmentTransaction.add(R.id.home_main_view, welcomeFragment);
//			fragmentTransaction.commit();
//			loadingAnimation.stop();
//			imageView.setVisibility(View.GONE);
		}
		
		@Override
		public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
			LogUtils.error("开机图片下载成功");
			String filePath ="/data/var/";
			String fileName = TvApplication.bootImage.substring(TvApplication.bootImage.lastIndexOf("/")+1,TvApplication.bootImage.length());
			
			LogUtils.error("目录下文件是否存在？"+mBootImageService.existFile(fileName,filePath));
			if(!mBootImageService.existFile(fileName,filePath)){
				LogUtils.error("文件不存在");
				try {
					mBootImageService.saveToSD(bitmap, filePath, fileName);
				} catch (IOException e) {
					e.printStackTrace();
				}
				LogUtils.error("准备发送更换开机图片信息");
				Intent intent = new Intent("SK_SYSTEM_LOGO_UPGRADE");
				intent.putExtra("filepath", filePath+fileName);
				sendBroadcast(intent);
				LogUtils.error("更换开机图片信息发送完毕");
			}else{
				LogUtils.error("文件存在");
			}
			
//			FragmentManager fragmentManager = getFragmentManager();
//			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			LogUtils.debug("创建 new welcome的fragment");
//			welcomeFragment = new WelcomeFragment();
//			fragmentTransaction.add(R.id.home_main_view, welcomeFragment);
//			fragmentTransaction.commit();
//			loadingAnimation.stop();
//			imageView.setVisibility(View.GONE);
		}
		
		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			LogUtils.error("开机图片下载失败");
//			FragmentManager fragmentManager = getFragmentManager();
//			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			LogUtils.debug("创建 new welcome的fragment");
//			welcomeFragment = new WelcomeFragment();
//			fragmentTransaction.add(R.id.home_main_view, welcomeFragment);
//			fragmentTransaction.commit();
//			loadingAnimation.stop();
//			imageView.setVisibility(View.GONE);
		}
	};

	
	
	/**
	 * 开始下载
	 */
	public void checkUpdate(){
		DeviceInfo info = new DeviceInfo(HomeActivity.this);
		
		LogUtils.debug("开始下载更新数据 hotelId:"+TvApplication.hotelId);
//		checkUpdateFactory.DownloadDatas(TvApplication.mChannel, info.getDeviceId(), info.getDeviceType(), info.getOsv(), info.getSv(),"HUAWEI",TvApplication.stbId,"HUAWEI");
		broadcast(ACTION_START_AUTH, null);
		// 临时直接加载开始
		LogUtils.error("临时直接加载开始");
	}
	
	/**
	 * 获取屏幕高宽
	 */
	public void widthAndHeight(){
		   View view = this.getWindow().getDecorView();
	        view.buildDrawingCache();
	 
	        // 获取状态栏高度
	        Rect rect = new Rect();
	        view.getWindowVisibleDisplayFrame(rect);
	        TvApplication._x=0;
	        TvApplication._y= rect.top;
	        Display display = this.getWindowManager().getDefaultDisplay();
	 
	        // 获取屏幕宽和高
			TvApplication.width= display.getWidth();
			TvApplication.height = display.getHeight();
	        
	}
	
}

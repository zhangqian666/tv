package com.iptv.rocky.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xutils.x;

import com.ikantech.support.utils.YiLog;
import com.iptv.common.data.BillingTypeOfHotelGuest;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.EnumType.StbOrientation;
import com.iptv.common.data.GuestInfoToClient;
import com.iptv.common.data.StbType;
import com.iptv.common.utils.AtvUtils;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.CrashHandler;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.auth.CUAuthService;
import com.iptv.rocky.auth.CUZTEAuthService;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.utils.ScreenUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class TvApplication extends Application {

    public static float density = -1.0f;
    public static float pixelHeight = -1.0f;
    public static float dpiHeight = -1.0f;
    public static float pixelWidth = -1.0f;
    public static float dpiWidth = -1.0f;

    public static int sTvItemPadding;
    public static int sTvChannelUnit;
    public static int sTvItemTopPadding;
    public static int sTvTabHeight;
    public static int sTvListTopPadding;
    public static int sTvReflectionHeight;
    public static int sTvLeftMargin;
    public static int sTvFloatLayerHeight;
    public static float sTvMasterTextSize;
    public static float sTvHomeCommViewTextSize;
    public static float sTvFloatTextSize;
    public static float sTvTitleViewTextSize;
    public static float sTvSelectorTextSize;//选集、tag文字 大小
    public static int sTvTabItemPadding;

    public static String mAppVersionName;
    public static String mDeviceID;
    public static int mAppVersionCode;
    
    public static String mChannel = "UTSTAR"; // TCL,HISENSE,HYBROAD,UTSTAR SKYWORTH
    public static String stbId;
	public static String mAuthBaseUrl;
	public static String authUrl;
	public static String preAuthUrl;
	
    public static String hotelEpgHost;
    public static String hotelServiceIpAddress;
    public static String account;
    public static String passwd;
    
    public static String hotelId;
    public static String roomId;
    public static String welcomeId;
    public static String groupId;
    public static boolean hasSpecialWelcome;
    
    public static int  height;
    public static int  width;
    public static int  _x;
    public static int  _y;
    
    public static boolean useProxy = false; //在外网时候启用，在内网关闭
    public static EnumType.Platform platform = null; // DEV:开发平台  RUN: 正式平台
    
    // 定义用户目前使用的语言 zh en
    public static String language = "zh";

    public static  BillingTypeOfHotelGuest billingType;
    public static double perMonthPrice; 	// 包月的每月价格
    public static boolean payForDaylyPayVod; 	// 是否需要支付包天点播费用
    public static double daylyPayVodPrice; 	//包天点播价格 
    public static boolean payForPayPerVod; 	// 单点单付是否免费
 /**
  * 每日价格
  */
    public static double perDayPrice;	
 /** 
  * 每个片子的价格
  */
    public static double perVodPrice; 	
  /**
   * 是否显示英文页
   */
    public static boolean showEnglish;
    
    public static Calendar shouldLoginDate;
    public static Calendar shouldLoginIptvDate; //登陆3A服务器时间
    
    
    
    public static boolean doNetState = false;
    public static boolean shoulLoginIptv =false;
    
    public static boolean stateLoginIptv =false;
    public static boolean stateDownloadInitdata =false;
    public static boolean stateDoingLoginHotelServer =false;
    public static boolean stateLoginHotelServer =false;
    
    public static String position;
    public static String status="BEGIN";
    
    public static boolean hasBackImage;
    public static Bitmap backImage;
    
    public static boolean useScreenProtect;
    public static String screenProtectType;
    public static String screenProtectId;
    
    public static StbType stbType;
    public static int rotation;
    public static StbOrientation stbOrientation;
    public static String idsContent;
    
    public static boolean homeShowPromptNotice; //欢迎界面后显示促销告示信息，先用图片。后面再做模板。
    
    public static String bootImage;
    public static String bootVideo;

    
    public static List<GuestInfoToClient> guests;
    
    private static TvApplication instance;
    
    public static TvApplication getInstance() {
    	return instance;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//Xutils初始化
        
        shouldLoginDate = Calendar.getInstance();
        shouldLoginDate.add(Calendar.MINUTE, 10);
        
        // 处理全局异常
        CrashHandler crashHandler = CrashHandler.getInstance();

        // 注册crashHandler
        crashHandler.init();
        
        
    
        // 打开日志
        LogUtils.LOG_LEVEL = Log.DEBUG - 1;
        YiLog.ENABLE_INFO = true;
        
        instance = this;
        init();
        initScreen();
        
        AtvUtils.sContext = getApplicationContext();
        
        mDeviceID = AtvUtils.generateUUID();
        mAppVersionName = AtvUtils.getAppVersionName();
        mAppVersionCode = AtvUtils.getAppVersionCode();
    }
    
    private void init() {
		if (useProxy) {
    		hotelEpgHost = "http://110.249.173.111:20000";
    		hotelServiceIpAddress ="110.249.173.111";
    	} else {

    		 //hotelEpgHost = "http://10.0.2.210:8080"; 	// 二枢纽服务器地址
    		 hotelEpgHost = "http://10.0.2.210"; 	// 二枢纽服务器地址
    		 hotelServiceIpAddress ="10.0.2.210";
    		 //测试服务器
//    		 hotelEpgHost = "http://10.0.2.211"; 	// 二枢纽服务器地址
//    		 hotelServiceIpAddress ="10.0.2.211";
    	}
    	guests = new ArrayList<GuestInfoToClient>();
    }
    
    /******
     * 从酒店服务器提取信息后，再根据服务器信息初始化平台相关地址
     * 
     ******/
    public static void initPlatformAuthUrl() {
    	if(platform==EnumType.Platform.DEVHUAWEI){
    		if (useProxy) {
    			mAuthBaseUrl = "http://110.249.173.111:33200";
        	} else {
        		mAuthBaseUrl = "http://10.0.3.71:33200";
        	}
    		authUrl = String.format("%s/EPG/jsp/AuthenticationURL", mAuthBaseUrl);
    	}else if(platform==EnumType.Platform.RUNHUAWEI){
    		mAuthBaseUrl = "http://10.0.3.77:8082";
    		preAuthUrl = String.format("%s/EDS/jsp/AuthenticationURL", mAuthBaseUrl);
    	}else if(platform==EnumType.Platform.ZTE){
    		mAuthBaseUrl = "http://10.0.5.72:4337";
    		authUrl= String.format("%s/iptvepg/platform/index.jsp", mAuthBaseUrl);
    		LogUtils.info("mAuthBaseUrl---->"+mAuthBaseUrl);
    	}else{
    		LogUtils.error("platform数据错误，platform="+platform);
    	}
    }

    private void initScreen() {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) getSystemService("window")).getDefaultDisplay().getMetrics(localDisplayMetrics);

        if (density < 0.0f) {
            density = localDisplayMetrics.density;

            pixelHeight = localDisplayMetrics.heightPixels;
            if (pixelHeight >= 672 && pixelHeight <= 720) {
                pixelHeight = 720;
            }
            pixelWidth = localDisplayMetrics.widthPixels;

            dpiHeight = pixelHeight / density;
            dpiWidth = pixelWidth / density;

            sTvItemPadding = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.home_item_padding);
            sTvChannelUnit = ScreenUtils.getChannelUnit();
            sTvItemTopPadding = ScreenUtils.getHomeItemTopPadding();
            sTvTabHeight = ScreenUtils.getTabLayoutHeight();
            sTvListTopPadding = ScreenUtils.getListTopPadding();
            sTvReflectionHeight = ScreenUtils.getReflectionHeight();
            sTvLeftMargin = ScreenUtils.getHomeLeftMargin();
            sTvFloatLayerHeight = (int) (sTvTabHeight / 1.7);
            sTvMasterTextSize = ScreenUtils.getMasterTextSize();
            sTvHomeCommViewTextSize = ScreenUtils.getHomeCommViewTextSize();
            sTvFloatTextSize = ScreenUtils.getFloatTextSize();
            sTvTitleViewTextSize = ScreenUtils.getTitleViewTextSize();
            sTvSelectorTextSize = (float) (sTvMasterTextSize * 0.8);
            sTvTabItemPadding = ScreenUtils.getTabItemPadding();

            initImageLoader(getApplicationContext());
        }
	}
	
    private static void initImageLoader(Context context) {	
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(Constants.cImageCacheMaxSize); // 100 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		//config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
    }
    
    public static void startAuth()
	{
		try {
			LogUtils.error("PLATFORM:"+platform);
			String authServiceAddr=null;
			if(platform == EnumType.Platform.RUNHUAWEI){
				LogUtils.info("正式平台 preAuthUrl:"+preAuthUrl);
				 authServiceAddr = preAuthUrl;
			}else if(platform == EnumType.Platform.DEVHUAWEI){
				LogUtils.info("开发平台 authUrl "+authUrl);
				 authServiceAddr = authUrl;
			}else if(platform == EnumType.Platform.ZTE){
				LogUtils.info("中兴平台 authUrl "+authUrl);
				 authServiceAddr = authUrl;
			}
			
			
			Pattern pattern = Pattern.compile("(http://(\\d+\\.\\d+\\.\\d+.\\d+):([\\d]+)[^/]+)");
			Matcher m = pattern.matcher(authServiceAddr);
			while (m.find()) {
				String authBaseUrl = m.group(1);
				
				if(platform==EnumType.Platform.RUNHUAWEI){
					LogUtils.info("正式平台  authServiceAddr:"+authServiceAddr +"  authBaseUrl "+authBaseUrl);
					CUAuthService.setAuthBaseUrl(authBaseUrl + "/EDS/jsp/");
					CUAuthService.setAuthUrl(authServiceAddr);
					CUAuthService.setmPreAuthUrl(authServiceAddr);
				}else if(platform==EnumType.Platform.DEVHUAWEI){
					LogUtils.info("开发平台 authServiceAddr:"+authServiceAddr +"  authBaseUrl "+authBaseUrl);
					CUAuthService.setAuthBaseUrl(authBaseUrl + "/EPG/jsp/");
					CUAuthService.setAuthUrl(authServiceAddr);
				}else if(platform==EnumType.Platform.ZTE){
					LogUtils.info("开发平台中兴 authServiceAddr:"+authServiceAddr +"  authBaseUrl "+authBaseUrl);
					CUZTEAuthService.setAuthBaseUrl(authBaseUrl + "/iptvepg/platform/");
					CUZTEAuthService.setAuthUrl(authServiceAddr);
				}
				
				//YiLog.getInstance().i("authBaseUrl %s", authBaseUrl);
			}
			if (platform==EnumType.Platform.ZTE) {
				LogUtils.error("中兴账号密码------>>>"+account+"      "+passwd);
				CUZTEAuthService.setUserId(account);
				CUZTEAuthService.setPasswd(passwd);
			}else{
				CUAuthService.setUserId(account);
				CUAuthService.setPasswd(passwd);

			}
			if(platform==EnumType.Platform.RUNHUAWEI){
				IPTVUriUtils.mAuthServiceBinder.startNormalPlatformAuth();
			}else if(platform==EnumType.Platform.DEVHUAWEI){
				IPTVUriUtils.mAuthServiceBinder.startAuth();
			}else if(platform==EnumType.Platform.ZTE){
				IPTVUriUtils.mAuthServiceBinder.startAuth();
			}
			
		} catch (Exception e) {
			LogUtils.error("未知错误!1------->>"+e.toString());
			Toast.makeText(AtvUtils.sContext, "未知错误!", Toast.LENGTH_LONG).show();
		}
	}
    
    /**
     * 登录中，避免aaa失效，再次登录
     */
    public static void reStartAuth()
	{
		try {
			String authServiceAddr=null;
			if(platform==EnumType.Platform.RUNHUAWEI){
				LogUtils.info("正式平台 preAuthUrl:"+preAuthUrl); 
				authServiceAddr = preAuthUrl;
			}else if(platform==EnumType.Platform.DEVHUAWEI){
				LogUtils.info("开发平台 authUrl "+authUrl);
				authServiceAddr = authUrl;
			}else if (platform==EnumType.Platform.ZTE) {
				LogUtils.info("开发平台 authUr中兴l "+authUrl);
				authServiceAddr = authUrl;
			}
			
			Pattern pattern = Pattern.compile("(http://(\\d+\\.\\d+\\.\\d+.\\d+):([\\d]+)[^/]+)");
			Matcher m = pattern.matcher(authServiceAddr);
			while (m.find()) {
				String authBaseUrl = m.group(1);
				
				if(platform==EnumType.Platform.RUNHUAWEI){
					LogUtils.info("正式平台  authServiceAddr:"+authServiceAddr +"  authBaseUrl "+authBaseUrl);
					CUAuthService.setAuthBaseUrl(authBaseUrl + "/EDS/jsp/");
					CUAuthService.setAuthUrl(authServiceAddr);
					CUAuthService.setmPreAuthUrl(authServiceAddr);
				}else if(platform==EnumType.Platform.DEVHUAWEI){
					LogUtils.info("开发平台 authServiceAddr:"+authServiceAddr +"  authBaseUrl "+authBaseUrl);
					CUAuthService.setAuthBaseUrl(authBaseUrl + "/EPG/jsp/");
					CUAuthService.setAuthUrl(authServiceAddr);
				}else if(platform==EnumType.Platform.ZTE){
					LogUtils.info("开发平台 authServiceAddr:"+authServiceAddr +"  authBaseUrl "+authBaseUrl);
					CUZTEAuthService.setAuthBaseUrl(authBaseUrl + "/iptvepg/platform/");
					CUZTEAuthService.setAuthUrl(authServiceAddr);
				}
				
				YiLog.getInstance().i("authBaseUrl %s", authBaseUrl);
			}
			if (platform==EnumType.Platform.ZTE) {
				CUZTEAuthService.setUserId(account);
				CUZTEAuthService.setPasswd(passwd);
			}else {
				CUAuthService.setUserId(account);
				CUAuthService.setPasswd(passwd);
			}
			if(platform==EnumType.Platform.RUNHUAWEI){
				IPTVUriUtils.mAuthServiceBinder.startNormalPlatformAuth();
			}else if(platform==EnumType.Platform.DEVHUAWEI){
				IPTVUriUtils.mAuthServiceBinder.startAuth();
			}else if(platform==EnumType.Platform.ZTE){
				IPTVUriUtils.mAuthServiceBinder.startAuth();
			}
			
		} catch (Exception e) {
			LogUtils.error("未知错误!2------->>"+e.getMessage());
			Toast.makeText(AtvUtils.sContext, "未知错误!", Toast.LENGTH_LONG).show();
		}
	}
    

}

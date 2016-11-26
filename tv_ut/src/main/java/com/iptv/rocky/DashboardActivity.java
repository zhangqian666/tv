package com.iptv.rocky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.DashboardInfo;
import com.iptv.common.data.DashboardPicture;
import com.iptv.common.data.EnumType;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.utils.NetWorkUtil;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.DashboardJsonFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.BackgroundMusic;
import com.iptv.rocky.view.dashboard.DashboardMasterLayout;
import com.iptv.rocky.view.dashboard.DashboardMediaLoading;
import com.iptv.rocky.view.dashboard.DashboardVideoView;
import com.ott.player.BaseMediaController;
import com.ui.player.TVMediaController;

import java.util.ArrayList;
import java.util.List;


/*****************************************
 * 公告信息管理
 * 2016-02-06
 *
 ******************************************/
public class DashboardActivity extends BaseActivity {

	private DashboardMasterLayout mDashboardMasterLayout;
	
	private DashboardJsonFactory dashboardFactory;
	private ArrayList<DashboardInfo> dashboardInfos;
	
    public static final int DIALOG_ERROR = 1;	/** 播放错误 */
    private static final int DIALOG_EXIT = 2;	/** 退出播放 */

    private static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    
	private int subVideoChannelID;
	
	// 电视剧信息；
	private int isSitcom = 0;
	private EnumType.Platform platform;
	
	private String playUrl;
	// 启动屏幕保护
	private Handler handlerRefreshDashboard = new Handler();
	
    private BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            LogUtils.error(" 收到的信息:action:" + action);
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
            {
                if (NetWorkUtil.isNetworkAvailable(DashboardActivity.this))
                {
                    // 移动网络
                    if (mVideoView != null)
                    {
                        mVideoView.play();
                    }
                }
            }
            else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction()))
            {
                // 解锁
            }
        }
    };

    private DashboardVideoView mVideoView;
    private DashboardMediaLoading mLoadingView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
        TvApplication.status = "SIGNAGE";
        quiteBackgroundMusic();
        
        LogUtils.debug("Dashboard onCreate");

        downloadDatas();
        
        //getPlayInfo();
       
        
	}



    /** single instance新发过来的uri，会通过这个方法获取 */
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        
        LogUtils.debug("onNewIntent");

        if (intent == null)
        {
            return;
        }
        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0)
        {
            // FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
            // this activity is being launched from history (longpress home key).
            if (mVideoView != null)
            {
                LogUtils.debug("this activity is being launched from history (longpress home key).");
                return;
            }
        }

        // 更新intent
        setIntent(intent);

        // if (mVideoView != null)
        // {
        // mVideoView.stopPlayback();
        // }

        if (!getParam())
        {
            finish();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        quiteBackgroundMusic();
        LogUtils.error("onStart");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        
        LogUtils.error("onResume");

        //TV 不需要处理锁屏和activity pause情况
        //if (mVideoView != null)
        //{
        //    mVideoView.start();
        //}
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        LogUtils.debug("onPause");

        // tv 休眠时是否mediaplayer
//        if (!isFinishing())
//        {
//            finish();
//        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        LogUtils.debug("onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        
        LogUtils.error("onDestroy");

        uninit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        
        showDialog(DIALOG_EXIT);
    }

    @Override
    public void finish()
    {
        if (mVideoView != null)
        {
            mVideoView.stopPlayback(true);
        }

        super.finish();
    }
    
    /**
     * 创建对话框
     * 
     * @param id 对话框ID
     * @return 创建的对话框
     */
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case DIALOG_EXIT:
                // 退出播放
                return showBackDialog();
                
            case DIALOG_ERROR:
                // 退出播放
                return showErrorDialog();

            default:
                break;
        }
        return null;
    }
    
    /** 播放报错 */
    public Dialog showErrorDialog()
    {
        try
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_title);
            builder.setMessage(R.string.player_error);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    finish();
                }
            });
            //builder.setNegativeButton(R.string.cancel, null);
            return builder.create();
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString());
        }
        return null;
    }

    /** 退出播放确认 */
    public Dialog showBackDialog()
    {
        try
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_title);
            builder.setMessage(R.string.player_exit_confirm);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                	//增加上报播放记录内容
                	mVideoView.exit();
                    finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            return builder.create();
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString());
        }
        return null;
    }

    private void init()
    {
        setContentView(R.layout.dashboard_video_play);
        mVideoView =  (DashboardVideoView) findViewById(R.id.dashboard_videoview);
       
        View bufferView = findViewById(R.id.dashboard_buffer);
        mVideoView.setBufferView(bufferView);

        LayoutInflater inflater = getLayoutInflater();
        mLoadingView = (DashboardMediaLoading) inflater.inflate(R.layout.dashboard_media_loading, null);
        mLoadingView.setVisibility(View.GONE);
        // Create media controller
        BaseMediaController mMediaController = new TVMediaController(this);

        // 设置MediaController
        mVideoView.setMediaController(mMediaController);
        
        ViewGroup parent = (ViewGroup) mVideoView.getParent();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        parent.addView(mLoadingView, lp);
        mLoadingView.bringToFront();
        mLoadingView.show("正在加载公告视频");
        mVideoView.mLoadingView = mLoadingView;
    }
    
    
	/**
	 * 下载公告信息
	 */
	public void downloadDatas() {
		dashboardFactory = new DashboardJsonFactory();
		dashboardFactory.setHttpEventHandler(dashBoardHandler);
		dashboardFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		dashboardFactory.DownloadDatas();
	}

    private boolean getParam()
    {
        Intent intent = getIntent();
        if (intent == null)
        {
            return false;
        }
        // 暂时不显示加载
        /*if (mLoadingView != null)
        {
            mLoadingView.show("");
        }*/

        String action = intent.getAction();
        LogUtils.error("action:" + action);
        //if (Intent.ACTION_VIEW.equals(action))
        {
        	long channelid = 0;
        	String VODNAME = "";
        	String PICPATH = "";
        	LogUtils.error("播放地址:"+playUrl);
        	mVideoView.setParams(playUrl, subVideoChannelID,platform, channelid, VODNAME, PICPATH);
        }
        return true;
    }

    private void registerReceiver()
    {
        try
        {
            // 注册广播
            IntentFilter filter = new IntentFilter();

            // 音量改变
            filter.addAction(VOLUME_CHANGED_ACTION);
            // 网络
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            // 解锁
            filter.addAction(Intent.ACTION_USER_PRESENT);
            registerReceiver(mReceiver, filter);
        }
        catch (Exception e)
        {
        }
    }

    private int getMusicVolume()
    {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // 音乐音量
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        LogUtils.error("MUSIC: max : " + max + ",  current : " + current);

        return (current * 100) / max;
    }

    private void setMusicVolume(int volume)
    {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // 音乐音量
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume * max / 100, AudioManager.FLAG_SHOW_UI);
    }

    private void setMusicMute(boolean mute)
    {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // 静音
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mute ? 0 : max / 2, AudioManager.FLAG_SHOW_UI);
    }

    /** 是否资源 */
    protected void uninit()
    {
        // if (mVideoView != null)
        // {
        // mVideoView.setParams(null);
        // }
        if (mVideoView.getParent() != null)
        {
            ViewGroup vg = (ViewGroup) mVideoView.getParent();
            vg.removeAllViews();
        }
        unregisterReceiver();

        /*try
        {
            dismissDialog(DIALOG_EXIT);
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString());
        }
        try
        {
            dismissDialog(DIALOG_ERROR);
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString());
        }*/
    }

    private void unregisterReceiver()
    {
        try
        {
            unregisterReceiver(mReceiver);
        }
        catch (Exception e)
        {
        }
    }

    /**
     * 是否处于锁屏状态
     * 
     * @param c 上下文
     * @return True 锁屏状态
     * @see [类、类#方法、类#成员]
     */
    public static final boolean isScreenLocked(Context c)
    {
        android.app.KeyguardManager mKeyguardManager = (KeyguardManager) c.getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }
    
	private void quiteBackgroundMusic() {
		if (BackgroundMusic.getInstance(this).isBackgroundMusicPlaying()) {
			BackgroundMusic.getInstance(this).stopBackgroundMusic();
		}
	}
	
	private HttpEventHandler<ArrayList<DashboardInfo>> dashBoardHandler = new HttpEventHandler<ArrayList<DashboardInfo>>() {
		@Override
		public void HttpSucessHandler(ArrayList<DashboardInfo> result) {
			
			int size = result.size();
			LogUtils.debug("公告数据的尺寸:"+size);
	    	dashboardInfos = result;
	    	List<DashboardPicture> pictureListOutput = new ArrayList<DashboardPicture>();
	    	List<String> videoUrls = new ArrayList<String>();
	    	String type = null;
	    	
	    	for(DashboardInfo info:dashboardInfos){
	    		LogUtils.debug("消息类型:"+info.getType()+ " ,内容消息类型:"+info.getContentType());
	    		if(info.getContentType() != null){
	    			if(info.getContentType().equals("PICTURE")){
		    			List<DashboardPicture> pictures = info.getImage();
			    		if(pictures != null){
			    			for(DashboardPicture picture:pictures){
			    				LogUtils.debug("图片:"+picture.getBackgroundImage());
				    			pictureListOutput.add(picture);
				    			type = "PICTURE";
				    		}
			    		}
		    		}else if(info.getContentType().equals("VIDEO")){
		    			videoUrls.add(info.getPlayUrl());
		    			type = "VIDEO";
		    		}
	    		}
	    	}
	    	
	    	if(dashboardInfos.size()>0){
	    		if(type.equals("PICTURE")){
			    	TvApplication.status ="SIGNAGE_PICTURE";
					mDashboardMasterLayout = (DashboardMasterLayout) findViewById(R.id.home_dashboard_view);
					mDashboardMasterLayout.setInfos(dashboardInfos);
					mDashboardMasterLayout.setVisibility(View.VISIBLE);
					mDashboardMasterLayout.beginShowPictures();
					if(mVideoView != null){
						if(mVideoView.getVisibility() == View.VISIBLE ){
							mVideoView.setVisibility(View.GONE);
							uninit();
						}
					}
		    	}else if(type.equals("VIDEO")){
		    		if(mDashboardMasterLayout != null){
		    			mDashboardMasterLayout.setVisibility(View.GONE);
		    		}
		    		LogUtils.debug("VIDEO，开始播放处理" + (mVideoView == null));
		    		if(mVideoView == null){
		    			init();
		    			registerReceiver();
		    		}
		    		
		    		TvApplication.status ="SIGNAGE_VIDEO";
		    		if( dashboardInfos.get(0).getPlayUrl().equals(playUrl)){
		    			
		    		}else{
		    			playUrl = dashboardInfos.get(0).getPlayUrl();
		    			if (!getParam())
			            {
			                finish();
			                return;
			            }
		    		}
		    	}
	    	}else{
	    		//做没有信息的提示，或者就什么都不显示，一般不应该出现这个，因为每个点都得配置最起码一个默认任务
	    		LogUtils.error("服务器没有这个点的公告数据。数据尺寸为0");
	    		if(mDashboardMasterLayout != null){
	    			mDashboardMasterLayout.destroy();
	    		}
	    		if(mVideoView != null){
					if(mVideoView.getVisibility() == View.VISIBLE ){
						mVideoView.setVisibility(View.GONE);
					}
				}
	    		TvApplication.status ="SIGNAGE_NO_DATA";
	    		dashboardInfos.clear();
	    	}


	    	
	    	//if(handlerRefreshDashboard.hasCallbacks(runnableRefreshDashboardInfo)){
	    	//	handlerRefreshDashboard.removeCallbacks(runnableRefreshDashboardInfo);
			//}
	    	handlerRefreshDashboard.postDelayed(runnableRefreshDashboardInfo, 60000);
		}

		@Override
		public void HttpFailHandler() {
			//TvUtils.processHttpFail();
		}
	};
	
	/**
	 *  定时更换信息
	 */
	Runnable runnableRefreshDashboardInfo = new Runnable() {
		@Override
		public void run() {
			dashboardFactory.DownloadDatas();
		}
	};
	
}

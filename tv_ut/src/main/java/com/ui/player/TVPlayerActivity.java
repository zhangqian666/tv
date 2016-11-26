package com.ui.player;

import java.util.ArrayList;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.utils.NetWorkUtil;
import com.iptv.rocky.R;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.utils.BackgroundMusic;
import com.ott.player.BaseMediaController;
import com.ui.player.fragment.ChoosePackage;
import com.ui.player.fragment.ChoosePayMethod;
import com.ui.player.fragment.ChoosePlayPosition;
import com.ui.player.fragment.OrderStatusBar;

/** 
 * tv 播放器 
 * */
public class TVPlayerActivity extends BaseActivity
	{
	
	private static final String ACTION_REPORT_VOD_PLAY_RECORD ="com.virgintelecom.iptv.VOD.PLAYRECORD";
	private static final String ACTION_REPORT_VOD_START_PLAY ="com.virgintelecom.iptv.VOD.START.PLAY";
	private static final String ACTION_SHOWCHOOSEMETHOD = "com.virgintelecom.iptv.ott.showchoosemethod";
	
	// 转发服务器来的通知信息
	private static final String ACTION_SHOWORDERSTATUS = "com.virgintelecom.iptv.ott.showorderstatus";
	private static final String ACTION_SHOWPACKAGECHOOSE ="com.virgintelecom.iptv.ott.showpackagechoose";
	
    /** 播放错误 */
    public static final int DIALOG_ERROR = 1;

    /** 退出播放 */
    private static final int DIALOG_EXIT = 2;

    private static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    
    private VodDetailInfo vodDetailInfo = null;
	private VodChannel vodChannel = null;
	private String HttpURL;
	private String subVideoChannelID;
	
	// 电视剧信息；
	private int isSitcom = 0;
	private EnumType.Platform platform;
	
	private String status; 
	
	private OrderStatusBar orderStatusBar;
	private ChoosePackage choosePackage;
	private ChoosePayMethod choosePayMethod;
	private ChoosePlayPosition choosePlayPosition;
	
    private BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            LogUtils.error(" 收到的信息:action:" + action);
            
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
            {
                // 网络改变（本地文件播放，不需要网络）
                // if (mVideoView != null && mVideoView.params != null &&
                // !mVideoView.params.isPlayFile())
                // {
                // mVideoView.play();
                // }
                if (NetWorkUtil.isNetworkAvailable(TVPlayerActivity.this))
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

    private TVVideoView mVideoView;
    private TVMediaLoading mLoadingView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        LogUtils.debug("onCreate");
        
        // 全屏
        // super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        // WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//vodPlayRecord = new VodPlayRecord();
		status = "PLAY";

        init();
        getPlayInfo();
       if (!getParam())
        {
            finish();
            return;
        }
        registerReceiver();
    }
    
    
    
    
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.error( "onkeydown keycode = " + keyCode);
		boolean todo = false;
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			LogUtils.error("状态:"+mVideoView.status);
			if(mVideoView.status.equals("CHOOSE_PACKAGE")){
				if(mVideoView.finishedPrePlay){
					onBackPressed();
					return true;
				}else{
					choosePackage.setVisibility(View.GONE);
					orderStatusBar.setVisibility(View.VISIBLE);
					mVideoView.start();
					mVideoView.status ="PLAY";
					return true;
				}
				
			}else if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
				
				return true;
			}
			break;
		case KeyEvent.KEYCODE_0:
			if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_1:
			if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_2:
			if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_3:
			if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_4:
			if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_5:
			if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_6:
			if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_7:
			if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_8:
			if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;	
		case KeyEvent.KEYCODE_9:
			if(mVideoView.status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;				
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
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
		BackgroundMusic.getInstance(this).playBackgroundMusic("music/background_ex.mp3", true);
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
        setContentView(R.layout.tv_player);
        mVideoView = (TVVideoView) findViewById(R.id.tv_player_videoview);
       
        View bufferView = findViewById(R.id.tv_player_buffer);
        mVideoView.setBufferView(bufferView);

        LayoutInflater inflater = getLayoutInflater();
        mLoadingView = (TVMediaLoading) inflater.inflate(R.layout.tv_media_loading, null);
        
        // Create media controller
        BaseMediaController mMediaController = new TVMediaController(this);

        // 设置MediaController
        mVideoView.setMediaController(mMediaController);
        
        ViewGroup parent = (ViewGroup) mVideoView.getParent();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        parent.addView(mLoadingView, lp);
        mLoadingView.bringToFront();
        mLoadingView.show("");
        mVideoView.mLoadingView = mLoadingView;
        orderStatusBar = (OrderStatusBar) findViewById(R.id.order_status_bar);
        choosePackage = (ChoosePackage) findViewById(R.id.choose_package);
        choosePayMethod = (ChoosePayMethod)findViewById(R.id.choose_pay_method);
        choosePlayPosition = (ChoosePlayPosition) findViewById(R.id.vod_play_choose_play_position);
    }
    
    /**
     * 获取播放信息
     * 2015-11-21
     */
    private boolean getPlayInfo(){
        Intent intent = getIntent();
        if (intent == null)
        {
            return false;
        }

        if (mLoadingView != null)
        {
            mLoadingView.show("");
        }

        String action = intent.getAction();
        // LogUtils.error("action:" + action);
        //if (Intent.ACTION_VIEW.equals(action))
        //{
        	String channelid = null;
        	String VODNAME = "";
        	String PICPATH = "";
        	HttpURL = intent.getStringExtra(IPTVUriUtils.PLAY_URL_PARAMS);
			subVideoChannelID = intent.getStringExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS);
        	Object o = (Object) intent.getSerializableExtra(IPTVUriUtils.EXTRA_PARAMS);
			if (o instanceof VodDetailInfo) {
				vodDetailInfo = (VodDetailInfo) o;
				channelid = vodDetailInfo.VODID;
				VODNAME = vodDetailInfo.VODNAME;
				PICPATH = vodDetailInfo.PICPATH;
				isSitcom = vodDetailInfo.ISSITCOM;
				platform = vodDetailInfo.platform;
				//vodPlayRecord.setOrdered(vodDetailInfo.ordered);
				
				// 对VODNAME重新赋值
				if (isSitcom == 1) {
					ArrayList<VodChannel> subVodIdList = vodDetailInfo.SUBVODIDLIST;
					for (int i = 0; i < subVodIdList.size(); i++) {
						VodChannel vodChannel = subVodIdList.get(i);
		             	if (vodChannel.VODID.equals(subVideoChannelID)) {
		             		VODNAME = vodDetailInfo.VODNAME + " (第" + String.format("%02d", vodDetailInfo.SUBVODNUMLIST.get(i)) + "集)";
		             	}
					}
		        }
				 mVideoView.initVodDetailInfo(vodDetailInfo,orderStatusBar,choosePackage,choosePayMethod,choosePlayPosition);
			}
			else if (o instanceof VodChannel) {
				vodChannel = (VodChannel) o;
				channelid = vodChannel.VODID;
				VODNAME = vodChannel.VODNAME;
				PICPATH = vodChannel.PICPATH;
				platform = vodChannel.platform;
			}
			LogUtils.error("获取到播放地址:"+HttpURL);
        return true;
    }
    

    private boolean getParam()
    {
        Intent intent = getIntent();
        if (intent == null)
        {
            return false;
        }

        if (mLoadingView != null)
        {
            mLoadingView.show("");
        }

        String action = intent.getAction();
        LogUtils.error("action:" + action);
        //if (Intent.ACTION_VIEW.equals(action))
        {
        	String channelid = null;
        	String VODNAME = "";
        	String PICPATH = "";
        	HttpURL = intent.getStringExtra(IPTVUriUtils.PLAY_URL_PARAMS);
			subVideoChannelID = intent.getStringExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS);
        	Object o = (Object) intent.getSerializableExtra(IPTVUriUtils.EXTRA_PARAMS);

        	
			if (o instanceof VodDetailInfo) {
				vodDetailInfo = (VodDetailInfo) o;
				channelid = vodDetailInfo.VODID;
				VODNAME = vodDetailInfo.VODNAME;
				PICPATH = vodDetailInfo.PICPATH;
				isSitcom = vodDetailInfo.ISSITCOM;
				platform = vodDetailInfo.platform;
				//vodPlayRecord.setOrdered(vodDetailInfo.ordered);
				
				// 对VODNAME重新赋值
				if (isSitcom == 1) {
					ArrayList<VodChannel> subVodIdList = vodDetailInfo.SUBVODIDLIST;
					for (int i = 0; i < subVodIdList.size(); i++) {
						VodChannel vodChannel = subVodIdList.get(i);
		             	if (vodChannel.VODID == subVideoChannelID) {
		             		VODNAME = vodDetailInfo.VODNAME + " (第" + String.format("%02d", vodDetailInfo.SUBVODNUMLIST.get(i)) + "集)";
		             	}
					}
		        }
			}
			else if (o instanceof VodChannel) {
				vodChannel = (VodChannel) o;
				channelid = vodChannel.VODID;
				VODNAME = vodChannel.VODNAME;
				PICPATH = vodChannel.PICPATH;
				platform = vodChannel.platform;
			}
        	mVideoView.setParams(HttpURL, subVideoChannelID,platform, channelid, VODNAME, PICPATH);
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
            filter.addAction(ACTION_REPORT_VOD_START_PLAY);
            filter.addAction(ACTION_SHOWORDERSTATUS);
            filter.addAction(ACTION_SHOWPACKAGECHOOSE);
            registerReceiver(mReceiver, filter);// 注册一个Receiver，接收广播消息
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

        try
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
        }
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
	
}

package com.ui.player;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.data.VodPlayRecord;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.utils.NetWorkUtil;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.local.VodHistoryLocalFactory;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.AppCommonUtils;
import com.ott.player.BaseMediaPlayer;
import com.ott.player.BaseVideoView;
import com.ui.player.fragment.ChoosePackage;
import com.ui.player.fragment.ChoosePackage.ChoosePayMethodListener;
import com.ui.player.fragment.ChoosePayMethod;
import com.ui.player.fragment.ChoosePayMethod.BackToChoosePackageListener;
import com.ui.player.fragment.ChoosePayMethod.ChoosePlayPositionListener;
import com.ui.player.fragment.ChoosePlayPosition;
import com.ui.player.fragment.ChoosePlayPosition.StartPlayOrderedVodListener;
import com.ui.player.fragment.OrderStatusBar;

/**
 * 
 */
public class TVVideoView extends BaseVideoView implements ChoosePayMethodListener,ChoosePlayPositionListener,StartPlayOrderedVodListener,BackToChoosePackageListener
{
	
	// 转发服务器来的通知信息
	private static final String ACTION_SHOWORDERSTATUS = "com.virgintelecom.iptv.ott.showorderstatus";
	private static final String ACTION_SHOWPACKAGECHOOSE ="com.virgintelecom.iptv.ott.showpackagechoose";
	private static final String ACTION_REPORT_VOD_PLAY_RECORD ="com.virgintelecom.iptv.VOD.PLAYRECORD";
	private static final String ACTION_REPORT_VOD_START_PLAY ="com.virgintelecom.iptv.VOD.START.PLAY";
	
    /** dmr 通知进度改变 */
    private static final int NOTIFY_PROGRESS = 1;

    public TVMediaLoading mLoadingView;

    private GetPlayinfoThread getPlayinfoThread;
    
    private boolean isFactoryTest = false;
    
    private Context context;
    
    /**
     * 提前20s播放-->改成5秒
     */
    public static final int PLAY_FROMLASTPOSITION = 5 * 1000;
    
    private String HttpURL;
    
    private String subVideoChannelID;
    
    private String channelid;
    
    private String VODNAME;
    
    private String PICPATH;
    
    private EnumType.Platform platform;
    
    /** 从头播放，切换上一集，下一集，强制从头播放，不使用history */
    private boolean playFromStart;
    
	private Handler notPayFiveMinuteHandler = new Handler();
	
	private int playLength= 300*1000; 
	public boolean finishedPrePlay; 
	private VodPlayRecord vodPlayRecord;
 
	// 处理已经播放的日期
	private Handler handlerDisplayTimePlayed = new Handler();
	
    public TVVideoView(Context context, AttributeSet attrs)
    {    	   
        super(context, attrs);
        status = "PLAY";
        this.context =context;
		vodPlayRecord = new VodPlayRecord();
    }
    
    public void initVodDetailInfo(VodDetailInfo vodDetailInfo,OrderStatusBar orderStatusBar,ChoosePackage choosePackage,ChoosePayMethod choosePayMethod,ChoosePlayPosition choosePlayPosition){
    	this.vodDetailInfo= vodDetailInfo;
    	this.orderStatusBar = orderStatusBar;
    	this.choosePackage = choosePackage;
    	this.choosePackage.setChoosePayMethodListener(this);
    	this.choosePayMethod = choosePayMethod;
    	this.choosePayMethod.setChoosePlayPositionListener(this);
    	this.choosePayMethod.setBackToChoosePackageListener(this);
    	this.choosePlayPosition = choosePlayPosition;
    	this.choosePlayPosition.setStartPlayOrderedVodListener(this);
    }

    /* 定义一个Handler，用于处理下载线程与UI间通讯 */
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case NOTIFY_PROGRESS:
                {
                    int position = getCurrentPosition();
                    int duration = getDuration();
                    removeMessages(NOTIFY_PROGRESS);
                    msg = obtainMessage(NOTIFY_PROGRESS);
                    sendMessageDelayed(msg, 1000 - (position % 1000));
                    checkFiveMinates(position,duration);
                    break;
                }
            }
        }
    };

    /** 加载视频 */
    @Override
    protected boolean openVideo()
    {
        boolean open = super.openVideo();
        
        LogUtils.error("openVideo:" + open);
        
        if (open)
        {
            if (mLoadingView != null)
            {
                mLoadingView.show("");
            }

            // http+mp4播放
            if (getPlayinfoThread != null)
            {
                getPlayinfoThread.interrupt();
                getPlayinfoThread.stop = true;
                getPlayinfoThread = null;
            }
            getPlayinfoThread = new GetPlayinfoThread(this);
            getPlayinfoThread.start();
        }
        return open;
    }

    /** 加载完毕，开始播放 */
    @Override
    protected void onPrepared(BaseMediaPlayer mp)
    {
        super.onPrepared(mp);

        LogUtils.error("onPrepared");

        if (getPlayinfoThread != null)
        {
            getPlayinfoThread.interrupt();
            getPlayinfoThread.stop = true;
            getPlayinfoThread = null;
        }

        long historyPosition = -1;
        
        if (isPlayHttp() && !playFromStart) {	// 网络记录
	        VodHistoryLocalFactory vodHistoryLocalFactory = new VodHistoryLocalFactory(getContext());
			HistoryChannelInfo historyChannelInfo = vodHistoryLocalFactory.getHistoryById(subVideoChannelID);
			if (historyChannelInfo != null)
			{
				LogUtils.debug("上次播放位置:"+ (historyChannelInfo.playposition));
				historyPosition = (int) (historyChannelInfo.playposition);
	
	            if (getDuration() > 0 && historyPosition >= getDuration() - PLAY_FROMLASTPOSITION)
	            {
	                // 上次播放完毕，从头开始播放
	                historyPosition = 0;
	            }
	
	            // 提前5秒
	            historyPosition = historyPosition - PLAY_FROMLASTPOSITION;
	
	            if (historyPosition > 0)
	            {
	                seekTo((int) historyPosition, false);
	            }
	        }
        } else if (isPlayFile()) { // 本地文件读取播放记录
        	historyPosition = 0;
        }
        vodPlayRecord.setProgramId(vodDetailInfo.VODID);
        vodPlayRecord.setProgramName(vodDetailInfo.VODNAME);
		vodPlayRecord.setBeginPosition(historyPosition);
		vodPlayRecord.setBeginPlayDateTime(new Date());
		vodPlayRecord.setLength(vodDetailInfo.ELAPSETIME);
        
		Intent intent = new Intent(ACTION_REPORT_VOD_START_PLAY);
		Bundle bundle = new Bundle();
		bundle.putSerializable("record", vodPlayRecord);
		intent.putExtras(bundle);
		this.context.sendBroadcast(intent); 
		TvApplication.status ="PLAYING_VOD";
		TvApplication.position = "VOD_ACTIVITY";
		
		status ="PLAY";
		if(vodDetailInfo.ordered){
			orderStatusBar.setVisibility(View.GONE);
		}else{
			orderStatusBar.setVisibility(View.VISIBLE);
		}
		
        start();
    }

    /** 播放完毕 */
    @Override
    protected void onCompletion(BaseMediaPlayer mp)
    {
        //factory test
        if (this.isFactoryTest)
        {
            this.seekTo(0, false);
            return;
        }
        super.onCompletion(mp);
        
        LogUtils.error("onCompletion");

        stopPlayback(true);

        // 连续播放
        if (canNext())
        {
            playNext();
        }
        else
        {
            ((Activity) getContext()).finish();
        }
    }

    /** 播放出错 */
    @Override
    protected boolean onError(BaseMediaPlayer mp, int what, int extra)
    {
        stopPlayback(true);
        
        AppCommonUtils.showToast(getContext(), getContext().getString(R.string.tv_player_error_play));
        
        ((Activity) getContext()).finish();

        return true;
    }

    /** 缓冲信息 */
    // @Override
    // protected boolean onInfo(BaseMediaPlayer mp, int what, int extra)
    // {
    //
    // if (what == BaseMediaPlayer.MEDIA_INFO_BUFFERING_START)
    // {
    // // 开始缓冲
    // if (params != null)
    // {
    // params.playingBufferStart();
    // }
    // }
    // else if (what == BaseMediaPlayer.MEDIA_INFO_BUFFERING_END)
    // {
    // // 缓冲结束
    // if (params != null)
    // {
    // params.playingBufferEnd();
    // }
    // }
    //
    // return super.onInfo(mp, what, extra);
    // }

    @Override
    protected void bufferingStart()
    {
        super.bufferingStart();
    }

    @Override
    protected void bufferingEnd()
    {
        super.bufferingEnd();
    }

    /** seek结束 */
    @Override
    protected void onSeekComplete(BaseMediaPlayer mp)
    {
        super.onSeekComplete(mp);
    }

    @Override
    public boolean start()
    {
        boolean start = super.start();
        if (mLoadingView != null)
        {
            mLoadingView.hide();
        }
       
        if (start)
        {
    		 mHandler.removeMessages(NOTIFY_PROGRESS);
             mHandler.sendEmptyMessage(NOTIFY_PROGRESS);
             LogUtils.error("发送开始RABBITMQ信息：");
        }
        return start;
    }

    @Override
    public void pause()
    {
        super.pause();
        mHandler.removeMessages(NOTIFY_PROGRESS);
    }

    /** 拖动 */
    @Override
    public boolean seekTo(int msec, boolean fromUser)
    {
        return super.seekTo(msec, fromUser);
    }
    
    //保存本地视频播放记录
    public static void saveHistory(Context context, String subVideoChannelID,EnumType.Platform platform, long position, String channelid, String VODNAME, String PICPATH)
    {
        if (position > 0)
        {
        	HistoryChannelInfo historyChannelInfo = new HistoryChannelInfo();
        	VodHistoryLocalFactory vodHistoryLocalFactory = new VodHistoryLocalFactory(context);
			historyChannelInfo.channelid = channelid;
			historyChannelInfo.VODNAME = VODNAME;
			historyChannelInfo.PICPATH = PICPATH;
			historyChannelInfo.VODID = subVideoChannelID;
			historyChannelInfo.platform = platform;
			historyChannelInfo.playposition = position; // *1000
			vodHistoryLocalFactory.saveHistory(historyChannelInfo);
        }
    }

    @Override
    public void stopPlayback(boolean clearUri)
    {
        //保存本地播放进度
        saveHistory(getContext(), subVideoChannelID, platform, getCurrentPosition(), channelid, VODNAME, PICPATH);
        
        if (getPlayinfoThread != null)
        {
            getPlayinfoThread.interrupt();
            getPlayinfoThread.stop = true;
            getPlayinfoThread = null;
        }
        // 通知底层
        mHandler.removeMessages(NOTIFY_PROGRESS);
        super.stopPlayback(clearUri);
    }
    
    private String getFtString(int ft)
    {
        String ftString = null;
        switch (ft)
        {
            case 1:
                // 高清
                ftString = getContext().getString(R.string.title_play_bitrate_high);
                break;

            case 2:
                // 标清
                ftString = getContext().getString(R.string.title_play_bitrate_normal);
                break;
                
            default:
                break;
        }

        if (ftString != null)
        {
            return "[" + ftString + "]";
        }
        return null;
    }
    
    private String getTitle(String title, int ft)
    {
        String ftString = getFtString(ft);
        if (ftString != null)
        {
            return ftString + title;
        }
        
        return title;
    }

    /** 标题 */
    @Override
    public String getTitle()
    {
        return VODNAME;
    }

    /** 能否暂停 */
    @Override
    public boolean canPause()
    {
        return true;
    }

    /** 能否快退 */
    @Override
    public boolean canSeekBackward()
    {
        return canPause();
    }

    /** 能否快进 */
    @Override
    public boolean canSeekForward()
    {
        return canPause();
    }

    /** 上一集 */
    @Override
    public boolean canPrev()
    {
        return false;
    }

    /** 上一集 */
    @Override
    public void playPrev()
    {
        if (canPrev())
        {
            stopPlayback(true);
            // 从头播放
            playFromStart = true;
            play();
        }
    }

    /** 下一集 */
    @Override
    public boolean canNext()
    {
        return false;
    }

    /** 下一集 */
    @Override
    public void playNext()
    {
        if (canNext())
        {
            stopPlayback(true);
            // 从头播放
            playFromStart = true;
            play();
        }
    }

    public void setParams(String HttpURL, String subVideoChannelID, EnumType.Platform platform, String channelid, String VODNAME, String PICPATH)
    {
        stopPlayback(true);

        this.HttpURL = HttpURL;
        
        this.subVideoChannelID = subVideoChannelID;
        
        this.channelid = channelid;
        
        this.VODNAME = VODNAME;
        
        this.PICPATH = PICPATH;
        
        this.platform = platform;

        play();
    }
    
    /** 判断参数是否有效 */
    private boolean isValid()
    {
        return isPlayFile() || isPlayHttp();
    }

    /** 本地播放，本地播放不需要网络 */
    private boolean isPlayFile()
    {
        if (!TextUtils.isEmpty(HttpURL) && (HttpURL.startsWith("file:///") || HttpURL.startsWith("/")))
        {
            return true;
        }
        return false;
    }

    /** http播放，需要网络 */
    private boolean isPlayHttp()
    {
        if (!TextUtils.isEmpty(HttpURL) && (HttpURL.startsWith("http://") || HttpURL.startsWith("https://")))
        {
            return true;
        }
        return false;
    }

    /**
     * 播放
     */
    public void play()
    {
        if (isInPlaybackState())
        {
            LogUtils.error("isInPlaybackState");
            return;
        }

        if (HttpURL == null || HttpURL.isEmpty())
        {
            LogUtils.error("HttpURL==null");
            return;
        }

        if (!isValid())
        {
            LogUtils.error("HttpURL invalid");
            return;
        }

        if (isPlayFile())
        {
            // 是否工厂循环测试
            isFactoryFile(HttpURL);
            
            // 本地文件播放，不需要网络
            if (!isInPlaybackState())
            {
                if (mLoadingView != null)
                {
                    mLoadingView.show("");
                }
                setVideoPath(HttpURL);
            }

            return;
        }

        // 判断网络
        if (!NetWorkUtil.isNetworkAvailable(getContext()))
        {
            LogUtils.debug("无网络");
            if (isInPlaybackState())
            {
                // 播放过程中，断网，不需要马上提醒
                return;
            }
            
            AppCommonUtils.showToast(getContext(), getContext().getString(R.string.network_error));

            ((Activity) getContext()).finish();

            return;
        }

        if (isPlayHttp())
        {
            // 播放http串
            if (!isInPlaybackState())
            {
                if (mLoadingView != null)
                {
                    mLoadingView.show("");
                }
                
                setVideoPath(HttpURL);
            }

            return;
        }
    }
    
    void isFactoryFile(String uriString)
    {
        try
        {
            String pathp = uriString.substring(0, uriString.lastIndexOf("/"));
            File testfile = new File(pathp + "/ForPlayTest!@#");
            if (testfile != null && testfile.exists())
            {
                isFactoryTest = true;
            }
        }
        catch (Exception ex)
        {

        }
    }

    /** http+mp4进度 */
    static class GetPlayinfoThread extends Thread
    {
        private WeakReference<TVVideoView> reference;

        boolean stop;

        public GetPlayinfoThread(TVVideoView videoView)
        {
            this.reference = new WeakReference<TVVideoView>(videoView);
        }

        @Override
        public void run()
        {
            try
            {
                Thread.sleep(2000);

                while (!stop)
                {
                    Thread.sleep(1000);

                    if (stop)
                    {
                        break;
                    }
                }
            }
            catch (InterruptedException e)
            {
                LogUtils.error("GetPlayinfoThread Interrupted");
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString(), e);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	LogUtils.error("TvVideoView Key:"+keyCode);
    	
    	switch (keyCode) {
    	case KeyEvent.KEYCODE_DPAD_CENTER:
    		
    		if(status.equals("PLAY")){
    			if(vodDetailInfo.ordered){
    				
    			}else{
    				LogUtils.error("开始设置显示条状态为不显示");
    				orderStatusBar.setVisibility(View.GONE);
            		status = "CHOOSE_PACKAGE";
            		pause();
        	        choosePackage.setVisibility(View.VISIBLE);
        	        choosePackage.initData(vodDetailInfo);
        	        choosePackage.forFocus();
        	        
        	        TvApplication.status ="PLAYING_VOD_PAY_CHOOESE_PACKAGE";
					TvApplication.position = "VOD_ACTIVITY";
    			}
    	        return true;
    		}else if(status.equals("PAY")){
    			
				/*choosePackage.setVisibility(View.GONE);
				choosePayMethod.setVisibility(View.VISIBLE);
				choosePayMethod.initData(vodDetailInfo, packageSelected, priceSel);
				choosePackage.forFocus();
				status ="CHOOSE_PACKAGE";*/
				return true;
    		}
	        break;
		case KeyEvent.KEYCODE_BACK:
			LogUtils.error("状态:"+status);
			if(status.equals("CHOOSE_PACKAGE")){
				
				return super.onKeyDown(keyCode, event);
			}else if(status.equals("PAY")){
				return super.onKeyDown(keyCode, event);
			}else if(status.equals("CHOOSE_PLAY_POSITION")){
				return super.onKeyDown(keyCode, event);
			}
			LogUtils.error("退出 status:"+status);
			break;
		case KeyEvent.KEYCODE_0:
			if(status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_1:
			if(status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_2:
			if(status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_3:
			if(status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_4:
			if(status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_5:
			if(status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_6:
			if(status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_7:
			if(status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_8:
			if(status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;	
		case KeyEvent.KEYCODE_9:
			if(status.equals("PAY")){
				choosePayMethod.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			LogUtils.error("状态:"+status);
			if(status.equals("CHOOSE_PACKAGE")){
				return super.onKeyDown(keyCode, event);
				
			}else if(status.equals("PAY")){
				return super.onKeyDown(keyCode, event);
			}else if(status.equals("CHOOSE_PLAY_POSITION")){
				return super.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			LogUtils.error("状态:"+status);
			if(status.equals("CHOOSE_PACKAGE")){
				return super.onKeyDown(keyCode, event);
			}else if(status.equals("PAY")){
				return super.onKeyDown(keyCode, event);
			}else if(status.equals("CHOOSE_PLAY_POSITION")){
				return super.onKeyDown(keyCode, event);
			}
			break;	
		case KeyEvent.KEYCODE_DPAD_LEFT:
			LogUtils.error("状态:"+status);
			if(status.equals("CHOOSE_PACKAGE")){
				return super.onKeyDown(keyCode, event);
			}else if(status.equals("PAY")){
				return super.onKeyDown(keyCode, event);
			}else if(status.equals("CHOOSE_PLAY_POSITION")){
				return super.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			LogUtils.error("状态:"+status);
			if(status.equals("CHOOSE_PACKAGE")){
				return super.onKeyDown(keyCode, event);
			}else if(status.equals("PAY")){
				return super.onKeyDown(keyCode, event);
			}else if(status.equals("CHOOSE_PLAY_POSITION")){
				return super.onKeyDown(keyCode, event);
			}
			break;
		
		default:
			break;
		}
    	return super.onKeyDown(keyCode, event);
    	
        // 上下键映射为音量键，不呼出控制条
        
            /*if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN)
            {
                adjustStreamVolume(AudioManager.ADJUST_LOWER);
                return true;
            }
            else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP)
            {
                adjustStreamVolume(AudioManager.ADJUST_RAISE);
                return true;
            }
            else */
            /*if (event.getKeyCode() == 223)
            {
                switchFullScreen();
                return true;
            }*/
            /*else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER ){
            	
            	LogUtils.error("显示Choose Package");
            	if(!vodDetailInfo.ordered){
            		
            	}
        		// 发送信息
				pause();
				//mMediaController.show();
		        status="CHOOSE_PACKAGE";
		        
		        choosePackage.setVisibility(View.VISIBLE);
            	
		        return true;
            }*/
        /*if(status == null){
        	return super.onKeyDown(keyCode, event);
        }else if(status.equals("PLAY")){
        	return super.onKeyDown(keyCode, event);
        }else if(status.equals("CHOOSE_PLAY_POSITION") || status.equals("CHOOSE_PACKAGE") || status.equals("PAY")){
        	if(keyCode == KeyEvent.KEYCODE_BACK){
        		return super.onKeyDown(keyCode, event);
        	}else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
        		//choosePayMethod.onKeyDown(keyCode, event);
        		return super.onKeyDown(keyCode, event);
        	}else{
        		return true;
        	}
        }else{
        	return super.onKeyDown(keyCode, event);
        }*/
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        // 上下键映射为音量键，不呼出控制条
        {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN)
            {
                return true;
            }
            else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP)
            {
                return true;
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    /** 调节音量 */
    private void adjustStreamVolume(int direction)
    {
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        // AudioManager.FLAG_SHOW_UI：显示系统音量调节界面
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public void switchFullScreen()
    {
        if (this.screenType == SCREEN_FULL_SCREEN)
        {
            // 拉伸
            this.screenType = SCREEN_STRETCH;
        }
        else
        {
            this.screenType = SCREEN_FULL_SCREEN;
        }

        requestLayout();

        if (mMediaController != null && mMediaController.isShowing())
        {
            mMediaController.updateFullScreen();
        }
    }
    
    /**
     * 检查5分钟的情况，并更新显示的时间点
     */
    private void checkFiveMinates(int position, int duration){
		if(position >= playLength){
			if(vodDetailInfo != null){
				if(vodDetailInfo.ordered){
		    	}else{
					finishedPrePlay =true;
					reportVodPlayRecord();
					
					pause();
					mMediaController.hide(true);
			        status="CHOOSE_PACKAGE";
			        
			        //先隐藏，后期加动画
			        orderStatusBar.setVisibility(View.GONE);
			        // 启动选择套餐
			        choosePackage.initData(vodDetailInfo);
			        choosePackage.setVisibility(View.VISIBLE);
			        choosePackage.forFocus();
			        
			        TvApplication.status ="PLAYING_VOD_PAY_CHOOESE_PACKAGE";
					TvApplication.position = "VOD_ACTIVITY";
		    	}
			}
			
		}else{
			orderStatusBar.setTimePlayed(position/1000);
		}
    }

	private void reportVodPlayRecord(){
		vodPlayRecord.setEndPosition((long)(getCurrentPosition()/1000));
		vodPlayRecord.setEndPlayDateTime(new Date());
		vodPlayRecord.setProgramId(vodDetailInfo.VODID);
		vodPlayRecord.setPlatform(vodDetailInfo.platform.toString());
		vodPlayRecord.setProgramName(vodDetailInfo.VODNAME);
		vodPlayRecord.setBillingType(TvApplication.billingType);
		vodPlayRecord.setLength(vodDetailInfo.ELAPSETIME);
		
		
		
		Intent intent = new Intent(ACTION_REPORT_VOD_PLAY_RECORD);
		Bundle bundle = new Bundle();
		bundle.putSerializable("record", vodPlayRecord);
		intent.putExtras(bundle);
		this.context.sendBroadcast(intent); 
		
	}
	
	public void orderedStart(boolean fromStart){
		if(fromStart){
			seekTo(0, false);
			start();
		}else{
			start();
		}
		mMediaController.hide(false);
	}

	@Override
	public void onChoose(String packageSelected,double price) {
		
		super.onChoose(packageSelected, price);
		choosePackage.setVisibility(View.INVISIBLE);
		choosePayMethod.initData(vodDetailInfo,packageSelected,price);
		choosePayMethod.setVisibility(View.VISIBLE);
		choosePayMethod.initFocus();
		status="PAY";
		TvApplication.status ="PLAYING_VOD_PAY_CHOOESE_PAYMETHOD";
		TvApplication.position = "VOD_ACTIVITY";
		
	}

	@Override
	public void choosePlayPosition() {
		choosePayMethod.setVisibility(View.GONE);
		choosePlayPosition.setVisibility(View.VISIBLE);
		choosePlayPosition.initFocus();
		TvApplication.status ="PLAYING_VOD_CHOOSE_PLAY_POSITION";
		TvApplication.position = "VOD_ACTIVITY";
		status="CHOOSE_PLAY_POSITION";
	}

	@Override
	public void startPlay(boolean fromStart) {
		choosePlayPosition.setVisibility(View.GONE);
		status="PLAY";
		if(fromStart){
			seekTo(0, false);
			start();
		}else{
			start();
		}
	}
	
	@Override
	public void back() {
		choosePayMethod.setVisibility(View.GONE);
		choosePackage.setVisibility(View.VISIBLE);
		status="CHOOSE_PACKAGE";
		choosePackage.forFocus();
	}
	
	/**
	 * 退出
	 */
	public void exit(){
		vodPlayRecord.setEndPosition((long)(getCurrentPosition()/1000));
		vodPlayRecord.setEndPlayDateTime(new Date());
		vodPlayRecord.setProgramId(vodDetailInfo.VODID);
		vodPlayRecord.setProgramName(vodDetailInfo.VODNAME);
		vodPlayRecord.setBillingType(TvApplication.billingType);
		vodPlayRecord.setPlatform(vodDetailInfo.platform.toString());
		vodPlayRecord.setLength(vodDetailInfo.ELAPSETIME);
		
		Intent intent = new Intent(ACTION_REPORT_VOD_PLAY_RECORD);
		Bundle bundle = new Bundle();
		bundle.putSerializable("record", vodPlayRecord);
		intent.putExtras(bundle);
		context.sendBroadcast(intent); 
		stopPlayback(false);
	}

}

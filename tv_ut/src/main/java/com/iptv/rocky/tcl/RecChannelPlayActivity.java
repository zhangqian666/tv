package com.iptv.rocky.tcl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.data.RecBill;
import com.iptv.common.data.RecChan;
import com.iptv.common.data.ReviewPlayRecord;
import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.local.VodHistoryLocalFactory;
import com.iptv.rocky.mediaplayer.MediaPlayer;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnBufferingUpdateListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnCompletionListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnErrorListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnPreparedListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnRtspStatusEndOfStreamListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnRtspStatusStartOfStreamListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnVideoSizeChangedListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.PlayStatus;
import com.iptv.rocky.mediaplayer.MediaPlayer.PlayType;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.BackgroundMusic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecChannelPlayActivity extends BaseActivity 
	implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener,OnRtspStatusStartOfStreamListener,OnRtspStatusEndOfStreamListener,OnErrorListener {
	
	// 回看播放记录上传
	private static final String ACTION_REPORT_REVIEW_PLAY_RECORD ="com.virgintelecom.iptv.LIVE.REVIEW.PLAYRECORD";
	private static final String ACTION_REPORT_REVIEW_START_PLAY ="com.virgintelecom.iptv.LIVE.REVIEW.START.PLAY";
	
	private MediaPlayer tclPlayer;
	
	private RelativeLayout controlbar;
	private RelativeLayout exit_confirm_dialog;
	private ProgressBar progressBar;
	
	private ImageView play;
	private Button btn_exit_play;
	private Button btn_continue_play;
	
	private TextView vodPlayVodName;
	private TextView txtSpeedDisplay;
	private TextView vod_play_current_time;
	private TextView vodPlayTotalTime;
	private LinearLayout vodPlayExitThanks;
	private LinearLayout  vodPlayErrorExitApk;

	private int status = -1; // 0 stop 1 playing 2:fast forward

	private static String TAG = VodChannelPlayActivity.class.getSimpleName();

	private Float speed = (float) 0;
	
	private VodDetailInfo vodDetailInfo;
	
	private VodChannel vodChannel;
	
	private String RTSPURL;
	
	private String subVideoChannelID;
	
	private String playType="";
	
	private RecChan mRecChan;
	private RecBill mRecBill;
	private ReviewPlayRecord playRecord;
	
	Handler handler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		quiteBackgroundMusic();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.tcl_activity_rec_channel);

		controlbar = (RelativeLayout) findViewById(R.id.shedule_progress_controlbar);
		txtSpeedDisplay = (TextView) findViewById(R.id.schedule_play_speed);
		exit_confirm_dialog = (RelativeLayout) findViewById(R.id.exit_confirm_dialog);
		btn_exit_play = (Button) findViewById(R.id.btn_exit_play);
		btn_continue_play = (Button) findViewById(R.id.btn_continue_play);
		progressBar = (ProgressBar) findViewById(R.id.schedule_play_progressBar);
		
		play = (ImageView) findViewById(R.id.schedule_play_status_icon);
		vodPlayVodName=(TextView)findViewById(R.id.schedule_play_vod_name);
		vod_play_current_time=(TextView) findViewById(R.id.schedule_play_current_time);
		vodPlayTotalTime=(TextView) findViewById(R.id.schedule_play_total_time);
		vodPlayExitThanks=(LinearLayout) findViewById(R.id.schedule_play_exit_thanks);
		vodPlayErrorExitApk= (LinearLayout) findViewById(R.id.schedule_play_error_exit_apk);
		
		btn_exit_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Save played record;
				VodHistoryLocalFactory vodHistoryLocalFactory = new VodHistoryLocalFactory(RecChannelPlayActivity.this);
				HistoryChannelInfo historyChannelInfo = new HistoryChannelInfo();
				historyChannelInfo = new HistoryChannelInfo();
				
				/*if (vodDetailInfo != null) {
					// historyChannelInfo.channelid = vodDetailInfo.VODID;
					// historyChannelInfo.VODNAME = vodDetailInfo.VODNAME;
					// historyChannelInfo.PICPATH = vodDetailInfo.PICPATH;
					historyChannelInfo.channelid = mRecChan.CHANNELID;
					historyChannelInfo.VODNAME = mRecChan.CHANNELNAME;
					
				} else if (vodChannel != null) {
					historyChannelInfo.channelid = mRecChan.CHANNELID;
					historyChannelInfo.VODNAME = mRecChan.CHANNELNAME;
					historyChannelInfo.channelid = vodChannel.VODID;
					historyChannelInfo.VODNAME = vodChannel.VODNAME;
					historyChannelInfo.PICPATH = vodChannel.PICPATH;
				}*/
				historyChannelInfo.VODID = subVideoChannelID;
				historyChannelInfo.playposition = tclPlayer.getCurrentPlayTime(); //此处改为实际的当前位置；
				vodHistoryLocalFactory.saveHistory(historyChannelInfo);
				tclPlayer.stop();
				finish();
			}
		});
		
		// Back to resume playing;
		btn_continue_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "btn_continue_play :" + tclPlayer.getPlayStatus());
				if(tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)){
					tclPlayer.resume();
					exit_confirm_dialog.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onkeydown keycode = " + keyCode +" status: "+tclPlayer.getPlayStatus());
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			if(tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)){
				tclPlayer.resume();
				play.setImageResource(R.drawable.play_icon);
				controlbar.setVisibility(View.INVISIBLE);
				handler.removeCallbacks(runnable);
				return false;
			}
	    	else if(tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) || tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND))
	    	{
				tclPlayer.normalrate();
				play.setImageResource(R.drawable.play_icon);
				controlbar.setVisibility(View.INVISIBLE);
				handler.removeCallbacks(runnable);
				return false;
			}	
			else if(tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY)){
//				tclPlayer.pause();
//				exit_confirm_dialog.setVisibility(View.VISIBLE);
//				btn_exit_play.requestFocus();
				tclPlayer.stop();
			}
			break;
		case KeyEvent.KEYCODE_HOME:
//			if (status == 1 || status == 3) {
//				status = 0;
//				testPlayer(3);
//			}
			break;
		case 17:
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if(tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY)){
				tclPlayer.gotoStart();
			}
			
			break;
		
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY)){
				tclPlayer.gotoEnd();
			}
			break;
		
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(exit_confirm_dialog.getVisibility() == View.GONE || exit_confirm_dialog.getVisibility() == View.INVISIBLE){
				if(!tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)){
					speed = (float) 2.0;
				}else{
					if(speed == 32){
						speed = (float) 2.0;
					}else{
						speed = speed*2;
					}
				}
				tclPlayer.fastRewind(speed);
				play.setImageResource(R.drawable.rewind_icon);
				controlbar.setVisibility(View.VISIBLE);
				
				if(handler != null){
					handler.removeCallbacks(runnable);
					handler.postDelayed(runnable, 500);
				}
				//handler.postDelayed(runnable, 500);
				txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(-speed)));
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			
			if(exit_confirm_dialog.getVisibility() == View.GONE || exit_confirm_dialog.getVisibility() == View.INVISIBLE){
				if(!tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD)){
					speed = (float) 2.0;
				}else{
					if(speed == 32){
						speed = (float) 2.0;
					}else{
						speed = speed*2;
					}
				}
				tclPlayer.fastForward(speed);
				play.setImageResource(R.drawable.speed_icon);
				controlbar.setVisibility(View.VISIBLE);
				if(handler != null){
					handler.removeCallbacks(runnable);
					handler.postDelayed(runnable, 500);
				}
				txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(speed)));
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if(exit_confirm_dialog.getVisibility() == View.VISIBLE){
				
			}else if(controlbar.getVisibility() == View.VISIBLE){
				if(tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)){
					tclPlayer.resume();
					controlbar.setVisibility(View.INVISIBLE);
					handler.removeCallbacks(runnable);
				}else if( tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) || tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)){
					tclPlayer.normalrate();
					controlbar.setVisibility(View.INVISIBLE);
					handler.removeCallbacks(runnable);
				}
			}else if(vodPlayExitThanks.getVisibility() == View.VISIBLE){
				finish();	
			}else{	
				if(tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY)){
					tclPlayer.pause();
					play.setImageResource(R.drawable.pause_iocn);
					txtSpeedDisplay.setText("X 0");
					if (handler!=null) {
						handler.removeCallbacks(runnable);
						handler.postDelayed(runnable, 500);
					}
					controlbar.setVisibility(View.VISIBLE);
				}else if(tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
						|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)){
					tclPlayer.normalrate();
					controlbar.setVisibility(View.INVISIBLE);
				}else if(tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)){
					tclPlayer.resume();
					exit_confirm_dialog.setVisibility(View.INVISIBLE);
				}
			}
			break;
		case KeyEvent.KEYCODE_PAGE_UP:
			if(tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY)){
				tclPlayer.gotoStart();
			}
			break;
		case KeyEvent.KEYCODE_PAGE_DOWN:
			if(tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY)){
				tclPlayer.gotoEnd();
			}
			break;
		case 82:
//			Intent destIntent = new Intent();
//			destIntent.setAction("com.tcl.matrix.action.submenu.show");
//			destIntent.putExtra("callback", "com.tcl.simpleplay.TestSubMenuService");
//			destIntent.putExtra("fucos_item", "");
//			Log.d(TAG, "352 key down, send submenu broadcast..." );
//    		sendOrderedBroadcast(destIntent, null);
			break;
		case 253:
//			Intent destIntent2 = new Intent();
//			destIntent2.setAction("com.tcl.matrix.action.submenu.show");
//			destIntent2.putExtra("callback", "com.tcl.simpleplay.TestSubMenuService");
//			destIntent2.putExtra("fucos_item", "vedio:1");
//			Log.d(TAG, "352 key down, send submenu broadcast..." );
//    		sendOrderedBroadcast(destIntent2, null);
			break;
		case 303:
//			Intent destIntent1 = new Intent();
//			destIntent1.setAction("com.tcl.matrix.action.tvmenu.inputsrc.show");
//			destIntent1.putExtra("callback", "");
//			destIntent1.putExtra("currentInputSource", 1);
//			destIntent1.putIntegerArrayListExtra("inputSourceList", null);
//    		sendOrderedBroadcast(destIntent1, null);
    		break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
		//return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		Intent intent = getIntent();
		if (intent != null)
        {
			Object o = (Object) intent.getSerializableExtra(IPTVUriUtils.EXTRA_PARAMS);
			if (o instanceof VodDetailInfo){
				vodDetailInfo = (VodDetailInfo) o;
				playType="VOD";
			}
			else if (o instanceof VodChannel){
				playType="SCHEDULE";
				vodChannel = (VodChannel) o;
			}else if (o instanceof RecChan){
				playType="SCHEDULE";
				LogUtils.error("Is RecChan");
				mRecBill = (RecBill) intent.getSerializableExtra(IPTVUriUtils.RECBILL_PARAMS);
				mRecChan = (RecChan) o;
			}
			
            //System.out.println("要播放的RTSP URL:"+RTSPURL);
            RTSPURL = intent.getStringExtra(IPTVUriUtils.PLAY_URL_PARAMS);
            if (TextUtils.isEmpty(RTSPURL))
            {
            	finish();
            	return;
            }
            
            
            subVideoChannelID = intent.getStringExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS);
        }
		
		if(tclPlayer== null){
			tclPlayer = new MediaPlayer(RecChannelPlayActivity.this);
			tclPlayer.setKeepLastFrame(0);
			tclPlayer.setPlayType(PlayType.SCHEDULE);
			tclPlayer.setOnBufferingUpdateListener(RecChannelPlayActivity.this);
			//tclPlayer.setVideoDisplayArea(125, 0, 750, 1000);
			tclPlayer.setOnCompletionListener(RecChannelPlayActivity.this);
			tclPlayer.setOnPreparedListener(RecChannelPlayActivity.this);
			tclPlayer.setOnVideoSizeChangedListener(RecChannelPlayActivity.this);
			tclPlayer.setOnRtspStatusEndOfStreamListener(RecChannelPlayActivity.this);
			tclPlayer.setOnRtspStatusStartOfStreamListener(RecChannelPlayActivity.this);
	        tclPlayer.setSingleMedia(RTSPURL);
	       
	        
	        progressBar = (ProgressBar) findViewById(R.id.schedule_play_progressBar);
			progressBar.setMax(1000);//临时设置，channel中未定义节目长度；
			progressBar.setProgress(0);
			
			// 查看记录是否在
			VodHistoryLocalFactory vodHistoryLocalFactory = new VodHistoryLocalFactory(this);
			HistoryChannelInfo historyChannelInfo = vodHistoryLocalFactory.getHistoryById(subVideoChannelID);
			if (historyChannelInfo != null)
			{
				// 执行seek拖动操作,暂时从头播放；
				tclPlayer.playFromStart();
			}else{
				//System.out.println("START PLAY");
				 tclPlayer.playFromStart();
			}
			
			playRecord = new ReviewPlayRecord();
			playRecord.setStbId(TvApplication.stbId);
			playRecord.setUserId(TvApplication.account);
			playRecord.setHotelId(TvApplication.hotelId);
			playRecord.setRoomId(TvApplication.roomId);
			playRecord.setBeginPlayDateTime(new Date());
			playRecord.setLanguage(TvApplication.language);
			playRecord.setPlatform(TvApplication.platform);
			
			playRecord.setBeginPlayDateTime(new Date());
			playRecord.setProgramName(mRecBill.title);
			playRecord.setProgramId(Integer.toString(mRecBill.programId));
			
			Intent intentAmqp = new Intent(ACTION_REPORT_REVIEW_START_PLAY);
			Bundle bundle = new Bundle();
			bundle.putSerializable("record", playRecord);
			intentAmqp.putExtras(bundle);
			sendBroadcast(intentAmqp); 
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		Intent intent = getIntent();
		if (intent != null)
        {
			Object o = (Object) intent.getSerializableExtra(IPTVUriUtils.EXTRA_PARAMS);
			if (o instanceof VodDetailInfo)
				vodDetailInfo = (VodDetailInfo) o;
			else if (o instanceof VodChannel)
				vodChannel = (VodChannel) o;
            
            RTSPURL = intent.getStringExtra(IPTVUriUtils.PLAY_URL_PARAMS);
            if (TextUtils.isEmpty(RTSPURL))
            {
            	finish();
            	return;
            }
            subVideoChannelID = intent.getStringExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS);
        }
		
		//System.out.println("RESUME ："+(tclPlayer== null));
		if(tclPlayer== null){
			tclPlayer = new MediaPlayer(RecChannelPlayActivity.this);
			tclPlayer.setKeepLastFrame(0);
			tclPlayer.setOnBufferingUpdateListener(RecChannelPlayActivity.this);
			//tclPlayer.setVideoDisplayArea(125, 0, 750, 1000);
			tclPlayer.setOnCompletionListener(RecChannelPlayActivity.this);
			tclPlayer.setOnPreparedListener(RecChannelPlayActivity.this);
			tclPlayer.setOnVideoSizeChangedListener(RecChannelPlayActivity.this);
	        tclPlayer.setSingleMedia(RTSPURL);
	        
	        progressBar = (ProgressBar) findViewById(R.id.vod_play_progressBar);
			progressBar.setMax(vodDetailInfo.ELAPSETIME * 60);
			progressBar.setProgress(0);
	        
	     // 查看记录是否在
			VodHistoryLocalFactory vodHistoryLocalFactory = new VodHistoryLocalFactory(this);
			HistoryChannelInfo historyChannelInfo = vodHistoryLocalFactory.getHistoryById(subVideoChannelID);
			if (historyChannelInfo != null)
			{
				// 执行seek拖动操作
			}else{
				System.out.println("RESUME PLAY");
				tclPlayer.playFromStart();
			}
		}
	}
	
	@Override
	public void onStop(){

		if(handler.post(runnable)){
			handler.removeCallbacks(runnable);
		}
		
		BackgroundMusic.getInstance(this).playBackgroundMusic("music/background_ex.mp3", true);
		tclPlayer.reset();
		tclPlayer.release();
		deletePlayer();
		releaseMediaPlayer();
		super.onStop();
	}
	
/*	@Override
	public void onDestroy() {
		LogUtils.debug("Destroy Activity");
		
		//uninstallListeners();
		super.onDestroy();
	}*/
	
	
	private void deletePlayer(){
		if(tclPlayer != null){
			tclPlayer.setOnCompletionListener(null);
			tclPlayer.setOnPreparedListener(null);
			tclPlayer.setOnVideoSizeChangedListener(null);
			tclPlayer.setOnRtspStatusEndOfStreamListener(null);
			tclPlayer.setOnRtspStatusStartOfStreamListener(null);
			tclPlayer.setOnErrorListener(null);
			tclPlayer.setOnMediaStatusScaleListener(null);
		}
	}
	

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "点播播放页面：收到准备好信息");
		status = 1;
		
		// 查看记录是否在
		VodHistoryLocalFactory vodHistoryLocalFactory = new VodHistoryLocalFactory(this);
		HistoryChannelInfo historyChannelInfo = vodHistoryLocalFactory.getHistoryById(subVideoChannelID);
		if (historyChannelInfo != null)
		{
			// 执行seek拖动操作
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d(TAG, "点播播放页面：收到播放完毕信息");
		if(tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)){
			tclPlayer.rePlayFromStart();
		}else if(tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD)){
			onBackPressed();
		}
		//exit_confirm_dialog.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		
	}
	
	
	@Override
	public void onRtspStatusEndOfStream(MediaPlayer mp){
		Log.d(TAG, "onRtspStatusEndOfStream");
		controlbar.setVisibility(View.INVISIBLE);
		if(handler != null){
			handler.removeCallbacks(runnable);
		}
		
		playRecord.setEndPlayDateTime(new Date());
		
		
		LogUtils.debug("向 RABBITMQ 发送");
		Intent intent = new Intent(ACTION_REPORT_REVIEW_PLAY_RECORD);
		Bundle bundle = new Bundle();
		bundle.putSerializable("record", playRecord);
		intent.putExtras(bundle);
		sendBroadcast(intent); 
		
		//vodPlayExitThanks.setVisibility(View.VISIBLE);
		onBackPressed();
	}
	
	@Override
	public void onRtspStatusStartOfStream(MediaPlayer mp){
		Log.d(TAG, "onRtspStatusStartOfStream");
		progressBar.setProgress(0);
		controlbar.setVisibility(View.INVISIBLE);
		if(handler != null){
			handler.removeCallbacks(runnable);
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		if(what == MediaPlayer.MEDIA_DATA_FAILED_TODECODER){
			vodPlayErrorExitApk.setVisibility(View.VISIBLE);
		}
		return false;
	}
	
	/**
	 *  退出前释放player
	 */
	private void releaseMediaPlayer() {
		if (tclPlayer != null) {
			tclPlayer.release();
			tclPlayer = null;
		}
	}
	
	// 定时刷新进度条;
	Runnable runnable= new Runnable(){
		@Override
		public void run() {
			//要做的事情
			setProgressInfo();
			handler.postDelayed(runnable, 1000);
		}
	};
	
	// 创建要显示的时间
	private String createTimeString(int second){
		
		// 秒
        int sec = (second) % 60;
        // 分钟
        int min = (second / 60) % 60;
        // 小时
        int hour = (second / 60 / 60) % 24;
        // 天
        int day = second / 60 / 60 / 24;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(min);
        date.setSeconds(sec);
		return format.format(date);
	}
	
	// 绘画当前点的信息
	private void setProgressInfo(){
//		if(tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) || tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)){
//			vodPlayTotalTime.setText(createTimeString(tclPlayer.getMediaDuration()));
//			int currentTime =tclPlayer.getCurrentPlayTime();
//			progressBar.setMax(tclPlayer.getMediaDuration());
//			progressBar.setProgress(currentTime);
//	        vod_play_current_time.setText(createTimeString(currentTime));
//		}

		if (tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
				|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)||tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
			Log.d("总时长:",tclPlayer.getMediaDuration()+"");
			vodPlayTotalTime.setText(createTimeString(tclPlayer.getMediaDuration()));
			int currentTime = tclPlayer.getCurrentPlayTime();
			
			LogUtils.debug("当前播放进度："+tclPlayer.getCurrentPlayTime());
			
			Log.d("设置进度条","提取的当前时间:"+currentTime + "; 当前倍速："+txtSpeedDisplay.getText());
			progressBar.setMax(tclPlayer.getMediaDuration());
			progressBar.setProgress(currentTime);
			 vod_play_current_time.setText(createTimeString(currentTime));
		}
	
	}
	
	private void quiteBackgroundMusic() {
		if (BackgroundMusic.getInstance(this).isBackgroundMusicPlaying()) {
			BackgroundMusic.getInstance(this).stopBackgroundMusic();
		}
	}
	
}
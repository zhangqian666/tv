package com.iptv.rocky.tcl;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.LiveChannel;
import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.data.LiveChannelPlayRecord;
import com.iptv.common.data.PortalLiveType;
import com.iptv.common.data.ProgBill;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.hwdata.json.AllChannelProgBilJsonlFactory;
import com.iptv.rocky.hwdata.local.AAALiveChannelsLocalFactory;
import com.iptv.rocky.hwdata.local.LiveTypeLocalFactory;
import com.iptv.rocky.hwdata.xml.AllChannelProgBillFactory;
import com.iptv.rocky.mediaplayer.MediaPlayer;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnBufferingUpdateListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnCompletionListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnErrorListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnMediaStatusScaleListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnPreparedListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnRtspStatusEndOfStreamListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnRtspStatusStartOfStreamListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.OnVideoSizeChangedListener;
import com.iptv.rocky.mediaplayer.MediaPlayer.PlayStatus;
import com.iptv.rocky.mediaplayer.MediaPlayer.PlayType;
import com.iptv.rocky.model.ActivityStack;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.tcl.view.live.AdvertisementView;
import com.iptv.rocky.tcl.view.live.LiveChooseLayout;
import com.iptv.rocky.tcl.view.live.LiveHelpFragment;
import com.iptv.rocky.utils.BackgroundMusic;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 
 * 
 *
 */
public class LiveChannelPlayActivity extends BaseActivity 
implements OnBufferingUpdateListener, 
	OnCompletionListener, 
	OnPreparedListener, 
	OnVideoSizeChangedListener, 
	OnRtspStatusStartOfStreamListener, 
	OnRtspStatusEndOfStreamListener, 
	OnErrorListener,OnMediaStatusScaleListener,
	LiveChooseLayout.OnListChangeListener{

	private MediaPlayer player;

	private String mCurrentPlayChannelID =null;
	//	private String mCurrentPlayChannelIDZte = "";

	private List<String> channelIds = new ArrayList<String>();
	private int currentPlayChannelIndex = 0;
	private Float speed = (float) 0;

	private RelativeLayout liveChannelProgressControlbar;

	//Play status Icon displayed in fastfowrd fastwind pause status
	private ImageView play; 

	private Handler handler = new Handler();

	private Handler handlerProgBill = new Handler();

	private Handler handlerChannelList = new Handler();
	//数字频道切换的处理
	private Handler handlerUserChannelNumber = new Handler();
	private Handler handlerNoChannelInfo = new Handler();
	private Handler handlerHideNoChannelInfo = new Handler();
	private Handler handlerHideTimeshiftInfo = new Handler();
	// 处理帮助的延迟显示
	private Handler handlerShowHelp = new Handler();

	private FrameLayout playbill_main;

	private TextView tvPlaybill;
	private TextView tvPlaybillNext;
	private TextView liveChannelName;
	private TextView tv_menu_category_name;
	private TextView tv_playbill_channel_name;
	private TextView tv_playbill_channel_num;
	private TextView timeshift_play_current_time;
	private TextView txtSpeedDisplay;
	private TextView timeshiftPlayTotalTime;
	// 频道号
	private TextView userChannelNumber;
	private ImageView userChannelNumberBg;

	private ProgressBar progressBar;
	private ImageView timeshiftIcon;

	private LinearLayout vodPlayErrorExitApk;
	private TextView channelPlayErrorTxt;

	private LinearLayout channelResult;

	// private String displayMode = "16:9";

	// 定义显示频道的列表显示状态
	private boolean canForward = false;

	private TextView timeshiftPlayBeginTime;
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");	

	private LiveChooseLayout mLiveChoose;

	private ArrayList<PortalLiveType> mLstLiveType;

	private int mCurTypeId = 1;
	private int mCurPlayingTypeId =1;

	public static final String ACTION_CHANGE_LIVE_CHANNEL = "com.rocky.android.action.CHANGE_LIVE_CHANNEL";
	// 直播播放记录上传
	private static final String ACTION_REPORT_LIVE_PLAY_RECORD ="com.virgintelecom.iptv.LIVE.PLAYRECORD";
	private static final String ACTION_REPORT_LIVE_START_PLAY ="com.virgintelecom.iptv.LIVE.START.PLAY";

	public static final String LIVE_CHANNEL_ID = "LIVE_CHANNEL_ID";

	private AAALiveChannelsLocalFactory aaaLiveChannelsLocalFactory;
	private ArrayList<LiveChannel> lstLiveChannel;
	private LiveChannelPlayRecord record;

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	// 帮助的fragment
	private LiveHelpFragment helpFragment;
	private AdvertisementView mAdvertisementView;

	// 处理隐藏广告
	private Handler handlerHideAdvertisement;

	/** 
	 * Called when the activity is first created. 
	 * 
	 */ 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tcl_activity_live_channel);

		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


		quiteBackgroundMusic();

		LogUtils.error("on create， 启动fragment");
		fragmentManager = getFragmentManager();

		mAdvertisementView = new AdvertisementView();
		helpFragment = new LiveHelpFragment();

		record = new LiveChannelPlayRecord();
		record.setStbId(TvApplication.stbId);
		record.setUserId(TvApplication.account);
		record.setHotelId(TvApplication.hotelId);
		record.setRoomId(TvApplication.roomId);
		record.setStartPlayTime(new Date());
		record.setLanguage(TvApplication.language);

		LiveTypeLocalFactory liveTypeLocalFactory = new LiveTypeLocalFactory(LiveChannelPlayActivity.this);
		mLstLiveType = liveTypeLocalFactory.getLiveType();

		Intent intent = getIntent();
		if (intent != null) {
			mCurrentPlayChannelID = intent.getStringExtra(Constants.cDetailIdExtra);
		}

		for(PortalLiveType liveType:mLstLiveType){
			for(String channelId:liveType.lstChannelIds){
				if(channelId.equals(mCurrentPlayChannelID)){
					mCurTypeId = liveType.typeId;
					mCurPlayingTypeId = liveType.typeId;
					record.setChannelId(mCurrentPlayChannelID);
					break;
				}
			}
		}



		if (mCurrentPlayChannelID==null||mCurrentPlayChannelID.isEmpty()) {
			finish();
			return;
		}

		// 左侧频道列表初始化
		if(mLiveChoose == null){
			mLiveChoose = (LiveChooseLayout) findViewById(R.id.live_choose_layout);
			mLiveChoose.setOnListChangeListener(this);
			mLiveChoose.updateListByTypeId(mCurTypeId,mCurTypeId ,mCurrentPlayChannelID);
		}

		playbill_main = (FrameLayout) findViewById(R.id.playbill_main);
		tv_menu_category_name = (TextView) findViewById(R.id.tv_menu_category_name);
		liveChannelName = (TextView) findViewById(R.id.live_channel_name);
		tv_playbill_channel_name = (TextView) findViewById(R.id.tv_playbill_channel_name);
		tv_playbill_channel_num = (TextView) findViewById(R.id.tv_playbill_channel_num);
		liveChannelProgressControlbar = (RelativeLayout) findViewById(R.id.live_channel_progress_controlbar);
		timeshiftPlayBeginTime = (TextView) findViewById(R.id.timeshift_play_begin_time);
		timeshift_play_current_time = (TextView) findViewById(R.id.timeshift_play_current_time);
		txtSpeedDisplay = (TextView) findViewById(R.id.timeshift_play_speed);
		timeshiftPlayTotalTime = (TextView) findViewById(R.id.timeshift_play_total_time);
		play = (ImageView) findViewById(R.id.timeshift_play_status_icon);
		progressBar = (ProgressBar) findViewById(R.id.timeshift_channel_play_progressBar);

		//waitProgressBar = (ProgressBar) findViewById(R.id.tv_progressbar);
		userChannelNumber = (TextView) findViewById(R.id.user_channel_number);
		userChannelNumberBg = (ImageView) findViewById(R.id.iv_channel_num_bg);

		tvPlaybill = (TextView) findViewById(R.id.tv_playbill);
		tvPlaybillNext = (TextView) findViewById(R.id.tv_playbill_next);
		timeshiftIcon = (ImageView) findViewById(R.id.timeshift_icon);
		vodPlayErrorExitApk = (LinearLayout) findViewById(R.id.channel_play_error_exit_apk);
		channelPlayErrorTxt = (TextView) findViewById(R.id.channel_play_error_txt);
		channelResult = (LinearLayout) findViewById(R.id.channel_result);
		createProgBill();
		installListeners();
		aaaLiveChannelsLocalFactory = new AAALiveChannelsLocalFactory(this);
		lstLiveChannel = aaaLiveChannelsLocalFactory.getAAALiveChannelListInfos();

	}

	private void createPlayer() {
		player = MediaPlayer.getInstance(this);
		player.setKeepLastFrame(1);
		player.setOnBufferingUpdateListener(this);
		player.setVideoDisplayArea(0, 0, 1920, 1080);
		player.setOnCompletionListener(this);
		player.setOnPreparedListener(this);
		player.setOnVideoSizeChangedListener(this);
		player.setOnRtspStatusEndOfStreamListener(this);
		player.setOnRtspStatusStartOfStreamListener(this);
		player.setOnErrorListener(this);
		player.setOnMediaStatusScaleListener(this);
	}

	private void deletePlayer(){
		if(player != null){
			player.setKeepLastFrame(0);
			player.setOnCompletionListener(null);
			player.setOnPreparedListener(null);
			player.setOnVideoSizeChangedListener(null);
			player.setOnRtspStatusEndOfStreamListener(null);
			player.setOnRtspStatusStartOfStreamListener(null);
			player.setOnErrorListener(null);
			player.setOnMediaStatusScaleListener(null);
		}
	}

	private int fillPlayerInfo() {

		for (int i = 0; i < lstLiveChannel.size(); i++) {
			LiveChannel liveChannel = lstLiveChannel.get(i);
			if (liveChannel.ChannelID.equals(mCurrentPlayChannelID)) {
				currentPlayChannelIndex = i;
			}
			player.getLiveChannelMap().put(liveChannel.ChannelID, liveChannel);
			channelIds.add(liveChannel.ChannelID);
		}
		int result = player.joinChannel(mCurrentPlayChannelID);
		return result;
		

	}

	private void installListeners() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CHANGE_LIVE_CHANNEL);
		intentFilter.addAction(ACTION_REPORT_LIVE_START_PLAY);
		registerReceiver(mBroadcastReceiver, intentFilter);
	}

	private void uninstallListeners() {
		unregisterReceiver(mBroadcastReceiver);
	}

	@Override 
	public void onStart() {
		super.onStart();

		boolean hasAdvertisement = false;
		if(hasAdvertisement){

			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			LogUtils.debug("创建广告fragment");
			mAdvertisementView = new AdvertisementView();
			fragmentTransaction.add(R.id.iptv_live_play, mAdvertisementView);
			fragmentTransaction.commit();

			if(handlerHideAdvertisement == null){
				handlerHideAdvertisement = new Handler();
				handlerHideAdvertisement.postDelayed(runnableHideAdvertisement, 5000);
			}else{
				handlerHideAdvertisement.postDelayed(runnableHideAdvertisement, 5000);
			}
		}else{
			if (player == null) {
				LogUtils.debug("Create MediaPlayer");
				createPlayer();

				int result = fillPlayerInfo();
				if(result == 0){
					LiveChannel channel = aaaLiveChannelsLocalFactory.findRecordZ(mCurrentPlayChannelID);
					record.setUserChannelId(channel.UserChannelID);
					record.setChannelName(channel.ChannelName);
					record.setEndPlayTime(new Date());
					record.setChannelId(channel.ChannelID);
					record.setPlatform(channel.platform);

					Intent intentAmqp = new Intent(ACTION_REPORT_LIVE_START_PLAY);
					Bundle bundle = new Bundle();
					bundle.putSerializable("record", record);
					intentAmqp.putExtras(bundle);
					sendBroadcast(intentAmqp); 

				}else{
					LogUtils.error("启动频道失败");
				}
				//LogUtils.debug("CREATE JOINCHANNEL");
			} else {
				LogUtils.debug("CREATE NOT NULL");
				//tclPlayer.joinChannel(mCurrentPlayChannelID);
			}
			LogUtils.error("LiveChannelPlayActivity 启动start");

			TvApplication.position ="LIVE_ACTIVITY";
			TvApplication.status ="PLAYING_LIVE";

			LogUtils.error("定时启动，准备加载帮助Fragment");
			/*if(handlerShowHelp == null){
				handlerShowHelp = new Handler();
				handlerShowHelp.postDelayed(runnableShowHelp, 3000);
			}else{
				handlerShowHelp.postDelayed(runnableShowHelp, 3000);
			}*/
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.error("LiveChannelPlayActivity 启动resume");
		if (player == null) {
			LogUtils.debug("MediaPlayer Resume");
			createPlayer();

			fillPlayerInfo();
			LogUtils.debug("RESUME JOINCHANNEL");
		} else {
			LogUtils.debug("RESUME Player not null");
			//tclPlayer.joinChannel(mCurrentPlayChannelID);
		}
	}
	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		View v = getCurrentFocus();
		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
			displayUserChannelNumber("0");
			break;
		case KeyEvent.KEYCODE_1:
			displayUserChannelNumber("1");
			break;
		case KeyEvent.KEYCODE_2:
			displayUserChannelNumber("2");
			break;
		case KeyEvent.KEYCODE_3:
			displayUserChannelNumber("3");
			break;
		case KeyEvent.KEYCODE_4:
			displayUserChannelNumber("4");
			break;
		case KeyEvent.KEYCODE_5:
			displayUserChannelNumber("5");
			break;
		case KeyEvent.KEYCODE_6:
			displayUserChannelNumber("6");
			break;
		case KeyEvent.KEYCODE_7:
			displayUserChannelNumber("7");
			break;
		case KeyEvent.KEYCODE_8:
			displayUserChannelNumber("8");
			break;	
		case KeyEvent.KEYCODE_9:
			displayUserChannelNumber("9");
			break;		

		case KeyEvent.KEYCODE_ENTER:
			if (player.getPlayStatus().equals(PlayStatus.CHANNEL_PLAY)) {

				/** 设置透明度渐变动画 */ 
				Animation left_in_animation = AnimationUtils.loadAnimation(LiveChannelPlayActivity.this, R.anim.left_in);
				mLiveChoose.startAnimation(left_in_animation);//使用view的startAnimation方法开始执行动画   
				mLiveChoose.setVisibility(View.VISIBLE);
				mLiveChoose.updateListByTypeId(mCurTypeId,mCurTypeId ,mCurrentPlayChannelID);
				mLiveChoose.currentPlayLiveType = mCurTypeId;
				mLiveChoose.currentPlayUserChannelId = mCurrentPlayChannelID;

				if (handlerChannelList != null) {
					handlerChannelList.removeCallbacks(runnableChannelLeftList);
					handlerChannelList.postDelayed(runnableChannelLeftList, 4000);
				}
			} else if (player.getPlayStatus().equals(PlayStatus.FASTREWIND) || player.getPlayStatus().equals(PlayStatus.FASTFORWORD)) {
				//channelList.setVisibility(View.INVISIBLE);
				// 回复正常播放；
				player.normalrate();
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
			} else if (player.getPlayStatus().equals(PlayStatus.PAUSE)) {
				player.resume();
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
			} else if (v instanceof ListView) { //正在显示状态
				// 播放所在焦点的频道。
				LogUtils.info("----->播放所在焦点的频道");
			}
			break;
		case KeyEvent.KEYCODE_BACK:
			if (getCurrentFocus() != null) {
				if (v instanceof ListView) {	//if (mLiveChoose.getVisibility() == View.VISIBLE) {
					Animation left_out_animation = AnimationUtils.loadAnimation(LiveChannelPlayActivity.this, R.anim.left_out);
					mLiveChoose.startAnimation(left_out_animation);//使用view的startAnimation方法开始执行动画   
					mLiveChoose.setVisibility(View.INVISIBLE);
					return false;
				} else if (liveChannelProgressControlbar.getVisibility() == View.VISIBLE) {
					if (player.getPlayStatus().equals(PlayStatus.FASTFORWORD) || player.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
						player.normalrate();
						liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
					} else if (player.getPlayStatus().equals(PlayStatus.PAUSE)) {
						player.resume();
					}
					return false;
				} else {
				}
			} else {
				// 停止直播播放，离开当前页面
				handler.removeCallbacks(runnableProgressControl);
				fragmentTransaction = fragmentManager.beginTransaction();
				//fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
				//fragmentTransaction = fragmentManager.beginTransaction();

				if(helpFragment.isAdded() || helpFragment.isVisible()){
					if(helpFragment.isVisible()){
						fragmentTransaction.hide(helpFragment);
					}
					fragmentTransaction.remove(helpFragment);
					fragmentTransaction.commit();
					return false;
				}else if(player.getPlayType() != null){
					if (player.getPlayType().equals(PlayType.SCHEDULE)) {
						player.stop();
					} else {
						player.setKeepLastFrame(0);
						player.leaveChannel();

						LiveChannelPlayRecord recordToSave = new LiveChannelPlayRecord();
						recordToSave.setStbId(TvApplication.stbId);
						recordToSave.setUserId(TvApplication.account);
						recordToSave.setHotelId(TvApplication.hotelId);
						recordToSave.setRoomId(TvApplication.roomId);
						recordToSave.setStartPlayTime(record.getStartPlayTime());
						recordToSave.setLanguage(TvApplication.language);
						recordToSave.setChannelId(mCurrentPlayChannelID);

						LiveChannel channel = (LiveChannel) player.getLiveChannelMap().get(channelIds.get(currentPlayChannelIndex));
						liveChannelName.setText(channel.ChannelName);
						recordToSave.setUserChannelId(channel.UserChannelID);
						recordToSave.setChannelName(channel.ChannelName);
						recordToSave.setEndPlayTime(new Date());
						recordToSave.setChannelId(channel.ChannelID);
						recordToSave.setPlatform(channel.platform);
						//if((recordToSave.getEndPlayTime().getTime() - recordToSave.getStartPlayTime().getTime()) > 10000){
						//liveChannelPlayRecordFactory.DownloadDatas(recordToSave);

						//添加发送信息到rabbitmq去
						LogUtils.debug("发送向rabbitmq送信息的事件");
						Intent intent = new Intent(ACTION_REPORT_LIVE_PLAY_RECORD);
						Bundle bundle = new Bundle();
						bundle.putSerializable("record", recordToSave);
						intent.putExtras(bundle);
						sendBroadcast(intent); 

						//}
						player.clearVideo();
					}
				}
			}
			break;
		case KeyEvent.KEYCODE_HOME:
			LogUtils.debug("HOME 键");
			player.leaveChannel();
			//uninstallListeners();
			deletePlayer();
			releaseMediaPlayer();
			//BackgroundMusic.getInstance(this).playBackgroundMusic("music/background_ex.mp3", true);
			TvApplication.status ="FREE";
			LogUtils.debug("Destroy Activity");
			ActivityStack.popAllFront();
			break;
		case 17:
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if(helpFragment.isAdded()){
				if(helpFragment.isVisible()){
					fragmentTransaction = fragmentManager.beginTransaction();
					//fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
					fragmentTransaction.hide(helpFragment);
					fragmentTransaction.remove(helpFragment);
					fragmentTransaction.commit();
				}
			}

			if (mLiveChoose.getVisibility() == View.VISIBLE) {
				if (handlerChannelList != null) {
					handlerChannelList.removeCallbacks(runnableChannelLeftList);
					handlerChannelList.postDelayed(runnableChannelLeftList, 4000);
				}
				//请加判断是否是第一个，如果是，跳转最后一个或者上一个分类
			} else {
				if (liveChannelProgressControlbar.getVisibility() == View.VISIBLE) {
					liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
					timeshiftIcon.setVisibility(View.INVISIBLE);
				}
				timeshiftIcon.setVisibility(View.INVISIBLE);
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
				vodPlayErrorExitApk.setVisibility(View.INVISIBLE);
				progressBar.setProgress(0);
				channelUp();
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:

			if(helpFragment.isAdded()){
				if(helpFragment.isVisible()){
					fragmentTransaction = fragmentManager.beginTransaction();
					//fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
					fragmentTransaction.hide(helpFragment);
					fragmentTransaction.remove(helpFragment);
					fragmentTransaction.commit();
				}
			}

			if (mLiveChoose.getVisibility() == View.VISIBLE) {
				if (handlerChannelList != null) {
					handlerChannelList.removeCallbacks(runnableChannelLeftList);
					handlerChannelList.postDelayed(runnableChannelLeftList, 4000);
				}
				//请加判断是否是第一个，如果是，跳转最后一个或者上一个分类
			} else {
				if (liveChannelProgressControlbar.getVisibility() == View.VISIBLE) {
					liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
					timeshiftIcon.setVisibility(View.INVISIBLE);
				}
				timeshiftIcon.setVisibility(View.INVISIBLE);
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
				vodPlayErrorExitApk.setVisibility(View.INVISIBLE);
				progressBar.setProgress(0);
				channelDown();
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			LiveChannel channel = (LiveChannel) player.getLiveChannelMap().get(channelIds.get(currentPlayChannelIndex));

			if (v instanceof ListView) {	
				mCurTypeId--;
				if (mCurTypeId == 0) {
					mCurTypeId = mLstLiveType.size();
				}
				mLiveChoose.updateListByTypeId(mCurTypeId,mCurTypeId ,mCurrentPlayChannelID);

				if (handlerChannelList != null) {
					handlerChannelList.removeCallbacks(runnableChannelLeftList);
					handlerChannelList.postDelayed(runnableChannelLeftList, 4000);
				}
				return false;
			} else if (liveChannelProgressControlbar.getVisibility() == View.VISIBLE) {

				if(channel.TimeShift && TvApplication.shoulLoginIptv){
					if (player.getPlayStatus().equals(PlayStatus.CHANNEL_PLAY)) {
						speed = (float) 2.0;
						player.fastRewind(speed);
						canForward = true;
					}else if(player.getPlayStatus().equals(PlayStatus.FASTREWIND)){
						if (speed == 32) {
							speed = (float) 2.0;
						} else {
							speed = speed * 2;
						}
						player.fastRewind(speed);
						canForward = true;
					}else if(player.getPlayStatus().equals(PlayStatus.PAUSE)){
						speed = (float) 2.0;
						player.fastRewind(speed);
						canForward = true;
					}

					txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(-speed)));

					if (handler != null) {
						handler.removeCallbacks(runnableProgressControl);
						handler.postDelayed(runnableProgressControl, 2000);
					}
				}else{
					LogUtils.info("此频道不支持时移");
				}
			}else if(mLiveChoose.getVisibility() == View.VISIBLE){
				LogUtils.error("频道列表可见");
				mCurTypeId--;
				if (mCurTypeId == 0) {
					mCurTypeId = mLstLiveType.size();
				}
				mLiveChoose.updateListByTypeId(mCurTypeId,mCurTypeId ,mCurrentPlayChannelID);

				if (handlerChannelList != null) {
					handlerChannelList.removeCallbacks(runnableChannelLeftList);
					handlerChannelList.postDelayed(runnableChannelLeftList, 4000);
				}
				return false;
			}
			else {
				if(channel.TimeShift && TvApplication.shoulLoginIptv){
					if (player.getPlayStatus().equals(PlayStatus.CHANNEL_PLAY)) {
						speed = (float) 2.0;
						player.fastRewind(speed);
						canForward = true;
					}else if(player.getPlayStatus().equals(PlayStatus.FASTREWIND)){
						if (speed == 32) {
							speed = (float)2.0;
						} else {
							speed = speed * 2;
						}
						player.fastRewind(speed);
						canForward = true;
					}else if(player.getPlayStatus().equals(PlayStatus.TIMESHIFT_PLAY)){
						speed = (float) 2.0;
						player.fastRewind(speed);
						canForward = true;
					}

					play.setImageResource(R.drawable.rewind_icon);
					liveChannelProgressControlbar.setVisibility(View.VISIBLE);
					txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(-speed)));
					timeshiftIcon.setVisibility(View.VISIBLE);
					if (handler != null) {
						handler.removeCallbacks(runnableProgressControl);
						handler.postDelayed(runnableProgressControl, 1000);
					}
				}else{
					LogUtils.info("此频道不支持时移");
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (v instanceof ListView) {	//if (mLiveChoose.getVisibility() == View.VISIBLE) {
				mCurTypeId++;
				if (mCurTypeId >= mLstLiveType.size()) {
					mCurTypeId = 1;
				}
				mLiveChoose.updateListByTypeId(mCurTypeId,mCurTypeId ,mCurrentPlayChannelID);
				if (handlerChannelList != null) {
					handlerChannelList.removeCallbacks(runnableChannelLeftList);
					handlerChannelList.postDelayed(runnableChannelLeftList, 4000);
				}
				return false;
			} else if (liveChannelProgressControlbar.getVisibility() == View.GONE
					|| liveChannelProgressControlbar.getVisibility() == View.INVISIBLE) {

				if (canForward) {
					if (!player.getPlayStatus().equals(PlayStatus.FASTFORWORD)) {
						speed = (float) 2.0;
					}else if(player.getPlayStatus().equals(PlayStatus.TIMESHIFT_PLAY)){
						speed = (float) 2.0;
					}
					else 
					{
						if (speed == 32) {
							speed = (float) 2.0;
						} else {
							speed = speed * 2;
						}
					}
					player.fastForward(speed);
					play.setImageResource(R.drawable.speed_icon);
					liveChannelProgressControlbar.setVisibility(View.VISIBLE);
					if (handler != null) {
						handler.removeCallbacks(runnableProgressControl);
						handler.postDelayed(runnableProgressControl, 1000);
					}
					txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(speed)));
				}
			} else {
				LiveChannel channelPlay = (LiveChannel) player.getLiveChannelMap().get(channelIds.get(currentPlayChannelIndex));
				if(channelPlay.TimeShift){
					if (!player.getPlayStatus().equals(PlayStatus.FASTFORWORD)) {
						speed = (float) 2.0;
					}else if(player.getPlayStatus().equals(PlayStatus.TIMESHIFT_PLAY)){
						speed = (float) 2.0;
					}else {
						if (speed == 32) {
							speed = (float) 2.0;
						} else {
							speed = speed * 2;
						}
					}
					player.fastForward(speed);
					play.setImageResource(R.drawable.speed_icon);
					liveChannelProgressControlbar.setVisibility(View.VISIBLE);
					if (handler != null) {
						handler.removeCallbacks(runnableProgressControl);
						handler.postDelayed(runnableProgressControl, 1000);
					}
					txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(speed)));
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if(helpFragment != null){
				if(helpFragment.isAdded()){
					if(helpFragment.isVisible()){
						fragmentTransaction = fragmentManager.beginTransaction();
						// fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
						fragmentTransaction.hide(helpFragment);
						fragmentTransaction.remove(helpFragment);
						fragmentTransaction.commit();
						handlerShowHelp.removeCallbacks(runnableHideHelp); 
					}
				}
			}

			if(userChannelNumber.getVisibility() == View.VISIBLE){
				if(handlerUserChannelNumber != null) {
					handlerUserChannelNumber.removeCallbacks(runnablePlayChannel);
					handlerUserChannelNumber.postDelayed(runnablePlayChannel, 400);
				}
			}
			else if (player.getPlayStatus().equals(PlayStatus.CHANNEL_PLAY) || player.getPlayStatus().equals(PlayStatus.CHANNEL_NOTAVAILABLENOW)) 
			{
				if(player.getPlayStatus().equals(PlayStatus.CHANNEL_NOTAVAILABLENOW)){
					channelPlayErrorTxt.setVisibility(View.VISIBLE);
					channelPlayErrorTxt.setText("");
					vodPlayErrorExitApk.setVisibility(View.GONE);
				}

				if(handlerShowHelp.post(runnableShowHelp)){
					handlerShowHelp.removeCallbacks(runnableShowHelp);
				}

				/** 设置透明度渐变动画 */ 
				Animation left_in_animation = AnimationUtils.loadAnimation(LiveChannelPlayActivity.this, R.anim.left_in);
				if(mLiveChoose.getAnimation() == null){
					mLiveChoose.startAnimation(left_in_animation);
					mLiveChoose.setVisibility(View.VISIBLE);
					mLiveChoose.updateListByTypeId(mCurTypeId,mCurTypeId ,mCurrentPlayChannelID);
					if (handlerChannelList != null) {
						handlerChannelList.removeCallbacks(runnableChannelLeftList);
						handlerChannelList.postDelayed(runnableChannelLeftList, 5000);
					}

					if(playbill_main.getVisibility() == View.VISIBLE){
						playbill_main.setVisibility(View.INVISIBLE);
					}
				}
			} else if (player.getPlayStatus().equals(PlayStatus.FASTREWIND) || player.getPlayStatus().equals(PlayStatus.FASTFORWORD)) {
				// 回复正常播放；
				player.normalrate();
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
			} else if (player.getPlayStatus().equals(PlayStatus.PAUSE)) {
				player.resume();
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
			}else if(player.getPlayStatus().equals(PlayStatus.TIMESHIFT_PLAY)){
				/** 设置透明度渐变动画 */ 
				Animation left_in_animation = AnimationUtils.loadAnimation(LiveChannelPlayActivity.this, R.anim.left_in);
				if(mLiveChoose.getAnimation() == null){
					mLiveChoose.startAnimation(left_in_animation);
					mLiveChoose.setVisibility(View.VISIBLE);
					mLiveChoose.updateListByTypeId(mCurTypeId,mCurTypeId ,mCurrentPlayChannelID);
					if (handlerChannelList != null) {
						handlerChannelList.removeCallbacks(runnableChannelLeftList);
						handlerChannelList.postDelayed(runnableChannelLeftList, 5000);
					}

					if(playbill_main.getVisibility() == View.VISIBLE){
						playbill_main.setVisibility(View.INVISIBLE);
					}
				}

			}else if (v instanceof ListView) { //正在显示状态

				// 播放所在焦点的频道。
			}else{
				LogUtils.info("状态不知道");
			}
			break;
		case KeyEvent.KEYCODE_PAGE_UP:
			if(player.getPlayStatus().equals(PlayStatus.TIMESHIFT_PLAY)){
				player.gotoStart();
			}
			break;
		case KeyEvent.KEYCODE_PAGE_DOWN:
			if(player.getPlayStatus().equals(PlayStatus.TIMESHIFT_PLAY)){
				player.gotoEnd();
				canForward = false;
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
				timeshiftIcon.setVisibility(View.INVISIBLE);
				if (handler != null) {
					handler.removeCallbacks(runnableProgressControl);
				}
			}
			break;	
		case 82:

			if(handlerShowHelp == null){
				handlerShowHelp = new Handler();
				handlerShowHelp.post(runnableShowHelp);
			}else{
				handlerShowHelp.post(runnableShowHelp);
			}
			break;
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			LogUtils.info("-------->>暂停");
			LogUtils.error("player.getPlayStatus()--------->>"+player.getPlayStatus());
			if(player.getPlayStatus() == PlayStatus.TIMESHIFT_PLAY){
				player.pause();
				play.setImageResource(R.drawable.pause_iocn);
				if (handler != null) {
					handler.removeCallbacks(runnableProgressControl);
					handler.postDelayed(runnableProgressControl, 500);
				}
				txtSpeedDisplay.setText("X 0");
				liveChannelProgressControlbar.setVisibility(View.VISIBLE);
			}else if(player.getPlayStatus() == PlayStatus.PAUSE){
				player.resume();
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
			}else if(player.getPlayStatus() == PlayStatus.FASTFORWORD || player.getPlayStatus() == PlayStatus.FASTREWIND){
				player.normalrate();
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
			}else if(player.getPlayStatus() == PlayStatus.CHANNEL_PLAY){
				player.pause();
				timeshiftIcon.setVisibility(View.VISIBLE);
				play.setImageResource(R.drawable.pause_iocn);
				if (handler != null) {
					handler.removeCallbacks(runnableProgressControl);
					handler.postDelayed(runnableProgressControl, 500);
				}
				txtSpeedDisplay.setText("X 0");
				liveChannelProgressControlbar.setVisibility(View.VISIBLE);
			}
			break;	
		case KeyEvent.KEYCODE_CHANNEL_UP:

			if(helpFragment.isAdded()){
				if(helpFragment.isVisible()){
					fragmentTransaction = fragmentManager.beginTransaction();
					//fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
					fragmentTransaction.hide(helpFragment);
					fragmentTransaction.remove(helpFragment);
					fragmentTransaction.commit();
				}
			}

			if (mLiveChoose.getVisibility() == View.VISIBLE) {
				if (handlerChannelList != null) {
					handlerChannelList.removeCallbacks(runnableChannelLeftList);
					Animation left_out_animation = AnimationUtils.loadAnimation(LiveChannelPlayActivity.this, R.anim.left_out);
					mLiveChoose.startAnimation(left_out_animation);//使用view的startAnimation方法开始执行动画
					mLiveChoose.setVisibility(View.INVISIBLE);
					channelUp();
				}
				//请加判断是否是第一个，如果是，跳转最后一个或者上一个分类
			} else {
				if (liveChannelProgressControlbar.getVisibility() == View.VISIBLE) {
					liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
					timeshiftIcon.setVisibility(View.INVISIBLE);
				}
				timeshiftIcon.setVisibility(View.INVISIBLE);
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
				vodPlayErrorExitApk.setVisibility(View.INVISIBLE);
				channelUp();
			}
			break;
		case KeyEvent.KEYCODE_CHANNEL_DOWN:

			if(helpFragment.isAdded()){
				if(helpFragment.isVisible()){
					fragmentTransaction = fragmentManager.beginTransaction();
					//fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
					fragmentTransaction.hide(helpFragment);
					fragmentTransaction.remove(helpFragment);
					fragmentTransaction.commit();
				}
			}

			if (mLiveChoose.getVisibility() == View.VISIBLE) {
				if (handlerChannelList != null) {
					handlerChannelList.removeCallbacks(runnableChannelLeftList);
					Animation left_out_animation = AnimationUtils.loadAnimation(LiveChannelPlayActivity.this, R.anim.left_out);
					mLiveChoose.startAnimation(left_out_animation); //使用view的startAnimation方法开始执行动画
					mLiveChoose.setVisibility(View.INVISIBLE);
					progressBar.setProgress(0);
					channelDown();
				} //请加判断是否是第一个，如果是，跳转最后一个或者上一个分类
			} else {
				if (liveChannelProgressControlbar.getVisibility() == View.VISIBLE) {
					liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
					timeshiftIcon.setVisibility(View.INVISIBLE);
				}
				timeshiftIcon.setVisibility(View.INVISIBLE);
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
				vodPlayErrorExitApk.setVisibility(View.INVISIBLE);
				progressBar.setProgress(0);
				channelDown();
			}
			break;
		case 253:
			break;
		case 303:
			break;
		case 328: 	// channel +
			channelUp();
			break;
		case 331:	// channel -
			channelDown();
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void stopCurPlayer() {
		if (player.getPlayType().equals(PlayType.SCHEDULE)) {
			player.setKeepLastFrame(0);
			LogUtils.debug("Stop");
			player.stop();

		} else {
			player.leaveChannel();
			// 增加直播播放记录上报信息
			LogUtils.info("添加直播上报信息");

			LiveChannelPlayRecord recordToSave = new LiveChannelPlayRecord();
			recordToSave.setStbId(TvApplication.stbId);
			recordToSave.setUserId(TvApplication.account);
			recordToSave.setHotelId(TvApplication.hotelId);
			recordToSave.setRoomId(TvApplication.roomId);
			recordToSave.setStartPlayTime(record.getStartPlayTime());
			recordToSave.setLanguage(TvApplication.language);
			recordToSave.setChannelId(mCurrentPlayChannelID);

			//			recordToSave.setChannelId(mCurrentPlayChannelID);
			LiveChannel channel = (LiveChannel) player.getLiveChannelMap().get(channelIds.get(currentPlayChannelIndex));
			liveChannelName.setText(channel.ChannelName);
			recordToSave.setUserChannelId(channel.UserChannelID);
			recordToSave.setChannelName(channel.ChannelName);
			recordToSave.setEndPlayTime(new Date());
			recordToSave.setChannelId(channel.ChannelID);
			recordToSave.setPlatform(channel.platform);
			//只上传看了10秒钟以上的
			//if((recordToSave.getEndPlayTime().getTime() - recordToSave.getStartPlayTime().getTime()) > 10000){
			//liveChannelPlayRecordFactory.DownloadDatas(recordToSave);
			Intent intent = new Intent(ACTION_REPORT_LIVE_PLAY_RECORD);
			Bundle bundle = new Bundle();
			bundle.putSerializable("record", recordToSave);
			intent.putExtras(bundle);
			sendBroadcast(intent); 
			//}
		}
	}

	private void updateChannelInfo()
	{
		playbill_main.setVisibility(View.VISIBLE);
		LiveChannel channel = (LiveChannel) player.getLiveChannelMap().get(channelIds.get(currentPlayChannelIndex));
		liveChannelName.setText(channel.ChannelName);
		
		mCurrentPlayChannelID = channelIds.get(currentPlayChannelIndex);
		LogUtils.debug("mCurrentPlayChannelID:"+mCurrentPlayChannelID);
		
		//更新正在播放的频道信息
		for(PortalLiveType liveType:mLstLiveType){
			for(String channelId:liveType.lstChannelIds){
				if(channelId.equals(mCurrentPlayChannelID)){
					mCurPlayingTypeId = liveType.typeId;
					mCurTypeId=mCurPlayingTypeId;
					break;
				}
			}
		}
		Log.d("LiveChannelPlayActivity", "开始更新左列表：mCurTypeId："+mCurTypeId+";mCurPlayingTypeId:"+mCurPlayingTypeId+";mCurrentPlayChannelID:"+mCurrentPlayChannelID);

		mLiveChoose.currentPlayLiveType =mCurPlayingTypeId;
		mLiveChoose.currentPlayUserChannelId=mCurrentPlayChannelID;
		mLiveChoose.updateListByTypeId(mCurTypeId,mCurPlayingTypeId ,mCurrentPlayChannelID);

		//		player.joinChannel(mCurrentPlayChannelID);	
		player.joinChannel(mCurrentPlayChannelID);	
		tv_playbill_channel_name.setText(channel.ChannelName);
		//waitProgressBar.setVisibility(View.VISIBLE);
		tv_playbill_channel_num.setText(Integer.toString(channel.UserChannelID));

		createProgBill();

		record.setStartPlayTime(new Date());
		record.setChannelId(channel.ChannelID);
		record.setUserChannelId(channel.UserChannelID);
		record.setChannelName(channel.ChannelName);
		record.setEndPlayTime(null);
		record.setPlatform(channel.platform);

		if (handlerProgBill != null) {
			handlerProgBill.removeCallbacks(runnableChannelInfo);
			handlerProgBill.postDelayed(runnableChannelInfo, 3000);
		}

		Intent intentAmqp = new Intent(ACTION_REPORT_LIVE_START_PLAY);
		Bundle bundle = new Bundle();
		bundle.putSerializable("record", record);
		intentAmqp.putExtras(bundle);
		sendBroadcast(intentAmqp); 

	}

	private void channelUp() {
		stopCurPlayer();
		if (currentPlayChannelIndex < channelIds.size()-1) {
			currentPlayChannelIndex++;
		} else {
			currentPlayChannelIndex = 0;
		}
		updateChannelInfo();
	}

	private void channelDown() {
		stopCurPlayer();
		if (currentPlayChannelIndex > 0) {
			currentPlayChannelIndex--;
		} else {
			currentPlayChannelIndex = channelIds.size()-1;
		}
		updateChannelInfo();
	}

	// 要做的工作，定时隐藏频道信息；
	Runnable runnableProgressControl = new Runnable() {
		@Override
		public void run() {
			//要做的事情
			if (player.getPlayStatus().equals(PlayStatus.CHANNEL_PLAY) ) {
				// 回复正常播放；
				player.normalrate();
				if (timeshiftIcon.getVisibility()==View.VISIBLE) {
					timeshiftIcon.setVisibility(View.GONE);
				}
				
				liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
			}else{
				setProgressInfo();
			}
			handler.postDelayed(runnableProgressControl, 500);
		}
	};

	// 要做的工作，定时隐藏频道信息；
	Runnable runnableChannelInfo = new Runnable() {
		@Override
		public void run() {
			playbill_main.setVisibility(View.INVISIBLE);
		}
	};

	// 要做的工作，定时隐藏左侧频道列表信息；
	Runnable runnableChannelLeftList = new Runnable() {
		@Override
		public void run() {
			/** 设置透明度渐变动画 */ 
			Animation left_out_animation = AnimationUtils.loadAnimation(LiveChannelPlayActivity.this, R.anim.left_out);
			mLiveChoose.startAnimation(left_out_animation);//使用view的startAnimation方法开始执行动画
			mLiveChoose.setVisibility(View.INVISIBLE);
		}
	};


	// 要做的工作,定时处理数字频道输入和切换；
	Runnable runnableUserChannelNumber = new Runnable() {
		@Override
		public void run() {
			userChannelNumber.setVisibility(View.INVISIBLE);
			userChannelNumberBg.setVisibility(View.GONE);
			channelResult.setVisibility(View.GONE);
			userChannelNumber.setText("");
		}
	};


	// 显示未找到频道的提示信息
	Runnable runnableDisplayNoChannel = new Runnable() {
		@Override
		public void run() {

			userChannelNumber.setVisibility(View.GONE);
			userChannelNumberBg.setVisibility(View.GONE);
			channelResult.setVisibility(View.VISIBLE);

			if(handlerHideNoChannelInfo != null){
				handlerHideNoChannelInfo.removeCallbacks(runnableHideNoChannel);
				handlerHideNoChannelInfo.postDelayed(runnableHideNoChannel, 3000);
			}
		}
	};

	// 隐藏无此频道信息
	Runnable runnableHideNoChannel = new Runnable() {
		@Override
		public void run() {
			channelResult.setVisibility(View.GONE);
		}
	};

	// 隐藏无此频道信息
	Runnable runnableChangeChannel = new Runnable() {
		@Override
		public void run() {
			stopCurPlayer();
			updateChannelInfo();
			userChannelNumber.setVisibility(View.INVISIBLE);
			userChannelNumber.setText("");
			userChannelNumberBg.setVisibility(View.INVISIBLE);
		}
	};


	// 隐藏时移信息
	Runnable runnableTimeshiftInfo = new Runnable() {
		@Override
		public void run() {
			channelPlayErrorTxt.setVisibility(View.VISIBLE);
			channelPlayErrorTxt.setText("");
			vodPlayErrorExitApk.setVisibility(View.GONE);
		}
	};


	// 要做的工作,播放数字输入的频道
	Runnable runnablePlayChannel = new Runnable() {
		@Override
		public void run() {
			boolean find = false;
			String userChannelNumberStr = userChannelNumber.getText().toString();
			LogUtils.info("userChannelNumberStr----->"+userChannelNumberStr);
			for (int i = 0; i < lstLiveChannel.size(); i++) {
				LiveChannel liveChannel = lstLiveChannel.get(i);
				if(userChannelNumberStr != ""){
					if (liveChannel.UserChannelID == Integer.parseInt(userChannelNumberStr)) {
						currentPlayChannelIndex = i;
						find = true;
						break;
					}
				}
			}

			if(!find){
				channelResult.setVisibility(View.VISIBLE);
				if(handlerNoChannelInfo != null){
					handlerNoChannelInfo.removeCallbacks(runnableHideNoChannel);
					handlerNoChannelInfo.postDelayed(runnableHideNoChannel, 2000);
				}

				if (handlerUserChannelNumber != null) {
					handlerUserChannelNumber.removeCallbacks(runnableUserChannelNumber);
					handlerUserChannelNumber.postDelayed(runnableUserChannelNumber, 500);
				}
			}else{

				// 先将频道号移动到左下角
				Log.d("LiveChannelPlayActivity","开始动画");
				Animation center_to_left_bottom_animation = AnimationUtils.loadAnimation(LiveChannelPlayActivity.this, R.anim.center_to_left_bottom);
				userChannelNumber.startAnimation(center_to_left_bottom_animation);
				Log.d("LiveChannelPlayActivity","结束动画");
				userChannelNumber.setVisibility(View.INVISIBLE);
				userChannelNumber.setText("");
				userChannelNumberBg.setVisibility(View.INVISIBLE);
				stopCurPlayer();
				updateChannelInfo();


			}
		}
	};


	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
		LogUtils.debug("onBufferingUpdate percent:" + percent);
	}

	public void onCompletion(MediaPlayer arg0) {
		LogUtils.debug("直播界面,onCompletion called");
		if (timeshiftIcon.getVisibility()==View.VISIBLE) {
			timeshiftIcon.setVisibility(View.GONE);
		}
	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		LogUtils.debug("onVideoSizeChanged called");
		//		if (width == 0 || height == 0) {
		//			Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
		//			return;
		//		}
		//		mIsVideoSizeKnown = true;
		//		mVideoWidth = width;
		//		mVideoHeight = height;
		//		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
		//			startVideoPlayback();
		//		}
	}

	// 退出前释放player
	private void releaseMediaPlayer() {
		if (player != null) {
			player.release();
			player = null;

		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		LogUtils.debug("直播收到准备好信息,后面可提示频道信息。");
		if(vodPlayErrorExitApk.getVisibility() == View.VISIBLE){
			channelPlayErrorTxt.setVisibility(View.VISIBLE);
			channelPlayErrorTxt.setText("");
			vodPlayErrorExitApk.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onRtspStatusEndOfStream(MediaPlayer mp) {
		LogUtils.debug("onRtspStatusEndOfStream");
		canForward = false;
		liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
		timeshiftIcon.setVisibility(View.INVISIBLE);
		if (handler != null) {
			handler.removeCallbacks(runnableProgressControl);
		}
	}

	@Override
	public void onRtspStatusStartOfStream(MediaPlayer mp) {
		LogUtils.debug("onRtspStatusStartOfStream");
		liveChannelProgressControlbar.setVisibility(View.INVISIBLE);
		if (handler != null) {
			handler.removeCallbacks(runnableProgressControl);
		}
	}

	@Override
	public void onMediaStatusScaleListener(MediaPlayer mp, int speed) {
		LogUtils.info("收到 onMediaStatusScale speed:"+ speed+ "  开始改变界面状态");
		this.speed=(float) speed;
		//直播 -1，暂停0，时宜播放1，快进2倍就是2，快退2倍就是-2

		if(player.getPlayStatus() == PlayStatus.FASTREWIND){
			LogUtils.debug("后退");
			play.setImageResource(R.drawable.rewind_icon);
			liveChannelProgressControlbar.setVisibility(View.VISIBLE);
			txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(-speed)));
		}else if(player.getPlayStatus() == PlayStatus.FASTFORWORD){
			play.setImageResource(R.drawable.speed_icon);
			liveChannelProgressControlbar.setVisibility(View.VISIBLE);
			txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(speed)));
		}else if(player.getPlayStatus() == PlayStatus.TIMESHIFT_PLAY){

		}else if(player.getPlayStatus() == PlayStatus.CHANNEL_PLAY){
			liveChannelProgressControlbar.setVisibility(View.GONE);
			timeshiftIcon.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		LogUtils.info("收到错误:"+what);
		if (what == MediaPlayer.MEDIA_DATA_FAILED_TODECODER) {
			vodPlayErrorExitApk.setVisibility(View.VISIBLE);
		}else if (what == MediaPlayer.EIS_VOD_PLAY_ABNORMAL){
		
			channelPlayErrorTxt.setVisibility(View.VISIBLE);
			channelPlayErrorTxt.setText(LiveChannelPlayActivity.this.getResources().getString(R.string.channel_media_not_available_now));
			vodPlayErrorExitApk.setVisibility(View.VISIBLE);	
		}else if (what == MediaPlayer.MEDIA_DATA_NOTAVAILABLENOW){
		
			channelPlayErrorTxt.setVisibility(View.VISIBLE);
			channelPlayErrorTxt.setText(LiveChannelPlayActivity.this.getResources().getString(R.string.channel_media_not_available_now));
			vodPlayErrorExitApk.setVisibility(View.VISIBLE);
		}else if (what == MediaPlayer.EIS_VOD_MEMBERSHIP_ERROR){ // 上海电信使用,组播控制异常,p2值区分具体含义:1:组播源错误,无法播放
			if(extra == 1){
				LiveChannel channelToPlay = aaaLiveChannelsLocalFactory.findRecordZ(mCurrentPlayChannelID);
				channelPlayErrorTxt.setVisibility(View.VISIBLE);
				channelPlayErrorTxt.setText(channelToPlay.ChannelName+ "--" + LiveChannelPlayActivity.this.getResources().getString(R.string.channel_media_not_available_now));
				vodPlayErrorExitApk.setVisibility(View.VISIBLE);	
			}
		}else if(what == MediaPlayer.MEDIA_PREPARE_FAILED){
			channelPlayErrorTxt.setVisibility(View.VISIBLE);
			channelPlayErrorTxt.setText(LiveChannelPlayActivity.this.getResources().getString(R.string.channel_media_prepare_failed));
			vodPlayErrorExitApk.setVisibility(View.VISIBLE);

			liveChannelProgressControlbar.setVisibility(View.GONE);
			timeshiftIcon.setVisibility(View.GONE);
			if (handlerHideTimeshiftInfo != null) {
				handlerHideTimeshiftInfo.removeCallbacks(runnableTimeshiftInfo);
				handlerHideTimeshiftInfo.postDelayed(runnableTimeshiftInfo, 3000);
			}

			if (handler != null) {
				handler.removeCallbacks(runnableProgressControl);
				//handler.postDelayed(runnableProgressControl, 1000);
			}

			canForward = false;
		}
		return false;
	}

	@Override
	public void onStop() {
		player.setKeepLastFrame(0);
		player.clearVideo();
		BackgroundMusic.getInstance(this).playBackgroundMusic("music/background_ex.mp3", true);
		deletePlayer();
		releaseMediaPlayer();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		if(handlerShowHelp.post(runnableHideHelp)){
			handlerShowHelp.removeCallbacks(runnableHideHelp);
		}
		if(handlerShowHelp.post(runnableShowHelp)){
			handlerShowHelp.removeCallbacks(runnableShowHelp);
		}
		TvApplication.status ="FREE";
		uninstallListeners();
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}


	// 绘画当前点的信息
	@SuppressLint("SimpleDateFormat")
	private void setProgressInfo() {
		if (player != null) {
			if (player.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
					|| player.getPlayStatus().equals(PlayStatus.FASTREWIND)||player.getPlayStatus().equals(PlayStatus.PAUSE)) {
				DateFormat formatter = new SimpleDateFormat("HH-mm-ss");
				String nowstr = formatter.format(new Date());
				LogUtils.info("nowstr-------->>>>>"+nowstr);
				long currentPlayTime = (long) player.getCurrentPlayTime();
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
				Calendar endCal = Calendar.getInstance();

				Date end = endCal.getTime();

				int duration = player.getMediaDuration();

				Calendar beginCal = Calendar.getInstance();
				long beginPlayTime = endCal.getTimeInMillis()-duration*1000;

				beginCal.setTimeInMillis(beginPlayTime);
				Date begin = beginCal.getTime();

				format.setTimeZone(TimeZone.getTimeZone("GMT+8"));

				Calendar currentCal = Calendar.getInstance();
				currentCal.setTimeInMillis((currentPlayTime)*1000);
				Date current = currentCal.getTime();

				int now = (int) (currentPlayTime -(beginCal.getTimeInMillis()/1000));
				progressBar.setMax(duration);
				progressBar.setProgress(now);

				timeshiftPlayTotalTime.setText(format.format(end));
				timeshiftPlayBeginTime.setText(format.format(begin));
				timeshift_play_current_time.setText(format.format(current));
			}
		}
	}

	// 生成节目提示
	private void createProgBill() {
		boolean find = false;
		if(TvApplication.platform==EnumType.Platform.ZTE){
			if(AllChannelProgBilJsonlFactory.mapLiveChannelBillZte != null){
				if (AllChannelProgBilJsonlFactory.mapLiveChannelBillZte.containsKey(mCurrentPlayChannelID)) {
					LiveChannelBill liveChannelBill = AllChannelProgBilJsonlFactory.mapLiveChannelBillZte.get(mCurrentPlayChannelID);
					if (liveChannelBill.ChannelID.equals(mCurrentPlayChannelID)) {
						for (int j = 0; j < liveChannelBill.lstProgBill.size(); j++) {
							ProgBill prog = liveChannelBill.lstProgBill.get(j);
							String beginDate = prog.beginDate;
							String beginTime = prog.beginTime;
							String endTime = prog.endTime;
							DateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

							Calendar calNow = Calendar.getInstance();
							Calendar calBegin = Calendar.getInstance();
							Calendar calEnd = Calendar.getInstance();
							try {
									calBegin.setTime(df.parse(beginDate+beginTime));
									calEnd.setTime(df.parse(beginDate+endTime));
								
								if (calBegin.before(calNow) && calEnd.after(calNow)) {
									find = true;
									tvPlaybill.setText("正在播放："+prog.title);
									if (j < (liveChannelBill.lstProgBill.size()-1)) {
										ProgBill progNext = liveChannelBill.lstProgBill.get(j+1);
										tvPlaybillNext.setText("即将播放:"+progNext.title);
										break;
									}
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
				} else {

				}
			}
		}else{
			if(AllChannelProgBillFactory.mapLiveChannelBill != null){
				if (AllChannelProgBillFactory.mapLiveChannelBill.containsKey(mCurrentPlayChannelID)) {
					LiveChannelBill liveChannelBill = AllChannelProgBillFactory.mapLiveChannelBill.get(mCurrentPlayChannelID);
					if (liveChannelBill.ChannelID.equals(mCurrentPlayChannelID)) {
						for (int j = 0; j < liveChannelBill.lstProgBill.size(); j++) {
							ProgBill prog = liveChannelBill.lstProgBill.get(j);
							String beginDate = prog.beginDate;
							String beginTime = prog.beginTime;
							String endTime = prog.endTime;
							DateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

							Calendar calNow = Calendar.getInstance();
							Calendar calBegin = Calendar.getInstance();
							Calendar calEnd = Calendar.getInstance();
							try {
								calBegin.setTime(df.parse(beginDate+beginTime));
								calEnd.setTime(df.parse(beginDate+endTime));
								if (calBegin.before(calNow) && calEnd.after(calNow)) {
									find = true;
									tvPlaybill.setText("正在播放："+prog.title);
									if (j < (liveChannelBill.lstProgBill.size()-1)) {
										ProgBill progNext = liveChannelBill.lstProgBill.get(j+1);
										tvPlaybillNext.setText("即将播放:"+progNext.title);
										break;
									}
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
				} else {

				}
			}
		}

		if (!find) {
			tvPlaybill.setText("");
			tvPlaybillNext.setText("");
		}
	}

	// 处理频道号的显示
	private void displayUserChannelNumber(String text){
		boolean findChannel = false;
		if(userChannelNumber.getVisibility() != View.VISIBLE){

			userChannelNumberBg.setVisibility(View.VISIBLE);
			userChannelNumber.setVisibility(View.VISIBLE);
			userChannelNumber.setText(text);
			if(handlerUserChannelNumber != null) {
				handlerUserChannelNumber.removeCallbacks(runnablePlayChannel);
				handlerUserChannelNumber.postDelayed(runnablePlayChannel, 3000);
			}
		}else{
			//开始切换频道
			if(userChannelNumber.getText().length()<2){
				userChannelNumber.setText(userChannelNumber.getText()+text);
				if (handlerUserChannelNumber != null) {
					handlerUserChannelNumber.removeCallbacks(runnablePlayChannel);
					handlerUserChannelNumber.postDelayed(runnablePlayChannel, 3000);
				}
			}else{
				//切换频道
				userChannelNumber.setText(userChannelNumber.getText()+text);
				for (int i = 0; i < lstLiveChannel.size(); i++) {
					LiveChannel liveChannel = lstLiveChannel.get(i);
					if (liveChannel.UserChannelID == Integer.parseInt(userChannelNumber.getText().toString())) {
						currentPlayChannelIndex = i;
						findChannel = true;
						break;
					}
				}
				if(!findChannel){
					//channelResult.setVisibility(View.VISIBLE);
					if (handlerNoChannelInfo != null) {
						handlerNoChannelInfo.removeCallbacks(runnableDisplayNoChannel);
						handlerNoChannelInfo.postDelayed(runnableDisplayNoChannel, 1500);
					}
				}else{
					// 加一个500毫秒的延迟
					if (handlerUserChannelNumber != null) {
						handlerUserChannelNumber.removeCallbacks(runnablePlayChannel);
						handlerUserChannelNumber.postDelayed(runnablePlayChannel, 3000);
					}
				}
			}
		}
	}

	@Override
	public void notifyDataSetChanged() {
		if (handlerChannelList != null) {
			handlerChannelList.removeCallbacks(runnableChannelLeftList);
			handlerChannelList.postDelayed(runnableChannelLeftList, 4000);
		}
	}

	private void quiteBackgroundMusic() {
		if (BackgroundMusic.getInstance(this).isBackgroundMusicPlaying()) {
			BackgroundMusic.getInstance(this).stopBackgroundMusic();
		}
	}




	/*	private HttpEventHandler<VodOrLivePlayRecordReportResult> livePlayRecordEventHandler = new HttpEventHandler<VodOrLivePlayRecordReportResult>() {
		@Override
		public void HttpSucessHandler(VodOrLivePlayRecordReportResult result) {
			LogUtils.debug("提交结果 "+result.getResult()+" "+result.getReason());
		}

		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			TvUtils.processHttpFail(LiveChannelPlayActivity.this);
		}
	};*/

	// 延迟显示隐藏帮助信息
	Runnable runnableShowHelp = new Runnable() {
		@Override
		public void run() {
			showHelp();
			if(handlerShowHelp != null){

				if(handlerShowHelp.post(runnableShowHelp)){
					handlerShowHelp.removeCallbacks(runnableShowHelp);
				}

				if(handlerShowHelp.post(runnableHideHelp)){
					handlerShowHelp.removeCallbacks(runnableHideHelp);
				}
				handlerShowHelp.postDelayed(runnableHideHelp, 8000);
				LogUtils.error("5秒后隐藏帮助信息");
			}
		}
	};

	// 延迟显示隐藏帮助信息
	Runnable runnableHideHelp = new Runnable() {
		@Override
		public void run() {
			LogUtils.error("开始启动隐藏帮助信息。");
			fragmentTransaction = fragmentManager.beginTransaction();
			//fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);

			if(helpFragment.isAdded()){
				if(helpFragment.isVisible()){
					fragmentTransaction.hide(helpFragment);
				}
				fragmentTransaction.remove(helpFragment);
			}
			fragmentTransaction.commit();
			//LogUtils.error("隐藏帮助信息");
			handlerShowHelp.removeCallbacks(runnableHideHelp); 
		}
	};

	// 延迟显示隐藏帮助信息
	Runnable runnableHideAdvertisement = new Runnable() {
		@Override
		public void run() {
			LogUtils.error("开始启动隐藏帮助信息。");
			fragmentTransaction = fragmentManager.beginTransaction();
			//fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);

			if(mAdvertisementView.isAdded()){
				if(mAdvertisementView.isVisible()){
					fragmentTransaction.hide(mAdvertisementView);
				}
				fragmentTransaction.remove(mAdvertisementView);
			}
			fragmentTransaction.commit();
			//LogUtils.error("隐藏帮助信息");
			handlerHideAdvertisement.removeCallbacks(runnableHideAdvertisement); 

			if (player == null) {
				LogUtils.debug("Create MediaPlayer");
				createPlayer();

				int result = fillPlayerInfo();
				if(result == 0){
					LiveChannel channel = aaaLiveChannelsLocalFactory.findRecordZ(mCurrentPlayChannelID);
					record.setUserChannelId(channel.UserChannelID);
					record.setChannelName(channel.ChannelName);
					record.setEndPlayTime(new Date());
					record.setChannelId(channel.ChannelID);
					record.setPlatform(channel.platform);

					Intent intentAmqp = new Intent(ACTION_REPORT_LIVE_START_PLAY);
					Bundle bundle = new Bundle();
					bundle.putSerializable("record", record);
					intentAmqp.putExtras(bundle);
					sendBroadcast(intentAmqp); 

				}else{
					LogUtils.error("启动频道失败");
				}
				//LogUtils.debug("CREATE JOINCHANNEL");
			} else {
				LogUtils.debug("CREATE NOT NULL");
				//tclPlayer.joinChannel(mCurrentPlayChannelID);
			}

			LogUtils.error("LiveChannelPlayActivity 启动start");

			TvApplication.position ="LIVE_ACTIVITY";
			TvApplication.status ="PLAYING_LIVE";

			//LogUtils.error("定时启动，准备加载帮助Fragment");
			/*if(handlerShowHelp == null){
				handlerShowHelp = new Handler();
				handlerShowHelp.postDelayed(runnableShowHelp, 3000);
			}else{
				handlerShowHelp.postDelayed(runnableShowHelp, 3000);
			}*/
		}
	};


	/***************************
	 * 显示帮助信息
	 **************************/
	private void showHelp(){

		fragmentTransaction = fragmentManager.beginTransaction();
		//fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
		if(helpFragment != null){
			if(helpFragment.isAdded()){
				if(helpFragment.isVisible()){
					LogUtils.error("帮助已经显示，不动作");
				}else{
					LogUtils.error("帮助不可见，显示");
					fragmentTransaction.show(helpFragment).commit();;
				}
			}else{
				LogUtils.error("帮助未添加，现在添加");
				fragmentTransaction.add(R.id.iptv_live_play, helpFragment).commit();;
			}
		}else{
			LogUtils.error("帮助为空，添加");
			helpFragment = new LiveHelpFragment();
			fragmentTransaction.add(R.id.iptv_live_play, helpFragment).commit();;
		}

	}


	// 通过注册一个广播事件，来接收ListItemView中的点击事件
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			new Handler(LiveChannelPlayActivity.this.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					String action = intent.getAction();
					if (action.equals(ACTION_CHANGE_LIVE_CHANNEL)) {
						//						int nLiveChannelID = intent.getIntExtra(LIVE_CHANNEL_ID, -1);
						String nLiveChannelID = intent.getStringExtra(LIVE_CHANNEL_ID);
						//						if (mCurrentPlayChannelID != nLiveChannelID) {
						if (!mCurrentPlayChannelID.equals(nLiveChannelID)) {
							stopCurPlayer();
							LiveChannel channel = (LiveChannel) player.getLiveChannelMap().get(nLiveChannelID);
							//							player.joinChannel(nLiveChannelID);	
							player.joinChannel(nLiveChannelID);	

							Animation left_out_animation = AnimationUtils.loadAnimation(LiveChannelPlayActivity.this, R.anim.left_out);
							if (handlerChannelList != null) {
								handlerChannelList.removeCallbacks(runnableChannelLeftList);
							}
							mLiveChoose.startAnimation(left_out_animation);//使用view的startAnimation方法开始执行动画   
							mLiveChoose.setVisibility(View.INVISIBLE);

							// 为加减频道准备
							for (int i = 0; i < lstLiveChannel.size(); i++) {
								LiveChannel liveChannel = lstLiveChannel.get(i);
								if (liveChannel.ChannelID.equals(nLiveChannelID)) {
									currentPlayChannelIndex = i;
									break;
								}
							}

							mCurrentPlayChannelID = nLiveChannelID;


							playbill_main.setVisibility(View.VISIBLE);
							liveChannelName.setText(channel.ChannelName);
							tv_playbill_channel_name.setText(channel.ChannelName);
							tv_playbill_channel_num.setText(Integer.toString(channel.UserChannelID));
							createProgBill();
							if (handlerProgBill != null) {
								handlerProgBill.removeCallbacks(runnableChannelInfo);
								handlerProgBill.postDelayed(runnableChannelInfo, 3000);
							}

							record.setStartPlayTime(new Date());
							record.setChannelId(channel.ChannelID);
							record.setUserChannelId(channel.UserChannelID);
							record.setChannelName(channel.ChannelName);
							record.setEndPlayTime(null);
							record.setPlatform(channel.platform);

							Intent intentAmqp = new Intent(ACTION_REPORT_LIVE_START_PLAY);
							Bundle bundle = new Bundle();
							bundle.putSerializable("record", record);
							intentAmqp.putExtras(bundle);
							sendBroadcast(intentAmqp); 

						}
					}
				}
			});
		}
	};
}
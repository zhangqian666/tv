package com.iptv.rocky.tcl;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.gson.Gson;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.data.OrderResult;
import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.data.VodPlayRecord;
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
import com.iptv.rocky.model.ActivityStack;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.tcl.vodplay.OrderStatusFragment;
import com.iptv.rocky.tcl.vodplay.OrderStatusFragment.StartOrderPlayedVodListener;
import com.iptv.rocky.tcl.vodplay.OrderSuccessFragment;
import com.iptv.rocky.tcl.vodplay.OrderSuccessFragment.StartPlayOrderedVodListener;
import com.iptv.rocky.tcl.vodplay.VodPayByWeChatFragment;
import com.iptv.rocky.tcl.vodplay.VodPlayChoosePackageFragment;
import com.iptv.rocky.tcl.vodplay.VodPlayChoosePackageFragment.StartPayOrderedPackageListener;
import com.iptv.rocky.tcl.vodplay.VodPlayChoosePayMethodFragment;
import com.iptv.rocky.tcl.vodplay.VodPlayChoosePayMethodFragment.ChoosePlayPositionListener;
import com.iptv.rocky.utils.AESUtil;
import com.iptv.rocky.utils.BackgroundMusic;
import com.iptv.rocky.utils.QRCodeUtil;
import com.iptv.rocky.view.splash.AlertDialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("SimpleDateFormat")
public class VodChannelPlayActivity extends BaseActivity 
implements OnBufferingUpdateListener, 
OnCompletionListener, 
OnPreparedListener, 
OnVideoSizeChangedListener, 
OnRtspStatusStartOfStreamListener, 
OnRtspStatusEndOfStreamListener, 
OnErrorListener, 
StartOrderPlayedVodListener,
ChoosePlayPositionListener,
StartPlayOrderedVodListener,
StartPayOrderedPackageListener {

	private MediaPlayer tclPlayer;
	private RelativeLayout exitConfirmDialog;
	private RelativeLayout controlbar;
	private ProgressBar progressBar;

	private ImageView playStatusIcon;
	private Button btnExitPlay;
	private Button btnContinuePlay;
	private TextView vodPlayVodName;
	private TextView txtSpeedDisplay;
	private TextView vodPlayCurrentTime;
	private TextView vodPlayTotalTime;
	private LinearLayout vodPlayExitThanks;

	private LinearLayout vodPlayErrorExitApk;
	private TextView vodPlayErrorInfo;

	private String status;
	private static final String ACTION_REPORT_VOD_PLAY_RECORD ="com.virgintelecom.iptv.VOD.PLAYRECORD";
	private static final String ACTION_REPORT_VOD_START_PLAY ="com.virgintelecom.iptv.VOD.START.PLAY";
	/**
	 * 微信扫码支付完成 ACTION标识
	 */
	private static final String ACTION_PUBLISHSERVER_WECHAT_PAY_SUCCESS = "com.virgintelecom.iptv.RABBITMQ.PUBLISH.SERVER.WECHAT.PAY.SUCCESS";

	private static String TAG = VodChannelPlayActivity.class.getSimpleName();

	private Float speed = (float) 0;
	private VodDetailInfo vodDetailInfo = null;
	private VodChannel vodChannel = null;
	private String RTSPURL;

	//订购状态
	private boolean orderStatus;
	private int playLength= 300; 
	private boolean finishedPrePlay; 
	private ProgressDialog dialog;
	private OkHttpClient client;
	private String payStatus;

	private String subVideoChannelID;

	// 电视剧信息；
	private int isSitcom = 0;
	private List<Integer> sitcomNumberList = new ArrayList<Integer>();

	Handler handler = new Handler();

	// 处理已经播放的日期
//	private Handler handlerDisplayTimePlayed = new Handler();

	@SuppressWarnings("unused")
	private boolean toExit = false;

	private VodHistoryLocalFactory vodHistoryLocalFactory = new VodHistoryLocalFactory(VodChannelPlayActivity.this);
	private HistoryChannelInfo historyChannelInfo;
	private Handler notPayFiveMinuteHandler = new Handler();

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private OrderStatusFragment orderStatusFragment;
	private VodPlayChoosePayMethodFragment vodPlayChoosePayMethodFragment;
	private VodPlayChoosePackageFragment vodPlayChoosePackageFragment;
	private OrderSuccessFragment orderSuccessFragment;
	private VodPlayRecord vodPlayRecord;
	private AlertDialogFragment alertDialogFragment;
	private VodPayByWeChatFragment vodPayByWeChatFragment;

	/** Called when the activity is first created. */	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		quiteBackgroundMusic();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tcl_activity_vod_channel);

		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		controlbar = (RelativeLayout) findViewById(R.id.vod_progress_controlbar);
		txtSpeedDisplay = (TextView) findViewById(R.id.vod_play_speed);
		exitConfirmDialog = (RelativeLayout) findViewById(R.id.vod_exit_confirm_dialog);
		btnExitPlay = (Button) findViewById(R.id.btn_vod_exit_play);
		btnContinuePlay = (Button) findViewById(R.id.btn_vod_continue_play);
		progressBar = (ProgressBar) findViewById(R.id.vod_play_progressBar);
		playStatusIcon = (ImageView) findViewById(R.id.vod_play_status_icon);
		vodPlayVodName = (TextView)findViewById(R.id.vod_play_vod_name);
		vodPlayCurrentTime = (TextView) findViewById(R.id.vod_play_current_time);
		vodPlayTotalTime = (TextView) findViewById(R.id.vod_play_total_time);
		vodPlayExitThanks = (LinearLayout) findViewById(R.id.vod_play_exit_thanks);
		vodPlayErrorExitApk = (LinearLayout) findViewById(R.id.vod_play_error_exit_apk);
		vodPlayErrorInfo = (TextView)findViewById(R.id.vod_play_error_info);

		btnExitPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Save played record;
				stopPlay();
				toExit = true;
				onBackPressed();
			}
		});

		// Back to resume playing;
		btnContinuePlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "继续播放，当前状态:" + tclPlayer.getPlayStatus());
				if (tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
					tclPlayer.resume();
					exitConfirmDialog.setVisibility(View.INVISIBLE);
				} else {
				}
			}
		});

		LogUtils.error("VODChannelPlayActivity 启动create");

		vodPlayRecord = new VodPlayRecord();
		status = "PLAY";
		client = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.build();

		installListeners();

	}

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

	@SuppressWarnings("unused")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onkeydown keycode = " + keyCode);
		boolean todo = false;

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(status.equals("CHOOSE_PACKAGE")){
				fragmentManager = getFragmentManager();
				fragmentTransaction = fragmentManager.beginTransaction();
				if(vodPlayChoosePackageFragment.isVisible()){
					fragmentTransaction.hide(vodPlayChoosePackageFragment);
					if(orderStatusFragment.isAdded()){
						fragmentTransaction.show(orderStatusFragment);
					}
					fragmentTransaction.commit();
				}

				if(tclPlayer.getCurrentPlayTime()< playLength){
					tclPlayer.resume();
					status = "PLAY";
					//if(notPayFiveMinuteHandler.hasCallbacks(fiveMinateRunnable)){
						notPayFiveMinuteHandler.removeCallbacks(fiveMinateRunnable);
						notPayFiveMinuteHandler.postDelayed(fiveMinateRunnable, 1000);
					//}else{
					//	notPayFiveMinuteHandler.postDelayed(fiveMinateRunnable, 1000);
					//}

					return false;
				}else{
					return super.onKeyDown(keyCode, event);
				}
			}else if(status.equals("PAY")){if (payStatus.equals("PassWord")) {

				boolean result =vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
				if(result){
					fragmentManager = getFragmentManager();
					fragmentTransaction = fragmentManager.beginTransaction();

					if(vodPlayChoosePackageFragment.isAdded()){
						fragmentTransaction.remove(vodPlayChoosePayMethodFragment);
						fragmentTransaction.show(vodPlayChoosePackageFragment).commit();
					}else{
						fragmentTransaction.remove(vodPlayChoosePayMethodFragment );
						fragmentTransaction.add(R.id.iptv_vod_play,vodPlayChoosePackageFragment).commit();
					}
					status="CHOOSE_PACKAGE";
					return false;
				}else{
					return false;
				}
			}else if(payStatus.equals("WeChat")){

				boolean result =vodPayByWeChatFragment.onKeyDown(keyCode, event);
				if(result){
					fragmentManager = getFragmentManager();
					fragmentTransaction = fragmentManager.beginTransaction();

					if(vodPlayChoosePackageFragment.isAdded()){
						fragmentTransaction.remove(vodPayByWeChatFragment);
						fragmentTransaction.show(vodPlayChoosePackageFragment).commit();
					}else{
						fragmentTransaction.remove(vodPayByWeChatFragment );
						fragmentTransaction.add(R.id.iptv_vod_play,vodPlayChoosePackageFragment).commit();
					}
					status="CHOOSE_PACKAGE";
					return false;
				}else{
					return false;
				}

			}
			}
			else if(status.equals("CHOOSE_PLAY_POSITION"))
			{
				fragmentManager = getFragmentManager();
				fragmentTransaction = fragmentManager.beginTransaction();

				if(orderSuccessFragment.isAdded()){
					if(orderSuccessFragment.isVisible()){
						fragmentTransaction.remove(orderSuccessFragment).commit();
						tclPlayer.resume();
						status = "PLAY";
					}
				}
				return false;
			}
			else if(status.equals("PLAY"))
			{
				if (tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
					tclPlayer.resume();
					playStatusIcon.setImageResource(R.drawable.play_icon);
					controlbar.setVisibility(View.INVISIBLE);
					handler.removeCallbacks(runnable);
					return false;
				}
				else if(tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
						|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND))
				{
					tclPlayer.normalrate();
					playStatusIcon.setImageResource(R.drawable.play_icon);
					controlbar.setVisibility(View.INVISIBLE);
					handler.removeCallbacks(runnable);
					return false;
				}	
				else if (tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY))
				{
					handler.removeCallbacks(runnable);
					historyChannelInfo = new HistoryChannelInfo();
					if (vodDetailInfo != null) {
						historyChannelInfo.channelid = vodDetailInfo.VODID;
						historyChannelInfo.VODNAME = vodDetailInfo.VODNAME;
						historyChannelInfo.PICPATH = vodDetailInfo.PICPATH;
						historyChannelInfo.platform = vodDetailInfo.platform;
					} else if (vodChannel != null) {
						historyChannelInfo.channelid = vodChannel.VODID;
						historyChannelInfo.VODNAME = vodChannel.VODNAME;
						historyChannelInfo.PICPATH = vodChannel.PICPATH;
						historyChannelInfo.platform = vodChannel.platform;
					}
					historyChannelInfo.VODID = subVideoChannelID;
					//					historyChannelInfo.playposition = tclPlayer.getCurrentPlayTime()*1000; 
					historyChannelInfo.playposition = tclPlayer.getCurrentPlayTime(); 
					Log.d("VodChannelPlayActivity","当前播放时间:"+tclPlayer.getCurrentPlayTime());
					vodHistoryLocalFactory.saveHistory(historyChannelInfo);
					Log.d("VodChannelPlayActivity", "保存历史记录");

					vodPlayRecord.setEndPosition((long)tclPlayer.getCurrentPlayTime());
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
					sendBroadcast(intent); 



					tclPlayer.stop();
					LogUtils.debug("准备调取RESET");
					tclPlayer.reset();
					Log.d("VodChannelPlayActivity", "停止播放，添加上报数据到服务器信息");

					return super.onKeyDown(keyCode, event);
				}
			}
			break;
		case KeyEvent.KEYCODE_HOME:

			//if(notPayFiveMinuteHandler.hasCallbacks(fiveMinateRunnable)){
				notPayFiveMinuteHandler.removeCallbacks(fiveMinateRunnable);
			//}
			LogUtils.debug("HOME 键");
			tclPlayer.stop();
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
		case KeyEvent.KEYCODE_0:
			if(status.equals("PAY")){
				vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_1:
			if(status.equals("PAY")){
				vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_2:
			if(status.equals("PAY")){
				vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_3:
			if(status.equals("PAY")){
				vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_4:
			if(status.equals("PAY")){
				vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_5:
			if(status.equals("PAY")){
				vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_6:
			if(status.equals("PAY")){
				vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_7:
			if(status.equals("PAY")){
				vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_8:
			if(status.equals("PAY")){
				vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
			}
			break;	
		case KeyEvent.KEYCODE_9:
			if(status.equals("PAY")){
				vodPlayChoosePayMethodFragment.onKeyDown(keyCode, event);
			}
			break;			
		case KeyEvent.KEYCODE_DPAD_UP:
			if(status.equals("PLAY")){
				tclPlayer.gotoStart();
			}
			break;

		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(status.equals("PLAY")){
				tclPlayer.gotoEnd();
			}
			break;

		case KeyEvent.KEYCODE_DPAD_LEFT:

			if(orderStatus && status.equals("PLAY")){
				if (exitConfirmDialog.getVisibility() == View.GONE 
						|| exitConfirmDialog.getVisibility() == View.INVISIBLE)
				{
					if (!tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
						speed = (float) 2.0;
					} else {
						if (speed == 32) {
							speed = (float) 2.0;
						} else {
							speed = speed*2;
						}
					}
					tclPlayer.fastRewind(speed);
					playStatusIcon.setImageResource(R.drawable.rewind_icon);
					controlbar.setVisibility(View.VISIBLE);

					if(handler != null){
						handler.removeCallbacks(runnable);
						handler.postDelayed(runnable, 500);
					}
					//handler.postDelayed(runnable, 500);
					txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(speed)));
				}
			}else{
				if(finishedPrePlay){

				}else{

				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			LogUtils.debug("状态："+status);
			if(orderStatus && status.equals("PLAY")){
				if (exitConfirmDialog.getVisibility() == View.GONE || exitConfirmDialog.getVisibility() == View.INVISIBLE) {
					if (!tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD)) {
						speed = (float) 2.0;
					} else {
						if (speed == 32) {
							speed = (float) 2.0;
						} else {
							speed = speed*2;
						}
					}
					LogUtils.debug("开始发起快进");
					tclPlayer.fastForward(speed);
					playStatusIcon.setImageResource(R.drawable.speed_icon);
					controlbar.setVisibility(View.VISIBLE);
					if (handler != null) {
						handler.removeCallbacks(runnable);
						handler.postDelayed(runnable, 500);
					}
					txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(speed)));
				}
			}else{

			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if(!orderStatus){
				if(orderStatusFragment.isVisible()){
					startOrder(1);
				}
			}else{
				if (exitConfirmDialog.getVisibility() == View.VISIBLE) {

				} else if(controlbar.getVisibility() == View.VISIBLE) {
					if (tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
						LogUtils.info("(tclPlayer.getPlayStatus()------->>>>"+tclPlayer.getPlayStatus());
						if (handler != null) {
							handler.removeCallbacks(runnable);
							handler.postDelayed(runnable, 500);
						}
						tclPlayer.resume();
						controlbar.setVisibility(View.INVISIBLE);
					} else if( tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
							|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {

						tclPlayer.normalrate();
						controlbar.setVisibility(View.INVISIBLE);
						handler.removeCallbacks(runnable);
					}
				} else if (vodPlayExitThanks.getVisibility() == View.VISIBLE) {
					//stopPlay();
					finish();
					return false;
				} else {	
					LogUtils.info("播放状态:"+tclPlayer.getPlayStatus());
					if (tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY)) {
						tclPlayer.pause();
						playStatusIcon.setImageResource(R.drawable.pause_iocn);
						if (handler != null) {
							handler.removeCallbacks(runnable);
							handler.postDelayed(runnable, 500);
						}
						txtSpeedDisplay.setText("X 0");
						controlbar.setVisibility(View.VISIBLE);
					} else if (tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
							|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
						tclPlayer.normalrate();
						controlbar.setVisibility(View.INVISIBLE);
						txtSpeedDisplay.setText("X 1");
					} else if (tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
						tclPlayer.resume();
						if (handler != null) {
							handler.removeCallbacks(runnable);
							handler.postDelayed(runnable, 500);
						}
						exitConfirmDialog.setVisibility(View.INVISIBLE);
					}
				}
			}
			break;
		case KeyEvent.KEYCODE_PAGE_UP:
			if(status.equals("PLAY")){
				tclPlayer.gotoStart();
			}
			break;
		case KeyEvent.KEYCODE_PAGE_DOWN:
			if(status.equals("PLAY")){
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
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:

			if(orderStatus){
				LogUtils.debug("暂停播放键 播放状态:"+tclPlayer.getPlayStatus());
				if(tclPlayer.getPlayStatus() == PlayStatus.NORMAL_PLAY){
					tclPlayer.pause();
					playStatusIcon.setImageResource(R.drawable.pause_iocn);
					txtSpeedDisplay.setText("X 0");
					controlbar.setVisibility(View.VISIBLE);
				}else if(tclPlayer.getPlayStatus() == PlayStatus.PAUSE){
					tclPlayer.resume();
					controlbar.setVisibility(View.INVISIBLE);
				}else if(tclPlayer.getPlayStatus() == PlayStatus.FASTFORWORD || tclPlayer.getPlayStatus() == PlayStatus.FASTREWIND){
					tclPlayer.normalrate();
					controlbar.setVisibility(View.INVISIBLE);
				}
			}else{

			}
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
	}

	@Override
	public void onStart(){
		super.onStart();
		quiteBackgroundMusic();
		Intent intent = getIntent();
		LogUtils.error("intent是否为空?"+ (intent ==null));
		if (intent != null)
		{
			Object o = (Object) intent.getSerializableExtra(IPTVUriUtils.EXTRA_PARAMS);

			if (o instanceof VodDetailInfo) {
				LogUtils.error("内容类型为 vodDetailInfo");
				vodDetailInfo = (VodDetailInfo) o;
				LogUtils.info("购买状态:"+vodDetailInfo.ordered + "; ELAPSETIME:"+ vodDetailInfo.ELAPSETIME);
				/*if(vodDetailInfo.ELAPSETIME >5 ) {
					orderStatus = intent.getBooleanExtra(IPTVUriUtils.ORDER_STATUS, false);
					vodPlayRecord.setOrdered(orderStatus);
				}else{
					vodPlayRecord.setOrdered(true);
				}*/
				orderStatus= intent.getBooleanExtra(IPTVUriUtils.ORDER_STATUS, false);
				//				orderStatus = vodDetailInfo.ordered;
				//				orderStatus = true;
				vodPlayRecord.setOrdered(vodDetailInfo.ordered);

				isSitcom = vodDetailInfo.ISSITCOM;
				LogUtils.error("isSitcom:"+isSitcom);
			}
			else if (o instanceof VodChannel) {
				LogUtils.error("内容类型为 vodChannel");
				vodChannel = (VodChannel) o;
			}

			RTSPURL = intent.getStringExtra(IPTVUriUtils.PLAY_URL_PARAMS);

			if (TextUtils.isEmpty(RTSPURL))
			{
				onBackPressed();
				//finish();
				return;
			}
		}
		// Log.d(TAG, "onStart 播放器是否为空:"+ (tclPlayer == null));
		if (tclPlayer == null) {
			tclPlayer = MediaPlayer.getInstance(this);
			tclPlayer.setKeepLastFrame(0);
			//			tclPlayer.setVideoDisplayArea(0, 0, 800, 600);
			tclPlayer.setOnBufferingUpdateListener(VodChannelPlayActivity.this);
			tclPlayer.setOnCompletionListener(VodChannelPlayActivity.this);
			tclPlayer.setOnPreparedListener(VodChannelPlayActivity.this);
			tclPlayer.setOnVideoSizeChangedListener(VodChannelPlayActivity.this);
			tclPlayer.setOnRtspStatusEndOfStreamListener(VodChannelPlayActivity.this);
			tclPlayer.setOnRtspStatusStartOfStreamListener(VodChannelPlayActivity.this);
			tclPlayer.setOnErrorListener(VodChannelPlayActivity.this);
			tclPlayer.setSingleMedia(RTSPURL);
			if (isSitcom == 1) {
				sitcomNumberList = vodDetailInfo.SUBVODNUMLIST;
				List<VodChannel> subVodIdList = vodDetailInfo.SUBVODIDLIST;
				subVideoChannelID = intent.getStringExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS);
				for (int i=0;i<subVodIdList.size();i++) {
					VodChannel vodChannel = subVodIdList.get(i);
					if (vodChannel.VODID == subVideoChannelID) {
						vodPlayVodName.setText(vodDetailInfo.VODNAME+"   第"+sitcomNumberList.get(i)+"集");
					}
				}
			} else {
				if (vodDetailInfo != null) {
					vodPlayVodName.setText(vodDetailInfo.VODNAME);
					subVideoChannelID = vodDetailInfo.VODID;
				} else if(vodChannel != null) {
					vodPlayVodName.setText(vodChannel.VODNAME);
				}
			}

			progressBar = (ProgressBar) findViewById(R.id.vod_play_progressBar);

			// 查看记录是否在
			VodHistoryLocalFactory vodHistoryLocalFactory = new VodHistoryLocalFactory(this);
			HistoryChannelInfo historyChannelInfo = vodHistoryLocalFactory.getHistoryById(subVideoChannelID);
			//Log.d(TAG, (null == historyChannelInfo)+";subVideoChannelID:"+subVideoChannelID);
			LogUtils.info("历史播放记录是否为空"+(historyChannelInfo == null));
			if (historyChannelInfo != null)
			{
				// 执行seek拖动操作,暂时从头播放；
				//				Log.d("播放记录","上次播放位置"+ (historyChannelInfo.playposition/1000)+"");
				Log.d("播放记录","上次播放位置"+ (historyChannelInfo.playposition)+"");
				tclPlayer.setPlayType(PlayType.VOD);

				//				vodPlayRecord.setBeginPosition( (historyChannelInfo.playposition/1000));
				vodPlayRecord.setBeginPosition( (historyChannelInfo.playposition));
				vodPlayRecord.setBeginPlayDateTime(new Date());
				//				tclPlayer.playByTime("", (int) (historyChannelInfo.playposition/1000), 2);
				tclPlayer.playByTime("", (int) (historyChannelInfo.playposition), 2);

			} else {
				tclPlayer.setPlayType(PlayType.VOD);
				vodPlayRecord.setBeginPosition((long) 0);
				vodPlayRecord.setBeginPlayDateTime(new Date());
				tclPlayer.playFromStart();
				LogUtils.debug("开始播放");
				//添加未购买用户播放进度检查
			}

			reportStartPlay(); //上报信息

			//检查购买状态
			LogUtils.error("购买状态:"+orderStatus);
			if(!orderStatus){
				//显示提醒的fragment。

				vodPlayChoosePayMethodFragment = new VodPlayChoosePayMethodFragment();
				vodPayByWeChatFragment=new VodPayByWeChatFragment();
				notPayFiveMinuteHandler.post(fiveMinateRunnable);
				fragmentManager = getFragmentManager();
				fragmentTransaction = fragmentManager.beginTransaction();
				orderStatusFragment = new OrderStatusFragment();
				fragmentTransaction.add(R.id.iptv_vod_play, orderStatusFragment);
				fragmentTransaction.commit();
			}else{
				LogUtils.info("已经购买，可以一直播放");
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		Intent intent = getIntent();
		if (intent != null)
		{
			Object o = (Object) intent.getSerializableExtra(IPTVUriUtils.EXTRA_PARAMS);
			if (o instanceof VodDetailInfo) {
				vodDetailInfo = (VodDetailInfo) o;
				isSitcom = vodDetailInfo.ISSITCOM;
			}
			else if (o instanceof VodChannel) {
				vodChannel = (VodChannel) o;
			}

			RTSPURL = intent.getStringExtra(IPTVUriUtils.PLAY_URL_PARAMS);
			//RTSPURL = "rtsp://10.42.12.15:8554/";
			if (TextUtils.isEmpty(RTSPURL))
			{
				onBackPressed();
				//finish();
				return;
			}

			subVideoChannelID = intent.getStringExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS); 
		}

		if (tclPlayer == null) {
			Log.d(TAG, "Resume");
			tclPlayer= MediaPlayer.getInstance(this);
			//			tclPlayer.setVideoDisplayArea(0, 0, 800, 600);
			tclPlayer.setOnBufferingUpdateListener(VodChannelPlayActivity.this);
			tclPlayer.setOnCompletionListener(VodChannelPlayActivity.this);
			tclPlayer.setOnPreparedListener(VodChannelPlayActivity.this);
			tclPlayer.setOnVideoSizeChangedListener(VodChannelPlayActivity.this);
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
			} else {
				//System.out.println("RESUME PLAY");
				tclPlayer.playFromStart();
			}
		}
	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop");

		//if(notPayFiveMinuteHandler.hasCallbacks(fiveMinateRunnable)){
		//	notPayFiveMinuteHandler.removeCallbacks(fiveMinateRunnable);
		//}


		BackgroundMusic.getInstance(this).playBackgroundMusic("music/background_ex.mp3", true);
		LogUtils.debug("STOP:准备调取RESET");
		//		tclPlayer.reset();
		//		tclPlayer.release();

		deletePlayer();
		releaseMediaPlayer();
		super.onStop();
	}

	/**
	 * 汇报服务器，开始播放点播
	 */
	private void reportStartPlay(){

		vodPlayRecord.setProgramId(vodDetailInfo.VODID);
		vodPlayRecord.setProgramName(vodDetailInfo.VODNAME);
		vodPlayRecord.setBillingType(TvApplication.billingType);
		vodPlayRecord.setLength(vodDetailInfo.ELAPSETIME);
		LogUtils.error("开始发送开始播放点播信息");
		Intent intent = new Intent(ACTION_REPORT_VOD_START_PLAY);
		Bundle bundle = new Bundle();
		bundle.putSerializable("record", vodPlayRecord);
		intent.putExtras(bundle);
		sendBroadcast(intent); 
		TvApplication.status ="PLAYING_VOD";
		TvApplication.position = "VOD_ACTIVITY";
	}

	/*	@Override
	public void onDestroy() {
		LogUtils.debug("Destroy Activity");
		super.onDestroy();
	}*/

	/*	private void uninstallListeners() {
		unregisterReceiver(mBroadcastReceiver);
	}*/

	//	@Override
	//	public void onPrepared(MediaPlayer mp) {
	//		Log.d(TAG, "点播播放页面：收到准备好信息");
	//		status = 1;
	//		
	//		// 查看记录是否在
	//		VodHistoryLocalFactory vodHistoryLocalFactory = new VodHistoryLocalFactory(this);
	//		HistoryChannelInfo historyChannelInfo = vodHistoryLocalFactory.getHistoryById(subVideoChannelID);
	//		if (historyChannelInfo != null)
	//		{
	//			// 执行seek拖动操作
	//		}
	//	}
	//
	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d(TAG, "点播播放页面：收到播放完毕信息");
		Log.d(TAG, "onRtspStatusEndOfStream");
		controlbar.setVisibility(View.INVISIBLE);
		if (handler != null) {
			handler.removeCallbacks(runnable);
		}

		LogUtils.debug("播放完毕，添加一个上报");
		vodPlayRecord.setEndPosition((long)tclPlayer.getCurrentPlayTime());
		vodPlayRecord.setEndPlayDateTime(new Date());
		vodPlayRecord.setProgramName(vodDetailInfo.VODNAME);
		vodPlayRecord.setBillingType(TvApplication.billingType);
		vodPlayRecord.setLength(vodDetailInfo.ELAPSETIME);

		Intent intent = new Intent(ACTION_REPORT_VOD_PLAY_RECORD);
		Bundle bundle = new Bundle();
		bundle.putSerializable("record", vodPlayRecord);
		intent.putExtras(bundle);
		sendBroadcast(intent); 

		onBackPressed();
	}

	@Override
	public void onRtspStatusEndOfStream(MediaPlayer mp) {
		Log.d(TAG, "onRtspStatusEndOfStream");
		controlbar.setVisibility(View.INVISIBLE);
		if (handler != null) {
			handler.removeCallbacks(runnable);
		}
		//vodPlayExitThanks.setVisibility(View.VISIBLE);



		onBackPressed();
	}

	@Override
	public void onRtspStatusStartOfStream(MediaPlayer mp) {
		Log.d(TAG, "onRtspStatusStartOfStream");
		progressBar.setProgress(0);
		controlbar.setVisibility(View.INVISIBLE);
		if (handler != null) {
			handler.removeCallbacks(runnable);
		}
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
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			//要做的事情
			setProgressInfo();
			handler.postDelayed(runnable, 1000);
		}
	};



	// 创建要显示的时间
	@SuppressWarnings({ "unused", "deprecation" })
	private String createTimeString(int second) {

		// 秒
		// UT送的是毫秒

		if(TvApplication.mChannel.equals("UTSTAR")){
			//			second = second /1000;
			LogUtils.info("mChannel----->"+TvApplication.mChannel);
		}else{

		}
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

	// 创建要显示的时间
	@SuppressWarnings({ "unused", "deprecation" })
	private String createVodTimeString(int second) {
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
	private void setProgressInfo() {
		if (tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
				|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)||tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
			Log.d("总时长:",tclPlayer.getMediaDuration()+"");
			vodPlayTotalTime.setText(createTimeString(tclPlayer.getMediaDuration()));
			int currentTime = tclPlayer.getCurrentPlayTime();

			LogUtils.debug("当前播放进度："+tclPlayer.getCurrentPlayTime());

			Log.d("设置进度条","提取的当前时间:"+currentTime + "; 当前倍速："+txtSpeedDisplay.getText());
			progressBar.setMax(tclPlayer.getMediaDuration());
			progressBar.setProgress(currentTime);
			vodPlayCurrentTime.setText(createTimeString(currentTime));
		}
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.d(TAG, "onVideoSizeChanged");
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "onPrepared");
	}

	/*	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d(TAG, "onCompletion");
	}*/

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		Log.d(TAG, "onBufferingUpdate");
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.d(TAG, "onError");
		if (what == MediaPlayer.MEDIA_DATA_FAILED_TODECODER) {
			vodPlayErrorExitApk.setVisibility(View.VISIBLE);
		}else if (what == MediaPlayer.MEDIA_PREPARE_FAILED){
			vodPlayErrorInfo.setText("无法播放");
			vodPlayErrorExitApk.setVisibility(View.VISIBLE);
		}
		return false;
	}

	private void stopPlay() {
		Log.d(TAG, "stopPlay");
		tclPlayer.stop();
		//releaseMediaPlayer();
	}

	// 定义控制未购买的播放的
	Runnable fiveMinateRunnable = new Runnable() {
		@Override
		public void run() {
			//要做的事情
			if(tclPlayer.getPlayStatus() != null){
				if (tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) || tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {

					int currentTime = tclPlayer.getCurrentPlayTime();
					if(currentTime >= playLength){
						LogUtils.debug("时间超过300了，提示用户付款");
						tclPlayer.pause();
						finishedPrePlay =true;
						reportVodPlayRecord();

						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

						vodPlayChoosePackageFragment = new VodPlayChoosePackageFragment();
						fragmentTransaction.hide(orderStatusFragment);

						Bundle bundle = new Bundle();
						bundle.putString("VODID", vodDetailInfo.VODID);
						bundle.putSerializable("VOD", vodDetailInfo);
						vodPlayChoosePackageFragment.setArguments(bundle);

						fragmentTransaction.add(R.id.iptv_vod_play, vodPlayChoosePackageFragment);
						fragmentTransaction.commit();
						status="CHOOSE_PACKAGE";

					}else{
						LogUtils.debug("时间尚未到6分钟，可以继续观看。");
						notPayFiveMinuteHandler.removeCallbacks(fiveMinateRunnable);
						notPayFiveMinuteHandler.postDelayed(fiveMinateRunnable, 500);
					}
				}else if(tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY)){
					//LogUtils.info("正常播放状态");

					int currentTime = tclPlayer.getCurrentPlayTime();
					if(currentTime >= playLength){
						LogUtils.debug("时间超过300了，提示用户付款");


						finishedPrePlay =true;
						reportVodPlayRecord();
						tclPlayer.pause();

						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						vodPlayChoosePackageFragment = new VodPlayChoosePackageFragment();
						fragmentTransaction.hide(orderStatusFragment);

						Bundle bundle = new Bundle();
						bundle.putString("VODID", vodDetailInfo.VODID);

						//bundle.putDouble("PRICE", vodDetailInfo.price);
						bundle.putSerializable("VOD", vodDetailInfo);
						LogUtils.error("要传递的参数:voddetailInfo 是否为空？"+(vodDetailInfo == null));
						vodPlayChoosePackageFragment.setArguments(bundle);
						fragmentTransaction.add(R.id.iptv_vod_play, vodPlayChoosePackageFragment);
						fragmentTransaction.commit();
						status="CHOOSE_PACKAGE";

					}else{
						//LogUtils.debug("时间尚未到6分钟，可以继续观看。");
						orderStatusFragment.setTimePlayed(tclPlayer.getCurrentPlayTime());
						notPayFiveMinuteHandler.removeCallbacks(fiveMinateRunnable);
						notPayFiveMinuteHandler.postDelayed(fiveMinateRunnable, 500);
					}
				}
			}
		}
	};


	private void reportVodPlayRecord(){
		vodPlayRecord.setEndPosition((long)tclPlayer.getCurrentPlayTime());
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
		sendBroadcast(intent); 

	}

	private void quiteBackgroundMusic() {
		if (BackgroundMusic.getInstance(this).isBackgroundMusicPlaying()) {
			BackgroundMusic.getInstance(this).stopBackgroundMusic();
		}
	}

	/**
	 * 开始支付，先显示选择的购买方式：包天或包片，后期加微信、支付宝在线支付。
	 */
	@Override
	public void startOrder(int index) {
		LogUtils.error("startOrder,开始启动付款选择项");
		reportVodPlayRecord();

		//暂停
		tclPlayer.pause();


		//保存播放记录开始
		historyChannelInfo = new HistoryChannelInfo();
		if (vodDetailInfo != null) {
			historyChannelInfo.channelid = vodDetailInfo.VODID;
			historyChannelInfo.VODNAME = vodDetailInfo.VODNAME;
			historyChannelInfo.PICPATH = vodDetailInfo.PICPATH;
			historyChannelInfo.platform = vodDetailInfo.platform;
		} else if (vodChannel != null) {
			historyChannelInfo.channelid = vodChannel.VODID;
			historyChannelInfo.VODNAME = vodChannel.VODNAME;
			historyChannelInfo.PICPATH = vodChannel.PICPATH;
			historyChannelInfo.platform = vodChannel.platform;
		}
		historyChannelInfo.VODID = subVideoChannelID;
		//		historyChannelInfo.playposition = tclPlayer.getCurrentPlayTime()*1000; 
		historyChannelInfo.playposition = tclPlayer.getCurrentPlayTime(); 
		LogUtils.info("当前播放时间:"+tclPlayer.getCurrentPlayTime());
		vodHistoryLocalFactory.saveHistory(historyChannelInfo);
		// 保存播放记录结束


		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		vodPlayChoosePackageFragment = new VodPlayChoosePackageFragment();

		//vodPlayOrderByPasswordFragment = new VodPlayOrderByPasswordFragment();

		Bundle bundle = new Bundle();
		bundle.putString("VODID", vodDetailInfo.VODID);
		bundle.putString("VODNAME", vodDetailInfo.VODNAME);
		bundle.putDouble("PRICE", vodDetailInfo.price);
		bundle.putSerializable("VOD", vodDetailInfo);
		fragmentTransaction.hide(orderStatusFragment);

		if(vodPlayChoosePackageFragment.isAdded()){
			vodPlayChoosePackageFragment.setArguments(bundle);
			fragmentTransaction.show(vodPlayChoosePackageFragment).commit();
		}else{
			vodPlayChoosePackageFragment.setArguments(bundle);
			fragmentTransaction.add(R.id.iptv_vod_play, vodPlayChoosePackageFragment).commit();
		}
		TvApplication.status ="PLAYING_VOD_PAY_CHOOESE_PACKAGE";
		TvApplication.position = "VOD_ACTIVITY";

		status="CHOOSE_PACKAGE";
	}

	/**
	 * 用户订购完成，开始播放，此处可以考虑加选择从头还是继续播放。
	 */
	@Override
	public void startPlay(boolean fromStart) {

		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		if(orderSuccessFragment.isVisible()){
			fragmentTransaction.hide(orderSuccessFragment);
			fragmentTransaction.remove(orderSuccessFragment);
		}
		LogUtils.error("提交  ");
		fragmentTransaction.commit();

		orderStatus = true;
		status = "PLAY";
		if (isSitcom == 1) {
			//sitcomNumberList = vodDetailInfo.SUBVODNUMLIST;
			List<VodChannel> subVodIdList = vodDetailInfo.SUBVODIDLIST;
			//subVideoChannelID = intent.getIntExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS, 0);
			for (int i=0;i<subVodIdList.size();i++) {
				VodChannel vodChannel = subVodIdList.get(i);
				if (vodChannel.VODID == subVideoChannelID) {
					vodPlayVodName.setText(vodDetailInfo.VODNAME+"   第"+sitcomNumberList.get(i)+"集");
				}
			}
		} else {
			if (vodDetailInfo != null) {
				vodPlayVodName.setText(vodDetailInfo.VODNAME);
				if (TvApplication.platform==EnumType.Platform.ZTE) {

					subVideoChannelID = vodDetailInfo.VODID;
				}
			} else if(vodChannel != null) {
				vodPlayVodName.setText(vodChannel.VODNAME);
			}
		}

		progressBar = (ProgressBar) findViewById(R.id.vod_play_progressBar);
		tclPlayer.setPlayType(PlayType.VOD);

		if(fromStart){
			vodPlayRecord.setBeginPosition((long) 0);
			vodPlayRecord.setBeginPlayDateTime(new Date());
			tclPlayer.playFromStart();
		}else{
			// 查看记录是否在
			/*VodHistoryLocalFactory vodHistoryLocalFactory = new VodHistoryLocalFactory(this);
    		HistoryChannelInfo historyChannelInfo = vodHistoryLocalFactory.getHistoryById(subVideoChannelID);

    		vodPlayRecord.setBeginPosition(historyChannelInfo.playposition);
    		vodPlayRecord.setBeginPlayDateTime(new Date());
    		tclPlayer.playByTime("", (int) historyChannelInfo.playposition/1000, 1);*/
			tclPlayer.resume();
		}

		Intent intent = new Intent(ACTION_REPORT_VOD_START_PLAY);
		Bundle bundle = new Bundle();
		bundle.putSerializable("record", vodPlayRecord);
		intent.putExtras(bundle);
		sendBroadcast(intent); 
		TvApplication.status ="PLAYING_VOD";
		TvApplication.position = "VOD_ACTIVITY";
	}

	@Override
	public void startChoosePayMethod(final String packageSelected, final double price,String payType) {

		LogUtils.info("开始选择支付方式,已经选择的套餐方式:"+packageSelected);

		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		if("PassWord".equals(payType)){
			if(vodPlayChoosePayMethodFragment.isAdded()){
				fragmentTransaction.hide(vodPlayChoosePackageFragment);
				fragmentTransaction.show(vodPlayChoosePayMethodFragment).commit();
			}else{

				Bundle bundle = new Bundle();
				bundle.putString("VODID", vodDetailInfo.VODID);
				LogUtils.error("vodDetailInfo.VODID----"+vodDetailInfo.VODID);
				bundle.putString("SELECTED_PACKAGE", packageSelected);
				bundle.putSerializable("VOD", vodDetailInfo);
				bundle.putDouble("PRICE", price);
				bundle.putString("PAY_TYPE", "PassWord");
				vodPlayChoosePayMethodFragment.setArguments(bundle);
				fragmentTransaction.hide(vodPlayChoosePackageFragment);
				TvApplication.status ="PLAYING_VOD_PAY_CHOOESE_PAYMETHOD";
				fragmentTransaction.add(R.id.iptv_vod_play,vodPlayChoosePayMethodFragment).commit();
				TvApplication.position = "VOD_ACTIVITY";
			}
		}else if("WeChat".equals(payType)){
			dialog = ProgressDialog.show(VodChannelPlayActivity.this, "系统提示", "正在生成二维码，请稍等...", false);//创建ProgressDialog
			String token="";
			try {
				token=AESUtil.encrypt();
			} catch (Exception e) {
				LogUtils.error("--->token加密失败！");
			}
			//			mWeChatTask=new Task();
			Map<String,String> params=new HashMap<String,String>();
			params.put("userid", TvApplication.account);
			params.put("orderType", packageSelected);
			params.put("price", price+"");
			params.put("token", token);
			params.put("progid", vodDetailInfo.VODID);
			params.put("progname", vodDetailInfo.VODNAME);
			params.put("series", (vodDetailInfo.ISSITCOM==1)+"");
			params.put("days", "1");
			//			mWeChatTask.execute(params);
LogUtils.error(	"userid:"+ TvApplication.account+"orderType:"+ packageSelected	+"price:"+ price+""+"token:"+ token+"progid:"+ vodDetailInfo.VODID
			+"progname:"+ vodDetailInfo.VODNAME+"series, " +(vodDetailInfo.ISSITCOM==1)+"days:"+ "1");
			Iterator<Map.Entry<String,String>> it=params.entrySet().iterator();
			String connect="";
			while(it.hasNext()){
				Map.Entry<String, String> entry=it.next();
				connect+=entry.getKey()+"="+entry.getValue();
				if (it.hasNext()) {
					connect+="&";
				}
			}
			RequestBody requestBodyPost = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"),
					connect);
			//	.url("http://10.0.2.210/pay/weixin/unifiedorder")
			String url="http://10.0.2.210/pay/weixin/unifiedorder";
			if (TvApplication.useProxy) {
				url="http://110.249.173.111:20000/pay/weixin/unifiedorder";
			}
			Request requestPost = new Request.Builder()
					.url(url)
					.post(requestBodyPost)
					.build();
			client.newCall(requestPost).enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					LogUtils.error("error----->"+e);
					if (dialog!=null) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						showAlertDialog("系统提示！", "请求超时，请重试！");
					}


				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					String string = response.body().string();
					LogUtils.error("string----->"+string);
					if (string==null||string.equals("")) {
						showAlertDialog("系统提示！", "请求超时，请重试！");
						if (dialog!=null&&dialog.isShowing()) {
							dialog.dismiss();
						}
						return;
					}

					OrderResult orderResult=new Gson().fromJson(string, OrderResult.class);
					if (orderResult.getResult_code().equals("SUCCESS")) {
						LogUtils.error("orderResult.getResult_code()>>>"+orderResult.getResult_code());
						//						resultStr=	orderResult.getCode_url();

						final String filePath = getFileRoot(VodChannelPlayActivity.this) + File.separator
								+ "qr_" + System.currentTimeMillis() + ".jpg";
						boolean success = QRCodeUtil.createQRImage(orderResult.getCode_url(), 500, 500,null,filePath);//true ? BitmapFactory.decodeResource(getResources(), R.drawable.launcher_bg) : 

						if (success) {
							dialog.dismiss();
							if(vodPayByWeChatFragment.isAdded()){
								fragmentTransaction.hide(vodPlayChoosePackageFragment);
								fragmentTransaction.show(vodPayByWeChatFragment).commit();
							}else{

								Bundle bundle = new Bundle();
								bundle.putString("VODID", vodDetailInfo.VODID);
								bundle.putString("SELECTED_PACKAGE", packageSelected);
								bundle.putSerializable("VOD", vodDetailInfo);
								bundle.putDouble("PRICE", price);
								bundle.putString("PAY_TYPE", "WeChat");
								bundle.putString("FILEPATH", filePath);
								vodPayByWeChatFragment.setArguments(bundle);
								fragmentTransaction.hide(vodPlayChoosePackageFragment);
								TvApplication.status ="PLAYING_VOD_PAY_CHOOESE_PAYMETHOD";
								fragmentTransaction.add(R.id.iptv_vod_play,vodPayByWeChatFragment).commit();
								TvApplication.position = "VOD_ACTIVITY";
							}
						}else{
							if (dialog!=null) {
								dialog.dismiss();
							}

							LogUtils.error("---->创建二维码失败   "+filePath);
						}


					}else{
						showAlertDialog("系统提示！", "生成二维码失败，请重试！"+string);
						if (dialog!=null) {
							dialog.dismiss();
						}
					}

				}
			});


		}
		payStatus=payType;
		status = "PAY";
	}

	/**
	 * 选择从头播放或者从指定位置播放
	 */
	@Override
	public void choosePlayPosition() {

		orderStatus = true;
		status = "CHOOSE_PLAY_POSITION";
		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		orderSuccessFragment = new OrderSuccessFragment();
		fragmentTransaction.add(R.id.iptv_vod_play, orderSuccessFragment);

		if(vodPlayChoosePackageFragment.isVisible()){
			fragmentTransaction.remove(vodPlayChoosePackageFragment);
		}
		
		if(vodPayByWeChatFragment.isAdded()){
			if(vodPayByWeChatFragment.isVisible()){
				fragmentTransaction.remove(vodPayByWeChatFragment).commit();
			}
		}
		if(vodPlayChoosePayMethodFragment.isAdded()){
			if(vodPlayChoosePayMethodFragment.isVisible()){
				fragmentTransaction.remove(vodPlayChoosePayMethodFragment).commit();
			}
		}else{

		}

		TvApplication.status ="PLAYING_VOD_CHOOSE_PLAY_POSITION";
		TvApplication.position = "VOD_ACTIVITY";
	}


	/**
	 * 广播接收器 接受rebit推送消息
	 */
	@SuppressWarnings("unused")
	private BroadcastReceiver mBroadcastReceiver =new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, final Intent intent) {

			new Handler(VodChannelPlayActivity.this.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {

					int ret = -1;
					int message = -1;
					String mConnectType = null;

					String action = intent.getAction();
					LogUtils.error("收到的消息:"+action);
					//收到支付成功的消息
					if(action.equals(ACTION_PUBLISHSERVER_WECHAT_PAY_SUCCESS)){

						Bundle bundle =intent.getExtras();
						String scrollmessage = bundle.getString("MESSAGE");
						LogUtils.debug("收到微信支付成功的消息："+scrollmessage);
						choosePlayPosition();
					}else{
						LogUtils.error("splash 收到不明事件:"+action);
					}
				}
			});


		}
	};

	/**
	 * 注册要监听的广播
	 */
	private void installListeners() {
		IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction(ACTION_PUBLISHSERVER_WECHAT_PAY_SUCCESS);
		registerReceiver(mBroadcastReceiver, intentFilter);
	}
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
	//文件存储根目录
	private String getFileRoot(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File external = context.getExternalFilesDir(null);
			if (external != null) {
				return external.getAbsolutePath();
			}
		}

		return context.getFilesDir().getAbsolutePath();
	}
}
package com.iptv.rocky.mediaplayer;

import java.util.HashMap;
import java.util.Map;

import com.iptv.common.api.CommonPlayer;
import com.iptv.common.api.CommonPlayer.ICallback;
/*import com.iptv.common.api.CommonPlayer;
import com.iptv.common.api.CommonPlayer.ICallback;*/
import com.iptv.common.data.LiveChannel;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * This media player service for IPTV play. 
 *
 */
public class MediaPlayer {

	private static MediaPlayer instance = null;  

	private Context mContext;
	private EventHandler mEventHandler;

	private OnPreparedListener mOnPreparedListener;
	private OnCompletionListener mOnCompletionListener;
	private OnBufferingUpdateListener mOnBufferingUpdateListener;
	private OnBufferingUpdateListener mOnCachingUpdateListener;
	private OnVideoSizeChangedListener mOnVideoSizeChangedListener;
	private OnSeekCompleteListener mOnSeekCompleteListener;
	private OnErrorListener mOnErrorListener;
	private OnMediaStatusScaleListener mOnMediaStatusScaleListener;



	// 自定义的媒体类
	private OnRtspStatusEndOfStreamListener mOnRtspStatusEndOfStreamListener;
	private OnRtspStatusStartOfStreamListener mOnRtspStatusStartOfStreamListener;


	// 点播、时移、回看的rtsp URL
	private String mediaURL;

	// 播放列表模式时候，保存列表url数据；
	private Map<String, String> mediaUrlList;

	// MediaPlayer 的播放模式。在 初始化 MediaPlayer 对象时 必须进行该参数的初始设 定。在播放过程中,此属性 不能再被修改。
	private int singleOrPlaylistMode;

	// MediaPlayer 对象对应的视 频窗口的显示模式.
	private int videoDisplayMode;
	// 视频窗口的位置,相对于所 在浏览器窗口左上角的右 向偏移(象素点个数)
	private int videoDisplayLeft;
	private int videoDisplayTop;
	private int videoDisplayWidth;
	private int videoDisplayHeight;

	private int muteFlag;

	// 表示该播放器实例在生命 周期内都是否允许任何TrickMode 操作
	private int allowTrickmodeFlag;

	// 可选属性:设置是否循环播放节目
	private int cycleFlag;

	// 当前播放的媒体的总时长,单位：秒
	private int mediaDuration;

	// 媒体播放到的当前时间点;
	private int currentPlayTime;
	private int absStartTime;
	private int absEndTime;

	// 播放器的当前播放模式
	private String playbackMode; // Normal Play Pause Trickmode

	// 机顶盒当前播放 的频道号,不能获得有效的 频道号时,返回-1
	//private int channelNum = -1;
	private String channelNum = null;

	private LiveChannel chnToPlay;
	private static String TAG = MediaPlayer.class.getSimpleName();

	// AAA数据中的直播数据保存在此处。
	// key:   channelId
	// value: 频道信息
	//private Map<Integer, LiveChannel> liveChannelMap;
	private Map<String, LiveChannel> liveChannelMap;

	// 与底层对接的实现类
	private CommonPlayer iptvPlayer;

	private Float speed = (float) 0;

	// Define event from C
	private static final int MEDIA_NOP = 0;
	private static final int MEDIA_PREPARED = 1;
	private static final int MEDIA_PLAYBACK_COMPLETE = 2;
	private static final int MEDIA_ABS_TIMEPOS = 3;
	public static final int MEDIA_PREPARE_FAILED = 4;
	public static final int MEDIA_DATA_NOTAVAILABLENOW = 5;
	public static final int MEDIA_DATA_FAILED_TODECODER = 6;
	public static final int MEDIA_STATUS_SCALE = 7;

	public static final int MEDIA_END =0x51;

	private static final int RTSP_STATUS_END_OF_STREAM = 2101;
	private static final int RTSP_STATUS_START_OF_STREAM =2102;
	private boolean doTimeShift = false;
	// 定义单播播放的类型，区分点播和时移；主要用于播放时移、回看、点播时候，针对时移做处理。
	private PlayStatus playStatus;  

	private PlayType playType;

	private static final int CMD_SET_VIDEO_FRAME_MODE = 5000;
	private static final int STOP_MODE_FREEMODE = 0;
	private static final int STOP_MODE_BLACKMODR = 1;

	/* 下载数据成功;请求返回时移频道列表 */
	private static final int 	EIS_VOD_GETDATA_SUCCESS					= 5200;
	/* 下载数据失败;请求未返回时移频道列表 */
	private static final int 	EIS_VOD_GETDATA_FAILED			= 5201;
	/* 准备播放媒体成功 */
	private static final int 	EIS_VOD_PREPAREPLAY_SUCCESS		= 5202; 
	/* 连接服务器失败;建立会话失败或者服务器返回超时 */
	private static final int 	EIS_VOD_CONNECT_FAILED			= 5203;
	/* DVB在VOD指定的IPQAM中搜索数据失败 */
	private static final int 	EIS_VOD_DVB_SEARCH_FAILED		= 5204;
	/* 播放媒体成功 */
	private static final int 	EIS_VOD_PLAY_SUCCESS			= 5205; 
	/* 播放媒体失败 */
	private static final int 	EIS_VOD_PLAY_FAILED			= 5206; 
	/* 更新时移频道的节目列表成功 */
	private static final int 	EIS_VOD_PREPARE_PROGRAMS_SUCCESS	= 5207; 
	/* 更新时移频道的节目列表失败 */
	private static final int 	EIS_VOD_PREPARE_PROGRAMS_FAILED		= 5208; 
	/* 时移频道或VOD影片播放到了点播开始的位置 */
	private static final int 	EIS_VOD_PROGRAM_BEGIN			= 5209;
	/* 时移频道或VOD影片播放到了点播结束的位置 */
	private static final int 	EIS_VOD_PROGRAM_END			= 5210; 
	/* only DMX need */
	private static final int 	EIS_VOD_RELEASE_SUCCESS			= 5211; 
	/* only DMX need */
	private static final int 	EIS_VOD_RELEASE_FAILED			= 5212; 
	/* 时移请求服务器失败 */
	private static final int 	EIS_VOD_TSTVREQUEST_FAILED		= 5213; 
	/* hangzhou don't need */
	private static final int 	EIS_VOD_EPGURL_REQUEST_FINISHED		= 5214;
	/* 以后要改成IPANEL_VOD_START_SUCCESS */
	private static final int 	EIS_VOD_CHANGE_SUCCESS			= 5220; 
	/* 以后要改成IPANEL_VOD_START_FAILED */
	private static final int 	EIS_VOD_CHANGE_FAIL			= 5221; 
	private static final int 	EIS_VOD_START_BUFF			= 5222;
	private static final int 	EIS_VOD_STOP_BUFF			= 5223;
	private static final int 	EIS_VOD_OUT_OF_RANGE			= 5224;
	private static final int 	EIS_VOD_USER_EXCEPTION			= 5225;
	//下面3个需要相关人确认一下
	private static final int 	EIS_VOD_GET_PARAMETER_SUCCESS		= 5226;
	private static final int 	EIS_VOD_GET_PARAMETER_FAILED		= 5227;

	/* 请相关人修改这个枚举值。。。。。*/
	private static final int 	EIS_MULTI_PLAYER_EVENT			= 5228;	

	/* 寻找可播放的视频资源，准备下载资源，网络状态为LOADING */
	private static final int 	EIS_VOD_PREPARE_TO_DOWNLOAD           	= 5229;	
	/* 正在下载媒体数据，网络状态为：LOADING */
	private static final int 	EIS_VOD_BEING_DOWNLOADED             	= 5230; 
	/* 媒体数据下载完毕，下载数据的网络连接有可能已经断开，但不会影响媒体的播放。网络状态为LOADED */
	private static final int 	EIS_VOD_DOWNLOAD_SUCCESS             	= 5231;	
	/* 调用load()方法后，断开已经连接了的网络资源连接，重新加载video插件。网络状态为EMPTY */
	private static final int 	EIS_VOD_RELOADING_PLUGIN               	= 5232;	
	/* 尝试下载数据，但是下载不下来。网络状态为LOADING。*/
	private static final int 	EIS_VOD_TRY_TO_DOWNLOAD                	= 5233;	
	/* 执行pause()方法，返回此消息，paused 属性为true。*/
	private static final int 	EIS_VOD_EXECUTE_PAUSE                	= 5234; 
	/* 执行the seek operation需要花费一些时间，期间.seeking属性改变为true。*/
	private static final int 	EIS_VOD_EXECUTE_SEEKING               	= 5235;	
	/* 执行完the seek operation操作后，seeking属性已经为false. */
	private static final int 	EIS_VOD_SEEK_SUCCESS                  	= 5236;	
	/* 快进、快退、正常播放切换 */
	private static final int 	EIS_VOD_MEDIA_STATE_CHANGE             	= 5237;	
	/* 正在缓冲. */
	private static final int 	EIS_VOD_DOWNLOAD_CACHEING               = EIS_VOD_START_BUFF;	
	/*vod模块通知应用获取相关信息*/
	private static final int 	EIS_VOD_MESSAGE_OPEN			= 5239;
	/*上海电信使用;媒体播放异常;p2值区分具体含义:1:流媒体播放时发生轻微断流现象 2:连接流媒体服务器后未收到媒体流 3:流媒体播放时发生严重断流现象(丢包率不小于1%;持续20s时间)*/
	public static final int 	EIS_VOD_PLAY_ABNORMAL			= 5250;
	/*上海电信使用;rtsp控制异常,p2值区分具体含义:1:无法读取从服务器发来的RTSP命令 2:发送流媒体命令不成功 3:服务器关闭了RTSP连接*/
	private static final int 	EIS_VOD_RTSP_CMD_ERROR			= 5251;
	/*上海电信使用,组播控制异常,p2值区分具体含义:1:组播源错误,无法播放 2:组播时移服务器连接不上*/
	public static final int 	EIS_VOD_MEMBERSHIP_ERROR			= 5252;
	/*断流恢复*/
	private static final int 	EIS_VOD_PLAY_ABNORMAL_RESUME            = 5253;
	private static final int 	EIS_VOD_UNDEFINED=0;



	public MediaPlayer(Context ctx) {
		this(ctx, false);
	}

	// 静态定义方式
	public static MediaPlayer getInstance(Context ctx){  
		//Log.d("MediaPlayer", "MediaPlayer getInstance 是否为空:"+(instance==null));
		if(instance==null){  
			synchronized(MediaPlayer.class){  
				if(null == instance){  
					instance = new MediaPlayer(ctx);  
				}  
			}  
		}else{
			//mContext = ctx;
		}
		return instance;  
	}  

	public MediaPlayer(Context ctx, boolean preferHWDecoder) {
		Looper looper;
		if ((looper = Looper.myLooper()) != null) {
			mEventHandler = new EventHandler(this, looper);
		} else if ((looper = Looper.getMainLooper()) != null) {
			mEventHandler = new EventHandler(this, looper);
		} else {
			mEventHandler = null;
		}

		// 添加自定义变量初始化
		iptvPlayer = new CommonPlayer(ctx);
		RegisterCallBack();
		//liveChannelMap= new HashMap<Integer, LiveChannel>();
		liveChannelMap= new HashMap<String, LiveChannel>();
		singleOrPlaylistMode = 0;
		playStatus = PlayStatus.STOP;
	}

	// ------------------------------
	//直播部分涉及到的函数
	// ------------------------------
	/**
	 *  要求终端访问指定的频道,并立即返回。
	 */
	public int joinChannel(String channelid) {
		LogUtils.info("channelid : "+channelid);
		int result = -1;
		chnToPlay = liveChannelMap.get(channelid);
		String playURL = null;
		if(chnToPlay.TimeShift){
			if(chnToPlay.TimeShiftURL != null){
				playURL = chnToPlay.ChannelURL + ";" + chnToPlay.TimeShiftURL;
			}else {
				playURL = chnToPlay.ChannelURL; 
			}
		}else{
			playURL = chnToPlay.ChannelURL; 
		}
		playURL += "?sky:&channel=1&timeshift=";
		playURL += chnToPlay.TimeShift?"1":"0";
		playURL += "&timeshiftlength=";
		playURL += String.valueOf(chnToPlay.TimeShiftLength);
		if(playURL != null){
			LogUtils.debug("channel Play Url:"+playURL);
			if (playURL.startsWith("igmp")) {
				iptvPlayer.SetDataSource(playURL, 0);
				if (iptvPlayer.Open()) {
					if (iptvPlayer.Start()) {
						setVideoDisplayArea(TvApplication._x, TvApplication._y, TvApplication.width, TvApplication.height);
						LogUtils.error("TvApplication._x=="+TvApplication._x+", TvApplication._y=="+ TvApplication._y+", TvApplication.width=="+TvApplication.width+", TvApplication.height=="+TvApplication.height );
						result = 0;
						channelNum = channelid;
						playStatus=PlayStatus.CHANNEL_PLAY;
						playType=PlayType.CHANNEL;
					}else{

					}
				}else{

				}
			} else if (playURL.startsWith("rtsp")) {
				playStatus=PlayStatus.CHANNEL_PLAY;
			}
		}else{
		}
		return result;
	}


	//	public int joinChannel(int channelid) {
	//		int result = -1;
	//		chnToPlay = liveChannelMap.get(channelid);
	//		String playURL = null;
	//		if(chnToPlay.TimeShift){
	//			if(chnToPlay.TimeShiftURL != null){
	//				playURL = chnToPlay.ChannelURL + ";" + chnToPlay.TimeShiftURL;
	//			}else {
	//				playURL = chnToPlay.ChannelURL; 
	//			}
	//		}else{
	//			playURL = chnToPlay.ChannelURL; 
	//		}
	//		playURL += "?sky:&channel=1&timeshift=";
	//		playURL += chnToPlay.TimeShift?"1":"0";
	//		playURL += "&timeshiftlength=";
	//		playURL += String.valueOf(chnToPlay.TimeShiftLength);
	//		if(playURL != null){
	//	
	//			if (playURL.startsWith("igmp")) {
	//				iptvPlayer.SetDataSource(playURL, 0);
	//				if (iptvPlayer.Open()) {
	//					if (iptvPlayer.Start()) {
	//						result = 0;
	//						channelNum = channelid;
	//						playStatus=PlayStatus.CHANNEL_PLAY;
	//						playType=PlayType.CHANNEL;
	//					}else{
	//						
	//					}
	//				}else{
	//					
	//				}
	//			} else if (playURL.startsWith("rtsp")) {
	//				playStatus=PlayStatus.CHANNEL_PLAY;
	//			}
	//		}else{
	//		}
	//		return result;
	//	}

	/**
	 * 要求终端离开指定的频道,并立即 返回。 返回值:0,表示成功; -1:表示频道号无效。
	 * 
	 * @return
	 */
	public int leaveChannel() {
		int result = -1;
		if (channelNum != null) {
			if (liveChannelMap.containsKey(channelNum)) {
				LiveChannel chnToPlay = liveChannelMap.get(channelNum);
				String url = chnToPlay.ChannelURL;

				// 后面加单播和组播的判断；
				if (url.startsWith("igmp")) {
					iptvPlayer.Stop();
					result = 0;
					playStatus=PlayStatus.STOP;
				} else if (url.startsWith("rtsp")) {

				}
			}
		}
		return result;
	}

	// ------------------------------
	//直播部分涉及到的函数
	//-------------------------------
	/**
	 * 
	 *  从媒体起始点开始播放。对TV channel,以实时TV的方式 开始播放
	 */
	public void playFromStart() {
		LogUtils.debug("mediaURL:"+mediaURL);
		iptvPlayer.SetDataSource(mediaURL, 1);
		if (iptvPlayer.Open()) {
			if (iptvPlayer.Start()) {
				LogUtils.error("--------->2");
//				setVideoDisplayArea(0, 0, 1920, 1080);
				LogUtils.error("TvApplication._x=="+TvApplication._x+", TvApplication._y=="+ TvApplication._y+", TvApplication.width=="+TvApplication.width+", TvApplication.height=="+TvApplication.height );
				setVideoDisplayArea(TvApplication._x, TvApplication._y, TvApplication.width, TvApplication.height);
			}
			
			playStatus=PlayStatus.NORMAL_PLAY;
		}
	}

	// ------------------------------
	//直播部分涉及到的函数
	//-------------------------------
	/**
	 * 
	 *  从媒体起始点开始播放。对TV channel,以实时TV的方式 开始播放
	 */
	public void rePlayFromStart() {
		//iptvPlayer.SetDataSource(mediaURL, 1);
		//if (iptvPlayer.Open()) {
		iptvPlayer.Start();
		setVideoDisplayArea(TvApplication._x, TvApplication._y, TvApplication.width, TvApplication.height);
		LogUtils.error("TvApplication._x=="+TvApplication._x+", TvApplication._y=="+ TvApplication._y+", TvApplication.width=="+TvApplication.width+", TvApplication.height=="+TvApplication.height );
		playStatus=PlayStatus.NORMAL_PLAY;
		//playType=PlayType.VOD;
		//}
	}

	/**
	 * 从指定地点开始播放
	 */
	public void playByTime(String type, int timestamp, int speed) {
		iptvPlayer.SetDataSource(mediaURL, 1);
		if (iptvPlayer.Open()) {
			iptvPlayer.Seek(timestamp);
			iptvPlayer.Start();
			setVideoDisplayArea(TvApplication._x, TvApplication._y, TvApplication.width, TvApplication.height);
			LogUtils.error("TvApplication._x=="+TvApplication._x+", TvApplication._y=="+ TvApplication._y+", TvApplication.width=="+TvApplication.width+", TvApplication.height=="+TvApplication.height );
			playStatus=PlayStatus.NORMAL_PLAY;
			playType=PlayType.VOD;
		}
	}

	/**
	 * 快进 Float 类型,2 至 32
	 */
	public void fastForward(Float speed) {
		LogUtils.debug("speed: "+speed);
		if (speed == 2 || speed == 4 || speed == 8 || speed == 16
				|| speed == 32) {

			iptvPlayer.Scale(speed);
			playStatus=PlayStatus.FASTFORWORD;
			speed = -speed;
		}
	}

	/**
	 * 快退 
	 *  参数类型： Float ,
	 *  可选倍数：2、4、、8、16、32
	 */
	public void fastRewind(Float speed) {
		if (speed == 2 || speed == 4 || speed == 8 || speed == 16 || speed == 32) {
			//LogUtils.debug("开始 Scale  speed-"+ speed);
			iptvPlayer.Scale(-speed);
			//LogUtils.debug("结束 Scale speed-"+speed);
			playStatus=PlayStatus.FASTREWIND;
			this.speed = -speed;
		}else if(speed == -2 || speed == -4 || speed == -8 || speed == -16 || speed == -32){
			//LogUtils.debug("开始 Scale  speed-"+ speed);
			iptvPlayer.Scale(-speed);
			//LogUtils.debug("结束 Scale speed-"+speed);
			playStatus=PlayStatus.FASTREWIND;
			this.speed = speed;
		}
	}

	//pause恢复到播放的时候调用
	public void resume() {
		iptvPlayer.Resume();
		if(playType == PlayType.CHANNEL){
			playStatus=PlayStatus.TIMESHIFT_PLAY;
		}else if(playType == PlayType.VOD){
			playStatus = PlayStatus.NORMAL_PLAY;
		}else if(playType == PlayType.SCHEDULE){
			playStatus = PlayStatus.NORMAL_PLAY;
		}
		//playStatus=PlayStatus.NORMAL_PLAY;
	}

	//快进快退恢复到正常播放速率的时候调用
	public void normalrate() {
		iptvPlayer.Resume();
		if(playType == PlayType.CHANNEL){
			playStatus=PlayStatus.TIMESHIFT_PLAY;
		}else if(playType == PlayType.VOD){
			playStatus = PlayStatus.NORMAL_PLAY;
		}else if(playType == PlayType.SCHEDULE){
			playStatus = PlayStatus.NORMAL_PLAY;
		}
		speed = (float) 1;
	}

	public void gotoEnd() {
		iptvPlayer.gotoEnd();
	}

	public void gotoStart() {
		iptvPlayer.gotoStart();
	}

	// 暂停正在播放的媒体
	public void pause() {
		iptvPlayer.pause();
		playStatus=PlayStatus.PAUSE;

	}

	/**
	 * 停止正在播放的媒体。并释放机顶盒本地播放器的相关资源。
	 * 本方法只能用于停止通过playFromStart或playByTime方法开始的流媒体对象。
	 */
	public void stop() {
		iptvPlayer.Stop();
		playStatus=PlayStatus.STOP;
	}

	public void setKeepLastFrame(int nKeepLastFrame){
		iptvPlayer.setKeepLastFrame(nKeepLastFrame);
	}

	/**
	 * 每次调用该函数后,视频显 示窗口并不会被立即重新 刷新以反映更改后的显示 效果只有等到显式调用 refreshVideoDisplay()后才会
	 * 刷新
	 */
	public void setVideoDisplayArea(int left, int top, int width, int height) {
		videoDisplayLeft = left;
		videoDisplayTop = top;
		videoDisplayWidth = width;
		videoDisplayHeight = height;
		iptvPlayer.SetWindow(left, top, width, height);
	}

	/**
	 * Register a callback to be invoked when the media source is ready for playback.
	 */
	public void setOnPreparedListener(OnPreparedListener listener) {
		mOnPreparedListener = listener;
	}



	/**
	 * Register a callback to be invoked when the end of a media source has been
	 * reached during playback.
	 */
	public void setOnCompletionListener(OnCompletionListener listener) {
		mOnCompletionListener = listener;
	}

	/**
	 * Register a callback to be invoked when the status of a network stream's
	 * buffer has changed.
	 *
	 * @param listener the callback that will be run.
	 */
	public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
		mOnBufferingUpdateListener = listener;
	}

	public void setOnRtspStatusStartOfStreamListener(OnRtspStatusStartOfStreamListener listener){
		this.mOnRtspStatusStartOfStreamListener = listener;
	}

	public void setOnMediaStatusScaleListener(OnMediaStatusScaleListener listener){
		this.mOnMediaStatusScaleListener = listener;
	}

	public interface OnPreparedListener {
		/**
		 * Called when the media file is ready for playback.
		 * 
		 * @param mp the MediaPlayer that is ready for playback
		 */
		void onPrepared(MediaPlayer mp);
	}

	public interface OnSeekCompleteListener {
		/**
		 * Called to indicate the completion of a seek operation.
		 *
		 * @param mp the MediaPlayer that issued the seek operation
		 */
		public void onSeekComplete(MediaPlayer mp);
	}

	public interface OnCompletionListener {
		/**
		 * Called when the end of a media source is reached during playback.
		 * 
		 */
		void onCompletion(MediaPlayer mp);
	}

	public interface OnBufferingUpdateListener {
		/**
		 * Called to update status in buffering a media stream. Buffering is
		 * storing data in memory while caching on external storage.
		 * 
		 */
		void onBufferingUpdate(MediaPlayer mp, int percent);
	}

	public interface OnVideoSizeChangedListener {
		/**
		 * 
		 */
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height);
	}

	public interface OnErrorListener {
		/**
		 * Called to indicate an error.
		 */
		boolean onError(MediaPlayer mp, int what, int extra);
	}

	public interface OnRtspStatusEndOfStreamListener {
		/**
		 * Called to indicate an error.
		 */
		void onRtspStatusEndOfStream(MediaPlayer mp);
	}

	public interface OnRtspStatusStartOfStreamListener {
		/**
		 * Called to indicate an error.
		 */
		void onRtspStatusStartOfStream(MediaPlayer mp);
	}

	public interface OnMediaStatusScaleListener {
		void onMediaStatusScaleListener(MediaPlayer mp, int speed);
	}


	/**
	 * Register a callback to be invoked when the video size is known or updated.
	 *
	 * @param listener the callback that will be run
	 */
	public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
		mOnVideoSizeChangedListener = listener;
	}

	public void setOnRtspStatusEndOfStreamListener(OnRtspStatusEndOfStreamListener mOnRtspStatusEndOfStreamListener){
		this.mOnRtspStatusEndOfStreamListener=mOnRtspStatusEndOfStreamListener;
	}


	/**
	 * Register a callback to be invoked when an error has happened during an
	 * asynchronous operation.
	 *
	 * @param listener the callback that will be run
	 */
	public void setOnErrorListener(OnErrorListener listener) {
		mOnErrorListener = listener;
	}

	/**
	 * Register a callback to be invoked when a seek operation has been completed.
	 *
	 * @param listener the callback that will be run
	 */
	public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
		this.mOnSeekCompleteListener = listener;
	}

	public void RegisterCallBack() {
		ICallback cb = new ICallback() {
			public void Notify(int what, int arg1, int arg2, String str) {
				LogUtils.debug("消息处理。registercallback");
				if (mEventHandler != null) {
					Log.i(TAG, "Notify...");
					Message m = mEventHandler.obtainMessage(what, arg1, arg2, str);
					mEventHandler.sendMessage(m);
				}
			}
		};
		iptvPlayer.RegisterCallBack(cb);
	}

	@SuppressLint("HandlerLeak")
	private class EventHandler extends Handler {
		private MediaPlayer mMediaPlayer;
		private Bundle mData;

		public EventHandler(MediaPlayer mp, Looper looper) {
			super(looper);
			mMediaPlayer = mp;
		}

		@Override
		public void handleMessage(Message msg) {
			LogUtils.error( "---------收到的消息:"+(msg.what)+"");
			switch (msg.what) {
			//				case MEDIA_PREPARED:  媒体准备成功
			case EIS_VOD_PREPAREPLAY_SUCCESS:
				LogUtils.info("playStatus  :"+playStatus+"  mOnPreparedListener 是空？  "+(mOnPreparedListener==null));
				if(playStatus.equals(PlayStatus.CHANNEL_NOTAVAILABLENOW)) playStatus = PlayStatus.CHANNEL_PLAY;
				if (mOnPreparedListener != null) mOnPreparedListener.onPrepared(mMediaPlayer);

				break;
				//				case MEDIA_PLAYBACK_COMPLETE:
			case EIS_VOD_PROGRAM_END:
				LogUtils.error("MEDIA_PLAYBACK_COMPLETE");
				if (mOnCompletionListener != null)
					mOnCompletionListener.onCompletion(mMediaPlayer);
				if (playStatus.equals(PlayStatus.FASTFORWORD)) {
					playStatus=PlayStatus.CHANNEL_PLAY;
				}
				// stayAwake(false); //打开允许休眠
				break;
			case EIS_VOD_PROGRAM_BEGIN:
				if (mOnRtspStatusStartOfStreamListener != null){
					iptvPlayer.Resume();
					mOnRtspStatusStartOfStreamListener.onRtspStatusStartOfStream(mMediaPlayer);
				}

				break;
			case MEDIA_ABS_TIMEPOS:
				absStartTime = msg.arg1;
				absEndTime = msg.arg2;
				Log.d("开始时间点 ：", absStartTime+"结束时间点："+ absEndTime + "");
				break;

			case RTSP_STATUS_END_OF_STREAM:
				//LogUtils.error("RTSP_STATUS_END_OF_STREAM");
				if (mOnRtspStatusEndOfStreamListener != null){
					mOnRtspStatusEndOfStreamListener.onRtspStatusEndOfStream(mMediaPlayer);
				}
				//Log.d("MediaPlayer", "RTSP_STATUS_END_OF_STREAM playType："+playType);
				playStatus=PlayStatus.STOP;
				break;

			case RTSP_STATUS_START_OF_STREAM:
				if (mOnRtspStatusStartOfStreamListener != null) mOnRtspStatusStartOfStreamListener.onRtspStatusStartOfStream(mMediaPlayer);
				break;	

			case MEDIA_PREPARE_FAILED:
				Log.d("MediaPlayer playback prepare failed , status code:", msg.arg1 + "" );

				LogUtils.info("播放状态："+playStatus);
				if(playStatus.equals(PlayStatus.FASTREWIND)){
					playStatus = PlayStatus.CHANNEL_PLAY;
				}
				if(mOnErrorListener != null){ 
					mOnErrorListener.onError(mMediaPlayer, MEDIA_PREPARE_FAILED,  msg.arg1);	
				}
				break;

			case MEDIA_DATA_NOTAVAILABLENOW:
				Log.d("no data received in 10 seconds " ,"exit playback");
				playStatus = PlayStatus.CHANNEL_NOTAVAILABLENOW;
				if(mOnErrorListener != null){ 
					mOnErrorListener.onError(mMediaPlayer, MEDIA_DATA_NOTAVAILABLENOW, msg.arg1);

				}
				break;
			case EIS_VOD_MEMBERSHIP_ERROR:
				Log.d("EIS_VOD_MEMBERSHIP_ERROR" ,"msg.arg1："+msg.arg1);
				playStatus = PlayStatus.CHANNEL_NOTAVAILABLENOW;
				if(mOnErrorListener != null){ 
					mOnErrorListener.onError(mMediaPlayer, EIS_VOD_MEMBERSHIP_ERROR, msg.arg1);

				}
				break;	
				
				
				
			case EIS_VOD_PLAY_ABNORMAL:
				Log.d("no data received in 10 seconds " ,"stream not available now");
				playStatus = PlayStatus.CHANNEL_NOTAVAILABLENOW;
				if(mOnErrorListener != null){ 
					mOnErrorListener.onError(mMediaPlayer, EIS_VOD_PLAY_ABNORMAL, msg.arg1);

				}
				break;	
				
					

			case MEDIA_DATA_FAILED_TODECODER:
				Log.d("get buffer failed, so failed to send data to decoder,","please reInit MHAL player");
				if(mOnErrorListener != null){ 
					mOnErrorListener.onError(mMediaPlayer, MEDIA_DATA_FAILED_TODECODER, msg.arg1);
				}
				break;

				//				case MEDIA_STATUS_SCALE:
			case EIS_VOD_MEDIA_STATE_CHANGE:
				LogUtils.info("EIS_VOD_MEDIA_STATE_CHANGE------->");
				speed = (float) msg.arg1;
				//直播 -1，暂停0，时宜播放1，快进2倍就是2，快退2倍就是-2
				if(msg.arg1 == -1){
					playStatus = PlayStatus.CHANNEL_PLAY;
				} else if(msg.arg1 == 0){
					playStatus = PlayStatus.PAUSE;
				} else if(msg.arg1 ==1){
					if(playType == PlayType.VOD){
						playStatus = PlayStatus.NORMAL_PLAY;
					}else if(playType == playType.SCHEDULE){
						playStatus = PlayStatus.NORMAL_PLAY;
					}else if(playType == playType.CHANNEL){
						playStatus = PlayStatus.TIMESHIFT_PLAY;
					}
				} else if(msg.arg1 == 2 || msg.arg1 ==4 || msg.arg1 ==8 || msg.arg1 == 16 || msg.arg1 ==32){
					playStatus = PlayStatus.FASTFORWORD;
				} else if(msg.arg1 == -2 || msg.arg1 ==-4 || msg.arg1 ==-8 || msg.arg1 == -16 || msg.arg1 == -32){
					playStatus = PlayStatus.FASTREWIND;
				}

				if(mOnMediaStatusScaleListener != null){
					mOnMediaStatusScaleListener.onMediaStatusScaleListener(mMediaPlayer,msg.arg1);
				}
				break;
			case MEDIA_END:
				LogUtils.error("收到Media_end:");
				break;

			default:
				Log.d("不知道的消息类型 ", msg.what+"");
				return;
			}
		}
	}

	/**
	 * Releases resources associated with this MediaPlayer object. It is
	 * considered good practice to call this method when you're done using the
	 * MediaPlayer.
	 */
	public void release() {
		//	    stayAwake(false);
		//	    updateSurfaceScreenOn();
		iptvPlayer.setKeepLastFrame(0);
		iptvPlayer.clearVideo();
		mOnPreparedListener = null;
		mOnBufferingUpdateListener = null;
		mOnCompletionListener = null;
		mOnSeekCompleteListener = null;
		mOnErrorListener = null;
		// mOnInfoListener = null;
		mOnVideoSizeChangedListener = null;
		mOnCachingUpdateListener = null;
		//mOnHWRenderFailedListener = null;
		iptvPlayer.release();
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public void setChannelNum(String channelNum) {
		this.channelNum = channelNum;
	}

	public void clearAllMedia() {

	}

	public void clearVideo(){
		iptvPlayer.clearVideo();
	}

	public void refreshVideoDisplay() {

	}

	public String getMediaURL() {
		return mediaURL;
	}

	public void setSingleMedia(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public Map getLiveChannelMap() {
		return liveChannelMap;
	}

	public void setLiveChannelMap(Map liveChannelMap) {
		this.liveChannelMap = liveChannelMap;
	}

	public String getChannelNum() {
		return channelNum;
	}

	public int getSingleOrPlaylistMode() {
		return singleOrPlaylistMode;
	}

	public void setSingleOrPlaylistMode(int singleOrPlaylistMode) {
		this.singleOrPlaylistMode = singleOrPlaylistMode;
	}

	public int getVideoDisplayMode() {
		return videoDisplayMode;
	}

	public void setVideoDisplayMode(int videoDisplayMode) {
		this.videoDisplayMode = videoDisplayMode;
	}

	public Map<String, String> getMediaUrlList() {
		return mediaUrlList;
	}

	public void setMediaUrlList(Map<String, String> mediaUrlList) {
		this.mediaUrlList = mediaUrlList;
	}

	public int getVideoDisplayLeft() {
		return videoDisplayLeft;
	}

	public int getVideoDisplayTop() {
		return videoDisplayTop;
	}

	public int getVideoDisplayWidth() {
		return videoDisplayWidth;
	}

	public int getVideoDisplayHeight() {
		return videoDisplayHeight;
	}

	public int getMuteFlag() {
		return muteFlag;
	}

	public void setMuteFlag(int muteFlag) {
		this.muteFlag = muteFlag;
	}

	public int getAllowTrickmodeFlag() {
		return allowTrickmodeFlag;
	}

	public void setAllowTrickmodeFlag(int allowTrickmodeFlag) {
		this.allowTrickmodeFlag = allowTrickmodeFlag;
	}

	public int getCycleFlag() {
		return cycleFlag;
	}

	public void setCycleFlag(int cycleFlag) {
		this.cycleFlag = cycleFlag;
	}

	/** Get media's length; 视频时长
	 * 
	 * @return
	 */
	public int getMediaDuration() {
		mediaDuration = iptvPlayer.getMediaDuration();
		return mediaDuration;
	}

	/** Get current play time 当前播放时间;
	 * 
	 * @return
	 */
	public int getCurrentPlayTime() {
		currentPlayTime = iptvPlayer.GetCurrentPlaybackTimePos();
		Log.d("current playback time: ", currentPlayTime +"");
		return currentPlayTime;
	}

	public String getPlaybackMode() {
		return playbackMode;
	}

	public void setPlaybackMode(String playbackMode) {
		this.playbackMode = playbackMode;
	}


	public enum PlayStatus {
		STOP("停止"),
		CHANNEL_PLAY("直播正常播放"),
		CHANNEL_NOTAVAILABLENOW("直播现在无法播放"),
		NORMAL_PLAY("正常播放"),
		TIMESHIFT_PLAY("时移正常播放"),
		MULTICAST_CHANNEL_PLAY("组播频道直播状态"),
		UNICAST_CHANEL_PLAY("单播频道直播状态"),
		PAUSE("暂停"),
		FASTFORWORD("快进"),
		FASTREWIND("快退");

		private final String status;

		private PlayStatus(String status){
			this.status = status;
		}

		public String getStatus(){
			return this.status;
		}
	}

	public enum PlayType {
		VOD("vod"),
		SCHEDULE("schedule"),
		CHANNEL("Live Channel");

		private final String type;

		private PlayType(String type){
			this.type = type;
		}

		public String getType(){
			return this.type;
		}
	}

	public PlayStatus getPlayStatus() {
		return playStatus;
	}

	public int getAbsStartTime() {
		return iptvPlayer.GetCurrentPlaybackTimePos();
	}

	public int getAbsEndTime() {
		return absEndTime;
	}

	public PlayType getPlayType() {
		return playType;
	}

	public void setPlayType(PlayType playType) {
		this.playType = playType;
	}

	public void reset(){
		this.iptvPlayer.release();
	}

}

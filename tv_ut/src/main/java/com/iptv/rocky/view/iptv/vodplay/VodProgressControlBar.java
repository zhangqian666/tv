package com.iptv.rocky.view.iptv.vodplay;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.iptv.rocky.R;
import com.iptv.rocky.mediaplayer.MediaPlayer.PlayStatus;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 点播的进度条控制
 * 2016-02-05
 * 
 */
public class VodProgressControlBar extends RelativeLayout{

	private ProgressBar progressBar;
	private ImageView playStatusIcon;
	
	private TextView txtSpeedDisplay;
	private TextView vodPlayCurrentTime;
	private TextView vodPlayTotalTime;
	private PlayStatus playStatus;
	// 当前播放的媒体的总时长,单位：秒
	private int mediaDuration;
	
	private Handler handler = new Handler();
	
	public VodProgressControlBar(Context context) {
		this(context, null, 0);
	}
	
	public VodProgressControlBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public VodProgressControlBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		progressBar = (ProgressBar) findViewById(R.id.vod_play_progressBar);
		playStatusIcon = (ImageView) findViewById(R.id.vod_play_status_icon);
		
		vodPlayCurrentTime = (TextView) findViewById(R.id.vod_play_current_time);
		vodPlayTotalTime = (TextView) findViewById(R.id.vod_play_total_time);
	}
	
	
	// 绘画当前点的信息
/*	private void setProgressInfo() {
		if (tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) || tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
			//Log.d("总时长:",tclPlayer.getMediaDuration()+"");
			vodPlayTotalTime.setText(createTimeString(tclPlayer.getMediaDuration()));
			int currentTime = tclPlayer.getCurrentPlayTime();
			
			SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss");
			String now = formatter.format(new Date());
			
			//Log.d("设置进度条","提取的当前时间:"+currentTime+""+ "; 现在时间"+ now+ "; 当前倍速："+txtSpeedDisplay.getText());
			progressBar.setMax(tclPlayer.getMediaDuration());
			progressBar.setProgress(currentTime);
			vodPlayCurrentTime.setText(createTimeString(currentTime));
		}
	}*/
	
	
	// 绘画当前点的信息
	private void setProgressInfo(int currentPlayTime) {
		if (playStatus.equals(PlayStatus.FASTFORWORD) || playStatus.equals(PlayStatus.FASTREWIND)) {
			//Log.d("总时长:",tclPlayer.getMediaDuration()+"");
			vodPlayTotalTime.setText(createTimeString(mediaDuration));
			int currentTime = currentPlayTime;
			
			SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss");
			String now = formatter.format(new Date());
			
			//Log.d("设置进度条","提取的当前时间:"+currentTime+""+ "; 现在时间"+ now+ "; 当前倍速："+txtSpeedDisplay.getText());
			progressBar.setMax(mediaDuration);
			progressBar.setProgress(currentTime);
			vodPlayCurrentTime.setText(createTimeString(currentTime));
		}
	}
	
	
	// 创建要显示的时间
	private String createTimeString(int second) {
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
	
	
	// 定时刷新进度条;
/*	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			//要做的事情
			setProgressInfo();
			handler.postDelayed(runnable, 1000);
		}
	};*/
	
}

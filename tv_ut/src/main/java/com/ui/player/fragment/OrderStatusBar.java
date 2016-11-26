package com.ui.player.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.rocky.R;

public class OrderStatusBar extends RelativeLayout{

	private TextView txtTimeLeft;
	
	public OrderStatusBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	 @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        
        //initView();
		txtTimeLeft = (TextView)findViewById(R.id.txt_vod_try_play_time_left);
		
    }
	 
	public void destroy() {
		//mPageView.destroy();
	}
	
	public void resume() {
		//mTabView.autoFocusStart();
	}
	
	public void pause() {
		//mTabView.autoFocusStop();
	} 

	 
	//显示已经播放的时间
	public void setTimePlayed(int playedTime){
		txtTimeLeft.setText(createTimeString(playedTime));
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
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(min);
        date.setSeconds(sec);
		return format.format(date);
	}
}

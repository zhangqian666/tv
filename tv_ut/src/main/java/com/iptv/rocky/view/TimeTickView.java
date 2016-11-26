package com.iptv.rocky.view;

import java.util.Calendar;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.format.DateFormat;
import android.util.AttributeSet;

public class TimeTickView extends TextViewDip {

	private Context mContext;
	private Calendar mCalendar;
	
	private BroadcastReceiver broadCast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			setStateTip();
		}
	};
	
	public TimeTickView(Context context) {
		this(context, null, 0);
	}
	
	public TimeTickView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TimeTickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mContext = context;
		setTextSize(TvApplication.sTvMasterTextSize);
		setStateTip();
	}
	
	public void start() {
		try {
			IntentFilter intent = new IntentFilter(Intent.ACTION_TIME_TICK);
			mContext.registerReceiver(broadCast, intent);
		} catch (Exception e) {
			LogUtils.e("TimeTickView", "registerReceiver error.");
		}
	}
	
	public void stop() {
		try {
			mContext.unregisterReceiver(broadCast);
		} catch (Exception e) {
			LogUtils.e("TimeTickView", "unregisterReceiver error.");
		}
	}
	
	private void setStateTip() {
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		setText(DateFormat.format("kk:mm", mCalendar));
	}
}

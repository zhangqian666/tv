package com.iptv.rocky.tcl.view.live;

import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.R.color;
import com.iptv.rocky.base.LiveBaseListItemView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.tcl.ArialBlackTextView;
import com.iptv.rocky.tcl.LiveChannelPlayActivity;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.TextViewDip;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

public class ListItemView extends LiveBaseListItemView {
	private LiveChannelBill mLiveChannel;
	private ArialBlackTextView mIndex;
	private TextViewDip mName;
	private FrameLayout frameChannelNumberBg;
	
	//是否是正在播放
	public boolean playing;


	public ListItemView(Context context) {
		this(context, null, 0);
	}

	public ListItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		isRelection = true;
		ListView.LayoutParams params = new ListView.LayoutParams((int) (ScreenUtils.getRecChanWidth()*1.4), (int) (ScreenUtils.getRecChanListItemHeight()*1.18));
		setLayoutParams(params);
		//Log.d("Live",ScreenUtils.getRecChanListItemHeight()+";"+ScreenUtils.getRecChanWidth());
	}

/*	public void initView(LiveChannelBill info) {
		mLiveChannel = info;
		
		mIndex.setText(String.format("%03d", mLiveChannel.UserChannelID));
		mName.setText(mLiveChannel.ChannelName);
	}*/
	
	public void initView(LiveChannelBill info,boolean playing) {
		mLiveChannel = info;
		this.playing=playing;
		if(playing){
			frameChannelNumberBg.setBackgroundResource(R.drawable.ic_channel_current_normal);
			mIndex.setVisibility(View.GONE);
		}else{
			frameChannelNumberBg.setBackgroundResource(R.drawable.ic_channel_normal);
			mIndex.setText(String.format("%03d", mLiveChannel.UserChannelID));
			mIndex.setVisibility(View.VISIBLE);
		}
		mName.setText(mLiveChannel.ChannelName);
	}

	@Override
	public boolean isNotTopPadding() {
		//Log.d("ListItemView","isNotTopPadding:");
		return true;
	}

	@Override
	public void processFocus(boolean hasFocus) {
		// Log.d("ListItemView","processFocus:是否有焦点？"+hasFocus);
		super.processFocus(hasFocus);
	}

	@Override
	public void onClick(Context context) {
		// 发送一个切换直播频道的广播
		Intent intent = new Intent(LiveChannelPlayActivity.ACTION_CHANGE_LIVE_CHANNEL);
			intent.putExtra(LiveChannelPlayActivity.LIVE_CHANNEL_ID, mLiveChannel.ChannelID);
		LogUtils.info(" mLiveChannel.ChannelID------->"+ mLiveChannel.ChannelID);
		context.sendBroadcast(intent);
	}
	
	public void setViewEnlarge() {
		
		mName.setTextSize(TvApplication.sTvSelectorTextSize+2);
		mIndex.setTextColor(color.md_indigo_900);
		//mName.setTextColor(Color.WHITE);
		setBackgroundResource(R.drawable.detail_summery_text_bg);
		if(playing){
			frameChannelNumberBg.setBackgroundResource(R.drawable.ic_channel_current_focused);
		}else{
			frameChannelNumberBg.setBackgroundResource(R.drawable.ic_channel_focused);
			//mIndex.setTextSize(TvApplication.sTvSelectorTextSize+2);
		}
	}
	
	public void setViewNormal() {
		
		mName.setTextSize(TvApplication.sTvSelectorTextSize);
		mIndex.setTextColor(color.md_grey_700);
		setBackgroundResource(R.drawable.recchan_bg);
		if(playing){
			frameChannelNumberBg.setBackgroundResource(R.drawable.ic_channel_current_normal);
		}else{
			frameChannelNumberBg.setBackgroundResource(R.drawable.ic_channel_normal);
			//mIndex.setTextSize(TvApplication.sTvSelectorTextSize);
		}
	}
	
	public void setViewNotFocus() {
		
		mName.setTextSize(TvApplication.sTvSelectorTextSize);

		if(playing){
			frameChannelNumberBg.setBackgroundResource(R.drawable.ic_category_normal);
		}else{
			mIndex.setTextSize(TvApplication.sTvSelectorTextSize);
		}
		setBackgroundResource(R.drawable.recchan_bg);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mIndex = (ArialBlackTextView) findViewById(R.id.channel_user_channelid);
		mIndex.setTextSize(TvApplication.sTvSelectorTextSize);
		frameChannelNumberBg =(FrameLayout)findViewById(R.id.frame_channel_number_bg);
		mName = (TextViewDip) findViewById(R.id.name);
		mName.setTextSize(TvApplication.sTvSelectorTextSize);
	}

}

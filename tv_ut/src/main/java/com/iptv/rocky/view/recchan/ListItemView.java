package com.iptv.rocky.view.recchan;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

import com.iptv.common.data.RecChan;
import com.iptv.rocky.base.RecChanBaseListItemView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class ListItemView extends RecChanBaseListItemView {

	private RecChan mRecChan;
	private TextViewDip mIndex;
	private TextViewDip mName;

	public ListItemView(Context context) {
		this(context, null, 0);
	}

	public ListItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		isRelection = true;
		ListView.LayoutParams params = new ListView.LayoutParams(
				ScreenUtils.getRecChanWidth(), ScreenUtils.getRecChanListItemHeight());
		setLayoutParams(params);
	}

	public void initView(RecChan info) {
		mRecChan = info;
		
		mIndex.setText(mRecChan.CHANNELINDEX);
		mName.setText(mRecChan.CHANNELNAME);
	}

	@Override
	public boolean isNotTopPadding() {
		return true;
	}

	@Override
	public void processFocus(boolean hasFocus) {
		super.processFocus(hasFocus);

//		if (hasFocus) {
//			mFloatLayer.displaySubTitle();
//			mFloatLayer.startMarquee();
//		} else {
//			mFloatLayer.hideSubTitle();
//			mFloatLayer.stopMarquee();
//		}
	}

	@Override
	public void onClick(Context context) {
//		Intent intent = new Intent(context, VodChannelDetailActivity.class);
//		intent.putExtra(Constants.cDetailIdExtra, mVodInfo.VODID);
//		context.startActivity(intent);
	}
	
	public void setViewEnlarge() {
		mIndex.setTextSize(TvApplication.sTvSelectorTextSize+2);
		mName.setTextSize(TvApplication.sTvSelectorTextSize+2);
		mIndex.setTextColor(Color.WHITE);
		mName.setTextColor(Color.WHITE);
		setBackgroundResource(R.drawable.detail_summery_text_bg);
	}
	
	public void setViewNormal() {
		mIndex.setTextSize(TvApplication.sTvSelectorTextSize);
		mName.setTextSize(TvApplication.sTvSelectorTextSize);
		mIndex.setTextColor(Color.WHITE);
		mName.setTextColor(Color.WHITE);
		setBackgroundResource(R.drawable.recchan_bg);
	}
	
	public void setViewNotFocus() {
		mIndex.setTextSize(TvApplication.sTvSelectorTextSize);
		mName.setTextSize(TvApplication.sTvSelectorTextSize);
		mIndex.setTextColor(Color.BLUE);
		mName.setTextColor(Color.BLUE);
		setBackgroundResource(R.drawable.recchan_bg);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mIndex = (TextViewDip) findViewById(R.id.index);
		mIndex.setTextSize(TvApplication.sTvSelectorTextSize);
		mName = (TextViewDip) findViewById(R.id.name);
		mName.setTextSize(TvApplication.sTvSelectorTextSize);
	}

}

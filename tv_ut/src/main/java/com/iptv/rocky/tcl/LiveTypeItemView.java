package com.iptv.rocky.tcl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.iptv.common.data.PortalLiveType;
import com.iptv.rocky.base.RecChanBaseListItemView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class LiveTypeItemView extends RecChanBaseListItemView {

	private PortalLiveType mPortalLiveType;
	private TextViewDip mName;

	public LiveTypeItemView(Context context) {
		this(context, null, 0);
	}

	public LiveTypeItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LiveTypeItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		isRelection = true;
		ListView.LayoutParams params = new ListView.LayoutParams(
				ScreenUtils.getRecChanWidth(), ScreenUtils.getRecChanListItemHeight());
		setLayoutParams(params);
	}

	public void initView(PortalLiveType info) {
		mPortalLiveType = info;
		
		mName.setText(info.title);
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
	
	public void setViewEnlarge()
	{
		mName.setTextSize(TvApplication.sTvSelectorTextSize+2);
		setBackgroundResource(R.drawable.detail_summery_text_bg);
	}
	
	public void setViewNormal()
	{
		mName.setTextSize(TvApplication.sTvSelectorTextSize);
		setBackgroundResource(R.drawable.tv_tab_default);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mName = (TextViewDip) findViewById(R.id.name);
		mName.setTextSize(TvApplication.sTvSelectorTextSize);
	}

}

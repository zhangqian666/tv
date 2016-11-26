package com.iptv.rocky.view.voddetail;

import com.iptv.common.data.VodDetailInfo;
import com.iptv.rocky.R;
import com.iptv.rocky.model.TvApplication;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class DetailMainView extends RelativeLayout {

	private DetailMainMetroView metroView;
	private DetailMainVod video;
	private DetailSummeryView summeryView;

	private VodDetailInfo info;

	public DetailMainView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DetailMainView(Context context) {
		this(context, null);
	}

	public void initView(VodDetailInfo info) {
		this.info = info;
		metroView.setData(info);
		fillData();
	}

	public void destory() {
		if (metroView != null) {
			metroView.destroy();
		}
	}

	public void restart() {
		if (metroView != null) {
			metroView.restart();
		}
	}

	private void fillData() {
		summeryView.createView(info);
		video.video_bg.setImageUrl(info.PICPATH);
		
		String chaseSubTitle = "";
		if (info.ISSITCOM == 1) {
			if (info.SITCOMNUM == info.SUBVODIDLIST.size())
				chaseSubTitle = String.format(getContext().getString(R.string.detail_all), info.SITCOMNUM+"");
			else
				chaseSubTitle = String.format(getContext().getString(R.string.detail_update), info.SUBVODIDLIST.size()+"");
		}
		//chaseSubTitle = TvUtils.createSubTitle(info.getType(), info.getVsValue(), info.getVsTitle(), getContext());
		video.floatLayerView.initView(info.VODNAME, chaseSubTitle, true);
	}

	@Override
	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {
		if (metroView != null && metroView.getChildCount() > 0) {
			return metroView.getChildAt(0).requestFocus();
		} else {
			return super.onRequestFocusInDescendants(direction,
					previouslyFocusedRect);
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		metroView = (DetailMainMetroView) findViewById(R.id.detail_main_metro);
		metroView.bringToFront();
		metroView.isAutoFocus = false;
		video = (DetailMainVod) findViewById(R.id.detail_main_video);
		setVideoLayout();

		summeryView = (DetailSummeryView) findViewById(R.id.summer_container);
		setSummeryLayout();
	}

	private void setVideoLayout() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				(int) (TvApplication.sTvChannelUnit * 1.3),
				TvApplication.sTvChannelUnit * 2);
		lp.topMargin = TvApplication.sTvTabHeight;
		lp.leftMargin = 5 + TvApplication.sTvLeftMargin
				+ (int) (0.8 * TvApplication.sTvChannelUnit);
		video.setVideoRectLayout(lp);
	}

	private void setSummeryLayout() {
		RelativeLayout.LayoutParams lp = (LayoutParams) summeryView.getLayoutParams();
		lp.topMargin = TvApplication.sTvTabHeight;
		lp.leftMargin = 5;
		lp.height = TvApplication.sTvChannelUnit * 2;
	}
	


}

package com.iptv.rocky.view.voddetail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.view.FloatLayerView;
import com.iptv.rocky.R;

public class DetailMainVod extends RelativeLayout {
	
	private View viewRoot;
	
	public AsyncImageView video_bg;
	public FloatLayerView floatLayerView;	
	
	public DetailMainVod(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public DetailMainVod(Context context) {
		this(context, null);
	}

	private void init(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		viewRoot = inflater.inflate(R.layout.vod_detail_main_video_rect, null);
		
		video_bg = (AsyncImageView) viewRoot.findViewById(R.id.video_bg);
		floatLayerView = (FloatLayerView) viewRoot.findViewById(R.id.tv_floatlayer);
		floatLayerView.startMarquee();
		addView(viewRoot);
	}
	
	public void setVideoRectLayout(RelativeLayout.LayoutParams lp) {
		if (viewRoot != null) {
			viewRoot.setLayoutParams(lp);
		}
	}
}

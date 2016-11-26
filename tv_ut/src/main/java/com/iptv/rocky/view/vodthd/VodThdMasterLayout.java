package com.iptv.rocky.view.vodthd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iptv.rocky.view.TitleView;
import com.iptv.rocky.R;

public class VodThdMasterLayout extends RelativeLayout {

	private TitleView mTitleView;
	private ProgressBar mProgressbar;
	private VodThdGallery mGallery;
	
	public VodThdMasterLayout(Context context) {
		this(context, null, 0);
	}
	
	public VodThdMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public VodThdMasterLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void destroy() {
		mGallery.destroy();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mTitleView = (TitleView) findViewById(R.id.vod_titleview);
		mProgressbar = (ProgressBar) findViewById(R.id.tv_progressbar);
		mGallery = (VodThdGallery) findViewById(R.id.vodthd_gallery);
			
		mProgressbar.setVisibility(View.VISIBLE);
		
		requestChildFocus(mGallery, mGallery.findFocus());
	}
	
	public void createView(String id, String name) {
		mTitleView.setText(name);
		
		mGallery.crateView(id, mProgressbar);
	}

}

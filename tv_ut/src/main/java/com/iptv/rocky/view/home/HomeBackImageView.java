package com.iptv.rocky.view.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.HomeBackImageData;
import com.iptv.rocky.view.FloatLayerView;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.iptv.rocky.R;

public class HomeBackImageView extends RelativeLayout 
	implements AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener {

	private String mTitle;
	private HomeBackImageData mImageData;
	private AsyncImageView mBackImageView;
	private FloatLayerView mFloatLayer;
	
	public HomeBackImageView(Context context) {
		this(context, null, 0);
	}
	
	public HomeBackImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HomeBackImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void initView(HomeBackImageData data) {
		if (!data.isNotTopPadding) {
			setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);
		}
		
		mImageData = data;
		mBackImageView.setImageUrl(data.pageItem.background);
		mTitle = data.pageItem.title;
		if (!TextUtils.isEmpty(mTitle)) {
			mFloatLayer.initView(mTitle);
		}
	}
	
	public void onOwnerFocusChange(boolean hasFocus) {
		if (hasFocus && !TextUtils.isEmpty(mTitle)) {
			mFloatLayer.setVisibility(View.VISIBLE);
			mFloatLayer.startMarquee();
		} else {
			mFloatLayer.setVisibility(View.GONE);
			mFloatLayer.stopMarquee();
		}
	}

	@Override
	public void onLoadComplete(String imageUri, View view, Bitmap loadedImage) {
		mImageData.isViewLoaded = true;
	}

	@Override
	public void onLoadFailed(String imageUri, View view, FailType failType) {
		mImageData.isViewLoaded = true;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mBackImageView = (AsyncImageView)findViewById(R.id.home_backimage_img);
		mFloatLayer = (FloatLayerView)findViewById(R.id.tv_floatlayer);
		
		mFloatLayer.setVisibility(View.GONE);
		mBackImageView.setImageLoadedListener(this);
		mBackImageView.setImageFailedListener(this);
	}
}

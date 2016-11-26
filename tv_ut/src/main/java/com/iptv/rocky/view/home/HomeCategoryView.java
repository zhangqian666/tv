package com.iptv.rocky.view.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.HomeCategoryData;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.iptv.rocky.R;

public class HomeCategoryView extends RelativeLayout 
	implements AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener {

	private View mLayoutView;
	private TextView mTitleView;
	private AsyncImageView mBackgroundView;
	private AsyncImageView mIconView;
	private HomeCategoryData mItemData;
	
	public HomeCategoryView(Context context) {
		this(context, null, 0);
	}
	
	public HomeCategoryView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HomeCategoryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void initView(HomeCategoryData data) {
		if (!data.isNotTopPadding) {
			setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);	
		}
		
		mItemData = data;
		mTitleView.setText(data.pageItem.title);
		mBackgroundView.setImageUrl(data.pageItem.background);
		mIconView.setImageUrl(data.pageItem.icon);
	}

	@Override
	public void onLoadComplete(String imageUri, View view, Bitmap loadedImage) {
		mItemData.isViewLoaded = true;
	}

	@Override
	public void onLoadFailed(String imageUri, View view, FailType failType) {
		mItemData.isViewLoaded = true;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mLayoutView = findViewById(R.id.home_category_layout);
		mTitleView = (TextView)findViewById(R.id.home_category_title);
		// 文字大小和homecommview一样
		mTitleView.setTextSize(TvApplication.sTvHomeCommViewTextSize);
		mBackgroundView = (AsyncImageView)findViewById(R.id.home_category_img);
		mIconView = (AsyncImageView)findViewById(R.id.home_category_icon);

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mLayoutView.getLayoutParams();
		params.height = TvApplication.sTvChannelUnit / 3;
		
		int padding = (int)(TvApplication.sTvMasterTextSize / 3.0);
		mTitleView.setPadding(padding, padding, padding, padding);
		mIconView.setPadding(padding, padding, padding, padding);
        
        mBackgroundView.setImageLoadedListener(this);
        mBackgroundView.setImageFailedListener(this);
	}

}

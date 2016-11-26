package com.iptv.rocky.view.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.HomeLiveData;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.iptv.rocky.R;

public class HomeLiveView extends RelativeLayout 
	implements AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener {

//  private Context mContext;
//	private View mRootView;
	private AsyncImageView mImageView;
	private AsyncImageView mBackImageView;
	private TextView mTextView;

    private HomeLiveData mData;

    public HomeLiveView(Context context) {
		this(context, null, 0);
	}
	
	public HomeLiveView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HomeLiveView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//      mContext = context;
    }
	
	public void initView(HomeLiveData data) {
		mData = data;
		if (!data.isNotTopPadding) {
			setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);
		}
		mImageView.setImageUrl(data.pageItem.icon);
		mBackImageView.setImageUrl(data.pageItem.background);
		mTextView.setText(data.pageItem.title);
		mTextView.getPaint().setFakeBoldText(data.isFontBlod);
//		int resId = AppCommonUtils.getResourceIdByName(mContext, data.pageItem.background);
//		if (resId < 0) {
//			resId = AppCommonUtils.getResourceIdByName(mContext, AtvUtils.getMetroItemBackground(0));
//		}
//		mRootView.setBackgroundResource(resId);
	}

	@Override
	public void onLoadComplete(String imageUri, View view, Bitmap loadedImage) {
		mData.isViewLoaded = true;
	}

	@Override
	public void onLoadFailed(String imageUri, View view, FailType failType) {
		mData.isViewLoaded = true;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		//mRootView = findViewById(R.id.home_common_root);
		mImageView = (AsyncImageView)findViewById(R.id.home_common_img);
		mTextView = (TextView)findViewById(R.id.home_common_txt);
		mTextView.setTextSize(TvApplication.sTvHomeCommViewTextSize);
		
		mBackImageView = (AsyncImageView)findViewById(R.id.home_backimage_img);
		mBackImageView.setImageLoadedListener(this);
		mBackImageView.setImageFailedListener(this);
		
		mImageView.setImageLoadedListener(this);
		mImageView.setImageFailedListener(this);
		
        int padding = (int)(TvApplication.pixelHeight / 38.0);
		mImageView.setPadding(0, 0, 0, padding);
		mTextView.setPadding(0, 0, 0, padding);
	}

}

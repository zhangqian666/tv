package com.iptv.rocky.view.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.common.utils.AtvUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.R;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.HomeCommonData;
import com.iptv.rocky.utils.AppCommonUtils;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;


public class HomeCommonView extends RelativeLayout 
	implements AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener {

	private View mRootView;
	private AsyncImageView mImageView;
	private TextView mTextView;
    private Context mContext;
    private HomeCommonData mData;

    public HomeCommonView(Context context) {
		this(context, null, 0);
	}
	
	public HomeCommonView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HomeCommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        mContext = context;
    }
	
	public void initView(HomeCommonData data) {
		mData = data;
		if (!data.isNotTopPadding) {
			setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);
		}
		mImageView.setImageUrl(data.pageItem.icon);
		mTextView.setText(data.pageItem.title);
		mTextView.getPaint().setFakeBoldText(data.isFontBlod);
		
		int resId = AppCommonUtils.getResourceIdByName(mContext, data.pageItem.background);
//		LogUtils.debug("data.pageItem.background:"+data.pageItem.background +" resId:"+resId);
		
		if (resId < 0) {
			resId = AppCommonUtils.getResourceIdByName(mContext, AtvUtils.getMetroItemBackground(0));
		}
		mRootView.setBackgroundResource(resId);
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
		
		mRootView = findViewById(R.id.home_common_root);
		mImageView = (AsyncImageView)findViewById(R.id.home_common_img);
		mTextView = (TextView)findViewById(R.id.home_common_txt);
		mTextView.setTextSize(TvApplication.sTvHomeCommViewTextSize);
		
		mImageView.setImageLoadedListener(this);
		mImageView.setImageFailedListener(this);
		
        int padding = (int)(TvApplication.pixelHeight / 38.0);
		mImageView.setPadding(0, 0, 0, padding);
		mTextView.setPadding(0, 0, 0, padding);
	}

}

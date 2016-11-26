package com.iptv.rocky.view.live;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.model.live.LiveTypeCommonData;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.iptv.rocky.R;

public class LiveTypeCommonView extends RelativeLayout implements AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener {

	//private View mRootView;
	private AsyncImageView mImageView;
	private TextView mTextView;
	private TextView mBill;
    private Context mContext;
    private LiveTypeCommonData mData;

    public LiveTypeCommonView(Context context) {
		this(context, null, 0);
	}
	
	public LiveTypeCommonView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public LiveTypeCommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        mContext = context;
    }
	
	public void initView(LiveTypeCommonData data) {
		mData = data;
		if (!data.isNotTopPadding) {
			//setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);
			setPadding(0, 0, 0, 0);
		}
		mImageView.setImageUrl(data.pageItem.icon);
		mTextView.setText(data.pageItem.title);
		//mTextView.getPaint().setFakeBoldText(data.isFontBlod);
		mBill.setText(data.pageItem.bill);
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
		
		//mRootView = findViewById(R.id.live_common_root);
		mImageView = (AsyncImageView)findViewById(R.id.live_common_img);
		
		mTextView = (TextView)findViewById(R.id.live_common_txt);
//		mTextView.setTextSize(TvApplication.sTvHomeCommViewTextSize);
		
		mBill = (TextView)findViewById(R.id.live_bill_txt);
//		mBill.setTextSize(TvApplication.sTvHomeCommViewTextSize);
		
		mImageView.setImageLoadedListener(this);
		mImageView.setImageFailedListener(this);
		
//      int padding = (int)(TvApplication.pixelHeight / 38.0);
//		mImageView.setPadding(0, 0, 0, padding);
//		mTextView.setPadding(0, 0, 0, padding);
//		mBill.setPadding(0, 0, 0, padding);
	}

}

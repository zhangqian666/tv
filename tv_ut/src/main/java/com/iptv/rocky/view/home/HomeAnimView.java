package com.iptv.rocky.view.home;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.HomeAnimData;
import com.iptv.rocky.view.FloatLayerView;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.iptv.rocky.R;

public class HomeAnimView extends RelativeLayout {
	
	private int mViewLoadedFlag;
	
	private View mLayoutView;
	private AsyncImageView mBackImage;
	private AsyncImageView mImageView1;
	private AsyncImageView mImageView2;
	private FloatLayerView mFloatLayer;
	private HomeAnimData mAnimData;
	
	private View mDefaultImage;
	private ObjectAnimator mAnimator1;
	private ObjectAnimator mAnimator2;
	private ObjectAnimator mAnimator3;
	private ObjectAnimator mAnimator4;

	public HomeAnimView(Context context) {
		this(context, null, 0);
	}
	
	public HomeAnimView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HomeAnimView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setChildrenDrawingOrderEnabled(true);
	}
	
	public void initView(HomeAnimData data) {
		if (!data.isNotTopPadding) {
			mLayoutView.setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mDefaultImage.getLayoutParams();
			params.setMargins(0, TvApplication.sTvItemTopPadding, 0, 0);
		}
		mAnimData = data;
		
		if (data.pageItem.images != null) {
			int size = data.pageItem.images.size();
			if (size > 0) {
				mImageView1.setImageUrl(data.pageItem.images.get(0));
			} else {
				mImageView1.setImageUrl(null);
			}
			if (size > 1) {
				mImageView2.setImageUrl(data.pageItem.images.get(1));
			} else {
				mImageView2.setImageUrl(null);
			} 
		} else {
			mImageView1.setImageUrl(null);
			mImageView2.setImageUrl(null);
		}
		
		String title = data.pageItem.title;
		if (!TextUtils.isEmpty(title)) {
			mFloatLayer.initView(title);
		}
		mBackImage.setImageUrl(data.pageItem.background);
	}
	
	public void onOwnerFocusChange(boolean hasFocus) {
		mAnimator1.cancel();
		mAnimator2.cancel();
		mAnimator3.cancel();
		mAnimator4.cancel();
		if (hasFocus) {
			mAnimator1.start();
			mAnimator2.start();
			if (!TextUtils.isEmpty(mAnimData.pageItem.title)) {
				mFloatLayer.setVisibility(View.VISIBLE);
				mFloatLayer.startMarquee();
			}
		} else {
			mAnimator3.start();
			mAnimator4.start();
			mFloatLayer.setVisibility(View.INVISIBLE);
			mFloatLayer.stopMarquee();
		}
	}

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
    	int img1Index = indexOfChild(mImageView1);
    	int img2Index = indexOfChild(mImageView2);
    	
    	if (i == img1Index){
    		return childCount - 1;
    	} 
    	
    	if (i == img2Index) {
    		return childCount - 3;
    	}
    	
    	if (i == childCount - 1) {
    		return childCount - 2;
    	}
    	
    	if (i == childCount - 2) {
    		return img1Index;
    	}
    	
    	if (i == childCount - 3) {
    		return img2Index;
    	}
    	
    	return i;
    }

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mLayoutView = findViewById(R.id.home_anim_layout);
		mDefaultImage = findViewById(R.id.tv_default_img);
		mBackImage = (AsyncImageView)findViewById(R.id.home_anim_backimg);
		mImageView1 = (AsyncImageView)findViewById(R.id.home_anim_img1);
		mImageView2 = (AsyncImageView)findViewById(R.id.home_anim_img2);
		mFloatLayer = (FloatLayerView)findViewById(R.id.tv_floatlayer);
		mFloatLayer.setVisibility(View.GONE);

		mBackImage.setImageLoadedListener(new AsyncImageLoadedListener(1));
		mImageView1.setImageLoadedListener(new AsyncImageLoadedListener(2));
		mImageView2.setImageLoadedListener(new AsyncImageLoadedListener(4));
		mBackImage.setImageFailedListener(new AsyncImageFailedListener(1));
		mImageView1.setImageFailedListener(new AsyncImageFailedListener(2));
		mImageView2.setImageFailedListener(new AsyncImageFailedListener(4));

		mAnimator1 = (ObjectAnimator)AnimatorInflater.loadAnimator(getContext(), R.animator.home_translate_vertical);
		mAnimator2 = (ObjectAnimator)AnimatorInflater.loadAnimator(getContext(), R.animator.home_translate_horizontal);
		mAnimator3 = (ObjectAnimator)AnimatorInflater.loadAnimator(getContext(), R.animator.home_translate_vertical_reset);
		mAnimator4 = (ObjectAnimator)AnimatorInflater.loadAnimator(getContext(), R.animator.home_translate_horizontal_reset);
		mAnimator1.setTarget(mImageView1);
		mAnimator2.setTarget(mImageView2);
		mAnimator3.setTarget(mImageView1);
		mAnimator4.setTarget(mImageView2);
		
		RelativeLayout.LayoutParams mImageView1_lp = (LayoutParams) mImageView1.getLayoutParams();
		mImageView1_lp.setMargins(1, 1, 1, 1);
		RelativeLayout.LayoutParams mImageView2_lp = (LayoutParams) mImageView2.getLayoutParams();
		mImageView2_lp.setMargins(1, 1, 1, 1);
		RelativeLayout.LayoutParams mFloatLayer_lp = (LayoutParams) mFloatLayer.getLayoutParams();
		mFloatLayer_lp.setMargins(1, 1, 1, 1);
	}
	
	private class AsyncImageLoadedListener  
		implements AsyncImageView.AsyncImageLoadedListener {

		private int mFlag;
		
		public AsyncImageLoadedListener(int flag) {
			mFlag = flag;
		}
		
		@Override
		public void onLoadComplete(String imageUri, View view, Bitmap loadedImage) {
			mViewLoadedFlag |= mFlag;
			if (mViewLoadedFlag == 7) {
				mAnimData.isViewLoaded = true;
			}
		}
	}
	
	private class AsyncImageFailedListener 
		implements AsyncImageView.AsyncImageFailListener {

		private int mFlag;
		
		public AsyncImageFailedListener(int flag) {
			mFlag = flag;
		}
		
		@Override
		public void onLoadFailed(String imageUri, View view, FailType failType) {
			mViewLoadedFlag |= mFlag;
			if (mViewLoadedFlag == 7) {
				mAnimData.isViewLoaded = true;
			}
		}
	}
	
}

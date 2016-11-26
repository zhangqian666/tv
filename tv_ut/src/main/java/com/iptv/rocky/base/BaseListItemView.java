package com.iptv.rocky.base;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.iptv.common.utils.Constants;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.R;

public abstract class BaseListItemView extends RelativeLayout {

	public boolean isRelection;
	public boolean isViewLoaded;
	
	private int mMeasuredWidth;
	private int mMeasuredHeight;
	private int mReflectionHeight;
	
	private boolean mIsFocused;
	
	private Paint mPaint;
	private Matrix mFlipMatrix;
   	private ImageView mWhiteBorder;
   	private Bitmap mFadedBitmap;
   	private AnimatorSet mZoomInAnimation;
    private AnimatorSet mZoomOutAnimation;
	
	private static int sPadding = -1;
	
	public BaseListItemView(Context context) {
		this(context, null, 0);
	}
	
	public BaseListItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BaseListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		//sPadding = TvApplication.sTvItemPadding;
		if (sPadding < 0) {
			sPadding = context.getResources().getDimensionPixelSize(R.dimen.home_item_padding);
		}
		
		setClickable(true);
	    setFocusable(true);
	    setFocusableInTouchMode(true);
		setWillNotDraw(false);
		setPadding(sPadding, sPadding, sPadding, sPadding);
		
		mPaint = new Paint();
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		
		mFlipMatrix = new Matrix();
		mFlipMatrix.preScale(1, -1);
		
		mZoomInAnimation = (AnimatorSet)AnimatorInflater.loadAnimator(context, R.animator.tv_zoom_in);
	    mZoomOutAnimation = (AnimatorSet)AnimatorInflater.loadAnimator(context, R.animator.tv_zoom_out);
	    mZoomInAnimation.setTarget(this);
	    mZoomOutAnimation.setTarget(this);
	}
	
	public void destroy() {
		if (mFadedBitmap != null && !mFadedBitmap.isRecycled()) {
			mFadedBitmap.recycle();
		}
		mFadedBitmap = null;
		isViewLoaded = false;
		mWhiteBorder.setVisibility(View.INVISIBLE);
	}
	
	public int getReflectionHeight() {
		return mReflectionHeight;
	}
	
	public abstract boolean isNotTopPadding();
	public abstract void onClick(Context context);

	@Override
	protected void onDraw(Canvas canvas) {
		if (isRelection && isViewLoaded) {
			createReflection(canvas);
		}
	}
	
	private void createReflection(Canvas canvas) {
		if (mFadedBitmap == null) {
			initFadedBitmap(canvas);
		} else {
			if (mFadedBitmap.isRecycled()) {
				destroy();
				isViewLoaded = true;
				initFadedBitmap(canvas);
			}
		}
		if (mIsFocused) {
			mWhiteBorder.setVisibility(View.VISIBLE);
		}
		
		canvas.drawBitmap(mFadedBitmap, 0, mMeasuredHeight, null);
	}
	
	private void initFadedBitmap(Canvas canvas) {
		mFadedBitmap = createFlippedBitmap(canvas);
		Canvas fadeCanvas = new Canvas(mFadedBitmap);
		if (mPaint.getShader() == null) {
			mPaint.setShader(new LinearGradient(0, 0, 0, mReflectionHeight, Constants.cReflectionColor, 0x00FFFFFF, TileMode.CLAMP));
		}
		fadeCanvas.drawRect(0, 0, mMeasuredWidth, mReflectionHeight, mPaint);
	}
	
	private Bitmap createFlippedBitmap(Canvas canvas) {
		int width = mMeasuredWidth;
		int height = mMeasuredHeight;
		Bitmap sourceBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		Canvas tempCanvas = new Canvas(sourceBitmap);
		
		View[] children = getDrawChildren();
		if (children == null) {
			dispatchDraw(tempCanvas);
		} else {
			long drawingTime = getDrawingTime();
			for (int i = 0, length = children.length; i < length; i++) {
				drawChild(tempCanvas, children[i], drawingTime);
			}
		}
		
		Bitmap flippedBitmap = Bitmap.createBitmap(sourceBitmap, 
				0, height - mReflectionHeight, width, mReflectionHeight, mFlipMatrix, false);
		sourceBitmap.recycle();
		return flippedBitmap;
	}
	
	protected View[] getDrawChildren() {
		return null;
	}
	
	@Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
        
        if (isRelection) {
        	mMeasuredWidth = getMeasuredWidth();
            mMeasuredHeight = getMeasuredHeight();
            if (mMeasuredHeight < TvApplication.sTvReflectionHeight) {
            	mReflectionHeight = mMeasuredHeight / 2; 
            } else {
            	mReflectionHeight = TvApplication.sTvReflectionHeight;
            }
        	setMeasuredDimension(mMeasuredWidth, mMeasuredHeight + mReflectionHeight);
        } 
    }
	
	@Override
	public boolean performClick() {
		onClick(getContext());
		return true;
	}
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	if (mIsFocused) {
    		switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            	return performClick();
        	}
    	}
    	return super.onTouchEvent(event);
	}

	public void processFocus(boolean hasFocus) {
		mIsFocused = hasFocus;
		clearAnimation();
    	if (hasFocus) {
    		mZoomOutAnimation.cancel();
    		mZoomInAnimation.start();
    		if (!isRelection) {
    			mWhiteBorder.setVisibility(View.VISIBLE);
    		}
    	} else {
    		mZoomInAnimation.cancel();
    		mZoomOutAnimation.start();
    		mWhiteBorder.setVisibility(View.INVISIBLE);
    	}
    }
    
    protected RelativeLayout.LayoutParams getBorderLayoutParam() {
    	return new RelativeLayout.LayoutParams(ScreenUtils.getChannelWidth(), ScreenUtils.getChannelHeight());
    };
    
    @Override
    protected void onFinishInflate() {
    	mWhiteBorder = new ImageView(getContext());
    	mWhiteBorder.setVisibility(View.INVISIBLE);
		mWhiteBorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_white_border));
		//addView(mWhiteBorder, getBorderLayoutParam());
		addViewInLayout(mWhiteBorder, -1, getBorderLayoutParam(), true);
    }

}

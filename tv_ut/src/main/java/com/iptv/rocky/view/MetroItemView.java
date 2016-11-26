package com.iptv.rocky.view;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.iptv.common.utils.Constants;
import com.iptv.common.utils.DataReloadUtil;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.base.BaseMetroItemView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class MetroItemView extends FrameLayout
	implements View.OnFocusChangeListener, TVItemViewReloadable {

	public boolean isRelection;
	
	private int mMeasuredWidth;
	private int mMeasuredHeight;
	private int mReflectionHeight;
	
	private Context mContext;
    private ImageView mWhiteBorder;
	    
	private BaseMetroItemView mItemView;
	private Matrix mFlipMatrix;
   	private Paint mPaint;
   	private Bitmap mFadedBitmap;
    private AnimatorSet mZoomInAnimation;
    private AnimatorSet mZoomOutAnimation;
    
    public MetroItemView(Context context) {
		this(context, null, 0);
	}
	
	public MetroItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MetroItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
        mContext = context;
        //int padding = TvApplication.sTvItemPadding;
		
		setWillNotDraw(false);
		//setPadding(padding, padding, padding, padding);
		
		mPaint = new Paint();
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		
		mFlipMatrix = new Matrix();
		mFlipMatrix.preScale(1, -1);
		
		mWhiteBorder = new ImageView(context);
		mWhiteBorder.setVisibility(View.INVISIBLE);
		mWhiteBorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_white_border));
		
		mZoomInAnimation = (AnimatorSet)AnimatorInflater.loadAnimator(context, R.animator.tv_zoom_in);
		mZoomOutAnimation = (AnimatorSet)AnimatorInflater.loadAnimator(context, R.animator.tv_zoom_out);
		mZoomInAnimation.setTarget(this);
		mZoomOutAnimation.setTarget(this);
	}
	
	public void addWhiteBorder(RelativeLayout.LayoutParams lp){
		ViewGroup viewGroup = (ViewGroup) mItemView.getView(mContext);
		viewGroup.addView(mWhiteBorder, lp);
	}
	
	public void addMetroItem(BaseMetroItemData itemData) {
		mItemView = new BaseMetroItemView(mContext);
		mItemView.addMetroItem(itemData);
		mItemView.setOnFocusChangeListener(this); 
		addView(mItemView, getMetroItemLayoutParams());
	}
	
	protected FrameLayout.LayoutParams getMetroItemLayoutParams(){
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		int margin = TvApplication.sTvItemPadding;
		lp.setMargins(margin, margin, margin, margin);
		return lp;
	}
	
	public void destroy() {
		if (mFadedBitmap != null && !mFadedBitmap.isRecycled()) {
			mFadedBitmap.recycle();
		}
		mFadedBitmap = null;
		mItemView.setViewLoaded(false);
		mWhiteBorder.setVisibility(View.INVISIBLE);
	}
	
	public boolean isNotTopPadding() {
		return mItemView.isNotTopPadding();
	}
	
	public View getView(Context context) {
		return mItemView.getView(context);
	}
	
	public int getReflectionHeight() {
		return mReflectionHeight;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isRelection && mItemView.isViewLoaded()) {
			createReflection(canvas);
		}
	}
	
	private void createReflection(Canvas canvas) {
		if (mFadedBitmap == null) {
			initFadedBitmap(canvas);
		} else {
			if (mFadedBitmap.isRecycled()) {
				destroy();
				mItemView.setViewLoaded(true);
				initFadedBitmap(canvas);
			}
		}
		if (mItemView.hasFocus()) {
			mWhiteBorder.setVisibility(View.VISIBLE);
		}

		canvas.drawBitmap(mFadedBitmap, 0, mMeasuredHeight, null);
	}
	
	private void initFadedBitmap(Canvas canvas) {
		mFadedBitmap = createFlippedBitmap(canvas);
		Canvas tempCanvas = new Canvas(mFadedBitmap);
		if (mPaint.getShader() == null) {
			mPaint.setShader(new LinearGradient(0, 0, 0, mReflectionHeight, Constants.cReflectionColor, 0x00FFFFFF, TileMode.CLAMP));
		}
		tempCanvas.drawRect(0, 0, mMeasuredWidth, mReflectionHeight, mPaint);
	}
	
	private Bitmap createFlippedBitmap(Canvas canvas) {
		int width = mMeasuredWidth;
		int height = mMeasuredHeight;
		Bitmap sourceBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		Canvas tempCanvas = new Canvas(sourceBitmap);
		dispatchDraw(tempCanvas);
		Bitmap flippedBitmap = Bitmap.createBitmap(sourceBitmap, 
				0, height - mReflectionHeight, width, mReflectionHeight, mFlipMatrix, false);
		sourceBitmap.recycle();
		return flippedBitmap;
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
	public void onFocusChange(View v, boolean hasFocus) {
		clearAnimation();
		if (hasFocus) {
			mZoomOutAnimation.cancel();
			mZoomInAnimation.start();
			if (!isRelection || 
				(isRelection && mFadedBitmap != null && mItemView.isViewLoaded())) {
				mWhiteBorder.setVisibility(View.VISIBLE);
			} 		
			mItemView.onFocusChange(true);
		} else {
			mZoomInAnimation.cancel();
			mZoomOutAnimation.start();
			mWhiteBorder.setVisibility(View.INVISIBLE);
			mItemView.onFocusChange(false);
		}
	}

    @Override
    public void reloadData() {
    	destroy();
    	View view = mItemView.getView(mContext);
    	if (view instanceof TVItemViewReloadable) {
			((TVItemViewReloadable) view).reloadData();
		}
        //((TVItemViewReloadable) mItemView.getView(mContext)).reloadData();
    }
}

package com.iptv.rocky.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class ScaleAsyncImageView extends AsyncImageView {

	public ScaleAsyncImageView(Context context) {
		this(context, null, 0);
	}
	
	public ScaleAsyncImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ScaleAsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setAdjustViewBounds(true);
		
		TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.TvImageView, defStyle, 0);
		
		boolean isScaleWidth = a.getBoolean(
				R.styleable.TvImageView_isScaleWidth, true);
		
		float sacleHeight = a.getDimension(
                R.styleable.TvImageView_scaleHeight, 6.0f);
		
		if (isScaleWidth) {
			float sacleWidth = a.getDimension(
	                R.styleable.TvImageView_scaleWidth, 6.0f);
			setMaxWidth(setSize(sacleWidth));
		}
		setMaxHeight(setSize(sacleHeight));
		
		String src = a.getString(R.styleable.TvImageView_src);
		if (!TextUtils.isEmpty(src)) {
			setImageUrl(src);
		}
		
		a.recycle();
	}

	private int setSize(float source) {
		return (int)(TvApplication.pixelHeight / source);
	}
}

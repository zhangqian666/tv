package com.iptv.rocky.view.detail;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.base.BaseMetroView;
import com.iptv.rocky.model.TvApplication;

/**
 * detail页 和 sports的选集通用此类
 * */
public class SelectNotNumberMetroView extends BaseMetroView {
	
	public SelectNotNumberMetroView(Context context) {
		this(context, null, 0);
	}
	
	public SelectNotNumberMetroView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SelectNotNumberMetroView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		int padding = TvApplication.sTvChannelUnit / 4;
		setPadding(padding, TvApplication.sTvTabHeight, padding, 0);
	}

	@Override
	protected int getColumenNumber() {
		return 4;
	}

	@Override
	protected int getItemUnitNumber() {
		return TvApplication.sTvChannelUnit;
	}

	@Override
	protected boolean isRelection(BaseMetroItemData itemData) {
		return false;
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
		clear();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), (int)TvApplication.pixelHeight);
	}

}

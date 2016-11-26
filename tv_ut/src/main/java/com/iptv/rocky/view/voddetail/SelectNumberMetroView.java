package com.iptv.rocky.view.voddetail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.base.BaseMetroView;
import com.iptv.rocky.model.TvApplication;

public class SelectNumberMetroView extends BaseMetroView {
	
	public SelectNumberMetroView(Context context) {
		this(context, null);
	}

	public SelectNumberMetroView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setPadding(0, TvApplication.sTvTabHeight, 0, 0);
		setGravity(Gravity.CENTER_HORIZONTAL);
	}

	@Override
	protected int getColumenNumber() {
		return 6;
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

}

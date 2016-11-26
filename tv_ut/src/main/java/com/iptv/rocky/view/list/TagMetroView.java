package com.iptv.rocky.view.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.base.BaseMetroView;
import com.iptv.rocky.utils.ScreenUtils;

public class TagMetroView extends BaseMetroView {

	private int mUnitNumber;
	
	public TagMetroView(Context context) {
		this(context, null, 0);
	}
	
	public TagMetroView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TagMetroView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		isAutoFocus = true;
		mUnitNumber = ScreenUtils.getListTagUnit();
		
		setPadding(0, 0, 0, 0);
		setGravity(Gravity.CENTER);
	}

	@Override
	protected int getColumenNumber() {
		return 5;
	}

	@Override
	protected int getItemUnitNumber() {
		return mUnitNumber;
	}

	@Override
	protected boolean isRelection(BaseMetroItemData itemData) {
		return false;
	}

}

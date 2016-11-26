package com.iptv.rocky.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.FrameLayout;

/**
 * this class process touch and focus
 *
 */
public class BaseMetroItemView extends FrameLayout {
	
	private BaseMetroItemData mItemData;
	
	public static final FrameLayout.LayoutParams sFillParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

	public BaseMetroItemView(Context context) {
		this(context, null, 0);
	}
	
	public BaseMetroItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BaseMetroItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setClickable(true);
	    setFocusable(true);
	    setFocusableInTouchMode(true);
	}
	
	public final void addMetroItem(BaseMetroItemData itemData) {
		removeAllViewsInLayout();
		mItemData = itemData;
	    addView(mItemData.getView(getContext()), sFillParams);
	}
	
	public final void onFocusChange(boolean hasFocus) {
		mItemData.onOwnerFocusChange(hasFocus);
	}
	
	public boolean isViewLoaded() {
		return mItemData.isViewLoaded;
	}
	
	public void setViewLoaded(boolean isViewLoaded) {
		mItemData.isViewLoaded = isViewLoaded;
	}
	
	public boolean isNotTopPadding() {
		return mItemData.isNotTopPadding;
	}
	
	public View getView(Context context) {
		return mItemData.getView(context);
	}
	
	@Override
	public boolean performClick() {
        playSoundEffect(SoundEffectConstants.CLICK);
		if (mItemData != null) {
			mItemData.onClick(getContext());
			return true;
		}
		return false;
	}
	
}

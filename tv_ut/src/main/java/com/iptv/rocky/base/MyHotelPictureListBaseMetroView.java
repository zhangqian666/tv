package com.iptv.rocky.base;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.LiveMetroItemView;

public abstract class MyHotelPictureListBaseMetroView extends RelativeLayout {

	public boolean isAutoFocus;
	
	private int mFocusIndex;
	
	private Point mCurrentPoint = new Point(0, 0);
	
	public MyHotelPictureListBaseMetroView(Context context) {
		this(context, null, 0);
	}
	
	public MyHotelPictureListBaseMetroView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MyHotelPictureListBaseMetroView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setFocusable(false);
		setFocusableInTouchMode(false);
		setChildrenDrawingOrderEnabled(true);
	    setClipToPadding(false);
	    setWillNotDraw(true);
	    
	    if (isTopPadding()) {
	    	setPadding(TvApplication.sTvLeftMargin, TvApplication.sTvTabHeight - TvApplication.sTvItemTopPadding, TvApplication.sTvLeftMargin, 0);
	    } else {
	    	setPadding(TvApplication.sTvLeftMargin, TvApplication.sTvTabHeight, TvApplication.sTvLeftMargin, 0);
	    }
	}
	
	public void addMetroItem(BaseMetroItemData itemData) {
		LiveMetroItemView itemView = new LiveMetroItemView(getContext());
		itemView.isRelection = isRelection(itemData);
		itemView.setLayoutParams(setItemPosition(itemData));
		itemView.addMetroItem(itemData);
		itemView.addWhiteBorder(setBorderPosition(itemData));
		addView(itemView);
		
		if (itemData.id > 0) {
			itemView.setId(itemData.id);
		}
		if (isAutoFocus && indexOfChild(itemView) == 0) {
			itemView.requestFocus();
		}
	}
	
	public void destroy() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			if (child instanceof LiveMetroItemView) {
				LiveMetroItemView itemView = (LiveMetroItemView)child;
				itemView.destroy();
			}
		}
	}
	
	public void clear() {
		removeAllViewsInLayout();
		mCurrentPoint.set(0, 0);
	}
	
	protected abstract int getColumenNumber();
	protected abstract int getItemUnitNumber();
	protected abstract boolean isRelection(BaseMetroItemData itemData);
	
	protected boolean isTopPadding() {
		return false;
	}
	
	protected MetroItemOrientation getMetroItemOrientation() {
		return MetroItemOrientation.HORIZONTAL;
	}
	
	protected RelativeLayout.LayoutParams setItemPosition(BaseMetroItemData itemData) {
		//int width = (int)(itemData.widthSpan * getItemUnitNumber());
		//int height = (int)(itemData.heightSpan * getItemUnitNumber());
		int width = (int)(itemData.widthSpan * ScreenUtils.getLiveItemWidth() * 1.5);
		int height = (int)(itemData.heightSpan * ScreenUtils.getVodMovieListItemHeight());
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.topMargin = mCurrentPoint.y * height;
		params.leftMargin = mCurrentPoint.x * width;
		
		if (MetroItemOrientation.HORIZONTAL.equals(getMetroItemOrientation())) {
			mCurrentPoint.x++;
			if (mCurrentPoint.x >= getColumenNumber()) {
				mCurrentPoint.x = 0;
				mCurrentPoint.y++;
			}
		} else {
			mCurrentPoint.y++;
			if (mCurrentPoint.y >= getColumenNumber()) {
				mCurrentPoint.y = 0;
				mCurrentPoint.x++;
			}
		}
		
		return params;
	}
	
	protected RelativeLayout.LayoutParams setBorderPosition(BaseMetroItemData itemData) {
		//int w = (int)(itemData.widthSpan * getItemUnitNumber());
		//int h = (int)(itemData.heightSpan * getItemUnitNumber());
		int w = (int)(itemData.widthSpan * ScreenUtils.getLiveItemWidth());
		int h = (int)(itemData.heightSpan * ScreenUtils.getVodMovieListItemHeight());
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
		params.width -= TvApplication.sTvItemPadding * 2;
		params.height -= TvApplication.sTvItemPadding * 2;
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		return params;
	}
	
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		int focusIndex = mFocusIndex;
	    if (focusIndex < 0 || focusIndex >= childCount)
	    	return i;

	    if (i != focusIndex) {
	    	if (i == childCount - 1)
		    	return focusIndex;
	    	return i;
	    }
	    return childCount - 1;
	}
	
	@Override
	public void requestChildFocus(View child, View focused) {
		mFocusIndex = indexOfChild(child);
		super.requestChildFocus(child, focused);
	}
	
	public enum MetroItemOrientation {
		VERTICAL,
		HORIZONTAL
	}
	
}

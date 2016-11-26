package com.iptv.rocky.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.MetroItemView;
import com.iptv.rocky.R;

public abstract class BaseMetroView extends RelativeLayout {

	public boolean isAutoFocus;
	
	private int mFocusIndex;
	public Drawable mBorderDrawable;
	
	private Point mCurrentPoint = new Point(0, 0);
	private Rect mBorderRect = new Rect();
	private Rect mItemRect = new Rect();
	
	public BaseMetroView(Context context) {
		this(context, null, 0);
	}
	
	public BaseMetroView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BaseMetroView(Context context, AttributeSet attrs, int defStyle) {
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
	    
	    Drawable drawable = context.getResources().getDrawable(R.drawable.tv_item_focused);
	    drawable.getPadding(mBorderRect);
	    mBorderDrawable = drawable;
	    mBorderDrawable.setAlpha(160);
	}
	
	public void addMetroItem(BaseMetroItemData itemData) {
		
		MetroItemView itemView = new MetroItemView(getContext());
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
			if (child instanceof MetroItemView) {
				MetroItemView itemView = (MetroItemView)child;
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
		int width = (int)(itemData.widthSpan * getItemUnitNumber());
		int height = (int)(itemData.heightSpan * getItemUnitNumber());
		
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
		int w = (int)(itemData.widthSpan * getItemUnitNumber());
		int h = (int)(itemData.heightSpan * getItemUnitNumber());
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
		params.width -= TvApplication.sTvItemPadding * 2;
		params.height -= TvApplication.sTvItemPadding * 2;
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		return params;
	}
	
	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		if (child != getFocusedChild()) {
			return super.drawChild(canvas, child, drawingTime);
		}
		if (child instanceof MetroItemView) {
			MetroItemView itemView = (MetroItemView)child;
	    	Rect itemRect = mItemRect;
	    	itemView.getDrawingRect(itemRect);
	    	offsetDescendantRectToMyCoords(itemView, itemRect);
	    	Rect borderRect = mBorderRect;
	    	int left = itemRect.left - borderRect.left + itemView.getPaddingLeft();
	    	int top = itemRect.top - borderRect.top + itemView.getPaddingTop();
	    	int right = itemRect.right + borderRect.right - itemView.getPaddingRight();
	    	int bottom = itemRect.bottom + borderRect.bottom - itemView.getPaddingBottom();
	    	if (itemView.isRelection) {
	    		bottom -= itemView.getReflectionHeight();
	    	}
	    	if (isTopPadding() && !itemView.isNotTopPadding()) {
	    		top += TvApplication.sTvItemTopPadding;
	    	}
	    	
    		int widthTips = (int)((right - left) * 0.05);
	    	int heightTips = (int)((bottom - top) * 0.05);
	    	left -= widthTips;
	    	top -= heightTips;
	    	right += widthTips;
	    	bottom += heightTips;
	    	
	    	mBorderDrawable.setBounds(left, top, right, bottom);
	    	mBorderDrawable.draw(canvas);
	    }
		return super.drawChild(canvas, child, drawingTime);
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

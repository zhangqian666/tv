package com.iptv.rocky.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;

import com.iptv.rocky.base.BaseListItemView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.horizontallistview.AbsHorizontalListView;
import com.iptv.rocky.view.horizontallistview.AdapterView;
import com.iptv.rocky.view.horizontallistview.HorizontalListView;
import com.iptv.rocky.R;

public class TvHorizontalListView extends HorizontalListView {
	
	protected boolean mIsNotAutoFocus;

	private boolean mDataLoadding;
	private int mSelectedIndex = -1;

	private View mPreSelectedView;
	private Drawable mShadowDrawable;
//	private Drawable mBorderDrawable;
	
	private Rect mShadowRect = new Rect();
	private Rect mItemRect = new Rect();
	
	public TvHorizontalListView(Context context) {
		this(context, null, 0);
	}
	
	public TvHorizontalListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TvHorizontalListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
		setChildrenDrawingOrderEnabled(true);
	    setClipToPadding(false);
	    setWillNotDraw(true);
	    
	    setOnScrollListener(onScrollListener);
		setOnItemSelectedListener(onItemSelectedListener);
	    setPadding(TvApplication.sTvLeftMargin, TvApplication.sTvListTopPadding, TvApplication.sTvLeftMargin, 0);
	    
	    Drawable drawable = context.getResources().getDrawable(R.drawable.tv_item_focused);
	    drawable.getPadding(mShadowRect);
	    mShadowDrawable = drawable;
	    mShadowDrawable.setAlpha(160);
	    
//	    Drawable drawable2 = context.getResources().getDrawable(R.drawable.tv_white_border);
//	    mBorderDrawable = drawable2;
	    this.setSelector(R.drawable.tv_transparent_selector);
	}
	
	public void destroy() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			if (child instanceof BaseListItemView) {
				BaseListItemView item = (BaseListItemView)child;
				item.destroy();
			}
		}
	}
	
	public void setAutoFocus(boolean autoFocus) {
		mIsNotAutoFocus = !autoFocus;
	}
	
	protected void loaddingComplete() {
		mDataLoadding = false; 
	}
	
	protected void loadDatas() {
	}
	
	private void processFocus(View focusView) {
		if (mPreSelectedView != null) {
            ((BaseListItemView)mPreSelectedView).processFocus(false);
		}
    	mPreSelectedView = focusView;
    	((BaseListItemView)focusView).processFocus(true);
	}
	
	private void processKey() {
		if (mSelectedIndex != -1) {
			mSelectedIndex = -1;
			if (mPreSelectedView != null) {
				((BaseListItemView)mPreSelectedView).processFocus(false);
				mPreSelectedView.clearFocus();
				mPreSelectedView = null;
				mIsNotAutoFocus = true;
			}
		}
	}
	
	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		if (child != getSelectedView() || mSelectedIndex < 0) 
			return super.drawChild(canvas, child, drawingTime);
		if (child instanceof BaseListItemView) {
			BaseListItemView itemView = (BaseListItemView)child;
	    	Rect itemRect = mItemRect;
	    	itemView.getDrawingRect(itemRect);
	    	offsetDescendantRectToMyCoords(itemView, itemRect);
	    	Rect shadowRect = mShadowRect;
	    	int left = itemRect.left - shadowRect.left + itemView.getPaddingLeft();
	    	int top = itemRect.top - shadowRect.top + itemView.getPaddingTop();
	    	int right = itemRect.right + shadowRect.right - itemView.getPaddingRight();
	    	int bottom = itemRect.bottom + shadowRect.bottom - itemView.getPaddingBottom();
	    	if (itemView.isRelection) {
	    		bottom -= itemView.getReflectionHeight();
	    	}
	    	if (!itemView.isNotTopPadding()) {
	    		top += TvApplication.sTvItemTopPadding;
	    	}
	    	
    		int widthTips = (int)((right - left) * 0.05);
	    	int heightTips = (int)((bottom - top) * 0.05);
	    	left -= widthTips;
	    	top -= heightTips;
	    	right += widthTips;
	    	bottom += heightTips;
	    	
		    Drawable drawable = mShadowDrawable;
		    drawable.setBounds(left, top, right, bottom);
		    drawable.draw(canvas);

//		    int left2 = itemView.getLeft();
//	    	int top2 = itemView.getTop();
//	    	int right2 = itemView.getRight();
//	    	int bottom2 = itemView.getBottom();
//	    	int widthTips2 = (int)((right2 - left2) * 0.05);
//	    	int heightTips2 = (int)((bottom2 - top2) * 0.05);
//	    	left2 -= widthTips2;
//	    	top2 -= heightTips2;
//	    	right2 += widthTips2;
//	    	bottom2 += heightTips2;
//	    	
//	    	if (itemView.isRelection) {
//	    		bottom2 -= (itemView.getReflectionHeight() * 1.1);
//	    	}
//	    	if (!itemView.isNotTopPadding()) {
//	    		top2 += (TvApplication.sTvItemTopPadding * 1.1);
//	    	}
//		    Drawable drawable2 = mBorderDrawable;
//		    drawable2.setBounds(left2, top2, right2, bottom2);
//		    drawable2.draw(canvas);
		}
		return super.drawChild(canvas, child, drawingTime);
	}
	
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		int focusIndex = mNextSelectedPosition - mFirstPosition;
		
	    if (focusIndex < 0 || focusIndex >= childCount)
	    	return i;

	    if (i != focusIndex) {
	    	if (i == childCount - 1)
		    	return focusIndex;
	    	return i;
	    }
//	    View child = getChildAt(i); 
//	    if (child.getScaleX() == 1.0f) {
//	    	int position = i + mFirstPosition;
//			setSelectedPositionInt(position);
//			setNextSelectedPositionInt(position);
//	    }
	    return childCount - 1;
	}
	
	@Override
	public void requestChildFocus(View child, View focused) {
		super.requestChildFocus(child, focused);

		mSelectedIndex = indexOfChild(focused);
		
		mIsNotAutoFocus = false;
		processFocus(focused);
		if (getSelectedView() != focused) {
			int position = getPositionForView(focused);
			mOldSelectedPosition = INVALID_POSITION;
			setSelectedPositionInt(position);
			setNextSelectedPositionInt(position);
		}	
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		boolean handler = super.dispatchKeyEvent(event);
		
		if (!handler) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP && focusSearch(FOCUS_UP) != null) {
					processKey();
				} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && focusSearch(FOCUS_DOWN) != null) {
					processKey();
				} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && focusSearch(FOCUS_LEFT) != null) {
					processKey();
				} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && focusSearch(FOCUS_RIGHT) != null) {
					processKey();
				}
			}
		}
		
		return handler;
	}
	
	private OnScrollListener onScrollListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsHorizontalListView view, int scrollState) {
		}
		
		@Override
		public void onScroll(AbsHorizontalListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (!mDataLoadding &&
				firstVisibleItem > 0 &&
				totalItemCount > 0 && 
				firstVisibleItem + visibleItemCount + 2 >= totalItemCount) {
				mDataLoadding = true;
				loadDatas();
			}
		}
	};
	
	private AdapterView.OnItemSelectedListener onItemSelectedListener =   
			new AdapterView.OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			if (!mIsNotAutoFocus) {
				requestFocus();
				view.requestFocus();
			}
		}
	
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			final View view = getSelectedView();
			if (view != null) {
				playSoundEffect(SoundEffectConstants.CLICK);
				return view.performClick();
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
}

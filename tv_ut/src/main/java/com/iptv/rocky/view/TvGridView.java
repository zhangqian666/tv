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
import com.iptv.rocky.utils.AppCommonUtils;
import com.iptv.rocky.view.gridview.TwoWayAbsListView;
import com.iptv.rocky.view.gridview.TwoWayAdapterView;
import com.iptv.rocky.view.gridview.TwoWayGridView;
import com.iptv.rocky.R;

public class TvGridView extends TwoWayGridView {

	private boolean mDataLoadding;
	private int mSelectedIndex = -2;
	
	private View mPreSelectedView;
	private View mPreFocusedView;
	private Drawable mBorderDrawable;
	
	private Rect mBorderRect = new Rect();
	private Rect mItemRect = new Rect();
	
	public TvGridView(Context context) {
		this(context, null, 0);
	}
	
	public TvGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TvGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setChildrenDrawingOrderEnabled(true);
	    setClipToPadding(false);
	    setWillNotDraw(true);
	    
	    setOnScrollListener(onScrollListener);
	    setOnItemSelectedListener(itemSelectListener);
	    
	    int tvItemPaddingPixel = AppCommonUtils.convertDipToPixels(context, TvApplication.sTvItemPadding);
		int padding = (int) ((TvApplication.pixelHeight - TvApplication.sTvChannelUnit * 1.6 - tvItemPaddingPixel * 4) / 2);
		setPadding(TvApplication.sTvLeftMargin, padding, 0, padding);
		setHorizontalSpacing(tvItemPaddingPixel * 2);
		Drawable drawable = context.getResources().getDrawable(R.drawable.tv_item_focused);
	    drawable.getPadding(mBorderRect);
	    mBorderDrawable = drawable;
	    mBorderDrawable.setAlpha(160);
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
	
	public void loaddingComplete() {
		mDataLoadding = false; 
	}
	
	protected void loadDatas() {
	}
	
	private void processFocus(View focusView) {
		if (mPreSelectedView != null) {
			((BaseListItemView)mPreSelectedView).processFocus(false);
		}
    	((BaseListItemView)focusView).processFocus(true);
    	mPreSelectedView = focusView;
	}
	
	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		if (child != getChildAt(mSelectedIndex))  //getSelectedView()
			return super.drawChild(canvas, child, drawingTime);
		if (child instanceof BaseListItemView) {
			BaseListItemView itemView = (BaseListItemView)child;
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
	    	if (!itemView.isNotTopPadding()) {
	    		top += TvApplication.sTvItemTopPadding;
	    	}
	    	
    		int widthTips = (int)((right - left) * 0.05);
	    	int heightTips = (int)((bottom - top) * 0.05);
	    	left -= widthTips;
	    	top -= heightTips;
	    	right += widthTips;
	    	bottom += heightTips;
	    	
		    Drawable drawable = mBorderDrawable;
		    drawable.setBounds(left, top, right, bottom);
		    drawable.draw(canvas);
	    }
		return super.drawChild(canvas, child, drawingTime);
	}
	
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		int focusIndex = mSelectedIndex;
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
	public boolean dispatchKeyEvent(KeyEvent event) {
		boolean handler = super.dispatchKeyEvent(event);
		if (!handler) {
			if (mSelectedIndex != -1) {
				handler = event.dispatch(this);
				if (handler) {
					this.requestFocus();
					return true;
				}
			}
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
					return processKey(FOCUS_UP, focusSearch(FOCUS_UP));
				} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
					return processKey(FOCUS_DOWN, focusSearch(FOCUS_DOWN));
				} else {
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						return processKey(FOCUS_LEFT);
					} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						return processKey(FOCUS_RIGHT);
					} 
				}
			}
		}
		return handler;
	}
	
	private boolean processKey(int direction, View focusView) {
		if (focusView != null) {
			if (mSelectedIndex != -1) {
				mSelectedIndex = -1;
				if (mPreSelectedView != null) {
					((BaseListItemView)mPreSelectedView).processFocus(false);
					focusView.requestFocus();
					mPreFocusedView = focusView;
					playSoundEffect( SoundEffectConstants.getContantForFocusDirection( direction ) );
					return true;
				}
			} else {
				if (mPreSelectedView != null) {
					mSelectedIndex = indexOfChild(mPreSelectedView);
					((BaseListItemView)mPreSelectedView).processFocus(true);
					this.requestFocus();
					mPreFocusedView = null;
					playSoundEffect( SoundEffectConstants.getContantForFocusDirection( direction ) );
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean processKey(int direction) {
		if (mPreFocusedView != null) {
			View focusedView = mPreFocusedView.focusSearch(direction);
			if (focusedView != null) {
				if (AppCommonUtils.isChild(this, focusedView)) {
					return processKey(direction, focusedView);
				} else {
					mPreFocusedView = focusedView;
					focusedView.requestFocus();
					playSoundEffect( SoundEffectConstants.getContantForFocusDirection( direction ) );
					return true;
				}
			}
		} else {
			return processKey(direction, focusSearch(direction));
		}
		return false;
	}
	
	private OnScrollListener onScrollListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(TwoWayAbsListView view, int scrollState) {
		}
		
		@Override
		public void onScroll(TwoWayAbsListView view, int firstVisibleItem,
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
	
	private OnItemSelectedListener itemSelectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(TwoWayAdapterView<?> parent, View view,
				int position, long id) {
			mSelectedIndex = indexOfChild(view);
			processFocus(view);
		}

		@Override
		public void onNothingSelected(TwoWayAdapterView<?> parent) {}
	};
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (mSelectedPosition >= 0 && mAdapter != null &&
					mSelectedPosition < mAdapter.getCount()) {
				final View view = getChildAt(mSelectedPosition - mFirstPosition);
				if (view != null) {
					playSoundEffect(SoundEffectConstants.CLICK);
					return view.performClick();
				}
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
}

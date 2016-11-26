package com.iptv.rocky.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.gallery.Gallery;

public class TvGallery extends Gallery {
	
	private static final String TAG = "TvGallery";
	
	private final Rect mTempRect = new Rect();
	
	private OnItemChangedListener onItemChangedListener;
	
	public TvGallery(Context context) {
		this(context, null, 0);
	}
	
	public TvGallery(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TvGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setFocusable(false);
		setFocusableInTouchMode(false);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		if (action == KeyEvent.ACTION_UP) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
				View focusView = this.getFocusedChild().findFocus();
				if (focusView != null) {
					return focusView.performClick();
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}

	public interface OnItemChangedListener {
		 void onItemChanged(int position);
	}
	
	public void setOnItemChangedListener(OnItemChangedListener l) {
		this.onItemChangedListener = l;
	}
	
	@Override
	public void requestChildFocus(View child, View focused) {
		super.requestChildFocus(child, focused);
		
		if (child == focused && child instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup)focused;
			if (viewGroup.getChildCount() > 0) {
				viewGroup.getChildAt(0).requestFocus();
			}
		}
		
		View selectedView = getSelectedView();
		if (child.getClass().equals(selectedView.getClass()) &&
			child != selectedView) {
			if (child.getLeft() < selectedView.getLeft()) {
				if (movePrevious()) {
					if (onItemChangedListener != null) {
						onItemChangedListener.onItemChanged(mSelectedPosition - 1);
					}
	            }
			} else {
				if (moveNext()) {
					if (onItemChangedListener != null) {
						onItemChangedListener.onItemChanged(mSelectedPosition + 1);
					}
	            }
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean handled = false;
		switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
        	handled = arrowScroll(FOCUS_LEFT);
        	break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
        	handled = arrowScroll(FOCUS_RIGHT);
        	break;
        }
//		if (!handled) {
//			return super.onKeyDown(keyCode, event);
//		}
        return handled;
	}
	
	public boolean arrowScroll(int direction) {
		View currentFocused = findFocus();
		if (currentFocused == this) {
			currentFocused = null;
		} else if (currentFocused != null) {
			boolean isChild = false;
			for (ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup;
					parent = parent.getParent()) {
				if (parent == this) {
					isChild = true;
					break;
				}
			}
			if (!isChild) {
				// This would cause the focus search down below to fail in fun ways.
				final StringBuilder sb = new StringBuilder();
				sb.append(currentFocused.getClass().getSimpleName());
				for (ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup;
    				 parent = parent.getParent()) {
					sb.append(" => ").append(parent.getClass().getSimpleName());
				}
				Log.e(TAG, "arrowScroll tried to find focus based on non-child " +
        				 "current focused view " + sb.toString());
				currentFocused = null;
			}
		}

		boolean handled = false;

		View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused,
				 direction);
		if (nextFocused != null && nextFocused != currentFocused) {
			if (direction == View.FOCUS_LEFT) {
				// If there is nothing to the left, or this is causing us to
				// jump to the right, then what we really want to do is page left.
				final int nextLeft = getChildRectInPagerCoordinates(mTempRect, nextFocused).left;
				final int currLeft = getChildRectInPagerCoordinates(mTempRect, currentFocused).left;
//				if (currentFocused != null && nextLeft >= currLeft) {
//					handled = false;//pageLeft();
//				} else {
//					handled = nextFocused.requestFocus();
//				}
				if (currentFocused != null && currLeft - nextLeft - (TvApplication.sTvItemPadding * 2) == currentFocused.getMeasuredWidth()) {
					handled = nextFocused.requestFocus();
				} else {
					handled = false;
				}
			} else if (direction == View.FOCUS_RIGHT) {
				// If there is nothing to the right, or this is causing us to
				// jump to the left, then what we really want to do is page right.
				final int nextLeft = getChildRectInPagerCoordinates(mTempRect, nextFocused).left;
				final int currLeft = getChildRectInPagerCoordinates(mTempRect, currentFocused).left;
//				if (currentFocused != null && nextLeft <= currLeft) {
//					handled = false;//pageRight();
//				} else {
//					handled = nextFocused.requestFocus();
//				}
				if (currentFocused != null && nextLeft - currLeft - (TvApplication.sTvItemPadding * 2)  == currentFocused.getMeasuredWidth()) {
					handled = nextFocused.requestFocus();
				} else {
					handled = false;
				}
			}
		} else if (direction == FOCUS_LEFT || direction == FOCUS_BACKWARD) {
			// Trying to move left and nothing there; try to page.
			handled = false;//pageLeft();
		} else if (direction == FOCUS_RIGHT || direction == FOCUS_FORWARD) {
			// Trying to move right and nothing there; try to page.
			handled = false;//pageRight();
		}
		if (handled) {
			playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
		}
		return handled;
	}
	 
	private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
		if (outRect == null) {
			outRect = new Rect();
		}
		if (child == null) {
			outRect.set(0, 0, 0, 0);
			return outRect;
		}
		outRect.left = child.getLeft();
		outRect.right = child.getRight();
		outRect.top = child.getTop();
		outRect.bottom = child.getBottom();
	
		ViewParent parent = child.getParent();
		while (parent instanceof ViewGroup && parent != this) {
			final ViewGroup group = (ViewGroup) parent;
			outRect.left += group.getLeft();
			outRect.right += group.getRight();
			outRect.top += group.getTop();
			outRect.bottom += group.getBottom();
	
			parent = group.getParent();
		}
		return outRect;
    }
	
}

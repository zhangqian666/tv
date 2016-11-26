package com.iptv.rocky.view;

import com.iptv.common.utils.LogUtils;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.HorizontalScrollView;

public class TvHorizontalScrollView extends HorizontalScrollView {
	
	public int mCount = 0;
	
	public void onItemScrolled(int nCount)
	{
		
	}

	public TvHorizontalScrollView(Context context) {
		this(context, null, 0);
	}
	
	public TvHorizontalScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TvHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);	
	}
	
	@Override
	public boolean arrowScroll(int direction) {
		boolean handled = super.arrowScroll(direction);
		
		LogUtils.debug("arrowScroll():direction:" + direction + ";handled:" + handled);
		
		if (handled) {
			playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
			
			if (direction == View.FOCUS_RIGHT) {
				mCount++;
				onItemScrolled(mCount);
			} else if (direction == View.FOCUS_LEFT) {
				mCount--;
				onItemScrolled(mCount);
			}
		}
		return handled;
	}
	
	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
		if (getChildCount() == 0) {
			return 0;
		}
		
		int width = getWidth();
	    int x = getScrollX();
	    int edge = getHorizontalFadingEdgeLength();
	    
	    int left = x + 200;
	    int right = width + x - 200;
	    if (rect.left > 0) {
	    	left += edge;
	    }
	    if (rect.right < getChildAt(0).getWidth()) {
	    	right -= edge;
	    }
	    
	    //Log.i("width", width + "");
		//Log.i("x", x + "");
		//Log.i("edge", edge + "");
		//Log.i("rect", rect.left + "   " + rect.right + "   " + rect.width());
		//Log.i("delta", delta + "");
		//Log.i("child", getChildAt(0).getWidth() + "    " + getChildAt(0).getRight());
		//Log.i("left-right", left + "   " + right);
		
		if (rect.left < left && rect.right < right) {
			return Math.max(rect.left - left, -x);
		} else if (rect.left > left && rect.right > right) {
			return Math.min(rect.left - left, rect.right - right);
		}
		return super.computeScrollDeltaToGetChildRectOnScreen(rect);
	}

}

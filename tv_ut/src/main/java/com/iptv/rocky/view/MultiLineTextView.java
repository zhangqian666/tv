package com.iptv.rocky.view;

import android.content.Context;
import android.util.AttributeSet;

public class MultiLineTextView extends TextViewDip {

	public MultiLineTextView(Context context) {
		this(context, null);
	}

	public MultiLineTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void calculateLines() {
		int mHeight = getMeasuredHeight();
		int lHeight = getLineHeight();
		int lines = mHeight / lHeight;
		setLines(lines);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			calculateLines();
	}
}

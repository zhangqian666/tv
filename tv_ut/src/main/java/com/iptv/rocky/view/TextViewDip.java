package com.iptv.rocky.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class TextViewDip extends TextView {

	public TextViewDip(Context context) {
		this(context, null, 0);
	}
	
	public TextViewDip(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TextViewDip(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setTextSize(float size) {
		super.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
	}
	
}

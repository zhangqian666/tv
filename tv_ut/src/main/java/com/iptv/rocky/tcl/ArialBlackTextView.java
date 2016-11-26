package com.iptv.rocky.tcl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class ArialBlackTextView extends TextView{

	public ArialBlackTextView(Context paramContext) {
		//super(context);
		this(paramContext, null, 0);
		// TODO Auto-generated constructor stub
	}

	  public ArialBlackTextView(Context paramContext, AttributeSet paramAttributeSet)
	  {
	    this(paramContext, paramAttributeSet, 0);
	  }
	  
	  public ArialBlackTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	  {
	    super(paramContext, paramAttributeSet, paramInt);
	    setTypeface(Typeface.createFromAsset(paramContext.getAssets(), "fonts/ArialBlack.ttf"));
	  }
	  
	@Override
	public void setTextSize(float size) {
		super.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
	}
}

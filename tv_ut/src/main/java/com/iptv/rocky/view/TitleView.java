package com.iptv.rocky.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import com.iptv.rocky.model.TvApplication;

public class TitleView extends TextViewDip {

	public TitleView(Context context) {
		this(context, null, 0);
	}
	
	public TitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setPadding(TvApplication.sTvLeftMargin, 0, TvApplication.sTvLeftMargin, 0);
		setHeight(TvApplication.sTvTabHeight);
		//setTextSize(TvApplication.dpiHeight / context.getResources().getDimension(R.dimen.tv_title_textsize));
		setTextSize(TvApplication.sTvTitleViewTextSize);
		setGravity(Gravity.CENTER);
	}

}

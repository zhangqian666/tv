package com.iptv.rocky.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.iptv.rocky.base.BaseTabItemData;
import com.iptv.rocky.model.TvApplication;

public class TabItemView extends TextViewDip {

	private static int sViewPadding = -1;
	
	public TabItemView(Context context) {
		this(context, null, 0);
	}
	
	public TabItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TabItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public final void initView(BaseTabItemData itemData, 
			View.OnKeyListener onKeyListener, View.OnClickListener onClickListener) {
		setText(itemData.title);
		setPadding(sViewPadding, 0, sViewPadding, 0);
		setOnKeyListener(onKeyListener);
		setOnClickListener(onClickListener);
	}
	
	private void init() {
		setTextSize(TvApplication.sTvHomeCommViewTextSize);//sTvMasterTextSize
		
		if (sViewPadding < 0) {
			//sViewPadding = (int)(TvApplication.sTvMasterTextSize * 3);
			//sViewPadding = (int)(TvApplication.sTvMasterTextSize * 4);
			sViewPadding = TvApplication.sTvTabItemPadding;
		}
	}
}

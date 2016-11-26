package com.iptv.rocky.view.horizontallistview;

import android.view.View;

import com.iptv.rocky.view.horizontallistview.ViewHelperFactory.ViewHelperDefault;

public class ViewHelper14 extends ViewHelperDefault {

	public ViewHelper14(View view) {
		super(view);
	}
	
	@Override
	public void setScrollX(int value) {
		view.setScrollX(value);
	}
	
	@Override
	public boolean isHardwareAccelerated() {
		return view.isHardwareAccelerated();
	}
	
}
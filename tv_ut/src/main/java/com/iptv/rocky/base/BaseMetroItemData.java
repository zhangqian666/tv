package com.iptv.rocky.base;

import android.content.Context;
import android.view.View;

public abstract class BaseMetroItemData {
	
    public int id;

    public double widthSpan;
	
	public double heightSpan;
	
	public boolean isViewLoaded;
	
	public boolean isNotTopPadding;
	
	public abstract View getView(Context context);
	
	public abstract void onClick(Context context);
	
	public void onOwnerFocusChange(boolean hasFocus) {
	}
}

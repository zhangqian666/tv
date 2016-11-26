package com.iptv.rocky.view;

import android.app.Dialog;
import android.content.Context;

import com.iptv.rocky.R;
public class TvDialog extends Dialog {
	
	public TvDialog(Context context) {
		this(context, R.style.Tv_Dialog);
	}

	public TvDialog(Context context, int theme) {
		super(context, theme);
		
		setCanceledOnTouchOutside(true);
	}
	
}

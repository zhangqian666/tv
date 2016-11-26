package com.iptv.rocky.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iptv.rocky.VodSearchActivity;
import com.iptv.rocky.model.TvApplication;

public class SearchView extends LinearLayout {
	
	public SearchView(Context context) {
		this(context, null, 0);
	}
	
	public SearchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SearchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setClickable(true);
	    setFocusable(true);
	    setFocusableInTouchMode(true);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)getLayoutParams();

		params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.height = TvApplication.sTvTabHeight;
	    setLayoutParams(params);
	    int padding = TvApplication.sTvTabItemPadding;
	    setPadding(padding, 0, padding, 0);
	}

	@Override
	public boolean performClick() {
        Intent searchIntent = new Intent(this.getContext(), VodSearchActivity.class);
        this.getContext().startActivity(searchIntent);
        return true;
    }
	
}

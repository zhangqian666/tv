package com.iptv.rocky.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class UserSettingMasterLayout extends RelativeLayout{


	public UserSettingMasterLayout(Context context) {
		this(context, null, 0);
	}

	public UserSettingMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UserSettingMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
    }

	@Override
	protected void onFinishInflate() {
        super.onFinishInflate();
    }
}

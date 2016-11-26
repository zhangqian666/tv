package com.iptv.rocky.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.iptv.rocky.model.TvApplication;

public class SettingContentLayout extends LinearLayout {

    public SettingContentLayout(Context context) {
        super(context);
    }

    public SettingContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        int paddingVer = (int) (TvApplication.pixelHeight / 24);
        int paddingHor = (int) (TvApplication.pixelWidth / 22);
        this.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
    }

    public SettingContentLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int margin = (int) (TvApplication.pixelHeight / 34.3);
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ((LayoutParams)this.getChildAt(i).getLayoutParams()).setMargins(margin,margin,margin,margin);
        }
    }
}

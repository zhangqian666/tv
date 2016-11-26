package com.iptv.rocky.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class SettingClickableItem extends LinearLayout {
    private Context mContext;
    private TextViewDip titleView;
    private TextViewDip valueView;

    public SettingClickableItem(Context context) {
        super(context);
    }

    public SettingClickableItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setPadding(6, 12, (int) (TvApplication.pixelWidth / 40), 12);
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOrientation(HORIZONTAL);
        this.setBackgroundResource(R.drawable.setting_item_view_bg);
    }

    public SettingClickableItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initItemViews();
    }

    private void initItemViews() {
        titleView = new TextViewDip(mContext);
        this.addView(titleView);

        valueView = new TextViewDip(mContext);
        this.addView(valueView);

        titleView.getLayoutParams().width = (int) (TvApplication.pixelWidth / 6.0);
        titleView.getLayoutParams().height = (int) (TvApplication.pixelHeight / 15);
        titleView.setGravity(Gravity.CENTER);
        titleView.setText(mContext.getResources().getString(R.string.update_check));
        titleView.setTextColor(mContext.getResources().getColorStateList(R.color.white));
        titleView.setTextSize(TvApplication.dpiHeight / 25);

        valueView.getLayoutParams().width = (int) (TvApplication.pixelWidth / 2.6);
        String content = String.format(mContext.getResources().getString(
				R.string.current_version), TvApplication.mAppVersionName, "", "");
        valueView.setText(content);
        valueView.setTextColor(mContext.getResources().getColorStateList(R.color.white));
        valueView.setTextSize(TvApplication.dpiHeight / 25);
        valueView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        valueView.getLayoutParams().height = LayoutParams.MATCH_PARENT;
    }
}

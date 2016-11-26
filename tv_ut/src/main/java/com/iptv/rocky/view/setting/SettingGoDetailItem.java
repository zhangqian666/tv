package com.iptv.rocky.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class SettingGoDetailItem extends LinearLayout {
    private Context mContext;
    private TextView titleView;
    private TextView emptyView;

    public SettingGoDetailItem(Context context) {
        super(context);
    }

    public SettingGoDetailItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setPadding(6, 12, (int) (TvApplication.pixelWidth / 40), 12);
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOrientation(HORIZONTAL);
        this.setBackgroundResource(R.drawable.setting_item_view_bg);
        this.setOnKeyListener(onKeyListener);
        this.setGravity(Gravity.CENTER_VERTICAL);
    }

    public SettingGoDetailItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initItemViews();
    }

    private void initItemViews() {
        titleView = new TextView(mContext);
        this.addView(titleView);

        emptyView = new TextView(mContext);
        this.addView(emptyView);

        titleView.getLayoutParams().width = (int) (TvApplication.pixelWidth / 4.9);
        titleView.getLayoutParams().height = (int) (TvApplication.pixelHeight / 15);
        titleView.setGravity(Gravity.CENTER);
        titleView.setText(mContext.getResources().getString(R.string.download_setting));
        titleView.setTextColor(mContext.getResources().getColorStateList(R.color.white));
        titleView.setTextSize(TvApplication.dpiHeight / 32);

        emptyView.getLayoutParams().width = (int) (TvApplication.pixelWidth / 2.6);
        emptyView.setText(" ");
        emptyView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.setting_right_img,0);
        emptyView.setGravity(Gravity.CENTER_VERTICAL);
    }
    
    private OnKeyListener onKeyListener = new OnKeyListener() {
    	
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            return false;
        }
    };
}

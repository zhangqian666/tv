package com.iptv.rocky.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

/**
 * 账户设置按钮
 */
public class AccountSettingBtn extends Button {
    private AccountSettingBtn mSelf;

    public AccountSettingBtn(Context context) {
        this(context, null, 0);
    }

    public AccountSettingBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccountSettingBtn(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mSelf = this;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.setClickable(true);
        this.setFocusable(true);
        int height = (int) (TvApplication.pixelHeight / 12);
        int textSize = (int) (TvApplication.dpiHeight / 30);
        this.setHeight(height);
        this.setWidth((int) (TvApplication.pixelWidth / 5.6));
        this.setTextSize(textSize);
        this.setGravity(Gravity.CENTER);
        this.setBackgroundResource(R.drawable.setting_btn_bg_normal);
        this.setOnKeyListener(onKeyListener);
        this.setOnFocusChangeListener(onFocusChangeListener);
    }

    private OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    mSelf.setBackgroundResource(R.drawable.setting_btn_bg_pressed);
                }
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    mSelf.setBackgroundResource(R.drawable.setting_btn_bg_focused);
                }
            }
            return false;
        }
    };
    
    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mSelf.setBackgroundResource(R.drawable.setting_btn_bg_focused);
            } else {
                mSelf.setBackgroundResource(R.drawable.setting_btn_bg_normal);
            }
        }
    };

}

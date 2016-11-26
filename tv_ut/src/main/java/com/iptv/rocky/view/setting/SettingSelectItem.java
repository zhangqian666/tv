package com.iptv.rocky.view.setting;

import java.util.EventListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

/**
 * 切换选择型设置项View
 */
public class SettingSelectItem extends LinearLayout{
    private Context mContext;
    private TextViewDip titleView;
    private TextViewDip splitView;
    private ImageView leftFlagView;
    private TextViewDip valueView;
    private ImageView rightFlagView;

    private OnItemValueChanngeListener mChangeListener;

    public SettingSelectItem(Context context) {
        super(context);
    }

    public SettingSelectItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setPadding(6, 12, 6, 12);
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOrientation(HORIZONTAL);
        this.setBackgroundResource(R.drawable.setting_item_view_bg);
    }

    public SettingSelectItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void initItemViews(String title, String curTxt) {
        titleView = new TextViewDip(mContext);
        this.addView(titleView);

        splitView = new TextViewDip(mContext);
        this.addView(splitView);

        leftFlagView = new ImageView(mContext);
        this.addView(leftFlagView);

        valueView = new TextViewDip(mContext);
        this.addView(valueView);

        rightFlagView = new ImageView(mContext);
        this.addView(rightFlagView);

        titleView.getLayoutParams().width = (int) (TvApplication.pixelWidth / 4.5);
        titleView.getLayoutParams().height = (int) (TvApplication.pixelHeight / 15);
        titleView.setGravity(Gravity.CENTER);
        titleView.setText(title);
        titleView.setTextColor(mContext.getResources().getColorStateList(R.color.white));
        titleView.setTextSize(TvApplication.dpiHeight / 25);

        splitView.setWidth(2);
        splitView.setBackgroundResource(R.color.white);
        splitView.setGravity(Gravity.CENTER);
        splitView.getLayoutParams().height = LayoutParams.MATCH_PARENT;

        leftFlagView.getLayoutParams().width = (int) (TvApplication.pixelWidth / 10);
        leftFlagView.setImageResource(R.drawable.setting_left_img);
        leftFlagView.getLayoutParams().height = LayoutParams.MATCH_PARENT;
        leftFlagView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        valueView.getLayoutParams().width = (int) (TvApplication.pixelWidth / 4.5);
        valueView.setText(curTxt);
        valueView.setTextColor(mContext.getResources().getColorStateList(R.color.white));
        valueView.setTextSize(TvApplication.dpiHeight / 25);
        valueView.setGravity(Gravity.CENTER);
        valueView.getLayoutParams().height = LayoutParams.MATCH_PARENT;

        rightFlagView.getLayoutParams().width = (int) (TvApplication.pixelWidth / 25);
        rightFlagView.setImageResource(R.drawable.setting_right_img);
        rightFlagView.getLayoutParams().height = LayoutParams.MATCH_PARENT;
        rightFlagView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        setOnKeyListener(onKeyListener);
        leftFlagView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChangeListener != null) {
                    mChangeListener.onChange(OnItemValueChanngeListener.ORIENTATION_LEFT);
                }
            }
        });
        rightFlagView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChangeListener != null) {
                    mChangeListener.onChange(OnItemValueChanngeListener.ORIENTATION_RIGHT);
                }
            }
        });
    }

    public void setValueTxt(String valueTxt) {
        valueView.setText(valueTxt);
    }
    private OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    playSoundEffect(SoundEffectConstants.NAVIGATION_RIGHT);
                    if (mChangeListener != null) {
                        mChangeListener.onChange(OnItemValueChanngeListener.ORIENTATION_RIGHT);
                    }
                }

                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    playSoundEffect(SoundEffectConstants.NAVIGATION_LEFT);
                    if (mChangeListener != null) {
                        mChangeListener.onChange(OnItemValueChanngeListener.ORIENTATION_LEFT);
                    }
                }

            }
            return false;
        }
    };

    public void setOnItemValueChanngeListener(OnItemValueChanngeListener l) {
        mChangeListener = l;
    }

    /**
     * 选择项发生变化自定义监听器
     */
    public interface OnItemValueChanngeListener extends EventListener {
        public static int ORIENTATION_LEFT = 0;
        public static int ORIENTATION_RIGHT = 1;
        public void onChange(int orientation);
    }
}

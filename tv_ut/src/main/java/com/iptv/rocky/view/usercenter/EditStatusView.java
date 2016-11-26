package com.iptv.rocky.view.usercenter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

/**
 * 历史、收藏、追剧左下编辑、清空对应的View
 */
public class EditStatusView extends LinearLayout {
    private TextViewDip editBtn;
    private TextViewDip clearBtn;
    private LinearLayout editLinearLayout;
    private LinearLayout clearLinearLayout;

    public EditStatusView(Context context) {
        super(context);
    }

    public EditStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        editBtn = (TextViewDip) findViewById(R.id.usercenter_btn_edit);
        clearBtn = (TextViewDip) findViewById(R.id.usercenter_btn_clear);

        editLinearLayout = (LinearLayout)findViewById(R.id.usercenter_linearlayout_edit);
        clearLinearLayout = (LinearLayout)findViewById(R.id.usercenter_linearlayout_clear);
        editLinearLayout.setFocusable(true);
        clearLinearLayout.setFocusable(true);
        editBtn.setTextSize(ScreenUtils.getMasterTextSize());
        clearBtn.setTextSize(ScreenUtils.getMasterTextSize());
    }
}

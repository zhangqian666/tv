package com.ui.player;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class TVButton extends RelativeLayout
{
    public TVButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setClickable(true);
        setFocusable(true);
    }

    /** 焦点 */
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect)
    {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        int count = getChildCount();
        View view;
        for (int i = 0; i < count; i++)
        {
            view = getChildAt(i);
            view.setSelected(gainFocus);
        }
    }

    /** 设置可用 */
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);

        int count = getChildCount();
        View view;
        for (int i = 0; i < count; i++)
        {
            view = getChildAt(i);
            view.setEnabled(enabled);
        }
    }
}

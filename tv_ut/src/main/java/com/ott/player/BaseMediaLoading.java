package com.ott.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 播放器loading界面
 */
public abstract class BaseMediaLoading extends RelativeLayout
{
    protected TextView textView;

    public BaseMediaLoading(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        initView();
    }

    protected abstract void initView();

    public void show(String text)
    {
        setText(text);

        setVisibility(View.VISIBLE);
    }

    public void hide()
    {
        setVisibility(View.INVISIBLE);
    }

    public void setText(String text)
    {
        if (textView != null)
        {
            textView.setText(text);
        }
    }
}

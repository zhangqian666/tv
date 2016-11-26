package com.ui.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.SeekBar;

public class TVPlayerSeekBar extends SeekBar
{
    private OnSeekBarChangeListener onSeekBarChangeListener;

    public TVPlayerSeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l)
    {
        super.setOnSeekBarChangeListener(l);

        this.onSeekBarChangeListener = l;
    }

    /** 将视频拖动到最后时刻松手，光标会移动到屏幕设置上，不执行拖动操作 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean b = super.onKeyDown(keyCode, event);
        if (isEnabled())
        {
            // 第一次按下
            switch (keyCode)
            {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (event.getRepeatCount() == 0)
                    {
                        if (onSeekBarChangeListener != null)
                        {
                            onSeekBarChangeListener.onStartTrackingTouch(this);
                        }
                    }
                    return true;
            }
        }

        return b;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (isEnabled())
        {
            switch (keyCode)
            {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (onSeekBarChangeListener != null)
                    {
                        onSeekBarChangeListener.onStopTrackingTouch(this);
                    }
                    break;
            }
        }

        return super.onKeyUp(keyCode, event);
    }
}

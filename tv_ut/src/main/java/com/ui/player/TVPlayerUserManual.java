package com.ui.player;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.base.BaseActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * 播放用户教育
 */
public class TVPlayerUserManual extends BaseActivity
{
    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_player_manual);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LogUtils.debug("onResume");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        finish();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        finish();
        return super.dispatchKeyEvent(event);
    }
}

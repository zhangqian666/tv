package com.ui.player;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.ott.player.BaseMediaController;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 播放控制
 */
public class TVMediaController extends BaseMediaController
{
    private ImageView mFullscreenImageView;

    public TVMediaController(Context context)
    {
        super(context);
    }

    @Override
    protected View makeControllerView()
    {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mRoot = inflate.inflate(R.layout.tv_media_controller, null, true);

        initControllerView(mRoot);

        return mRoot;
    }

    @Override
    protected void initControllerView(View v)
    {
        mPauseButton = v.findViewById(R.id.tv_media_controller_pause);

        mProgress = (ProgressBar) v.findViewById(R.id.tv_media_controller_progress);
        if (mProgress != null)
        {
            // 进度条获取焦点时，按确认键，暂停或播放
            mProgress.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    LogUtils.debug(event.toString());
                    if (event.getAction() == KeyEvent.ACTION_UP
                            && (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        // 暂停or播放
                        doPauseResume();
                        return true;
                    }
                    return false;
                }
            });
        }

        // 音量
        // mVolumeIndicator = (ImageView) v.findViewById(R.id.volume_indicator);
        // mVolumeProgress = (ProgressBar) v.findViewById(R.id.seekbar_volume);
        // mVolumeText = (TextView) v.findViewById(R.id.volume_text);

        mEndTime = (TextView) v.findViewById(R.id.tv_media_controller_time);
        mCurrentTime = (TextView) v.findViewById(R.id.tv_media_controller_time_current);

        mTitle = (TextView) v.findViewById(R.id.tv_media_controller_title);

        mPrevButton = v.findViewById(R.id.tv_media_controller_prev);
        mNextButton = v.findViewById(R.id.tv_media_controller_next);

        // bufferingView = v.findViewById(R.id.tv_player_buffer);

        mFullScreenButton = v.findViewById(R.id.tv_media_controller_fullscreen);
        mFullScreenButton.setFocusable(true);
        mFullscreenImageView = (ImageView) v.findViewById(R.id.tv_media_controller_fullscreen_image);
    }

    @Override
    public void updateFullScreen()
    {
        if (mFullscreenImageView != null && mPlayer != null)
        {
            if (mPlayer.isFullScreen())
            {
                mFullscreenImageView.setImageResource(R.drawable.tv_player_restore_selector);
            }
            else
            {
                mFullscreenImageView.setImageResource(R.drawable.tv_player_fullscreen_selector);
            }
        }
    }

    /** 电量图标 */
    @Override
    protected int getBatteryIcon(int status)
    {
        return -1;
    }

    @Override
    protected void updatePausePlay()
    {
        if (mRoot == null || mPauseButton == null)
        {
            return;
        }

        if (mPlayer.isPlaying())
        {
            mPauseButton.setBackgroundResource(R.drawable.tv_pause);
        }
        else
        {
            mPauseButton.setBackgroundResource(R.drawable.tv_play);
        }
    }

    @Override
    protected String stringForTime(int timeMs)
    {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);

        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    /** 显示 */
    @Override
    public void show(int timeout)
    {
        try
        {
            super.show(timeout);

            if (mRoot != null && !mRoot.hasFocus())
            {
                mRoot.requestFocus();
                mRoot.requestFocusFromTouch();

                // 第一个获取焦点的是进度条
                mProgress.requestFocus();
                mProgress.requestFocusFromTouch();
            }
        }
        catch (BadTokenException ex)
        {
            LogUtils.debug("activity has destroy");
        }
    }

    @Override
    public boolean hide(boolean force)
    {
        boolean hide = super.hide(force);
        if (hide)
        {
            // 隐藏status bar
            // UtilMethod.setSystemUiVisibility((Activity) getContext(), false);
        }
        return hide;
    }

    @Override
    protected void disableUnsupportedButtons()
    {
        super.disableUnsupportedButtons();

        if (mNextButton != null && !mPlayer.canNext())
        {
            mNextButton.setVisibility(View.INVISIBLE);
        }

        if (mPrevButton != null && !mPlayer.canPrev())
        {
            mPrevButton.setVisibility(View.INVISIBLE);
        }
    }

}

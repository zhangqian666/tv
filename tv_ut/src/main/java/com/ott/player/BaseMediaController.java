package com.ott.player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import com.iptv.common.utils.LogUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A view containing controls for a MediaPlayer. Typically contains the
 * buttons like "Play/Pause", "Rewind", "Fast Forward" and a progress slider.
 * It takes care of synchronizing the controls with the state of the
 * MediaPlayer.
 * <p>
 * The way to use this class is to instantiate it programatically. The
 * MediaController will create a default set of controls and put them in a
 * window floating above your application. Specifically, the controls will
 * float above the view specified with setAnchorView(). The window will
 * disappear if left idle for three seconds and reappear when the user touches
 * the anchor view.
 * <p>
 * Functions like show() and hide() have no effect when MediaController is
 * created in an xml layout. MediaController will hide and show the buttons
 * according to these rules:
 * <ul>
 * <li>The "previous" and "next" buttons are hidden until
 * setPrevNextListeners() has been called
 * <li>The "previous" and "next" buttons are visible but disabled if
 * setPrevNextListeners() was called with null listeners
 * <li>The "rewind" and "fastforward" buttons are shown unless requested
 * otherwise by using the MediaController(Context, boolean) constructor with
 * the boolean set to false
 * </ul>
 */
public abstract class BaseMediaController extends FrameLayout
{
    /** 长 超时时间:1小时 */
    private static final int LONG_TIMEOUT = 3600000;

    /** 默认显示时间：3秒 */
    private static final int sDefaultTimeout = 5000;

    /** 快进时间 */
    private static final int INCREMENT_TIME = 30000;

    private static final int FADE_OUT = 1;

    private static final int SHOW_PROGRESS = 2;

    private static final int SHOW_TIME = 3;

    private static final long PROGRESS_MAX = 1000;

    private static final String ACTION_VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";

    // private static final String ACTION_RINGER_MODE_CHANGED =
    // "android.media.RINGER_MODE_CHANGED";

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case FADE_OUT:
                    hide(false);
                    break;

                case SHOW_PROGRESS:
                {
                    // 显示进度
                    int pos = setProgress();
                    if (!mDragging && mShowing && mPlayer.isPlaying())
                    {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
                }

                case SHOW_TIME:
                {
                    // 显示时间
                    long time = setTime();
                    msg = obtainMessage(SHOW_TIME);
                    // 更新分钟
                    long delay = 60 * 1000 - (time % (60 * 1000));
                    LogUtils.error("delay:" + delay);
                    sendMessageDelayed(msg, delay);
                    break;
                }
            }
        }
    };

    protected MediaPlayerControl mPlayer;

    protected Context mContext;

    private View mAnchor;

    protected View mRoot;

    private View mDecor;

    protected ProgressBar mProgress;

    protected TextView mEndTime;

    protected TextView mCurrentTime;

    private boolean mShowing;

    private boolean mDragging;

    protected StringBuilder mFormatBuilder;

    protected Formatter mFormatter;

    protected View mPauseButton;

    /** 快进 */
    protected View mFfwdButton;

    /** 快退 */
    protected View mRewButton;

    /** 下一集 */
    protected View mNextButton;

    /** 上一集 */
    protected View mPrevButton;

    protected TextView mTitle;

    protected View bufferingView;

    /** 手势 */
    private GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener()
    {
        public boolean onDoubleTap(MotionEvent e)
        {
            LogUtils.error("onDoubleTap");

            BaseMediaController.this.onDoubleTap();

            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            LogUtils.error("onSingleTapConfirmed");

            BaseMediaController.this.onSingleTapConfirmed();

            return true;
        }

        public void onLongPress(MotionEvent e)
        {
        }
    });

    private OnTouchListener mTouchListener = new OnTouchListener()
    {
        public boolean onTouch(View v, MotionEvent event)
        {
            // if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // if (mShowing) {
            // hide();
            // }
            // }
            // return false;
            return gestureDetector.onTouchEvent(event);
        }
    };

    // There are two scenarios that can trigger the seekbar listener to
    // trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed
    // by
    // a number of onProgressChanged notifications, concluded by
    // onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the
    // dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in
    // this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch
    // notifications,
    // we will simply apply the updated position without suspending regular
    // updates.
    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener()
    {
        public void onStartTrackingTouch(SeekBar bar)
        {
            show(LONG_TIMEOUT);

            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser)
        {
            if (!fromuser)
            {
                // We're not interested in programmatically generated changes
                // to
                // the progress bar's position.
                return;
            }

            if (mPlayer != null)
            {
                long duration = mPlayer.getDuration();
                long newposition = (duration * progress) / PROGRESS_MAX;

                // add 20120520 手松开再seek
                if (!mDragging)
                {
                    mPlayer.seekTo((int) newposition, true);
                }

                if (mCurrentTime != null)
                {
                    mCurrentTime.setText(stringForTime((int) newposition));
                    mDecor.invalidate();
                }
            }
        }

        public void onStopTrackingTouch(SeekBar bar)
        {
            mDragging = false;

            // add by 20120520 手松开再seek
            if (mPlayer != null)
            {
                long duration = mPlayer.getDuration();
                long newposition = (duration * bar.getProgress()) / PROGRESS_MAX;
                mPlayer.seekTo((int) newposition, true);
            }

            setProgress();
            updatePausePlay();
            show();

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    /** 音量 */
    protected ProgressBar mVolumeProgress;

    protected TextView mVolumeText;

    private int mCurrentVolume;

    private int mLastVolume;

    private boolean mVolumeDragging;

    protected ImageView mVolumeIndicator;

    private VerticalSeekBar.OnSeekBarChangeListener mVolumeSeekListener = new VerticalSeekBar.OnSeekBarChangeListener()
    {

        @Override
        public void onStartTrackingTouch(VerticalSeekBar seekBar)
        {
            show(LONG_TIMEOUT);

            mVolumeDragging = true;

            mHandler.removeMessages(SHOW_PROGRESS);
        }

        @Override
        public void onProgressChanged(VerticalSeekBar seekBar, int progress, boolean fromUser)
        {
            setVolumeText(progress);

            setVolumeIndicator(progress, seekBar.getMax());

            // if (!fromUser) {
            // return;
            // }

            // int newVolume = (int) (progress * mMaxVolume / PROGRESS_MAX);
            // LogUtils.error("newVolume:" + newVolume);
            LogUtils.error("progress:" + progress);
            if (mCurrentVolume != progress)
            {
                if (mCurrentVolume > 0)
                {
                    mLastVolume = mCurrentVolume;
                }

                mCurrentVolume = progress;

                AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
        }

        @Override
        public void onStopTrackingTouch(VerticalSeekBar seekBar)
        {
            mVolumeDragging = false;

            show();
        }
    };

    private View.OnClickListener mVolumeIndicatorListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (mVolumeProgress != null)
            {
                if (mCurrentVolume == 0)
                {
                    // long progress = PROGRESS_MAX * mLastVolume / mMaxVolume;

                    mVolumeProgress.setProgress(mLastVolume);
                }
                else
                {
                    // 静音
                    mVolumeProgress.setProgress(0);
                }
            }
        }
    };

    /** 暂停or开始 */
    private View.OnClickListener mPauseListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            doPauseResume();
        }
    };

    /** 快退 */
    private View.OnClickListener mRewListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            rew();
        }
    };

    /** 快进 */
    private View.OnClickListener mFfwdListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            fastforwad();
        }
    };

    /** 上一集 */
    private View.OnClickListener mPrevListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            playPrev();
        }
    };

    /** 下一集 */
    private View.OnClickListener mNextListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            playNext();
        }
    };

    protected View mFullScreenButton;

    private View.OnClickListener mFullScreeListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switchFullScreen();
        }
    };

    protected ImageView mBatteryImage;

    /** 监听音量、电量 */
    private BroadcastReceiver ppReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent == null)
            {
                return;
            }
            String action = intent.getAction();

            LogUtils.error("action:" + action);
            // ACTION_RINGER_MODE_CHANGED.equals(action) ||
            if (ACTION_VOLUME_CHANGED.equals(action))
            {
                // 音量改变
                if (!mVolumeDragging)
                {
                    setVolumeProgress();
                }
            }
            else if (Intent.ACTION_BATTERY_CHANGED.equals(action))
            {
                // 电量改变
                setBattery(intent);
            }
        }
    };

    // 时间
    protected TextView mTimeTextView;

    // private int mMaxVolume;

    public BaseMediaController(Context context)
    {
        super(context);
        mContext = context;

        initFloatingWindow();
    }

    private void initFloatingWindow()
    {
        mDecor = this;
        mDecor.setOnTouchListener(mTouchListener);

        mDecor.setBackgroundResource(android.R.color.transparent);
        mDecor.setVisibility(View.INVISIBLE);
        // While the media controller is up, the volume control keys should
        // affect the media stream type
        //mDecor.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        requestFocus();
    }

    /**
     * Set the view that acts as the anchor for the control view. This can for
     * example be a VideoView, or your Activity's main view.
     * 
     * @param view The view to which to anchor the controller when it is
     *            visible.
     */
    public void setAnchorView(View view)
    {
        mAnchor = view;

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);

        removeAllViews();
        mRoot = makeControllerView();

        initControllerView();

        addView(mRoot, frameParams);
        try
        {
            addViewToAnchor();
        }
        catch (Exception ex)
        {
            
        }
    }
    
    public void addViewToAnchor()
    {
        WindowManager.LayoutParams p = new WindowManager.LayoutParams();
        p.gravity = Gravity.TOP;
        p.width = mAnchor.getWidth();
        p.height = LayoutParams.WRAP_CONTENT;
        p.x = 0;
        // p.y = anchorpos[1] + mAnchor.getHeight() - p.height;
        p.y = 0;
        p.format = PixelFormat.TRANSLUCENT;
        p.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        p.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        p.token = null;
        p.windowAnimations = 0; // android.R.style.DropDownAnimationDown;
        ((ViewGroup) mAnchor).addView(mDecor, p);
    }

    /**
     * Create the view that holds the widgets that control playback. Derived
     * classes can override this to create their own.
     * 
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected abstract View makeControllerView();

    protected abstract void initControllerView(View v);

    private void initControllerView()
    {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        if (mPauseButton != null)
        {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        if (mFfwdButton != null)
        {
            mFfwdButton.setOnClickListener(mFfwdListener);
        }

        if (mRewButton != null)
        {
            mRewButton.setOnClickListener(mRewListener);
        }

        if (mNextButton != null)
        {
            mNextButton.setOnClickListener(mNextListener);
        }

        if (mPrevButton != null)
        {
            mPrevButton.setOnClickListener(mPrevListener);
        }

        if (mProgress != null)
        {
            if (mProgress instanceof SeekBar)
            {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax((int) PROGRESS_MAX);
        }

        // 全屏按钮
        if (mFullScreenButton != null)
        {
            mFullScreenButton.setOnClickListener(mFullScreeListener);
        }

        if (mVolumeIndicator != null)
        {
            mVolumeIndicator.setOnClickListener(mVolumeIndicatorListener);
        }

        if (mVolumeProgress != null)
        {
            if (mVolumeProgress instanceof VerticalSeekBar)
            {
                VerticalSeekBar seeker = (VerticalSeekBar) mVolumeProgress;
                seeker.setOnSeekBarChangeListener(mVolumeSeekListener);
            }
            // mVolumeProgress.setMax((int) PROGRESS_MAX);
        }
    }

    /** 后退 */
    protected void rew()
    {
        if (mPlayer != null)
        {
            int pos = mPlayer.getCurrentPosition();
            pos -= 15000; // milliseconds
            mPlayer.seekTo(pos, true);
            setProgress();

            show();
        }
    }

    /** 快进 */
    protected void fastforwad()
    {
        if (mPlayer != null)
        {
            int pos = mPlayer.getCurrentPosition();
            pos += 15000; // milliseconds
            mPlayer.seekTo(pos, true);
            setProgress();

            show();
        }
    }

    /** 播放下一集 */
    protected void playNext()
    {
        if (mPlayer != null)
        {
            mPlayer.playNext();
        }
    }

    /** 播放上一集 */
    protected void playPrev()
    {
        if (mPlayer != null)
        {
            mPlayer.playPrev();
        }
    }

    public void setMediaPlayer(MediaPlayerControl player)
    {
        mPlayer = player;
        updatePausePlay();
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked.
     * This requires the control interface to be a MediaPlayerControlExt
     */
    protected void disableUnsupportedButtons()
    {
        try
        {
            if (mPauseButton != null && !mPlayer.canPause())
            {
                mPauseButton.setEnabled(false);
            }

            if (mRewButton != null && !mPlayer.canSeekBackward())
            {
                mRewButton.setEnabled(false);
            }

            if (mFfwdButton != null && !mPlayer.canSeekForward())
            {
                mFfwdButton.setEnabled(false);
            }

            if (mProgress != null && !mPlayer.canSeekBackward() && !mPlayer.canSeekForward())
            {
                mProgress.setEnabled(false);
            }

            if (mNextButton != null && !mPlayer.canNext())
            {
                mNextButton.setEnabled(false);

                //mNextButton.setFocusable(false);
            }

            if (mPrevButton != null && !mPlayer.canPrev())
            {
                mPrevButton.setEnabled(false);

                //mPrevButton.setFocusable(false);
            }
        }
        catch (IncompatibleClassChangeError ex)
        {
            // We were given an old version of the interface, that doesn't
            // have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't
            // disable
            // the buttons.
        }
    }

    /**
     * Show the controller on screen. It will go away automatically after 3
     * seconds of inactivity.
     */
    public void show()
    {
        if (!mPlayer.isPlaying())
        {
            // 暂停时，不设置超时时间
            show(0);
        }
        else
        {
            show(sDefaultTimeout);
        }
    }

    /**
     * Show the controller on screen. It will go away automatically after
     * 'timeout' milliseconds of inactivity.
     * 
     * @param timeout The timeout in milliseconds. Use 0 to show the
     *            controller until hide() is called.
     */
    public void show(int timeout)
    {

        if (!mShowing && mAnchor != null)
        {
            // 设置播放进度
            setProgress();

            // add by 20120520 显示控制条，取一下音量，显示过程中音量改变会通过广播通知
            setVolumeProgress();

            // 注册广播
            registerReceiver();

            if (mPauseButton != null)
            {
                mPauseButton.requestFocus();
            }
            // disableUnsupportedButtons();

            //int[] anchorpos = new int[2];
            //mAnchor.getLocationOnScreen(anchorpos);

            mDecor.setVisibility(View.VISIBLE);

            mShowing = true;
        }

        // 显示标题
        setTitle();

        updatePausePlay();

        // cause the progress bar to be updated even if mShowing
        // was already true. This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        // add by 20120520
        if (mTimeTextView != null)
        {
            mHandler.sendEmptyMessage(SHOW_TIME);
        }

        // 缓冲
        if (bufferingView != null && mPlayer != null)
        {
            bufferingView.setVisibility(mPlayer.isBuffering() ? View.VISIBLE : View.INVISIBLE);
        }

        Message msg = mHandler.obtainMessage(FADE_OUT);
        mHandler.removeMessages(FADE_OUT);
        if (timeout > 0)
        {
            mHandler.sendMessageDelayed(msg, timeout);
        }

        updateFullScreen();
    }

    public boolean isShowing()
    {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public boolean hide(boolean force)
    {
        if (mAnchor == null)
            return false;

        if (!force && !mPlayer.isPlaying())
        {
            // 暂停状态，控制条不消失
            return false;
        }

        if (mShowing)
        {
            try
            {
                mHandler.removeMessages(SHOW_PROGRESS);

                mHandler.removeMessages(SHOW_TIME);
                mDecor.setVisibility(View.GONE);
            }
            catch (Exception ex)
            {
                LogUtils.error("already removed");
            }

            // add by 20120520
            unregisterReceiver();

            mShowing = false;

            return true;
        }

        return false;
    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event)
    // {
    // show(sDefaultTimeout);
    // return true;
    // }

    @Override
    public boolean onTrackballEvent(MotionEvent ev)
    {
        show();
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        LogUtils.debug(event.toString());

        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0
                && /* event.isDown() */event.getAction() == KeyEvent.ACTION_DOWN
                && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE))
        {
            // add by chuckcheng 20120616 确认键 || keyCode ==
            // KeyEvent.KEYCODE_DPAD_CENTER || keyCode ==
            // KeyEvent.KEYCODE_ENTER
            doPauseResume();
            // show(sDefaultTimeout);
            if (mPauseButton != null)
            {
                mPauseButton.requestFocus();
            }
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP)
        {
            if (mPlayer.isPlaying())
            {
                mPlayer.pause();
                updatePausePlay();
            }
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)
        {
            hide(true);
            return true;
        }
        // else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode ==
        // KeyEvent.KEYCODE_DPAD_RIGHT)
        // {
        // // add by chuckcheng 20120616 左右键
        // if (mProgress != null)
        // {
        // // 进度条获取焦点，progress组件自己处理左右键，快进快退
        // mProgress.requestFocus();
        // }
        // show(sDefaultTimeout);
        // }

        // KEYCODE_F11 ppbox全屏键
        else if (event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == 223)
        {
            switchFullScreen();
            return true;
        }

        else
        {
            show();
        }
        return super.dispatchKeyEvent(event);
    }

    /** 双击 */
    protected void onDoubleTap()
    {
        switchFullScreen();
    }

    /** 点击 */
    protected void onSingleTapConfirmed()
    {
        if (mShowing && mPlayer.isPlaying())
        {
            hide(false);
        }
    }

    protected void switchFullScreen()
    {
        if (mPlayer != null)
        {
            mPlayer.switchFullScreen();
        }

        updateFullScreen();

        show();
    }

    /** 更新全屏按钮 */
    public abstract void updateFullScreen();

    protected String stringForTime(int timeMs)
    {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0)
        {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        }
        else
        {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /** 设置进度 */
    private int setProgress()
    {
        if (mPlayer == null || mDragging)
        {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null && duration > 0)
        {
            if (duration > 0)
            {
                // use long to avoid overflow，必须用long,用int会溢出
                long pos = PROGRESS_MAX * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);

            if (mProgress instanceof SeekBar)
            {
                SeekBar seeker = (SeekBar) mProgress;

                // 设置快进快退的进度值，一次30s
                long increment = (INCREMENT_TIME * PROGRESS_MAX) / duration;
                seeker.setKeyProgressIncrement((int) increment);
            }
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    /** 设置音量条 */
    private void setVolumeProgress()
    {
        if (mVolumeProgress == null)
        {
            return;
        }

        AudioManager audioManager = (AudioManager) this.getContext().getSystemService(Context.AUDIO_SERVICE);
        int mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mCurrentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        LogUtils.error("mCurrentVolume:" + mCurrentVolume);
        if (mLastVolume == 0)
        {
            if (mCurrentVolume == 0)
            {
                mLastVolume = mMaxVolume / 2;
            }
            else
            {
                mLastVolume = mCurrentVolume;
            }
        }

        // long progress = PROGRESS_MAX * mCurrentVolume / mMaxVolume;
        // setMax 必须在 setProgress 之前
        mVolumeProgress.setMax(mMaxVolume);
        mVolumeProgress.setProgress(mCurrentVolume);
    }

    private void setVolumeText(int volume)
    {
        if (mVolumeText != null)
        {
            mVolumeText.setText(volume + "");
        }
    }

    private void setVolumeIndicator(int progress, int max)
    {
        if (mVolumeIndicator == null)
        {
            return;
        }

        int newProgress = (int) (progress * 100L / max);
        if (newProgress == 0)
        {
            mVolumeIndicator.setImageLevel(0);
        }
        else if (newProgress < 100 / 3)
        {
            mVolumeIndicator.setImageLevel(1);
        }
        else if (newProgress > 100 * 2 / 3)
        {
            mVolumeIndicator.setImageLevel(3);
        }
        else
        {
            mVolumeIndicator.setImageLevel(2);
        }
    }

    /** 设置标题 */
    private void setTitle()
    {
        if (mTitle == null || mPlayer == null)
        {
            return;
        }
        mTitle.setText(mPlayer.getTitle());
    }

    /** 设置电量图标 */
    private void setBattery(Intent intent)
    {
        if (mBatteryImage != null)
        {
            int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int level = -1; // percentage, or -1 for unknown
            if (rawlevel >= 0 && scale > 0)
            {
                level = (rawlevel * 100) / scale;
            }

            mBatteryImage.setImageResource(getBatteryIcon(status));
            mBatteryImage.getDrawable().setLevel(level);
        }
    }

    /** 电量图标 */
    protected abstract int getBatteryIcon(int status);

    /** 设置时间 */
    private long setTime()
    {
        long time = System.currentTimeMillis();
        if (mTimeTextView != null)
        {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            mTimeTextView.setText(format.format(new Date(time)));
        }
        return time;
    }

    /** 注册广播 */
    private void registerReceiver()
    {
        try
        {
            IntentFilter filter = new IntentFilter();

            // 音量
            filter.addAction(ACTION_VOLUME_CHANGED);
            // filter.addAction(ACTION_RINGER_MODE_CHANGED);

            // 电量
            if (mBatteryImage != null)
            {
                filter.addAction(Intent.ACTION_BATTERY_CHANGED);
            }

            mContext.registerReceiver(ppReceiver, filter);
        }
        catch (Exception e)
        {
        }
    }

    private void unregisterReceiver()
    {
        try
        {
            mContext.unregisterReceiver(ppReceiver);
        }
        catch (Exception e)
        {
        }
    }

    /** 暂停or开始 */
    protected void doPauseResume()
    {
        if (mPlayer != null)
        {
            if (mPlayer.isPlaying())
            {
                mPlayer.pause();
            }
            else
            {
                mPlayer.start();
            }
            updatePausePlay();

            show();
        }
    }

    protected abstract void updatePausePlay();

    @Override
    public void setEnabled(boolean enabled)
    {
        if (mPauseButton != null)
        {
            mPauseButton.setEnabled(enabled);
        }
        if (mFfwdButton != null)
        {
            mFfwdButton.setEnabled(enabled);
        }
        if (mRewButton != null)
        {
            mRewButton.setEnabled(enabled);
        }
        if (mNextButton != null)
        {
            mNextButton.setEnabled(enabled);
            mNextButton.setFocusable(enabled);
        }
        if (mPrevButton != null)
        {
            mPrevButton.setEnabled(enabled);
            mPrevButton.setFocusable(enabled);
        }
        if (mProgress != null)
        {
            mProgress.setEnabled(enabled);
        }

        if (mFullScreenButton != null)
        {
            mFullScreenButton.setEnabled(enabled);
        }

        disableUnsupportedButtons();

        super.setEnabled(enabled);
    }

    public interface MediaPlayerControl
    {
        boolean start();

        void pause();

        int getDuration();

        int getCurrentPosition();

        boolean seekTo(int pos, boolean fromUser);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        boolean canPrev();

        void playPrev();

        boolean canNext();

        void playNext();

        boolean isFullScreen();

        void switchFullScreen();
        
        void setScreenType(int type);
        
        String getTitle();

        /** 正在缓冲 */
        boolean isBuffering();
    }

}

package com.ott.player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.LogUtils;
import com.ui.player.fragment.ChoosePackage;
import com.ui.player.fragment.ChoosePackage.ChoosePayMethodListener;
import com.ui.player.fragment.ChoosePayMethod;
import com.ui.player.fragment.ChoosePayMethod.BackToChoosePackageListener;
import com.ui.player.fragment.ChoosePlayPosition;
import com.ui.player.fragment.OrderStatusBar;


/**
 * Displays a video file. The VideoView class can load images from various
 * sources (such as resources or content providers), takes care of computing
 * its measurement from the video so that it can be used in any layout
 * manager, and provides various display options such as scaling and tinting.
 * 
 * @version 20120904 4.0 删除了resume和suspend
 */
public class BaseVideoView extends SurfaceView implements BaseMediaController.MediaPlayerControl, ChoosePayMethodListener,BackToChoosePackageListener
{
    // All the stuff we need for playing and showing a video
    protected SurfaceHolder mSurfaceHolder = null;

    private int mSurfaceWidth;

    private int mSurfaceHeight;

    protected BaseMediaController mMediaController;

    // recording the seek position while preparing
    private int mSeekWhenPrepared;

    private int mDuration;

    // settable by the client
    private Uri mUri;

    private HashMap<String, String> mHeaders;

    // all possible internal states
    public static final int STATE_ERROR = -1;

    public static final int STATE_IDLE = 0;

    public static final int STATE_PREPARING = 1;

    public static final int STATE_PREPARED = 2;

    public static final int STATE_PLAYING = 3;

    public static final int STATE_PAUSED = 4;

    public static final int STATE_PLAYBACK_COMPLETED = 5;

    public static final int GESTURE_MAX_DURATION_TIME = 5000;

	private static final String ACTION_SHOWCHOOSEMETHOD = "com.virgintelecom.iptv.ott.showchoosemethod";
    
    protected VodDetailInfo vodDetailInfo = null;
    
    public String status;
    protected ChoosePackage choosePackage;
    protected OrderStatusBar orderStatusBar;
    protected ChoosePayMethod choosePayMethod;
    protected ChoosePlayPosition choosePlayPosition;
    // public static final int STATE_SUSPEND = 6;
    //
    // public static final int STATE_RESUME = 7;
    //
    // public static final int STATE_SUSPEND_UNSUPPORTED = 8;

    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    public int mCurrentState = STATE_IDLE;

    public int mTargetState = STATE_IDLE;

    /** 是否支持硬解 */
    protected boolean isOMXSurface = true;

    /** 是否是surface硬解切软解 */
    protected boolean isSwitching = false;

    /**
     * 最后次缓存的百分比
     */
    protected int lastBuffingPercent;

    private long timeGestureStart = 0;

    public void setTimeGestureStart(long timeGestureStart)
    {
        this.timeGestureStart = timeGestureStart;
    }

    // FIXME: 临时解决播放器切换，导致播放进度丢失无法续播问题。
    /** 是否播放器切换 */
    protected boolean mIsSwitchPlayer = false;

    //protected boolean mIsPPbox = false;

    private static String PPBOX_SCREENMODE_STRECH = "1";
    private static String PPBOX_SCREENMODE_NORMAL = "0";

    /** 是否设置好surface大小 */
    // protected boolean isSurfaceSizeSet = false;

    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback()
    {
        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            BaseVideoView.this.surfaceCreated(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
        {
            mSurfaceWidth = w;
            mSurfaceHeight = h;
            int mSeekPos = mSeekWhenPrepared;
            // isSurfaceSizeSet = true;

            if (mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight)
            {
                if (mTargetState == STATE_PLAYING && !isPlaying())
                {
                    start();
                    // if (mMediaController != null)
                    // {
                    // mMediaController.show();
                    // }
                }
                else if (!isPlaying() && (mSeekPos != 0 || getCurrentPosition() > 0))
                {
                    if (mMediaController != null)
                    {
                        // Show the media controls when we're paused into
                        // a video and make 'em stick.
                    	mMediaController.show(0);
                    }
                }
            }

            // if (mMediaPlayer != null)
            // {
            //
            // boolean isValidState = (mTargetState == STATE_PLAYING);
            // // boolean hasValidSize = (mMediaPlayer.mVideoWidth == w &&
            // // mMediaPlayer.mVideoHeight == h);
            // boolean hasValidSize = (mVideoWidth == w && mVideoHeight == h);
            //
            // if (isValidState && hasValidSize)
            // {
            // if (mSeekWhenPrepared > 0)
            // {
            // seekTo(mSeekWhenPrepared, false);
            // }
            // start();
            //
            // // delete by 切换全屏，不需要显示控制条
            // // if (mMediaController != null) {
            // // if (mMediaController.isShowing()) {
            // // // ensure the controller will get repositioned later
            // // mMediaController.hide();
            // // }
            // // mMediaController.show();
            // // }
            // }
            // }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder)
        {
            BaseVideoView.this.surfaceDestroyed(holder);
        }
    };

    private BaseMediaPlayer.OnPreparedListener mOnPreparedListener;

    private BaseMediaPlayer.OnPreparedListener mPreparedListener = new BaseMediaPlayer.OnPreparedListener()
    {
        public void onPrepared(BaseMediaPlayer mp)
        {
            BaseVideoView.this.onPrepared(mp);
        }
    };

    private BaseMediaPlayer.OnCompletionListener mOnCompletionListener;

    private BaseMediaPlayer.OnCompletionListener mCompletionListener = new BaseMediaPlayer.OnCompletionListener()
    {
        public void onCompletion(BaseMediaPlayer mp)
        {
            if (mp != mMediaPlayer)
            {
                return;
            }

            mCurrentState = STATE_PLAYBACK_COMPLETED;
            mTargetState = STATE_PLAYBACK_COMPLETED;

            if (mMediaController != null)
            {
                mMediaController.hide(true);
            }

            BaseVideoView.this.onCompletion(mp);
        }
    };

    private BaseMediaPlayer.OnErrorListener mOnErrorListener;

    private BaseMediaPlayer.OnErrorListener mErrorListener = new BaseMediaPlayer.OnErrorListener()
    {
        public boolean onError(BaseMediaPlayer mp, int framework_err, int impl_err)
        {
            LogUtils.error("Error: " + framework_err + "," + impl_err);

            if (mp != mMediaPlayer)
            {
                return true;
            }

            // stop called in state 0
            // error (-38, 0)
            if (framework_err == -38)
            {
                return true;
            }

            return BaseVideoView.this.onError(mp, framework_err, impl_err);
        }
    };

    private BaseMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new BaseMediaPlayer.OnVideoSizeChangedListener()
    {
        public void onVideoSizeChanged(BaseMediaPlayer mp, int width, int height)
        {
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            if (mVideoWidth != 0 && mVideoHeight != 0)
            {
                getHolder().setFixedSize(width, height);
                requestLayout();
            }
        }
    };

    private BaseMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;

    private BaseMediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new BaseMediaPlayer.OnSeekCompleteListener()
    {
        @Override
        public void onSeekComplete(BaseMediaPlayer mp)
        {
            LogUtils.info("onSeekComplete");
            // seeking = false;
            // if (bufferView != null && !buffering && !seeking)
            // {
            // bufferView.setVisibility(View.INVISIBLE);
            // }

            BaseVideoView.this.onSeekComplete(mp);
        }
    };

    private BaseMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;

    private BaseMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new BaseMediaPlayer.OnBufferingUpdateListener()
    {
        @Override
        public void onBufferingUpdate(BaseMediaPlayer mp, int percent)
        {
            lastBuffingPercent = percent;
            if (mOnBufferingUpdateListener != null)
            {
                mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
            }
        }
    };

    private BaseMediaPlayer.OnInfoListener mOnInfoListener;

    private BaseMediaPlayer.OnInfoListener mInfoListener = new BaseMediaPlayer.OnInfoListener()
    {
        @Override
        public boolean onInfo(BaseMediaPlayer mp, int what, int extra)
        {
            if (mp != mMediaPlayer)
            {
                return true;
            }
            return BaseVideoView.this.onInfo(mp, what, extra);
        }
    };

    protected BaseMediaPlayer mMediaPlayer;

    private int mVideoWidth;

    private int mVideoHeight;

    private Class<? extends BaseMediaPlayer> playerClass;

    /** 延迟open */
    private static final int MSG_WHAT_OPEN = 0;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == MSG_WHAT_OPEN)
            {
                openVideo();
            }
        }
    };

    public BaseVideoView(Context context)
    {
        super(context);
        initVideoView();
      
    }

    public BaseVideoView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        initVideoView();
       
    }

    public BaseVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initVideoView();
        
    }

    public void setPPboxScreenMode(String mode)
    {
        String ScreenModeFile = "/sys/class/video/screen_mode";
        File file = new File(ScreenModeFile);
        if (!file.exists())
        {
            return;
        }
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(ScreenModeFile), 32);
            try
            {
                out.write(mode);
            }
            finally
            {
                out.close();
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
        }
    }

//    public void setIfPPbox(boolean ppbox)
//    {
//        mIsPPbox = ppbox;
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // // Log.i("@@@@", "onMeasure");
        //
        // // int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        // // int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        //
        int width = getDefaultSize(-1, widthMeasureSpec);
        int height = getDefaultSize(-1, heightMeasureSpec);

/*        if (mIsPPbox)
        {
            if (screenType == SCREEN_FULL_SCREEN)
            {
                setPPboxScreenMode(PPBOX_SCREENMODE_NORMAL);
            }
            else if (screenType == SCREEN_STRETCH)
            {
                setPPboxScreenMode(PPBOX_SCREENMODE_STRECH);
            }
            setMeasuredDimension(width, height);
            return;
        }*/

        if (mVideoWidth > 0 && mVideoHeight > 0)
        {
            if (screenType == SCREEN_FULL_SCREEN)
            {
                // 等比全屏，有黑边
                if (mVideoWidth * height > width * mVideoHeight)
                {
                    // Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                else if (mVideoWidth * height < width * mVideoHeight)
                {
                    // Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                }
                else
                {
                    // Log.i("@@@", "aspect ratio is correct: " +
                    // width+"/"+height+"="+
                    // mVideoWidth+"/"+mVideoHeight);
                }
            }
            else if (screenType == SCREEN_STRETCH)
            {
                // 拉伸全屏

            }
            else if (screenType == SCREEN_CROP)
            {
                // 裁剪全屏，无黑白
                if (mVideoWidth * height > width * mVideoHeight)
                {
                    // Log.i("@@@", "image too tall, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                }
                else if (mVideoWidth * height < width * mVideoHeight)
                {
                    // Log.i("@@@", "image too wide, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                else
                {
                    // Log.i("@@@", "aspect ratio is correct: " +
                    // width+"/"+height+"="+
                    // mVideoWidth+"/"+mVideoHeight);
                }
            }
        }
        // Log.i("@@@@@@@@@@", "setting size: " + width + 'x' + height);
        // LogUtils.error("setting size: " + width + 'x' + height);
        setMeasuredDimension(width, height);
    }

    /**
     * Register a callback to be invoked when the media file is loaded and
     * ready to go.
     * 
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(BaseMediaPlayer.OnPreparedListener l)
    {
        mOnPreparedListener = l;
    }

    /**
     * Register a callback to be invoked when the end of a media file has been
     * reached during playback.
     * 
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(BaseMediaPlayer.OnCompletionListener l)
    {
        mOnCompletionListener = l;
    }

    /**
     * Register a callback to be invoked when an error occurs during playback
     * or setup. If no listener is specified, or if the listener returned
     * false, VideoView will inform the user of any errors.
     * 
     * @param l The callback that will be run
     */
    public void setOnErrorListener(BaseMediaPlayer.OnErrorListener l)
    {
        mOnErrorListener = l;
    }

    public void setOnSeekCompleteListener(BaseMediaPlayer.OnSeekCompleteListener listener)
    {
        this.mOnSeekCompleteListener = listener;
    }

    public void setOnBufferingUpdateListener(BaseMediaPlayer.OnBufferingUpdateListener listener)
    {
        this.mOnBufferingUpdateListener = listener;
    }

    public void setOnInfoListener(BaseMediaPlayer.OnInfoListener listener)
    {
        this.mOnInfoListener = listener;
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec)
    {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode)
        {
            case MeasureSpec.UNSPECIFIED:
                /*
                 * Parent says we can be as big as we want. Just don't be
                 * larger than max size imposed on ourselves.
                 */
                result = desiredSize;
                break;

            case MeasureSpec.AT_MOST:
                /*
                 * Parent says we can be as big as we want, up to specSize.
                 * Don't be larger than specSize, and don't be larger than the
                 * max size imposed on ourselves.
                 */
                result = Math.min(desiredSize, specSize);
                break;

            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    private void initVideoView()
    {
        mVideoWidth = 0;
        mVideoHeight = 0;
        LogUtils.info("mVideoWidth:" + mVideoWidth + "mVideoHeight:" + mVideoHeight);
        getHolder().addCallback(mSHCallback);

        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // getHolder().setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        // getHolder().setFormat(PixelFormat.RGBX_8888);

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

        // mCurrentState = STATE_IDLE;
        // mTargetState = STATE_IDLE;
    }

    public void setVideoPath(String path)
    {
        setVideoURI(null, Uri.parse(path), "");
    }

    public void setVideoPath(String path, String title)
    {
        setVideoURI(null, Uri.parse(path), title);
    }

    public void setVideoPath(Class<? extends BaseMediaPlayer> playerClass, String path)
    {
        setVideoURI(playerClass, Uri.parse(path), "");
    }

    public void setVideoPath(Class<? extends BaseMediaPlayer> playerClass, String path, String title)
    {
        setVideoURI(playerClass, Uri.parse(path), title);
    }

    public void setVideoURI(Uri uri)
    {
        setVideoURI(null, uri, null, "");
    }

    public void setVideoURI(Class<? extends BaseMediaPlayer> playerClass, Uri uri)
    {
        setVideoURI(playerClass, uri, null, "");
    }

    public void setVideoURI(Class<? extends BaseMediaPlayer> playerClass, Uri uri, String title)
    {
        setVideoURI(playerClass, uri, null, title);
    }

    public void setVideoURI(Class<? extends BaseMediaPlayer> playerClass, Uri uri, HashMap<String, String> headers,String title)
    {
        LogUtils.debug("uri:" + uri);

        mUri = null;
        if (uri != null)
        {
        	//硬解码
            getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            isOMXSurface = true;

            mUri = uri;
            mHeaders = headers;
            mSeekWhenPrepared = 0;

            this.playerClass = playerClass;
            boolean open = openVideo();
            LogUtils.debug("openVideo:" + open);

            requestLayout();
            invalidate();
        }
    }

    @Deprecated
    public void stopPlayback()
    {
        stopPlayback(false);
    }

    public void stopPlayback(boolean clearUri)
    {
        LogUtils.info("stopPlayback");
//        if (mIsPPbox)
//        {
//            setPPboxScreenMode(PPBOX_SCREENMODE_NORMAL);
//        }
        
        // if (mMediaPlayer != null)
        // {
        // try
        // {
        // mMediaPlayer.stopPlayback();
        // }
        // catch (Exception e)
        // {
        // LogUtils.error(e.toString(), e);
        // }
        //
        // // ANR
        // // new StopPlayThread(mMediaPlayer).start();
        // // mMediaPlayer = null;
        //
        // mCurrentState = STATE_IDLE;
        // mTargetState = STATE_IDLE;
        // }

        // 取消open
        mHandler.removeMessages(MSG_WHAT_OPEN);

        // 隐藏控制条
        if (mMediaController != null)
        {
            mMediaController.hide(true);
        }

        release(!isSwitching);

        if (clearUri)
        {
            setVideoURI(null);
            setVisibility(View.INVISIBLE);
        }
        setBuffering(false);
    }

    // static class StopPlayThread extends Thread
    // {
    // private BaseMediaPlayer mediaPlayer;
    //
    // public StopPlayThread(BaseMediaPlayer mediaPlayer)
    // {
    // this.mediaPlayer = mediaPlayer;
    // }
    //
    // @Override
    // public void run()
    // {
    // if (mediaPlayer != null)
    // {
    // try
    // {
    // mediaPlayer.stopPlayback();
    // }
    // catch (Exception e)
    // {
    // LogUtils.error(e.toString(), e);
    // }
    // }
    // }
    // }

    protected boolean openVideo()
    {
        LogUtils.info("openVideo");

        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);

        if (mUri == null)
        {
            // not ready for playback just yet, will try again later
            return false;
        }

        if (mSurfaceHolder == null)
        {
            // not ready for playback just yet, will try again later

            if (getVisibility() != View.VISIBLE)
            {
                LogUtils.info("getVisibility() != View.VISIBLE");
                setVisibility(View.VISIBLE);
            }

            return false;
        }

        if (mMediaPlayer != null)
        {
            // 延迟open
            mHandler.sendEmptyMessageDelayed(MSG_WHAT_OPEN, 2000);

            return false;
        }

        if (playerClass == null)
        {
            // 默认使用系统播放器
            playerClass = AndroidMediaPlayer.class;
        }

        // getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (AndroidMediaPlayer.class.getCanonicalName().equals(playerClass.getCanonicalName()))
        {
            mMediaPlayer = new AndroidMediaPlayer(getContext().getApplicationContext());
        }
        else
        {
            LogUtils.error("playerClass not exist!");
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, BaseMediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return false;
        }

        // try
        // {
        // Constructor<? extends BaseMediaPlayer> constructor =
        // playerClass.getConstructor(Context.class);
        // mMediaPlayer =
        // constructor.newInstance(getContext().getApplicationContext());
        // }
        // catch (Exception e)
        // {
        // LogUtils.error(playerClass.toString(), e);
        // return false;
        // }

        mMediaPlayer.setOnPreparedListener(mPreparedListener);
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
        mMediaPlayer.setOnErrorListener(mErrorListener);
        mMediaPlayer.setOnSizeChangedListener(mSizeChangedListener);
        mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
        mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
        mMediaPlayer.setOnInfoListener(mInfoListener);

        if (mMediaPlayer != null)
        {
            boolean open = false;
            try
            {
                open = mMediaPlayer.openVideo(mSurfaceHolder, mUri, isOMXSurface);
                if (open)
                {
                    mDuration = -1;

                    // we don't set the target state here either, but preserve
                    // the
                    // target state that was there before.
                    mCurrentState = STATE_PREPARING;
                    attachMediaController();
                }
            }
            catch (Throwable ex)
            {
                LogUtils.error("Unable to open content: " + mUri, ex);
                mCurrentState = STATE_ERROR;
                mTargetState = STATE_ERROR;
                mErrorListener.onError(mMediaPlayer, BaseMediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            }

            return open;
        }
        return false;
    }

    // public void suspend()
    // {
    // // if (mMediaPlayer != null)
    // // {
    // // mMediaPlayer.suspend();
    // // }
    //
    // release(false);
    // }
    //
    // public void resume()
    // {
    // // if (mMediaPlayer != null)
    // // {
    // // if (!mMediaPlayer.resume(mSurfaceHolder))
    // // {
    // // openVideo();
    // //
    // // // add by chuckcheng 20120616
    // //
    // // // start();
    // // }
    // // else if (mMediaPlayer.mCurrentState ==
    // // BaseMediaPlayer.STATE_PAUSED)
    // // {
    // // // add by chuckcheng 20120616
    // // // 解决暂停状态下，suspend再resume黑屏的问题。**********再pause会err？
    // // // E/MediaPlayer(16303): pause called in state 8
    // // // E/MediaPlayer(16303): error (-38, 0)
    // //
    // // // start();
    // // }
    // // }
    //
    // openVideo();
    // }

    public void setMediaController(BaseMediaController controller)
    {
        if (mMediaController != null)
        {
            mMediaController.hide(true);
        }
        mMediaController = controller;
        attachMediaController();
    }

    /**   */
    private void attachMediaController()
    {
        if (mMediaPlayer != null && mMediaController != null)
        {
            mMediaController.setMediaPlayer(this);
            View anchorView = this.getParent() instanceof View ? (View) this.getParent() : this;
            mMediaController.setAnchorView(anchorView);
            mMediaController.setEnabled(isInPlaybackState());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        // if (isInPlaybackState() && mMediaController != null)
        // {
        toggleMediaControlsVisiblity();
        // }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev)
    {

        toggleMediaControlsVisiblity();

        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
    	
        LogUtils.error("Base VideoView 收到key Up事件"+event.toString());

        // KEYCODE_F11 ppbox全屏键
        if (event.getKeyCode() == 141)
        {
            switchFullScreen();
            return true;
        }
        // KEYCODE PPBOX视频比例切换键
        if (event.getKeyCode() == 223)
        {
            if (screenType == SCREEN_STRETCH)
            {
                setScreenType(SCREEN_FULL_SCREEN);
            }
            else
            {
                setScreenType(screenType + 1);
            }
        }

        // KEYCODE_ESCAPE 111
        boolean isKeyCodeSupported = keyCode != 111 && keyCode != KeyEvent.KEYCODE_BACK
                && keyCode != KeyEvent.KEYCODE_VOLUME_UP && keyCode != KeyEvent.KEYCODE_VOLUME_DOWN
                && keyCode != KeyEvent.KEYCODE_MENU && keyCode != KeyEvent.KEYCODE_CALL
                && keyCode != KeyEvent.KEYCODE_ENDCALL;
        if (isInPlaybackState() && isKeyCodeSupported && mMediaController != null)
        {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                    || keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)
            {
                // add by chuckcheng 20120616 确认键 || keyCode ==
                // KeyEvent.KEYCODE_SPACE || keyCode ==
                // KeyEvent.KEYCODE_DPAD_CENTER || keyCode ==
                // KeyEvent.KEYCODE_ENTER
            	
            	LogUtils.error("购买状态:"+vodDetailInfo.ordered+";  status:"+status);
                if (mMediaPlayer.isPlaying())
                {
                	if(vodDetailInfo.ordered){
                		 pause();
                         mMediaController.show();
                	}else{
                	}
                }
                else
                {
                	start();
                    mMediaController.hide(false);
                    
                }
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP && mMediaPlayer.isPlaying())
            {
                pause();
                mMediaController.show();
            }
            else
            {
            	if(status.equals("CHOOSE_PACKAGE") || status.equals("PAY") || status.equals("CHOOSE_PLAY_POSITION")){
            		
            	}else{
            		toggleMediaControlsVisiblity();
            	}
                
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    public boolean isInPlaybackState()
    {
        return (mMediaPlayer != null && mCurrentState != STATE_ERROR && mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
    }

    public void toggleMediaControlsVisiblity()
    {
        if (isInPlaybackState() && mMediaController != null)
        {
            if (mMediaController.isShowing())
            {
                mMediaController.hide(false);
            }
            else
            {
                mMediaController.show();
            }
        }
    }

    @Deprecated
    public void showMediaControls()
    {
        if (isInPlaybackState() && mMediaController != null)
        {
            mMediaController.show();
        }
    }

    protected void surfaceCreated(SurfaceHolder holder)
    {
        mSurfaceHolder = holder;
        // resume() was called before surfaceCreated()
        // if (mMediaPlayer != null && mMediaPlayer.mCurrentState ==
        // BaseMediaPlayer.STATE_SUSPEND
        // && mMediaPlayer.mTargetState == BaseMediaPlayer.STATE_RESUME)
        // {
        // mMediaPlayer.setDisplay(mSurfaceHolder);
        // resume();
        // }
        // else
        // {
        // openVideo();
        // }
        openVideo();
    }

    protected void surfaceDestroyed(SurfaceHolder holder)
    {
        LogUtils.info("surfaceDestroyed");

        // after we return from this we can't use the surface any more
        mSurfaceHolder = null;
        if (mMediaController != null)
        {
            mMediaController.hide(true);
        }

        // if (mMediaPlayer != null && mMediaPlayer.mCurrentState !=
        // BaseMediaPlayer.STATE_SUSPEND)
        // {
        // try
        // {
        // mMediaPlayer.release(true);
        // }
        // catch (Exception e)
        // {
        // LogUtils.error(e.toString(), e);
        // }
        //
        // // new ReleaseThread(mMediaPlayer).start();
        // }

        // release(true);
        // stopPlayback(false);
    }

    protected void onPrepared(BaseMediaPlayer mp)
    {
        LogUtils.info("系统播放器准备好了 回调 BaseVideoView的 onPrepared");
        if (mp != mMediaPlayer)
        {
            return;
        }

        if (mMediaPlayer == null)
        {
            return;
        }

        mCurrentState = STATE_PREPARED;

        setBuffering(false);

        if (mOnPreparedListener != null)
        {
            mOnPreparedListener.onPrepared(mMediaPlayer);
        }

        if (mMediaController != null)
        {
            mMediaController.setEnabled(true);
        }

        int seekToPosition = mSeekWhenPrepared; // mSeekWhenPrepared
                                                // may
        // be changed after
        // seekTo() call
        if (seekToPosition != 0)
        {
            seekTo(seekToPosition, false);
        }

        if (mMediaPlayer == null)
        {
            return;
        }
        mVideoWidth = mMediaPlayer.getVideoWidth();
        mVideoHeight = mMediaPlayer.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0)
        {
            getHolder().setFixedSize(mVideoWidth, mVideoHeight);

            if (mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight)
            {
                if (mTargetState == STATE_PLAYING)
                {
                    start();
                    // if (mMediaController != null)
                    // {
                    // mMediaController.show();
                    // }
                }
                else if (!isPlaying() && (seekToPosition != 0 || getCurrentPosition() > 0))
                {
                    if (mMediaController != null)
                    {
                        // Show the media controls when we're paused into
                        // a video and make 'em stick.
                        mMediaController.show(0);
                    }
                }
            }
            // delete by chuckcheng 20120616 加载完毕不播放
            // if (mSurfaceWidth == mVideoWidth && mSurfaceHeight ==
            // mVideoHeight)
            // {

            // We didn't actually change the size (it was already at
            // the size
            // we need), so we won't get a "surface changed" callback,
            // so
            // start the video here instead of in the callback.
            // }

        }
        else
        {
            // We don't know the video size yet, but should start anyway.
            // The video size might be reported to us later.
            if (mTargetState == STATE_PLAYING)
            {
                start();
            }
        }
    }

    protected void onCompletion(BaseMediaPlayer mp)
    {
        // if (mp != mMediaPlayer)
        // {
        // return;
        // }
        //
        // mCurrentState = STATE_PLAYBACK_COMPLETED;
        // mTargetState = STATE_PLAYBACK_COMPLETED;
        //
        // if (mMediaController != null)
        // {
        // mMediaController.hide(true);
        // }

        if (mOnCompletionListener != null)
        {
            mOnCompletionListener.onCompletion(mp);
        }
    }

    protected boolean onInfo(BaseMediaPlayer mp, int what, int extra)
    {
        LogUtils.debug("what:" + what + " ,extra:" + extra);

        if (what == BaseMediaPlayer.MEDIA_INFO_BUFFERING_START)
        {
            // 开始缓冲
            setBuffering(true);

            // if (bufferView != null)
            // {
            // bufferView.setVisibility(View.VISIBLE);
            //
            // // add by chuckcheng 20120911
            // // 增加这句，目的是为了解决“第一次缓冲，不显示控制条，圈圈不显示出来”，原因还不知道
            // showMediaControls();
            // }
        }
        else if (what == BaseMediaPlayer.MEDIA_INFO_BUFFERING_END)
        {
            // 缓冲结束
            setBuffering(false);

            // if (bufferView != null)// && !buffering && !seeking)
            // {
            // bufferView.setVisibility(View.INVISIBLE);
            // }
        }

        if (mOnInfoListener != null)
        {
            return mOnInfoListener.onInfo(mp, what, extra);
        }
        return false;
    }

    protected void onSeekComplete(BaseMediaPlayer mp)
    {
        if (mOnSeekCompleteListener != null)
        {
            mOnSeekCompleteListener.onSeekComplete(mp);
        }
    }

    protected boolean onError(BaseMediaPlayer mp, int framework_err, int impl_err)
    {
        // if (mp != mMediaPlayer)
        // {
        // return true;
        // }
        //
        // // stop called in state 0
        // // error (-38, 0)
        // if (framework_err == -38)
        // {
        // return true;
        // }

        /* If an error handler has been supplied, use it and finish. */
        if (mOnErrorListener != null)
        {
            if (mOnErrorListener.onError(mp, framework_err, impl_err))
            {
                return true;
            }
        }

        mCurrentState = STATE_ERROR;
        mTargetState = STATE_ERROR;

        if (mMediaController != null)
        {
            mMediaController.hide(true);
        }

        /*
         * Otherwise, pop up an error dialog so the user knows that something
         * bad has happened. Only try and pop up the dialog if we're attached
         * to a window. When we're going away and no longer have a window,
         * don't bother showing the user an error.
         */
        if (getWindowToken() != null)
        {
            // Resources r = getContext().getResources();
            int messageId;

            // if (framework_err ==
            // MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK)
            // {
            // messageId =
            // android.R.string.VideoView_error_text_invalid_progressive_playback;
            // }
            // else
            // {
            messageId = android.R.string.VideoView_error_text_unknown;
            // }

            new AlertDialog.Builder(getContext()).setTitle(android.R.string.VideoView_error_title).setMessage(messageId).setPositiveButton(
                    android.R.string.VideoView_error_button, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            /*
                             * If we get here, there is no onError listener,
                             * so at least inform them that the video is over.
                             */
                            if (mOnCompletionListener != null)
                            {
                                mOnCompletionListener.onCompletion(mMediaPlayer);
                            }
                        }
                    }).setCancelable(false).show();
        }
        return true;
    }

    protected void release(boolean cleartargetstate)
    {
        LogUtils.debug("###");
        if (mMediaPlayer != null)
        {
            // try
            // {
            // mMediaPlayer.stopPlayback();
            // }
            // catch (Exception e)
            // {
            // LogUtils.error(e.toString(), e);
            // }

            // if (isInPlaybackState())
            // {
            // // 同步
            // try
            // {
            // mMediaPlayer.release();
            // }
            // catch (Exception e)
            // {
            // LogUtils.error(e.toString(), e);
            // }
            // }
            // else
            // {
            // // 异步
            // new ReleaseThread(mMediaPlayer).start();
            // }
            // 系统播放器 release很容易ANR，所以使用线程释放
            if (releaseThread == null)
            {
                releaseThread = new ReleaseThread();
                releaseThread.start();
            }
        }

        mCurrentState = STATE_IDLE;
        if (cleartargetstate)
        {
            mTargetState = STATE_IDLE;
        }
    }

    private ReleaseThread releaseThread;

    /** 系统播放器异步释放资源 */
    class ReleaseThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                if (mMediaPlayer != null)
                {
                    long now = SystemClock.elapsedRealtime();
                    try
                    {
                        mMediaPlayer.release();
                    }
                    catch (Exception e)
                    {
                        LogUtils.error(e.toString(), e);
                    }
                    LogUtils.debug("release time:" + (SystemClock.elapsedRealtime() - now));

                    mMediaPlayer = null;
                }
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString(), e);
            }
            finally
            {
                releaseThread = null;
            }
        }
    }

    /**************** implements BaseMediaController.MediaPlayerControl *******************/
    @Override
    public boolean start()
    {
        LogUtils.debug("### START");

        // 701过程中点击暂停，会导致702不发出
        // if (bufferView != null)
        // {
        // bufferView.setVisibility(View.INVISIBLE);
        // }
        setBuffering(false);

        if (isInPlaybackState())
        {
            if ((mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) || // for
                                                                                    // video
                                                                                    // file
                                                                                    // playback
                    (mVideoWidth == 0 && mVideoHeight == 0)) // for audio only
                                                             // file playback
            {
                try
                {
                    boolean start = mMediaPlayer.start();

                    if (start)
                    {
                        mCurrentState = STATE_PLAYING;
                    }
                    if (mMediaController != null)
                    {
                        mMediaController.show();
                    }

                    return start;

                }
                catch (Exception e)
                {
                    LogUtils.error(e.toString());
                }
            }
            // if (start && mMediaController != null &&
            // mMediaController.isShowing())
            // {
            // mMediaController.updatePausePlay();
            // }
        }

        mTargetState = STATE_PLAYING;
        return false;
    }

    @Override
    public void pause()
    {
        LogUtils.debug("###， pause");

        // 701过程中点击暂停，会导致702不发出
        // if (bufferView != null)
        // {
        // bufferView.setVisibility(View.INVISIBLE);
        // }
        setBuffering(false);

        if (isInPlaybackState())
        {
            try
            {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;

                // if (mMediaController != null &&
                // mMediaController.isShowing())
                // {
                // mMediaController.updatePausePlay();
                // }
                if (mMediaController != null)
                {
                	if(vodDetailInfo.ordered){
                		mMediaController.show();
                	}
                	else{
                		choosePackage.setVisibility(View.VISIBLE);
                	}
                    
                }
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString());
            }
        }
        mTargetState = STATE_PAUSED;
    }

    // cache duration as mDuration for faster access
    @Override
    public int getDuration()
    {
        if (isInPlaybackState())
        {
            if (mDuration > 0)
            {
                return mDuration;
            }

            try
            {
                mDuration = mMediaPlayer.getDuration();
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString());
            }

            return mDuration;
        }
        mDuration = -1;
        return mDuration;
    }

    @Override
    public int getCurrentPosition()
    {
        if (mMediaPlayer != null && isInPlaybackState())
        {
            try
            {
                return mMediaPlayer.getCurrentPosition();
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString());
            }
        }
        return 0;
    }

    // public boolean seekTo(int msec)
    // {
    // return seekTo(msec, true);
    // }

    @Override
    public boolean seekTo(int msec, boolean fromUser)
    {
        LogUtils.info("msec:" + msec);

        if (isInPlaybackState())
        {
            // seeking = true;

            // 如果上一次buffer end还没有发出，又seek，会出现圈圈不消失的问题。所以seek之前主动消失一下
            // if (bufferView != null)
            // {
            // bufferView.setVisibility(View.INVISIBLE);
            // }
            setBuffering(false);

            // 容错处理
            if (getDuration() > 0 && msec > getDuration() - 5000)
            {
                // 结束前5s
                msec = getDuration() - 5000;
            }
            if (msec < 0)
            {
                msec = 0;
            }

            LogUtils.info("msec:" + msec);

            try
            {
                mMediaPlayer.seekTo(msec);
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString());
            }

            mSeekWhenPrepared = 0;

            LogUtils.error("true");
            return true;
        }

        mSeekWhenPrepared = msec;

        LogUtils.error("false");
        return false;
    }

    @Override
    public boolean isPlaying()
    {
        // if (isInPlaybackState())
        // {
        // try
        // {
        // return mMediaPlayer.isPlaying();
        // }
        // catch (Exception e)
        // {
        // LogUtils.error(e.toString());
        // }
        // }
        // return false;
        return mCurrentState == STATE_PLAYING;
    }

    @Override
    public int getBufferPercentage()
    {
        if (mMediaPlayer != null)
        {
            try
            {
                return mMediaPlayer.getBufferPercentage();
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString());
            }
        }
        return 0;
    }

    @Override
    public boolean canPause()
    {
        if (mMediaPlayer != null)
        {
            return mMediaPlayer.canPause();
        }
        return false;
    }

    @Override
    public boolean canSeekBackward()
    {
        try
        {
            if (mMediaPlayer != null)
            {
                return mMediaPlayer.canSeekBackward();
            }
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString());
        }
        return false;
    }

    @Override
    public boolean canSeekForward()
    {
        try
        {
            if (mMediaPlayer != null)
            {
                return mMediaPlayer.canSeekForward();
            }
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString());
        }
        return false;
    }

    /** 等比全屏 */
    public static final int SCREEN_FULL_SCREEN = 0;

    /** 拉伸全屏 */
    public static final int SCREEN_STRETCH = 1;

    /** 等比裁剪 */
    public static final int SCREEN_CROP = 2;

    protected int screenType = SCREEN_FULL_SCREEN;

    /** seek,缓冲时显示的view */
    private View bufferView;

    // private boolean seeking;

    private boolean buffering;

    public int getScreenType()
    {
        return screenType;
    }

    @Override
    public boolean isFullScreen()
    {
        return screenType == SCREEN_STRETCH || screenType == SCREEN_CROP;
    }

    @Override
    public void switchFullScreen()
    {
        this.screenType = (this.screenType + 1) % 3;

        requestLayout();

        if (mMediaController != null && mMediaController.isShowing())
        {
            mMediaController.updateFullScreen();
        }
    }

    @Override
    public void setScreenType(int type)
    {
        if (type == SCREEN_STRETCH || type == SCREEN_CROP || type == SCREEN_FULL_SCREEN)
        {
            this.screenType = type;
            requestLayout();
            if (mMediaController != null && mMediaController.isShowing())
            {
                mMediaController.updateFullScreen();
            }
        }
    }

    /** 上一集 */
    @Override
    public boolean canPrev()
    {
        return false;
    }

    /** 上一集 */
    @Override
    public void playPrev()
    {

    }

    /** 下一集 */
    @Override
    public boolean canNext()
    {
        return false;
    }

    /** 下一集 */
    @Override
    public void playNext()
    {

    }

    @Override
    public String getTitle()
    {
        return "";
    }

    public void setBufferView(View bufferView)
    {
        this.bufferView = bufferView;
    }

    private void setBuffering(boolean b)
    {
        this.buffering = b;

        if (bufferView != null)
        {
            if (b)
            {
                bufferView.setVisibility(View.VISIBLE);

                // //响应超过手势后的时间间隔
                // if((SystemClock.elapsedRealtime() - timeGestureStart) >
                // GESTURE_MAX_DURATION_TIME)
                // {
                // // add by chuckcheng 20120911
                // // 增加这句，目的是为了解决“第一次缓冲，不显示控制条，圈圈不显示出来”，原因还不知道
                // showMediaControls();
                // }
            }
            else
            {
                bufferView.setVisibility(View.INVISIBLE);
            }
        }

        if (mMediaController != null && mMediaController.isShowing()
                && (SystemClock.elapsedRealtime() - timeGestureStart) > GESTURE_MAX_DURATION_TIME)
        {
            mMediaController.show();
        }

        if (b)
        {
            bufferingStart();
        }
        else
        {
            bufferingEnd();
        }
    }

    @Override
    public boolean isBuffering()
    {
        return buffering;
    }

    /** 开始缓冲 */
    protected void bufferingStart()
    {

    }

    /** 缓冲结束 */
    protected void bufferingEnd()
    {

    }


	@Override
	public void onChoose(String packageSelected, double price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		
	}


	
}

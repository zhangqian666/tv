package com.ott.player;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.base.BaseActivity;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


// 1) power
//    a) onPause 调用 mVideoView.suspend();
//    b) onResume，没有解锁就onResume了  等解锁了再调用 mVideoView.resume();
// 2) home
//    a) onPause-surfaceDestroyed-onStop
//    b) onStart-onResume-surfaceCreated
// 播放状态下 suspend resume就够用了
// 但是暂停状态下，resume回来是黑屏的

/**
 * @author chuckcheng
 * @version [版本号, 2012-5-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class BasePlayerActivity extends BaseActivity implements BaseMediaPlayer.OnPreparedListener,
        BaseMediaPlayer.OnCompletionListener, BaseMediaPlayer.OnErrorListener, BaseMediaPlayer.OnSeekCompleteListener
{
    protected BaseVideoView mVideoView;

    protected BaseMediaLoading mLoadingView;

    /** 手势 */
    // private GestureDetector gestureDetector = new GestureDetector(new
    // GestureDetector.SimpleOnGestureListener()
    // {
    // public boolean onDoubleTap(MotionEvent e)
    // {
    // LogUtils.error("onDoubleTap");
    //
    // return BasePlayerActivity.this.onDoubleTap();
    // }
    //
    // public boolean onSingleTapConfirmed(MotionEvent e)
    // {
    // LogUtils.error("onSingleTapConfirmed");
    //
    // return BasePlayerActivity.this.onSingleTapConfirmed();
    // }
    //
    // public void onLongPress(MotionEvent e)
    // {
    // }
    // });

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent == null)
            {
                return;
            }
            LogUtils.error("action:" + intent.getAction());
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction()))
            {
                // 解锁
                resume();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 全屏
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();

        if (!getParam())
        {
            finish();
        }

        registerReceiver();
    }

    /** single instance新发过来的uri，会通过这个方法获取 */
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        // 更新intent
        setIntent(intent);

        if (mVideoView != null)
        {
            mVideoView.stopPlayback();
        }

        if (!getParam())
        {
            finish();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        LogUtils.error("onStart");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LogUtils.error("onResume");

        if (!isScreenLocked(this))
        {
            // 锁屏状态下不resume
            resume();
        }
    }

    private void resume()
    {
        if (mVideoView != null)
        {
            // mVideoView.resume();

            // 如果不start，暂停-home/power-回来是黑的
            mVideoView.start();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        LogUtils.error("onPause");

        if (mVideoView != null)
        {
            // mVideoView.suspend();
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        LogUtils.error("onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        LogUtils.error("onDestroy");

        uninit();

    }

    /** 初始化 */
    protected abstract void init();

    /** 注册广播 */
    protected void registerReceiver()
    {
        try
        {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_USER_PRESENT);
            registerReceiver(receiver, filter);
        }
        catch (Exception e)
        {
        }
    }

    protected void uninit()
    {
        if (mVideoView != null)
        {
            mVideoView.stopPlayback();
        }
        unregisterReceiver();
    }

    protected void unregisterReceiver()
    {
        try
        {
            unregisterReceiver(receiver);
        }
        catch (Exception e)
        {
        }
    }

    /** 获取参数 */
    protected boolean getParam()
    {
        Intent intent = getIntent();
        if (intent == null)
        {
            return false;
        }

        if (mLoadingView != null)
        {
            mLoadingView.show("");
        }

        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action))
        {
            Uri uri = getIntent().getData();

            if (uri == null)
            {
                return false;
            }
            LogUtils.error("uri:" + uri);

            String scheme = uri.getScheme();
            if ("file".equals(scheme) || "http".equals(scheme))
            {
                if (mVideoView != null)
                {
                    mVideoView.setVideoURI(null, uri, "");
                    // mVideoView.start();
                }
                return true;
            }
        }

        return false;
    }

    /**
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(BaseMediaPlayer mp, int what, int extra)
    {
        LogUtils.error("what:" + what + " ,extra:" + extra);
        return false;
    }

    /**
     * 准备完成,可以播放了
     * 
     * @param mp
     */
    @Override
    public void onPrepared(BaseMediaPlayer mp)
    {
        LogUtils.error("onPrepared");

        if (mLoadingView != null)
        {
            mLoadingView.hide();
        }

        if (mVideoView != null)
        {
            mVideoView.start();
        }
    }

    /**
     * @param mp
     */
    @Override
    public void onCompletion(BaseMediaPlayer mp)
    {
        LogUtils.error("onCompletion");
    }

    @Override
    public void onSeekComplete(BaseMediaPlayer mp)
    {
    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event)
    // {
    // return gestureDetector.onTouchEvent(event);
    // }
    //
    // /** 双击 */
    // public boolean onDoubleTap()
    // {
    // LogUtils.error("onDoubleTap");
    //
    // if (mVideoView != null)
    // {
    // mVideoView.switchFullScreen();
    // }
    //
    // return true;
    // }
    //
    // /** 单击 */
    // public boolean onSingleTapConfirmed()
    // {
    // LogUtils.error("onSingleTapConfirmed");
    //
    // if (mVideoView != null)
    // {
    // mVideoView.toggleMediaControlsVisiblity();
    // }
    // return true;
    // }

    /**
     * 是否处于锁屏状态
     * 
     * @param c 上下文
     * @return True 锁屏状态
     * @see [类、类#方法、类#成员]
     */
    public static final boolean isScreenLocked(Context c)
    {
        android.app.KeyguardManager mKeyguardManager = (KeyguardManager) c.getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }
}

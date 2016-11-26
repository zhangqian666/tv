package com.ott.player;

import java.lang.reflect.Method;

import com.iptv.common.utils.LogUtils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.view.SurfaceHolder;

/**
 * @version 20120904 4.0 删除了resume和suspend
 */
public class AndroidMediaPlayer extends BaseMediaPlayer
{
    // Playback capabilities.
    private static final int PAUSE_AVAILABLE = 29; // Boolean

    private static final int SEEK_BACKWARD_AVAILABLE = 30; // Boolean

    private static final int SEEK_FORWARD_AVAILABLE = 31; // Boolean

    private MediaPlayer mMediaPlayer = null;

    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener()
    {
        @Override
        public void onPrepared(MediaPlayer mp)
        {
            LogUtils.debug("onPrepared");

            // Get the capabilities of the player for this stream
            // Metadata data = mp.getMetadata(MediaPlayer.METADATA_ALL,
            // MediaPlayer.BYPASS_METADATA_FILTER);
            // Metadata data = null;
            Object data = null;
            try
            {
                Method getMetadataMethod = MediaPlayer.class.getDeclaredMethod("getMetadata", boolean.class,
                        boolean.class);
                getMetadataMethod.setAccessible(true);
                // data = (Metadata) getMetadataMethod.invoke(mp, false,
                // false);
                data = getMetadataMethod.invoke(mp, false, false);
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString(), e);
            }

            if (data != null)
            {
                // mCanPause = !data.has(Metadata.PAUSE_AVAILABLE) ||
                // data.getBoolean(Metadata.PAUSE_AVAILABLE);
                // mCanSeekBack = !data.has(Metadata.SEEK_BACKWARD_AVAILABLE)
                // || data.getBoolean(Metadata.SEEK_BACKWARD_AVAILABLE);
                // mCanSeekForward =
                // !data.has(Metadata.SEEK_FORWARD_AVAILABLE)
                // || data.getBoolean(Metadata.SEEK_FORWARD_AVAILABLE);
                try
                {
                    Class clazz = data.getClass();
                    Method hasMethod = clazz.getDeclaredMethod("has", int.class);
                    hasMethod.setAccessible(true);

                    Method getBooleanMethod = clazz.getDeclaredMethod("getBoolean", int.class);
                    getBooleanMethod.setAccessible(true);

                    mCanPause = !(Boolean) hasMethod.invoke(data, PAUSE_AVAILABLE)
                            || (Boolean) getBooleanMethod.invoke(data, PAUSE_AVAILABLE);
                    mCanSeekBack = !(Boolean) hasMethod.invoke(data, SEEK_BACKWARD_AVAILABLE)
                            || (Boolean) getBooleanMethod.invoke(data, SEEK_BACKWARD_AVAILABLE);
                    mCanSeekForward = !(Boolean) hasMethod.invoke(data, SEEK_FORWARD_AVAILABLE)
                            || (Boolean) getBooleanMethod.invoke(data, SEEK_FORWARD_AVAILABLE);
                }
                catch (Exception e)
                {
                    LogUtils.error(e.toString(), e);
                    mCanPause = mCanSeekBack = mCanSeekForward = true;
                }
            }
            else
            {
                mCanPause = mCanSeekBack = mCanSeekForward = true;
            }

            // mVideoWidth = mp.getVideoWidth();
            // mVideoHeight = mp.getVideoHeight();

            AndroidMediaPlayer.super.onPrepared();
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(MediaPlayer mp)
        {
            AndroidMediaPlayer.super.onCompletion();
        }
    };

    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener()
    {
        @Override
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err)
        {
            LogUtils.error("Error: " + framework_err + "," + impl_err);

            return AndroidMediaPlayer.super.onError(framework_err, impl_err);
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener()
    {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent)
        {
            AndroidMediaPlayer.super.onBufferingUpdate(percent);
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener()
    {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height)
        {

            // mVideoWidth = width;// mp.getVideoWidth();
            // mVideoHeight = height;// mp.getVideoHeight();

            AndroidMediaPlayer.super.onVideoSizeChanged(width, height);
        }
    };

    private MediaPlayer.OnSeekCompleteListener mSeekCompleteListerner = new MediaPlayer.OnSeekCompleteListener()
    {
        @Override
        public void onSeekComplete(MediaPlayer mp)
        {
            AndroidMediaPlayer.super.onSeekComplete();
        }
    };

    private MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener()
    {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra)
        {
            return AndroidMediaPlayer.super.onInfo(what, extra);
        }
    };

    public AndroidMediaPlayer(Context mContext)
    {
        super(mContext);
    }

    // 20120904 4.0 删除了resume和suspend
    // @Override
    // public void suspend()
    // {
    // if (isInPlaybackState())
    // {
    // // if (mMediaPlayer.suspend())
    // if (suspend(mMediaPlayer))
    // {
    // mStateWhenSuspended = mCurrentState;
    // mCurrentState = STATE_SUSPEND;
    // mTargetState = STATE_SUSPEND;
    // }
    // else
    // {
    // release(false);
    // mCurrentState = STATE_SUSPEND_UNSUPPORTED;
    // LogUtils.error("Unable to suspend video. Release MediaPlayer.");
    // }
    // }
    // }

    // @Override
    // public boolean resume(SurfaceHolder mSurfaceHolder)
    // {
    // if (mSurfaceHolder == null && mCurrentState == STATE_SUSPEND)
    // {
    // mTargetState = STATE_RESUME;
    // return true;
    // }
    // if (mMediaPlayer != null && mCurrentState == STATE_SUSPEND)
    // {
    // // if (mMediaPlayer.resume())
    // if (resume(mMediaPlayer))
    // {
    // mCurrentState = mStateWhenSuspended;
    // mTargetState = mStateWhenSuspended;
    // return true;
    // }
    // else
    // {
    // LogUtils.error("Unable to resume video");
    // return false;
    // }
    // // return true;
    // }
    // if (mCurrentState == STATE_SUSPEND_UNSUPPORTED)
    // {
    // // openVideo(mSurfaceHolder);
    // return false;
    // }
    // return true;
    // }

    // private boolean resume(MediaPlayer mediaPlayer)
    // {
    // Class<MediaPlayer> clazz = MediaPlayer.class;
    // try
    // {
    // Method resume = clazz.getDeclaredMethod("resume");
    // resume.setAccessible(true);
    // return (Boolean) resume.invoke(mediaPlayer);
    // }
    // catch (Exception e)
    // {
    // LogUtils.error(e.toString(), e);
    // }
    //
    // return false;
    // }
    //
    // private boolean suspend(MediaPlayer mediaPlayer)
    // {
    // Class<MediaPlayer> clazz = MediaPlayer.class;
    // try
    // {
    // Method suspend = clazz.getDeclaredMethod("suspend");
    // suspend.setAccessible(true);
    // return (Boolean) suspend.invoke(mediaPlayer);
    // }
    // catch (Exception e)
    // {
    // LogUtils.error(e.toString(), e);
    // }
    //
    // return false;
    // }

    @Override
    public void stopPlayback()
    {
        LogUtils.debug("###");

        if (mMediaPlayer != null)
        {

            try
            {
                mMediaPlayer.stop();
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString(), e);
            }

            try
            {
                mMediaPlayer.release();
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString(), e);
            }

            mMediaPlayer = null;

        }
    }

    @Override
    public void setDisplay(SurfaceHolder mSurfaceHolder, boolean isOMXSurface)
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.setDisplay(mSurfaceHolder);
        }
    }

    @Override
    public boolean openVideo(SurfaceHolder mSurfaceHolder, Uri mUri, boolean isOMXSurface) throws Exception
    {
        // Tell the music playback service to pause
        // TODO: these constants need to be published somewhere in the framework.
        if(!isOMXSurface)
        {
            throw new Exception("Surface isn't a OMXSurface");
        }
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        mContext.sendBroadcast(i);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(mPreparedListener);
        mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);

        mMediaPlayer.setOnCompletionListener(mCompletionListener);
        mMediaPlayer.setOnErrorListener(mErrorListener);
        mMediaPlayer.setOnInfoListener(mInfoListener);
        mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
 
        mCurrentBufferPercentage = 0;

        mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListerner);

        // mMediaPlayer.setDataSource(getContext(), mUri, mHeaders);

        mMediaPlayer.setDataSource(mContext, mUri);
        mMediaPlayer.setDisplay(mSurfaceHolder);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setScreenOnWhilePlaying(true);
        mMediaPlayer.prepareAsync();

        return true;
    }

    /*
     * release the media player in any state
     */
    @Override
    public void release()
    {
        LogUtils.debug("###");

        if (mMediaPlayer != null)
        {
            // try
            // {
            // ???这个方法不调用，有没有问题
            // mMediaPlayer.reset();
            // }
            // catch (Exception e)
            // {
            // LogUtils.error(e.toString(), e);
            // }

            try
            {
                mMediaPlayer.release();
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString(), e);
            }

            // 异步
            // new ReleaseThread(mMediaPlayer).start();

            mMediaPlayer = null;
        }
    }

    static class ReleaseThread extends Thread
    {
        private MediaPlayer player;

        public ReleaseThread(MediaPlayer mMediaPlayer)
        {
            this.player = mMediaPlayer;
        }

        @Override
        public void run()
        {
            long now = SystemClock.elapsedRealtime();

            try
            {
                player.release();
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString(), e);
            }
            LogUtils.debug("release time:" + (SystemClock.elapsedRealtime() - now));
        }
    }

    @Override
    public boolean start()
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.start();
            return true;
        }

        return false;
    }

    @Override
    public boolean pause()
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.pause();
            return true;
        }
        return false;
    }

    // cache duration as mDuration for faster access
    @Override
    public int getDuration()
    {
        if (mMediaPlayer != null)
        {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition()
    {
        if (mMediaPlayer != null)
        {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public boolean seekTo(int msec)
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.seekTo(msec);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPlaying()
    {
        if (mMediaPlayer != null)
        {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getBufferPercentage()
    {
        if (mMediaPlayer != null)
        {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    @Override
    public int getVideoWidth()
    {
        if (mMediaPlayer != null)
        {
            try
            {
                return mMediaPlayer.getVideoWidth();
            }
            catch (IllegalStateException e)
            {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    @Override
    public int getVideoHeight()
    {
        if (mMediaPlayer != null)
        {
            try
            {
                return mMediaPlayer.getVideoHeight();
            }
            catch (IllegalStateException e)
            {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

}

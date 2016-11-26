package com.ott.player;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceHolder;

/**
 * @version 20120904 4.0 删除了resume和suspend
 */
public abstract class BaseMediaPlayer
{
	public static final int MEDIA_ERROR_UNKNOWN = 1;
	
    /** 开始缓冲 */
    public static final int MEDIA_INFO_BUFFERING_START = 701;

    /** 结束缓冲 */
    public static final int MEDIA_INFO_BUFFERING_END = 702;

    /**
     * Interface definition of a callback to be invoked to communicate some
     * info and/or warning about the media or its playback.
     */
    public interface OnInfoListener
    {
        /**
         * Called to indicate an info or a warning.
         * 
         * @param mp the MediaPlayer the info pertains to.
         * @param what the type of info or warning.
         *            <ul>
         *            <li>{@link #MEDIA_INFO_UNKNOWN}
         *            <li>{@link #MEDIA_INFO_VIDEO_TRACK_LAGGING}
         *            <li>{@link #MEDIA_INFO_BAD_INTERLEAVING}
         *            <li>{@link #MEDIA_INFO_NOT_SEEKABLE}
         *            <li>{@link #MEDIA_INFO_METADATA_UPDATE}
         *            </ul>
         * @param extra an extra code, specific to the info. Typically
         *            implementation dependant.
         * @return True if the method handled the info, false if it didn't.
         *         Returning false, or not having an OnErrorListener at all,
         *         will cause the info to be discarded.
         */
        boolean onInfo(BaseMediaPlayer mp, int what, int extra);
    }

    /**
     * Interface definition for a callback to be invoked when the media source
     * is ready for playback.
     */
    public interface OnPreparedListener
    {
        /**
         * Called when the media file is ready for playback.
         * 
         * @param mp the MediaPlayer that is ready for playback
         */
        void onPrepared(BaseMediaPlayer mp);
    }

    /**
     * Interface definition for a callback to be invoked when playback of a
     * media source has completed.
     */
    public interface OnCompletionListener
    {
        /**
         * Called when the end of a media source is reached during playback.
         * 
         * @param mp the MediaPlayer that reached the end of the file
         */
        void onCompletion(BaseMediaPlayer mp);
    }

    /**
     * Interface definition of a callback to be invoked when there has been an
     * error during an asynchronous operation (other errors will throw
     * exceptions at method call time).
     */
    public interface OnErrorListener
    {
        /**
         * Called to indicate an error.
         * 
         * @param mp the MediaPlayer the error pertains to
         * @param what the type of error that has occurred:
         *            <ul>
         *            <li>{@link #MEDIA_ERROR_UNKNOWN}
         *            <li>{@link #MEDIA_ERROR_SERVER_DIED}
         *            </ul>
         * @param extra an extra code, specific to the error. Typically
         *            implementation dependant.
         * @return True if the method handled the error, false if it didn't.
         *         Returning false, or not having an OnErrorListener at all,
         *         will cause the OnCompletionListener to be called.
         */
        boolean onError(BaseMediaPlayer mp, int what, int extra);
    }

    /**
     * Interface definition of a callback to be invoked when the video size is
     * first known or updated
     */
    public interface OnVideoSizeChangedListener
    {
        /**
         * Called to indicate the video size
         * 
         * @param mp the MediaPlayer associated with this callback
         * @param width the width of the video
         * @param height the height of the video
         */
        public void onVideoSizeChanged(BaseMediaPlayer mp, int width, int height);
    }

    /**
     * Interface definition of a callback to be invoked indicating the
     * completion of a seek operation.
     */
    public interface OnSeekCompleteListener
    {
        /**
         * Called to indicate the completion of a seek operation.
         * 
         * @param mp the MediaPlayer that issued the seek operation
         */
        public void onSeekComplete(BaseMediaPlayer mp);
    }

    /**
     * Interface definition of a callback to be invoked indicating buffering
     * status of a media resource being streamed over the network.
     */
    public interface OnBufferingUpdateListener
    {
        /**
         * Called to update status in buffering a media stream.
         * 
         * @param mp the MediaPlayer the update pertains to
         * @param percent the percentage (0-100) of the buffer that has been
         *            filled thus far
         */
        void onBufferingUpdate(BaseMediaPlayer mp, int percent);
    }

    // public int mVideoWidth;

    // public int mVideoHeight;

    protected boolean mCanPause;

    protected boolean mCanSeekBack;

    protected boolean mCanSeekForward;

    protected int mCurrentBufferPercentage;

    // protected int mStateWhenSuspended; // state before calling suspend()

    private BaseMediaPlayer.OnPreparedListener mOnPreparedListener;

    private BaseMediaPlayer.OnCompletionListener mOnCompletionListener;

    private BaseMediaPlayer.OnErrorListener mOnErrorListener;

    private BaseMediaPlayer.OnVideoSizeChangedListener mOnSizeChangedListener;

    private BaseMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;

    private BaseMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;

    private BaseMediaPlayer.OnInfoListener mOnInfoListener;

    protected Context mContext;

    public BaseMediaPlayer(Context mContext)
    {
        this.mContext = mContext;
    }

    protected void onPrepared()
    {
        if (mOnPreparedListener != null)
        {
            mOnPreparedListener.onPrepared(BaseMediaPlayer.this);
        }
    }

    protected void onCompletion()
    {
        if (mOnCompletionListener != null)
        {
            mOnCompletionListener.onCompletion(BaseMediaPlayer.this);
        }
    }

    protected boolean onError(int framework_err, int impl_err)
    {
        /* If an error handler has been supplied, use it and finish. */
        if (mOnErrorListener != null)
        {
            if (mOnErrorListener.onError(BaseMediaPlayer.this, framework_err, impl_err))
            {
                return true;
            }
        }

        return true;
    }

    protected void onBufferingUpdate(int percent)
    {
        mCurrentBufferPercentage = percent;

        if (mOnBufferingUpdateListener != null)
        {
            mOnBufferingUpdateListener.onBufferingUpdate(this, percent);
        }
    }

    protected void onVideoSizeChanged(int width, int height)
    {
        if (mOnSizeChangedListener != null)
        {
            mOnSizeChangedListener.onVideoSizeChanged(BaseMediaPlayer.this, width, height);
        }
    }

    protected void onSeekComplete()
    {
        if (mOnSeekCompleteListener != null)
        {
            mOnSeekCompleteListener.onSeekComplete(this);
        }
    }

    protected boolean onInfo(int what, int extra)
    {
        if (mOnInfoListener != null)
        {
            return mOnInfoListener.onInfo(BaseMediaPlayer.this, what, extra);
        }
        return false;
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

    public void setOnSizeChangedListener(BaseMediaPlayer.OnVideoSizeChangedListener l)
    {
        this.mOnSizeChangedListener = l;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener listener)
    {
        this.mOnSeekCompleteListener = listener;
    }

    /**
     * Register a callback to be invoked when the status of a network stream's
     * buffer has changed.
     * 
     * @param listener the callback that will be run.
     */
    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener)
    {
        mOnBufferingUpdateListener = listener;
    }

    /**
     * Register a callback to be invoked when an info/warning is available.
     * 
     * @param listener the callback that will be run
     */
    public void setOnInfoListener(OnInfoListener listener)
    {
        mOnInfoListener = listener;
    }

    /** 是否在可播放状态 */
    // public abstract boolean isInPlaybackState();

    // public abstract void suspend();

    // public abstract boolean resume(SurfaceHolder mSurfaceHolder);

    public abstract void stopPlayback();

    public abstract void setDisplay(SurfaceHolder mSurfaceHolder, boolean isOMXSurface);

    public abstract boolean openVideo(SurfaceHolder mSurfaceHolder, Uri mUri, boolean isOMXSurface) throws Exception;

    /*
     * release the media player in any state
     */
    public abstract void release();

    public abstract boolean start();

    public abstract boolean pause();

    // cache duration as mDuration for faster access
    public abstract int getDuration();

    public abstract int getCurrentPosition();

    public abstract boolean seekTo(int msec);

    public abstract boolean isPlaying();

    public abstract int getBufferPercentage();

    /**
     * Returns the width of the video.
     * 
     * @return the width of the video, or 0 if there is no video, no display
     *         surface was set, or the width has not been determined yet. The
     *         OnVideoSizeChangedListener can be registered via
     *         {@link #setOnVideoSizeChangedListener(OnVideoSizeChangedListener)}
     *         to provide a notification when the width is available.
     */
    public abstract int getVideoWidth();

    /**
     * Returns the height of the video.
     * 
     * @return the height of the video, or 0 if there is no video, no display
     *         surface was set, or the height has not been determined yet. The
     *         OnVideoSizeChangedListener can be registered via
     *         {@link #setOnVideoSizeChangedListener(OnVideoSizeChangedListener)}
     *         to provide a notification when the height is available.
     */
    public abstract int getVideoHeight();

    public boolean canPause()
    {
        return mCanPause;
    }

    public boolean canSeekBackward()
    {
        return mCanSeekBack;
    }

    public boolean canSeekForward()
    {
        return mCanSeekForward;
    }

}

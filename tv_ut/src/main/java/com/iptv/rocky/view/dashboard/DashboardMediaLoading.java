package com.iptv.rocky.view.dashboard;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.iptv.rocky.R;
import com.ott.player.BaseMediaLoading;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 播放器loading界面
 */
public class DashboardMediaLoading extends BaseMediaLoading
{
    public TextView progressTextView;
    public View mLoadingView;
    public TextView mSpeedView;
    public TextView mLoadingFrom;
    private WifiManager wifiManager;
    public Context mContext;
    public ImageView loadingAD;
    public ImageView progressBar;
    public View webiewlayout;
    
    public Bitmap virtualAD;

    public DashboardMediaLoading(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void initView()
    {
        textView = (TextView) findViewById(R.id.tv_media_loading_text);
        progressTextView = (TextView) findViewById(R.id.tv_media_loading_progress);
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        progressBar = (ImageView) this.findViewById(R.id.progressbar);
        setLoadingAnim();
        mLoadingView = this;
    }
    
    private void setLoadingAnim()
    {
        if (progressBar != null)
        {
            AnimationSet animationSet = new AnimationSet(true);
            RotateAnimation ra = new RotateAnimation(0, 360,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(1000);
            ra.setRepeatCount(Animation.INFINITE);
            animationSet.addAnimation(ra);
            
            progressBar.setAnimation(animationSet);
            animationSet.startNow();
        }
    }

    @Override
    public void hide()
    {
        if (this.getParent() != null)
        {
            this.getParent().bringChildToFront(this);
        }

        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mLoadingView.setVisibility(View.GONE);
            }
        }, 1000);
    }

    @Override
    public void show(String text)
    {
        if (this.getParent() != null)
        {
            this.getParent().bringChildToFront(this);
        }

        super.show(text);

        setProgress("");
    }
    
    public Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            setSpeedTxt();
            this.sendMessageDelayed(new Message(), 1000);
            
            if (msg.what == 1)
            {
                loadingAD.setImageBitmap(virtualAD);
            }
            super.handleMessage(msg);
        }
    };
    
    public void setSpeedTxt()
    {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int speedMbps = wifiInfo.getLinkSpeed();
        if (mSpeedView != null)
        {
            mSpeedView.setText("" + speedMbps + "Kb/s");
        }
    }

    /** 设置进度 */
    public void setProgress(final String progress)
    {
        if (progressTextView != null)
        {
            ((Activity) getContext()).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    progressTextView.setText(progress);
                }
            });
        }
    }
    
    private String getVirtualADUrl(Context context)
    {
        return null;
    }
    
    public Bitmap getbitmap()
    {
        Bitmap bitmap = null;
        String imageUri = getVirtualADUrl(mContext);
        if (imageUri == null)
        {
            return null;
        }
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    
    public void loadVirtualADImg()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                virtualAD = getbitmap();
                if (virtualAD != null)
                {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }.start();
    }
}

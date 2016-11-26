package com.iptv.rocky;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.VideoScreenProtect;
import com.iptv.common.data.VideoScreenProtectInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.json.VideoScreenProtectFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.tcl.view.vod.VideoPlayer;
import com.iptv.rocky.utils.BackgroundMusic;


/**
 * 用于视频类播放的屏保
 * 
 *
 */
public class ScreenProtectVideoActivity extends Activity{

	private VideoScreenProtectFactory mVideoScreenProtectFactory;
	
	private String preStatus;
	
    private VideoPlayer VideoPlayer;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		quiteBackgroundMusic();
		
    	/*mVideoScreenProtectFactory = new VideoScreenProtectFactory();
    	mVideoScreenProtectFactory.setHttpEventHandler(screenProtectHandler);
    	mVideoScreenProtectFactory.DownloadDatas(); */ 
        setContentView(R.layout.use_widget_activity);
        preStatus = TvApplication.status;
		TvApplication.status = "SCREENPROTECT";
        init();
    }
    
    
    private void init(){
        VideoPlayer = (VideoPlayer) findViewById(R.id.base_video_player);
        ArrayList<String> path = new ArrayList<String>();

        path.add("http://10.0.2.211:1935/vod/mp4:sample.mp4/playlist.m3u8");
        // path.add("http://10.144.0.2:1935/vod/mp4:sample.mp4/playlist.m3u8");

        VideoPlayer.setDisplay(path);
        TextView text = (TextView) findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoPlayer.setFullScreenSwitcher();
            }
        });
    }

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
         
    }

    public void onPause(){
        super.onPause();
        try {
            VideoPlayer.pause();
        }catch (Exception e) {e.printStackTrace();}
    }

    public void onStop(){
        super.onStop();
        try {
            VideoPlayer.stop();
        }catch (Exception e) {e.printStackTrace();}
    }

    public void onDestroy(){
        super.onDestroy();
        try {
            VideoPlayer.release();
        }catch (Exception e) {
        	e.printStackTrace();
        }
        TvApplication.status = preStatus;
        //add 播放背景音乐
      	BackgroundMusic.getInstance(this).playBackgroundMusic("music/background_ex.mp3", true);
    }
    
	private void quiteBackgroundMusic() {
		if (BackgroundMusic.getInstance(this).isBackgroundMusicPlaying()) {
			BackgroundMusic.getInstance(this).stopBackgroundMusic();
		}
	}
	
	
	/**
	 * 接收屏保数据
	 */
	private HttpEventHandler<VideoScreenProtect> screenProtectHandler = new HttpEventHandler<VideoScreenProtect>() {

		@Override
		public void HttpSucessHandler(VideoScreenProtect result) {
			LogUtils.error("保护数据下载成功，开始播放");
			/* VideoPlayer = (VideoPlayer) findViewById(R.id.base_video_player);
	        ArrayList<String> path = new ArrayList<String>();

	        for(VideoScreenProtectInfo info: result.getInfos()){
	        	//path.add("http://10.144.0.2:1935/vod/mp4:sample.mp4/playlist.m3u8");
	        	path.add(info.getUrl());
	        }
	        //path.add("http://10.144.0.2:1935/vod/mp4:sample.mp4/playlist.m3u8");
	        //path.add("http://10.144.0.2:1935/vod/mp4:sample.mp4/playlist.m3u8");
	        
	        VideoPlayer.setDisplay(path);
	        
	        TextView text = (TextView) findViewById(R.id.text);
	        text.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                VideoPlayer.setFullScreenSwitcher();
	            }
	        });
	        init();*/
		}

		@Override
		public void HttpFailHandler() {
			LogUtils.error("下载屏保数据错误");
		}
	};
	
}

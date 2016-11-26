package com.ui.player.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;

public class ChoosePlayPosition extends RelativeLayout {

	private static final String ACTION_STARTLOGINHOTELSERVER = "com.virgintelecom.action.STARTLOGINHOTELSERVER";

	private TextView info;
	private Button btnPlayFromStart;
	private Button btnPlayFromPosition;
	private StartPlayOrderedVodListener mStartPlayOrderedVodListener;
	

	// 是否启用选择互动和纯直播模式
	private boolean displayChooseInteractive = false;

	public ChoosePlayPosition(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	
	@Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
		
		btnPlayFromStart = (Button) findViewById(R.id.play_from_start);
		btnPlayFromStart.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				mStartPlayOrderedVodListener.startPlay(true);
			}
		});
		btnPlayFromStart.setFocusable(true);
		
		
		btnPlayFromPosition= (Button) findViewById(R.id.play_from_position);
		btnPlayFromPosition.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				mStartPlayOrderedVodListener.startPlay(false);
			}
		});
    }    
	
	/**
	 * 初始化焦点
	 */
	public void initFocus(){
		btnPlayFromStart.requestFocus();
	}
	
	public interface StartPlayOrderedVodListener{
		void startPlay(boolean fromStart);
	}
 
	/**
	 * Register a callback to be invoked when the media source is ready for playback.
	 */
	public void setStartPlayOrderedVodListener(StartPlayOrderedVodListener listener) {
		mStartPlayOrderedVodListener = listener;
	}


}

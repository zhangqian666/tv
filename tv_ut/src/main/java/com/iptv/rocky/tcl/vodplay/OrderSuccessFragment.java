package com.iptv.rocky.tcl.vodplay;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;

public class OrderSuccessFragment extends Fragment {

	private static final String ACTION_STARTLOGINHOTELSERVER = "com.virgintelecom.action.STARTLOGINHOTELSERVER";

	private TextView info;
	private Button btnPlayFromStart;
	private Button btnPlayFromPosition;
	private StartPlayOrderedVodListener mStartOrderPlayedVodListener;
	

	// 是否启用选择互动和纯直播模式
	private boolean displayChooseInteractive = false;

	/** Fragment第一次附属于Activity时调用,在onCreate之前调用 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mStartOrderPlayedVodListener = (StartPlayOrderedVodListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_vod_order_success,container, false);
		btnPlayFromStart = (Button) view.findViewById(R.id.play_from_start);
		btnPlayFromStart.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				LogUtils.debug("从头播放.");
				mStartOrderPlayedVodListener.startPlay(true);
			}
		});
		btnPlayFromStart.setFocusable(true);
		btnPlayFromStart.requestFocus();
		
		btnPlayFromPosition= (Button) view.findViewById(R.id.play_from_position);
		btnPlayFromPosition.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				LogUtils.debug("从指定位置开始播放.");
				mStartOrderPlayedVodListener.startPlay(false);
			}
		});
		
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	public interface StartPlayOrderedVodListener {
		public void startPlay(boolean fromStart);
	}

}

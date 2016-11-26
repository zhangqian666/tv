package com.iptv.rocky.view.splash;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.MarqueeTextView;
import com.iptv.rocky.R;

public class ScrollingMessageFragment extends Fragment {

	private MarqueeTextView tv;

	//private TextView messageText;

	private View view;

	/** Fragment第一次附属于Activity时调用,在onCreate之前调用 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtils.debug("welcomeFragment--->onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		LogUtils.info("Fragment收到的消息"+bundle.getString("MESSAGE"));
		view = inflater.inflate(R.layout.fragment_scrolling_message,container, false);
		tv = (MarqueeTextView) view.findViewById(R.id.scollMessage);
		tv.setScrollText(bundle.getString("MESSAGE"));

		//messageText = (TextView) view.findViewById(R.id.scrolling_message);
		//messageText.setText(bundle.getString("MESSAGE"));
		//LogUtils.error("收到滚动信息:"+messageText.getText().toString());

		//startAnimation();

		return view;
	}


	@Override
	public void onPause() {
		LogUtils.error("PAUSE");
		super.onPause();
	}

	@Override
	public void onResume() {
		LogUtils.error("RESUME");
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	/**
	 * 显示消息
	 * 
	 */
	public void showMessage(String message){
			tv.setScrollText(message);
	}
}

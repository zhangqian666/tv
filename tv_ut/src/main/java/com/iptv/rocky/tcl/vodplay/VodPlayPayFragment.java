package com.iptv.rocky.tcl.vodplay;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;

public class VodPlayPayFragment extends Fragment {

	private static final String ACTION_STARTLOGINHOTELSERVER = "com.virgintelecom.action.STARTLOGINHOTELSERVER";

	private EditText passwordEditText;
	private TextView info;
	private Button btnStartOrder;
	private StartOrderPlayedVodListener mStartOrderPlayedVodListener;
	

	// 是否启用选择互动和纯直播模式
	private boolean displayChooseInteractive = false;

	/** Fragment第一次附属于Activity时调用,在onCreate之前调用 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtils.debug("welcomeFragment--->onAttach");
		mStartOrderPlayedVodListener = (StartOrderPlayedVodListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_vodplay_order_input_number,container, false);
		
		/*passwordEditText = (EditText) view.findViewById(R.id.btn_vodplay_order_by_password);
		String password = passwordEditText.getText().toString();
		
		btnStartOrder = (Button) view.findViewById(R.id.btn);
		btnStartOrder.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				LogUtils.error("点击购买键。下一步动作：停止播放、弹出购买的fragment.");
				mStartOrderPlayedVodListener.startOrder(1);
			}
		});*/
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

	/** Acitivity要实现这个接口，这样Fragment和Activity就可以共享事件触发的资源了 */
	public interface StartOrderPlayedVodListener {
		public void startOrder(int index);
	}

}

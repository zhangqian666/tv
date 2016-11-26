package com.iptv.rocky.model.voddetail;

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
import com.iptv.rocky.tcl.vodplay.OrderStatusFragment.StartOrderPlayedVodListener;


public class ChooseOrderMethodFragment extends Fragment {

	private static final String ACTION_STARTLOGINHOTELSERVER = "com.virgintelecom.action.STARTLOGINHOTELSERVER";

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

		View view = inflater.inflate(R.layout.fragment_voddetail_choose_order_method,container, false);
		/*btnStartOrder = (Button) view.findViewById(R.id.btn_vodplay_start_order);
		btnStartOrder.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				LogUtils.debug("点击购买键。下一步动作：停止播放、弹出购买的fragment.");
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

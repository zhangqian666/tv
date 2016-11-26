package com.iptv.rocky.view.voddetail.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.iptv.rocky.R;


public class VodDetailChoosePackageFragment extends Fragment {

	//private static final String ACTION_STARTLOGINHOTELSERVER = "com.virgintelecom.action.STARTLOGINHOTELSERVER";

	private TextView info;
	private EditText passwordEditText;
	
	private RadioGroup group;
	private Button btnStartOrderVod;
	private Button btnStartOrderDayPackage;
	
	private StartPayOrderedPackageListener mStartPayOrderedPackageListener;
	private int programId;
	private double price;
	private String packageSelected;
	
	private RadioButton radioBtnChooseVod;
	private RadioButton radioBtnChooseDay;

	// 是否启用选择互动和纯直播模式
	private boolean displayChooseInteractive = false;

	/** Fragment第一次附属于Activity时调用,在onCreate之前调用 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mStartPayOrderedPackageListener = (StartPayOrderedPackageListener)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		packageSelected = "DAY";
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_vodplay_choose_package,container, false);

		Bundle bundle = getArguments();
		programId = bundle.getInt("VODID");
		price = bundle.getDouble("PRICE");
		
		//passwordEditText = (EditText) view.findViewById(R.id.vodplay_password);
		group = (RadioGroup) view.findViewById(R.id.radio_group1);
		//radioBtnChooseVod= (RadioButton) view.findViewById(R.id.radio_per_vod);
		
		radioBtnChooseDay =(RadioButton) view.findViewById(R.id.radio_per_day);
		if(packageSelected.equals("DAY")){
			radioBtnChooseDay.setChecked(true);
		}else{
			radioBtnChooseVod.setChecked(true);
		}
		
		
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				/*if(checkedId == R.id.radio_per_day){
					packageSelected = "DAY";
				}else if(checkedId == R.id.radio_per_vod){
					packageSelected = "VOD";
				}else{
					packageSelected = "DAY";
				}*/
			}
		});
		
		btnStartOrderVod = (Button) view.findViewById(R.id.btn_vodplay_begin_pay);
		btnStartOrderVod.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(packageSelected.equals("DAY")){
					price = 50.0;
				}else if(packageSelected.equals("MONTH")){
					price = 200;
				}else if(packageSelected.equals("VOD")){
					if(price == 0){
						price = 5;
					}else{
						price = 10;
					}
				}
				mStartPayOrderedPackageListener.startChoosePayMethod(packageSelected,price);
			}
		});
		btnStartOrderVod.requestFocus();
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

/*	public interface StartPlayOrderedVodListener {
		public void startPlay(int index);
	}*/
	
	public interface StartPayOrderedPackageListener {
		public void startChoosePayMethod(String packageSelected,double price);
	}
	
	
/*	private HttpEventHandler<OrderVodResult> orderVodHandler = new HttpEventHandler<OrderVodResult>() {
		@Override
		public void HttpSucessHandler(OrderVodResult result) {
			// 成功
			if (result.getResult() == 200) {
			    LogUtils.error("成功购买， 开始正常播放");
			    mStartPayOrderedPackageListener.startChoosePayMethod(packageSelected,price);
			}else if(result.getResult() == 500 || result.getResult() == 503){
				//showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getReason());
			}else if(result.getResult() == 600){
				//showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getReason());
			}else{
				//showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getReason());
			}
		}

		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			//TvUtils.processHttpFail(view.this);
		}
	};
	
	private HttpEventHandler<OrderVodResult> orderDayPackageHandler = new HttpEventHandler<OrderVodResult>() {
		@Override
		public void HttpSucessHandler(OrderVodResult result) {
			// 成功
			if (result.getResult() == 200) {
			    
			    if(passwordEditText.getText().toString().equals("5300")){
					LogUtils.error("密码正确，开始观看");
					 mStartPayOrderedPackageListener.startChoosePayMethod(packageSelected,price);
				}
			}else if(result.getResult() == 500 || result.getResult() == 503){
				//showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getReason());
			}else if(result.getResult() == 600){
				//showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getReason());
			}else{
				//showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getReason());
			}
		}

		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			//TvUtils.processHttpFail(view.this);
		}
	};*/

}

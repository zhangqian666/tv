package com.iptv.rocky.tcl.vodplay;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.model.TvApplication;


public class VodPlayChoosePackageFragment extends Fragment {

	/*//private static final String ACTION_STARTLOGINHOTELSERVER = "com.virgintelecom.action.STARTLOGINHOTELSERVER";

	private TextView info;
	private EditText passwordEditText;*/

	private RadioGroup group;
	private Button btnStartOrderVod;
	private Button btnWeChatOrderVod;
//	private Button btnStartOrderDayPackage;

	private StartPayOrderedPackageListener mStartPayOrderedPackageListener;
	private double price;
	private VodDetailInfo vodDetailInfo = null;

	private String packageSelected="";
	private  String payType="";

	private RadioButton radioBtnChooseVod;
	private RadioButton radioBtnChooseDay;

	// 是否启用选择互动和纯直播模式
//	private boolean displayChooseInteractive = false;

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
		vodDetailInfo = (VodDetailInfo) bundle.getSerializable("VOD");
		price = vodDetailInfo.priceDisp;
		//price = 10.00;

		group = (RadioGroup) view.findViewById(R.id.radio_group1);
		radioBtnChooseVod= (RadioButton) view.findViewById(R.id.radio_per_vod);

		if(vodDetailInfo.ISSITCOM == 1){
			String fm =getResources().getString(R.string.vod_play_choose_package_fragment_package_vod_series);
			String out = String.format(fm, vodDetailInfo.priceDisp);
			radioBtnChooseVod.setText(out);
		}else{
			String fm =getResources().getString(R.string.vod_play_choose_package_fragment_package_vod);
			String out = String.format(fm, vodDetailInfo.priceDisp);
			radioBtnChooseVod.setText(out);
		}

		radioBtnChooseDay =(RadioButton) view.findViewById(R.id.radio_per_day);
		String fm =getResources().getString(R.string.vod_play_choose_package_fragment_package_day);
		LogUtils.error("产品包，每天应支付的价格"+TvApplication.daylyPayVodPrice);
		String out = String.format(fm, TvApplication.perDayPrice);
		radioBtnChooseDay.setText(out);


		radioBtnChooseDay =(RadioButton) view.findViewById(R.id.radio_per_day);
		if(packageSelected.equals("DAY")){
			radioBtnChooseDay.setChecked(true);
		}else{
			radioBtnChooseVod.setChecked(true);
		}

		radioBtnChooseDay.setChecked(true);

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.radio_per_day){
					packageSelected = "DAY";
				}else if(checkedId == R.id.radio_per_vod){
					packageSelected = "VOD";
				}else{
					packageSelected = "DAY";
				}
			}
		});

		//密码支付按钮
		btnStartOrderVod = (Button) view.findViewById(R.id.btn_vodplay_begin_pay);
		btnStartOrderVod.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(packageSelected.equals("DAY")){
					price = TvApplication.perDayPrice;
				}else if(packageSelected.equals("MONTH")){
					price = 200;
				}else if(packageSelected.equals("VOD")){
					price = vodDetailInfo.priceDisp;
				}
				payType="PassWord";
				mStartPayOrderedPackageListener.startChoosePayMethod(packageSelected,price,payType);
			}
		});
		//微信支付按钮
		btnWeChatOrderVod=(Button) view.findViewById(R.id.btn_vodplay_begin_pay_qrcode);
		btnWeChatOrderVod.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(packageSelected.equals("DAY")){
					price = TvApplication.perDayPrice;
				}else if(packageSelected.equals("MONTH")){
					price = 200;
				}else if(packageSelected.equals("VOD")){
					price = vodDetailInfo.priceDisp;
				}
				payType="WeChat";
				mStartPayOrderedPackageListener.startChoosePayMethod(packageSelected,price,payType);
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

	public interface StartPayOrderedPackageListener {
		public void startChoosePayMethod(String packageSelected,double price,String payType);
	}
}

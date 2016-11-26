package com.iptv.rocky.tcl.vodplay;


import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.ConfirmPasswordResult;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TextViewDip;
import com.ui.player.fragment.ChoosePayMethod.ChoosePlayPositionListener;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class VodPayByWeChatFragment extends Fragment {

	private TextViewDip skeyView;
	private TextView promptInputPassword;
	private String skeyViewText;
	private TextView orderedInfo;
	private View view;
	private VodDetailInfo vodDetailInfo;
	private ChoosePlayPositionListener choosePlayPositionListener;
	private String filePath;
	private double price;
	private String selectedPackage;
	//二维码显示 imageView
	private ImageView qrcode_iv;
	
//	public interface ChoosePlayPositionListener{
//		public void choosePlayPosition();
//	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_vod_pay_by_wechat, container,false);

		orderedInfo = (TextView) view.findViewById(R.id.txt_choosed_info);
		qrcode_iv=(ImageView) view.findViewById(R.id.qrcode_iv);
		Bundle bundle = getArguments();
		filePath=bundle.getString("FILEPATH");
		price = bundle.getDouble("PRICE");
		selectedPackage = bundle.getString("SELECTED_PACKAGE"); //选择的包天还是包片
		vodDetailInfo = (VodDetailInfo) bundle.getSerializable("VOD");
		String disp= "";
		if(selectedPackage.equals("VOD")){
			if(vodDetailInfo.ISSITCOM==1){
				String fm =getResources().getString(R.string.vod_play_choose_pay_method_package_choosed_series);
				disp = String.format(fm, price);
			}else{
				String fm =getResources().getString(R.string.vod_play_choose_pay_method_package_choosed_vod);
				disp = String.format(fm, price);
			}
		}else{
			String fm =getResources().getString(R.string.vod_play_choose_pay_method_package_choosed_day);
			disp = String.format(fm, price);
		}
		if (filePath!=null&&!filePath.equals("")) {
			qrcode_iv.setImageBitmap(BitmapFactory.decodeFile(filePath));
		}
		orderedInfo.setText(disp);

		return view;
	}

	@SuppressWarnings("unused")
	private HttpEventHandler<ConfirmPasswordResult> weChatQRcodeHandler = new HttpEventHandler<ConfirmPasswordResult>() {
		@Override
		public void HttpSucessHandler(ConfirmPasswordResult result) {
			if(result.isPasswordResult()){

				// 成功
				if (result.getOrderResult() == 200) {
					LogUtils.error("成功购买， 开始正常播放，后面加，播放成功，然后选择从头播放，从播放处开始播放");
					vodDetailInfo.ordered = true;
					choosePlayPositionListener.choosePlayPosition();

				}else if(result.getOrderResult() == 500){
					promptInputPassword.setVisibility(View.VISIBLE);
					promptInputPassword.setText("用户名不存在");
				}else{
					//showAlertDialog("认证错误，错误代码 "+result.getResult(),result.getReason());
				}
			}else{
				promptInputPassword.setVisibility(View.VISIBLE);
				promptInputPassword.setText(R.string.password_password_error);
			}

		}

		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			TvUtils.processHttpFail(view.getContext());
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {


		boolean result = true;

		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
			break;
		case KeyEvent.KEYCODE_1:
			result=true;
			break;
		case KeyEvent.KEYCODE_2:
			result=true;
			break;
		case KeyEvent.KEYCODE_3:
			result=true;
			break;
		case KeyEvent.KEYCODE_4:
			result=true;
			break;
		case KeyEvent.KEYCODE_5:
			result=true;
			break;
		case KeyEvent.KEYCODE_6:
			result=true;
			break;
		case KeyEvent.KEYCODE_7:
			result=true;
			break;
		case KeyEvent.KEYCODE_8:
			result=true;
			break;	
		case KeyEvent.KEYCODE_9:
			result=true;
			break;		

		case KeyEvent.KEYCODE_ENTER:
			result=true;
			break;
		case KeyEvent.KEYCODE_BACK:
//			qrcode_iv.setBackgroundResource(R.drawable.launcher_bg);
			result=true;
			break;
		case KeyEvent.KEYCODE_HOME:
			result=true;
			break;
		case 17:
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			result=true;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			result=true;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			result=true;

			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			result=true;
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			result=true;
			break;
		default:
			result=true;
			break;
		}
		return result;
	}
	public void skeyChanged() {
		skeyView.setText(skeyViewText);
		if (TextUtils.isEmpty(skeyViewText)) {
			//mSearchView.destroy();
			//viewBeginSearch();
			return;
		}
		//viewSearchResult();
		//mSearchView.DownloadDatas();
	}


}

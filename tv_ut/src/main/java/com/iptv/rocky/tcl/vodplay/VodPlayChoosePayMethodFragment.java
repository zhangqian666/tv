package com.iptv.rocky.tcl.vodplay;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.ConfirmPasswordResult;
import com.iptv.common.data.OrderVodResult;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.json.OrderDayPackageFactory;
import com.iptv.rocky.hwdata.json.OrderVodConfirmPasswordFactory;
import com.iptv.rocky.hwdata.json.OrderVodFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TextViewDip;



public class VodPlayChoosePayMethodFragment extends Fragment {

	private VodPlayPasswordKeyBoardView  keyboardView;

	private TextViewDip skeyView;
	private TextView promptInputPassword;
	private String skeyViewText;
	private TextView orderedInfo;
	
	private View view;
	
	private VodDetailInfo vodDetailInfo;
	private ChoosePlayPositionListener choosePlayPositionListener;
	private OrderVodConfirmPasswordFactory orderVodConfirmPasswordFactory;
	
	private String programId;
	private String programName;
	private double price;
	private String selectedPackage;
	private String payMethodSelected;
	

	// 是否启用选择互动和纯直播模式
	private boolean displayChooseInteractive = false;

	/** Fragment第一次附属于Activity时调用,在onCreate之前调用 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtils.debug("welcomeFragment--->onAttach");
		choosePlayPositionListener = (ChoosePlayPositionListener) activity;
		skeyViewText="";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_vodplay_choose_pay_method,container, false);
		
		keyboardView = (VodPlayPasswordKeyBoardView) view.findViewById(R.id.password_KeyBoard);

		skeyView = (TextViewDip) view.findViewById(R.id.vodplay_choose_pay_method_password_keyView);
		int textSize = ScreenUtils.getKeyTextSize();// 大小和键盘字母一样
		skeyView.setTextSize(textSize*2);
		
		orderedInfo = (TextView) view.findViewById(R.id.txt_choosed_info);
		
		Bundle bundle = getArguments();
		programId = bundle.getString("VODID");
		LogUtils.error("programId----"+programId);
		price = bundle.getDouble("PRICE");
		selectedPackage = bundle.getString("SELECTED_PACKAGE"); //选择的包天还是包片
		
		vodDetailInfo = (VodDetailInfo) bundle.getSerializable("VOD");
		programName = vodDetailInfo.VODNAME;
		
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
				
		orderedInfo.setText(disp);
		promptInputPassword = (TextView) view.findViewById(R.id.txt_prompt_input_password);
		
		initSkeyInput();
		initKeyBoardBtns();
		keyboardView.requestFocus();

		orderVodConfirmPasswordFactory = new OrderVodConfirmPasswordFactory();
		orderVodConfirmPasswordFactory.setHttpEventHandler(confirmPasswordHandler);
		
		
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

	public interface ChoosePlayPositionListener{
		public void choosePlayPosition();
	}
	
	private HttpEventHandler<ConfirmPasswordResult> confirmPasswordHandler = new HttpEventHandler<ConfirmPasswordResult>() {
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
	
	
	private void initSkeyInput() {
    	int width = (int) (TvApplication.pixelWidth / 5.0);
    	int height = (int) (TvApplication.pixelHeight / 10.0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.gravity = Gravity.CENTER;
        params.bottomMargin = ScreenUtils.getSearchKeyPadding();
        skeyView.setLayoutParams(params);
        LogUtils.info("参数:width:"+params.width +" height:"+params.height);
    }
	
	private void initKeyBoardBtns(){
        int keysBtnCount = keyboardView.getChildCount();
        for (int i = 0;i < keysBtnCount; i++) {
            Button btn = (Button)keyboardView.getChildAt(i);
            if (btn == null) {
                continue;
            }
            btn.setOnClickListener(searchKeyClickListener);
            if (btn.getTag().equals("delete")) {
                btn.setOnLongClickListener(removeKeyClickListener);
            }
        }
    }
	
	private OnClickListener searchKeyClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			LogUtils.error("标签:"+v.getTag().toString());
			if (v.getTag().equals("delete")) {
				if(skeyViewText.length()>0){
					skeyViewText = skeyViewText.substring(0, skeyViewText.length()-1);
	            	skeyChanged();
	            	promptInputPassword.setVisibility(View.INVISIBLE);
					promptInputPassword.setText("");
				}
            } else if(v.getTag().equals("OK")){
            	String password = skeyViewText;
				if(password != null){
					if(password != ""){
						if(password.length()>0 && password.length()<4){
							promptInputPassword.setVisibility(View.VISIBLE);
							promptInputPassword.setText(R.string.password_please_enter_4);
						}else if(password.length() == 4){
							int days=1;
							orderVodConfirmPasswordFactory.DownloadDatas(password,days, price,programId,programName,selectedPackage,(vodDetailInfo.ISSITCOM ==1));
						}else{
							promptInputPassword.setVisibility(View.VISIBLE);
							promptInputPassword.setText(R.string.password_please_enter_4);
						}
					}else{
						promptInputPassword.setVisibility(View.VISIBLE);
						promptInputPassword.setText(R.string.password_please_enter_4);
					}
				}else{
					promptInputPassword.setVisibility(View.VISIBLE);
					promptInputPassword.setText(R.string.password_please_enter_4);
				}
            }else {
            	if(skeyViewText.length()<4){
            		promptInputPassword.setVisibility(View.INVISIBLE);
					promptInputPassword.setText("");
            		skeyViewText = skeyViewText + v.getTag();
                	skeyChanged();
            	}else{
            		promptInputPassword.setVisibility(View.VISIBLE);
					promptInputPassword.setText(R.string.password_please_enter_4);
            	}
            }
		}
	};
	
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		
		boolean result = true;
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
			dealWithKeyInputPassword("0");
			result=true;
			break;
		case KeyEvent.KEYCODE_1:
			dealWithKeyInputPassword("1");
			result=true;
			break;
		case KeyEvent.KEYCODE_2:
			dealWithKeyInputPassword("2");
			result=true;
			break;
		case KeyEvent.KEYCODE_3:
			dealWithKeyInputPassword("3");
			result=true;
			break;
		case KeyEvent.KEYCODE_4:
			dealWithKeyInputPassword("4");
			result=true;
			break;
		case KeyEvent.KEYCODE_5:
			dealWithKeyInputPassword("5");
			result=true;
			break;
		case KeyEvent.KEYCODE_6:
			dealWithKeyInputPassword("6");
			result=true;
			break;
		case KeyEvent.KEYCODE_7:
			dealWithKeyInputPassword("7");
			result=true;
			break;
		case KeyEvent.KEYCODE_8:
			dealWithKeyInputPassword("8");
			result=true;
			break;	
		case KeyEvent.KEYCODE_9:
			dealWithKeyInputPassword("9");
			result=true;
			break;		
		
		case KeyEvent.KEYCODE_ENTER:
			result=true;
			break;
		case KeyEvent.KEYCODE_BACK:
			
			if(skeyViewText.length()>0){
				skeyViewText = skeyViewText.substring(0, skeyViewText.length()-1);
            	skeyChanged();
            	promptInputPassword.setVisibility(View.INVISIBLE);
				promptInputPassword.setText("");
				result=false;
			}else{
				result=true;
			}
			
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
	
	 private OnLongClickListener removeKeyClickListener = new OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
        	skeyViewText ="";
            skeyChanged();
            return true;
        }
    };
    
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
    
    private void dealWithKeyInputPassword(String key){
    	if(skeyViewText.length()<4){
    		promptInputPassword.setVisibility(View.INVISIBLE);
			promptInputPassword.setText("");
    		skeyViewText = skeyViewText + key;
        	skeyChanged();
    	}else{
    		promptInputPassword.setVisibility(View.VISIBLE);
			promptInputPassword.setText("密码不能超过4位！");
    	}
    }

}

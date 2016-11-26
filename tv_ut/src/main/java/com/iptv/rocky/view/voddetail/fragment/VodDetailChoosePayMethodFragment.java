package com.iptv.rocky.view.voddetail.fragment;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.OrderVodResult;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.json.OrderDayPackageFactory;
import com.iptv.rocky.hwdata.json.OrderVodFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.tcl.vodplay.VodPlayPasswordKeyBoardView;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.TextViewDip;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class VodDetailChoosePayMethodFragment extends Fragment {

	private VodPlayPasswordKeyBoardView  keyboardView;

	private TextView info;
	private TextViewDip skeyView;
	private TextView promptInputPassword;
	private String skeyViewText;
	
	private RadioGroup group;
	private RadioButton btnStartOrderVod;
	private RadioButton btnStartOrderDayPackage;
	private RadioButton radioBtnPayByPassword;
	
	private Button btnPayByPassword;
	
	private StartPlayOrderedVodListener mStartPlayOrderedVodListener;
	
	private OrderVodFactory orderVodFactory;
	private OrderDayPackageFactory orderDayPackageFactory;
	
	private String programId;
	private double price;
	private String selectedPackage;
	private String payMethodSelected;
	private boolean isSeries;

	// 是否启用选择互动和纯直播模式
	private boolean displayChooseInteractive = false;

	/** Fragment第一次附属于Activity时调用,在onCreate之前调用 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtils.debug("welcomeFragment--->onAttach");
		mStartPlayOrderedVodListener = (StartPlayOrderedVodListener) activity;
		skeyViewText="";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_vodplay_choose_pay_method,container, false);
		
		keyboardView = (VodPlayPasswordKeyBoardView) view.findViewById(R.id.password_KeyBoard);

		skeyView = (TextViewDip) view.findViewById(R.id.vodplay_choose_pay_method_password_keyView);
		int textSize = ScreenUtils.getKeyTextSize();// 大小和键盘字母一样
		skeyView.setTextSize(textSize*2);
		
		Bundle bundle = getArguments();
		programId = bundle.getString("VODID");
		price = bundle.getDouble("PRICE");
		isSeries = bundle.getBoolean("ISSERIES");
		selectedPackage = bundle.getString("SELECTED_PACKAGE");
		LogUtils.error("选择的套餐："+selectedPackage);
		
		btnPayByPassword = (Button) view.findViewById(R.id.btn_vodplay_paybypassword);
		promptInputPassword = (TextView) view.findViewById(R.id.txt_prompt_input_password);
		
		/*group = (RadioGroup) view.findViewById(R.id.radio_group_pay_method);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.radiobtn_vodplay_order_by_password){
					payMethodSelected = "PASSWORD";
				}else if(checkedId == R.id.radiobtn_vodplay_order_by_alipay){
					payMethodSelected = "ALIPAY";
				}else if(checkedId == R.id.radiobtn_vodplay_order_by_weixin){
					payMethodSelected = "WEIXIN";
				}
				LogUtils.error("选择的支付方式："+payMethodSelected);
			}
		});*/
		
		//passwordEditText = (EditText) view.findViewById(R.id.edit_vodplay_order_password);
		/*radioBtnPayByPassword = (RadioButton) view.findViewById(R.id.radiobtn_vodplay_order_by_password);
		radioBtnPayByPassword.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String password = passwordEditText.getText().toString();
				orderVodFactory = new OrderVodFactory();
				orderVodFactory.setHttpEventHandler(orderVodHandler);
				orderVodFactory.DownloadDatas(programId,price,password);
				LogUtils.error("通过密码支付，确认支付");
				
			}
		});*/
		
		/*btnStartOrderVod = (RadioButton) view.findViewById(R.id.radiobtn_vodplay_order_by_alipay);
		btnStartOrderVod.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String password = passwordEditText.getText().toString();
				orderVodFactory = new OrderVodFactory();
				orderVodFactory.setHttpEventHandler(orderVodHandler);
				orderVodFactory.DownloadDatas(programId,price,password);
			}
		});*/
		
		/*btnStartOrderDayPackage = (RadioButton) view.findViewById(R.id.radiobtn_vodplay_order_by_weixin);
		btnStartOrderDayPackage.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				LogUtils.debug("点击购买键。下一步动作：停止播放、弹出购买的fragment.");
				
				int days=1;
				String password = passwordEditText.getText().toString();
				orderDayPackageFactory = new OrderDayPackageFactory();
				orderDayPackageFactory.setHttpEventHandler(orderDayPackageHandler);
				orderDayPackageFactory.DownloadDatas(days,password);
			}
		});*/
		initSkeyInput();
		initKeyBoardBtns();
		
		//passwordEditText.requestFocus();
		
		
		//通过密码支付的按钮。
		/*btnPayByPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String password = passwordEditText.getText().toString();
				
				if(password != null){
					if(selectedPackage.equals("DAY")){
						orderDayPackageFactory = new OrderDayPackageFactory();
						orderDayPackageFactory.setHttpEventHandler(orderDayPackageHandler);
						int days = 1;
						orderDayPackageFactory.DownloadDatas(days,price);
					}else if(selectedPackage.equals("VOD")){
						orderVodFactory = new OrderVodFactory();
						orderVodFactory.setHttpEventHandler(orderVodHandler);
						LogUtils.error("选择的包："+selectedPackage);
						orderVodFactory.DownloadDatas(programId,price,password,selectedPackage);
						LogUtils.error("通过密码支付，确认支付");
					}
				}else{
					passwordEditText.requestFocus();
				}
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

	public interface StartPlayOrderedVodListener {
		public void startPlay(int index);
	}
	
	
	private HttpEventHandler<OrderVodResult> orderVodHandler = new HttpEventHandler<OrderVodResult>() {
		@Override
		public void HttpSucessHandler(OrderVodResult result) {
			// 成功
			if (result.getResult() == 200) {
			    LogUtils.error("成功购买， 开始正常播放，后面加，播放成功，然后选择从头播放，从播放处开始播放");
			    mStartPlayOrderedVodListener.startPlay(1);
			    
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
			    LogUtils.error("成功购买， 开始正常播放，后面加，播放成功，然后选择从头播放，从播放处开始播放");
			    mStartPlayOrderedVodListener.startPlay(1);
			    
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
            	skeyViewText = skeyViewText.substring(0, skeyViewText.length()-1);
            	skeyChanged();
            } else if(v.getTag().equals("OK")){
            	String password = skeyViewText;
				
				if(password != null){
					if(password != ""){
						if(password.length()>0 && password.length()<6){
							promptInputPassword.setVisibility(View.VISIBLE);
							promptInputPassword.setText("您输入的密码不足6位！");
						}else if(password.length() == 6){
							if(selectedPackage.equals("DAY")){
								orderDayPackageFactory = new OrderDayPackageFactory();
								orderDayPackageFactory.setHttpEventHandler(orderDayPackageHandler);
								int days = 1;
								orderDayPackageFactory.DownloadDatas(days,price);
							}else if(selectedPackage.equals("VOD")){
								orderVodFactory = new OrderVodFactory();
								orderVodFactory.setHttpEventHandler(orderVodHandler);
								LogUtils.error("选择的包："+selectedPackage);
								orderVodFactory.DownloadDatas(programId,price,password,selectedPackage,isSeries);
								LogUtils.error("通过密码支付，确认支付");
							}
						}else{
							promptInputPassword.setVisibility(View.VISIBLE);
							promptInputPassword.setText("请输入6位密码！");
						}
					}else{
						promptInputPassword.setVisibility(View.VISIBLE);
						promptInputPassword.setText("请输入6位密码！");
					}
				}else{
					promptInputPassword.setVisibility(View.VISIBLE);
					promptInputPassword.setText("请输入6位密码！");
				}
            }else {
            	skeyViewText = skeyViewText + v.getTag();
            	skeyChanged();
            }
            
		}
	};
   
    private OnLongClickListener removeKeyClickListener = new OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
        	skeyViewText ="";
            skeyChanged();
            return true;
        }
    };
    
   private OnLongClickListener okKeyClickListener = new OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
        	skeyViewText ="";
            skeyChanged();
            return true;
        }
    };
    
	private void initSkeyInput() {
    	int width = (int) (TvApplication.pixelWidth / 6.0);
    	int height = (int) (TvApplication.pixelHeight / 10.0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.gravity = Gravity.CENTER;
        params.bottomMargin = ScreenUtils.getSearchKeyPadding();
        skeyView.setLayoutParams(params);
        LogUtils.info("参数:width:"+params.width +" height:"+params.height);
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

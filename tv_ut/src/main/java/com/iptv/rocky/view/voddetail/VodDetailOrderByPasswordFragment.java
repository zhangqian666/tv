package com.iptv.rocky.view.voddetail;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.OrderVodResult;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.json.OrderVodFactory;

public class VodDetailOrderByPasswordFragment extends Fragment{
	
	private Button btnConfirmOrder;
	private EditText passwordEditText;
	private VoddetailStartPlayOrderedVodListener mStartPlayOrderedVodListener;
	
	private OrderVodFactory orderVodFactory;
	
	
	 /** Fragment第一次附属于Activity时调用,在onCreate之前调用 */  
    @Override  
    public void onAttach(Activity activity)   
    {  
        super.onAttach(activity);  
        LogUtils.debug("welcomeFragment--->onAttach");  
        mStartPlayOrderedVodListener = (VoddetailStartPlayOrderedVodListener) activity;   
    }  

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        orderVodFactory = new OrderVodFactory();
        orderVodFactory.setHttpEventHandler(orderVodHandler);
    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,   Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_voddetail_order_by_password, container,false);

		passwordEditText = (EditText) view.findViewById(R.id.edit_voddetail_order_password);
		
		
		btnConfirmOrder = (Button) view.findViewById(R.id.btn_voddetail_order_by_password);
		btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//向服务器送订购信息。
				LogUtils.info("确认采用密码卡支付");
				final String password = passwordEditText.getText().toString();
				LogUtils.info("密码是:"+password);
				if(password.equals("5300")){
					LogUtils.error("密码正确，开始观看,先送支付信息给服务器，确认已经支付");
					int programId = 100;
					String price ="100";
					orderVodFactory.DownloadDatas(programId,price);
					//mStartPlayOrderedVodListener.orderedAndStartPlay(1);
					
				}
			}
		});
		

		return view;
	}
	
	

	
	/*@Override
	public void onBackPressed() {
		LogUtils.debug("ActivityStack.size():"+ActivityStack.size());
		if (ActivityStack.size() == 2) {
			Dialog alertDialog = new AlertDialog.Builder(this)
					.setTitle(getString(R.string.dialog_title))
					.setMessage(getString(R.string.dialog_tips))
					//.setIcon(R.drawable.tv_logo)
					.setNegativeButton(getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
						@Override 
						public void onClick(DialogInterface dialog, int which) {
							quiteBackgroundMusic();
							TvUtils.doubleClickQuitApp(WelcomeActivity.this);
						}
					})
					.setPositiveButton(getString(R.string.dialog_cancel), 
							new DialogInterface.OnClickListener() {
						@Override 
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.create(); 
				alertDialog.show();
		} else {
			ActivityStack.popCurrent();
		}
	}*/
	

	private HttpEventHandler<OrderVodResult> orderVodHandler = new HttpEventHandler<OrderVodResult>() {
		@Override
		public void HttpSucessHandler(OrderVodResult result) {
			// 成功
			if (result.getResult() == 200) {
			    LogUtils.error("登陆数据成功， 开始下载栏目控制");
				mStartPlayOrderedVodListener.orderedAndStartPlay(1);
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

	
	
	 @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }
    
	public interface VoddetailStartPlayOrderedVodListener {
		public void orderedAndStartPlay(int index);
	}
	
}

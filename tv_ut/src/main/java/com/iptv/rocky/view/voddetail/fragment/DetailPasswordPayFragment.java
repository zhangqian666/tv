package com.iptv.rocky.view.voddetail.fragment;

import java.util.Date;

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
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.json.OrderVodFactory;
import com.iptv.rocky.model.TvApplication;

public class DetailPasswordPayFragment extends Fragment{

    private static final String ACTION_STARTLOGINHOTELSERVER ="com.virgintelecom.action.STARTLOGINHOTELSERVER";
	
	private Button btnPayByPassword;
	private EditText passwordEditText;
	
	private OrderVodFactory orderVodFactory;
	private VodDetailInfo vodDetailObj = new VodDetailInfo();
	private ToPlayListener toPlayListener;
	
	 /** Fragment第一次附属于Activity时调用,在onCreate之前调用 */  
    @Override  
    public void onAttach(Activity activity)   
    {  
        super.onAttach(activity);  
        LogUtils.debug("DetailPasswordPayFragment--->onAttach");  
        toPlayListener = (ToPlayListener) activity;   
    }  

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,   Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_voddetail_choose_order_method, container,false);

		/*btnPayByPassword = (Button) view.findViewById(R.id.btn_vodplay_start_order);
		btnPayByPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//向服务器送订购信息。
				LogUtils.info("确认采用密码卡支付,密码可以是房号或者从总机来的，校验密码后，跳转到播放去");
				String password = passwordEditText.getText().toString();
				
				Bundle bundle = getArguments();
				int programId =bundle.getInt("VODID");
				String price = bundle.getString("VODPRICE");
				String orderType ="";
				
				orderVodFactory = new OrderVodFactory();
				orderVodFactory.setHttpEventHandler(orderHandler);
				
				Date orderDate = new Date();
				String userId = TvApplication.account;
				String hotelId = TvApplication.hotelId;
				
				orderVodFactory.DownloadDatas(programId,userId);
			}
		});*/
		return view;
	}
	
	private HttpEventHandler<OrderVodResult> orderHandler = new HttpEventHandler<OrderVodResult>() {
		@Override
		public void HttpSucessHandler(OrderVodResult result) {
			// 成功，通知ACtivity去播放
			toPlayListener.play(1);
		}


		
		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			//TvUtils.processHttpFail();
		}
	};

	
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
    

    
    /** Acitivity要实现这个接口，这样Fragment和Activity就可以共享事件触发的资源了 */  
    public interface ToPlayListener   
    {  
        public void play(int index);   
    }  
	
}

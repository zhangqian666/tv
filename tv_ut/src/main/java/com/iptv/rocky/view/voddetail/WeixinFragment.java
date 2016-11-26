package com.iptv.rocky.view.voddetail;

import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class WeixinFragment extends Fragment{

    private static final String ACTION_STARTLOGINHOTELSERVER ="com.virgintelecom.action.STARTLOGINHOTELSERVER";
	
	private Button btnConfirmOrder;
	
	private Button btnWeixin;//微信
	private Button btnAlipay;//支付宝
	
	private OpenOrderFragmentListener openOrderFragmentListener;
	
	
	
	 /** Fragment第一次附属于Activity时调用,在onCreate之前调用 */  
    @Override  
    public void onAttach(Activity activity)   
    {  
        super.onAttach(activity);  
        LogUtils.debug("welcomeFragment--->onAttach");  
        //openOrderFragmentListener = (OpenOrderFragmentListener) activity;   
    }  

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,   Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_voddetail_choose_order_method, container,false);

		/*btnConfirmOrder = (Button) view.findViewById(R.id.btn_vodplay_start_order);
		btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//向服务器送订购信息。
				LogUtils.info("确认采用密码卡支付");
			}
		});
		
		*/
		
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
    public interface OpenOrderFragmentListener   
    {  
        public void open(int index);   
    }  
	
}

package com.ui.player.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.model.TvApplication;

/**
 * 显示购买状态的
 *
 */
public class ChoosePackage extends RelativeLayout{

	private TextView info;
	private EditText passwordEditText;
	
	private RadioGroup group;
	private Button btnStartOrderVod;
	private Button btnStartOrderDayPackage;
	
	private int programId;
	private double price;
	private VodDetailInfo vodDetailInfo = null;
	
	private String packageSelected;
	
	private RadioButton radioBtnChooseVod;
	private RadioButton radioBtnChooseDay;
	
	public ChoosePayMethodListener mChoosePayMethodListener;
	
	private Context mContext;

	// 是否启用选择互动和纯直播模式
	private boolean displayChooseInteractive = false;
	
	private static final String ACTION_SHOWCHOOSEMETHOD = "com.virgintelecom.iptv.ott.showchoosemethod";
	
	public ChoosePackage(Context context, AttributeSet attrs) {
		super(context, attrs);
		packageSelected = "DAY";
		mContext = context;
	}
	
	@Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        
		LogUtils.error("");
		group = (RadioGroup) findViewById(R.id.radio_group1);
		radioBtnChooseVod= (RadioButton) findViewById(R.id.radio_per_vod);
		radioBtnChooseDay =(RadioButton) findViewById(R.id.radio_per_day);
		
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
				//packageSelected = "DAY";
			}
		});
		btnStartOrderVod = (Button) findViewById(R.id.btn_vodplay_begin_pay);
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
				Intent intent = new Intent(ACTION_SHOWCHOOSEMETHOD);
				v.getContext().sendBroadcast(intent);
				LogUtils.error("发起订购需求");
				
				mChoosePayMethodListener.onChoose(packageSelected,price);
			}
		});
    }

	 public void initData(final VodDetailInfo vodDetailInfo){
		 this.vodDetailInfo = vodDetailInfo;
		 
		price = vodDetailInfo.priceDisp;
		
		if(vodDetailInfo.ISSITCOM == 1){
			String fm =getResources().getString(R.string.vod_play_choose_package_fragment_package_vod_series);
			String out = String.format(fm, vodDetailInfo.priceDisp);
			radioBtnChooseVod.setText(out);
		}else{
			String fm =getResources().getString(R.string.vod_play_choose_package_fragment_package_vod);
			String out = String.format(fm, vodDetailInfo.priceDisp);
			radioBtnChooseVod.setText(out);
		}
		
		String fm =getResources().getString(R.string.vod_play_choose_package_fragment_package_day);
		String out = String.format(fm, TvApplication.perDayPrice);
		radioBtnChooseDay.setText(out);
		
		radioBtnChooseDay.setChecked(true);
		
		if(packageSelected.equals("DAY")){
			radioBtnChooseDay.setChecked(true);
		}else{
			radioBtnChooseVod.setChecked(true);
		}
	 }
	 
		public interface ChoosePayMethodListener{
			void onChoose(String packageSelected, double price);
		}
	 
		/**
		 * Register a callback to be invoked when the media source is ready for playback.
		 */
		public void setChoosePayMethodListener(ChoosePayMethodListener listener) {
			mChoosePayMethodListener = listener;
		}
	 
	 /**
	  * 获取焦点
	  */
	 public void forFocus(){
		 btnStartOrderVod.requestFocus();
	 }
}

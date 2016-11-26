package com.iptv.rocky;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.iptv.common.utils.Constants;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.vodsec.VodSecMasterLayout;
import com.iptv.rocky.R;

public class VodSecActivity extends BaseActivity {

	private VodSecMasterLayout mMasterLayout;
	private String id;
	private String name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vod_sec);
		
		mMasterLayout = (VodSecMasterLayout)findViewById(R.id.vodsec_main_view);
		if(TvApplication.hasBackImage){
			mMasterLayout.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
		}
		Intent intent = getIntent();
		if (intent != null) {
			id = intent.getStringExtra(Constants.cListIdExtra);
			name = intent.getStringExtra(Constants.cListNameExtra);
			mMasterLayout.createView(id, name);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (mMasterLayout != null) {
			mMasterLayout.destroy();
		}
	}
	
}

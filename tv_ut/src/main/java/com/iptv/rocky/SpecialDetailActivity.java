package com.iptv.rocky;

import com.iptv.common.data.EnumType;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.special.SpecialDetailMasterView;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

public class SpecialDetailActivity extends BaseActivity {
	
	private SpecialDetailMasterView masterView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_special_detail);
		
		masterView = (SpecialDetailMasterView) findViewById(R.id.special_detail_master);
		if(TvApplication.hasBackImage){
			masterView.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
		}
		
		Intent intent = getIntent();
		if (intent != null) {
			String id = intent.getStringExtra(Constants.cSpecialDetailExtra);
			EnumType.Platform platform = EnumType.Platform.createPlatform(intent.getStringExtra(Constants.cSpecialDetailPlatformExtra)) ;
			masterView.createView(platform,id);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		masterView.destory();
	}
}

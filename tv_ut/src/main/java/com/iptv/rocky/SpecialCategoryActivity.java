package com.iptv.rocky;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.iptv.common.data.EnumType;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.special.SpecialCategoryMasterLayout;

/**
 * 专题分类页
 * */
public class SpecialCategoryActivity extends BaseActivity {
	
	private SpecialCategoryMasterLayout mMasterLayout;
	String id;
	private EnumType.Platform platform;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_special_category);
		
		Intent intent = getIntent();
		
		if (intent != null) {
			id = intent.getStringExtra(Constants.cListIdExtra);
			platform = EnumType.Platform.createPlatform(intent.getStringExtra(Constants.cPlatformExtra));
			
			mMasterLayout = (SpecialCategoryMasterLayout) findViewById(R.id.special_catetory_layout);
			if(TvApplication.hasBackImage){
				mMasterLayout.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
			}
			mMasterLayout.createView(platform,id);
		}
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mMasterLayout.destory();
	}
	
}

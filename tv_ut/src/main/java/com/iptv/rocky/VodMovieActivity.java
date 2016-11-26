package com.iptv.rocky;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.iptv.common.data.EnumType;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.movie.ListMasterLayout;
import com.iptv.rocky.R;

public class VodMovieActivity extends BaseActivity {

	private ListMasterLayout mMasterLayout;
	private String id;
	private String name;
	private EnumType.Platform platform;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vod_movie);
		
		mMasterLayout = (ListMasterLayout) findViewById(R.id.list_layout);
		if(TvApplication.hasBackImage){
			mMasterLayout.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
		}
		
		Intent intent = getIntent();
		if (intent != null) {
			id = intent.getStringExtra(Constants.cListIdExtra);
			name = intent.getStringExtra(Constants.cListNameExtra);
			platform = EnumType.Platform.createPlatform(intent.getStringExtra(Constants.cPlatformExtra));
			mMasterLayout.createView(platform,id, name);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mMasterLayout.destroy();
	}
	
}

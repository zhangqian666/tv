package com.iptv.rocky;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.iptv.common.data.EnumType;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.vodmovielist.VodMovieListMasterLayout;
import com.iptv.rocky.R;

public class VodMovieListActivity extends BaseActivity {

	private VodMovieListMasterLayout mMasterLayout;
	private String id;
	private String name;
	private EnumType.Platform platform;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vod_movie_list);
		
		mMasterLayout = (VodMovieListMasterLayout)findViewById(R.id.vod_movie_list_layout);
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
		
		if (mMasterLayout != null)
			mMasterLayout.destroy();
	}
	
}

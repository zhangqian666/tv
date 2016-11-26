package com.iptv.rocky;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.iptv.common.utils.Constants;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.vodmoviecolumn.VodMovieColumnMasterLayout;
import com.iptv.rocky.R;

public class VodMovieColumnActivity extends BaseActivity {

	private VodMovieColumnMasterLayout mMasterLayout;
	private String id;
	private String name;
	private String platform;
	private  String columnCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vod_movie_column);
		
		mMasterLayout = (VodMovieColumnMasterLayout) findViewById(R.id.vod_movie_column_layout);
		if(TvApplication.hasBackImage){
			mMasterLayout.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
		}
		
		Intent intent = getIntent();
		if (intent != null) {
			id = intent.getStringExtra(Constants.cListIdExtra);
			name = intent.getStringExtra(Constants.cListNameExtra);
			platform = intent.getStringExtra(Constants.cPlatformExtra);
			columnCode=intent.getStringExtra(Constants.COLUMNCODE_EXTRA_PRICE);
			mMasterLayout.createView(id, name,platform,columnCode);
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

package com.iptv.rocky;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.vodsearch.SearchMasterLayout;
import com.iptv.rocky.R;

public class VodSearchActivity extends BaseActivity {

	private SearchMasterLayout mMasterLayout;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vod_search);
		
		mMasterLayout = (SearchMasterLayout)findViewById(R.id.search_layout);
		if(TvApplication.hasBackImage){
			mMasterLayout.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
		}
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mMasterLayout.destroy();
	}

    @Override
    public void onBackPressed() {
        if (mMasterLayout.onBackPressed()) {
        	return;
        }
        super.onBackPressed();
    }
}

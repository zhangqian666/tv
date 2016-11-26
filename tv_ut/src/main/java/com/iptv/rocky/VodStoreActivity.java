package com.iptv.rocky;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.usercenter.vodstore.StoreMasterLayout;


/**
 * 收藏
 */
public class VodStoreActivity extends BaseActivity {

	private StoreMasterLayout mMasterLayout;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vod_store);
		
        mMasterLayout = (StoreMasterLayout)findViewById(R.id.store_layout);
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
        if (mMasterLayout.isEditing()) {
            mMasterLayout.cancelEdit();
        } else {
            super.onBackPressed();
        }
    }
}

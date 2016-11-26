package com.iptv.rocky;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.usercenter.vodhistory.HistoryMasterLayout;
import com.iptv.rocky.R;

public class VodHistoryActivity extends BaseActivity {

	private HistoryMasterLayout mMasterLayout;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vod_history);

        mMasterLayout = (HistoryMasterLayout)findViewById(R.id.history_layout);
        if(TvApplication.hasBackImage){
        	mMasterLayout.setBackground(new BitmapDrawable(this.getResources(),TvApplication.backImage));
		}
    }

	@Override
	protected void onResume() {
		super.onResume();
		
		mMasterLayout.resume();
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

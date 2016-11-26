package com.iptv.rocky;

import android.os.Bundle;

import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.R;

public class PlaySettingActivity extends BaseActivity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_setting);
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

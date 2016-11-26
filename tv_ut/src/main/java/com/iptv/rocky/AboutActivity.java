package com.iptv.rocky;

import android.os.Bundle;

import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.R;

public class AboutActivity extends BaseActivity {
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
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

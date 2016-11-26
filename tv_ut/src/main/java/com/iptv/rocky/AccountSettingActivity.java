package com.iptv.rocky;

import android.content.Intent;
import android.os.Bundle;

import com.iptv.common.utils.Constants;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.view.setting.AccountMasterLayout;

public class AccountSettingActivity extends BaseActivity {

	private AccountMasterLayout mMasterLayout;

	private String loginSuccess;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_setting);

        mMasterLayout = (AccountMasterLayout) findViewById(R.id.account_setting_layout);
        
        Intent intent = getIntent();
		if (intent != null) {
			loginSuccess = intent.getStringExtra(Constants.cVipGoToLogin);
			mMasterLayout.setContent(loginSuccess);
		}
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
        mMasterLayout.destroy();
	}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

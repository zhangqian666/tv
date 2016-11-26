package com.iptv.rocky;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.update.VersionInfo;
import com.iptv.rocky.base.BaseActivity;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.xml.CheckUpdateFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.AppCommonUtils;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.setting.SettingClickableItem;
import com.iptv.rocky.view.update.AppUpdateManager;
import com.iptv.rocky.view.update.DeviceInfo;

public class UserSettingActivity extends BaseActivity {

	private SettingClickableItem item;
	
	private ProgressBar mProgressBar;
	
	private CheckUpdateFactory checkUpdateFactory;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_setting);

        mProgressBar = (ProgressBar) findViewById(R.id.tv_progressbar);
        mProgressBar.setVisibility(View.GONE);
        item = (SettingClickableItem) findViewById(R.id.user_setting_bitrate);
        item.setOnClickListener(updateCheckClick);
    }
    
    private OnClickListener updateCheckClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mProgressBar.setVisibility(View.VISIBLE);
			if (checkUpdateFactory == null) {
				checkUpdateFactory = new CheckUpdateFactory();
				checkUpdateFactory.setHttpEventHandler(handler);
				checkUpdateFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
			}
			
			DeviceInfo info = new DeviceInfo(UserSettingActivity.this);
			checkUpdateFactory.DownloadDatas(TvApplication.mChannel, info.getDeviceId(), info.getDeviceType(), info.getOsv(), info.getSv(),"HUAWEI",TvApplication.hotelId,"HUAWEI");
		}
	};

	private HttpEventHandler<VersionInfo> handler = new HttpEventHandler<VersionInfo>() {
		
		public void HttpSucessHandler(VersionInfo result) {
			mProgressBar.setVisibility(View.GONE);
			if (!result.isUpdate()) {
				AppCommonUtils.showToast(UserSettingActivity.this, getResources().getString(R.string.update_check_no_update));
				return;
			}
			AppUpdateManager manager = new AppUpdateManager(UserSettingActivity.this, result);
        	manager.showUpdate();
		};
		
		public void HttpFailHandler() {
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(UserSettingActivity.this);
		};
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (checkUpdateFactory != null) {
			checkUpdateFactory.cancel();
		}
	}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

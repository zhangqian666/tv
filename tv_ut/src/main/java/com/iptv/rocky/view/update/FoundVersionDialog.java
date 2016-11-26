package com.iptv.rocky.view.update;

import android.content.Context;
import android.view.View;

import com.iptv.common.update.VersionInfo;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.PromptDialogBase;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class FoundVersionDialog extends PromptDialogBase {
	private Context mContext;
	private TextViewDip updateLog;
	private TextViewDip updateVersion;
	
	public FoundVersionDialog(Context context, int type) {
		super(context, type);
		mContext = context;
		setCanceledOnTouchOutside(false);
		initContentView();
	}

	@Override
	protected void initContentView() {
		View contentView = View.inflate(mContext, R.layout.update_dialog_layout, null);
		updateLog = (TextViewDip) contentView.findViewById(R.id.update_log);
		updateLog.setTextSize((float)(TvApplication.sTvMasterTextSize * 0.6));
		updateVersion = (TextViewDip) contentView.findViewById(R.id.update_version);
		updateVersion.setTextSize((float)(TvApplication.sTvMasterTextSize * 0.7));
		mDialogLayout.mContentLayout.addView(contentView);
	}
	
	public void setData(VersionInfo versionInfo){
		updateVersion.setText(mContext.getResources().getString(R.string.update_dialog_version_txt) + versionInfo.getVersionName() + " (" + versionInfo.getSize() + "M)");
		updateLog.setText(versionInfo.getUpdateLog());
	}
	
}

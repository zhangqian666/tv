package com.iptv.rocky.view.update;

import java.io.File;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.iptv.common.update.VersionInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.utils.AppCommonUtils;
import com.iptv.rocky.view.PromptDialogBase;
import com.iptv.rocky.view.PromptDialogBase.OnCancelListentner;
import com.iptv.rocky.view.PromptDialogBase.OnConfirmListentner;

public class AppUpdateManager implements AppUpdate.IAppUpdate {

	private final static String LOG = "AppUpdateManager";

	private final static int UPDATE_FORCE = 4;
	// private final static int UPDATE_EVERYDAY = 3;
	// private final static int UPDATE_REMIND_ALWAYES = 2;
	// private final static int UPDATE_REMIND_1 = 1;

	private Context mContext;
	private AppUpdate appUpdate;

	private FoundVersionDialog versionDialog = null;
	private DownLoadDialog downLoadDialog = null;
	private VersionInfo mVersionInfo;
	private float size;
	private DecimalFormat formater = new DecimalFormat("##0.00");

	public AppUpdateManager(Context context, VersionInfo info) {
		mContext = context;
		mVersionInfo = info;
		size = Float.parseFloat(info.getSize());
		appUpdate = new AppUpdate(context, info);
		appUpdate.setAppUpdateListener(this);
	}

	public void showUpdate() {
		LogUtils.debug("");
		if (mVersionInfo.getMode() == UPDATE_FORCE && mVersionInfo.isUpdate()) {
			showDownloadDialog();
			beginDownLoad();
		} else {
			showVersionDialog();
		}
	}

	private void showVersionDialog() {
		versionDialog = new FoundVersionDialog(mContext,
				PromptDialogBase.DIALOG_BUTTON_TYPE_DEFAULT);
		versionDialog.setData(mVersionInfo);
		versionDialog.setOnConfirmListenner(VersionDialogconformListener);
		versionDialog.show();
	}

	private OnConfirmListentner VersionDialogconformListener = new OnConfirmListentner() {

		@Override
		public void onConfirm() {
			if (mVersionInfo.isUpdate()) {
				showDownloadDialog();
				beginDownLoad();
			}
			versionDialog.cancel();
		}
	};

	private void showDownloadDialog() {
		if (mVersionInfo.getMode() == UPDATE_FORCE) {
			downLoadDialog = new DownLoadDialog(mContext, PromptDialogBase.DIALOG_BUTTON_TYPE_NONE);
			downLoadDialog.setCancelable(false);
			downLoadDialog.setCanceledOnTouchOutside(false);
		} else {
			downLoadDialog = new DownLoadDialog(mContext, PromptDialogBase.DIALOG_BUTTON_TYPE_JUST_CANCEL);
			downLoadDialog.setCancelable(false);
			downLoadDialog.setCanceledOnTouchOutside(false);
			downLoadDialog.setOnCancelListentner(new OnCancelListentner() {

				@Override
				public void onCancel() {
					appUpdate.downLoadCancle();
				}
			});
		}

		downLoadDialog.show();
	}

	private void beginDownLoad() {
		appUpdate.downLoad();
	}

	@Override
	public void onCancle() {
		if (downLoadDialog != null){
			downLoadDialog.dismiss();
		}
		AppCommonUtils.showToast(mContext, "updateCancled");
		LogUtils.i(LOG, "updateCancled by user");
	}

	@Override
	public void onDownloaded(File apkFile) {
		if (apkFile == null) {
			return;
		}

		LogUtils.i(LOG, "onDownloadComplete");
		
		if (downLoadDialog != null) {
			downLoadDialog.cancel();
		}

		if (!apkFile.exists()) {
			LogUtils.e(LOG, "apk not found");
			return;
		}
		
		
//		if (mContext instanceof Activity)
//        {
//            Activity activity = (Activity) mContext;
//            if (!activity.isFinishing())
//            {
//                activity.finish();
//            }
//        }
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String type = getMIMEType(apkFile);
        LogUtils.debug("----MIMEType ----" + type);
		i.setDataAndType(Uri.fromFile(apkFile), type);
		mContext.startActivity(i);
	}
	
	private String getMIMEType(File f)
    {
        String type = "";
        String fName = f.getName();
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg")
                || end.equals("wav"))
        {
            type = "audio";
        }
        else if (end.equals("3gp") || end.equals("mp4"))
        {
            type = "video";
        }
        else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp"))
        {
            type = "image";
        }
        else if (end.equals("apk"))
        {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        }
        else
        {
            type = "*";
        }
        if (end.equals("apk"))
        {
        }
        else
        {
            type += "/*";
        }
        return type;
    }

	@Override
	public void onDownloading(int progress) {
		String progressSize = formater.format(progress * size / 100);
		downLoadDialog.setTextProgress(progress + "%" + "(" + progressSize + "M/" + size + "M)");
	}

	@Override
	public void onSdcardNotFound() {
		LogUtils.e(LOG, "onSdcardNotFound");
		appUpdate.cancel();
		if (versionDialog != null) {
			if (versionDialog.isShowing()) {
				versionDialog.dismiss();
			}
		}
		if (downLoadDialog != null) {
			if (downLoadDialog.isShowing()) {
				downLoadDialog.dismiss();
			}
		}
		AppCommonUtils.showToast(mContext, "SdcardNotFound");
	}
}

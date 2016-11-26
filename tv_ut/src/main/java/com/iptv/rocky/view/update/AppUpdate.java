package com.iptv.rocky.view.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import android.content.Context;
import android.os.AsyncTask;

import com.iptv.common.update.VersionInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class AppUpdate {

	private DownLoadTast mTask;
	private boolean isInterceptDownload = false;
	private VersionInfo mVersionInfo;
//	private boolean hasSdCard = false;
	private Context mContext;

	public AppUpdate(Context context, VersionInfo versionInfo) {
		mContext = context;
		mVersionInfo = versionInfo;
		// test code
		// versionInfo.setUpdate_url("http://www.ilovedeals.sg/file/apk/ilovedeals-phone-release-sg-20-1.2.9.apk");
	}

	public void downLoad() {
		cancel();
		mTask = new DownLoadTast();
		mTask.execute(mVersionInfo);
	}

	public void cancel() {
		if (mTask != null) {
			mTask.cancel(true);
			mTask = null;
		}
	}

	private class DownLoadTast extends AsyncTask<VersionInfo, Integer, File> {

		@Override
		protected void onPreExecute() {
//			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//				hasSdCard = true;
//			} else {
//				hasSdCard = false;
//			}
			
//			File file = Environment.getExternalStorageDirectory();
//			if (file.exists()) {
//				hasSdCard = true;
//			} else {
//				hasSdCard = false;
//			}
		}

		@Override
		protected File doInBackground(VersionInfo... params) {
//			if (!hasSdCard) {
//				return null;
//			}

			VersionInfo versionInfo = params[0];
			//versionInfo.setUpdateUrl("http://download.pplive.com/android/livePlatform-release.apk");
			//versionInfo.setUpdateUrl("http://192.168.1.100:81/tv.apk");
			InputStream is = null;
			FileOutputStream fos = null;
			int length;
			int count = 0;
			int numRead = 0;
			int progress = 0;
			File ApkFile = null;
			
			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(versionInfo.getUpdateUrl()).openConnection();
				Header[] headers = IPTVUriUtils.getHeader();
				if (headers != null && headers.length > 0)
				{
					for (Header header : headers)
					{
						connection.addRequestProperty(header.getName(), header.getValue());
					}
				}
				
				connection.setConnectTimeout(15000);
				connection.setReadTimeout(15000);
				connection.connect();
				int code = connection.getResponseCode();
				length = connection.getContentLength();
				if (code == HttpStatus.SC_OK) {
					is = connection.getInputStream();
				}
				
				String filePath = mContext.getFilesDir().getParentFile().getAbsolutePath() + "/tv_hilton.apk";
				//String filePath = mContext.getFilesDir() + "/tv.apk";
				LogUtils.i("APK", filePath);
				ApkFile = new File(filePath);
				if (ApkFile.exists()) {
					ApkFile.delete();
				}
				fos = new FileOutputStream(ApkFile);
				//fos = mContext.openFileOutput("tv.apk", Context.MODE_WORLD_READABLE);
				byte buf[] = new byte[512];
				do {
					numRead = is.read(buf);
					count += numRead;
					progress = (int) (((float) count / length) * 100);
					publishProgress(progress);
					if (numRead <= 0) {
						break;
					}
					fos.write(buf, 0, numRead);
				} while (!isInterceptDownload);
				
				Process process = Runtime.getRuntime().exec("chmod 777 " + filePath);
				process.waitFor();
			} catch (Exception e) {
				LogUtils.i("AppUpdate", "download error " + e.toString());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return ApkFile;
		}

		@Override
		protected void onPostExecute(File file) {
			if (appUpdateListener != null) {
//				if (hasSdCard) {
					appUpdateListener.onDownloaded(file);
//				} else {
//					appUpdateListener.onSdcardNotFound();
//				}
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (appUpdateListener != null) {
				appUpdateListener.onDownloading(values[0]);
			}
		}
	};

	private void setInterceptDownload(boolean isInterceptDownload) {
		this.isInterceptDownload = isInterceptDownload;
		if (isInterceptDownload) {
			if (appUpdateListener != null) {
				appUpdateListener.onCancle();
			}
		}
	}

	public void downLoadCancle() {
		cancel();
		setInterceptDownload(true);
	}

	private IAppUpdate appUpdateListener;

	public void setAppUpdateListener(IAppUpdate appUpdateListener) {
		this.appUpdateListener = appUpdateListener;
	}

	public interface IAppUpdate {
		public void onCancle();

		public void onDownloaded(File apkFile);

		public void onDownloading(int progress);

		public void onSdcardNotFound();
	}
}

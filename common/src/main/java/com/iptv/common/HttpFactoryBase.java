package com.iptv.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.os.AsyncTask;

import com.iptv.common.utils.LogUtils;

/**
 * HTTP数据管理基类，支持GET与POST，默认为GET
 */
public abstract class HttpFactoryBase<T> {

	private HttpDownloadTask task;
	private HttpEventHandler<T> httpEventHandler;

	private HttpURLConnection mHttpConection;
	private HttpPost mHttpPost;
	
	private Header[] mHeaders;

	public void setHttpEventHandler(HttpEventHandler<T> httpEventHandler) {
		this.httpEventHandler = httpEventHandler;
	}
	
	public void setHttpHeaderParams(Header[] headers) {
		mHeaders = headers;
	}

	/**
	 * 异步下载数据
	 */
	public void DownloadDatas(Object... args) {
		cancel();
		task = new HttpDownloadTask();
		task.execute(args);
	}

	/**
	 * 同步下载数据
	 */
	public T syncDownloaDatas(Object... args) {
		return doNetworkRequest(args);
	}

	/**
	 * 取消下载
	 */
	public void cancel() {
		try {
			if (task != null) {
				task.cancel(true);
				task = null;
			}
			if (mHttpConection != null) {
				mHttpConection.disconnect();
				mHttpConection = null;
			}
			if (mHttpPost != null) {
				mHttpPost.abort();
				mHttpPost = null;
			}
		} catch (Exception e) {
			LogUtils.e("取消下载  httpfactorybase", e.toString());
		}
	}

	protected int getConnectTimeout() {
		return 15000;
	}

/*	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}*/
	
	protected abstract ArrayList<NameValuePair> getPostArgs();

	/**
	 * Http请求地址
	 * 
	 * @param args
	 * @return
	 */
	protected abstract String CreateUri(Object... args);

	/**
	 * 子类解析内容
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	protected abstract T AnalysisContent(InputStream stream) throws IOException;

	protected T doNetworkRequest(Object... params) {
		try {
			String uri = CreateUri(params);
			LogUtils.debug("需要访问的URL:"+uri);
			if (getPostArgs() != null) {
				//LogUtils.info("PostArgs is not null");
				BasicHttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters,getConnectTimeout());
				HttpConnectionParams.setSoTimeout(httpParameters,getConnectTimeout());
				DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
				mHttpPost = new HttpPost(uri);
				mHttpPost.setEntity(new UrlEncodedFormEntity(getPostArgs(),HTTP.UTF_8));
				HttpResponse response = httpclient.execute(mHttpPost);
				int code = response.getStatusLine().getStatusCode();
				// LogUtils.info("返回数据状态: "+code);
				if (code == HttpStatus.SC_OK) {
					String responseData = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
					LogUtils.error("返回的数据："+responseData);
					ByteArrayInputStream stream = new ByteArrayInputStream(responseData.getBytes());
					try {
						return AnalysisContent(stream);
					} finally {
						stream.close();
					}
				}
			} else {
				// LogUtils.info("PostArgs is null");
				mHttpConection = (HttpURLConnection) new URL(uri).openConnection();
				if (mHeaders != null && mHeaders.length > 0)
				{
					for (Header header : mHeaders)
					{
						// LogUtils.debug("" +header.getName()+"; "+header.getValue());
						mHttpConection.addRequestProperty(header.getName(), header.getValue());
					}
				}
				mHttpConection.setReadTimeout(60*1000);//getConnectTimeout());
				mHttpConection.setConnectTimeout(60*1000);//getConnectTimeout());
				mHttpConection.connect();
				int code = mHttpConection.getResponseCode();
				// LogUtils.info("返回数据状态: "+code);
				if (code == HttpStatus.SC_OK) {
					InputStream stream = mHttpConection.getInputStream();
					try {
						return AnalysisContent(stream);
					} finally {
						stream.close();
					}
				}else if(code == HttpStatus.SC_INTERNAL_SERVER_ERROR){
					InputStream stream = mHttpConection.getInputStream();
					// LogUtils.error("返回服务器错误:"+stream);
				}
			}
		} catch (MalformedURLException e) {
			LogUtils.error( e.toString());
		} catch (IOException e) {
			InputStream stream;
			try {
				stream = mHttpConection.getInputStream();
				// LogUtils.error("返回服务器错误:"+stream);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			// LogUtils.error( e.toString());
		}
		return null;
	}

	/**
	 * 异步下载任务
	 */
	private class HttpDownloadTask extends AsyncTask<Object, Integer, T> {

		@Override
		protected T doInBackground(Object... params) {
			if (isCancelled()) {
				return null;
			}
			return doNetworkRequest(params);
		}

		@Override
		protected void onPostExecute(T result) {
			if (isCancelled()) {
				return;
			}
			if (result == null) {
				if (httpEventHandler != null) {
					httpEventHandler.HttpFailHandler();
				}
			} else {
				if (httpEventHandler != null) {
					httpEventHandler.HttpSucessHandler(result);
				}
			}
		}
	}
}

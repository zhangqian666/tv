package com.ikantech.support.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.util.Log;

import com.ikantech.support.utils.YiLog;
import com.iptv.common.utils.LogUtils;

public class InHttpHelper
{
	// http request time out
	private static int TIME_OUT_DELAY = 5000;

	// no instance
	private InHttpHelper()
	{

	}

	public static YiHttpResponse execute(YiHttpRequest request)
	{
		YiHttpResponse response = new YiHttpResponse(request.getUrl());
		response.setResponseId(request.getRequestId());
		response.setUid(request.getUid());

		if (request.getMode().equals(YiHttpRequest.YiHttpRequestMode.MODE_GET))
		{
			executeHttpGet(request, response);
		}
		else
		{
			executeHttpPost(request, response);
		}

		return response;
	}

	// use apache http client.
	protected static void executeHttpGet(YiHttpRequest request,
			YiHttpResponse response)
	{
		String result = null;
		BufferedReader reader = null;
		try
		{
			HttpClient client = new DefaultHttpClient();
			int timeOut = TIME_OUT_DELAY;
			if(request.getTimeOut() > 0) {
				timeOut = request.getTimeOut();
			}
			client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
					timeOut); // 超时设置
			client.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, TIME_OUT_DELAY);// 连接超时
			HttpGet httpGet = new HttpGet();
			String url = request.getUrl();
			boolean pafix = url.indexOf('?') > -1;
			if (request.getParams() != null)
			{
				List<NameValuePair> params = request.getParams();
				for (int i = 0; i < params.size(); i++)
				{
					NameValuePair pair = params.get(i);
					if(i == 0 && !pafix) {
						url += "?" + pair.getName() + "=" + pair.getValue();
					}else {
						url += "&" + pair.getName() + "=" + pair.getValue();
					}
				}
			}
			Log.e("访问地址：", url);
			httpGet.setURI(new URI(url));
			
			Header[] headers = request.getHeaders();
			if(headers != null) {
				for (Header header : headers)
				{
					httpGet.addHeader(header);
				}
			}
			
			HttpResponse httpResponse = client.execute(httpGet);
			response.setHeaders(httpResponse.getAllHeaders());
			
			String locale = request.getResponseCharset();
			if(locale.equals("iso-8859-1")){
				locale = "gbk";
			}
			
			reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), locale));

			StringBuffer strBuffer = new StringBuffer("");
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				strBuffer.append(line);
			}
			result = strBuffer.toString();
			response.setResponse(result);
			//LogUtils.info("result--->"+result);
		}
		catch (ConnectTimeoutException timeoutException)
		{
			
			response.setErrorCode(YiHttpResponse.ERROR_TIME_OUT);
			YiLog.getInstance().e(timeoutException, "executeHttpGet error:");
		}
		catch (Exception e)
		{
			YiLog.getInstance().e("通常错误 "+e.toString());
			response.setErrorCode(YiHttpResponse.ERROR_UNKNOWN);
			YiLog.getInstance().e(e, "executeHttpGet error:");
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
					reader = null;
				}
				catch (IOException e)
				{
					YiLog.getInstance().e(e,
							"executeHttpGet close reader error:");
				}
			}
		}
	}

	protected static void executeHttpPost(YiHttpRequest request,
			YiHttpResponse response)
	{
		String result = null;
		BufferedReader reader = null;
		try
		{
			HttpClient client = new DefaultHttpClient();
			int timeOut = TIME_OUT_DELAY;
			if(request.getTimeOut() > 0) {
				timeOut = request.getTimeOut();
			}
			client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
					timeOut); // 超时设置
			client.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, TIME_OUT_DELAY);// 连接超时
			HttpPost httpPost = new HttpPost();
			httpPost.setURI(new URI(request.getUrl()));
			
			Header[] headers = request.getHeaders();
			if(headers != null) {
				for (Header header : headers)
				{
					httpPost.addHeader(header);
				}
			}
			
			if (request.getParams() != null)
			{
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						request.getParams());
				httpPost.setEntity(formEntity);
			}

			HttpResponse httpResponse = client.execute(httpPost);
			response.setHeaders(httpResponse.getAllHeaders());
			
			String charSet = request.getResponseCharset();
			if(charSet.equals("UTF-8")){
				
			}else{
				charSet = "gbk";
			}
			
			reader = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent(), charSet));
			
			StringBuffer strBuffer = new StringBuffer("");
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				strBuffer.append(line);
			}
			result = strBuffer.toString();
			//Log.d("InHttpHelper", "Result"+result);
			response.setResponse(result);
		}
		catch (ConnectTimeoutException timeoutException)
		{
			response.setErrorCode(YiHttpResponse.ERROR_TIME_OUT);
			YiLog.getInstance().e(timeoutException, "executeHttpPost error:");
		}
		catch (Exception e)
		{
			response.setErrorCode(YiHttpResponse.ERROR_UNKNOWN);
			e.printStackTrace();
			YiLog.getInstance().e(e, "executeHttpPost error:");
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
					reader = null;
				}
				catch (IOException e)
				{
					YiLog.getInstance().e(e,"executeHttpPost close reader error:");
				}
			}
		}
	}
}

package com.ikantech.support.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class YiHttpRequest
{
	public enum YiHttpRequestMode
	{
		MODE_GET, MODE_POST
	}

	private int uid;
	private YiHttpRequestMode mode;
	private int requestId;
	private String url;
	private String responseCharset;
	private Header[] headers;
	private int timeOut;
	private List<NameValuePair> params;

	public YiHttpRequest(String url, YiHttpRequestMode mode)
	{
		if (url == null || mode == null)
		{
			throw new NullPointerException("params non-null");
		}
		this.url = url;
		this.mode = mode;
		this.requestId = -1;
		this.uid = -1;
		this.timeOut = -1;
		this.responseCharset = "utf-8";
	}

	public int getUid()
	{
		return uid;
	}

	public void setUid(int uid)
	{
		this.uid = uid;
	}

	public int getRequestId()
	{
		return requestId;
	}

	public void setRequestId(int requestId)
	{
		this.requestId = requestId;
	}

	public List<NameValuePair> getParams()
	{
		return params;
	}

	public void setParams(List<NameValuePair> params)
	{
		this.params = params;
	}

	public YiHttpRequestMode getMode()
	{
		return mode;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	protected void initParams()
	{
		if (params == null)
		{
			params = new ArrayList<NameValuePair>();
		}
	}

	public void addParam(String key, String value)
	{
		initParams();
		params.add(new BasicNameValuePair(key, value));
	}

	public void addParam(String key, long value)
	{
		this.addParam(key, String.valueOf(value));
	}

	public void addParam(String key, boolean value)
	{
		this.addParam(key, value);
	}
	
	public void clearParam() {
		params.clear();
	}

	public String getResponseCharset()
	{
		return responseCharset;
	}

	public void setResponseCharset(String responseCharset)
	{
		this.responseCharset = responseCharset;
	}

	public Header[] getHeaders()
	{
		return headers;
	}

	public void setHeaders(Header[] headers)
	{
		this.headers = headers;
	}

	public int getTimeOut()
	{
		return timeOut;
	}

	public void setTimeOut(int timeOut)
	{
		this.timeOut = timeOut;
	}
}

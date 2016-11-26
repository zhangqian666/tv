package com.ikantech.support.net;

import org.apache.http.Header;

public class YiHttpResponse
{
	public static final int ERROR_TIME_OUT = -100;
	public static final int ERROR_UNKNOWN = -2;
	public static final int SUCCESS = 0;

	private int uid;
	private int errorCode;
	private int responseId;
	private String url;
	private Header[] headers;
	private Header[] headers2;
	private String response;

	public YiHttpResponse(String url)
	{
		if (url == null)
		{
			throw new NullPointerException("params non-null");
		}
		this.url = url;
		this.errorCode = SUCCESS;
		this.responseId = -1;
		this.response = "";
		this.uid = -1;
	}

	public int getUid()
	{
		return uid;
	}

	public void setUid(int uid)
	{
		this.uid = uid;
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}

	public int getResponseId()
	{
		return responseId;
	}

	public void setResponseId(int responseId)
	{
		this.responseId = responseId;
	}

	public String getResponse()
	{
		return response;
	}

	public void setResponse(String response)
	{
		this.response = response;
	}

	public String getUrl()
	{
		return url;
	}

	public Header[] getHeaders()
	{
		return headers;
	}

	public void setHeaders(Header[] headers)
	{
		this.headers = headers;
	}

	public Header[] getHeaders2()
	{
		return headers2;
	}

	public void setHeaders2(Header[] headers2)
	{
		this.headers2 = headers2;
	}
}

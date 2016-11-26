package com.iptv.common.data;

import java.io.Serializable;

public class Auth implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/* 错误码*/
	public String _error_code;
	/* 返回信息*/
	public String _return_message;
	/* 鉴权成功后的鉴权ID*/
	public String AuthorizationID;
}

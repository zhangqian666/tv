package com.iptv.common;

/**
 * Http事件处理类
 *
 * @param <T>
 */
public abstract class HttpEventHandler <T> {
	
	public abstract void HttpSucessHandler(T result);
	
	public abstract void HttpFailHandler();
	
}

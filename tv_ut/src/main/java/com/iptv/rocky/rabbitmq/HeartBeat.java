package com.iptv.rocky.rabbitmq;


public class HeartBeat {

	private String action;
	private String user;
	private String stbid;
	private Long date;
	
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getStbid() {
		return stbid;
	}
	public void setStbid(String stbid) {
		this.stbid = stbid;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
}

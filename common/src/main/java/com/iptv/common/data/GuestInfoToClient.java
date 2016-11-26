package com.iptv.common.data;

/**
 * 发送到客户端的顾客信息
 * 目前用于欢迎显示
 * @author Xiaoqiang
 * 2016-01-24
 *
 */
public class GuestInfoToClient {

	private String name;
	private int sex;
	
	public GuestInfoToClient(){
		
	}
	
	public GuestInfoToClient(String name, int sex){
		this.name = name;
		this.sex =sex;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
}

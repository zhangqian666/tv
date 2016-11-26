package com.iptv.common.data;

import java.util.List;

public class LoginResult {

	//登陆结果
	private int result;
	// 如果失败，原因
	private String reason;
	// 是否选择语言。不选择就只显示中文
	private String chooseLanguage;
	// 
	private String hotelId;
	
	private List<HotelColumnEnabled> columnEnabled;
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getChooseLanguage() {
		return chooseLanguage;
	}
	public void setChooseLanguage(String chooseLanguage) {
		this.chooseLanguage = chooseLanguage;
	}
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public List<HotelColumnEnabled> getColumnEnabled() {
		return columnEnabled;
	}
	public void setColumnEnabled(List<HotelColumnEnabled> columnEnabled) {
		this.columnEnabled = columnEnabled;
	}
}

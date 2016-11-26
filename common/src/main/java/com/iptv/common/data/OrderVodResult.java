package com.iptv.common.data;

import java.util.Date;

public class OrderVodResult {

	private int result; 		//订购结果
	private int programId; 		//订购的产品
	private double price;		//订购价格
	private Date expiryDateTime; //失效日期和时间
	private String reason; 		//如果失效，提示原因
	private Date orderDate; 	//订购时间
	
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getProgramId() {
		return programId;
	}
	public void setProgramId(int programId) {
		this.programId = programId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Date getExpiryDateTime() {
		return expiryDateTime;
	}
	public void setExpiryDateTime(Date expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}
}

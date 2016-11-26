package com.iptv.common.data;

import java.util.Date;

public class VodOrderStatusResult {

	private int result; //订购结果
	private int programId; //订购的产品
	private double price;
	private Date inValidDate;//失效期
	private String reason; //如果失效，提示原因
	private Date orderDate; //
	private Double guestPayedProgramPrice;		// 客户已经支付过的其他节目
	
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
	public Date getInValidDate() {
		return inValidDate;
	}
	public void setInValidDate(Date inValidDate) {
		this.inValidDate = inValidDate;
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
	public Double getGuestPayedProgramPrice() {
		return guestPayedProgramPrice;
	}
	public void setGuestPayedProgramPrice(Double guestPayedProgramPrice) {
		this.guestPayedProgramPrice = guestPayedProgramPrice;
	}
	
	
}

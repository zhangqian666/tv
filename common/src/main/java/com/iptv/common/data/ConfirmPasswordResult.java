package com.iptv.common.data;

import java.util.Date;


public class ConfirmPasswordResult {

	private boolean passwordResult;					//
	private int orderResult; 						// 订购结果
	
	private int programId; 						// 订购的产品
	private double price;						// 订购价格
	private Date expiryDateTime;				// 失效日期
	private String reason; 						// 如果失效，提示原因
	private BillingTypeOfHotelGuest billingTypeOfGuest;
	private String orderRecordId;  				// 订购记录的ID;
	private Double guestPayedProgramPrice;		// 客户已经支付过的其他节目
	
	public int getOrderResult() {
		return orderResult;
	}
	public void setOrderResult(int orderResult) {
		this.orderResult = orderResult;
	}
	
	public boolean isPasswordResult() {
		return passwordResult;
	}
	public void setPasswordResult(boolean passwordResult) {
		this.passwordResult = passwordResult;
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
	public Date getExpiryDateTime() {
		return expiryDateTime;
	}
	public void setExpiryDateTime(Date expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public BillingTypeOfHotelGuest getBillingTypeOfGuest() {
		return billingTypeOfGuest;
	}
	public void setBillingTypeOfGuest(BillingTypeOfHotelGuest billingTypeOfGuest) {
		this.billingTypeOfGuest = billingTypeOfGuest;
	}
	public String getOrderRecordId() {
		return orderRecordId;
	}
	public void setOrderRecordId(String orderRecordId) {
		this.orderRecordId = orderRecordId;
	}
	public Double getGuestPayedProgramPrice() {
		return guestPayedProgramPrice;
	}
	public void setGuestPayedProgramPrice(Double guestPayedProgramPrice) {
		this.guestPayedProgramPrice = guestPayedProgramPrice;
	}
	

}

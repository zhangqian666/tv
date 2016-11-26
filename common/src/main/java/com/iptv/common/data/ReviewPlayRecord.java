package com.iptv.common.data;

import java.io.Serializable;
import java.util.Date;


/**
 * Vod Play Record send to Server
 */
public class ReviewPlayRecord implements Serializable{
	
	private String userId;
	private String stbId;
	private String hotelId;
	private String roomId;
	private String language;
	
	private Date beginPlayDateTime;
	private Date endPlayDateTime;
	private Long beginPosition;
	private Long endPosition;
	private String programId;
	private String programName;
	private String orderType; //订购方式：包月、包天、单片
	private BillingTypeOfHotelGuest billingType;
	private int price;
	private EnumType.Platform platform; // HUAWEI HOTEL ZTE
	private boolean ordered;
	private int length;  	// 节目长度
	
	public Date getBeginPlayDateTime() {
		return beginPlayDateTime;
	}
	public void setBeginPlayDateTime(Date beginPlayDateTime) {
		this.beginPlayDateTime = beginPlayDateTime;
	}
	public Date getEndPlayDateTime() {
		return endPlayDateTime;
	}
	public void setEndPlayDateTime(Date endPlayDateTime) {
		this.endPlayDateTime = endPlayDateTime;
	}

	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	public EnumType.Platform getPlatform() {
		return platform;
	}
	public void setPlatform(EnumType.Platform platform) {
		this.platform = platform;
	}
	public boolean isOrdered() {
		return ordered;
	}
	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public BillingTypeOfHotelGuest getBillingType() {
		return billingType;
	}
	public void setBillingType(BillingTypeOfHotelGuest billingType) {
		this.billingType = billingType;
	}
	public Long getBeginPosition() {
		return beginPosition;
	}
	public void setBeginPosition(Long beginPosition) {
		this.beginPosition = beginPosition;
	}
	public Long getEndPosition() {
		return endPosition;
	}
	public void setEndPosition(Long endPosition) {
		this.endPosition = endPosition;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStbId() {
		return stbId;
	}
	public void setStbId(String stbId) {
		this.stbId = stbId;
	}
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	 
	
}

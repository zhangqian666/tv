package com.iptv.common.data;

import java.util.List;

import com.iptv.common.data.EnumType.StbOrientation;


public class IptvAccountInitResult {

	private int result;
	private String stbId;
	private String userId;
	private String hotelId;
	private String password;
	private String groupId;
	private String roomId;
	private String floorId;
	private BillingTypeOfHotelGuest  billingTypeOfHotelGuest;
	
	private String welcomeId; //欢迎界面
	private boolean hasSpecialWelcome;
	private boolean hasScreenProtect;
	private String screenProtectType;
	private String screenProtectId;
	private boolean hasBackground;
	private String backGround;
	
	/**
	 * 包天的每日付费价格
	 */
	private double perDayPrice;
	/** 
	 * 包月的每月价格
	 */
	private double perMonthPrice;
	/**
	 * 是否需要支付包天点播费用
	 */
	private boolean payForDaylyPayVod;
	/**
	 * 包天点播价格
	 */
	private double daylyPayVodPrice; 
	/** 
	 * 单点单付是否免费
	 */
	private boolean payForPayPerVod;
	/**
	 *  每个片子的价格
	 */
	private double perVodPrice;
	private String iptvLoginAddress;
	private boolean loginIptv;
	private String iptvPlatform;
	private String errorInfo;
	
	private String bootImage;
	private String bootVideo;
	
	private String backImageUrl;
	/**
	 * 客户端类型:
	 */
	private StbType stbType; 
	private boolean showHomeNoticePrompt;
	/**
	 * 是否显示英文页面/按钮
	 */
	private boolean showEnglish;
	private StbOrientation stbOrientation;
	
	public StbOrientation getStbOrientation() {
		return stbOrientation;
	}
	public void setStbOrientation(StbOrientation stbOrientation) {
		this.stbOrientation = stbOrientation;
	}
	private List<GuestInfoToClient> guests;
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getStbId() {
		return stbId;
	}
	public void setStbId(String stbId) {
		this.stbId = stbId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public String getIptvPlatform() {
		return iptvPlatform;
	}
	public void setIptvPlatform(String iptvPlatform) {
		this.iptvPlatform = iptvPlatform;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getFloorId() {
		return floorId;
	}
	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	public double getPerDayPrice() {
		return perDayPrice;
	}
	public void setPerDayPrice(double perDayPrice) {
		this.perDayPrice = perDayPrice;
	}
	public double getPerMonthPrice() {
		return perMonthPrice;
	}
	public void setPerMonthPrice(double perMonthPrice) {
		this.perMonthPrice = perMonthPrice;
	}
	public boolean isPayForDaylyPayVod() {
		return payForDaylyPayVod;
	}
	public void setPayForDaylyPayVod(boolean payForDaylyPayVod) {
		this.payForDaylyPayVod = payForDaylyPayVod;
	}
	public double getDaylyPayVodPrice() {
		return daylyPayVodPrice;
	}
	public void setDaylyPayVodPrice(double daylyPayVodPrice) {
		this.daylyPayVodPrice = daylyPayVodPrice;
	}
	public boolean isPayForPayPerVod() {
		return payForPayPerVod;
	}
	public void setPayForPayPerVod(boolean payForPayPerVod) {
		this.payForPayPerVod = payForPayPerVod;
	}
	public double getPerVodPrice() {
		return perVodPrice;
	}
	public void setPerVodPrice(double perVodPrice) {
		this.perVodPrice = perVodPrice;
	}
	public String getIptvLoginAddress() {
		return iptvLoginAddress;
	}
	public void setIptvLoginAddress(String iptvLoginAddress) {
		this.iptvLoginAddress = iptvLoginAddress;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public boolean isLoginIptv() {
		return loginIptv;
	}
	public void setLoginIptv(boolean loginIptv) {
		this.loginIptv = loginIptv;
	}
	public BillingTypeOfHotelGuest getBillingTypeOfHotelGuest() {
		return billingTypeOfHotelGuest;
	}
	public void setBillingTypeOfHotelGuest(
			BillingTypeOfHotelGuest billingTypeOfHotelGuest) {
		this.billingTypeOfHotelGuest = billingTypeOfHotelGuest;
	}
	public String getWelcomeId() {
		return welcomeId;
	}
	public void setWelcomeId(String welcomeId) {
		this.welcomeId = welcomeId;
	}
	public String getBackImageUrl() {
		return backImageUrl;
	}
	public void setBackImageUrl(String backImageUrl) {
		this.backImageUrl = backImageUrl;
	}
	public String getScreenProtectId() {
		return screenProtectId;
	}
	public void setScreenProtectId(String screenProtectId) {
		this.screenProtectId = screenProtectId;
	}
	public boolean isHasBackground() {
		return hasBackground;
	}
	public void setHasBackground(boolean hasBackground) {
		this.hasBackground = hasBackground;
	}
	public String getBackGround() {
		return backGround;
	}
	public void setBackGround(String backGround) {
		this.backGround = backGround;
	}
	public String getScreenProtectType() {
		return screenProtectType;
	}
	public void setScreenProtectType(String screenProtectType) {
		this.screenProtectType = screenProtectType;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public StbType getStbType() {
		return stbType;
	}
	public void setStbType(StbType stbType) {
		this.stbType = stbType;
	}
	public boolean isHasScreenProtect() {
		return hasScreenProtect;
	}
	public void setHasScreenProtect(boolean hasScreenProtect) {
		this.hasScreenProtect = hasScreenProtect;
	}
	public boolean isShowHomeNoticePrompt() {
		return showHomeNoticePrompt;
	}
	public void setShowHomeNoticePrompt(boolean showHomeNoticePrompt) {
		this.showHomeNoticePrompt = showHomeNoticePrompt;
	}
	public List<GuestInfoToClient> getGuests() {
		return guests;
	}
	public void setGuests(List<GuestInfoToClient> guests) {
		this.guests = guests;
	}
	public String getBootImage() {
		return bootImage;
	}
	public void setBootImage(String bootImage) {
		this.bootImage = bootImage;
	}
	public String getBootVideo() {
		return bootVideo;
	}
	public void setBootVideo(String bootVideo) {
		this.bootVideo = bootVideo;
	}
	public boolean isHasSpecialWelcome() {
		return hasSpecialWelcome;
	}
	public void setHasSpecialWelcome(boolean hasSpecialWelcome) {
		this.hasSpecialWelcome = hasSpecialWelcome;
	}
	public boolean getShowEnglish() {
		return showEnglish;
	}
	public void setShowEnglish(boolean showEnglish) {
		this.showEnglish = showEnglish;
	}
	
	
	
}

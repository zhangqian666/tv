package com.iptv.common.data;

import java.util.List;


/**
 * 定义酒店开放的功能块儿
 * @since 2015-4-10
 *
 */
public class HotelColumnEnabled{

	private String id;
	// 酒店ID
	private String hotelId;
	//设定的语言
	private String language;
	
	// 是否注册到IPTV平台的AAA
	private String loginIptv;
	
	// Recommend
	private String recommend;
	private String vod;
	//是否允许订购vod产品
	private String orderVodItem;

	private String hotelIntroduce;
	// 酒店服务
	private String hotelService;
	private String review;
	// 专题
	private String special;
	// 时移
	private String timeshift;
	// 是否允许订购收费频道
	private String orderChannel;

	// 是否开放传统直播模式
	private String traditionalMode;
	
	// 点播播放记录
	private String playRecord;
	
	// 个人收藏
	private String favorate; 
	
	private String game;
	
	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	// 开机广告，选择完
	private String showPowerOnAdvice;
	
	// 开机广告的图片
	private List<String> powerOnAdviceImages;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getPowerOnAdviceImages() {
		return powerOnAdviceImages;
	}

	public void setPowerOnAdviceImages(List<String> powerOnAdviceImages) {
		this.powerOnAdviceImages = powerOnAdviceImages;
	}

	public String getShowPowerOnAdvice() {
		return showPowerOnAdvice;
	}

	public void setShowPowerOnAdvice(String showPowerOnAdvice) {
		this.showPowerOnAdvice = showPowerOnAdvice;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getVod() {
		return vod;
	}

	public void setVod(String vod) {
		this.vod = vod;
	}

	public String getOrderVodItem() {
		return orderVodItem;
	}

	public void setOrderVodItem(String orderVodItem) {
		this.orderVodItem = orderVodItem;
	}

	public String getHotelIntroduce() {
		return hotelIntroduce;
	}

	public void setHotelIntroduce(String hotelIntroduce) {
		this.hotelIntroduce = hotelIntroduce;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getTimeshift() {
		return timeshift;
	}

	public void setTimeshift(String timeshift) {
		this.timeshift = timeshift;
	}

	public String getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(String orderChannel) {
		this.orderChannel = orderChannel;
	}

	public String getTraditionalMode() {
		return traditionalMode;
	}

	public void setTraditionalMode(String traditionalMode) {
		this.traditionalMode = traditionalMode;
	}

	public String getPlayRecord() {
		return playRecord;
	}

	public void setPlayRecord(String playRecord) {
		this.playRecord = playRecord;
	}

	public String getFavorate() {
		return favorate;
	}

	public void setFavorate(String favorate) {
		this.favorate = favorate;
	}

	public String getLoginIptv() {
		return loginIptv;
	}

	public void setLoginIptv(String loginIptv) {
		this.loginIptv = loginIptv;
	}

	public String getHotelService() {
		return hotelService;
	}

	public void setHotelService(String hotelService) {
		this.hotelService = hotelService;
	}

	
	
}

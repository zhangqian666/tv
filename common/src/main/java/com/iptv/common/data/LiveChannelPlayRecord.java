package com.iptv.common.data;

import java.io.Serializable;
import java.util.Date;

public class LiveChannelPlayRecord implements Serializable{
	
	private String userId;
	private String stbId;
	private String hotelId;
	private String roomId;
	private String language;
//	private int channelId;
	private String channelId;
	private int userChannelId;
	private String channelName;
	private Date startPlayTime;
	private Date endPlayTime;
	private EnumType.Platform platform;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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

	public Date getStartPlayTime() {
		return startPlayTime;
	}
	public void setStartPlayTime(Date startPlayTime) {
		this.startPlayTime = startPlayTime;
	}
	public Date getEndPlayTime() {
		return endPlayTime;
	}
	public void setEndPlayTime(Date endPlayTime) {
		this.endPlayTime = endPlayTime;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public int getUserChannelId() {
		return userChannelId;
	}
	public void setUserChannelId(int userChannelId) {
		this.userChannelId = userChannelId;
	}
	public EnumType.Platform getPlatform() {
		return platform;
	}
	public void setPlatform(EnumType.Platform platform) {
		this.platform = platform;
	}
	public String getStbId() {
		return stbId;
	}
	public void setStbId(String stbId) {
		this.stbId = stbId;
	}

}

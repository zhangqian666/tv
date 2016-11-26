package com.iptv.common.data;

import java.util.Date;
import java.util.List;

public class DashboardInfo {

	private String type; // 缺省、普通、定制
	private String contentType; // 内容类型
	private Date beginDate;			// 开始播放时间
	private Date endDate;			// 结束播放时间
	private String playUrl;			// 播放地址
	private List<DashboardPicture> image;
	private String title;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	public List<DashboardPicture> getImage() {
		return image;
	}
	public void setImage(List<DashboardPicture> image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}

package com.iptv.common.data;

import java.util.Date;


/**********************************
 * 
 * 开机欢迎界面之后的告示信息，分视频类和图片类。
 * 2015-01-13
 * 
 **********************************/
public class HomePrompt {

	private HomePromptType type; 			// 图片、视频
	private String backgroundImage; // 背景照片
	private int duration; 			// 持续时长
	private Date beginDate;			// 开始播放时间
	private Date endDate;			// 结束播放时间
	public HomePromptType getType() {
		return type;
	}
	public void setType(HomePromptType type) {
		this.type = type;
	}
	public String getBackgroundImage() {
		return backgroundImage;
	}
	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
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


	
}

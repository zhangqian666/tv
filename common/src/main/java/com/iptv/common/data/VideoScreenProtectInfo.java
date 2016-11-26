package com.iptv.common.data;

/**
 * 视频类屏保的详细信息
 * 
 * 2015-12-07
 */
public class VideoScreenProtectInfo {
	
	private int seq;						// 播放顺序
	private String url; 					// 播放地址
	private int duration;				// 持续时长
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	
}

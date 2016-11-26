package com.iptv.common.data;

import java.util.List;

/**
 * 视频类屏幕保护信息
 * 用于显示视频类的屏幕保护，对服务器压力较大。
 * 需要在服务器到位后再大量开放。
 * 2015-12-07
 */
public class ScreenProtect {
	// 图片信息列表
	private List<ScreenProtectImageInfo> images;

	public List<ScreenProtectImageInfo> getImages() {
		return images;
	}

	public void setImages(List<ScreenProtectImageInfo> images) {
		this.images = images;
	}
}

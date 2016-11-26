package com.iptv.common.data;

import java.util.List;

/**
 * 广告类信息
 * 先实现图片类广告
 * 2015-12-13
 */
public class Advertisement {
	// 图片信息列表
	private List<AdvertisementImageInfo> images;

	public List<AdvertisementImageInfo> getImages() {
		return images;
	}

	public void setImages(List<AdvertisementImageInfo> images) {
		this.images = images;
	}
}

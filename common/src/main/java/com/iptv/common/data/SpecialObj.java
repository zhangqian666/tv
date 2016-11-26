package com.iptv.common.data;

import java.util.List;

public class SpecialObj {
	private String bgimg;
	private String title;
	private String cover_image;
	private String id;
	private String categoryId;
	
	private EnumType.Platform platform;
	
	private List<SpecialItemObj> specialItemObjs;

	public String getBgimg() {
		return bgimg;
	}

	public void setBgimg(String bgimg) {
		this.bgimg = bgimg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover_image() {
		return cover_image;
	}

	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SpecialItemObj> getSpecialItemObjs() {
		return specialItemObjs;
	}

	public void setSpecialItemObjs(List<SpecialItemObj> specialItemObjs) {
		this.specialItemObjs = specialItemObjs;
	}

	public EnumType.Platform getPlatform() {
		return platform;
	}

	public void setPlatform(EnumType.Platform platform) {
		this.platform = platform;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
}

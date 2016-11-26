package com.iptv.common.data;

/**
 * 欢迎界面用到的数据
 * 2015-11-23
 *
 */
public class WelcomeInfo {
	private String backgroundImage; // 背景图片
	private String backMusic;		// 背景音乐
	private String topImageChinese;	// 顶部中文名字
	private String topImageEnglish;	// 顶部英文名字
	
	private String welcomeChineseText;	// 中文欢迎词，适用于带名字的欢迎
	private String welcomeEnglishText;	// 英文欢迎词，适用于带名字的欢迎
	
	public String getBackgroundImage() {
		return backgroundImage;
	}
	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	public String getBackMusic() {
		return backMusic;
	}
	public void setBackMusic(String backMusic) {
		this.backMusic = backMusic;
	}
	public String getTopImageChinese() {
		return topImageChinese;
	}
	public void setTopImageChinese(String topImageChinese) {
		this.topImageChinese = topImageChinese;
	}
	public String getTopImageEnglish() {
		return topImageEnglish;
	}
	public void setTopImageEnglish(String topImageEnglish) {
		this.topImageEnglish = topImageEnglish;
	}
	public String getWelcomeChineseText() {
		return welcomeChineseText;
	}
	public void setWelcomeChineseText(String welcomeChineseText) {
		this.welcomeChineseText = welcomeChineseText;
	}
	public String getWelcomeEnglishText() {
		return welcomeEnglishText;
	}
	public void setWelcomeEnglishText(String welcomeEnglishText) {
		this.welcomeEnglishText = welcomeEnglishText;
	}
}

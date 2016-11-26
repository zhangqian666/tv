package com.iptv.common.data;

public enum HomePromptType {
	
	Picture("图片"),		// 平时播放的缺省任务，无时间限制，每个盒子必须有，只有一个
	Video("视频");		// 指定任务，只有一项，用于重要用户。不显示其他的。
	
	private final String type;
	
	private HomePromptType(String type){
		this.type = type;
	}
	
	public String getType(){
		return this.type;
	}
	
	
	public static HomePromptType createType(String type) {
		switch (type) {
		case "Day_UsedDay_FreeAllVod":
			return HomePromptType.Picture;
		case "Day_UsedDay_FreeDaylyPayVod_PayPPV":
			return HomePromptType.Video;
		default:
			return HomePromptType.Picture;
		}
	}
	
	public static HomePromptType convertStr(String str) {
		if ("Day_UsedDay_FreeAllVod".equalsIgnoreCase(str)) {
			return HomePromptType.Picture;
		} else if ("Day_UsedDay_FreeDaylyPayVod_PayPPV".equalsIgnoreCase(str)) {
			return HomePromptType.Video;
		} else {
			return HomePromptType.Picture;
		}
	}
}

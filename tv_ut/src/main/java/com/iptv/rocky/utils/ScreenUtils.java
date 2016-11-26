package com.iptv.rocky.utils;

import com.iptv.rocky.model.TvApplication;

public final class ScreenUtils {
	
	public static int getChannelUnit() {
		return (int)(TvApplication.pixelHeight * 3 / 10.0);
	}
	
	public static int getChannelWidth() {
		return (int)(TvApplication.sTvChannelUnit * 1.0);
	}
	
	public static int getChannelHeight() {
		return (int)(TvApplication.sTvChannelUnit * 1.37);
	}
	
	public static int getHomeItemTopPadding() {
		return (int)(TvApplication.pixelHeight / 23.0);
	}
	
	public static int getTabLayoutHeight() {
		return (int)(TvApplication.pixelHeight / 5.0);
	}
	
	public static int getListTopPadding() {
		return (int)(TvApplication.pixelHeight / 4.5);
	}
	
	public static int getReflectionHeight() {
		return (int)(TvApplication.pixelHeight / 5.0);
	}
	
	public static int getHomeLeftMargin() {
		return (int)(TvApplication.pixelHeight / 6.0);
	}
	
	public static int getSearchKeyUnit() {
		return (int)(TvApplication.pixelWidth / 17.4);
	}
	
	public static int getKeyTextSize() {
		return (int)(TvApplication.dpiHeight / 26.0);
	}
	
	public static int getSearchKeyPadding() {
		return (int)(TvApplication.pixelWidth / 244.0);
	}
	
	public static int getListTagUnit() {
		return (int)(TvApplication.pixelWidth / 14.0);
	}
	
	public static int getSpecialListItemSpace() {
		return (int)(TvApplication.pixelHeight / 40.0);
	}
	
	/**
	 * 首页四个标题文字size
	 * */
	public static float getMasterTextSize() {
		return (float)(TvApplication.dpiHeight / 23.0);
	}
	
	/**
	 * getHomeCommViewTextSize
	 * */
	public static float getHomeCommViewTextSize() {
		return (float)(TvApplication.dpiHeight / 25.0);
	}
	
	/**
	 * 列表页 floatLayout title
	 * */
	public static float getFloatTextSize() {
		return (float)(TvApplication.dpiHeight / 32.0);
	}
	
	/**
	 * titleView size
	 * */
	public static float getTitleViewTextSize() {
		return (float)(TvApplication.dpiHeight / 20.0);
	}
	
	public static int getTabItemPadding() {
		return (int)(TvApplication.dpiHeight / 16.0);
	}
	
	public static int getScreenWidth() {
		return (int) TvApplication.pixelWidth;
	}
	
	public static int getScreenHeight(){
		return (int) TvApplication.pixelHeight;
	}
	
	public static int getLiveItemHeight() {
		return (int) (TvApplication.sTvChannelUnit / 1.8);
	}
	
	public static int getLiveItemWidth() {
		return (int) (TvApplication.sTvChannelUnit * 1.8);
	}
	
	public static int getVodMovieListItemHeight() {
		return (int)(TvApplication.pixelHeight / 16.0);
	}
	
	public static int getRecChanWidth() {
		return (int) (TvApplication.sTvChannelUnit * 1.2);
	}
	
	public static int getRecChanListItemHeight() {
		return (int)(TvApplication.pixelHeight / 20.0);
	}
	
}

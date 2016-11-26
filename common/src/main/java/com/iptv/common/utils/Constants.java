package com.iptv.common.utils;

import android.os.Environment;

public final class Constants {

	public static final int cPageNumber = 6;
	
	public static final int cRequestNumber = 16;
	
	public static final int cRequestIPTVNumber = 12;
	
	public static final int cSpecialCategoryNumber = 6;
	
	public static final int cImageCacheMaxSize = 100 * 1024 * 1024;
	
	public static final int cReflectionColor = 0x30FFFFFF;
	
	public static final int WEEKDAYS = 7;
	
	public static final String cListIdExtra = "LIST_ID_EXTRA";
	
	public static final String cListNameExtra = "LIST_NAME_EXTRA";
	
	public static final String cDetailIdExtra = "DETAIL_ID_EXTRA";
	
	public static final String CDETAIL_COLUMNCODE_EXTRA="DATAIL_COLUMNCODE_EXTRA";
	
	public static final String CDETAIL_CONTENTCODE_EXTRA="DATAIL_CONTENTCODE_EXTRA";
	
	public static final String COLUMNCODE_EXTRA_PRICE="DATAIL_COLUMNCODE_EXTRA_PRICE";
	
	public static final String cSpecialDetailExtra = "SPECIAL_ID_EXTRA";
	
	public static final String cSpecialDetailPlatformExtra = "SPECIAL_PLATFORM_EXTRA";
	
	public static final String cRootDirectory = Environment.getExternalStorageDirectory() + "/iptv/";
	
	public static final String cImageCacheDirectory = cRootDirectory + "images/";
	
	public static final String cUpdateDirectory = cRootDirectory + "update/";
	
	public static final String cVipGoToLogin = "VIP_GO_TO_LOGIN";
	
	public static final String cPlatformExtra = "PLATFORM";
	
}

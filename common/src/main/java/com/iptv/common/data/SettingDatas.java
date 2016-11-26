package com.iptv.common.data;

import java.util.HashMap;

/**
 * 所有设置相关的数据
 */
public class SettingDatas {

	public static final int TYPE_PLAY_SETTING_BITRATE = 1;
	public static final int TYPE_PLAY_SETTING_FRAMERATE = 2;
	public static final int TYPE_PLAY_SETTING_AUTOJUMP = 3;

	public static final String KEY_PLAY_BITRATE = "bitrate";
	public static final String KEY_PLAY_FRAMERATE = "framerate";
	public static final String KEY_PLAY_AOTUJUMP = "autojump";

	public static final int VAL_PLAY_BITRATE_ORIGINAL = 4;
	public static final int VAL_PLAY_BITRATE_BLUERAY = 3;
	public static final int VAL_PLAY_BITRATE_SUPER = 2;
	public static final int VAL_PLAY_BITRATE_HIGH = 1;
	public static final int VAL_PLAY_BITRATE_NORMAL = 0;

	public static final int VAL_PLAY_FRAMERATE_ORIGINAL = 0;
	public static final int VAL_PLAY_FRAMERATE_STRETCH = 1;

	public static final int VAL_PLAY_AUTOJUMP_ON = 1;
	public static final int VAL_PLAY_AUTOJUMP_OFF = 0;

	public HashMap<Integer, String> playBitrateDatas;
	public int playBitrateDefalutValue;

	public HashMap<Integer, String> playFramerateDatas;
	public int playFramerateDefalutValue;

	public HashMap<Integer, String> playAutojumpDatas;
	public int playAutojumpDefalutValue;

	public SettingDatas(int settingType) {
	}
}

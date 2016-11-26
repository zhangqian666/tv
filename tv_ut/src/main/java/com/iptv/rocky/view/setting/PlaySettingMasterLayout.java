package com.iptv.rocky.view.setting;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.iptv.common.data.SettingDatas;
import com.iptv.common.local.PlaySettingFactory;
import com.iptv.rocky.model.setting.SettingSelectItemAdapter;
import com.iptv.rocky.R;

public class PlaySettingMasterLayout extends RelativeLayout{

	private Context mContext;

	public PlaySettingMasterLayout(Context context) {
		this(context, null, 0);
	}

	public PlaySettingMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PlaySettingMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;
    }

	@SuppressLint("UseSparseArrays")
	@Override
	protected void onFinishInflate() {
        super.onFinishInflate();
        PlaySettingFactory factory = new PlaySettingFactory(mContext);

        //码流率
        SettingSelectItem bitrateItemView = (SettingSelectItem) findViewById(R.id.play_setting_bitrate);

        SettingDatas bitrateDatas = new SettingDatas(SettingDatas.TYPE_PLAY_SETTING_BITRATE);
        bitrateDatas.playBitrateDatas = new HashMap<Integer, String>(5);
        bitrateDatas.playBitrateDatas.put(SettingDatas.VAL_PLAY_BITRATE_ORIGINAL, getContext().getString(R.string.title_play_bitrate_original));
        bitrateDatas.playBitrateDatas.put(SettingDatas.VAL_PLAY_BITRATE_BLUERAY, getContext().getString(R.string.title_play_bitrate_blueray));
        bitrateDatas.playBitrateDatas.put(SettingDatas.VAL_PLAY_BITRATE_SUPER, getContext().getString(R.string.title_play_bitrate_super));
        bitrateDatas.playBitrateDatas.put(SettingDatas.VAL_PLAY_BITRATE_HIGH, getContext().getString(R.string.title_play_bitrate_high));
        bitrateDatas.playBitrateDatas.put(SettingDatas.VAL_PLAY_BITRATE_NORMAL, getContext().getString(R.string.title_play_bitrate_normal));
        bitrateDatas.playBitrateDefalutValue = SettingDatas.VAL_PLAY_BITRATE_SUPER;
        
        new SettingSelectItemAdapter(mContext,getResources().getString(R.string.play_setting_bitrate_title), SettingDatas.KEY_PLAY_BITRATE,
                factory,bitrateDatas.playBitrateDatas,bitrateDatas.playBitrateDefalutValue,bitrateItemView);
        //画面比
        SettingSelectItem frameRateItemView = (SettingSelectItem) findViewById(R.id.play_setting_framerate);

        SettingDatas framerateDatas = new SettingDatas(SettingDatas.TYPE_PLAY_SETTING_FRAMERATE);
        framerateDatas.playFramerateDatas = new HashMap<Integer, String>(2);
        framerateDatas.playFramerateDatas.put(SettingDatas.VAL_PLAY_FRAMERATE_ORIGINAL, getContext().getString(R.string.title_play_framerate_original));
        framerateDatas.playFramerateDatas.put(SettingDatas.VAL_PLAY_FRAMERATE_STRETCH, getContext().getString(R.string.title_play_framerate_stretch));
        framerateDatas.playFramerateDefalutValue = SettingDatas.VAL_PLAY_FRAMERATE_ORIGINAL;
        
        new SettingSelectItemAdapter(mContext,getResources().getString(R.string.play_setting_framerate_title), SettingDatas.KEY_PLAY_FRAMERATE,
                factory,framerateDatas.playFramerateDatas,framerateDatas.playFramerateDefalutValue,frameRateItemView);
        //自动跳过片头片尾
        SettingSelectItem autoJumpItemView = (SettingSelectItem) findViewById(R.id.play_setting_autojump);

        SettingDatas autojumpDatas = new SettingDatas(SettingDatas.TYPE_PLAY_SETTING_AUTOJUMP);
        autojumpDatas.playAutojumpDatas = new HashMap<Integer, String>(2);
        autojumpDatas.playAutojumpDatas.put(SettingDatas.VAL_PLAY_AUTOJUMP_ON, getContext().getString(R.string.title_play_autojump_on));
        autojumpDatas.playAutojumpDatas.put(SettingDatas.VAL_PLAY_AUTOJUMP_OFF, getContext().getString(R.string.title_play_autojump_off));
        autojumpDatas.playAutojumpDefalutValue = SettingDatas.VAL_PLAY_AUTOJUMP_ON;
        
        new SettingSelectItemAdapter(mContext,getResources().getString(R.string.play_setting_autojump_title), SettingDatas.KEY_PLAY_AOTUJUMP,
                factory,autojumpDatas.playAutojumpDatas,autojumpDatas.playAutojumpDefalutValue,autoJumpItemView);
    }
}

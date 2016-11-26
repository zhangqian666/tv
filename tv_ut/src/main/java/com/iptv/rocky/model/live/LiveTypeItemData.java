package com.iptv.rocky.model.live;

import android.content.Context;
import android.content.Intent;

import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.tcl.LiveChannelPlayActivity;

public abstract class LiveTypeItemData extends BaseMetroItemData {
	
	public LiveTypePageItem pageItem;

	@Override
	public void onClick(Context context) {
        Intent intent = new Intent(context, LiveChannelPlayActivity.class);
        //intent.putExtra(Constants.cDetailIdExtra, CommonUtils.parseInt(pageItem.contentid));
        intent.putExtra(Constants.cDetailIdExtra, pageItem.contentid);
        //LogUtils.debug("准备去播放的频道:"+pageItem.contentid);
        context.startActivity(intent);
    }
	
    public void initSpecialData() {

    }
    
}

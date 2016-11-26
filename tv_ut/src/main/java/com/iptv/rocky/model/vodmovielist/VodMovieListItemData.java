package com.iptv.rocky.model.vodmovielist;

import android.content.Context;

import com.iptv.common.data.VodChannel;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.utils.TvUtils;

public abstract class VodMovieListItemData extends BaseMetroItemData {
	
	public VodMovieListPageItem pageItem;
	
	private VodChannel vodChannel;

	@Override
	public void onClick(Context context) {
		TvUtils.playVideo(context, vodChannel);
    }
	
    public void initSpecialData(VodChannel info) {
    	this.vodChannel = info;
    }
    
}

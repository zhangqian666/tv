package com.iptv.rocky.model.recchan;

import android.content.Context;

import com.iptv.common.data.RecBill;
import com.iptv.rocky.RecChanActivity;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.utils.TvUtils;

public abstract class RecBillItemData extends BaseMetroItemData {
	
	public RecBillPageItem pageItem;
	
	private RecBill recBill;

	@Override
	public void onClick(Context context) {
		TvUtils.playVideo(context, RecChanActivity.mRecChan, recBill);
    }
	
    public void initSpecialData(RecBill info) {
    	this.recBill = info;
    }
    
}

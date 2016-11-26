package com.iptv.rocky.model.hotel;

import android.content.Context;
import com.iptv.common.data.MyHotelPicture;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.utils.TvUtils;

public abstract class MyHotelPictureListItemData extends BaseMetroItemData {
	
	public MyHotelPictureListPageItem pageItem;
	
	private MyHotelPicture myHotelPicture;

	@Override
	public void onClick(Context context) {
		TvUtils.myHotelshowPictures(context, myHotelPicture);
    }
	
    public void initSpecialData(MyHotelPicture info) {
    	this.myHotelPicture = info;
    }
    
}

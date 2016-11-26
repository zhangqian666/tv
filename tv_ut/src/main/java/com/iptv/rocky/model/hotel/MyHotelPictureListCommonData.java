package com.iptv.rocky.model.hotel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.view.myHotel.MyHotelPictureListCommonView;
import com.iptv.rocky.R;

public class MyHotelPictureListCommonData extends MyHotelPictureListItemData {
	
	private MyHotelPictureListCommonView mContentView;
	
	public boolean isFontBlod = false;
	
	@Override
	public View getView(Context context) {
		MyHotelPictureListCommonView view = mContentView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = (MyHotelPictureListCommonView) inflater.inflate(R.layout.my_hotel_picture_list_page_item_common, null);
            view.initView(this);
            mContentView = view;
        }
        return view;
    }
	
}

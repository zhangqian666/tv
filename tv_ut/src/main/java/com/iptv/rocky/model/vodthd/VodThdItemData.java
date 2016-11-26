package com.iptv.rocky.model.vodthd;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodSec;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.R;
import com.iptv.rocky.VodMovieColumnActivity;
import com.iptv.rocky.VodMovieListActivity;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.view.vodthd.VodThdItemView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

public class VodThdItemData extends BaseMetroItemData {
	
	public VodSec categoryItem;
	
	public String mColumnCode;
	
	private VodThdItemView mContentView;

	public VodThdItemData() {
		widthSpan = 1.5;
		heightSpan = 1.0;
	}
	
	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			mContentView = (VodThdItemView)LayoutInflater.from(context).inflate(R.layout.vodthd_item, null);
			mContentView.initView(this);
		}
		return mContentView;
	}
	
	@Override
	public void onClick(Context context) {
		Intent intent = null;
		if (categoryItem.sub_content_type == EnumType.SubContentType.IPTV_VIRTICAL_POSTER_CONTENT_lIST){
			intent = new Intent(context, VodMovieColumnActivity.class);
		} else if(categoryItem.sub_content_type == EnumType.SubContentType.IPTV_HORIZONTAL_POSTER_CONTENT_lIST){
			intent = new Intent(context, VodMovieColumnActivity.class);
		} else if(categoryItem.sub_content_type == EnumType.SubContentType.UNKNOW){
			intent = new Intent(context, VodMovieColumnActivity.class);
			intent.putExtra(Constants.COLUMNCODE_EXTRA_PRICE, mColumnCode);
		}else{
			intent = new Intent(context, VodMovieListActivity.class);
		}
		intent.putExtra(Constants.cListIdExtra, categoryItem.type_id);
		intent.putExtra(Constants.cListNameExtra, categoryItem.title);
		context.startActivity(intent);
	}

//	@Override
//	public void onOwnerFocusChange(boolean hasFocus) {
//		if (hasFocus) {
//			mContentView.startMarquee();
//		} else {
//			mContentView.stopMarquee();
//		}
//	}

}

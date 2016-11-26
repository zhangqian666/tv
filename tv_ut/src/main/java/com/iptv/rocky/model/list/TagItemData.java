package com.iptv.rocky.model.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.view.list.TagItemView;
import com.iptv.rocky.R;

public class TagItemData extends BaseMetroItemData {
	
//	public TagInfo tagInfo;
	
	private TagItemView mContentView;
	private onClickListener mClickListener;

	public TagItemData(){
		
	}
	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = (TagItemView)inflater.inflate(R.layout.list_tag_item, null);
//			mContentView.initView(tagInfo);
		}		
		return mContentView;
	}

	@Override
	public void onClick(Context context) {
		if (mClickListener != null) {
//			mClickListener.onClick(tagInfo.Name);
		}
	}
	
	public void setOnClickListener(onClickListener clickListener) {
		mClickListener = clickListener;
	}
	
	public interface onClickListener {
		public void onClick(String title);
	}

}

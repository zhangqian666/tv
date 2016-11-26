package com.iptv.rocky.model.special;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;

import com.iptv.common.data.SpecialItemObj;
import com.iptv.rocky.R;
import com.iptv.rocky.base.BaseListItemData;
import com.iptv.rocky.view.special.SpecialListItemView;

public class SpecialListItemData extends BaseListItemData {
	
	public SpecialItemObj sItemObj;

	private SpecialListItemView mContentView;
	private onScrollListener mScrollListener;
	
	public void setView(SpecialItemObj sItemObj) {
		this.sItemObj = sItemObj;
		mContentView.initView(sItemObj);
	}
	
	public void destroy() {
		mContentView.destroy();
	}
	
	@Override
	public View getView(Context context) {
		if (mContentView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = (SpecialListItemView)inflater.inflate(R.layout.special_list_item_common, null);
			mContentView.setOnKeyListener(onKeyListener);
		}
		return mContentView;
	}
	
	private OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			
			int action = event.getAction();
			
			if(action == KeyEvent.ACTION_DOWN){
				if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
					if (mScrollListener != null) {
						mScrollListener.onScroll(KeyEvent.KEYCODE_DPAD_LEFT);
					}
				}
				
				if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
					if (mScrollListener != null) {
						mScrollListener.onScroll(KeyEvent.KEYCODE_DPAD_RIGHT);
					}
				}
			}			
			return false;
		}
	};
	
	public void setOnScrollListener(onScrollListener scrollListener) {
		mScrollListener = scrollListener;
	}
	
	public interface onScrollListener {
		public void onScroll(int direction);
	}
}

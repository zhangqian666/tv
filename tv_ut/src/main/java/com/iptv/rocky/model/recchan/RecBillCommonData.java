package com.iptv.rocky.model.recchan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.rocky.view.recchan.RecBillCommonView;
import com.iptv.rocky.R;

public class RecBillCommonData extends RecBillItemData {
	
	private RecBillCommonView mContentView;
	
	public boolean isFontBlod = false;
	
	@Override
	public View getView(Context context) {
		RecBillCommonView view = mContentView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = (RecBillCommonView) inflater.inflate(R.layout.rec_bill_page_item_common, null);
            view.initView(this);
            mContentView = view;
        }
        return view;
    }
	
    public void onOwnerFocusChange(boolean hasFocus) {
    	mContentView.onOwnerFocusChange(hasFocus);
	}
}

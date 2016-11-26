package com.iptv.rocky.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.base.BaseMetroItemView;
import com.iptv.rocky.model.TvApplication;

public class RecBillMetroItemView extends FrameLayout
	implements View.OnFocusChangeListener, TVItemViewReloadable {

	private Context mContext;
	    
	private BaseMetroItemView mItemView;
    
    public RecBillMetroItemView(Context context) {
		this(context, null, 0);
	}
	
	public RecBillMetroItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RecBillMetroItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
        mContext = context;
        //int padding = TvApplication.sTvItemPadding;
		
		setWillNotDraw(false);
		//setPadding(padding, padding, padding, padding);
	}
	
	public void addMetroItem(BaseMetroItemData itemData) {
		mItemView = new BaseMetroItemView(mContext);
		mItemView.addMetroItem(itemData);
		mItemView.setOnFocusChangeListener(this); 
		addView(mItemView, getMetroItemLayoutParams());
	}
	
	protected FrameLayout.LayoutParams getMetroItemLayoutParams(){
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		int margin = TvApplication.sTvItemPadding;
		lp.setMargins(margin, margin, margin, margin);
		return lp;
	}
	
	public void destroy() {
	}
	
	public boolean isNotTopPadding() {
		return mItemView.isNotTopPadding();
	}
	
	public View getView(Context context) {
		return mItemView.getView(context);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		mItemView.onFocusChange(hasFocus);
	}

    @Override
    public void reloadData() {
    	destroy();
        ((TVItemViewReloadable) mItemView.getView(mContext)).reloadData();
    }
}

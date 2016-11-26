package com.iptv.rocky.view.recchan;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.iptv.common.data.RecBill;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.recchan.RecBillItemData;
import com.iptv.rocky.model.recchan.RecBillLayoutFactory;
import com.iptv.rocky.model.recchan.RecBillLayoutItem;
import com.iptv.rocky.model.recchan.RecBillPageItem;
import com.iptv.rocky.view.TvHorizontalScrollView;
import com.iptv.rocky.view.recchan.RecBillViewPager.OnRecBillPageScrollChangeListener;

public class RecBillPageScrollView extends TvHorizontalScrollView {
	
	private RecBillMetroView mMetroView;
	
	private OnRecBillPageScrollChangeListener onRecBillPageScrollChangeListener;
	
	public void setOnRecBillPageScrollChangeListener(OnRecBillPageScrollChangeListener onRecBillPageScrollChangeListener) {
		this.onRecBillPageScrollChangeListener = onRecBillPageScrollChangeListener;
	}

	public RecBillPageScrollView(Context context) {
		this(context, null, 0);
	}
	
	public RecBillPageScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RecBillPageScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setWillNotDraw(true);
    	setSmoothScrollingEnabled(true);
	}
	
	public void createView(RecBillLayoutItem item, OnRecBillPageScrollChangeListener listener) {
		onRecBillPageScrollChangeListener = listener;
		mMetroView = new RecBillMetroView(getContext());
		for (int i = 0, j = item.items.size(); i < j; i++) {
			RecBillPageItem page = item.items.get(i);
			RecBillItemData data = RecBillLayoutFactory.createBillData(page);
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
		addView(mMetroView);
	}
	
	public void createView(ArrayList<RecBill> infos, boolean isAutoFocus) {
		if (mMetroView == null) {
			mMetroView = new RecBillMetroView(getContext());
			mMetroView.isAutoFocus = isAutoFocus;
			addView(mMetroView);
		} else {
			mMetroView.isAutoFocus = isAutoFocus;
			mMetroView.destroy();
			mMetroView.clear();
			//mMetroView.removeAllViewsInLayout();
		}
		
		for (int i = 0, j = infos.size(); i < j; i++) {
			RecBillItemData data = RecBillLayoutFactory.createBillData(infos.get(i));
			if (data != null) {
				mMetroView.addMetroItem(data);
			}
		}
	}
	
	public void destroy() {
		if (mMetroView != null) {
			mMetroView.destroy();
		}
	}
	
	@Override
	public void onItemScrolled(int nCount)
	{
		onRecBillPageScrollChangeListener.onItemScrolled(nCount);
	}
	
	@Override
    protected boolean onRequestFocusInDescendants(int direction,
            Rect previouslyFocusedRect) {
		LogUtils.debug("direction:" + direction);
		return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
	}

}

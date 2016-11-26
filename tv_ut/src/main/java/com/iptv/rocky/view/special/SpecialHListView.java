package com.iptv.rocky.view.special;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.common.data.SpecialItemObj;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.special.SpecialListAdapter;
import com.iptv.rocky.model.special.SpecialListItemData;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.TvHorizontalListView;

/**
 * 专辑详情页的HListview
 * */
public class SpecialHListView extends TvHorizontalListView {
	
	private SpecialListAdapter adapter;
	
	public SpecialHListView(Context context) {
		this(context, null, 0);
	}
	
	public SpecialHListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SpecialHListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		int paddingTop = ScreenUtils.getScreenHeight() - ScreenUtils.getChannelHeight()*4/3;
		setPadding(TvApplication.sTvLeftMargin, paddingTop, TvApplication.sTvLeftMargin, 0);
	}
	
	public void initView(List<SpecialItemObj> sItemObjs, SpecialListItemData.onScrollListener scrollListener) {
		if (adapter == null) {
			adapter = new SpecialListAdapter(getContext(), sItemObjs, scrollListener);
		}
		setAdapter(adapter);
	}
	
}

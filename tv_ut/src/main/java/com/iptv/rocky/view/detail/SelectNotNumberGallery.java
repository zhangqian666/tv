package com.iptv.rocky.view.detail;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.common.data.VodDetailInfo;
import com.iptv.rocky.model.detail.SelectNotNumberAdapter;
import com.iptv.rocky.view.TvGallery;

public class SelectNotNumberGallery extends TvGallery {

	public final static int sPageNumber = 12;

	public SelectNotNumberGallery(Context context) {
		this(context, null, 0);
	}

	public SelectNotNumberGallery(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SelectNotNumberGallery(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setData(VodDetailInfo obj) {
		if (obj.SUBVODIDLIST == null || obj.SUBVODIDLIST.size() <= 0) {
			return;
		}

		int size = obj.SUBVODIDLIST.size();
		int page = (size / sPageNumber) + (size % sPageNumber > 0 ? 1 : 0);

		ArrayList<SelectNotNumberMetroView> metroViews = new ArrayList<SelectNotNumberMetroView>(
				page);
		for (int i = 0; i < page; i++) {
			SelectNotNumberMetroView metroView = new SelectNotNumberMetroView(
					getContext());
			metroViews.add(metroView);
		}
		setAdapter(new SelectNotNumberAdapter(obj, metroViews));
	}

}

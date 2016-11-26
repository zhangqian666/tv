package com.iptv.rocky.view.vodmovielist;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.base.VodMovieListBaseMetroView;
import com.iptv.rocky.model.TvApplication;

public class VodMovieListMetroView extends VodMovieListBaseMetroView {
	
	public VodMovieListMetroView(Context context) {
		this(context, null, 0);
	}
	
	public VodMovieListMetroView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public VodMovieListMetroView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		isAutoFocus = true;
	}
	
	public void clear() {
		destroy();
	}

	@Override
	protected int getColumenNumber() {
		return 10;
	}
	
	@Override
	protected MetroItemOrientation getMetroItemOrientation() {
		return MetroItemOrientation.VERTICAL;
	}

	@Override
	protected int getItemUnitNumber() {
		return TvApplication.sTvChannelUnit;
	}

	@Override
	protected boolean isRelection(BaseMetroItemData itemData) {
		return false;
	}

//	@Override
//	protected boolean isTopPadding() {
//		return true;
//	}

//	@Override
//	protected LayoutParams setItemPosition(BaseMetroItemData itemData) {
//		LiveTypeItemData data = (LiveTypeItemData)itemData;
//		int width = (int)(data.widthSpan * TvApplication.sTvChannelUnit);
//		int height = (int)(data.heightSpan * TvApplication.sTvChannelUnit) + TvApplication.sTvItemTopPadding;
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
//		Point p1 = mOnePoint;
//		Point p2 = mTwoPoint;
//		if (p1.x == 0 && p2.x == 0) {
//			p1.x = width;
//			p1.y = height;
//			if (data.heightSpan >= 2) {
//				p2.x = width;
//				p2.y = height;
//			}
//		} else if (p1.x > p2.x) {
//			data.isNotTopPadding = true;
//			height -= TvApplication.sTvItemTopPadding;
//			if (data.heightSpan > 1.0) {
//				data.heightSpan = 1.0;
//				data.widthSpan = 1.0;
//				height = (int)(data.heightSpan * TvApplication.sTvChannelUnit);
//				width = (int)(data.widthSpan * TvApplication.sTvChannelUnit);
//				params.width = width;
//			}
//			params.height = height;
//			params.topMargin = p1.y;
//			params.leftMargin = p2.x;
//			p2.x += width;
//			p2.y = height;
//			if (data.heightSpan >= 2) {
//				p1.x += width;
//				p1.y = height;
//			}
//		} else if (p1.x == p2.x && p1.y - p2.y == height) {
//			params.topMargin = p2.y;
//			params.leftMargin = p1.x - width;
//		} else {
//			params.topMargin = 0;
//			params.leftMargin = p1.x;
//			p1.x += width;
//			p1.y = height;
//			if (data.heightSpan >= 2) {
//				p2.x += width;
//				p2.y = height;
//			}
//		}
//		return params;
//	}

}

package com.iptv.rocky.view.detail;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.TabItemView;
import com.iptv.rocky.R;

public class SelectNotNumberMasterView extends RelativeLayout {

	private final int ORDER_POSITIVE = 1;
	private final int ORDER_NEGAYIVE = 2;
	private int ORDER_CURRENT = ORDER_NEGAYIVE;

	private SelectNotNumberGallery mGallery;
	private TabItemView mOrderView;

	private List<VodChannel> mPlayLinks;

	private VodDetailInfo obj;

	public SelectNotNumberMasterView(Context context) {
		this(context, null, 0);
	}

	public SelectNotNumberMasterView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SelectNotNumberMasterView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void createView(VodDetailInfo obj) {
		this.obj = obj;

		List<VodChannel> datas = obj.SUBVODIDLIST;
		if (datas == null || datas.size() <= 0) {
			return;
		}
		mPlayLinks = obj.SUBVODIDLIST;

		mGallery.setData(obj);
	}

	private OnClickListener orderClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (ORDER_CURRENT == ORDER_NEGAYIVE) {
				ORDER_CURRENT = ORDER_POSITIVE;
				mOrderView.setText(getResources().getString(
						R.string.order_positive));
			} else {
				ORDER_CURRENT = ORDER_NEGAYIVE;
				mOrderView.setText(getResources().getString(
						R.string.order_negtive));
			}

			Collections.reverse(mPlayLinks);
			// obj.setPlayLinkObjs(mPlayLinks);
			mGallery.setData(obj);
		}
	};

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mGallery = (SelectNotNumberGallery) findViewById(R.id.select_gallery);
		mOrderView = (TabItemView) findViewById(R.id.select_order);
		mOrderView.setText(getResources().getString(R.string.order_negtive));
		RelativeLayout.LayoutParams lp = (LayoutParams) mOrderView
				.getLayoutParams();
		lp.height = TvApplication.sTvTabHeight;
		lp.leftMargin = TvApplication.sTvLeftMargin / 2;
		int padding = TvApplication.sTvTabItemPadding;
		mOrderView.setPadding(padding, 0, padding, 0);
		mOrderView.setOnClickListener(orderClickListener);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (ORDER_CURRENT == ORDER_POSITIVE) {
			Collections.reverse(mPlayLinks);
			// obj.setVideoList(mPlayLinks);
		}
	}

}

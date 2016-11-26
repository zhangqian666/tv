package com.iptv.rocky.view.voddetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import com.iptv.common.data.VodChannel;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.base.BaseListItemView;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.FloatLayerView;
import com.iptv.rocky.view.horizontallistview.AbsHorizontalListView;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.iptv.rocky.R;

public class DetailSimilarListItemView extends BaseListItemView implements
		AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener {

	private VodChannel mVodInfo;
	private AsyncImageView mBackImageView;
	private FloatLayerView mFloatLayer;

	public DetailSimilarListItemView(Context context) {
		this(context, null, 0);
	}

	public DetailSimilarListItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DetailSimilarListItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		isRelection = true;
		AbsHorizontalListView.LayoutParams params = new AbsHorizontalListView.LayoutParams(
			ScreenUtils.getChannelWidth(),
			ScreenUtils.getChannelHeight());
		setLayoutParams(params);
	}

	public void initView(VodChannel vodInfo) {
		mVodInfo = vodInfo;
		mFloatLayer.initView(vodInfo.VODNAME);
		mBackImageView.setImageUrl(vodInfo.PICPATH);
	}

	@Override
	public void onLoadComplete(String imageUri, View view, Bitmap loadedImage) {
		isViewLoaded = true;
	}

	@Override
	public void onLoadFailed(String imageUri, View view, FailType failType) {
		isViewLoaded = true;
	}

	@Override
	public boolean isNotTopPadding() {
		return true;
	}

	@Override
	public void onClick(Context context) {
//		int vid = mVodInfo.VODID;
//		VodChannelDetailActivity act = (VodChannelDetailActivity) getContext();
//		act.load(vid);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mBackImageView = (AsyncImageView) findViewById(R.id.detail_item_backimage);
		mFloatLayer = (FloatLayerView) findViewById(R.id.tv_floatlayer);
		
		mBackImageView.setImageLoadedListener(this);
		mBackImageView.setImageFailedListener(this);
	}

}

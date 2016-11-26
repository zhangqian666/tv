package com.iptv.rocky.view.usercenter.vodhistory;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.base.BaseListItemView;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.FloatLayerView;
import com.iptv.rocky.view.horizontallistview.AbsHorizontalListView;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.iptv.rocky.R;

public class HistoryItemView extends BaseListItemView implements
		AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener {

	private AsyncImageView mBackImageView;
	private FloatLayerView mFloatLayer;
	private ItemClickInterface mClickListener;
	private Context mContext;

	public HistoryItemView(Context context) {
		this(context, null, 0);
	}

	public HistoryItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HistoryItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;
		isRelection = true;
		AbsHorizontalListView.LayoutParams params = new AbsHorizontalListView.LayoutParams(
				ScreenUtils.getChannelWidth(), ScreenUtils.getChannelHeight());
		setLayoutParams(params);
	}

	public void initView(HistoryChannelInfo vodInfo) {
		String subTitle = TvUtils.createPlayPosition(mContext, vodInfo.subtitle, vodInfo.playposition);
		
		mFloatLayer.initView(vodInfo.VODNAME, subTitle, false);
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
		if (this.mClickListener != null) {
			this.mClickListener.onItemClick();
		}
	}

	@Override
	public void processFocus(boolean hasFocus) {
		super.processFocus(hasFocus);

		if (hasFocus) {
			mFloatLayer.displaySubTitle();
		} else {
			mFloatLayer.hideSubTitle();
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mBackImageView = (AsyncImageView) findViewById(R.id.history_item_backimage);
		mFloatLayer = (FloatLayerView) findViewById(R.id.tv_floatlayer);

		mBackImageView.setImageLoadedListener(this);
		mBackImageView.setImageFailedListener(this);
	}

	public interface ItemClickInterface {
		public void onItemClick();
	};

	public void setItemClickListener(ItemClickInterface clickListener) {
		this.mClickListener = clickListener;
	}
	
}

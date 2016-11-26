package com.iptv.rocky.view.movie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.VodChannelDetailActivity;
import com.iptv.rocky.base.BaseListItemView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.FloatLayerView;
import com.iptv.rocky.view.horizontallistview.AbsHorizontalListView;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.iptv.rocky.R;

public class ListItemView extends BaseListItemView implements
		AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener {

	private VodChannel mVodInfo;
	private String mColumnCode="";
	private AsyncImageView mBackImageView;
	private FloatLayerView mFloatLayer;
	

	public ListItemView(Context context) {
		this(context, null, 0);
	}

	public ListItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		isRelection = true;
		AbsHorizontalListView.LayoutParams params = new AbsHorizontalListView.LayoutParams(
				ScreenUtils.getChannelWidth(), ScreenUtils.getChannelHeight());
		setLayoutParams(params);
	}

	public void initView(VodChannel vodInfo,String columnCode) {
		mVodInfo = vodInfo;
		mColumnCode=columnCode;
		mFloatLayer.initView(vodInfo.VODNAME);
		mBackImageView.setImageUrl(vodInfo.PICPATH);
		
		mFloatLayer.initView(mVodInfo.VODNAME, "", false);
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
	public void processFocus(boolean hasFocus) {
		super.processFocus(hasFocus);

		if (hasFocus) {
			mFloatLayer.displaySubTitle();
			mFloatLayer.startMarquee();
		} else {
			mFloatLayer.hideSubTitle();
			mFloatLayer.stopMarquee();
		}
	}

	@Override
	public void onClick(Context context) {
		Intent intent = new Intent(context, VodChannelDetailActivity.class);
		if (TvApplication.platform==EnumType.Platform.ZTE) {
			intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, mVodInfo.columncode);
			intent.putExtra(Constants.CDETAIL_CONTENTCODE_EXTRA, mVodInfo.CONTENTCODE);
			LogUtils.info("mVodInfo.columncode---"+mVodInfo.columncode+"   mVodInfo.contentCode--"+mVodInfo.CONTENTCODE+"---platform=="+mVodInfo.platform.toString());
			
		}else{
			intent.putExtra(Constants.cDetailIdExtra, mVodInfo.VODID);
			intent.putExtra(Constants.COLUMNCODE_EXTRA_PRICE, mColumnCode);
		}
		intent.putExtra(Constants.cPlatformExtra, mVodInfo.platform.toString());
		
		context.startActivity(intent);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mBackImageView = (AsyncImageView) findViewById(R.id.list_item_backimage);
		mFloatLayer = (FloatLayerView) findViewById(R.id.tv_floatlayer);
		
		mBackImageView.setImageLoadedListener(this);
		mBackImageView.setImageFailedListener(this);
	}

}

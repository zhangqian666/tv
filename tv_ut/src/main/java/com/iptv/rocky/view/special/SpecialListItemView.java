package com.iptv.rocky.view.special;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import com.iptv.common.data.EnumType.Platform;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.SpecialItemObj;
import com.iptv.common.utils.Constants;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.R;
import com.iptv.rocky.VodChannelDetailActivity;
import com.iptv.rocky.base.BaseListItemView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.FloatLayerView;
import com.iptv.rocky.view.ScaleAsyncImageView;
import com.iptv.rocky.view.horizontallistview.AbsHorizontalListView;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;


public class SpecialListItemView extends BaseListItemView 
	implements AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener {
	
	private AsyncImageView mBackImageView;
	private FloatLayerView mFloatLayer;
	private ScaleAsyncImageView mVip;
	private SpecialItemObj mItemObj;
	
	public SpecialListItemView(Context context) {
		this(context, null, 0);
	}
	
	public SpecialListItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SpecialListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		isRelection = true;
		int paddingLR = ScreenUtils.getSpecialListItemSpace();
		int padding = TvApplication.sTvItemPadding;
		AbsHorizontalListView.LayoutParams params = new AbsHorizontalListView.LayoutParams(ScreenUtils.getChannelWidth() + paddingLR*2, ScreenUtils.getChannelHeight() + padding*2);
		setLayoutParams(params);
		setPadding(paddingLR, padding, paddingLR, padding);
	}
	
	public void initView(SpecialItemObj sItemObj) {
		mItemObj = sItemObj;
		mFloatLayer.initView(sItemObj.getTitle());
		mBackImageView.setImageUrl(sItemObj.getImage());
		
		if (sItemObj.isVip()) {
			mVip.setVisibility(View.VISIBLE);
		} else {
			mVip.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected View[] getDrawChildren() {
		return new View[] {mBackImageView};
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
		
		//int sId = mItemObj.getEpg_id();
		String sId = mItemObj.getId();
		
		Intent intent = new Intent(context, VodChannelDetailActivity.class);
		if (TvApplication.platform==EnumType.Platform.ZTE) {
			intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, mItemObj.getColumnCode());
			intent.putExtra(Constants.CDETAIL_CONTENTCODE_EXTRA, mItemObj.getContentCode());
		}else{
			intent.putExtra(Constants.cDetailIdExtra, sId);
		}
		intent.putExtra(Constants.cPlatformExtra,mItemObj.getPlatform().toString());
		
		context.startActivity(intent);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mBackImageView = (AsyncImageView)findViewById(R.id.list_item_backimage);
		mFloatLayer = (FloatLayerView)findViewById(R.id.tv_floatlayer);
		mVip = (ScaleAsyncImageView) findViewById(R.id.list_item_vip);
		
		mFloatLayer.setAlpha(0f);
		mBackImageView.setImageLoadedListener(this);
		mBackImageView.setImageFailedListener(this);
	}
	
	@Override
	public void processFocus(boolean hasFocus) {
		super.processFocus(hasFocus);
		if (hasFocus) {
			mFloatLayer.setAlpha(1f);
			mFloatLayer.startMarquee();
		} else {
			mFloatLayer.setAlpha(0f);
			mFloatLayer.stopMarquee();
		}
	}
}

package com.iptv.rocky.view.vodsearch;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.EnumType.Platform;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.Constants;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.R;
import com.iptv.rocky.VodChannelDetailActivity;
import com.iptv.rocky.base.BaseListItemView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.FloatLayerView;
import com.iptv.rocky.view.horizontallistview.AbsHorizontalListView;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

public class SearchItemView extends BaseListItemView {
    private Context mContext;

	private VodChannel mVodInfo;
	private AsyncImageView mBackImageView;
	private FloatLayerView mFloatLayer;

	public SearchItemView(Context context) {
		this(context, null, 0);
	}

	public SearchItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SearchItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        mContext = context;
        AbsHorizontalListView.LayoutParams params = new AbsHorizontalListView.LayoutParams((int) (TvApplication.sTvChannelUnit/1.37), TvApplication.sTvChannelUnit);
		setLayoutParams(params);
	}
	
	public void initView(VodChannel vodInfo) {
		mVodInfo = vodInfo;
		mFloatLayer.initView(vodInfo.VODNAME);
		mFloatLayer.setTitleTextSize((float) (TvApplication.sTvFloatTextSize * 0.8));
        mBackImageView.setImageUrl(vodInfo.PICPATH);
        
        String subTitle = "";//TvUtils.createSubTitle(mVodInfo.type, mVodInfo.vsValue, mVodInfo.vsTitle, mContext);
        mFloatLayer.initView(mVodInfo.VODNAME, subTitle, false);
    }
	
	@Override
	public boolean isNotTopPadding() {
		return true;
	}
	
	@Override
	public void onClick(Context context) {
		Intent intent = new Intent(context, VodChannelDetailActivity.class);
		if(TvApplication.platform==EnumType.Platform.ZTE){
			intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, mVodInfo.columncode);
			intent.putExtra(Constants.CDETAIL_CONTENTCODE_EXTRA, mVodInfo.CONTENTCODE);
		}else{
			intent.putExtra(Constants.cDetailIdExtra, mVodInfo.VODID);
		}
		intent.putExtra(Constants.cPlatformExtra, mVodInfo.platform.toString());
		
		context.startActivity(intent);
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
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mBackImageView = (AsyncImageView)findViewById(R.id.search_item_backimage);
		mFloatLayer = (FloatLayerView)findViewById(R.id.tv_floatlayer);
		mFloatLayer.stopMarquee();
	}

}

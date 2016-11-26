package com.iptv.rocky.view.vodmoviecolumn;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.R;
import com.iptv.rocky.VodChannelDetailActivity;
import com.iptv.rocky.base.BaseListItemView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.view.gridview.TwoWayAbsListView;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class VodMovieColumnGridItemView extends BaseListItemView 
	implements AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener {
	
	private Context context;
	
	private TextViewDip txt;
	
	private AsyncImageView mBackImageView;
	
	private VodChannel vodChannel;
	
	private String mColumnCode;
	
	public VodMovieColumnGridItemView(Context context) {
		this(context, null, 0);
	}
	
	public VodMovieColumnGridItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public VodMovieColumnGridItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		setPadding(0, 0, 0, 0);
		TwoWayAbsListView.LayoutParams params = new TwoWayAbsListView.LayoutParams((int)(TvApplication.sTvChannelUnit * 1.2), (int)(TvApplication.sTvChannelUnit * 0.8));
		setLayoutParams(params);
	}
	
	@Override
	public void onLoadComplete(String imageUri, View view, Bitmap loadedImage) {
	}

	@Override
	public void onLoadFailed(String imageUri, View view, FailType failType) {
	}
	
	public void fillViewData(VodChannel info,String columnCode) {
		reset();
		if (info == null) {
			return;
		}
		
		this.vodChannel = info;
		this.mColumnCode=columnCode;
		txt.setText(info.VODNAME);
		mBackImageView.setImageUrl(info.PICPATH);
	}
	
	private void reset() {
		if (txt.getVisibility() != View.VISIBLE) {
			txt.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean isNotTopPadding() {
		return true;
	}
	
	@Override
	public void onClick(Context context) {
		// 单集，直接播放
		LogUtils.error("点播横版列表,开始直接播放视频");
		
		
		 if (vodChannel.VODID!=null&&!vodChannel.VODID.isEmpty()) {
			if(TvApplication.platform==EnumType.Platform.ZTE){
				 Intent intent = new Intent(context, VodChannelDetailActivity.class);
	             intent.putExtra(Constants.cPlatformExtra, vodChannel.platform.toString());
	             intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, vodChannel.columncode);
	             intent.putExtra(Constants.CDETAIL_CONTENTCODE_EXTRA, vodChannel.CONTENTCODE);
	             intent.putExtra(Constants.COLUMNCODE_EXTRA_PRICE, mColumnCode);
	             context.startActivity(intent);
			}else{
				 Intent intent = new Intent(context, VodChannelDetailActivity.class);
	             intent.putExtra(Constants.cPlatformExtra, vodChannel.platform.toString());
	             intent.putExtra(Constants.cDetailIdExtra, vodChannel.VODID);
	             intent.putExtra(Constants.COLUMNCODE_EXTRA_PRICE, mColumnCode);
	             context.startActivity(intent);
			}
            
         }
		
		//TvUtils.playVideo(context, vodChannel);
	}
	
	@Override
	protected LayoutParams getBorderLayoutParam() {
		return new RelativeLayout.LayoutParams((int)(TvApplication.sTvChannelUnit * 1.2), (int)(TvApplication.sTvChannelUnit * 0.8));
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		// textsize 参考 floatlayout
		txt = (TextViewDip) findViewById(R.id.vod_movie_column_grid_item_title);
		txt.setTextSize(TvApplication.sTvFloatTextSize);
		mBackImageView = (AsyncImageView)findViewById(R.id.vod_movie_column_grid_item_img);
		
		mBackImageView.setImageLoadedListener(this);
		mBackImageView.setImageFailedListener(this);
	}

}

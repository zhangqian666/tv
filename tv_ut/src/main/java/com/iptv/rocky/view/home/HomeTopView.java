package com.iptv.rocky.view.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.HomeTopData;
import com.iptv.rocky.utils.AppCommonUtils;
import com.iptv.rocky.R;

public class HomeTopView extends RelativeLayout {

	private View mLayoutView;
	private TextView mTitle;
	private AsyncImageView mIconImage;
	private AsyncImageView mBackImage;
	private HomeTopData mItemData;
    private ImageView mFloatImage;

    public HomeTopView(Context context) {
		this(context, null, 0);
	}
	
	public HomeTopView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HomeTopView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setChildrenDrawingOrderEnabled(true);
	}
	
	/**
	 * @param data
	 */
	public void initView(HomeTopData data) {
        if (!data.isNotTopPadding) {
            mLayoutView.setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);
        }
        mItemData = data;
        mBackImage.setBackgroundResource(AppCommonUtils.getResourceIdByName(getContext(), data.pageItem.background));
        mItemData.isViewLoaded = true;
        mFloatImage.setBackgroundResource(data.floatResId);
        mTitle.setText(data.pageItem.title);
        mTitle.getPaint().setFakeBoldText(data.isFontBlod);
        mIconImage.setImageUrl(data.pageItem.icon);
    }

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if(i == indexOfChild(mIconImage)){
			return childCount - 1;
		}
		if (i == childCount - 1) {
    		return childCount - 2;
    	}
		return i;
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mLayoutView = findViewById(R.id.home_top_layout);
		mBackImage = (AsyncImageView)findViewById(R.id.home_top_backimg);
		mTitle = (TextView)findViewById(R.id.home_top_title);
		//文字大小和首页的四个标题大小一致
		mTitle.setTextSize(TvApplication.sTvMasterTextSize);
		mIconImage = (AsyncImageView)findViewById(R.id.home_top_icon);
        mFloatImage = (ImageView) findViewById(R.id.home_top_floatimage);
        int padding = (int)(TvApplication.pixelHeight / 32.0);
		mTitle.setPadding(0, padding, padding * 2, 0);
	}
	
}

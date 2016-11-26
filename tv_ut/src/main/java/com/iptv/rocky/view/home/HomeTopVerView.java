package com.iptv.rocky.view.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.data.StoreChannelInfo;
import com.iptv.common.utils.DataReloadUtil;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.hwdata.local.VodHistoryLocalFactory;
import com.iptv.rocky.hwdata.local.VodStoreLocalFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.HomeTopVerData;
import com.iptv.rocky.view.TVItemViewReloadable;
import com.iptv.rocky.view.TextViewDip;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.iptv.rocky.R;

public class HomeTopVerView extends RelativeLayout implements
		AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener, TVItemViewReloadable {

	private View mLayoutView;
	private LinearLayout mTitleLayout;
	private TextViewDip mTitle;
	private TextViewDip mSubTitle;
	private AsyncImageView mIconImage;
	private AsyncImageView mBackImage;
	private HomeTopVerData mItemData;
	private View mCutline;
	private ImageView mFloatImage;

	private Context mContext;

	public HomeTopVerView(Context context) {
		this(context, null, 0);
	}

	public HomeTopVerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HomeTopVerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setChildrenDrawingOrderEnabled(true);
	}

	public void initView(HomeTopVerData data) {
		if (!data.isNotTopPadding) {
			mLayoutView.setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);
		}
		mItemData = data;
		String title = data.pageItem.title;
		mTitle.setText(title);
		// 大小和首页title一样
		mTitle.setTextSize(TvApplication.sTvMasterTextSize);
		mTitle.getPaint().setFakeBoldText(true);
		mIconImage.setImageUrl(data.pageItem.icon);
		float textSize = mTitle.getPaint().getTextSize();
		int padding = (int) textSize;
		int itemPadding = padding / 4;
		mTitle.setPadding(0, itemPadding / 2, 0, 0);
		mSubTitle.setPadding(0, 0, 0, itemPadding / 2);
		mSubTitle.setTextSize(data.subtitleSize);
		mSubTitle.setTextColor(getResources().getColor(data.subtitleColor));
		mSubTitle.getPaint().setFakeBoldText(data.isSubTitleBold);
		mCutline.setBackgroundColor(getResources().getColor(
				data.cutlineBackColor));
		if (mTitle.getText().toString().trim().length() <= 2) {
			LinearLayout.LayoutParams cutlineParams = new LinearLayout.LayoutParams(
					(int) (padding * 4.5), 1);
			cutlineParams.rightMargin = padding / 4;
			mCutline.setLayoutParams(cutlineParams);
		} else if (mTitle.getText().toString().trim().length() >= 4) {
			LinearLayout.LayoutParams cutlineParams = new LinearLayout.LayoutParams(
					(int) (padding * 5.2), 1);
			mCutline.setLayoutParams(cutlineParams);
		}
		if (data.backImage > 0) {
			mBackImage.setBackgroundResource(data.backImage);
		}
		if (data.floatImage > 0) {
			mFloatImage.setBackgroundResource(data.floatImage);
		}

		mItemData.isViewLoaded = true;
		mTitleLayout.setPadding(0, 0, padding, padding * 2);
		int length = title.length();
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTitleLayout.getLayoutParams();
		params.width = (int) (padding * length * 2.1);
		if (length <= 2) {
			params.width = padding * (length * 2);
		} else {
			params.width = padding * (length + 2);
		}
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

		reloadData();
	}

	public void onLoadComplete(String imageUri, View view, Bitmap loadedImage) {
        mItemData.isViewLoaded = true;
    }
	
	@Override
	public void onLoadFailed(String imageUri, View view, FailType failType) {
		mItemData.isViewLoaded = true;
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if (i == indexOfChild(mIconImage)) {
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

		mLayoutView = findViewById(R.id.home_topver_layout);
		mTitleLayout = (LinearLayout) findViewById(R.id.home_topver_titlelayout);
		mBackImage = (AsyncImageView) findViewById(R.id.home_topver_backimg);
		mTitle = (TextViewDip) findViewById(R.id.home_topver_title);
		mSubTitle = (TextViewDip) findViewById(R.id.home_topver_subtitle);
		mIconImage = (AsyncImageView) findViewById(R.id.home_topver_icon);
		mCutline = findViewById(R.id.home_topver_cutline);
		mFloatImage = (ImageView) findViewById(R.id.home_hover_floatimage);

		mBackImage.setImageLoadedListener(this);
		mBackImage.setImageFailedListener(this);
	}

	@Override
	public void reloadData() {
		if (mItemData.id == DataReloadUtil.HomeHistoryViewID) {
			VodHistoryLocalFactory historyFactory = new VodHistoryLocalFactory(mContext);
			HistoryChannelInfo history = historyFactory.findNewestRecord();
			String subTitle = history != null ? history.VODNAME : mContext.getString(R.string.home_notcontent);
			mSubTitle.setText(subTitle);
			mItemData.isViewLoaded = true;
		} else if (mItemData.id == DataReloadUtil.HomeStoreViewID) {
			VodStoreLocalFactory storeFactory = new VodStoreLocalFactory(mContext);
			StoreChannelInfo store = storeFactory.findNewestRecord();
			mSubTitle.setText(String.valueOf(storeFactory.getRecordCount()));
			if (store != null) {
				mItemData.isViewLoaded = false;
				mBackImage.setImageUrl(store.PICPATH);
				mFloatImage.setVisibility(View.VISIBLE);
			} else {
				mItemData.isViewLoaded = true;
				mBackImage.setImageDrawable(getResources().getDrawable(
						R.drawable.home_store_itemview_bg));
				mFloatImage.setVisibility(View.INVISIBLE);
			}
		}
	}
}

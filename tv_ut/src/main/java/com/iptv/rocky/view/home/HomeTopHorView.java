package com.iptv.rocky.view.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.HomeTopHorData;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.TVItemViewReloadable;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class HomeTopHorView extends RelativeLayout 
	implements TVItemViewReloadable {

	private View mLayoutView;
	private LinearLayout mTitleLayout;
	private TextViewDip mTitle;
	private TextViewDip mSubTitle;
	private TextViewDip mNumber;
	private AsyncImageView mIconImage;
	private AsyncImageView mBackImage;
	private HomeTopHorData mItemData;
	private View mCutline;
    private Context mContext;

    public HomeTopHorView(Context context) {
		this(context, null, 0);
	}
	
	public HomeTopHorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HomeTopHorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        mContext = context;
        setChildrenDrawingOrderEnabled(true);
    }
	
	public void initView(HomeTopHorData data) {
		if (!data.isNotTopPadding) {
			mLayoutView.setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);
		}
        float mTitleTextSize = TvApplication.dpiHeight / 28;
        mItemData = data;
		mTitle.setText(data.pageItem.title);
		// 大小和首页title一样
        mTitle.setTextSize(TvApplication.sTvMasterTextSize);
        mTitle.getPaint().setFakeBoldText(true);
        mIconImage.setImageUrl(data.pageItem.icon);
		int padding = (int)(TvApplication.pixelHeight / 32.0);
		int itemPadding = padding / 2;
		mTitle.setPadding(itemPadding, itemPadding / 2, 0, 0);
		mSubTitle.setPadding(itemPadding, 0, 0, itemPadding);
        mSubTitle.setTextSize(mTitleTextSize);
        mSubTitle.getPaint().setFakeBoldText(false);
        mNumber.setPadding(0, 0, itemPadding, (int)(itemPadding * 1.5));
        mNumber.setTextSize((float)(ScreenUtils.getMasterTextSize() * 1.5));
		mTitleLayout.setPadding(padding, 0, 0, padding * 2);
        mSubTitle.setTextColor(getResources().getColor(data.subtitleColor));
        mBackImage.setBackgroundResource(data.backImage);
        mItemData.isViewLoaded = true;

        LinearLayout.LayoutParams cutlineParams = new LinearLayout.LayoutParams(1, (int)(padding * 3.5));
        cutlineParams.topMargin = (int)(itemPadding / 1.5);
        mCutline.setBackgroundColor(mContext.getResources().getColor(R.color.usercenter_cutline));
        mCutline.setLayoutParams(cutlineParams);
        int length = data.pageItem.title.length();
		if (length <= 0) {
			length = 4;
		}
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mTitleLayout.getLayoutParams();
		params.height = (int) (padding * length * 1.6);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

        reloadData();
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
		
		mLayoutView = findViewById(R.id.home_tophor_layout);
		mTitleLayout = (LinearLayout)findViewById(R.id.home_tophor_titlelayout);
		mBackImage = (AsyncImageView)findViewById(R.id.home_tophor_backimg);
		mTitle = (TextViewDip)findViewById(R.id.home_tophor_title);
		mSubTitle = (TextViewDip)findViewById(R.id.home_tophor_subtitle);
		mNumber = (TextViewDip)findViewById(R.id.home_tophor_number);
		mCutline = findViewById(R.id.home_tophor_cutline);
		mIconImage = (AsyncImageView)findViewById(R.id.home_tophor_icon);
	}

    @Override
    public void reloadData() {
//        if (mItemData.id == DataReloadUtil.HomeChaseViewID) {
//            VodChaseFactory chaseFactory = new VodChaseFactory(mContext);
//            mNumber.setText(String.valueOf(chaseFactory.getRecordCount()));
//            ChaseChannelInfo chase = chaseFactory.findNewestRecord();
//            String subTitle = chase != null ? chase.title : mContext.getString(R.string.home_notcontent);
//            mSubTitle.setText(subTitle);
//            mItemData.isViewLoaded = true;
//        }
    }
}

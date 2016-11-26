package com.iptv.rocky.view.special;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.SpecialCategoryObj;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.json.SpecialCategoryFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.SearchView;
import com.iptv.rocky.view.TextViewDip;

public class SpecialCategoryMasterLayout extends RelativeLayout implements ViewPager.OnPageChangeListener {

	private ProgressBar mProgressbar;
//	private SpecialCategoryGallery mGallery;
	private SearchView mSearchView;
	
	private SpecialCategoryViewPager mViewPager;
	private SpecialCategoryFactory mFactory;
	private LinearLayout mLinearLayout;
	private TextViewDip mCurrentPageText, mTotalPageText;
	
	private ArrayList<SpecialCategoryObj> mDatas = new ArrayList<SpecialCategoryObj>(20);
	private int mCurrentPage;
	private int mTotalPage;
	private boolean isLoadCompleted;
	
	private boolean isFirst;
	
	public SpecialCategoryMasterLayout(Context context) {
		this(context, null, 0);
	}
	
	public SpecialCategoryMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SpecialCategoryMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mFactory = new SpecialCategoryFactory();
		mFactory.setHttpEventHandler(new HttpEventHandler<ArrayList<SpecialCategoryObj>>() {
			
			@Override
			public void HttpSucessHandler(ArrayList<SpecialCategoryObj> result) {
				mProgressbar.setVisibility(View.GONE);
				if (result == null || result.size() == 0) {
//					mSearchView.requestFocus();
					isLoadCompleted = true;
					return;
				}
				
				mDatas.addAll(result);
				initPageText();
				mViewPager.createView(mDatas);
			}
			
			@Override
			public void HttpFailHandler() {
				mProgressbar.setVisibility(View.GONE);
				mSearchView.requestFocus();
				TvUtils.processHttpFail(getContext());
			}
		});
	}
	
	public void createView(EnumType.Platform platform,String id) {
//		mGallery.crateView(mProgressbar);
//		mGallery.setSearchView(mSearchView);
		clear();
		loadDatas(platform,id);
	}
	
	public void destory() {
//		mGallery.destory();
		mViewPager.destroy();
		mFactory.cancel();
	}
	
	private void clear() {
		mDatas.clear();
		mCurrentPage = 0;
	}
	
	public void loadDatas(EnumType.Platform platform,String id) {
		if (!isLoadCompleted) {
			isFirst = mCurrentPage == 0;
			mFactory.DownloadDatas(platform,mCurrentPage++,TvApplication.account,TvApplication.language);
		}
	}
	
	public void loadDatas() {
		if (!isLoadCompleted) {
			isFirst = mCurrentPage == 0;
			mFactory.DownloadDatas(mCurrentPage++);
		}
	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
//		mGallery = (SpecialCategoryGallery) findViewById(R.id.specail_category_gallery);
//		requestChildFocus(mGallery, mGallery.findFocus());
		mProgressbar = (ProgressBar) findViewById(R.id.tv_progressbar);
		mViewPager = (SpecialCategoryViewPager) findViewById(R.id.specail_category_viewpage);
		mSearchView = (SearchView)findViewById(R.id.tv_status_search);
		mLinearLayout = (LinearLayout) findViewById(R.id.specail_category_linearLayout);
		mLinearLayout.setAlpha(0f);
		mLinearLayout.setPadding(0, 0, 0, TvApplication.sTvTabItemPadding * 2);
		
		mCurrentPageText = (TextViewDip) findViewById(R.id.specail_category_current_page);
		mTotalPageText = (TextViewDip) findViewById(R.id.specail_category_total_page);
		
		mViewPager.setOnPageChangeListener(this);
	}

	private void initPageText() {
		mLinearLayout.setAlpha(1.0f);
		int size = mDatas.size();
		int pageNumber = Constants.cSpecialCategoryNumber;
		int page = mTotalPage = (size / pageNumber) + (size % pageNumber > 0 ? 1 : 0);
		if (isFirst) {
			mCurrentPageText.setText("1");
		}
		mTotalPageText.setText(String.valueOf(page));
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		mCurrentPageText.setText(String.valueOf(position + 1));
		if (mTotalPage == position + 2) {
			loadDatas();
		}
	}
}

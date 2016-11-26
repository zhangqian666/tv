package com.iptv.rocky.view.vodsearch;

import java.util.ArrayList;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.VodSearchJsonFactory;
import com.iptv.rocky.hwdata.xml.VodSearchFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.vodsearch.SearchAdapter;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TitleView;
import com.iptv.rocky.view.TvHorizontalListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

public class SearchHorizontalListView extends TvHorizontalListView {

	private int mCurrentPage = 0;
	private int mMaxPage = 2;
	
	private VodSearchFactory mSearchFactory;
	private VodSearchJsonFactory mSearchJsonFactory;
	private SearchAdapter mSearchAdapter;
    private ArrayList<VodChannel> mDatas = new ArrayList<VodChannel>(Constants.cRequestIPTVNumber * 2);

    public SearchMasterLayout smLayout;
    public String skey = "";

    private ProgressBar mProgressBar;
    
    private TitleView mPageInfoView;
    
	public SearchHorizontalListView(Context context) {
		this(context, null, 0);
	}

	public SearchHorizontalListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SearchHorizontalListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setPadding(TvApplication.sTvLeftMargin, TvApplication.sTvTabHeight, TvApplication.sTvLeftMargin, 0);
		mIsNotAutoFocus = true;
		
		mSearchFactory = new VodSearchFactory();
		mSearchFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mSearchFactory.setHttpEventHandler(new HttpEventHandler<ArrayList<VodChannel>>() {
			
			@Override
			public void HttpSucessHandler(ArrayList<VodChannel> result) {
				mDatas.addAll(result);
				
				if (mDatas.size() == 0) {
                    smLayout.viewSearchNoResult();
                } else {
                	smLayout.viewSearchResult();
                }
				
                if (mSearchAdapter == null) {
                    mSearchAdapter = new SearchAdapter(getContext(), mDatas);
                    setAdapter(mSearchAdapter);
                } else {
                    mSearchAdapter.notifyDataSetChanged();
                }
                
                mDataChanged = false;
                if (mSearchFactory.counttotal > 0)
				{
					mMaxPage = (int) (mSearchFactory.counttotal / Constants.cRequestIPTVNumber);
					
					if (mSearchFactory.counttotal % Constants.cRequestIPTVNumber > 0)
						mMaxPage++;
				}
                
                loaddingComplete();
                mProgressBar.setVisibility(GONE);
            }

            @Override
			public void HttpFailHandler() {
				loaddingComplete();
				mProgressBar.setVisibility(GONE);
				TvUtils.processHttpFail(getContext());
			}
		});
		
		/**
		 * 适配中兴
		 */
		mSearchJsonFactory = new VodSearchJsonFactory();
		mSearchJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mSearchJsonFactory.setHttpEventHandler(new HttpEventHandler<ArrayList<VodChannel>>() {
			
			@Override
			public void HttpSucessHandler(ArrayList<VodChannel> result) {
				mDatas.addAll(result);
				
				if (mDatas.size() == 0) {
                    smLayout.viewSearchNoResult();
                } else {
                	smLayout.viewSearchResult();
                }
				
                if (mSearchAdapter == null) {
                    mSearchAdapter = new SearchAdapter(getContext(), mDatas);
                    setAdapter(mSearchAdapter);
                } else {
                    mSearchAdapter.notifyDataSetChanged();
                }
                
                mDataChanged = false;
                if (mSearchJsonFactory.COUNTTOTAL > 0)
				{
					mMaxPage = (int) (mSearchJsonFactory.COUNTTOTAL / Constants.cRequestIPTVNumber);
					
					if (mSearchJsonFactory.COUNTTOTAL % Constants.cRequestIPTVNumber > 0)
						mMaxPage++;
				}
                
                loaddingComplete();
                mProgressBar.setVisibility(GONE);
            }

            @Override
			public void HttpFailHandler() {
				loaddingComplete();
				mProgressBar.setVisibility(GONE);
				TvUtils.processHttpFail(getContext());
			}
		});
	}
	
	
	public void setInfo(ProgressBar progressBar, TitleView pageInfo) {
		this.mProgressBar = progressBar;
		this.mPageInfoView = pageInfo;
	}
	
	public void DownloadDatas() {
		clear();
		loadDatas();
	}
	
	private void clear() {
		mDatas.clear();
//		resetList();
//		invokeOnItemScrollListener();
		mCurrentPage = 0;
		mMaxPage = 2;
		mDataChanged = true;
		
		mPageInfoView.setText("");
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		mProgressBar.setVisibility(GONE);
		mSearchFactory.cancel();
		mSearchJsonFactory.cancel();
	}
	
	@Override
	public void requestChildFocus(View child, View focused) {
		super.requestChildFocus(child, focused);
		
		//mPageInfoView.setText((mSelectedPosition / Constants.cPageNumber + 1) + " / " + mTotalPage);
		if (TvApplication.platform==EnumType.Platform.ZTE) {
			 mPageInfoView.setText((mSelectedPosition + 1) + " / " + mSearchJsonFactory.COUNTTOTAL);
		}else{
			mPageInfoView.setText((mSelectedPosition + 1) + " / " + mSearchFactory.counttotal);
		}
	}

	@Override
	protected void loadDatas() {
		if (++mCurrentPage <= mMaxPage) {
			if (TvApplication.platform==EnumType.Platform.ZTE) {
				mSearchJsonFactory.DownloadDatas("ZTE",skey, Constants.cRequestIPTVNumber*2,mCurrentPage);
			}else{
				mSearchFactory.DownloadDatas(skey, Constants.cRequestIPTVNumber, (mCurrentPage-1) * Constants.cRequestIPTVNumber);
			}
			mProgressBar.setVisibility(VISIBLE);
		}
	}

}

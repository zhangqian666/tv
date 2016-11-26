package com.iptv.rocky.view.special;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.SpecialCategoryObj;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.hwdata.json.SpecialCategoryFactory;
import com.iptv.rocky.model.special.SpecialCategoryAdapter;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.SearchView;
import com.iptv.rocky.view.TvGallery;

/**
 * 二级专题页面 分类页
 * */
public class SpecialCategoryGallery extends TvGallery {
	
	private View mProgressView;
	private SpecialCategoryFactory mHttpFactory;
	
	private boolean isFirst;
	private SpecialCategoryAdapter mSpecialAdapter;
	private int mCurrentPage;
	private ArrayList<SpecialCategoryObj> mDatas = new ArrayList<SpecialCategoryObj>(20);
	
	private SearchView mSearchView;
	
	public void setSearchView(SearchView searchView) {
		this.mSearchView = searchView;
	}
	
	public SpecialCategoryGallery(Context context) {
		this(context, null, 0);
	}
	
	public SpecialCategoryGallery(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SpecialCategoryGallery(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
//		setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				SpecialCategoryMetroView selected = (SpecialCategoryMetroView)view;
//				selected.stopMarquee();
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//			}
//		});
		
		mHttpFactory = new SpecialCategoryFactory();
		mHttpFactory.setHttpEventHandler(new HttpEventHandler<ArrayList<SpecialCategoryObj>>() {
			
			@Override
			public void HttpSucessHandler(ArrayList<SpecialCategoryObj> result) {
				mProgressView.setVisibility(View.GONE);
				
				if (result.size() == 0) {
					mSearchView.requestFocus();
					return;
				}
				mDatas.addAll(result);
				
				int pageNumber = Constants.cSpecialCategoryNumber;
				int size = mDatas.size();
				int page = (size / pageNumber) + (size % pageNumber > 0 ? 1 : 0);
				
				ArrayList<SpecialCategoryMetroView> metroViews = new ArrayList<SpecialCategoryMetroView>(page);
				for (int i = 0; i < page; i++) {
					SpecialCategoryMetroView metroView = new SpecialCategoryMetroView(getContext());
					if (i == 0 && isFirst) {
						metroView.isAutoFocus = true;
					}
					metroViews.add(metroView);
				}
				
				if (mSpecialAdapter == null) {
					mSpecialAdapter = new SpecialCategoryAdapter(mDatas, metroViews);
					setAdapter(mSpecialAdapter);
				} else {
					mSpecialAdapter.setmDatas(mDatas);
					mSpecialAdapter.setmViews(metroViews);
					mSpecialAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void HttpFailHandler() {
				mProgressView.setVisibility(View.GONE);
				mSearchView.requestFocus();
				TvUtils.processHttpFail(getContext());
			}
		});
	}
	
	public void crateView(View progressView) {
		mProgressView = progressView;
		clear();
		loadDatas();
	}
	
	private void clear() {
		mDatas.clear();
		mCurrentPage = 0;
	}
	
	public void destory(){
		mHttpFactory.cancel();
	}
	
	private void loadDatas() {
		isFirst = (mCurrentPage == 0);
		mHttpFactory.DownloadDatas(mCurrentPage);
	}
}

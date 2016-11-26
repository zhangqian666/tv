package com.iptv.rocky.view.movie;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.VodChannelJsonFactory;
import com.iptv.rocky.hwdata.xml.VodChannelFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.movie.ListAdapter;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TitleView;
import com.iptv.rocky.view.TvHorizontalListView;

public class ListHorizontalListView extends TvHorizontalListView {

	private String mEpgId;
	private int mCurrentPage = 0;
	private int mMaxPage = 2;
	private EnumType.Platform platform;
	
//	private int mTotalPage = 0;
	
	private VodChannelFactory mListFactory;
	private VodChannelJsonFactory vodChannelJsonFactory;
	private ListAdapter mListAdapter;
	private ProgressBar mProgressBar;
	private TitleView mPageInfoView;

	private ArrayList<VodChannel> mDatas = new ArrayList<VodChannel>();
	
	public ListHorizontalListView(Context context) {
		this(context, null, 0);
	}
	
	public ListHorizontalListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ListHorizontalListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		//华为平台
		mListFactory = new VodChannelFactory();
		mListFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mListFactory.setHttpEventHandler(new HttpEventHandler<ArrayList<VodChannel>>() {
			
			@Override
			public void HttpSucessHandler(ArrayList<VodChannel> result) {
				
				/*for(VodChannel item :result){
					LogUtils.info(""+item.VODID +" " + item.VODNAME +"   "+item.PICPATH);
				}*/
				
				mDatas.addAll(result);
				
				if (mListAdapter == null) {
					mListAdapter = new ListAdapter(getContext(), mDatas,mEpgId);
					setAdapter(mListAdapter);
				} else {
					mListAdapter.notifyDataSetChanged();
				}
				
				mDataChanged = false;
				if (mListFactory.counttotal > 0)
				{
					mMaxPage = (int) (mListFactory.counttotal / Constants.cRequestIPTVNumber);
					
					if (mListFactory.counttotal % Constants.cRequestIPTVNumber > 0)
						mMaxPage++;
					
//					mTotalPage = (int) (mListFactory.counttotal / Constants.cPageNumber);
//					
//					if (mListFactory.counttotal % Constants.cPageNumber > 0)
//						mTotalPage++;
				}
				loaddingComplete();
				mProgressBar.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void HttpFailHandler() {
				loaddingComplete();
				mProgressBar.setVisibility(View.INVISIBLE);
				TvUtils.processHttpFail(getContext());
			}
		});
		//中兴平台
		vodChannelJsonFactory = new VodChannelJsonFactory();
		vodChannelJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		vodChannelJsonFactory.setHttpEventHandler(new HttpEventHandler<ArrayList<VodChannel>>() {
			
			@Override
			public void HttpSucessHandler(ArrayList<VodChannel> result) {
				
				/*for(VodChannel item :result){
					LogUtils.info(""+item.VODID +" " + item.VODNAME +"   "+item.PICPATH);
				}*/
				
				mDatas.addAll(result);
				
				if (mListAdapter == null) {
					mListAdapter = new ListAdapter(getContext(), mDatas,mEpgId);
					setAdapter(mListAdapter);
					mListAdapter.notifyDataSetChanged();
				} else {
					
				}
				
				mDataChanged = false;
				LogUtils.info("counttotal-->2:  "+vodChannelJsonFactory.counttotal);
				if (vodChannelJsonFactory.counttotal > 0)
				{
					mMaxPage = (int) (vodChannelJsonFactory.counttotal / Constants.cRequestIPTVNumber);
					LogUtils.info("mMaxPage-->3:  "+mMaxPage);
					if (vodChannelJsonFactory.counttotal % Constants.cRequestIPTVNumber > 0)
						mMaxPage++;
					
//					mTotalPage = (int) (mListFactory.counttotal / Constants.cPageNumber);
//					
//					if (mListFactory.counttotal % Constants.cPageNumber > 0)
//						mTotalPage++;
				}
				loaddingComplete();
				mProgressBar.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void HttpFailHandler() {
				loaddingComplete();
				mProgressBar.setVisibility(View.INVISIBLE);
				TvUtils.processHttpFail(getContext());
			}
		});
	}
	
	public void setInfo(ProgressBar progressBar, TitleView pageInfo) {
		this.mProgressBar = progressBar;
		this.mPageInfoView = pageInfo;
	}
	
	public void createView(EnumType.Platform platform,String id) {
		this.mEpgId = id;
		this.platform = platform; 
		clear();
		loadDatas(); 
	}
	
	private void clear() {
		mDatas.clear();
		mCurrentPage = 0;
		mMaxPage = 2;
		mDataChanged = true;
	}

	@Override
	public void destroy() {
		super.destroy();
		
		mProgressBar.setVisibility(GONE);
		mListFactory.cancel();
		vodChannelJsonFactory.cancel();
	}
	
	@Override
	public void requestChildFocus(View child, View focused) {
		super.requestChildFocus(child, focused);
		
		//mPageInfoView.setText((mSelectedPosition / Constants.cPageNumber + 1) + " / " + mTotalPage);
		if (TvApplication.platform==EnumType.Platform.ZTE) {
			mPageInfoView.setText((mSelectedPosition + 1) + " / " + vodChannelJsonFactory.counttotal);
			LogUtils.info("mSelectedPosition   "+mSelectedPosition+" vodChannelJsonFactory.counttotal "+vodChannelJsonFactory.counttotal);
		}
		else{
			mPageInfoView.setText((mSelectedPosition + 1) + " / " + mListFactory.counttotal);
		}
	}

	@Override
	protected void loadDatas() {
		
		if (++mCurrentPage <= mMaxPage) {
			if (TvApplication.platform==EnumType.Platform.ZTE) {
				vodChannelJsonFactory.DownloadDatas(this.platform,  mEpgId, mCurrentPage, Constants.cRequestIPTVNumber*3);//mCurrentPage,Constants.cRequestIPTVNumber);
			}else{
				mListFactory.DownloadDatas(this.platform,  mEpgId, Constants.cRequestIPTVNumber, (mCurrentPage-1) * Constants.cRequestIPTVNumber);
			}
		}
	}
}

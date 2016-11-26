package com.iptv.rocky.view.vodmoviecolumn;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.VodChannelJsonFactory;
import com.iptv.rocky.hwdata.xml.VodChannelFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TitleView;
import com.iptv.rocky.view.vodmoviecolumn.VodMovieColumnGridView.CallBack;
import com.iptv.rocky.R;

public class VodMovieColumnMasterLayout extends RelativeLayout {
	
	private Context mContext;
	
	private VodMovieColumnGridView mGridView;
	private ProgressBar mProgressBar;
	private TitleView mTitle;
	
	private String mEpgId;
	private int mCurrentPage = 0;
	private int mMaxPage = 2;
	private String platform;
	private String mColumnCode;
	
	private ArrayList<VodChannel> gridViewdata = new ArrayList<VodChannel>(Constants.cRequestIPTVNumber * 2);
	private VodChannelFactory mListFactory;
	private VodChannelJsonFactory mVodChannelJsonFactory;
	
	public VodMovieColumnMasterLayout(Context context) {
		this(context, null);
	}

	public VodMovieColumnMasterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		
		mListFactory = new VodChannelFactory();
		mListFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mListFactory.setHttpEventHandler(new HttpEventHandler<ArrayList<VodChannel>>() {
			
			@Override
			public void HttpSucessHandler(ArrayList<VodChannel> result) {	
				gridViewdata.addAll(result);
				mGridView.createView(gridViewdata,mColumnCode);
				if (mListFactory.counttotal > 0){
					mMaxPage = (int) (mListFactory.counttotal / Constants.cRequestIPTVNumber);
					if (mListFactory.counttotal % Constants.cRequestIPTVNumber > 0)
						mMaxPage++;
				}
				
				mGridView.loaddingComplete();
				mProgressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void HttpFailHandler() {				
				mGridView.loaddingComplete();
				mProgressBar.setVisibility(View.GONE);
				TvUtils.processHttpFail(mContext);
			}
		});
		
		mVodChannelJsonFactory=new VodChannelJsonFactory();
		mVodChannelJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mVodChannelJsonFactory.setHttpEventHandler(new HttpEventHandler<ArrayList<VodChannel>>() {
			
			@Override
			public void HttpSucessHandler(ArrayList<VodChannel> result) {	
				gridViewdata.addAll(result);
				mGridView.createView(gridViewdata,mEpgId);
				LogUtils.info("counttotal-->3:  "+mVodChannelJsonFactory.counttotal);
				if (mVodChannelJsonFactory.counttotal > 0){
					mMaxPage = (int) (mVodChannelJsonFactory.counttotal / Constants.cRequestIPTVNumber);
					if (mVodChannelJsonFactory.counttotal % Constants.cRequestIPTVNumber > 0)
						mMaxPage++;
				}
				
				mGridView.loaddingComplete();
				mProgressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void HttpFailHandler() {				
				mGridView.loaddingComplete();
				mProgressBar.setVisibility(View.GONE);
				TvUtils.processHttpFail(mContext);
			}
		});
	}
	
	public void createView(String epg_id, String name,String platform,String columnCode) {
		mEpgId = epg_id;
		mTitle.setText(name);
		mColumnCode=columnCode;
		this.platform = platform;
		loadFirst();
	}
	
	public void destroy() {
		mVodChannelJsonFactory.cancel();
	}
	
	public void load() {
		if (++mCurrentPage <= mMaxPage) {
			if (TvApplication.platform==EnumType.Platform.ZTE) {
				mVodChannelJsonFactory.DownloadDatas(this.platform,  mEpgId, mCurrentPage, Constants.cRequestIPTVNumber*3);
			}else{
				mListFactory.DownloadDatas(platform, mEpgId, Constants.cRequestIPTVNumber, (mCurrentPage-1) * Constants.cRequestIPTVNumber);
			}
			
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}
	
	public void loadFirst() {
		clear();
		load();
	}
	
	private void clear() {
		gridViewdata.clear();
		mCurrentPage = 0;
		mMaxPage = 2;
	}
	
	private CallBack callback = new CallBack() {
		
		@Override
		public void loadDatas() {
			load();
		}
	};
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mProgressBar = (ProgressBar) findViewById(R.id.tv_progressbar);
		
		mGridView = (VodMovieColumnGridView) findViewById(R.id.vod_movie_column_gridview);
		mGridView.setColumnWidth((int)(ScreenUtils.getChannelUnit() * 1.2));
		mGridView.setRowHeight((int)(ScreenUtils.getChannelUnit() * 0.8));
		
		mGridView.setCallback(callback);
		
		mTitle = (TitleView) findViewById(R.id.vod_movie_column_title);
		
		requestChildFocus(mGridView, mGridView);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		boolean handled = super.dispatchKeyEvent(event);
		if (!handled) {
			return mGridView.dispatchKeyEvent(event);
		}
		return handled;
	}
	
}

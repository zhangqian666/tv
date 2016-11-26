package com.iptv.rocky.view.recchan;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.RecChan;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.RecChanJsonFactory;
import com.iptv.rocky.hwdata.xml.RecChanFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.recchan.ListAdapter;
import com.iptv.rocky.model.recchan.ListItemData;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.R;

public class RecChanListView extends RelativeLayout {

	public ListView mListView;
    
    private ListAdapter mListAdapter;
    
    private RecChanFactory mRecChanFactory;
    
    private RecChanJsonFactory mRecChanJsonFactory;
    
    private ProgressBar mProgressBar;
    
    private ArrayList<RecChan> mDatas = new ArrayList<RecChan>();
    
    private View mCurSelectView;
    
    private OnListChangeListener onListChangeListener;
	
	public void setOnListChangeListener(OnListChangeListener onListChangeListener) {
		this.onListChangeListener = onListChangeListener;
	}
    
	public interface OnListChangeListener {
		public abstract void notifyDataSetChanged(RecChan recChan);
	}
	
	public RecChanListView(Context context) {
		this(context, null, 0);
	}

	public RecChanListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RecChanListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setPadding(TvApplication.sTvLeftMargin, TvApplication.sTvTabHeight, 0, ScreenUtils.getRecChanListItemHeight());
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ScreenUtils.getLiveItemWidth(), RelativeLayout.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		
		mRecChanFactory = new RecChanFactory();
		mRecChanFactory.setHttpEventHandler(RecChanHandler);
		mRecChanFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		
		mRecChanJsonFactory = new RecChanJsonFactory();
		mRecChanJsonFactory.setHttpEventHandler(recChanJsonHandler);
		mRecChanJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mListView = (ListView) findViewById(R.id.recchan_list);
		//mListView.setItemsCanFocus(true);
		mListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// 失去焦点时，继续保持以前的状态
				if (!hasFocus) {
//					((ListItemData) mCurSelectView.getTag()).setViewNotFocus();
//				} else {
					((ListItemData) mCurSelectView.getTag()).setViewEnlarge();
				}
			}
		});
		
		mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				if (mCurSelectView != null)
					((ListItemData) mCurSelectView.getTag()).setViewNormal();
				mCurSelectView = view;
				((ListItemData) mCurSelectView.getTag()).setViewEnlarge();
				
				if (view.getTag() instanceof ListItemData)
				{
					onListChangeListener.notifyDataSetChanged(((ListItemData)view.getTag()).mRecChan);
				}
				/*LogUtils.error("开始做滚动位置判断:position:"+position +"  id:"+id);
				if(position>0){
					if (position%10==0) {
						int height = 0;
						for (int i = 0; i < position; i++) {
							View item = mListAdapter.getView(i, null, mListView);
							LogUtils.error("measure之前:"+item.getMeasuredHeight());
							item.measure(0, 0);
							LogUtils.error("measure之后:"+item.getMeasuredHeight());
							height = item.getMeasuredHeight();
						}
						//mListView.scrollTo(0, height);
						//mListView.smoothScrollToPositionFromTop(11, 1, 2000);
						mListView.smoothScrollToPositionFromTop(11, 0);
					}
				}
				
				LogUtils.error("结束位置调整判断");*/
			}
		
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		
	}
	
	public void destroy() {
		if (TvApplication.platform==EnumType.Platform.ZTE) {
			mRecChanJsonFactory.cancel();
		}else{
			mRecChanFactory.cancel();
			
		}
	}
	
	private HttpEventHandler<ArrayList<RecChan>> RecChanHandler = 
			new HttpEventHandler<ArrayList<RecChan>>() {
		
		@Override
		public void HttpSucessHandler(ArrayList<RecChan> result) {
			mDatas.addAll(result);
			
			if (mListAdapter == null) {
				mListAdapter = new ListAdapter(getContext(), mDatas);
				mListView.setAdapter(mListAdapter);
			} else {
				mListAdapter.notifyDataSetChanged();
			}
			
			if (mListView.getChildCount() > 0)
				requestChildFocus(mListView, mListView.findFocus());
			
			mProgressBar.setVisibility(View.GONE);
		}
		
		@Override
		public void HttpFailHandler() {
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};
	
	private HttpEventHandler<ArrayList<RecChan>> recChanJsonHandler = 
			new HttpEventHandler<ArrayList<RecChan>>() {
		
		@Override
		public void HttpSucessHandler(ArrayList<RecChan> result) {
			mDatas.addAll(result);
			
			if (mListAdapter == null) {
				mListAdapter = new ListAdapter(getContext(), mDatas);
				mListView.setAdapter(mListAdapter);
			} else {
				mListAdapter.notifyDataSetChanged();
			}
			
			if (mListView.getChildCount() > 0)
				requestChildFocus(mListView, mListView.findFocus());
			
			mProgressBar.setVisibility(View.GONE);
		}
		
		@Override
		public void HttpFailHandler() {
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};
	
	
	
	public void setProgressBar(ProgressBar progressBar) {
		this.mProgressBar = progressBar;
	}
	
	public void createView() {
		if (TvApplication.platform==EnumType.Platform.ZTE) {
			mRecChanJsonFactory.DownloadDatas();
		}else{
			mRecChanFactory.DownloadDatas();
		}
	}
	
}

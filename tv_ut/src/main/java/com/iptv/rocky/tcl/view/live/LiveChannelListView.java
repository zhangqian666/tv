package com.iptv.rocky.tcl.view.live;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.tcl.model.live.ListAdapter;
import com.iptv.rocky.tcl.model.live.ListItemData;

public class LiveChannelListView extends RelativeLayout {

	public ListView mListView;
    
    private ListAdapter mListAdapter;
    
    private ArrayList<LiveChannelBill> mDatas = new ArrayList<LiveChannelBill>();
    
    private View mCurSelectView;
    
    private Context mContext;
    

    
    // 正在播放的用户自定义频道号
    private String currentPlayUserChannelId;
    // 正在播放的频道分类
    private int currentPlayLiveType;
    
    private OnListChangeListener onListChangeListener;
	
	public void setOnListChangeListener(OnListChangeListener onListChangeListener) {
		this.onListChangeListener = onListChangeListener;
	}
    
	public interface OnListChangeListener {
		public abstract void notifyDataSetChanged();
	}
    
	public LiveChannelListView(Context context) {
		this(context, null, 0);
	}

	public LiveChannelListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LiveChannelListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mContext = context;
		
//		setPadding(TvApplication.sTvLeftMargin, TvApplication.sTvTabHeight, 0, ScreenUtils.getRecChanListItemHeight());
//		
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//				ScreenUtils.getLiveItemWidth(), RelativeLayout.LayoutParams.MATCH_PARENT);
//		setLayoutParams(params);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mListView = (ListView) findViewById(R.id.live_channel_list);
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
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (mCurSelectView != null)
					((ListItemData) mCurSelectView.getTag()).setViewNormal();
				mCurSelectView = view;
				((ListItemData) mCurSelectView.getTag()).setViewEnlarge();
				
				if (view.getTag() instanceof ListItemData)
				{
					if (onListChangeListener != null)
						onListChangeListener.notifyDataSetChanged();
				}
			}
		
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	
	public void destroy() {
	}
	
	public void updateLiveChannelList(ArrayList<LiveChannelBill> lstLiveChannelBill, int currentPlayLiveType, String currentPlayUserChannelId) {
		mDatas.clear();
		mDatas.addAll(lstLiveChannelBill);
		
		if (mListAdapter == null) {
			mListAdapter = new ListAdapter(mContext, mDatas);
			mListView.setAdapter(mListAdapter);
			LogUtils.d("LiveChannelList","初始化：currentPlayLiveType："+currentPlayLiveType+";currentPlayUserChannelId:"+currentPlayUserChannelId);
			mListAdapter.setCurrentPlayLiveType(currentPlayLiveType);
			mListAdapter.setCurrentPlayUserChannelId(currentPlayUserChannelId);
		
			mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, 
						int position, long id) {
					view.performClick();
				}
			});
		} else {
			mListAdapter.setCurrentPlayLiveType(currentPlayLiveType);
			mListAdapter.setCurrentPlayUserChannelId(currentPlayUserChannelId);
			mListAdapter.notifyDataSetChanged();
		}
	}
	


	public String getCurrentPlayUserChannelId() {
		return currentPlayUserChannelId;
	}

	public void setCurrentPlayUserChannelId(String currentPlayUserChannelId) {
		this.currentPlayUserChannelId = currentPlayUserChannelId;
		mListAdapter.setCurrentPlayUserChannelId(currentPlayUserChannelId);
		
	}

	public int getCurrentPlayLiveType() {
		return currentPlayLiveType;
	}

	public void setCurrentPlayLiveType(int currentPlayLiveType) {
		this.currentPlayLiveType = currentPlayLiveType;
		mListAdapter.setCurrentPlayLiveType(currentPlayLiveType);
	}
	
}

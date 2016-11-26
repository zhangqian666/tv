package com.iptv.rocky.tcl.view.live;

import java.util.ArrayList;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.data.PortalLiveType;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.json.AllChannelProgBilJsonlFactory;
import com.iptv.rocky.hwdata.local.LiveTypeLocalFactory;
import com.iptv.rocky.hwdata.xml.AllChannelProgBillFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.TextViewDip;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 左侧频道列表组件
 *
 */
public class LiveChooseLayout extends RelativeLayout implements LiveChannelListView.OnListChangeListener {
	
	private TextViewDip mLiveTypeName;
	
    private LiveChannelListView mLiveChannelListView;
    
    private ArrayList<PortalLiveType> mLstLiveType;
    
    // 正在播放的用户自定义频道号
//    public int currentPlayUserChannelId;
    public String currentPlayUserChannelId;
    // 正在播放的频道分类
    public int currentPlayLiveType;
    
    private OnListChangeListener onListChangeListener;
	
	public void setOnListChangeListener(OnListChangeListener onListChangeListener) {
		this.onListChangeListener = onListChangeListener;
	}
    
	public interface OnListChangeListener {
		public abstract void notifyDataSetChanged();
	}
	
	public LiveChooseLayout(Context context) {
		this(context, null, 0);
	}

	public LiveChooseLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LiveChooseLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setPadding(TvApplication.sTvLeftMargin / 4, TvApplication.sTvTabHeight / 4, 0, 10);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ScreenUtils.getLiveItemWidth(), RelativeLayout.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		
		LiveTypeLocalFactory liveTypeLocalFactory = new LiveTypeLocalFactory(getContext());
		mLstLiveType = liveTypeLocalFactory.getLiveType();
    }

	public void destroy() {
		mLiveChannelListView.destroy();
	}
	
	@Override
	protected void onFinishInflate() {
        super.onFinishInflate();
        
        mLiveTypeName = (TextViewDip) findViewById(R.id.live_type_name);
        mLiveTypeName.setTextSize(TvApplication.sTvSelectorTextSize+2);
        
		mLiveChannelListView = (LiveChannelListView) findViewById(R.id.list_view);
		mLiveChannelListView.setOnListChangeListener(this);
    }
	
	public void updateListByTypeId(int nTypeId,int currentPlayLiveType,String currentPlayUserChannelId) {
		if(mLstLiveType.size()>0){
			mLiveTypeName.setText(mLstLiveType.get(nTypeId-1).title);
		}
		
		this.currentPlayLiveType = currentPlayLiveType;
		this.currentPlayUserChannelId = currentPlayUserChannelId;
//		if(TvApplication.platform==EnumType.Platform.ZTE){
//			
//			mLiveChannelListView.updateLiveChannelList(getLiveChannelByTypeIdZte(nTypeId), currentPlayLiveType, currentPlayUserChannelId);
//		}else{
			mLiveChannelListView.updateLiveChannelList(getLiveChannelByTypeId(nTypeId), currentPlayLiveType, currentPlayUserChannelId);
//		}
	}
	
	public void updateListByChannelId(String nChannelId) {
		for (int i = 0; i < mLstLiveType.size(); i++) {
			for (int j = 0; j < mLstLiveType.get(i).lstChannelIds.size(); j++) {
				if (nChannelId.equals(mLstLiveType.get(i).lstChannelIds.get(j))) {
					mLiveTypeName.setText(mLstLiveType.get(i).title);
					mLiveChannelListView.updateLiveChannelList(getLiveChannelByChannelId(nChannelId),currentPlayLiveType,currentPlayUserChannelId);
					return;
				}
			}
		}
	}

	private ArrayList<LiveChannelBill> getLiveChannelByChannelId(String nChannelId) {
		ArrayList<LiveChannelBill> result = new ArrayList<LiveChannelBill>();
		if(TvApplication.platform==EnumType.Platform.ZTE){
			if (AllChannelProgBilJsonlFactory.mapLiveChannelBillZte.containsKey(nChannelId)) {
				result.add(AllChannelProgBilJsonlFactory.mapLiveChannelBillZte.get(nChannelId));
			}
		}else{
			if (AllChannelProgBillFactory.mapLiveChannelBill.containsKey(nChannelId)) {
				result.add(AllChannelProgBillFactory.mapLiveChannelBill.get(nChannelId));
			}
		}
		return result;
	}
	

	
	private ArrayList<LiveChannelBill> getLiveChannelByTypeId(int nTypeId) {
		ArrayList<LiveChannelBill> result = new ArrayList<LiveChannelBill>();
		for (PortalLiveType liveType : mLstLiveType)
		{
			if (liveType.typeId == nTypeId)
			{
				for (String channelid : liveType.lstChannelIds)
				{
					if (TvApplication.platform==EnumType.Platform.ZTE) {
						if (AllChannelProgBilJsonlFactory.mapLiveChannelBillZte.containsKey(channelid)) {
							result.add(AllChannelProgBilJsonlFactory.mapLiveChannelBillZte.get(channelid));
						}
					}else{
						if(AllChannelProgBillFactory.mapLiveChannelBill != null){
							if (AllChannelProgBillFactory.mapLiveChannelBill.containsKey(channelid)) {
								result.add(AllChannelProgBillFactory.mapLiveChannelBill.get(channelid));
							}
						}
					}
				}
				break;
			}
		}
		return result;
	}
	
	@Override
	public void notifyDataSetChanged() {
		if (onListChangeListener != null)
			onListChangeListener.notifyDataSetChanged();
	}
	
}

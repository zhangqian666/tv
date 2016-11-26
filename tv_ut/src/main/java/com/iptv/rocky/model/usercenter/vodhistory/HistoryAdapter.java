package com.iptv.rocky.model.usercenter.vodhistory;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.VodChannelDetailActivity;
import com.iptv.rocky.hwdata.local.VodHistoryLocalFactory;
import com.iptv.rocky.view.usercenter.vodhistory.HistoryHorizontalListView;
import com.iptv.rocky.view.usercenter.vodhistory.HistoryItemView;
import com.iptv.rocky.view.usercenter.vodhistory.HistoryMasterLayout;

public class HistoryAdapter extends BaseAdapter implements
		HistoryItemView.ItemClickInterface {

	public boolean isEdit = false;
	
	private Context mContext;
	private ArrayList<HistoryChannelInfo> mDatas;
	private HistoryHorizontalListView mListView;
	private VodHistoryLocalFactory mVodFacotry;

	public ArrayList<ImageView> mEditViews = new ArrayList<ImageView>();

	public HistoryAdapter(Context context, ArrayList<HistoryChannelInfo> datas,
			HistoryHorizontalListView listView) {
		
		mContext = context;
		mDatas = datas;
		mListView = listView;
		
		mVodFacotry = new VodHistoryLocalFactory(context);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HistoryItemData holder = null;
		if (convertView == null) {
			holder = new HistoryItemData();
			convertView = holder.getView(mContext);
			convertView.setTag(holder);
			mEditViews.add(holder.editView);
			holder.mContentView.setItemClickListener(this);
		} else {
			holder = (HistoryItemData) convertView.getTag();
			holder.destroy();
		}
		HistoryChannelInfo data = mDatas.get(position);
		holder.setView(data, isEdit);

		return convertView;
	}

	public void clearHistory() {
		this.mDatas.clear();
		isEdit = false;
	}

	@Override
	public void onItemClick() {
		if (isEdit) {
			int index = mListView.getSelectedItemPosition();
			mVodFacotry.deleteByChannelId(mDatas.get(index).channelid);
			mDatas.remove(index);
			mListView.setIsChanged(true);
			this.notifyDataSetChanged();
            if (getCount() == 0) {
                isEdit = false;
                HistoryMasterLayout historyMasterLayout = (HistoryMasterLayout) mListView.getParent();
                historyMasterLayout.showEmptyView();
            }
		} else {
			HistoryChannelInfo historyChannelInfo = (HistoryChannelInfo) mListView.getSelectedItem();
			Intent intent = new Intent(mContext, VodChannelDetailActivity.class);
            intent.putExtra(Constants.cDetailIdExtra, historyChannelInfo.channelid);
            intent.putExtra(Constants. cPlatformExtra , historyChannelInfo. platform .toString());
            intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, historyChannelInfo.columncode);
            intent.putExtra(Constants. CDETAIL_CONTENTCODE_EXTRA , historyChannelInfo.CONTENTCODE);
            mContext.startActivity(intent);
			
//			Intent intent = new Intent(mContext, TVPlayerActivity.class);
//			TVPlayerParams params = new TVPlayerParams();
//
//			HistoryChannelInfo hisChanInfo = (HistoryChannelInfo) mListView.getSelectedItem();
//			long chanId = hisChanInfo.channelid;
//			long vId = hisChanInfo.VODID;
//			params.channelId = String.valueOf(chanId);
//			params.videoId = String.valueOf(vId);
//
//			intent.putExtra(TVPlayerActivity.EXTRA_PARAMS, params);
//			mContext.startActivity(intent);
		}
	}
}

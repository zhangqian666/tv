package com.iptv.rocky.model.usercenter.vodstore;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.iptv.common.data.StoreChannelInfo;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.VodChannelDetailActivity;
import com.iptv.rocky.hwdata.local.VodStoreLocalFactory;
import com.iptv.rocky.view.usercenter.vodstore.StoreHorizontalListView;
import com.iptv.rocky.view.usercenter.vodstore.StoreItemView;
import com.iptv.rocky.view.usercenter.vodstore.StoreMasterLayout;

public class StoreAdapter extends BaseAdapter implements StoreItemView.ItemClickInterface{

	private Context mContext;
	private ArrayList<StoreChannelInfo> mDatas;
    private StoreHorizontalListView mListView;
    
    public ArrayList<ImageView> mEditViews = new ArrayList<ImageView>();

    public boolean isEdit = false;

    public StoreAdapter(Context context, ArrayList<StoreChannelInfo> datas, StoreHorizontalListView listView) {
        this.mContext = context;
        this.mDatas = datas;
        this.mListView = listView;
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
        StoreItemData holder = null;
        if (convertView == null) {
            holder = new StoreItemData();
            convertView = holder.getView(mContext);
            convertView.setTag(holder);
            mEditViews.add(holder.editView);
            holder.mContentView.setItemClickListener(this);
        } else {
            holder = (StoreItemData) convertView.getTag();
            holder.destroy();
        }
        StoreChannelInfo data = mDatas.get(position);
        holder.setView(data, isEdit);

        return convertView;
    }
    public void clearStore() {
        this.mDatas.clear();
        isEdit = false;
    }

    @Override
    public void onItemClick() {
        if (isEdit) {
            new VodStoreLocalFactory(mContext).deleteByChannelId(mDatas.get(mListView.getSelectedItemPosition()).VODID);
            mDatas.remove(mListView.getSelectedItemPosition());
            mListView.setIsChanged(true);
            this.notifyDataSetChanged();
            if (getCount() == 0) {
                isEdit = false;
                StoreMasterLayout storeMasterLayout = (StoreMasterLayout)mListView.getParent();
                storeMasterLayout.showEmptyView();
            }
        } else {
            Intent intent = new Intent(mContext, VodChannelDetailActivity.class);
            intent.putExtra(Constants.cDetailIdExtra, ((StoreChannelInfo) mListView.getSelectedItem()).VODID);
            intent.putExtra(Constants.cPlatformExtra, ((StoreChannelInfo) mListView.getSelectedItem()).platform.toString());
            mContext.startActivity(intent);
        }
    }
}

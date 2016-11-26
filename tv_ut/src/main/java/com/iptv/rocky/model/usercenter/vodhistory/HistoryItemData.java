package com.iptv.rocky.model.usercenter.vodhistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.rocky.base.BaseListItemData;
import com.iptv.rocky.view.usercenter.vodhistory.HistoryItemView;
import com.iptv.rocky.R;

public class HistoryItemData extends BaseListItemData {

    public HistoryChannelInfo mVodInfo;

    public HistoryItemView mContentView;

    public ImageView editView;

    public void setView(HistoryChannelInfo vodInfo, boolean isEdit) {
        mVodInfo = vodInfo;
        mContentView.initView(vodInfo);
        
        if (isEdit) {
            editView.setVisibility(View.VISIBLE);
        } else {
            editView.setVisibility(View.GONE);
        }
    }

    public void destroy() {
        mContentView.destroy();
    }

    @Override
    public View getView(Context context) {
        if (mContentView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mContentView = (HistoryItemView) inflater.inflate(R.layout.vod_history_item_common, null);
            editView = (ImageView) mContentView.findViewById(R.id.history_edit_view);
        }
        return mContentView;
    }

}

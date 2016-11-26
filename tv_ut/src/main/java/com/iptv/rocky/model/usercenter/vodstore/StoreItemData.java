package com.iptv.rocky.model.usercenter.vodstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.iptv.common.data.StoreChannelInfo;
import com.iptv.rocky.base.BaseListItemData;
import com.iptv.rocky.view.usercenter.vodstore.StoreItemView;
import com.iptv.rocky.R;

public class StoreItemData extends BaseListItemData {

    public StoreChannelInfo mVodInfo;

    public StoreItemView mContentView;

    public ImageView editView;

    public void setView(StoreChannelInfo vodInfo, boolean isEdit) {
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
            mContentView = (StoreItemView) inflater.inflate(R.layout.vod_store_item_common, null);
            editView = (ImageView) mContentView.findViewById(R.id.store_edit_view);
        }
        return mContentView;
    }

}

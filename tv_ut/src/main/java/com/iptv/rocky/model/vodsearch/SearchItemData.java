package com.iptv.rocky.model.vodsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.data.VodChannel;
import com.iptv.rocky.base.BaseListItemData;
import com.iptv.rocky.view.vodsearch.SearchItemView;
import com.iptv.rocky.R;

public class SearchItemData extends BaseListItemData {

    public VodChannel mVodInfo;

    private SearchItemView mContentView;

    public void initView(VodChannel vodInfo) {
        mVodInfo = vodInfo;
        mContentView.initView(vodInfo);
    }

    public void destroy() {
        mContentView.destroy();
    }

    @Override
    public View getView(Context context) {
        if (mContentView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mContentView = (SearchItemView) inflater.inflate(R.layout.vod_search_item_common, null);
        }
        return mContentView;
    }

}

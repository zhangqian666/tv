package com.iptv.rocky.model.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.utils.DataReloadUtil;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.home.HomeTopVerView;
import com.iptv.rocky.R;

public class HomeTopVerData extends HomeItemData {
    public int subtitleSize;
    public int subtitleColor;
    public boolean isSubTitleBold;
    public int cutlineBackColor;
    public int backImage;
    public int floatImage;

    private HomeTopVerView mContentView;

    @Override
    public View getView(Context context) {
        if (mContentView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mContentView = (HomeTopVerView) inflater.inflate(R.layout.home_page_item_topver, null);
            mContentView.initView(this);
        }
        return mContentView;
    }

    @Override
    public void initSpecialData() {
        if (super.id == DataReloadUtil.HomeHistoryViewID) {
            subtitleSize = (int) (TvApplication.dpiHeight / 28);
            subtitleColor = R.color.history_subtitle_color;
            isSubTitleBold = false;
            cutlineBackColor = R.color.white;
            backImage = R.drawable.history_itemview_bg;
        } else if (super.id == DataReloadUtil.HomeStoreViewID) {
            subtitleSize = (int) ((TvApplication.dpiHeight / 28) * 1.8);
            isSubTitleBold = true;
            subtitleColor = R.color.white;
            cutlineBackColor = R.color.usercenter_cutline;
            floatImage = R.drawable.home_store_floatimage_bg;
        }
    }
}

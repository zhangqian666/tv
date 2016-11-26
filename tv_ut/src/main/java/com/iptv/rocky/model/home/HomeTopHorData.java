package com.iptv.rocky.model.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.utils.DataReloadUtil;
import com.iptv.rocky.view.home.HomeTopHorView;
import com.iptv.rocky.R;

public class HomeTopHorData extends HomeItemData {
    public int subtitleColor;
    public int backImage;

    private HomeTopHorView mContentView;

    @Override
    public View getView(Context context) {
        if (mContentView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mContentView = (HomeTopHorView) inflater.inflate(R.layout.home_page_item_tophor, null);
            mContentView.initView(this);
        }
        return mContentView;
    }

    @Override
    public void initSpecialData() {
//        if (super.id == DataReloadUtil.HomeChaseViewID) {
//            subtitleColor = R.color.history_subtitle_color;
//            backImage = R.drawable.chase_itemview_bg;
//        }
    }
}

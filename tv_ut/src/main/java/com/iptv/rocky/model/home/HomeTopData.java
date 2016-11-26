package com.iptv.rocky.model.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.utils.DataReloadUtil;
import com.iptv.rocky.view.home.HomeTopView;
import com.iptv.rocky.R;

public class HomeTopData extends HomeItemData {
    public int floatResId;
    public boolean isFontBlod = false;
    private HomeTopView mContentView;

    @Override
    public View getView(Context context) {
        if (mContentView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mContentView = (HomeTopView) inflater.inflate(R.layout.home_page_item_top, null);
            mContentView.initView(this);
        }
        return mContentView;
    }

    @Override
    public void initSpecialData() {
        if (super.id == DataReloadUtil.HomePlaySettingViewID) {
            this.floatResId = R.drawable.playsetting_floatimage;
            isFontBlod = true;
        } else if (super.id == DataReloadUtil.HomeUserSettingViewID) {
            this.floatResId = R.drawable.usersetting_floatimage;
            isFontBlod = true;
        }
    }
}

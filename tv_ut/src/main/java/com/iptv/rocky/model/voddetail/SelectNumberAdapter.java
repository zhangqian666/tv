package com.iptv.rocky.model.voddetail;

import java.util.ArrayList;
import java.util.List;

import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.view.voddetail.SelectNumberMetroView;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class SelectNumberAdapter extends PagerAdapter {

	private List<SelectNumberMetroView> list;

	private VodDetailInfo vodDetailobj;

	public SelectNumberAdapter(VodDetailInfo vodDetailobj, List<SelectNumberMetroView> list) {
		this.list = list;
		this.vodDetailobj = vodDetailobj;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		SelectNumberMetroView metro = list.get(position);
		if (metro.getChildCount() <= 0) {
			List<VodChannel> datas = callback.getPageItemData(position);
			for (int i = 0, size = datas.size(); i < size; i++) {
				SelectItemData itemData = new SelectItemData(0.75, 0.45);
				itemData.setVodDetailObj(vodDetailobj);
				itemData.setData(datas.get(i));
				metro.addMetroItem(itemData);
			}
		}
		((ViewPager) container).addView(metro);
		return metro;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(list.get(position));
	}

	@Override
	public void finishUpdate(View container) {
	}

	public final void restoreState(Parcelable paramParcelable,
			ClassLoader paramClassLoader) {
	}

	public final Parcelable saveState() {
		return null;
	}

	public final void startUpdate(View paramView) {
	}

	public interface CallBack {
		public ArrayList<VodChannel> getPageItemData(int position);
	}

	private CallBack callback;

	public void setCallback(CallBack callback) {
		this.callback = callback;
	}
}

package com.iptv.rocky.model.voddetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.voddetail.SelectItemView;
import com.iptv.rocky.R;

public class SelectItemData extends BaseMetroItemData {

	private SelectItemView itemView;
	private VodChannel video;
	private VodDetailInfo vodDetailobj;

	public SelectItemData(double widthSpan, double heighSpan) {
		super.widthSpan = widthSpan;
		super.heightSpan = heighSpan;
	}

	@Override
	public View getView(Context context) {
		if (itemView == null) {
			itemView = (SelectItemView) LayoutInflater.from(context).inflate(R.layout.vod_select_item, null);
			itemView.fillViewData(vodDetailobj, video);
		}
		return itemView;
	}

	public void setVodDetailObj(VodDetailInfo obj) {
		this.vodDetailobj = obj;
	}

	public void setData(VodChannel video) {
		this.video = video;
	}

	@Override
	public void onClick(Context context) {
		TvUtils.playVideo(context, vodDetailobj, video.VODID);
	}

}

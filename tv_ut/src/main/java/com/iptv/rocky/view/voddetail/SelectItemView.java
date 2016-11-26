package com.iptv.rocky.view.voddetail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class SelectItemView extends RelativeLayout {

	public TextViewDip textView;

	public SelectItemView(Context context) {
		this(context, null);
	}

	public SelectItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(R.drawable.shap_select_item);
	}

	public void fillViewData(VodDetailInfo vodInfo, VodChannel video) {

		String title = video.nNumber+"";
		if (vodInfo != null) {
			title = String.format(getContext().getResources().getString(R.string.detail_number_choose), title);
		}
		textView.setText(title);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		textView = (TextViewDip) findViewById(R.id.select_item_title);
		textView.setTextSize(TvApplication.sTvSelectorTextSize);
	}

}

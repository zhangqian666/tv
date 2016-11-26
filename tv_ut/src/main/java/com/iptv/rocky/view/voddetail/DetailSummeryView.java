package com.iptv.rocky.view.voddetail;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.common.data.VodDetailInfo;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.MultiLineTextView;
import com.iptv.rocky.R;

public class DetailSummeryView extends RelativeLayout {
	
	private MultiLineTextView content;
	
	public DetailSummeryView(Context context) {
		super(context);
		init(context);
	}

	public DetailSummeryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View root = inflater.inflate(R.layout.vod_detail_summery, this);
		float textSize = (float) (TvApplication.sTvMasterTextSize * 0.8);
		content = (MultiLineTextView) root.findViewById(R.id.summery_video_content);
		content.setTextSize(textSize);
		content.setLineSpacing(15, 1);
	}
	
	public void createView(VodDetailInfo info) {
		StringBuilder ret = new StringBuilder();

//		if (!info.getYear().equals("")) {
//			ret.append(String.format(getResources().getString(R.string.detail_year), info.getYear())).append("\n");
//		}
//		if (!info.getArea().equals("")) {
//			ret.append(String.format(getResources().getString(R.string.detail_area), info.getArea())).append("\n");
//		}
		if (!TextUtils.isEmpty(info.DIRECTOR)) {
			ret.append(String.format(getResources().getString(R.string.detail_director), info.DIRECTOR)).append("\n");
		}
		
		ArrayList<String> lstAct = info.CASTMAP.get(6);
		if (lstAct != null && lstAct.size() > 0) {
			String act = "";
			int i = 0;
			for (String s : lstAct)
			{
				if (i == 0)
					act += s;
				else
					act += ("," + s);
				i++;
			}
			ret.append(String.format(getResources().getString(R.string.detail_actor), act)).append("\n");
		}
		
		if (info.ELAPSETIME > 0) {
			ret.append(String.format(getResources().getString(R.string.detail_duration), info.ELAPSETIME)).append("\n");
		}
		
		ret.append(String.format(getResources().getString(R.string.detail_price), info.VODPRICE)).append("\n");
		
		if (!TextUtils.isEmpty(info.INTRODUCE)) {
			ret.append(String.format(getResources().getString(R.string.detail_content), info.INTRODUCE));
		}
		content.setText(ret.toString());
	}

}

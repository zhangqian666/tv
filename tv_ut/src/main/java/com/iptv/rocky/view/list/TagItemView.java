package com.iptv.rocky.view.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class TagItemView extends RelativeLayout {
	
	private TextViewDip mNameText;
	
	public TagItemView(Context context) {
		this(context, null, 0);
	}
	
	public TagItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TagItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
//	public void initView(TagInfo tagInfo) {
//		setBackgroundResource(R.drawable.tv_shap_tag_item);
//		mNameText.setText(tagInfo.Name);
//	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mNameText = (TextViewDip)findViewById(R.id.list_tag_text);
		mNameText.setTextSize(TvApplication.sTvSelectorTextSize);
	}

}

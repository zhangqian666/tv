package com.iptv.rocky.view.vodthd;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.model.vodthd.VodThdItemData;
import com.iptv.rocky.view.FloatLayerView;
import com.iptv.rocky.R;

public class VodThdItemView extends RelativeLayout {
	
	private AsyncImageView image;
	private FloatLayerView floatLayerView;
	
	private VodThdItemData categoryItemData;
	
	public VodThdItemView(Context context) {
		this(context, null, 0);
	}
	
	public VodThdItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VodThdItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void initView(VodThdItemData itemData) {
		categoryItemData = itemData;
		
		floatLayerView.initView(categoryItemData.categoryItem.title);
		//floatLayerView.setVisibility(GONE);
		image.setImageUrl(categoryItemData.categoryItem.bg);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		image = (AsyncImageView) findViewById(R.id.special_gallery_metro_item_asyncimageview);
		floatLayerView = (FloatLayerView) findViewById(R.id.tv_floatlayer);
	}
	
//	public void startMarquee() {
//		floatLayerView.setVisibility(View.VISIBLE);
//		floatLayerView.startMarquee();
//	}
//	
//	public void stopMarquee() {
//		floatLayerView.setVisibility(View.INVISIBLE);
//		floatLayerView.stopMarquee();
//	}
}

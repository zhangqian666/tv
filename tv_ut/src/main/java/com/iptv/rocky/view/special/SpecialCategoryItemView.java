package com.iptv.rocky.view.special;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.R;
import com.iptv.rocky.model.special.SpecialCategoryItemData;
import com.iptv.rocky.view.FloatLayerView;

public class SpecialCategoryItemView extends RelativeLayout {
	
	private AsyncImageView image;
	private FloatLayerView floatLayerView;
	
	private SpecialCategoryItemData categoryItemData;
	
	public SpecialCategoryItemView(Context context) {
		this(context, null, 0);
	}
	
	public SpecialCategoryItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SpecialCategoryItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void initView(SpecialCategoryItemData itemData){
		categoryItemData = itemData;
		
		floatLayerView.initView(categoryItemData.categoryItem.getTitle());
		image.setImageUrl(categoryItemData.categoryItem.getCover_img());
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
				
		image = (AsyncImageView) findViewById(R.id.special_gallery_metro_item_asyncimageview);
		floatLayerView = (FloatLayerView) findViewById(R.id.tv_floatlayer);
		
		floatLayerView.setAlpha(0f);
	}
	
	public void startMarquee() {
		floatLayerView.setAlpha(1f);
		floatLayerView.startMarquee();
	}
	
	public void stopMarquee() {
		floatLayerView.setAlpha(0f);
		floatLayerView.stopMarquee();
	}
}

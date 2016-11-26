package com.iptv.rocky.view.voddetail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.voddetail.DetailMainControlContentData;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class DetailMainControlView extends RelativeLayout {

	private ImageView imgPoiter;
	private TextViewDip text;
	
	public DetailMainControlView(Context context) {
		this(context, null);
	}
	
	public DetailMainControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void initView(DetailMainControlContentData contentData) {
		setBackgroundResource(contentData.backgroundResId);
		imgPoiter.setImageResource(contentData.imageResId);
		text.setTextSize(contentData.textSize);
		text.setText(contentData.text);
		
		setMarqueeable(contentData.isMarquee);
	}
	
	public CharSequence getText(){
		return text.getText();
	}
	
	public void setText(String src){
		text.setText(src);
	}
	
	public void setMarqueeable(boolean b){
		text.setSelected(b);
	}
	
	public void setImageResource(int resId) {
		imgPoiter.setImageResource(resId);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		imgPoiter = (ImageView) findViewById(R.id.control_img_pointer);
		text = (TextViewDip) findViewById(R.id.control_text);
		
		int padding = (int)(TvApplication.pixelHeight / 38.0);
		int paddingLR = (int) (text.getTextSize() / 4);
		text.setPadding(paddingLR, 0, paddingLR, padding);
		text.setTextSize(TvApplication.sTvMasterTextSize);
		imgPoiter.setPadding(0, 0, 0, padding);
	}
	
}

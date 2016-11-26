package com.iptv.rocky.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class FloatLayerView extends RelativeLayout {

	private TextViewDip mContentView;
	private TextViewDip mSubContentView;

	public FloatLayerView(Context context) {
		this(context, null, 0);
	}
	
	public FloatLayerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public FloatLayerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void initView(String title) {
        initView(title, null, false);
    }
	
	public void initView(String title, String subTitle, boolean showSubTitle) {
		mContentView.setText(title);
		mSubContentView.setText(subTitle);
		mSubContentView.setVisibility(showSubTitle && !TextUtils.isEmpty(subTitle) ? VISIBLE : GONE);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mContentView = (TextViewDip)findViewById(R.id.tv_floatlayer_txt);
		mContentView.setTextSize(TvApplication.sTvFloatTextSize);
        mSubContentView = (TextViewDip) findViewById(R.id.tv_floatlayer_subtxt);
        mSubContentView.setTextSize((float) (TvApplication.sTvFloatTextSize * 0.9));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)getLayoutParams();
		params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
		params.height = TvApplication.sTvFloatLayerHeight;

	    int padding = (int)(mContentView.getTextSize() / 2);
	    mContentView.setPadding(padding, padding, padding, padding / 2);
        mSubContentView.setPadding(padding, 0, padding, padding);
    }
	
	public void startMarquee() {
		mContentView.setSelected(true);
		mSubContentView.setSelected(true);
	}
	
	public void stopMarquee() {
		mContentView.setSelected(false);
		mSubContentView.setSelected(false);
	}

    public void displaySubTitle() {
    	if (mSubContentView.getText().length() > 0) {
    		mSubContentView.setVisibility(VISIBLE);
    	}
    }

    public void hideSubTitle() {
        mSubContentView.setVisibility(GONE);
    }

    public void setTitleTextSize(float size) {
        mContentView.setTextSize(size);
    }
}

package com.iptv.rocky.view.recchan;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.recchan.RecBillCommonData;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class RecBillCommonView extends RelativeLayout {

	private View mRootView;
	private TextViewDip mTextView;
    private Context mContext;
    private RecBillCommonData mData;
    private ImageView mImgPlay;
    
    public RecBillCommonView(Context context) {
		this(context, null, 0);
	}
	
	public RecBillCommonView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RecBillCommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        mContext = context;
    }
	
	public void initView(RecBillCommonData data) {
		mData = data;
		if (!data.isNotTopPadding) {
			//setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);
			setPadding(0, 0, 0, 0);
		}
		mTextView.setText(data.pageItem.title);
		//mTextView.getPaint().setFakeBoldText(data.isFontBlod);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mRootView = findViewById(R.id.rec_bill_common_root);
		mTextView = (TextViewDip) findViewById(R.id.rec_bill_common_txt);
		mTextView.setTextSize(TvApplication.sTvSelectorTextSize);
		mImgPlay = (ImageView) findViewById(R.id.play_img);
		
//      int padding = (int)(TvApplication.pixelHeight / 38.0);
//		mImageView.setPadding(0, 0, 0, padding);
//		mTextView.setPadding(0, 0, 0, padding);
	}

	public void onOwnerFocusChange(boolean hasFocus) {
    	if (hasFocus) {
    		mImgPlay.setVisibility(View.VISIBLE);
    		//mRootView.setBackgroundColor(Color.BLUE);
    		mRootView.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
    	} else {
    		mImgPlay.setVisibility(View.INVISIBLE);
    		mRootView.setBackgroundColor(Color.TRANSPARENT);
    	}
	}
	
}

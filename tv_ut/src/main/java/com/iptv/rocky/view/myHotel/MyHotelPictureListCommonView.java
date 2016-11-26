package com.iptv.rocky.view.myHotel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.rocky.R;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.hotel.MyHotelPictureListCommonData;

public class MyHotelPictureListCommonView extends RelativeLayout {

	//private View mRootView;
	private TextView mTextView;
    private Context mContext;
    private MyHotelPictureListCommonData mData;

    public MyHotelPictureListCommonView(Context context) {
		this(context, null, 0);
	}
	
	public MyHotelPictureListCommonView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MyHotelPictureListCommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        mContext = context;
    }
	
	public void initView(MyHotelPictureListCommonData data) {
		mData = data;
		if (!data.isNotTopPadding) {
			//setPadding(0, TvApplication.sTvItemTopPadding, 0, 0);
			setPadding(10, 10, 0, 0);
		}
		mTextView.setText(data.pageItem.title);
		//mTextView.getPaint().setFakeBoldText(data.isFontBlod);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		//mRootView = findViewById(R.id.live_common_root);
		mTextView = (TextView)findViewById(R.id.live_common_txt);
		mTextView.setTextSize(TvApplication.sTvFloatTextSize);
		
//      int padding = (int)(TvApplication.pixelHeight / 38.0);
//		mImageView.setPadding(0, 0, 0, padding);
//		mTextView.setPadding(0, 0, 0, padding);
	}

}

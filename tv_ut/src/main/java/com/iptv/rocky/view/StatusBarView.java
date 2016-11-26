package com.iptv.rocky.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class StatusBarView extends RelativeLayout {

	private NetworkStateView mStateView;
	private TimeTickView mTickView;
	private SearchView mSearchView;
	
	public StatusBarView(Context context) {
		this(context, null, 0);
	}
	
	public StatusBarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public StatusBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void start() {
		mStateView.start();
		mTickView.start();
	}
	
	public void stop() {
		mStateView.stop();
		mTickView.stop();
	}
	
	public void setSearchVisibility(int visibility) {
		mSearchView.setVisibility(visibility);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mStateView = (NetworkStateView)findViewById(R.id.tv_status_netstate);
		mTickView = (TimeTickView)findViewById(R.id.tv_status_TimeTick);
		mSearchView = (SearchView)findViewById(R.id.tv_status_search);
		
		//LogUtils.info("hotelColumnEnabled == null？"+(TvApplication.hotelColumnEnabled==null));
		/*if(TvApplication.hotelColumnEnabled !=null){
			if(){
		}
		if(TvApplication.hotelColumnEnabled.getLoginIptv().equals("1")){*/
			//mSearchView.setVisibility(View.VISIBLE);
		/*}else{
			mSearchView.setVisibility(View.INVISIBLE);
		}*/
		
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)getLayoutParams();
		params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.height = TvApplication.sTvTabHeight;
		setPadding(TvApplication.sTvLeftMargin - TvApplication.sTvTabItemPadding, 0, TvApplication.sTvLeftMargin, 0);
	}
	
}

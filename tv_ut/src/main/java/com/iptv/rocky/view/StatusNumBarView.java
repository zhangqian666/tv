package com.iptv.rocky.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class StatusNumBarView extends RelativeLayout {

//	private NetworkStateView mStateView;
//	private TimeTickView mTickView;
	private SearchView mSearchView;
	
	public StatusNumBarView(Context context) {
		this(context, null, 0);
	}
	
	public StatusNumBarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public StatusNumBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void start() {
//		mStateView.start();
//		mTickView.start();
	}
	
	public void stop() {
//		mStateView.stop();
//		mTickView.stop();
	}
	
	public void setSearchVisibility(int visibility) {
		mSearchView.setVisibility(visibility);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
//		mStateView = (NetworkStateView)findViewById(R.id.tv_status_netstate);
//		mTickView = (TimeTickView)findViewById(R.id.tv_status_TimeTick);
		mSearchView = (SearchView)findViewById(R.id.tv_status_search);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)getLayoutParams();
		params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.height = TvApplication.sTvTabHeight;
		setPadding(TvApplication.sTvLeftMargin - TvApplication.sTvTabItemPadding, 0, TvApplication.sTvLeftMargin, 0);
	}
	
}

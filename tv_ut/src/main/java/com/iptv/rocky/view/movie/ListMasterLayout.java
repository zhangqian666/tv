package com.iptv.rocky.view.movie;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iptv.common.data.EnumType;
import com.iptv.rocky.view.TitleView;
import com.iptv.rocky.R;

public class ListMasterLayout extends RelativeLayout {

	private TitleView mTitleView;
	
	private ProgressBar mProgressBar;
	private ListHorizontalListView mListView;
	
	private TitleView mPageInfo;
	private EnumType.Platform platform;

	/**
	 * 构造器
	 * 
	 * @param context
	 */
	public ListMasterLayout(Context context) {
		this(context, null, 0);
	}

	public ListMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ListMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 创建控件
	 * 
	 */
	public void createView(EnumType.Platform platform,String id, String name) {
		mTitleView.setText(name);
		mListView.createView(platform,id);
		this.platform = platform;
	}

	public void destroy() {
		mListView.destroy();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mTitleView = (TitleView) findViewById(R.id.list_titleview); 
		mPageInfo = (TitleView) findViewById(R.id.tv_status_total_page); 

		mListView = (ListHorizontalListView) findViewById(R.id.list_listview); // 水平ListView
		mProgressBar = (ProgressBar) findViewById(R.id.tv_progressbar); // 进度条
		
		mListView.setInfo(mProgressBar, mPageInfo);
		
		requestChildFocus(mListView, mListView.findFocus());
	}

}

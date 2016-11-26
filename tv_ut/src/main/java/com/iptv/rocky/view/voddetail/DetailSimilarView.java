package com.iptv.rocky.view.voddetail;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.voddetail.DetailSimilarListAdapter;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.view.TvHorizontalListView;
import com.iptv.rocky.R;

public class DetailSimilarView extends RelativeLayout {

	private TvHorizontalListView listview;
	private TextViewDip emptyText;
	private ProgressBar progressBar;

	//private VodSimilarFacotry factory;
	private DetailSimilarListAdapter adapter;

	public DetailSimilarView(Context context) {
		this(context, null, 0);
	}

	public DetailSimilarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DetailSimilarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void load(Object... args) {
//		factory = new VodSimilarFacotry();
//		factory.setHttpEventHandler(new HttpEventHandler<VodSimilarListObj>() {
//
//			@Override
//			public void HttpSucessHandler(VodSimilarListObj result) {
//				progressBar.setVisibility(View.GONE);
//				if(result.getList().size() == 0){
//					similarNoResult();
//					return;
//				}
//				
//				similarResult();
//				
//				if (adapter == null) {
//					adapter = new DetailSimilarListAdapter(getContext(), result.getList());
//					listview.setAdapter(adapter);
//				} else {
//					adapter.notifyDataSetChanged();
//				}
//				
//			}
//
//			@Override
//			public void HttpFailHandler() {
//				progressBar.setVisibility(View.GONE);
//				similarNoResult();
//				TvUtils.processHttpFail(getContext());
//			}
//		});
//
//		factory.DownloaDatas(args[0]);
	}
	
	private void similarNoResult(){
		if (listview != null) {
			listview.setVisibility(View.GONE);
		}
		if (emptyText != null) {
			emptyText.setVisibility(View.VISIBLE);
		}
	}
	
	private void similarResult() {
		if (listview != null) {
			if (listview.getVisibility() != View.VISIBLE) {
				listview.setVisibility(View.VISIBLE);
			}
		}
		if (emptyText != null) {
			emptyText.setVisibility(View.GONE);
		}
	}

	public void destory() {
		if (listview != null) {
			listview.destroy();
		}
//		factory.cancel();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		listview = (TvHorizontalListView) findViewById(R.id.detail_similar_hlistview);
		progressBar = (ProgressBar) findViewById(R.id.tv_progressbar);
		
		listview.setAutoFocus(false);
		listview.setPadding(TvApplication.sTvLeftMargin , TvApplication.sTvTabHeight, 0, 0);
		
		emptyText = (TextViewDip) findViewById(R.id.detail_similar_empty);
		emptyText.setTextSize(TvApplication.sTvMasterTextSize);
	}
	
	@Override
	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {
		if (direction == 2 || direction == 130) {
			return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
		}
		if (listview != null && listview.getChildCount() > 0) {
			return listview.getChildAt(0).requestFocus();
		}
		return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
	}
	
}

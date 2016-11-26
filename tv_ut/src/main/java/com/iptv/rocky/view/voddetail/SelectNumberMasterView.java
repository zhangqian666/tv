package com.iptv.rocky.view.voddetail;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.rocky.base.BaseTabItemData;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.voddetail.SelectNumberAdapter;
import com.iptv.rocky.model.voddetail.SelectNumberAdapter.CallBack;
import com.iptv.rocky.view.TabItemView;
import com.iptv.rocky.view.TabViewScrollable;
import com.iptv.rocky.R;

public class SelectNumberMasterView extends RelativeLayout {

	private int page_count = -1;
	private int total;

	private final int ORDER_POSITIVE = 1;
	private final int ORDER_NEGAYIVE = 2;
	private int ORDER_CURRENT = ORDER_POSITIVE;
	public final static int PAGE_SIZE = 24;

	private VodDetailInfo vodDetailobj;
	private TabItemView orderView;
	private TabViewScrollable tabView;
	private ViewPager viewpager;

	private List<SelectNumberMetroView> metroList;
	private List<VodChannel> videos;
	private ArrayList<BaseTabItemData> tabDatas;

	public SelectNumberMasterView(Context context) {
		this(context, null, 0);
	}

	public SelectNumberMasterView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SelectNumberMasterView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void createView(VodDetailInfo obj) {
		this.vodDetailobj = obj;
		this.videos = obj.SUBVODIDLIST;
		calculate();
		initViewPager();
	}

	private void calculate() {
		if (vodDetailobj.SUBVODNUMLIST != null) {
			this.total = vodDetailobj.SUBVODNUMLIST.size();
			int total = this.total;
			if (total % PAGE_SIZE == 0) {
				page_count = total / PAGE_SIZE;
			} else {
				page_count = total / PAGE_SIZE + 1;
			}
		}

		tabDatas = new ArrayList<BaseTabItemData>(page_count);
		metroList = new ArrayList<SelectNumberMetroView>(page_count);
	}

	private void initViewPager() {
		if (page_count <= 0) {
			return;
		}
		for (int i = 0; i < page_count; i++) {
			SelectNumberMetroView frame = new SelectNumberMetroView(getContext());
			initTabData(i);
			metroList.add(frame);
			frame = null;
		}
		tabView.createView(tabDatas);
		
		SelectNumberAdapter adapter = new SelectNumberAdapter(vodDetailobj, metroList);
		adapter.setCallback(new CallBack() {
			@Override
			public ArrayList<VodChannel> getPageItemData(int position) {
				ArrayList<VodChannel> subList = new ArrayList<VodChannel>();
				if (ORDER_CURRENT == ORDER_POSITIVE) {
					positiveOrder(position, subList);
				} else {
					negativeOrder(position, subList);
				}
				return subList;
			}
		});

		viewpager.setAdapter(adapter);
		tabView.setViewPager(viewpager);
	}

	private void positiveOrder(int curPage, ArrayList<VodChannel> subList) {
		int from = curPage * PAGE_SIZE;
		int to = from + PAGE_SIZE;
		if (curPage == page_count - 1) {
			if (total % PAGE_SIZE != 0) {
				to -= (PAGE_SIZE - (total % PAGE_SIZE));
			}
		}
		while ((from < total) && from < to) {
			subList.add(videos.get(from));
			from++;
		}
	}

	private void negativeOrder(int curPage, ArrayList<VodChannel> subList) {
		int from;
		if (total % PAGE_SIZE == 0) {
			from = (page_count - curPage) * PAGE_SIZE + (total % PAGE_SIZE);
		} else {
			from = (page_count - curPage - 1) * PAGE_SIZE + (total % PAGE_SIZE);
		}
		int to = from - PAGE_SIZE + 1;
		if (to <= 0) {
			to = 1;
		}
		while ((from >= to) && (to >= 0)) {
			subList.add(videos.get(from - 1));
			from--;
		}
	}

	private void initTabData(int curPage) {
		String tabText = "";

		if (ORDER_CURRENT == ORDER_POSITIVE) {
			int from = curPage * PAGE_SIZE;
			int tabFrom = from;
			int to = from + PAGE_SIZE;
			if (curPage == page_count - 1) {
				if (total % PAGE_SIZE != 0) {
					to -= (PAGE_SIZE - (total % PAGE_SIZE));
				}
			}

			tabText = (tabFrom + 1) + "-" + to;
		} else if (ORDER_CURRENT == ORDER_NEGAYIVE) {
			int from;
			int tabFrom;
			if (total % PAGE_SIZE == 0) {
				from = (page_count - curPage) * PAGE_SIZE + (total % PAGE_SIZE);
			} else {
				from = (page_count - curPage - 1) * PAGE_SIZE + (total % PAGE_SIZE);
			}
			tabFrom = from;
			int to = from - PAGE_SIZE + 1;
			if (to <= 0) {
				to = 1;
			}

			tabText = to + "-" + tabFrom;
		}

		BaseTabItemData tabData = new BaseTabItemData(tabText);
		tabDatas.add(tabData);
	}

	private OnClickListener orderClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (ORDER_CURRENT == ORDER_POSITIVE) {
				ORDER_CURRENT = ORDER_NEGAYIVE;
				orderView.setText(getResources().getString(R.string.order_negtive));
			} else {
				ORDER_CURRENT = ORDER_POSITIVE;
				orderView.setText(getResources().getString(R.string.order_positive));
			}

			if (metroList != null) {
				metroList.clear();
			}
			if (tabDatas != null) {
				tabDatas.clear();
			}
			initViewPager();
		}
	};

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();

		tabView.autoFocusStart();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		ORDER_CURRENT = ORDER_POSITIVE;
		tabView.autoFocusStop();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		orderView = (TabItemView) findViewById(R.id.select_master_order);
		tabView = (TabViewScrollable) findViewById(R.id.tv_tabScrollablelayout);
		viewpager = (ViewPager) findViewById(R.id.select_master_viewpager);

		RelativeLayout.LayoutParams lp = (LayoutParams) orderView.getLayoutParams();
		lp.height = TvApplication.sTvTabHeight;
		lp.leftMargin = TvApplication.sTvLeftMargin / 2;

		int padding = TvApplication.sTvTabItemPadding;
		orderView.setPadding(padding, 0, padding, 0);
		orderView.setText(getResources().getString(R.string.order_positive));
		orderView.setOnClickListener(orderClickListener);
	}

}

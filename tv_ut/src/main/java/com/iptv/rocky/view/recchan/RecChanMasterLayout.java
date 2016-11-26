package com.iptv.rocky.view.recchan;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.RecBill;
import com.iptv.common.data.RecChan;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.utils.TimeUtil;
import com.iptv.rocky.R;
import com.iptv.rocky.RecChanActivity;
import com.iptv.rocky.base.BaseTabItemData;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.RecBillJsonFactory;
import com.iptv.rocky.hwdata.xml.RecBillFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.recchan.RecBillLayoutItem;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TabView;

public class RecChanMasterLayout extends RelativeLayout 
implements ViewPager.OnPageChangeListener, TabView.SelectChangedListener, TabView.DownListener, 
RecBillViewPager.OnRecBillPageScrollChangeListener, RecChanListView.OnListChangeListener {

	private RecChanListView mRecChanListView;

	private ProgressBar mProgressbar;

	private TabView mTabView;
	private RecBillViewPager mPageView;

	private ArrayList<BaseTabItemData> sTabDatas;

	private RecBillFactory mRecBillFactory;
	private RecBillJsonFactory mRecBillJsonFactory;

	private ImageView mImgRightArrow;

	private int mTabNum;
	private int mIndex;

	private RecChan mRecChan;

	private Context mContext;

	private Handler handlerDownloadRecBill = new Handler();

	// 某一天中节目单的页码索引
	private int mBillIndex = 0;

	private HashMap<Integer, ArrayList<RecBill>> mRecBillMap = new HashMap<Integer, ArrayList<RecBill>>();

	public RecChanMasterLayout(Context context) {
		this(context, null, 0);
	}

	public RecChanMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RecChanMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;

		mRecBillFactory = new RecBillFactory();
		mRecBillFactory.setHttpEventHandler(RecBillHandler);
		mRecBillFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());

		mRecBillJsonFactory = new RecBillJsonFactory();
		mRecBillJsonFactory.setHttpEventHandler(mRecBillJsonHandler);
		mRecBillJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
	}

	public void destroy() {
		mRecChanListView.destroy();
		if (TvApplication.platform==EnumType.Platform.ZTE) {
			mRecBillJsonFactory.cancel();
		}else{
			mRecBillFactory.cancel();
		}
	}

	public void resume() {
		mRecChanListView.requestFocus();

		mTabView.autoFocusStart();
	}

	public void pause() {
		mRecChanListView.requestFocus();

		mTabView.autoFocusStop();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mProgressbar = (ProgressBar) findViewById(R.id.tv_progressbar);
		mProgressbar.setVisibility(View.VISIBLE);

		mRecChanListView = (RecChanListView) findViewById(R.id.list_view);
		mRecChanListView.setOnListChangeListener(this);
		mRecChanListView.setProgressBar(mProgressbar);

		mTabView = (TabView) findViewById(R.id.tv_tablayout);
		mPageView = (RecBillViewPager)findViewById(R.id.recchan_viewpage);

		mImgRightArrow = (ImageView) findViewById(R.id.right_arrow);
		mImgRightArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<RecBill> lstRecBill = mRecBillMap.get(mTabView.getSelectedIndex());
				// 根据 页面索引判断如何赋值
				ArrayList<RecBill> lstSubRecBill = new ArrayList<RecBill>();
				if (lstRecBill.size() > (mBillIndex + 1)*24) {	// 判断是否可以向后翻页
					if (lstRecBill.size() < (mBillIndex + 2)*24) {
						lstSubRecBill.addAll(lstRecBill.subList((mBillIndex + 1)*24, lstRecBill.size()));
					} else {
						lstSubRecBill.addAll(lstRecBill.subList((mBillIndex + 1)*24, (mBillIndex + 2)*24));
					}
					mBillIndex++;

					// 换向左图标
					mImgRightArrow.setImageResource(R.drawable.left_arrows);
				} else {
					mBillIndex = 0;

					lstSubRecBill.addAll(lstRecBill.subList(0, 24));

					// 换向右图标
					mImgRightArrow.setImageResource(R.drawable.right_arrows);
				}

				if (!mPageView.createView(mTabView.getSelectedIndex(), lstSubRecBill, false)) {
					return;
				}
			}
		});

		mImgRightArrow.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mImgRightArrow.setImageResource(mBillIndex == 0 ? R.drawable.right_arrows : R.drawable.left_arrows);
				} else {
					mImgRightArrow.setImageResource(mBillIndex == 0 ? R.drawable.right_arrow : R.drawable.left_arrow);
				}
			}
		});

		mTabView.setSelectChanngeListener(this);
		mTabView.setDownListener(this);
		mTabView.isAutoSelected(true);
		//mTabView.setPadding(TvApplication.sTvLeftMargin + (int) (ScreenUtils.getLiveItemWidth() / 1.3), TvApplication.sTvTabItemPadding, 0, 0);
		mTabView.setPadding(TvApplication.sTvLeftMargin + (int) (ScreenUtils.getLiveItemWidth() / 2), TvApplication.sTvTabItemPadding, 0, 0);


		//		mTabView.setItemPadding((int)(TvApplication.sTvTabItemPadding / 1.3));

		mPageView.setOnPageChangeListener(this);
		mPageView.setOnRecBillPageScrollChangeListener(this);

		//mPageView.setPadding(TvApplication.sTvLeftMargin + (int) (ScreenUtils.getLiveItemWidth() / 1.3), TvApplication.sTvTabItemPadding+40, 0, 0);
	}

	public void createView() {
		mRecChanListView.createView();
	}

	@Override
	public void onChange(int index) {
		if (mPageView.getCurrentItem() != index) {
			mPageView.setCurrentItem(index);

			// 设置当前tab的向下焦点移动索引
			//mTabView.mSelectedView.setNextFocusDownId(mPageView.getChildView(index).getChildAt(0).getId());
			//mTabView.mSelectedView.setNextFocusDownId(mPageView.getChildView(index).getId());
			//mTabView.getChildView(i).setNextFocusDownId(mPageView.getChildView(i).getId())

			ArrayList<RecBill> lstRecBill = mRecBillMap.get(index);
			if (lstRecBill != null && lstRecBill.size() > 24) {
				mImgRightArrow.setVisibility(View.VISIBLE);
				mImgRightArrow.setImageResource(R.drawable.right_arrow);
			} else {
				mImgRightArrow.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void onDown() {
		// 当前页的第一个 
		View view = mPageView.getChildView(mTabView.getSelectedIndex()).getChildAt(0);
		if (view != null) {
			view.requestFocus();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		mTabView.tabSelected(arg0);
	}

	//	@Override
	//	protected boolean onRequestFocusInDescendants(int direction,
	//			Rect previouslyFocusedRect) {
	//		View v = getFocusedChild();
	//		return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
	//	}

	@Override
	public void notifyDataSetChanged(RecChan recChan) {
		RecChanActivity.mRecChan = recChan;
		mRecChan = recChan;

		mTabNum =3;// mRecChan.RECORDLENGTH / (3600 * 24);
		createTabView();
		createPageView();

		//		for (int i = 0; i < mTabNum; i++) {
		//			//mTabView.getChildView(i).setNextFocusDownId(mPageView.getChildView(i).getId());
		//			mTabView.getChildView(i).setNextFocusDownId(mPageView.getChildView(i).getChildAt(0).getId());
		//		}
		if (TvApplication.platform==EnumType.Platform.ZTE) {
			mRecBillJsonFactory.cancel();
		}else{
			mRecBillFactory.cancel();
		}

		mIndex = 0;
		mBillIndex = 0;
		mRecBillMap.clear();

		mImgRightArrow.setVisibility(View.INVISIBLE);
		mProgressbar.setVisibility(View.VISIBLE);

		// 下载数据，
		//mRecBillFactory.DownloadDatas(mRecChan.CHANNELID, TimeUtil.getUserDate("yyyyMMdd"));
		// 改为多线程。
		if (handlerDownloadRecBill != null) {
			handlerDownloadRecBill.removeCallbacks(runnableDownloadRecBill);
			handlerDownloadRecBill.postDelayed(runnableDownloadRecBill, 100);
		}

	}

	/**
	 *  创建标题view
	 */
	private void createTabView() {
		mTabView.removeAllTabs();

		if (sTabDatas != null) {
			sTabDatas.clear();
		}
		sTabDatas = new ArrayList<BaseTabItemData>(mTabNum);
		sTabDatas.add(new BaseTabItemData(getContext().getString(R.string.today)));

		for (int i = 1; i <= mTabNum - 1; i++)
		{
			sTabDatas.add(new BaseTabItemData(TimeUtil.dateToStr(TvUtils.beforeDate(0-i), "MM-dd")));
		}
		mTabView.createView(sTabDatas);

		mTabView.autoFocusStart();
	}

	/**
	 *  创建Pageview
	 */
	private void createPageView() {
		mPageView.removeAllViews();

		ArrayList<RecBillLayoutItem> lstRecBillLayoutItem = new ArrayList<RecBillLayoutItem>(mTabNum);

		lstRecBillLayoutItem.add(new RecBillLayoutItem(getContext().getString(R.string.today)));

		for (int i = 1; i <= mTabNum - 1; i++) {
			lstRecBillLayoutItem.add(new RecBillLayoutItem(TimeUtil.dateToStr(TvUtils.beforeDate(0-i), "MM-dd")));
		}

		mPageView.createView(lstRecBillLayoutItem);
	}

	private HttpEventHandler<ArrayList<RecBill>> RecBillHandler = 
			new HttpEventHandler<ArrayList<RecBill>>() {

		@Override
		public void HttpSucessHandler(ArrayList<RecBill> result) {
			mProgressbar.setVisibility(View.GONE);

			ArrayList<RecBill> lstRecBill = new ArrayList<RecBill>();
			if (result.size() > 24) {
				lstRecBill.addAll(result.subList(0, 24));
			} else{
				lstRecBill.addAll(result);
			}

			if (!mPageView.createView(mIndex, lstRecBill, false)) {
				mIndex--;
				return;
			}

			mRecBillMap.put(mIndex, result);

			mIndex++;

			if (mIndex < mTabNum) {
				if (TvApplication.platform==EnumType.Platform.ZTE) {
					mRecBillJsonFactory.DownloadDatas(mRecChan.CHANNELIDZTE, mIndex);
				}else{
					mRecBillFactory.DownloadDatas(mRecChan.CHANNELID, TimeUtil.dateToStr(TvUtils.beforeDate(0-mIndex), "yyyyMMdd"),1);	
				}
				
			} else if (mIndex == mTabNum) {
				mRecChanListView.requestFocus();

				lstRecBill = mRecBillMap.get(0);
				if (lstRecBill.size() > 24) {
					mImgRightArrow.setVisibility(View.VISIBLE);
				} 
			} else {
				mIndex = 0;
			}
		}

		@Override
		public void HttpFailHandler() {
			mProgressbar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};


	private HttpEventHandler<ArrayList<RecBill>> mRecBillJsonHandler = 
			new HttpEventHandler<ArrayList<RecBill>>() {

		@Override
		public void HttpSucessHandler(ArrayList<RecBill> result) {
			mProgressbar.setVisibility(View.GONE);

			ArrayList<RecBill> lstRecBill = new ArrayList<RecBill>();
			if (result.size() > 24) {
				lstRecBill.addAll(result.subList(0, 24));
			} else{
				lstRecBill.addAll(result);
			}

			if (!mPageView.createView(mIndex, lstRecBill, false)) {
				mIndex--;
				return;
			}

			mRecBillMap.put(mIndex, result);
//			LogUtils.info("result---->"+result+"  mIndex----->"+mIndex);
			mIndex++;

			if (mIndex < mTabNum) {
				if (TvApplication.platform==EnumType.Platform.ZTE) {
					mRecBillJsonFactory.DownloadDatas(mRecChan.CHANNELIDZTE, mIndex);
				}else{
					mRecBillFactory.DownloadDatas(mRecChan.CHANNELID, TimeUtil.dateToStr(TvUtils.beforeDate(0-mIndex), "yyyyMMdd"),1);
				}
				
			} else if (mIndex == mTabNum) {
				mRecChanListView.requestFocus();

				lstRecBill = mRecBillMap.get(0);
				if (lstRecBill.size() > 24) {
					mImgRightArrow.setVisibility(View.VISIBLE);
				} 
			} else {
				mIndex = 0;
			}
		}

		@Override
		public void HttpFailHandler() {
			mProgressbar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};



	@Override
	public void onItemScrolled(int nCount) {
		LogUtils.debug("onItemScrolled:"+nCount);

		//		if (nCount > 1) {
		//			mRecChanListView.setVisibility(View.GONE);
		//		} else if (nCount <= 1) {
		//			mRecChanListView.setVisibility(View.VISIBLE);
		//		}
	}



	// 
	Runnable runnableDownloadRecBill = new Runnable() {
		@Override
		public void run() {
			if (TvApplication.platform==EnumType.Platform.ZTE) {
				mRecBillJsonFactory.DownloadDatas(mRecChan.CHANNELIDZTE, 0);
			}else{
				mRecBillFactory.DownloadDatas(mRecChan.CHANNELID, TimeUtil.getUserDate("yyyyMMdd"),1);
			}
			
		}
	};

}

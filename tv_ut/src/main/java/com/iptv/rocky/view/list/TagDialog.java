package com.iptv.rocky.view.list;

//import java.util.ArrayList;
//
//import android.content.Context;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.FrameLayout.LayoutParams;
//import android.widget.ProgressBar;
//
//import com.iptv.rocky.R;
//import com.pplive.androidtv.model.list.TagDimension;
//import com.pplive.androidtv.model.list.TagItemData;
//import com.pplive.androidtv.model.list.TagTabItemData;
//import com.pplive.androidtv.utils.TvUtils;
//import com.pplive.androidtv.view.TvDialog;
//import com.pptv.common.data.HttpEventHandler;
//
//public class TagDialog extends TvDialog implements TagItemData.onClickListener {
//
//	private TagFactory mTagFactory;
//	private onClickListener mClickListener;
//
//	private static TagTabItemData mTabItemData;
//	private static ArrayList<TagInfo> mTagInfos;
//	private static ArrayList<TagInfo> mOrders;
//
//	/**
//	 *
//	 */
//	private ProgressBar mProgressBar;
//
//	public TagDialog(Context context) {
//		this(context, R.style.Tv_Dialog);
//	}
//
//	public TagDialog(Context context, int theme) {
//		super(context, theme);
//
//		mTagFactory = new TagFactory();
//		mTagFactory.setHttpEventHandler(tagHttpHandler);
//
//		if (mOrders == null) {
//			mOrders = new ArrayList<TagInfo>(3);
//			mOrders.add(new TagInfo(context.getString(R.string.list_tag_order1)));
//			mOrders.add(new TagInfo(context.getString(R.string.list_tag_order2)));
//			mOrders.add(new TagInfo(context.getString(R.string.list_tag_order3)));
//		}
//	}
//
//	public void createView(int type, TagTabItemData itemData) {
//		showDialogBeforeLoadingData();
//		
//		if (mTabItemData != itemData) {
//			mTabItemData = itemData;
//			if (mTabItemData.dimension != TagDimension.UNKNOW) {
//				mTagFactory.DownloaDatas(type, mTabItemData.dimension.toString());
//			} else {
//				createMetroView(mOrders, false);
//			}
//		} else {
//			if (mTabItemData.dimension != TagDimension.UNKNOW) {
//				createMetroView(mTagInfos, true);
//			} else {
//				createMetroView(mOrders, false);
//			}
//		}
//	}
//
//	/**
//	 * �ڼ���TagItemData֮ǰ����ʾloading
//	 */
//	private void showDialogBeforeLoadingData() {
//		LayoutInflater inflater = LayoutInflater.from(getContext());
//		mProgressBar = (ProgressBar) inflater.inflate(R.layout.tv_progressbar, null);			
//		setContentView(mProgressBar);
//		mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));	
//		mProgressBar.setVisibility(View.VISIBLE);
//		show();
//	}
//
//	public static void destroy() {
//		mTabItemData = null;
//		mTagInfos = null;
//		mOrders = null;
//	}
//	
//	@Override
//	public void onClick(String title) {
//		if (mClickListener != null) {
//			if (mTabItemData.dimension == TagDimension.UNKNOW) {
//				title = getOrderTitle(title);
//			}
//			mClickListener.onClick(title);
//		}
//		dismiss();
//	}
//
//	@Override
//	public void dismiss() {
//		super.dismiss();
//		
//		mTagFactory.cancel();
//	}
//
//	public void setOnClickListener(onClickListener clickListener) {
//		mClickListener = clickListener;
//	}
//
//	public interface onClickListener {
//		public void onClick(String title);
//	}
//
//	private void createMetroView(ArrayList<TagInfo> tagInfos, boolean isAddFirst) {
//		if (mTabItemData == null) {
//			return;
//		}
//		mTagInfos = tagInfos;
//		TagMetroView metroView = new TagMetroView(getContext());
//		if (isAddFirst) {
//			metroView.addMetroItem(createItemData(null));
//		}
//		for (int i = 0, size = mTagInfos.size(); i < size; i++) {
//			TagInfo tagInfo = mTagInfos.get(i);
//			metroView.addMetroItem(createItemData(tagInfo));
//			boolean hasSelected;
//			if (mTabItemData.dimension != TagDimension.UNKNOW) {
//				hasSelected = mTabItemData.selectedTitle.equals(tagInfo.Name);
//			} else {
//				hasSelected = judgeOrderTitle(mTabItemData.selectedTitle,
//						tagInfo.Name);
//			}
//			if (hasSelected) {
//				int index = i;
//				if (isAddFirst) {
//					index++;
//				}
//				View child = metroView.getChildAt(index);
//				if (child != null) {
//					child.requestFocus();
//				}
//			}
//		}
//		setContentView(metroView);
//	}
//
//	private TagItemData createItemData(TagInfo tagInfo) {
//		if (tagInfo == null) {
//			tagInfo = new TagInfo();
//			tagInfo.Name = getContext().getString(R.string.list_tag_all);
//		}
//		TagItemData data = new TagItemData();
//		data.setOnClickListener(TagDialog.this);
//		data.tagInfo = tagInfo;
//		data.heightSpan = 1;
//		data.widthSpan = 2;
//		return data;
//	}
//
//	private boolean judgeOrderTitle(String selectedTitle, String title) {
//		title = getOrderTitle(title);
//		return title.equals(selectedTitle);
//	}
//
//	private String getOrderTitle(String title) {
//		Context context = getContext();
//		if (context.getString(R.string.list_tag_order3).equals(title)) {
//			return "n";
//		} else if (context.getString(R.string.list_tag_order2).equals(title)) {
//			return "g";
//		} else {
//			return "";
//		}
//	}
//
//	private HttpEventHandler<ArrayList<TagInfo>> tagHttpHandler = new HttpEventHandler<ArrayList<TagInfo>>() {
//
//		@Override
//		public void HttpSucessHandler(ArrayList<TagInfo> result) {
//			createMetroView(result, true);
//		}
//
//		@Override
//		public void HttpFailHandler() {
//			TvUtils.processHttpFail(getContext());
//		}
//	};
//
//}

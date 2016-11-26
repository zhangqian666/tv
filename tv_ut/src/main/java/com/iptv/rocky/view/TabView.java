package com.iptv.rocky.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.base.BaseTabItemData;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.AppCommonUtils;
import com.iptv.rocky.R;

public class TabView extends FrameLayout
	implements View.OnKeyListener, View.OnClickListener, ViewTreeObserver.OnGlobalFocusChangeListener {
	
	private int mSelectedIndex;
	private int mItemPadding;
	private boolean mIsAutoSelected = true;
	
	private View mFocusView;
	public View mSelectedView;
	private LayoutInflater mInflater;
	private LinearLayout mLinerLayout;
	private SelectChangedListener mSelectListener;
	private ClickListener mClickListener;

	private DownListener mDownListener;
	
	public TabView(Context context) {
		this(context, null, 0);
	}
	
	public TabView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mInflater = LayoutInflater.from(context);
		setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
	}

	public <T extends BaseTabItemData> void createView(ArrayList<T> items) {
		for (int i = 0, j = items.size(); i < j; i++) {
			TabItemView tabView = (TabItemView)mInflater.inflate(R.layout.tv_tab_item, null);
			tabView.initView(items.get(i), this, this);
			if (mItemPadding > 0) {
				tabView.setPadding(mItemPadding, 0, mItemPadding, 0);
			}
			mLinerLayout.addView(tabView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
			if (mIsAutoSelected && i == 0) {
				tabSelected(tabView);
				tabView.clearFocus();
			}
		}
	}
	
	public void removeAllTabs() {
		if (mLinerLayout != null) {
			mLinerLayout.removeAllViews();
		}
	}
	
	public void setItemPadding(int itemPadding) {
		mItemPadding = itemPadding;
	}
	
	public void setTabText(int index, String text) {
		if (index < 0 || index > mLinerLayout.getChildCount() - 1) {
			return;
		}
		TabItemView tabItemView = (TabItemView) mLinerLayout.getChildAt(index);
		tabItemView.setText(text);
	}
	
	public void setTabText(int index, BaseTabItemData itemData) {
		setTabText(index, itemData.title);
	}
	
	public void isAutoSelected(boolean isAutoSelected) {
		mIsAutoSelected = isAutoSelected;
	}
	
	public void tabSelected(int index) {
		View view = mLinerLayout.getChildAt(index);
		if (view != null) {
			if (mIsAutoSelected) {
				tabSelected(view);
			} else {
				view.setSelected(true);
			}
		}
	}
	
	public void tabUnSelect(int index) {
		View view = mLinerLayout.getChildAt(index);
		if (view != null) {
			view.setSelected(false);
		}
	}
	
	private void tabSelected(View view) {
		//LogUtils.error("tabSelected:");
		if (view == mSelectedView) {
			LogUtils.error("是选择的");
			return;
		}
		if (mSelectedView != null) {
			//LogUtils.error("不是选择的");
			mSelectedView.setSelected(false);
		}
		//LogUtils.error("设定选择的View:");
		mSelectedView = view;
		view.setSelected(true);
		int index = mLinerLayout.indexOfChild(view);
		//LogUtils.error("选择的index:"+index);
		
		if (index != mSelectedIndex) {
			//LogUtils.error("index 与 mSelectedIndex不同"+index +"  "+mSelectedIndex );
			mSelectedIndex = index;
			if (this.mSelectListener != null) {
				this.mSelectListener.onChange(index);
			}
		}else{
			//LogUtils.error("index 与 mSelectedIndex相同");
		}
	}
	
	public int getSelectedIndex() {
		return this.mSelectedIndex;
	}

	public void setSelectedIndex(int position) {
		this.mSelectedIndex = position;
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
				if (mClickListener != null) {
                    playSoundEffect(SoundEffectConstants.CLICK);
					mClickListener.onClick(v, mLinerLayout.indexOfChild(v));
					return true;
				}
			}
		} else if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if (mDownListener != null) {
					mDownListener.onDown();
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void onClick(View v) {
		if (mClickListener != null) {
			mClickListener.onClick(v, mLinerLayout.indexOfChild(v));
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mLinerLayout = ((LinearLayout)findViewById(R.id.tv_tab_linear));
	    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
	    params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
	    params.height = TvApplication.sTvTabHeight;
	    setLayoutParams(params);
	    //setPadding(TvApplication.sTvLeftMargin, 0, 0, 0);
	    //setPadding(TvApplication.sTvLeftMargin / 2, 0, 0, 0);
	    setPadding(TvApplication.sTvLeftMargin - TvApplication.sTvTabItemPadding, 0, 0, 0);
	}

	public interface SelectChangedListener {
		public void onChange(int index);
	}
	
	public interface DownListener {
		public void onDown();
	}
	
	public interface ClickListener {
		public void onClick(View view, int index);
	}
	
	public void setSelectChanngeListener(SelectChangedListener listener) {
		mSelectListener = listener;
	}
	
	public void setDownListener(DownListener listener) {
		mDownListener = listener;
	}
	
	public void setClickListener(ClickListener listener) {
		mClickListener = listener;
	}
	
	public void autoFocusStart() {
		//LogUtils.error("TabView的autofocus start");
		getViewTreeObserver().addOnGlobalFocusChangeListener(this);
	}
	
	public void autoFocusStop() {
		getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
	}
	
	@Override
	public void requestChildFocus(View child, View focused) {
		if (mIsAutoSelected) {
			int index = mLinerLayout.indexOfChild(getRootView().findFocus());
			if (index < 0) {
				super.requestChildFocus(child, focused);
			} else {
				tabSelected(focused);
				focused.requestFocus();
			}
		} else {
			super.requestChildFocus(child, focused);
		}
	}

	@Override
	public void onGlobalFocusChanged(View oldFocus, View newFocus) {
		//View focusView = mFocusView;
		mFocusView = newFocus;
		//LogUtils.error("触发焦点改变");
		
		//boolean isPreFocusChild = AppCommonUtils.isChild(this, focusView);
		boolean isPreFocusChild = AppCommonUtils.isChild(this, oldFocus);
		boolean isCurFocusChild = AppCommonUtils.isChild(this, newFocus);
		if (!isPreFocusChild) {
			if (isCurFocusChild) {
				if (mSelectedView == null) {
					mSelectedView = newFocus;
				} else {
					if (mSelectedView != newFocus) {
						tabSelected(mSelectedView);
						mSelectedView.requestFocus();
					}
				}
			}
		} 
	}
	
	public View getChildView(int index)
	{
		return mLinerLayout.getChildAt(index);
	}
	
}

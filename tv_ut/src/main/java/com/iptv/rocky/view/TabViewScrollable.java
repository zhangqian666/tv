package com.iptv.rocky.view;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iptv.rocky.base.BaseTabItemData;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.AppCommonUtils;
import com.iptv.rocky.R;

public class TabViewScrollable extends HorizontalScrollView implements TvPageIndicator,
	View.OnClickListener, ViewTreeObserver.OnGlobalFocusChangeListener, OnKeyListener{
	
    private Runnable mTabSelector;
    
    private LinearLayout mTabLayout;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;

    private View mFocusView;
    private View mSelectedView;
    private int mSelectedIndex;
    private boolean mIsAutoSelected = true;
    
    private LayoutInflater mInflater;

    public TabViewScrollable(Context context) {
        this(context, null);
    }

    public TabViewScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        mInflater = LayoutInflater.from(context);
        
        setHorizontalScrollBarEnabled(false);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }
    
    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
        	post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        if (mSelectedIndex >= 0) {
        	mTabLayout.getChildAt(mSelectedIndex).setSelected(false);
        }
        
        mSelectedIndex = item;
        mSelectedView = mTabLayout.getChildAt(item);
        mSelectedView.setSelected(true);
        mViewPager.setCurrentItem(item);
        animateToTab(item);
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

	@Override
	public void notifyDataSetChanged() {
	}
	
	public <T extends BaseTabItemData> void createView(ArrayList<T> items) {
		if(mTabLayout != null){
			mTabLayout.removeAllViews();
		}
		
		if(items == null){
			return;
		}
		
		for (int i = 0, j = items.size(); i < j; i++) {
			TabItemView tabView = (TabItemView)mInflater.inflate(R.layout.tv_tab_item, null);
			tabView.initView(items.get(i), this, this);
			mTabLayout.addView(tabView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
			if (i == 0 && mIsAutoSelected) {
				tabSelected(tabView);
				tabView.clearFocus();
			}
		}
	}
	
	public void isAutoSelected(boolean isAutoSelected) {
		mIsAutoSelected = isAutoSelected;
	}
	
	public void tabSelected(int index) {
		View view = mTabLayout.getChildAt(index);
		if (view != null) {
			if (mIsAutoSelected) {
				tabSelected(view);
			} else {
				view.setSelected(true);
			}
		}
	}
	
	public void tabUnSelect(int index) {
		View view = mTabLayout.getChildAt(index);
		if (view != null) {
			view.setSelected(false);
		}
	}
	
	private void tabSelected(View view) {
		int index = mTabLayout.indexOfChild(view);
		
		if (view == mSelectedView) {
			animateToTab(index);
			return;
		}
		if (mSelectedView != null) {
			mSelectedView.setSelected(false);
		}
		mSelectedView = view;
		view.setSelected(true);
		if (index != mSelectedIndex) {
			mSelectedIndex = index;
			mViewPager.setCurrentItem(index);
		}
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mTabLayout = ((LinearLayout)findViewById(R.id.tv_tab_scroll_linear));
		
	    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)getLayoutParams();
	    params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
	    params.height = TvApplication.sTvTabHeight;
	    params.rightMargin = TvApplication.sTvLeftMargin;
	    setLayoutParams(params);
	    //setPadding(0, 0, TvApplication.sTvLeftMargin, 0);
	    setPadding(0, 0, 0, 0);
	}

	@Override
	public void onClick(View v) {
		if (mViewPager != null) {
			int pos = mTabLayout.indexOfChild(v);
			mViewPager.setCurrentItem(pos);
			mSelectedIndex = pos;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
				int pos = mTabLayout.indexOfChild(v);
				mViewPager.setCurrentItem(pos);
				mSelectedIndex = pos;
				return true;
			}
		}
		return false;
	}
	
	public void autoFocusStart() {
		getViewTreeObserver().addOnGlobalFocusChangeListener(this);
	}
	
	public void autoFocusStop() {
		getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
	}

	@Override
	public void requestChildFocus(View child, View focused) {
		if (mIsAutoSelected) {
			int index = mTabLayout.indexOfChild(getRootView().findFocus());
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
	public boolean arrowScroll(int direction) {
		boolean handled = super.arrowScroll(direction);
		if (handled) {
			playSoundEffect( SoundEffectConstants.getContantForFocusDirection( direction ) );
		}
		return handled;
	}
	
	@Override
	public void onGlobalFocusChanged(View oldFocus, View newFocus) {
		View focusView = mFocusView;
		mFocusView = newFocus;
		
		boolean isPreFocusChild = AppCommonUtils.isChild(this, focusView);
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
}

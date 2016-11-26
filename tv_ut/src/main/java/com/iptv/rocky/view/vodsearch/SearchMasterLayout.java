package com.iptv.rocky.view.vodsearch;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.view.TitleView;
import com.iptv.rocky.R;

public class SearchMasterLayout extends RelativeLayout {
	
	private TextViewDip skeyView;
	private TextViewDip searchBeginNoteLayout;
    private LinearLayout searchEmptyNoteLayout;
    private TextViewDip searchEmptyNoteLayoutText;
    private SearchKeyBoardView keyboardView;
    private SearchHorizontalListView mSearchView;
    
    private TitleView mPageInfo;
    
	public SearchMasterLayout(Context context) {
		this(context, null, 0);
	}

	public SearchMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SearchMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
    }
	
	public void destroy() {
		mSearchView.destroy();
	}
	
	public boolean onBackPressed() {
		Activity parent = (Activity)getContext();
		View focusedView = parent.getCurrentFocus();
		if ((focusedView instanceof Button) && focusedView.getTag() != null) {
			if (mSearchView.skey.length() > 0) {
				mSearchView.skey = mSearchView.skey.substring(0, mSearchView.skey.length() - 1);
				skeyChanged();
	            return true;
			}
		}
		return false;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mPageInfo = (TitleView) findViewById(R.id.tv_status_total_page); // TextView
	
        keyboardView = (SearchKeyBoardView) findViewById(R.id.search_KeyBoard);
        skeyView = (TextViewDip) findViewById(R.id.search_keyView);
        int textSize = ScreenUtils.getKeyTextSize();// 大小和键盘字母一样
        skeyView.setTextSize(textSize);
        mSearchView = (SearchHorizontalListView) findViewById(R.id.search_listview);
        
        searchBeginNoteLayout = (TextViewDip) findViewById(R.id.search_beginnote);
        searchBeginNoteLayout.setPadding(0, TvApplication.sTvTabHeight, 0, 0);
        searchBeginNoteLayout.setTextSize(TvApplication.sTvMasterTextSize);
        searchEmptyNoteLayout = (LinearLayout) findViewById(R.id.search_emptynote);
        searchEmptyNoteLayout.setPadding(0, TvApplication.sTvTabHeight, 0, 0);
        
        searchEmptyNoteLayoutText = (TextViewDip) findViewById(R.id.search_emptynote_txt);
        searchEmptyNoteLayoutText.setTextSize(TvApplication.sTvMasterTextSize);
        
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.tv_small_progressbar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)progressBar.getLayoutParams();
        params.setMargins(0, (int)(TvApplication.sTvTabHeight * 1.5), 0, 0);
        
        mSearchView.setInfo(progressBar, mPageInfo);
        
        initSkeyInput();
        initKeyBoardBtns();

        if (mSearchView.smLayout == null) {
            mSearchView.smLayout = this;
        }
    }
	
	private void initSkeyInput() {
    	int width = (int) (TvApplication.pixelWidth / 2.0);
    	int height = (int) (TvApplication.pixelHeight / 14.0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.gravity = Gravity.CENTER;
        params.bottomMargin = ScreenUtils.getSearchKeyPadding();
        skeyView.setLayoutParams(params);
    }
	
    private void initKeyBoardBtns() {
        int keysBtnCount = keyboardView.getChildCount();
        for (int i = 0;i < keysBtnCount; i++) {
            Button btn = (Button)keyboardView.getChildAt(i);
            if (btn == null) {
                continue;
            }
            btn.setOnClickListener(searchKeyClickListener);
            if (btn.getTag().equals("delete")) {
                btn.setOnLongClickListener(removeKeyClickListener);
            }
        }
    }
    	
    public void skeyChanged() {
        skeyView.setText(mSearchView.skey);
        if (TextUtils.isEmpty(mSearchView.skey)) {
        	mSearchView.destroy();
        	viewBeginSearch();
            return;
        }
        viewSearchResult();
        mSearchView.DownloadDatas();
    }
    
    public void viewBeginSearch() {
        searchBeginNoteLayout.setVisibility(VISIBLE);
        mSearchView.setVisibility(GONE);
        searchEmptyNoteLayout.setVisibility(GONE);
    }

    public void viewSearchResult() {
        searchBeginNoteLayout.setVisibility(GONE);
        mSearchView.setVisibility(VISIBLE);
        searchEmptyNoteLayout.setVisibility(GONE);
    }

    public void viewSearchNoResult() {
        searchBeginNoteLayout.setVisibility(GONE);
        mSearchView.setVisibility(GONE);
        searchEmptyNoteLayout.setVisibility(VISIBLE);
    }
    
    private OnClickListener searchKeyClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
            if (v.getTag().equals("delete")) {
                if (mSearchView.skey.length() > 0) {
                    mSearchView.skey = mSearchView.skey.substring(0, mSearchView.skey.length() - 1);
                }
            } else {
                mSearchView.skey = mSearchView.skey + v.getTag();
            }
            skeyChanged();
		}
	};
   
    private OnLongClickListener removeKeyClickListener = new OnLongClickListener() {
    	
        @Override
        public boolean onLongClick(View v) {
            mSearchView.skey = "";
            skeyChanged();
            return true;
        }
    };

}

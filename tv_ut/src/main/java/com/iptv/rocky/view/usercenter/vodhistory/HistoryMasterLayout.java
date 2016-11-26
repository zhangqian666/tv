package com.iptv.rocky.view.usercenter.vodhistory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.rocky.hwdata.local.VodHistoryLocalFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.PromptDialog;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class HistoryMasterLayout extends RelativeLayout {

    private TextViewDip emptyView;
    private TextViewDip editBtn;
    private TextViewDip clearBtn;
    private PromptDialog clearDialog;
    private LinearLayout historyEmptyNoteLayout;
    private LinearLayout editLinearLayout;
    private LinearLayout clearLinearLayout;
    private HistoryHorizontalListView mHistoryView;
    
    private VodHistoryLocalFactory mVodFacotry;

	public HistoryMasterLayout(Context context) {
		this(context, null, 0);
	}

	public HistoryMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HistoryMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mVodFacotry = new VodHistoryLocalFactory(context);
        clearDialog = new PromptDialog(context, getResources().getString(R.string.history_clear_dialog));
    }
	
	public boolean isEditing() {
        return mHistoryView.isEditing();
    }

    public void cancelEdit() {
        mHistoryView.cancelListViewEdit();
        editBtn.setText(getContext().getString(R.string.usercenter_edit));
        clearLinearLayout.setVisibility(VISIBLE);
    }
    
    public void resume() {
    	Object obj = mHistoryView.getSelectedItem();
    	HistoryItemView selectedView = (HistoryItemView) mHistoryView.getSelectedView();
    	if (obj != null && selectedView != null) {
    		HistoryChannelInfo oldInfo = (HistoryChannelInfo)obj;
    		HistoryChannelInfo newInfo = mVodFacotry.getHistoryById(oldInfo.channelid);
    		if (oldInfo.playposition != newInfo.playposition) {
    			oldInfo.playposition = newInfo.playposition;
    			selectedView.initView(newInfo);
    			selectedView.processFocus(true);
    		}
    	}
    }

	public void destroy() {
		mHistoryView.destroy();
	}

	@Override
	protected void onFinishInflate() {
        super.onFinishInflate();

        mHistoryView = (HistoryHorizontalListView) findViewById(R.id.history_listview);
        mHistoryView.createView();

        emptyView = (TextViewDip) findViewById(R.id.history_emptynote_text);
        emptyView.setPadding(0, (int) (TvApplication.pixelHeight / 50), 0, 0);
        emptyView.setTextSize((int)(ScreenUtils.getMasterTextSize()));

        historyEmptyNoteLayout = (LinearLayout)findViewById(R.id.history_emptynote);
        historyEmptyNoteLayout.setPadding(0, (int)(TvApplication.sTvTabHeight * 1.5), 0, 0);
        
        editLinearLayout = (LinearLayout)findViewById(R.id.usercenter_linearlayout_edit);
        clearLinearLayout = (LinearLayout)findViewById(R.id.usercenter_linearlayout_clear);
        int padding = TvApplication.sTvTabItemPadding;
        editLinearLayout.setPadding(padding, padding, padding, padding);
        clearLinearLayout.setPadding(padding, padding, padding, padding);
        
        editBtn = (TextViewDip) findViewById(R.id.usercenter_btn_edit);
        clearBtn = (TextViewDip) findViewById(R.id.usercenter_btn_clear);

        editLinearLayout.setOnClickListener(editListener);
        clearLinearLayout.setOnClickListener(clearListenner);
        
        if (mHistoryView.getItemCount() > 0) {
            requestChildFocus(mHistoryView, mHistoryView.findFocus());
        } else {
        	showEmptyView();
        }
    }

	public void showEmptyView() {
		historyEmptyNoteLayout.setVisibility(VISIBLE);
    	mHistoryView.setVisibility(GONE);
        editBtn.setVisibility(GONE);
        clearBtn.setVisibility(GONE);
        editLinearLayout.setVisibility(GONE);
        clearLinearLayout.setVisibility(GONE);
    }
	
    private OnClickListener editListener = new OnClickListener() {
    	
        @Override
        public void onClick(View v) {
        	if (editBtn.getText().toString().equals(getContext().getResources().getString(R.string.usercenter_edit))) {
        		editBtn.setText(getContext().getResources().getString(R.string.usercenter_finished));
        		mHistoryView.setListViewEdit();
        		clearLinearLayout.setVisibility(GONE);
        	} else if(editBtn.getText().toString().equals(getContext().getResources().getString(R.string.usercenter_finished))) {
        		editBtn.setText(getContext().getResources().getString(R.string.usercenter_edit));
        		cancelEdit();
        		clearLinearLayout.setVisibility(VISIBLE);
        	}
        }
    };

    private OnClickListener clearListenner = new OnClickListener() {
    	
        @Override
        public void onClick(View v) {
            clearDialog.show();
            clearDialog.getmDialogLayout().mCancelBtn.requestFocus();
            clearDialog.setOnConfirmListenner(new PromptDialog.OnConfirmListentner() {
            	
                @Override
                public void onConfirm() {
                    mHistoryView.clearHistory();
                    showEmptyView();
                }
            });
        }
    };
}

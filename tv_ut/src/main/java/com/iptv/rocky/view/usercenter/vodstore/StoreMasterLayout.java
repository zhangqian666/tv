package com.iptv.rocky.view.usercenter.vodstore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.view.PromptDialog;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class StoreMasterLayout extends RelativeLayout {

	private StoreHorizontalListView mStoreView;
    private TextViewDip emptyView;

    private TextViewDip editBtn;
    private TextViewDip clearBtn;
    private LinearLayout editLinearLayout;
    private LinearLayout clearLinearLayout;
    
    private PromptDialog clearDialog;
    private LinearLayout storeEmptyNoteLayout;

	public StoreMasterLayout(Context context) {
		this(context, null, 0);
	}

	public StoreMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StoreMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        clearDialog = new PromptDialog(context, getResources().getString(R.string.store_clear_dialog));
    }

	public void destroy() {
		mStoreView.destroy();
	}

	@Override
	protected void onFinishInflate() {
        super.onFinishInflate();

        mStoreView = (StoreHorizontalListView)findViewById(R.id.store_listview);
        mStoreView.createView();
        
        emptyView = (TextViewDip) findViewById(R.id.store_emptynote_text);
        emptyView.setPadding(0, (int) (TvApplication.pixelHeight / 50), 0, 0);
        emptyView.setTextSize((int)(ScreenUtils.getMasterTextSize()));

        storeEmptyNoteLayout = (LinearLayout)findViewById(R.id.store_emptynote);
        storeEmptyNoteLayout.setPadding(0, (int)(TvApplication.sTvTabHeight * 1.5), 0, 0);
        
        editLinearLayout = (LinearLayout)findViewById(R.id.usercenter_linearlayout_edit);
        clearLinearLayout = (LinearLayout)findViewById(R.id.usercenter_linearlayout_clear);
        int padding = TvApplication.sTvTabItemPadding;
        editLinearLayout.setPadding(padding, padding, padding, padding);
        clearLinearLayout.setPadding(padding, padding, padding, padding);
        
        editBtn = (TextViewDip) findViewById(R.id.usercenter_btn_edit);
        clearBtn = (TextViewDip) findViewById(R.id.usercenter_btn_clear);
        
        editBtn.setTextSize(ScreenUtils.getMasterTextSize());
        clearBtn.setTextSize(ScreenUtils.getMasterTextSize());
        
        editLinearLayout.setOnClickListener(editListener);
        clearLinearLayout.setOnClickListener(clearListenner);

        if (mStoreView.getItemCount() > 0) {
            requestChildFocus(mStoreView, mStoreView.findFocus());
        } else {
            showEmptyView();
        }
    }

    public boolean isEditing() {
        return mStoreView.isEditing();
    }

    public void cancelEdit() {
        mStoreView.cancelListViewEdit();
        editBtn.setText(getContext().getString(R.string.usercenter_edit));
        clearLinearLayout.setVisibility(VISIBLE);
    }
    
    public void showEmptyView() {
    	storeEmptyNoteLayout.setVisibility(VISIBLE);
    	mStoreView.setVisibility(GONE);
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
        		mStoreView.setListViewEdit();
        		clearLinearLayout.setVisibility(GONE);
        	} else if (editBtn.getText().toString().equals(getContext().getResources().getString(R.string.usercenter_finished))){
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
                    mStoreView.clearStore();
                    showEmptyView();
                }
            });
        }
    };
}

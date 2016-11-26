package com.iptv.rocky.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.iptv.rocky.R;

/**
 * 确定&取消 弹出窗口 基类
 */
public class PromptDialogBase extends Dialog {
    //confirm and cancel
    public static int DIALOG_BUTTON_TYPE_DEFAULT = 0;
    //just confirm
    public static int DIALOG_BUTTON_TYPE_JUST_CONFIRM = 1;
    //just cancel
    public static int DIALOG_BUTTON_TYPE_JUST_CANCEL = 2;
    //none button
    public static int DIALOG_BUTTON_TYPE_NONE = 3;

    protected PromptDialogView mDialogLayout;
    
    private PromptDialogBase mDialog;
    
    private OnConfirmListentner mConfirmListentner;
    private OnCancelListentner mCancelListentner;

    public PromptDialogBase(Context context) {
        this(context, DIALOG_BUTTON_TYPE_DEFAULT);
    }
    
    public PromptDialogBase(Context context,int buttonType) {
        super(context, R.style.Tv_Dialog);
        
        mDialog = this;
        
        LayoutInflater inflater = LayoutInflater.from(context);
        mDialogLayout = (PromptDialogView) inflater.inflate(R.layout.prompt_dialog_layout, null);
        mDialogLayout.initButtonView(buttonType);
        setCancelable(true);
        setContentView(mDialogLayout);
        
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        
        if (buttonType == DIALOG_BUTTON_TYPE_DEFAULT || buttonType == DIALOG_BUTTON_TYPE_JUST_CONFIRM) {
            mDialogLayout.mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            	
                @Override
                public void onClick(View v) {
                    if (mConfirmListentner != null) {
                        mConfirmListentner.onConfirm();
                    }
                    mDialog.cancel();
                }
            });
        }

        if (buttonType == DIALOG_BUTTON_TYPE_DEFAULT || buttonType == DIALOG_BUTTON_TYPE_JUST_CANCEL) {
            mDialogLayout.mCancelBtn.setOnClickListener(new View.OnClickListener() {
            	
                @Override
                public void onClick(View v) {
                    if (mCancelListentner != null) {
                        mCancelListentner.onCancel();
                    }
                    mDialog.cancel();
                }
            });
        }
    }

    public PromptDialogView getmDialogLayout() {
		return mDialogLayout;
	}

	public void setOnConfirmListenner(OnConfirmListentner l) {
        mConfirmListentner = l;
    }

    public void setOnCancelListentner(OnCancelListentner l) {
        mCancelListentner = l;
    }

    public interface OnConfirmListentner {
        void onConfirm();
    }

    public interface OnCancelListentner {
        void onCancel();
    }

    @Override
    public void show() {
        super.show();
        if (mDialogLayout.mConfirmBtn != null) {
            mDialogLayout.mConfirmBtn.requestFocus();
        } else if(mDialogLayout.mCancelBtn != null) {
            mDialogLayout.mCancelBtn.requestFocus();
        }
    }

    protected void initContentView() {
    }
}

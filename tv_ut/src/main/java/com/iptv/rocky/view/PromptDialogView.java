package com.iptv.rocky.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.AppCommonUtils;
import com.iptv.rocky.R;

/**
 * 确定&取消 弹出窗口view
 */
public class PromptDialogView extends RelativeLayout{
    private Context mContext;
    private int textSize = (int) (TvApplication.dpiHeight / 30);
    private int width = (int) (TvApplication.pixelWidth / 3);
    private int height = (int) (width / 1.62);
    private int contentHeight = (int) (height / 1.33);

    public RelativeLayout mContentLayout;
    public Button mConfirmBtn;
    public Button mCancelBtn;

    public PromptDialogView(Context context) {
        this(context, null, 0);
    }

    public PromptDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PromptDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentLayout = (RelativeLayout) findViewById(R.id.dialog_content_layout);
        int contentHeight = (int) (height / 1.33);
        mContentLayout.getLayoutParams().height = contentHeight;
        mContentLayout.getLayoutParams().width = width;
    }

    public void initButtonView(int buttonType) {
        if (buttonType == PromptDialogBase.DIALOG_BUTTON_TYPE_NONE) {
            mContentLayout.setBackgroundResource(R.drawable.prompt_dialog_content_nonebtn_bg);
            return;
        } else {
            mContentLayout.setBackgroundResource(R.drawable.prompt_dialog_content_bg);
        }

        int btnPaddingLeft = (int) (width / 2 / 3.6);
        float scaleRate = 1;
        if (TvApplication.pixelHeight > 900 || TvApplication.pixelHeight < 650) {
            scaleRate = TvApplication.dpiHeight / 800;
        }

        if (buttonType == PromptDialogBase.DIALOG_BUTTON_TYPE_DEFAULT) {
            mConfirmBtn = new Button(mContext);
            mCancelBtn = new Button(mContext);
            LayoutParams confirmParams = new LayoutParams(width / 2, height - contentHeight);
            LayoutParams cancelParams = new LayoutParams(width / 2, height - contentHeight);
            mConfirmBtn.setLayoutParams(confirmParams);
            mCancelBtn.setLayoutParams(cancelParams);
        } else if (buttonType == PromptDialogBase.DIALOG_BUTTON_TYPE_JUST_CONFIRM) {
            mConfirmBtn = new Button(mContext);
            LayoutParams confirmParams = new LayoutParams(width, height - contentHeight);
            mConfirmBtn.setLayoutParams(confirmParams);
            btnPaddingLeft = btnPaddingLeft + width / 4;

        } else if (buttonType == PromptDialogBase.DIALOG_BUTTON_TYPE_JUST_CANCEL) {
            mCancelBtn = new Button(mContext);
            LayoutParams cancelParams = new LayoutParams(width, height - contentHeight);
            mCancelBtn.setLayoutParams(cancelParams);
            btnPaddingLeft = btnPaddingLeft + width / 4;
        } else {
            return;
        }

        if (mConfirmBtn != null) {
            final Drawable confirmDrawableFocused = scaleRate < 1 ? AppCommonUtils.scaleDrawable((BitmapDrawable) mContext.getResources().getDrawable(
                    R.drawable.dialog_confirm_icon_focused), scaleRate) : mContext.getResources().getDrawable(R.drawable.dialog_confirm_icon_focused);
            final Drawable confirmDrawableNormal = scaleRate < 1 ? AppCommonUtils.scaleDrawable((BitmapDrawable) mContext.getResources().getDrawable(
                    R.drawable.dialog_confirm_icon_normal), scaleRate) : mContext.getResources().getDrawable(R.drawable.dialog_confirm_icon_normal);
            ((LayoutParams) mConfirmBtn.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.dialog_content_layout);
            ((LayoutParams) mConfirmBtn.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, R.id.dialog_content_layout);
            mConfirmBtn.setGravity(Gravity.CENTER_VERTICAL);
            mConfirmBtn.setClickable(true);
            mConfirmBtn.setFocusable(true);
            mConfirmBtn.setBackgroundResource(buttonType == PromptDialogBase.DIALOG_BUTTON_TYPE_DEFAULT ?
                    R.drawable.prompt_dialog_confirm_btn_bg : R.drawable.prompt_dialog_confirm_only_btn_bg);
            mConfirmBtn.setCompoundDrawablesWithIntrinsicBounds(confirmDrawableFocused, null, null, null);
            mConfirmBtn.setText(" " + mContext.getResources().getString(R.string.dialog_confirm));
            mConfirmBtn.setTextSize(textSize);
            mConfirmBtn.setPadding(btnPaddingLeft, 0, 0, 0);
            mConfirmBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        mConfirmBtn.setTextColor(mContext.getResources().getColor(R.color.dialog_btn_txt_focused));
                        mConfirmBtn.setCompoundDrawablesWithIntrinsicBounds(confirmDrawableFocused, null, null, null);
                    } else {
                        mConfirmBtn.setTextColor(mContext.getResources().getColor(R.color.dialog_btn_txt_normal));
                        mConfirmBtn.setCompoundDrawablesWithIntrinsicBounds(confirmDrawableNormal, null, null, null);
                    }
                }
            });
            addView(mConfirmBtn);
        }

        if (mCancelBtn != null) {
            final Drawable cancelDrawableFocused = scaleRate < 1 ? AppCommonUtils.scaleDrawable((BitmapDrawable) mContext.getResources().getDrawable(
                    R.drawable.dialog_cancel_icon_focused), scaleRate) : mContext.getResources().getDrawable(R.drawable.dialog_cancel_icon_focused);
            final Drawable cancelDrawableNormal = scaleRate < 1 ? AppCommonUtils.scaleDrawable((BitmapDrawable) mContext.getResources().getDrawable(
                    R.drawable.dialog_cancel_icon_normal), scaleRate) : mContext.getResources().getDrawable(R.drawable.dialog_cancel_icon_normal);
            ((LayoutParams) mCancelBtn.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.dialog_content_layout);
            ((LayoutParams) mCancelBtn.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, R.id.dialog_content_layout);
            mCancelBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            mCancelBtn.setClickable(true);
            mCancelBtn.setFocusable(true);
            mCancelBtn.setBackgroundResource(buttonType == PromptDialogBase.DIALOG_BUTTON_TYPE_DEFAULT ?
                    R.drawable.prompt_dialog_cancel_btn_bg : R.drawable.prompt_dialog_cancel_only_btn_bg);
            mCancelBtn.setCompoundDrawablesWithIntrinsicBounds(cancelDrawableNormal, null, null, null);
            mCancelBtn.setText(" " + mContext.getResources().getString(R.string.dialog_cancel));
            mCancelBtn.setPadding(btnPaddingLeft, 0, 0, 0);
            mCancelBtn.setTextSize(textSize);
            mCancelBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        mCancelBtn.setTextColor(mContext.getResources().getColor(R.color.dialog_btn_txt_focused));
                        mCancelBtn.setCompoundDrawablesWithIntrinsicBounds(cancelDrawableFocused, null, null, null);
                    } else {
                        mCancelBtn.setTextColor(mContext.getResources().getColor(R.color.dialog_btn_txt_normal));
                        mCancelBtn.setCompoundDrawablesWithIntrinsicBounds(cancelDrawableNormal, null, null, null);
                    }
                }
            });
            addView(mCancelBtn);
        }
    }

}

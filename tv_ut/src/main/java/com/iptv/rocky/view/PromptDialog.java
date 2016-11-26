package com.iptv.rocky.view;

import com.iptv.rocky.model.TvApplication;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

/**
 * 确定&取消 弹出窗口
 */
public class PromptDialog extends PromptDialogBase {
    private String mContent;
    private Context mContext;

    public PromptDialog(Context context, String content) {
        this(context, content, PromptDialogBase.DIALOG_BUTTON_TYPE_DEFAULT);
    }
    
    public PromptDialog(Context context, String content, int buttonType) {
        super(context,buttonType);
        mContext = context;
        mContent = content;
        initContentView();
    }

    @Override
    protected void initContentView() {
        TextView textView = new TextView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TvApplication.dpiHeight / 30);
        textView.setText(mContent);
        textView.setLineSpacing(TvApplication.pixelHeight/40,1f);
        mDialogLayout.mContentLayout.addView(textView);
    }
}

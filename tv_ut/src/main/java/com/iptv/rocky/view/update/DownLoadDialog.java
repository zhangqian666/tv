package com.iptv.rocky.view.update;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.iptv.rocky.view.PromptDialogBase;
import com.iptv.rocky.R;

public class DownLoadDialog extends PromptDialogBase {

	private Context context;
	
	private TextView textProgress;
	
	public DownLoadDialog(Context context, int type) {
		super(context, type);
		this.context = context;
		initContentView();
	}
	
	@Override
	protected void initContentView() {
		View view = View.inflate(context, R.layout.download_dialog_layout, null);
		textProgress = (TextView) view.findViewById(R.id.download_text);
		mDialogLayout.mContentLayout.addView(view);
	}
	
	public void setTextProgress(String text){
		textProgress.setText(text);
	}

}

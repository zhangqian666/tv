package com.iptv.rocky.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class AboutMasterLayout extends RelativeLayout {
	private Context mContext;

    private TextViewDip mTitleText;

    private LinearLayout mContentLayout;
    private TextViewDip mContentView;

	public AboutMasterLayout(Context context) {
		this(context, null, 0);
	}

	public AboutMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AboutMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;
    }

	@Override
	protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleText = (TextViewDip) findViewById(R.id.about_content_title);
        mTitleText.setTextSize(TvApplication.dpiHeight / 26);
        mTitleText.setPadding(0, 0, 0, (int) (TvApplication.pixelHeight / 35));

        mContentLayout = (LinearLayout) findViewById(R.id.about_content_layout);
        int marginHor = (int) (TvApplication.pixelWidth / 13.5);
        ((LayoutParams)mContentLayout.getLayoutParams()).setMargins(marginHor, 0, marginHor, 0);
        mContentLayout.setPadding((int) (TvApplication.pixelWidth / 13.5), 8, 8, 8);

        mContentView = (TextViewDip) findViewById(R.id.about_content_detail);
        String content = String.format(mContext.getResources().getString(
				R.string.current_version), TvApplication.mAppVersionName, "(" + TvApplication.mAppVersionCode + ")", 
				"\r\n" + mContext.getResources().getString(R.string.about_content));
        
        // add 3A 信息
        content += ("\r\n认证地址：" + TvApplication.authUrl);
        content += ("\r\nID：" + TvApplication.stbId);
        content += ("\r\n账号：" + TvApplication.account);
        
		mContentView.setText(content);
        mContentView.setTextSize(TvApplication.dpiHeight / 26);
        mContentView.setLineSpacing(TvApplication.pixelHeight / 20, 1f);
    }
}

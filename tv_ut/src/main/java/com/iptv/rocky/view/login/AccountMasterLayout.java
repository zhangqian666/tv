package com.iptv.rocky.view.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class AccountMasterLayout extends RelativeLayout {
    public AccountLoginLayout mLoginLayout;
    
    public AccountMasterLayout(Context context) {
        this(context, null, 0);
    }

    public AccountMasterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccountMasterLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void destroy() {
    	mLoginLayout.destroy();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
       
        mLoginLayout = (AccountLoginLayout) findViewById(R.id.account_login_layout);
        int paddingHor = (int) (TvApplication.pixelWidth / 30.45);
        LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        int marginVer = (int) (TvApplication.pixelHeight / 6);
        //contentParams.setMargins(0, marginVer, 0, marginVer);
        contentParams.setMargins(0, marginVer, 0, 0);
        mLoginLayout.setLayoutParams(contentParams);
        mLoginLayout.setPadding(paddingHor, 20, paddingHor, 20);
        mLoginLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
    }

}

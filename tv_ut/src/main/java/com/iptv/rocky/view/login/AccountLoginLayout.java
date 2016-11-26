package com.iptv.rocky.view.login;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class AccountLoginLayout extends LinearLayout {
    private AccountInputLayout mInputLayout;

    public AccountLoginLayout(Context context) {
    	this(context, null, 0);
    }

    public AccountLoginLayout(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }

    public AccountLoginLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void destroy() {
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        mInputLayout = (AccountInputLayout) findViewById(R.id.account_input_login_layout);
        mInputLayout.setPadding((int) (TvApplication.pixelWidth / 38), 4, 4, 4);
        mInputLayout.getLayoutParams().width = (int) (TvApplication.pixelWidth / 2);
    }

}

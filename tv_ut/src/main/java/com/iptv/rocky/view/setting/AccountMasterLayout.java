package com.iptv.rocky.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iptv.common.local.UserInfoFactory;
import com.iptv.common.passport.UserInfo;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class AccountMasterLayout extends RelativeLayout {
	private Context mContext;
    public AccountLoginLayout mLoginLayout;
    private AccountLoginedLayout mLoginedLayout;
    private LinearLayout mProgressBar;

    private String content;

    public void setContent(String content) {
    	this.content = content;
    }
    
	public String getContent() {
		return content;
	}
	 
    public AccountMasterLayout(Context context) {
        this(context, null, 0);
    }

    public AccountMasterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccountMasterLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }
    
    public void destroy() {
    	mLoginLayout.destroy();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
       
        mProgressBar = (LinearLayout) findViewById(R.id.login_progressbar);

        mLoginLayout = (AccountLoginLayout) findViewById(R.id.account_login_layout);
        int paddingHor = (int) (TvApplication.pixelWidth / 30.45);
        LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        int marginVer = (int) (TvApplication.pixelHeight / 6);
        contentParams.setMargins(0, marginVer, 0, marginVer);
        mLoginLayout.setLayoutParams(contentParams);
        mLoginLayout.setPadding(paddingHor, 20, paddingHor, 20);
        mLoginLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

        mLoginedLayout = (AccountLoginedLayout) findViewById(R.id.account_input_logout_layout);
        mLoginedLayout.setLayoutParams(contentParams);
        mLoginedLayout.setPadding(paddingHor, 20, paddingHor, 20);
        mLoginedLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

        UserInfo userInfo = new UserInfoFactory(mContext).getLoginedUserInfo();
        if (userInfo != null) {
            toLoginedStatus(userInfo);
        } else {
            toLoginStatus();
        }
    }

    /**
     * 进入未登录状态
     */
    public void toLoginStatus() {
        mProgressBar.setVisibility(GONE);
        mLoginLayout.setVisibility(VISIBLE);
        mLoginedLayout.setVisibility(GONE);
    }
    
    /**
     * 进入登录中状态
     */
    public void toLoginLoadingStatus() {
        mProgressBar.setVisibility(VISIBLE);
    }
    
    /**
     * 进入已登录状态
     * @param userInfo
     */
    public void toLoginedStatus(UserInfo userInfo) {
        mProgressBar.setVisibility(GONE);
        mLoginedLayout.initView(userInfo);
        mLoginLayout.setVisibility(GONE);
        mLoginedLayout.setVisibility(VISIBLE);
    }

}

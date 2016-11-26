package com.iptv.rocky.view.setting;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.local.UserInfoFactory;
import com.iptv.common.passport.UserInfo;
import com.iptv.common.passport.UserLoginInfo;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.ActivityStack;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.AppCommonUtils;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class AccountLoginLayout extends LinearLayout {
    private Context mContext;
    private AccountInputLayout mInputLayout;
//    private LoginFactory loginFactory;

    private EditText mUsernameInputView;
    private EditText mPasswordInputView;
    private Button mSubmitBtn;
    private TextViewDip mLoginTipsView;

    private UserInfoFactory userInfoFactory;

    private String mStrContent;

    public AccountLoginLayout(Context context) {
    	this(context, null, 0);
    }

    public AccountLoginLayout(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }

    public AccountLoginLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        mContext = context;
//        loginFactory = new LoginFactory();
//        loginFactory.setHttpEventHandler(new HttpEventHandler<UserLoginInfo>() {
//            @Override
//            public void HttpSucessHandler(UserLoginInfo result) {
//            	if (result.errCode > 0) {
//					loginFailure(result.message);
//				} else {
//					loginSuccess(result.userInfo);	
//					/**
//					 * 当未登录用户登录成功后，转到vip详情页
//					 */
//					if (Constants.cVipLoginSuccess.equals(content)) {
//						ActivityStack.popCurrent();
//					} else {
//						((AccountMasterLayout) (AccountLoginLayout.this.getParent())).toLoginedStatus(result.userInfo);
//					}
//				}
//            }
//
//            @Override
//            public void HttpFailHandler() {
//                loginFailure(getResources().getString(R.string.account_login_failure));
//            }
//        });

        userInfoFactory = new UserInfoFactory(context);
    }
    
    public void destroy() {
//    	loginFactory.cancel();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mInputLayout = (AccountInputLayout) findViewById(R.id.account_input_login_layout);
        mInputLayout.setPadding((int) (TvApplication.pixelWidth / 38), 4, 4, 4);
        mInputLayout.getLayoutParams().width = (int) (TvApplication.pixelWidth / 2);

        mUsernameInputView = (EditText) findViewById(R.id.account_username_input);
        mPasswordInputView = (EditText) findViewById(R.id.account_password_input);
        mLoginTipsView = (TextViewDip) findViewById(R.id.account_login_tips);
        mSubmitBtn = (Button) findViewById(R.id.account_input_submit_btn);
        mSubmitBtn.setOnClickListener(submitListener);
    }

    private OnClickListener submitListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = mUsernameInputView.getText().toString();
            String password = mPasswordInputView.getText().toString();
            loginPassword(username, password);
        }
    };

    /**
     * 用户手动输入用户名密码登录
     * @param username
     * @param password
     */
    private void loginPassword(String username,String password){
        if (username.length() == 0) {
            loginFailure(getResources().getString(R.string.account_username_empty));
            return;
        }
        if (password.length() == 0) {
            loginFailure( getResources().getString(R.string.account_password_empty));
            return;
        }
        loginLoading();
        mSubmitBtn.setClickable(false);
//        loginFactory.DownloaDatas(LoginFactory.TYPE_LOGIN_PASSWORD,username, password, TvApplication.mAppVersionName, TvApplication.mDeviceID);
    }

    /**
     * 远程登录通过用户名、token登录
     * @param username
     * @param token
     */
    public void loginToken(String username,String token) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(token)) {
            loginFailure(getResources().getString(R.string.account_remote_data_err));
            return;
        }
        loginLoading();
        mSubmitBtn.setClickable(false);
//        loginFactory.DownloaDatas(LoginFactory.TYPE_LOGIN_TOKEN, username, token, TvApplication.mAppVersionName, TvApplication.mDeviceID);
    }

    private void loginLoading() {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mUsernameInputView.getWindowToken(),0);
        ((AccountMasterLayout) (this.getParent())).toLoginLoadingStatus();
    }

    private void loginFailure(String message) {
        LogUtils.i("user_login", "login failure -> " + message);
        mSubmitBtn.setClickable(true);
        mLoginTipsView.setVisibility(VISIBLE);
        mLoginTipsView.setText(message);
        mLoginTipsView.setTextSize(TvApplication.dpiHeight / 28);
        mLoginTipsView.setClickable(true);
        ((AccountMasterLayout)(this.getParent())).toLoginStatus();
    }
    
    private void loginSuccess(UserInfo userInfo) {
    	LogUtils.i("user_login", "login success");
        mSubmitBtn.setClickable(true);
        mLoginTipsView.setVisibility(View.INVISIBLE);
        userInfoFactory.saveLoginedUserInfo(userInfo);
        AppCommonUtils.showToast(getContext(), getContext().getResources().getString(R.string.account_login_success));
        mStrContent = ((AccountMasterLayout) (this.getParent())).getContent();
    }
}

package com.iptv.rocky.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class AccountInputLayout extends LinearLayout {
	private TextViewDip mUsernameTitleView;
	private EditText mUsernameInputView;
	private TextViewDip mPasswordTitleView;
	private EditText mPasswordInputView;
	private Button mSubmitBtn;
	private TextViewDip mLoginTipsView;

	public AccountInputLayout(Context context) {
		this(context, null, 0);
	}

	public AccountInputLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AccountInputLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		int textSize = (int) (TvApplication.dpiHeight / 26);
		int inputHeight = (int) (TvApplication.pixelHeight / 12);
		int inputWidth = (int) (TvApplication.pixelWidth / 3.6);
		int space = (int) (TvApplication.pixelHeight / 35);
		int leftMargin = (int) (TvApplication.pixelWidth / 12);
		super.onFinishInflate();

		mUsernameTitleView = (TextViewDip) findViewById(R.id.account_username_title);
		mUsernameTitleView.setTextSize(textSize);
		((LayoutParams) mUsernameTitleView.getLayoutParams()).leftMargin = leftMargin;

		mUsernameInputView = (EditText) findViewById(R.id.account_username_input);
		mUsernameInputView.setHeight(inputHeight);
		mUsernameInputView.setTextSize(textSize);
		((LayoutParams) mUsernameInputView.getLayoutParams()).topMargin = space;
		((LayoutParams) mUsernameInputView.getLayoutParams()).width = inputWidth;
		((LayoutParams) mUsernameInputView.getLayoutParams()).leftMargin = leftMargin;

		mPasswordTitleView = (TextViewDip) findViewById(R.id.account_password_title);
		mPasswordTitleView.setTextSize(textSize);
		((LayoutParams) mPasswordTitleView.getLayoutParams()).topMargin = space;
		((LayoutParams) mPasswordTitleView.getLayoutParams()).leftMargin = leftMargin;

		mPasswordInputView = (EditText) findViewById(R.id.account_password_input);
		mPasswordInputView.setHeight(inputHeight);
		mPasswordInputView.setTextSize(textSize);
		((LayoutParams) mPasswordInputView.getLayoutParams()).topMargin = space;
		((LayoutParams) mPasswordInputView.getLayoutParams()).bottomMargin = (int) (space * 1.8);
		((LayoutParams) mPasswordInputView.getLayoutParams()).width = inputWidth;
		((LayoutParams) mPasswordInputView.getLayoutParams()).leftMargin = leftMargin;

		mSubmitBtn = (Button) findViewById(R.id.account_input_submit_btn);
		mSubmitBtn.setTextSize(textSize);
		mSubmitBtn.setBackgroundResource(R.drawable.setting_btn_bg_normal);

		mLoginTipsView = (TextViewDip) findViewById(R.id.account_login_tips);
		((LayoutParams) mLoginTipsView.getLayoutParams()).topMargin = (int) (space * 1.5);
	}
}

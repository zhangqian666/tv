package com.iptv.rocky.view.setting;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iptv.common.local.UserInfoFactory;
import com.iptv.common.passport.UserInfo;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.PromptDialog;
import com.iptv.rocky.R;

public class AccountLoginedLayout extends LinearLayout {
    private Context mContext;
    private AccountLoginedLayout mLayout;
    private ImageView mUserIconView;
    private LinearLayout mWelcomeLayout;
    private TextView mUsernameView;
    private ImageView mVipIconView;
    private AccountSettingBtn mLogoutBtn;
    private TextView mWelcomeView;

    private PromptDialog logoutDialog;

    public AccountLoginedLayout(Context context) {
        this(context, null, 0);
    }

    public AccountLoginedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccountLoginedLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mLayout = this;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int textSize = (int) (TvApplication.dpiHeight / 26);
        int space = (int) (TvApplication.pixelHeight / 27);
        mUserIconView = (ImageView) findViewById(R.id.account_user_icon);
        mUserIconView.getLayoutParams().height = (int) (TvApplication.pixelHeight / 5.27);
        mUserIconView.getLayoutParams().width = (int) (mUserIconView.getLayoutParams().height / 1.22);
        mUserIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        mWelcomeLayout = (LinearLayout) findViewById(R.id.account_user_welcome);
        mLogoutBtn = (AccountSettingBtn) findViewById(R.id.account_logout_btn);
        mUsernameView = (TextView) findViewById(R.id.account_username);

        mVipIconView = (ImageView) findViewById(R.id.account_vip_icon);
        mVipIconView.getLayoutParams().width = (int) (TvApplication.pixelWidth / 24.36);
        mVipIconView.getLayoutParams().height = (int) (mUserIconView.getLayoutParams().width/1.57);
        mVipIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        mWelcomeView = (TextView) findViewById(R.id.account_welcome_text);

        ((LayoutParams) mWelcomeLayout.getLayoutParams()).topMargin = space;
        ((LayoutParams) mWelcomeLayout.getLayoutParams()).bottomMargin = (int) (space * 2.5);
        ((LayoutParams) mWelcomeView.getLayoutParams()).leftMargin = 6;

        mUsernameView.setTextSize(textSize);
        mWelcomeView.setTextSize(textSize);

        logoutDialog = new PromptDialog(mContext,getResources().getString(R.string.account_logout_dialog_content));

        mLogoutBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.show();
                logoutDialog.getmDialogLayout().mCancelBtn.requestFocus();
                logoutDialog.setOnConfirmListenner(new PromptDialog.OnConfirmListentner() {
                    @Override
                    public void onConfirm() {
                        new UserInfoFactory(mContext).logout();
                        ((AccountMasterLayout) (mLayout.getParent())).toLoginStatus();
                    }
                });
            }
        });
    }

    public void initView(UserInfo userInfo) {
        String username = userInfo.nickname.length() == 0 ? userInfo.username : userInfo.nickname;
        mUsernameView.setText(username);
        Drawable iconDrawable = userInfo.isVipValid ? getResources().getDrawable(R.drawable.account_user_vip) :
                getResources().getDrawable(R.drawable.account_user_normal);
        mUserIconView.setImageDrawable(iconDrawable);
        if (userInfo.isVipValid) {
            mUsernameView.setTextColor(getResources().getColor(R.color.username_vip));
            mVipIconView.setVisibility(VISIBLE);
            mVipIconView.setImageResource(R.drawable.account_vip_icon);
        }else{
            mUsernameView.setTextColor(getResources().getColor(R.color.username_normal));
            mVipIconView.setVisibility(GONE);
        }
        mLogoutBtn.requestFocus();
    }
}

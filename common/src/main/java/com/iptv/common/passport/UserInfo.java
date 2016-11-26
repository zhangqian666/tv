package com.iptv.common.passport;

/**
 * 用户信息
 */
public class UserInfo {
    public String username;
    public String nickname;
    public String token;
    public int vipType;//0普通VIP 1包月VIP
    public boolean isYearVip;
    public String vipValidDate;
    public boolean isVipValid;//是否为有效VIP
}
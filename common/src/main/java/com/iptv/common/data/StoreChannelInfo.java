package com.iptv.common.data;

import java.io.Serializable;

public class StoreChannelInfo extends VodDetailInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3989597534956770791L;

    //sqllite主键ID
    public int id;
    //创建时间
    public long ctime;
}

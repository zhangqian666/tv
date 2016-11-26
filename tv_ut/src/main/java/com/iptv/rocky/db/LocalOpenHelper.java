package com.iptv.rocky.db;

import com.iptv.rocky.hwdata.local.AAALiveChannelsLocalFactory;
import com.iptv.rocky.hwdata.local.LiveAllChannelBillLocalFactory;
import com.iptv.rocky.hwdata.local.LiveTypeLocalFactory;
import com.iptv.rocky.hwdata.local.PortalHomeLiveChannelsLocalFactory;
import com.iptv.rocky.hwdata.local.PortalHomeLocalFactory;
import com.iptv.rocky.hwdata.local.PortalHomeMyHotelTopFactory;
import com.iptv.rocky.hwdata.local.PortalHomeVodTopLocalFactory;
import com.iptv.rocky.hwdata.local.VodHistoryLocalFactory;
import com.iptv.rocky.hwdata.local.VodStoreLocalFactory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalOpenHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "iptv.db";
    private static final int DATABASE_VERSION = 2;
    
    public LocalOpenHelper(Context context) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
        /**
         * 创建我的收藏表
         */
        VodStoreLocalFactory.createDB(db);
        
        /**
         * 创建播放历史表
         */
        VodHistoryLocalFactory.createDB(db);

        /**
         * 创建home页面缓存数据表
         */
        PortalHomeLocalFactory.createDB(db);
        
        /**
         * 创建酒店介绍顶层分类
         */
        PortalHomeMyHotelTopFactory.createDB(db);
        
        /**
         * 创建home页面缓存数据表
         */
        PortalHomeLiveChannelsLocalFactory.createDB(db);
        
        /**
         * 创建home页面缓存数据表
         */
        PortalHomeVodTopLocalFactory.createDB(db);
        
        /**
         * 创建AAA直播数据缓存数据表
         */
        AAALiveChannelsLocalFactory.createDB(db);
        
        /**
         * 创建直播频道节目单数据缓存数据表
         */
        LiveAllChannelBillLocalFactory.createDB(db);
        
        /**
         * 创建直播分类数据缓存数据表
         */
        LiveTypeLocalFactory.createDB(db);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
		} else {
			VodStoreLocalFactory.dropTable(db, VodStoreLocalFactory.tableName);
			VodHistoryLocalFactory.dropTable(db, VodHistoryLocalFactory.tableName);
			PortalHomeLocalFactory.dropTable(db, PortalHomeLocalFactory.tableName);
			PortalHomeVodTopLocalFactory.dropTable(db, PortalHomeVodTopLocalFactory.tableName);
			PortalHomeLiveChannelsLocalFactory.dropTable(db, PortalHomeLiveChannelsLocalFactory.tableName);
			AAALiveChannelsLocalFactory.dropTable(db, AAALiveChannelsLocalFactory.tableName);
			LiveAllChannelBillLocalFactory.dropTable(db, LiveAllChannelBillLocalFactory.tableName);
			LiveTypeLocalFactory.dropTable(db, LiveTypeLocalFactory.tableName);
			onCreate(db);
		}
	}
}

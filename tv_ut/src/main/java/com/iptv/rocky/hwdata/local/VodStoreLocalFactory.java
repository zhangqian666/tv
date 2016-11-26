package com.iptv.rocky.hwdata.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.StoreChannelInfo;
import com.iptv.common.utils.DataReloadUtil;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.db.LocalFactoryBase;

public class VodStoreLocalFactory extends LocalFactoryBase<StoreChannelInfo> {
	
	public final static String tableName = "iptv_channel_store";

    public static void createDB(SQLiteDatabase db) {
    	db.execSQL(String.format("create table if not exists %s (", tableName) + 
                "_id integer primary key," +
                "vid bigint," +
                "title varchar," +
                "platform varchar,"+
                "imgurl varchar," +
                "ctime integer)");
    }

    public VodStoreLocalFactory(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected String getprimaryKey() {
        return "_id";
    }

    @Override
    protected String getOrderColumnName() {
        return "_id";
    }

    @Override
	protected long getMaxCount() {
		return 100;
	}
    
	@Override
    protected StoreChannelInfo createModel(Cursor cursor) {
        StoreChannelInfo channel = new StoreChannelInfo();
        channel.id = cursor.getInt(cursor.getColumnIndex("_id"));
        channel.ctime = cursor.getLong(cursor.getColumnIndex("ctime"));
        channel.VODID = cursor.getString(cursor.getColumnIndex("vid"));
        channel.VODNAME = cursor.getString(cursor.getColumnIndex("title"));
        LogUtils.info("从本地数据库提取的平台:"+cursor.getString(cursor.getColumnIndex("platform")));
        channel.platform = EnumType.Platform.createPlatform(cursor.getString(cursor.getColumnIndex("platform")));
        channel.PICPATH = cursor.getString(cursor.getColumnIndex("imgurl"));
        return channel;
    }

    @Override
    protected void insertRecord(SQLiteDatabase db, StoreChannelInfo record) {
    	LogUtils.info("Platform "+record.platform);
    	String sql = String.format("insert into %s (_id,vid,title,platform,imgurl,ctime) values(?,?,?,?,?,?)", getTableName());
        db.execSQL(sql, new Object[]{ null, record.VODID, record.VODNAME,record.platform,record.PICPATH,record.ctime });
    }

    /**
     * 保存历史记录
     * @param storeChannelInfo
     */
    public void saveStore(StoreChannelInfo storeChannelInfo) {
        deleteByChannelId(storeChannelInfo.VODID);
        LogUtils.info("要保存的平台:"+storeChannelInfo.platform);
        insertRecord(storeChannelInfo);
        DataReloadUtil.addMessage(DataReloadUtil.HomeStoreViewID);
    }

    /**
     * 根据channelid删除历史记录
     * @param channelId
     */
    public void deleteByChannelId(String channelId){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = String.format("delete from %s where vid=?", getTableName());
        db.execSQL(sql, new String[] {channelId});
        db.close();
        DataReloadUtil.addMessage(DataReloadUtil.HomeStoreViewID);
    }

    @Override
    public void deletedRecords() {
        super.deletedRecords();
        DataReloadUtil.addMessage(DataReloadUtil.HomeStoreViewID);
    }

	@Override
	protected int updateRecord(SQLiteDatabase db, StoreChannelInfo record) {
		return 0;
	}

}

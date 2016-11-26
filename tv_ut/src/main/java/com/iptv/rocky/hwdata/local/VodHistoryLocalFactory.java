package com.iptv.rocky.hwdata.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.utils.DataReloadUtil;
import com.iptv.rocky.db.LocalFactoryBase;
import com.iptv.rocky.model.TvApplication;

public class VodHistoryLocalFactory extends LocalFactoryBase<HistoryChannelInfo> {
	
	public final static String tableName = "iptv_channel_history";

	public static void createDB(SQLiteDatabase db) {
		db.execSQL(String.format("create table if not exists %s (", tableName)
				+ "_id integer primary key," 
				+ "channelid bigint,"
				+ "title varchar," 
				+ "imgurl varchar,"
				+ "vid bigint," 
				+ "subtitle varchar," 
				+ "platform varchar,"
				+ "playposition int,"
				+ "ctime integer)");
	}

	public VodHistoryLocalFactory(Context context) {
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
	protected HistoryChannelInfo createModel(Cursor cursor) {
		HistoryChannelInfo channel = new HistoryChannelInfo();
		channel.id = cursor.getInt(cursor.getColumnIndex("_id"));
		channel.ctime = cursor.getLong(cursor.getColumnIndex("ctime"));
		channel.VODID = cursor.getString(cursor.getColumnIndex("vid"));
		channel.channelid = cursor.getString(cursor.getColumnIndex("channelid"));
		channel.subtitle = cursor.getString(cursor.getColumnIndex("subtitle"));
		channel.platform = EnumType.Platform.createPlatform(cursor.getString(cursor.getColumnIndex("platform")));
		channel.playposition = cursor.getInt(cursor.getColumnIndex("playposition"));
		channel.VODNAME = cursor.getString(cursor.getColumnIndex("title"));
		channel.PICPATH = cursor.getString(cursor.getColumnIndex("imgurl"));
		return channel;
	}

	@Override
	protected void insertRecord(SQLiteDatabase db, HistoryChannelInfo record) {
		String sql = String.format("insert into %s (_id,channelid,title,imgurl,vid,subtitle,platform,playposition,ctime) values(?,?,?,?,?,?,?,?,?)", getTableName());
		db.execSQL(sql, new Object[] { null,
				record.channelid, record.VODNAME, record.PICPATH,
				record.VODID, record.subtitle, record.platform.toString(),record.playposition, record.ctime });
	}

	/**
	 * 保存历史记录
	 * 
	 * @param historyChannelInfo
	 */
	public void saveHistory(HistoryChannelInfo historyChannelInfo) {
		deleteByChannelId(historyChannelInfo.channelid);
		insertRecord(historyChannelInfo);
		DataReloadUtil.addMessage(DataReloadUtil.HomeHistoryViewID);
	}

	/**
	 * 根据channelid获取历史记录
	 */
	public HistoryChannelInfo getHistoryById(String channelId) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = String.format("select * from %s where channelid=?",
					getTableName());
			cursor = db.rawQuery(sql, new String[] { channelId + ""});
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					return createModel(cursor);
				}
			}
			return null;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * 根据channelid删除历史记录
	 * 
	 * @param channelId
	 */
	public void deleteByChannelId(String channelId) {
		SQLiteDatabase db = null;
			try {
				db = helper.getReadableDatabase();
				String sql = String.format("delete from %s where channelid=?",
						getTableName());
				db.execSQL(sql, new String[] { channelId });
				DataReloadUtil.addMessage(DataReloadUtil.HomeHistoryViewID);
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
		
	

	@Override
	public void deletedRecords() {
		super.deletedRecords();
		DataReloadUtil.addMessage(DataReloadUtil.HomeHistoryViewID);
	}

	@Override
	protected int updateRecord(SQLiteDatabase db, HistoryChannelInfo record) {
		// TODO Auto-generated method stub
		return 0;
	}
}

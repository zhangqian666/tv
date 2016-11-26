package com.iptv.rocky.hwdata.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.data.ProgBill;
import com.iptv.rocky.db.LocalFactoryBase;

public class LiveChannelBillLocalFactory extends LocalFactoryBase<LiveChannelBill> {
	
	private String mChannelId;
	
	public static final String tableName = "livechannelbill_store";
	
	public LiveChannelBillLocalFactory(Context context) {
		super(context);
	}
	
	public static void createDB(SQLiteDatabase db) {
		db.execSQL(String.format("create table if not exists %s (", tableName) +
                "_id integer primary key," +
                "channelid integer," +
                "begintime varchar," +
                "endtime varchar," +
                "title varchar," +
                "programid varchar," +
                "begindate varchar)");
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
	protected LiveChannelBill createModel(Cursor cursor) {
		return null;
	}

	private ProgBill createModelBill(Cursor cursor) {
		ProgBill info = new ProgBill();
		info.programId = cursor.getString(cursor.getColumnIndex("programid"));
		info.title = cursor.getString(cursor.getColumnIndex("title"));
		info.beginDate = cursor.getString(cursor.getColumnIndex("begindate"));
		info.beginTime = cursor.getString(cursor.getColumnIndex("begintime"));
		info.endTime = cursor.getString(cursor.getColumnIndex("endtime"));
		
		return info;
	}
	
	@Override
	protected void insertRecord(SQLiteDatabase db, LiveChannelBill info) {
		
		for (ProgBill bill : info.lstProgBill)
		{
			String sql = String.format("insert into %s (_id,channelid,programid,begindate,begintime,endtime,title) values(?,?,?,?,?,?,?)", getTableName());			
			Object[] os = new Object[] {null, mChannelId, bill.programId, bill.beginDate, bill.beginTime, bill.endTime, bill.title};
			db.execSQL(sql, os);
		}
	}
	
	public void insertBillInfos(LiveChannelBill list) {
		if (list != null) {
			mChannelId = list.ChannelID;
			deleteByChannelId();
			insertRecord(list);
		}
	}
	
	//删除旧的数据
    public void deleteByChannelId() {
    	SQLiteDatabase db = null;
    	try {
    		db = helper.getReadableDatabase();	
    		String sql = String.format("delete from %s where channelid=?", getTableName());
            db.execSQL(sql, new String[] {mChannelId});
    	} finally {
    		if (db != null) {
    			db.close();
    		}
    	}
    }
	
	public LiveChannelBill getLiveChannelsByChannelId(String nChannelId) {
		LiveChannelBill ret = new LiveChannelBill();
		ret.ChannelID = nChannelId;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			if (nChannelId!=null && !nChannelId.isEmpty())
			{
				String sql = String.format("select * from %s where channelid=?", getTableName());
				cursor = db.rawQuery(sql, new String[]{nChannelId});
				if (cursor != null) {
					while (cursor.moveToNext()) {
						ret.lstProgBill.add(createModelBill(cursor));
					}
				} else {
					return null;
				}
			}
			else
			{
				String sql = String.format("select * from %s", getTableName());
				cursor = db.rawQuery(sql, new String[]{});
				if (cursor != null) {
					while (cursor.moveToNext()) {
						ret.lstProgBill.add(createModelBill(cursor));
					}
				} else {
					return null;
				}
			}
			
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return ret;
	}

	@Override
	protected int updateRecord(SQLiteDatabase db, LiveChannelBill record) {
		// TODO Auto-generated method stub
		return 0;
	}

}

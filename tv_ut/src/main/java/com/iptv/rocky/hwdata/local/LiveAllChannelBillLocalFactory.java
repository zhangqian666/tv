package com.iptv.rocky.hwdata.local;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.data.ProgBill;
import com.iptv.rocky.db.LocalFactoryBase;

public class LiveAllChannelBillLocalFactory extends LocalFactoryBase<ArrayList<LiveChannelBill>> {
	
	public static final String tableName = "liveallchannelbill_store";
	
	public LiveAllChannelBillLocalFactory(Context context) {
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
	protected ArrayList<LiveChannelBill> createModel(Cursor cursor) {
		return null;
	}

	private void createModelBill(Cursor cursor, ArrayList<LiveChannelBill> list) {
		String nChannelID = cursor.getString(cursor.getColumnIndex("channelid"));
		ProgBill info = new ProgBill();
		info.programId = cursor.getString(cursor.getColumnIndex("programid"));
		info.title = cursor.getString(cursor.getColumnIndex("title"));
		info.beginDate = cursor.getString(cursor.getColumnIndex("begindate"));
		info.beginTime = cursor.getString(cursor.getColumnIndex("begintime"));
		info.endTime = cursor.getString(cursor.getColumnIndex("endtime"));
		
		boolean bFind = false;
		for (LiveChannelBill liveChannelBill : list)
		{
			if (liveChannelBill.ChannelID.equals(nChannelID))
			{
				bFind = true;
				liveChannelBill.lstProgBill.add(info);
				return;
			}
		}
		
		if (!bFind)
		{
			LiveChannelBill liveChannelBill = new LiveChannelBill();
			liveChannelBill.ChannelID = nChannelID;
			liveChannelBill.lstProgBill.add(info);
			list.add(liveChannelBill);
		}
	}
	
	@Override
	protected void insertRecord(SQLiteDatabase db, ArrayList<LiveChannelBill> list) {
		for (LiveChannelBill liveChannelBill : list)
		{
			for (ProgBill bill : liveChannelBill.lstProgBill)
			{
				String sql = String.format("insert into %s (_id,channelid,programid,begindate,begintime,endtime,title) values(?,?,?,?,?,?,?)", getTableName());			
				Object[] os = new Object[] {null, liveChannelBill.ChannelID, bill.programId, bill.beginDate, bill.beginTime, bill.endTime, bill.title};
				db.execSQL(sql, os);
			}
		}
	}
	
	public void insertBillInfos(ArrayList<LiveChannelBill> list) {
		if (list != null && list.size() > 0) {
			deletedRecords();
			insertRecord(list);
		}
	}
	
	public boolean isExit(String checkDate)
	{
		ArrayList<LiveChannelBill> ret = new ArrayList<LiveChannelBill>();
		
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = String.format("select * from %s where begindate=?", getTableName());
			cursor = db.rawQuery(sql, new String[]{checkDate});
			if (cursor != null) {
				while (cursor.moveToNext()) {
					createModelBill(cursor, ret);
					
					if (ret != null && ret.size() > 0)
						return true;
				}
			} else {
				return false;
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return false;
	}
	
	public ArrayList<LiveChannelBill> getLiveChannels() {
		ArrayList<LiveChannelBill> ret = new ArrayList<LiveChannelBill>();
		
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = String.format("select * from %s", getTableName());
			cursor = db.rawQuery(sql, new String[]{});
			if (cursor != null) {
				while (cursor.moveToNext()) {
					createModelBill(cursor, ret);
				}
			} else {
				return null;
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
	protected int updateRecord(SQLiteDatabase db,
			ArrayList<LiveChannelBill> record) {
		// TODO Auto-generated method stub
		return 0;
	}

}

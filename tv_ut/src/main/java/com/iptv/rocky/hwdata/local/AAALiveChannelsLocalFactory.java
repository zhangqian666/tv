package com.iptv.rocky.hwdata.local;

import java.util.ArrayList;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.LiveChannel;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.db.LocalFactoryBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AAALiveChannelsLocalFactory extends LocalFactoryBase<LiveChannel> {
	
	public static EnumType.Platform platform = EnumType.Platform.HUAWEI;
	
	public static final String tableName = "aaalivechannels_store";
	
	public AAALiveChannelsLocalFactory(Context context) {
		super(context);
	}
	
	public static void createDB(SQLiteDatabase db) {
		db.execSQL(String.format("create table if not exists %s (", tableName) +
                "_id varchar primary key," +
                "channelid varchar," +
                "platform varchar," +
                "userchannelid integer," +
                "title varchar," + 
                "channelurl varchar," + 
                "timeshifturl varchar," + 
                "channeltype integer," +
                "channelpurchased integer," +
                "timeshift integer," +
                "timeshiftlength integer)");
	}

	@Override
	protected String getTableName() {
		return tableName;
	}

	@Override
	protected String getprimaryKey() {
		return "_id";
	}
	
	public void createDB(){
		createDB(this.helper.getReadableDatabase());
	}
	
    public void dropTable(){
    	
    	LocalFactoryBase.dropTable(this.helper.getReadableDatabase(), tableName);
    }
	
	@Override
	protected LiveChannel createModel(Cursor cursor) {
		LiveChannel info = new LiveChannel();
//		LogUtils.error("indeb platform:"+cursor.getString(cursor.getColumnIndex("platform")));
		info.platform = EnumType.Platform.createPlatform(cursor.getString(cursor.getColumnIndex("platform")));
//		LogUtils.debug("Platform:"+info.platform+""+ cursor.getColumnIndex("channelidzte"));
		info.ChannelID = cursor.getString(cursor.getColumnIndex("channelid"));
		info.UserChannelID = cursor.getInt(cursor.getColumnIndex("userchannelid"));
		info.ChannelName = cursor.getString(cursor.getColumnIndex("title"));
		info.ChannelURL = cursor.getString(cursor.getColumnIndex("channelurl"));
		info.TimeShiftURL = cursor.getString(cursor.getColumnIndex("timeshifturl"));
		info.ChannelType = cursor.getInt(cursor.getColumnIndex("channeltype"));
		info.ChannelPurchased = cursor.getInt(cursor.getColumnIndex("channelpurchased")) == 1 ? true : false;
		info.TimeShift = cursor.getInt(cursor.getColumnIndex("timeshift")) == 1 ? true : false;
		info.TimeShiftLength =cursor.getInt(cursor.getColumnIndex("timeshiftlength"));
		return info;
	}

	@Override
	protected void insertRecord(SQLiteDatabase db, LiveChannel info) {
		String sql = String.format("insert into %s (_id,channelid,platform,userchannelid,title,channelurl,timeshifturl,channeltype,channelpurchased,timeshift, timeshiftlength) values(?,?,?,?,?,?,?,?,?,?,?)", getTableName());
			Object[] os = new Object[] {info.ChannelID, info.ChannelID,EnumType.Platform.createPlatform(info.platform),info.UserChannelID, info.ChannelName,info.ChannelURL, info.TimeShiftURL, info.ChannelType, info.ChannelPurchased, info.TimeShift, info.TimeShiftLength };
			db.execSQL(sql, os);

	}
	
	@Override
	protected int updateRecord(SQLiteDatabase db, LiveChannel info) {
	    ContentValues cv = new ContentValues();  
		cv.put("_id", info.ChannelID);
	    cv.put("channelid" , info.ChannelID); 
	    cv.put("platform" ,EnumType.Platform.createPlatform(info.platform));  
	    cv.put("userchannelid" , info.UserChannelID);  
	    cv.put("title" , info.ChannelName);  
	    cv.put("channelurl" , info.ChannelURL);  
	    cv.put("timeshifturl" , info.TimeShiftURL);  
	    cv.put("channeltype" , info.ChannelType); 
	    cv.put("channelpurchased" , info.ChannelPurchased);  
	    cv.put("timeshift" , info.TimeShift);  
	    cv.put("timeshiftlength", info.TimeShiftLength);
	    String[] args = {String.valueOf(info.ChannelID)};  
	    return db.update(getTableName(), cv, "_id=?" ,args);   
	}
	
	public void insertHomeInfos(ArrayList<LiveChannel> list) {
		
		if (list != null && list.size() > 0) {
			deletedRecords();
			insertRecord(list);
		}
	}
	
	public void updateHomeInfos(ArrayList<LiveChannel> list) {
		if (list != null && list.size() > 0) {
			for (LiveChannel liveChannel :list) {
				
					LiveChannel channelFind = findRecordZ(liveChannel.ChannelID);
					if (channelFind != null) {
						channelFind.TimeShiftURL = liveChannel.TimeShiftURL;
						updateRecord(channelFind);
					} else {
						insertRecord(liveChannel);
					}
			}
		}
	}
	
	public ArrayList<LiveChannel> getAAALiveChannelListInfos() {
		ArrayList<LiveChannel> ret = new ArrayList<LiveChannel>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = String.format("select * from %s order by userchannelid asc", getTableName());
			cursor = db.rawQuery(sql, new String[]{});
			if (cursor != null) {
				while (cursor.moveToNext()) {
					ret.add(createModel(cursor));
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

}

package com.iptv.rocky.hwdata.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.PortalChannels;
import com.iptv.common.data.PortalLiveChannel;
import com.iptv.common.data.PortalLiveType;
import com.iptv.rocky.db.LocalFactoryBase;

public class PortalHomeLiveChannelsLocalFactory extends LocalFactoryBase<PortalChannels> {
	
	private int mIndex;
	
	public static final String tableName = "portalhomelivechannels_store";
	
	public PortalHomeLiveChannelsLocalFactory(Context context) {
		super(context);
	}
	
	public void setIndex(int index) {
		mIndex = index;
	}

	/**
	 * position字段
	 * 0:首页的第0页;
	 * */
	public static void createDB(SQLiteDatabase db) {
		db.execSQL(String.format("create table if not exists %s (", tableName) +
                "_id integer primary key," +
				"position integer," +
                "id integer," +
                "channelid integer," +
                "typeid integer," +
                "contenttype varchar," +
                "layouttype varchar," +
                "bgtype integer," +
                "bg varchar," +
                "title varchar," +
                "icon varchar)");
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
	protected PortalChannels createModel(Cursor cursor) {
		return null;
	}
	
	private PortalLiveChannel createModelPortalLiveChannel(Cursor cursor) {
		PortalLiveChannel info = new PortalLiveChannel();
		info.id = cursor.getInt(cursor.getColumnIndex("id"));
		info.ChannelID = cursor.getString(cursor.getColumnIndex("channelid"));
		info.content_type = EnumType.ContentType.convertStr(cursor.getString(cursor.getColumnIndex("contenttype")));
		info.layout_type = EnumType.LayoutType.convertStr(cursor.getString(cursor.getColumnIndex("layouttype")));
		info.bg = cursor.getString(cursor.getColumnIndex("bg"));
		info.backGroundType = EnumType.BackGroundType.convertInt(cursor.getInt(cursor.getColumnIndex("bgtype")));
		return info;
	}
	
	private PortalLiveType createModelPortalLiveType(Cursor cursor) {
		PortalLiveType info = new PortalLiveType();
		info.id = cursor.getInt(cursor.getColumnIndex("id"));
		info.typeId = cursor.getInt(cursor.getColumnIndex("typeid"));
		info.content_type = EnumType.ContentType.convertStr(cursor.getString(cursor.getColumnIndex("contenttype")));
		info.layout_type = EnumType.LayoutType.convertStr(cursor.getString(cursor.getColumnIndex("layouttype")));
		info.bg = cursor.getString(cursor.getColumnIndex("bg"));
		info.title = cursor.getString(cursor.getColumnIndex("title"));
		info.icon = cursor.getString(cursor.getColumnIndex("icon"));
		info.backGroundType = EnumType.BackGroundType.convertInt(cursor.getInt(cursor.getColumnIndex("bgtype")));
		return info;
	}

	@Override
	protected void insertRecord(SQLiteDatabase db, PortalChannels obj) {
		String sql = String.format("insert into %s (_id,position,id,channelid,typeid,contenttype,layouttype,bgtype,bg,title,icon) values(?,?,?,?,?,?,?,?,?,?,?)", getTableName());
		{
			for (PortalLiveChannel info : obj.lstPortalLiveChannel)
			{
				Object[] os = new Object[] {null, mIndex, info.id, info.ChannelID, null, EnumType.converContentType(info.content_type), 
						EnumType.converLayoutType(info.layout_type), EnumType.convertBackgroundType(info.backGroundType), info.bg, info.ChannelName, null};
				db.execSQL(sql, os);
			}
		}
		
		{
		for (PortalLiveType info : obj.lstPortalLiveType)
		{
			Object[] os = new Object[] {null, mIndex, info.id, null, info.typeId, EnumType.converContentType(info.content_type), 
					EnumType.converLayoutType(info.layout_type), info.backGroundType, info.bg, info.title, info.icon};
			db.execSQL(sql, os);
		}
		}
	}
	
	public void insertLiveChannelInfos(PortalChannels info) {
		if (info != null) {
			deleteByIndex(mIndex);
			insertRecord(info);
		}
	}
	
	// 删除旧的数据
    public void deleteByIndex(int index) {
    	SQLiteDatabase db = null;
    	try {
    		db = helper.getReadableDatabase();	
    		String sql = String.format("delete from %s where position=?", getTableName());
            db.execSQL(sql, new Integer[] {index});
    	} finally {
    		if (db != null) {
    			db.close();
    		}
    	}
    }
	
	public PortalChannels getHomeInfosByIndex(int index) {
		PortalChannels ret = new PortalChannels();
		String indexStr = String.valueOf(index);
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = String.format("select * from %s where position=?", getTableName());
			cursor = db.rawQuery(sql, new String[]{indexStr});
			if (cursor != null) {
				while (cursor.moveToNext()) {
					if (cursor.getString(cursor.getColumnIndex("contenttype")).equalsIgnoreCase(EnumType.converContentType(EnumType.ContentType.LIVE_CHANNEL)))
						ret.lstPortalLiveChannel.add(createModelPortalLiveChannel(cursor));
					else if (cursor.getString(cursor.getColumnIndex("contenttype")).equalsIgnoreCase(EnumType.converContentType(EnumType.ContentType.LIVE_TYPE)))
						ret.lstPortalLiveType.add(createModelPortalLiveType(cursor));
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
	protected int updateRecord(SQLiteDatabase db, PortalChannels record) {
		// TODO Auto-generated method stub
		return 0;
	}

}

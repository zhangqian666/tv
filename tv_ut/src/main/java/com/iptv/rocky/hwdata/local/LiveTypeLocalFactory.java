package com.iptv.rocky.hwdata.local;

import java.util.ArrayList;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.PortalLiveType;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.db.LocalFactoryBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class LiveTypeLocalFactory extends LocalFactoryBase<ArrayList<PortalLiveType>> {

	public static final String tableName = "livetype_store";
	private static final int maxTotalTypeCount = 7;

	public LiveTypeLocalFactory(Context context) {
		super(context);
	}

	public static void createDB(SQLiteDatabase db) {
		db.execSQL(String.format("create table if not exists %s (", tableName) +
				"_id integer primary key," +
				"position integer," +
				"typeid integer," +
				"title varchar," +
				"bgtype integer," +
				"bg varchar," +
				"channelids varchar)");
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
	protected ArrayList<PortalLiveType> createModel(Cursor cursor) {
		return null;
	}

	@Override
	protected void insertRecord(SQLiteDatabase db, ArrayList<PortalLiveType> list) {
		for (PortalLiveType info : list) {

			if (info.typeId == -1 || TextUtils.isEmpty(info.title)
					|| info.lstChannelIds == null || info.lstChannelIds.size() == 0)
				continue;

			String sql = String.format("insert into %s (_id,position,typeid,title,bgtype,bg,channelids) values(?,?,?,?,?,?,?)", getTableName());
			String channelids = "";
			int size = info.lstChannelIds.size();
			//LogUtils.debug("要保存的直播分类id大小"+size);
			for (int i = 0; i < size; i++) {
				if (i == size - 1)
					channelids += info.lstChannelIds.get(i);
				else
					channelids += (info.lstChannelIds.get(i) + ",");
			}
			//LogUtils.info("要保存的分类id数据"+channelids + ";typeId:"+info.typeId+" title:"+info.title + "  数据库名字:"+getTableName());
			Object[] os = new Object[] {null, info.seq, info.typeId,info.title, info.backGroundType, info.bg, channelids};
			db.execSQL(sql, os);
		}
	}


	public void insertLiveTypeInfos(ArrayList<PortalLiveType> list) {
		if (list != null) {
			deletedRecords();
			insertRecord(list);
		}
	}

	public ArrayList<PortalLiveType> getLiveType() {
		ArrayList<PortalLiveType> ret = new ArrayList<PortalLiveType>();

		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			for (int i = 1; i <= maxTotalTypeCount; i++) {
				String sql = String.format("select * from %s where typeId=?", getTableName());
				cursor = db.rawQuery(sql, new String[]{i+""});
				if (cursor != null) {
					while (cursor.moveToNext()) {
						PortalLiveType liveType = new PortalLiveType();
						liveType.id = cursor.getInt(cursor.getColumnIndex("_id"));
						liveType.seq = cursor.getInt(cursor.getColumnIndex("position"));
						liveType.typeId = cursor.getInt(cursor.getColumnIndex("typeid"));
						liveType.title = cursor.getString(cursor.getColumnIndex("title"));
						liveType.backGroundType = EnumType.BackGroundType.convertInt(cursor.getInt(cursor.getColumnIndex("bgtype")));
						liveType.bg = cursor.getString(cursor.getColumnIndex("bg"));
						String[] strs = cursor.getString(cursor.getColumnIndex("channelids")).split(",");
						for (int j = 0; j < strs.length; j++) {
							liveType.lstChannelIds.add(strs[j]);
						}
						LogUtils.debug("添加分类:"+liveType.typeId+" "+liveType.title);
						ret.add(liveType);
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
	protected int updateRecord(SQLiteDatabase db,
			ArrayList<PortalLiveType> record) {
		// TODO Auto-generated method stub
		return 0;
	}

}

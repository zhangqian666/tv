package com.iptv.rocky.hwdata.local;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.PortalHome;
import com.iptv.rocky.db.LocalFactoryBase;

public class PortalHomeLocalFactory extends LocalFactoryBase<PortalHome> {

	private int mIndex;

	public static final String tableName = "portalhome_store";

	public PortalHomeLocalFactory(Context context) {
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
				"platform varchar," +
				"parentId varchar," +
				"programId varchar," +
                "contentid varchar," +
                "contenttype varchar," +
                "layouttype varchar," +
                "bgtype varchar," +
                "bg varchar," +
                "title varchar," +
                "icon varchar," +
				"imgs varchar)");
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
	protected PortalHome createModel(Cursor cursor) {
		PortalHome info = new PortalHome();
		info.id = cursor.getInt(cursor.getColumnIndex("id"));
		info.platform =  EnumType.Platform.createPlatform(cursor.getString(cursor.getColumnIndex("platform")));
		if(cursor.getColumnIndex("parentId")>0){
			info.parentId=cursor.getString(cursor.getColumnIndex("parentId"));
		}
		if(cursor.getColumnIndex("programId")>0){
			info.programId=cursor.getString(cursor.getColumnIndex("programId"));
		}
		if(cursor.getColumnIndex("contentid")>0){
			info.content_id = cursor.getString(cursor.getColumnIndex("contentid"));
		}
		info.content_type = EnumType.ContentType.convertStr(cursor.getString(cursor.getColumnIndex("contenttype")));
		info.layout_type = EnumType.LayoutType.convertStr(cursor.getString(cursor.getColumnIndex("layouttype")));
		info.bgType = EnumType.BackGroundType.createType(cursor.getInt(cursor.getColumnIndex("bgtype")));
		info.bg = cursor.getString(cursor.getColumnIndex("bg"));
		info.title = cursor.getString(cursor.getColumnIndex("title"));
		info.icon = cursor.getString(cursor.getColumnIndex("icon"));

		String str = cursor.getString(cursor.getColumnIndex("imgs"));
		String[] strs = str.split(",");
		int size = strs.length;
		ArrayList<String> list = new ArrayList<String>(size);
		for (int i = 0; i < size; i++) {
			String s = strs[i];
			if (!TextUtils.isEmpty(s)) {
				//如果image url是null的话，人为再变成原来的"".
				if ("null".equals(s)) {
					list.add("");
				} else {
					list.add(strs[i]);
				}
			}
		}
		info.imgs = list;

		return info;
	}

	@Override
	protected void insertRecord(SQLiteDatabase db, PortalHome info) {
		String sql = String.format("insert into %s (_id,position,id,platform,parentId,programId,contentid,contenttype,layouttype,bgtype,bg,title,icon,imgs) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", getTableName());
		ArrayList<String> imgs = info.imgs;
		StringBuilder sb = new StringBuilder();
		if (imgs != null) {
			for (String str : imgs) {
				if (TextUtils.isEmpty(str)) {
					//如果image url是""的话，db存"null",不然在分割字符串时候,取不到"".
					sb.append("null").append(",");
				} else {
					sb.append(str).append(",");
				}
			}
		}

		String sbStr = sb.toString();
		if (!TextUtils.isEmpty(sbStr)) {
			sbStr = sbStr.substring(0, sbStr.length() - 1);
		}

		Object[] os = new Object[] {null, mIndex, info.id, EnumType.Platform.createPlatform(info.platform),info.parentId,info.programId,info.content_id, EnumType.converContentType(info.content_type), 
				EnumType.converLayoutType(info.layout_type),EnumType.convertBackgroundType(info.bgType), info.bg, info.title, info.icon, sbStr};
		db.execSQL(sql, os);
	}

	public void insertHomeInfos(ArrayList<PortalHome> list) {
		if (list != null && list.size() > 0) {
			deleteByIndex(mIndex);
			
			insertRecord(list);
		}
	}

	//删除旧的数据
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

	public ArrayList<PortalHome> getHomeInfosByIndex(int index) {
		ArrayList<PortalHome> ret = new ArrayList<PortalHome>();
		String indexStr = String.valueOf(index);
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = String.format("select * from %s where position=?", getTableName());
			cursor = db.rawQuery(sql, new String[]{indexStr});
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

	@Override
	protected int updateRecord(SQLiteDatabase db, PortalHome record) {
		// TODO Auto-generated method stub
		return 0;
	}

}

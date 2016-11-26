package com.iptv.rocky.hwdata.local;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.MyHotelTop;
import com.iptv.rocky.db.LocalFactoryBase;
/**
 * Hotel introduce entry
 * 
 *
 */
public class PortalHomeMyHotelTopFactory extends LocalFactoryBase<MyHotelTop> {
	
	private int mIndex;
	
	public static final String tableName = "portalhomemyhoteltop_store";
	
	public PortalHomeMyHotelTopFactory(Context context) {
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
                "typeid varchar," +
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
	protected MyHotelTop createModel(Cursor cursor) {
		MyHotelTop info = new MyHotelTop();
		info.id = cursor.getInt(cursor.getColumnIndex("id"));
		info.type_id = cursor.getString(cursor.getColumnIndex("typeid"));
		info.content_type = EnumType.ContentType.convertStr(cursor.getString(cursor.getColumnIndex("contenttype")));
		info.layout_type = EnumType.LayoutType.convertStr(cursor.getString(cursor.getColumnIndex("layouttype")));
		info.bgType = EnumType.BackGroundType.convertInt(cursor.getInt(cursor.getColumnIndex("bgtype")));
		info.bg = cursor.getString(cursor.getColumnIndex("bg"));
		info.title = cursor.getString(cursor.getColumnIndex("title"));
		info.icon = cursor.getString(cursor.getColumnIndex("icon"));
		
		return info;
	}

	@Override
	protected void insertRecord(SQLiteDatabase db, MyHotelTop info) {
		String sql = String.format("insert into %s (_id,position,id,typeid,contenttype,layouttype,bgtype,bg,title,icon) values(?,?,?,?,?,?,?,?,?,?)", getTableName());
		
		Object[] os = new Object[] {null, mIndex, info.id, info.type_id, EnumType.converContentType(info.content_type), 
				EnumType.converLayoutType(info.layout_type),info.bgType ,info.bg, info.title, info.icon};
		db.execSQL(sql, os);
	}
	
	public void insertMyHotelTopInfos(ArrayList<MyHotelTop> list) {
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
	
	public ArrayList<MyHotelTop> getHomeInfosByIndex(int index) {
		ArrayList<MyHotelTop> ret = new ArrayList<MyHotelTop>();
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
		} finally{
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
	protected int updateRecord(SQLiteDatabase db, MyHotelTop record) {
		// TODO Auto-generated method stub
		return 0;
	}

}

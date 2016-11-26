package com.iptv.rocky.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

import com.iptv.common.utils.LogUtils;

/**
 * 本地数据库基类
 */
public abstract class LocalFactoryBase<T> {

	protected LocalOpenHelper helper = null;
	
	public LocalFactoryBase(Context context) {
		helper = new LocalOpenHelper(context);
    }

	protected abstract String getTableName();
	protected abstract String getprimaryKey();
	protected abstract T createModel(Cursor cursor);
	protected abstract void insertRecord(SQLiteDatabase db, T record);
	protected abstract int updateRecord(SQLiteDatabase db, T record);
	
	protected String getOrderColumnName() {
		return null;
	}
	
	protected long getMaxCount() {
		return 0;
	}
	
	/**
	 * 插入一条记录
	 * @param record
	 */
	public void insertRecord(T record) {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			if (getMaxCount() > 0) {
				long curCount = DatabaseUtils.queryNumEntries(db, getTableName());
				if (curCount >= getMaxCount()) {
					deleteOldRecord(db);
				}
			}
			insertRecord(db, record);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	/**
	 * 批量插入记录
	 * @param records
	 */
	public void insertRecord(List<T> records) {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			int size = records.size();
			long max = getMaxCount();
			if (max > 0) {
				long curCount = DatabaseUtils.queryNumEntries(db, getTableName());
				long total = curCount + size; 
				if (total >= max) {
					long del = total - max + 1;
					for (int i = 0; i < del; i ++) {
						
						deleteOldRecord(db);
					}
				}
			}
			for (T record : records) {
				insertRecord(db, record);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	public void updateRecord(T record){
		SQLiteDatabase db = null;
		db = helper.getWritableDatabase();
		updateRecord(db, record);
	}
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public ArrayList<T> findRecords() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = null;
			if (!TextUtils.isEmpty(getOrderColumnName())) {
				sql = String.format("select * from %s order by %s desc", getTableName(), getOrderColumnName());
			} else {
				sql = String.format("select * from %s", getTableName());
			}
			cursor = db.rawQuery(sql, null);
			if (cursor != null) {
				ArrayList<T> result = new ArrayList<T>();
				while (cursor.moveToNext()) {
					result.add(createModel(cursor));
				}
				return result;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return null;
	}
	
	/**
	 * 分页查询
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public ArrayList<T> findRecords(int firstResult, int maxResult) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = findCursorRecords(db, firstResult, maxResult);
			if (cursor != null) {
				ArrayList<T> result = new ArrayList<T>();
				while (cursor.moveToNext()) {
					result.add(createModel(cursor));
				}
				return result;
			}
 		} finally {
 			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return null;
	}
	
	/**
	 * 返回分页记录的游标，适配CursorAdapter
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public Cursor findCursorRecords(SQLiteDatabase db, int firstResult, int maxResult) {
        String sql = null;
        if (!TextUtils.isEmpty(getOrderColumnName())) {
            sql = String.format("select * from %s order by %s desc limit ?,?", getTableName(), getOrderColumnName());
        } else {
            sql = String.format("select * from %s limit ?,?", getTableName());
        }
        try {
            return db.rawQuery(sql, new String[]{String.valueOf(firstResult), String.valueOf(maxResult)});
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return null;
    }

    /**
	 * 返回所有记录的游标，适配CursorAdapter
	 * @return
	 */
	public Cursor findCursorRecords(SQLiteDatabase db) {
		String sql = null;
		if (!TextUtils.isEmpty(getOrderColumnName())) {
			sql = String.format("select * from %s order by %s desc", getTableName(), getOrderColumnName());
		} else {
			sql = String.format("select * from %s", getTableName());
		}
		try {
			return db.rawQuery(sql, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 根据主键查询一条记录
	 * @param primaryKey
	 * @return
	 */
	public T findRecord(int primaryKey) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = String.format("select * from %s where %s=?", getTableName(), getprimaryKey());
			cursor = db.rawQuery(sql, new String[] {String.valueOf(primaryKey)});
			T result = null;
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					result = createModel(cursor);
				}
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return null;
	}
	
	
	
	public T findRecordZ(String primaryKey) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = String.format("select * from %s where %s=?", getTableName(), getprimaryKey());
			cursor = db.rawQuery(sql, new String[] {primaryKey});
			T result = null;
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					result = createModel(cursor);
				}
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return null;
	}

    /**根据一个键值对查询
     * 
     * @param property
     * @param val
     * @return
     */
    public T findByProperty(String property, String val) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            String sql = String.format("select * from %s where %s=?", getTableName(), property);
            cursor = db.rawQuery(sql, new String[] {val});
            T result = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    result = createModel(cursor);
                }
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }
	
	/**
	 * 删除所有记录
	 */
	public void deletedRecords() {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			String sql = String.format("delete from %s", getTableName());
            db.execSQL(sql, new String[]{});
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	/**
	 * 根据主键删除
	 * @param primaryKey
	 */
	public void deletedRecord(int primaryKey) {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			String sql = String.format("delete from %s where %s=?", getTableName(), getprimaryKey());
			db.execSQL(sql, new Integer[] {primaryKey});
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	
	/**
	 * 根据主键删除  适配中兴
	 * @param primaryKey
	 */
	public void deletedRecordZTE(String primaryKey) {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			String sql = String.format("delete from %s where %s=?", getTableName(), getprimaryKey());
			db.execSQL(sql, new String[] {primaryKey});
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	/**
	 * 记录数量
	 * @return
	 */
	public long getRecordCount() {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			return DatabaseUtils.queryNumEntries(db, getTableName());
		} finally {
			if (db != null) {
				db.close();
			}
		}
    }

    /**
	 * 删除旧的记录
	 * @param db
	 */
	private void deleteOldRecord(SQLiteDatabase db) {
		String sql = null;
		if (!TextUtils.isEmpty(getOrderColumnName())) {
			sql = String.format("delete from %s where %s in (select %s from %s order by %s asc limit 1)", 
					getTableName(), getprimaryKey(), getprimaryKey(), getTableName(), getOrderColumnName());
		} else {
			sql = String.format("delete from %s where %s in (select %s from %s limit 1)", 
					getTableName(), getprimaryKey(), getprimaryKey(), getTableName());
		}
		db.execSQL(sql);
	}

    /**
     * 查询最新的一条记录
     */
    public T findNewestRecord() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            String sql = String.format("select * from %s order by %s desc limit 1", getTableName(), getOrderColumnName());
            cursor = db.rawQuery(sql, new String[] {});
            T result = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    result = createModel(cursor);
                }
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }
    
    /**
     * drop table
    */
    public static void dropTable(SQLiteDatabase db, String tableName){
    	try {
    		db.execSQL(String.format("DROP TABLE IF EXISTS %s", tableName));
		} catch (Exception e) {
			LogUtils.e("LocalFactoryBase", "drop table:" + tableName + "error.");
		}
    }
}

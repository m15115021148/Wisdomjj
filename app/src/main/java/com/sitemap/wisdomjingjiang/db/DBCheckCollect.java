package com.sitemap.wisdomjingjiang.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/** 
 * com.sitemap.wisdomjingjiang.db
 * @author chenmeng
 * @Description 查询数据库collect表中的所有内容
 * @date create at  2016年5月4日 上午10:36:55
 */
public class DBCheckCollect {

	private DBHelper dbHelper;
	SQLiteDatabase db;

	public DBCheckCollect(Context context) {
		dbHelper = DBHelper.getNewInstanceDBHelper(context);
	}
	
	/**
	 * 查询collect中的所有数据
	 * @return 返回一个Cursor
	 */
	public Cursor queryDBCollectData(){
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query(DBHelper.COLLECT, null, null, null, null, null, null);
		return cursor;
	}
}

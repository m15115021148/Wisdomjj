package com.sitemap.wisdomjingjiang.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.db
 * @author chenmeng
 * @Description 数据缓存  新闻
 * @date create at  2016年8月8日 上午11:23:31
 */
public class DBCheckNews {
	private DBHelper dbHelper;
	private SQLiteDatabase db;

	public DBCheckNews(Context context) {
		dbHelper = DBHelper.getNewInstanceDBHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * 查询数据
	 * @return 返回一个Cursor
	 */
	public Cursor queryDBCollectData(){
		Cursor cursor = db.query(DBHelper.NEWS, null, null, null, null, null, null);
		return cursor;
	}
	
	/**
	 *	插入数据
	 * @param title
	 * @param id
	 */
	public void insert(String id){
		ContentValues values = new ContentValues();
		values.put("newsid", id);
		db.insert(DBHelper.NEWS, null, values);
		Log.i("result", "插入成功");
	}
}

package com.sitemap.wisdomjingjiang.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.utils
 * @author chenmeng
 * @Description 数据 缓存  （新闻）
 * @date create at  2016年8月8日 上午10:45:38
 */
public class CacheUtils {
	public static final String SAVECACHE = "save_cache";
	public static final String GETCACHE = "get_cache";
	
	/**
	 * 保存 数据
	 * @param context
	 * @return
	 */
	public static boolean saveCache(Context context,String model){		
        SharedPreferences sp = context.getSharedPreferences(
        		SAVECACHE, Context.MODE_PRIVATE
        );
        return sp.edit().putString(GETCACHE, model).commit();
	}
	
	/**
	 * 获取 数据
	 * @param context
	 * @return
	 */
	public static String getCache(Context context){
		SharedPreferences sp = context.getSharedPreferences(
        		SAVECACHE, Context.MODE_PRIVATE
        );
		return sp.getString(GETCACHE, "");
	}
}

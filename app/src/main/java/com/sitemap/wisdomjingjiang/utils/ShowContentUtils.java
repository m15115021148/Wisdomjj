package com.sitemap.wisdomjingjiang.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/** 
 * com.sitemap.wisdomjingjiang.utils
 * @author 蒙
 * @Description Toast工具包
 * @date create at  2016年4月29日 下午5:58:59
 */
public class ShowContentUtils {
	public ShowContentUtils() {

	}

	/**
	 * 长时间显示 位置居中
	 * @param context 所在的activity
	 * @param title 显示的内容
	 */
	public static void showLongToastMessage(Context context, String title) {
		Toast toast = Toast.makeText(context, title, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 显示时间两秒，位置居中
	 * @param context 所在的activity
	 * @param title	显示的内容
	 */
	public static void showShortToastMessage(Context context, String title) {
		Toast toast = Toast.makeText(context, title, 2000);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}

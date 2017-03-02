package com.sitemap.wisdomjingjiang.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络连通性判断工具
 * 
 * @author Administrator
 * 
 */
public class NetworkState {
	private Context context;

	// 传入上下文对象，进行初始化
	public NetworkState(Context context) {
		this.context = context;
	}

	/**
	 * 检测网络是否连接
	 * 
	 * @return
	 */
	public boolean isNetConnected() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo infos = cm.getActiveNetworkInfo();
			if (infos != null) {
				return infos.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 检测wifi是否连接
	 * 
	 * @return
	 */
	public boolean isWifiConnected() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 检测手机移动网络是否连接
	 * 
	 * @return
	 */
	public boolean is3gConnected() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				return true;
			}
		}
		return false;
	}
}

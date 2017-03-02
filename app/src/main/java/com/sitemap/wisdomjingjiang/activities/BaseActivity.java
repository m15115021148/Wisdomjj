package com.sitemap.wisdomjingjiang.activities;

import org.xutils.x;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

/**
 * @author 陈蒙 activity的父类 内嵌fragment 需继承 fragmentactivity
 */
public abstract class BaseActivity extends FragmentActivity {
	// public static ArrayList<Activity> all=new ArrayList<Activity>();
	/**
	 * 内容描述 退出activity时 发送的广播信号
	 */
	public static final String TAG_ESC_ACTIVITY = "com.broader.esc";
	private MyBroaderEsc receiver;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// //设置全屏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// MyUncaughtExceptionHandler mue=
		// MyUncaughtExceptionHandler.getInstance();
		// mue.init(getApplicationContext());
		// Thread.setDefaultUncaughtExceptionHandler(mue);
		// all.add(this);
		// 注册广播
		receiver = new MyBroaderEsc();
		registerReceiver(receiver, new IntentFilter(TAG_ESC_ACTIVITY));
		// 反射注解机制初始化
		x.view().inject(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	/**
	 * @发送广播 退出activity
	 * 
	 */
	class MyBroaderEsc extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				finish();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		/**
		 * 设置为竖屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		// 友盟统计
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		// 友盟统计
		MobclickAgent.onPause(this);
	}

}

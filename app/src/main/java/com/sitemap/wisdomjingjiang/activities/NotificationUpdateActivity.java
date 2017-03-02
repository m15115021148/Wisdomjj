package com.sitemap.wisdomjingjiang.activities;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.activities.DownloadService.DownloadBinder;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * com.sitemap.na2ne.activities.NotificationUpdateActivity
 * 
 * @author zhang 更新类 create at 2015年12月30日 下午4:49:01
 */
public class NotificationUpdateActivity extends Activity {
	private Button btn_cancel;// 关闭按钮
	private TextView tv_progress;// 正在下载
	private DownloadBinder binder;// 下载服务
	private boolean isBinded;// 是否开启
	private ProgressBar mProgressBar;// 进度条
	// 获取到下载url后，直接复制给MapApp,里面的全局变量
	//
	private boolean isDestroy = true;// 是否销毁
	private MyApplication app;// application对象
	private TextView download_version;// 版本号
	private TextView download_size;// apk大小
	String isforce="";//是否强制更新

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		// 隐藏android系统的状态栏
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 隐藏应用程序的标题栏，即当前activity的标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update);
		
		app = (MyApplication) getApplication();
		// btn_update = (Button) findViewById(R.id.update);
		btn_cancel = (Button) findViewById(R.id.cancel);
		tv_progress = (TextView) findViewById(R.id.currentPos);
		download_version = (TextView) findViewById(R.id.download_version);
		download_size = (TextView) findViewById(R.id.download_size);
		download_version.setText(MyApplication.versionName);
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar1);
		 isforce=getIntent().getStringExtra("isforce");
		if (isforce!=null) {
			if (isforce.equals("0")) {
				btn_cancel.setVisibility(View.GONE);
				this.setFinishOnTouchOutside(false); 
			}
		}
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// binder.cancel();
				// binder.cancelNotification();
				finish();
			}
		});
	}

	/**
	 * 服务链接
	 */
	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			isBinded = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			binder = (DownloadBinder) service;
			System.out.println("服务启动!!!");
			// 开始下载
			isBinded = true;
			binder.addCallback(callback);
			binder.start();

		}
	};

	@Override
	protected void onResume() {
		// 友盟统计
		MobclickAgent.onResume(this);
		super.onResume();
//		Log.i("TAG", "aaaaaaaaaaa");
		if (isDestroy && app.isDownload()) {
//			Log.i("TAG", "bbbbbbbbbbb");
			Intent it = new Intent(NotificationUpdateActivity.this,
					DownloadService.class);
			it.putExtra("isforce",isforce);
			startService(it);
			bindService(it, conn, Context.BIND_AUTO_CREATE);
		}
		System.out.println(" notification  onresume");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if (isDestroy && app.isDownload()) {
			Log.i("test", "移动了");
			Intent it = new Intent(NotificationUpdateActivity.this,
					DownloadService.class);
			it.putExtra("isforce",isforce);
			startService(it);
			bindService(it, conn, Context.BIND_AUTO_CREATE);
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 友盟统计
		MobclickAgent.onPause(this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 友盟统计
		MobclickAgent.onResume(this);
		isDestroy = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isBinded) {
			unbindService(conn);
		}
		if (binder != null && binder.isCanceled()) {
			System.out.println(" onDestroy  stopservice");
			Intent it = new Intent(this, DownloadService.class);
			it.putExtra("isforce",isforce);
			stopService(it);
		}
	}

	/**
	 * 返回更新进度条
	 */
	private ICallbackResult callback = new ICallbackResult() {

		@Override
		public void OnBackResult(Object result) {
			// TODO Auto-generated method stub
			if ("finish".equals(result)) {
				finish();
				return;
			}

			int i = (Integer) result;
			mProgressBar.setProgress(i);
			// tv_progress.setText("当前进度 =>  "+i+"%");
			// tv_progress.postInvalidate();
			mHandler.sendEmptyMessage(i);
		}

	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tv_progress.setText("当前进度 ： " + msg.what + "%");
			download_size.setText("" + MyApplication.length/1024 + "MB");
		};
	};

	/**
	 * 
	 * com.sitemap.na2ne.activities.ICallbackResult
	 * 
	 * @author zhang create at 2015年12月30日 下午4:51:00
	 */
	public interface ICallbackResult {
		public void OnBackResult(Object result);
	}
	
	/**
	 * 返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

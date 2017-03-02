package com.sitemap.wisdomjingjiang.activities;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;

/**
 * 
 * Description: 加载网页Activity
 * @author chenhao
 * @date   2016-1-12
 */
public class WebViewActivity extends Activity {
	private WebView web;// 内嵌浏览器
	private String url;// 网页链接地址
 private  MyProgressDialog progressDialog;// 进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		// 隐藏应用程序的标题栏，即当前activity的标题栏
				this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏android系统的状态栏
		
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		setContentView(R.layout.activity_webview);
		progressDialog = MyProgressDialog.createDialog(this);
		web = (WebView) findViewById(R.id.webview);
		url = getIntent().getStringExtra("url");
		initView();

	}
	


	
	@Override
	public void onResume() {
		super.onResume();
		// 友盟统计
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		// 友盟统计
		MobclickAgent.onPause(this);
	}

	/**
	 * 初始化webview
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {

		// 可加载js
		WebSettings setting = web.getSettings();

		// 设置webview自适应屏幕
		setting.setUseWideViewPort(true);// 设置webview推荐窗口
		setting.setLoadWithOverviewMode(true);// 设置webview加载的页面的模式，也设置为true。这方法可以让你的页面适应手机屏幕的分辨率，完整的显示在屏幕上
		setting.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);

		setting.setDomStorageEnabled(true);
		setting.setJavaScriptEnabled(true);
		// 同一编码
		setting.setDefaultTextEncodingName("UTF-8");

		web.loadUrl(url);
		
		// 用webview打开该网页
		web.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
				// /返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.setMessage("加载中...");
					progressDialog.show();
				}
				
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();// 关闭进度条
				}
			}

		});

	}

	/**
	 * 关闭页面
	 * 
	 * @param view
	 */
	public void close(View view) {
		this.finish();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	/**
	 * 改写物理按键——返回的逻辑
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (web.canGoBack()) {
				web.goBack();// 返回上一页面
				return true;
			} else {
				// 调整到主页
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}

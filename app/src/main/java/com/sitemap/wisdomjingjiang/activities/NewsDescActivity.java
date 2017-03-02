package com.sitemap.wisdomjingjiang.activities;

import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.AddressModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.share.UMShareUtil;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * com.sitemap.wisdomjingjiang.activities
 * @author chenmeng
 * @Description 新闻详情 页面的显示
 * @date create at  2016年5月16日 下午1:59:14
 */
public class NewsDescActivity extends BaseActivity implements OnClickListener{
	/**全局类*/ 
	private Context mContext;
	/**标题栏 返回上一层  右侧图片*/ 
	private ImageView mBack,mMore;
	/**标题栏 标题*/ 
	private TextView mTitle;
	/**webview*/ 
	private WebView mWb;
	/**新闻详情的url*/ 
	private String newsUrl;
	private String img;//
	private String title;//分享标题
	 private  MyProgressDialog progressDialog;// 进度条
	 private TextView end_ime;//自动关闭时间显示 
	 private int ti=5;//倒计时时间
	 private 	int type;//类型
	 private String oldUrl;//老的URL

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_desc);
		progressDialog = MyProgressDialog.createDialog(this);
		mContext = this;
		initView();//初始化view
		initData();//初始化数据
		initWebView();//初始化webview
	}

	@Override
	public void onPause() {
		super.onPause();
		mWb.onPause();
	}
	
	@Override
	public void onResume() {
		mWb.onResume();
		super.onResume();
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();// 关闭进度条
			}
			super.handleMessage(msg);
			switch (msg.what) {
			case RequestCode.DAOJISHI://倒计时
				end_ime.setText(""+ti);
				ti--;
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.GETADDRESS) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}
	};	
	
	/**
	 * 初始化view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.news_desc_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.news_desc_title).findViewById(R.id.title);
		mMore = (ImageView) findViewById(R.id.news_desc_title).findViewById(R.id.more);
		mWb = (WebView) findViewById(R.id.news_webview);
		end_ime=(TextView)findViewById(R.id.end_ime);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mBack.setOnClickListener(this);
		mMore.setVisibility(View.VISIBLE);
		mMore.setOnClickListener(this);
		 type = getIntent().getIntExtra("type", 0);
		img=getIntent().getStringExtra("img");
		title=getIntent().getStringExtra("title");
		if(type==1){//广告
			mTitle.setText("详情");
			newsUrl = getIntent().getStringExtra("adUrl");
			oldUrl=newsUrl;
		}else if(type==2){//新靖江论坛
			mMore.setVisibility(View.GONE);
			mTitle.setText(title);
			newsUrl = getIntent().getStringExtra("jjUrl");
			oldUrl=newsUrl;
		}else if(type==3){//订单详情
			mTitle.setText(title);
			newsUrl = getIntent().getStringExtra("orderUrl");
			oldUrl=newsUrl;
			mMore.setVisibility(View.GONE);
	
		}else if(type==6){//团购券使用完毕
			mTitle.setText("详情");
			newsUrl = getIntent().getStringExtra("newsUrl");
			oldUrl=newsUrl;
			mMore.setVisibility(View.GONE);
		}
		else{
			mTitle.setText("资讯详情");
			newsUrl = getIntent().getStringExtra("newsUrl");
			oldUrl=newsUrl;
			newsUrl=newsUrl+"&type=1";
		}
	}
	
	/**
	 * 初始化webview
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {

		// 可加载js
		WebSettings setting = mWb.getSettings();

		// 设置webview自适应屏幕
		setting.setLoadWithOverviewMode(true);// 设置webview加载的页面的模式，也设置为true。这方法可以让你的页面适应手机屏幕的分辨率，完整的显示在屏幕上
		setting.setDomStorageEnabled(true);
		setting.setJavaScriptEnabled(true);
		setting.setSupportZoom(true);
//		setting.setTextSize(WebSettings.TextSize.NORMAL);
//		setting.setDefaultFontSize(16);
		setting.setCacheMode(WebSettings.LOAD_DEFAULT);// 设置 缓存模式 
//		setting.setBuiltInZoomControls(true);// 设置支持缩放
		// 同一编码
		setting.setDefaultTextEncodingName("UTF-8");
		mWb.loadUrl(newsUrl);
		// 用webview打开该网页
		mWb.setWebViewClient(new WebViewClient() {

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
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				ShowContentUtils.showShortToastMessage(mContext, "网页加载失败！");
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();// 关闭进度条
				}
				if (type==3) {
					end_ime.setVisibility(View.VISIBLE);
					Timer time=new Timer();
					time.schedule(new TimerTask() {
						
						@Override
						public void run() {
							if (ti==0) {
								finish();
							}else {
								handler.sendEmptyMessage(RequestCode.DAOJISHI);
							}
							
						}
					}, 10,1000);
				}
			}

			
		});

	}

	@Override
	public void onClick(View v) {
		if(v == mMore){//标题栏  右侧 图标
			UMShareUtil util = new UMShareUtil(this);
			if(img!=null&&!img.equals("")){
			util.shareNetImage(title,RequestCode.NAME,
					img, oldUrl);
			}else{
				util.shareDrawableImage(title,RequestCode.NAME,
						R.drawable.icon, oldUrl);
			}
					
					
		}
		if(v==mBack){
			onBackPressed();
			finish();
		}
	}
	
	/**
	 * 改写物理按键——返回的逻辑
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWb.canGoBack()) {
				mWb.goBack();// 返回上一页面
				return true;
			} else {
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}

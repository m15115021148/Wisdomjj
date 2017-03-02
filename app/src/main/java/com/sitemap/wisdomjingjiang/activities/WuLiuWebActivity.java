package com.sitemap.wisdomjingjiang.activities;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author chenmeng
 *	物流显示页面
 */
public class WuLiuWebActivity extends BaseActivity implements OnClickListener{
	/**全局类*/ 
	private Context mContext;
	/**标题栏 返回上一层  右侧图片*/ 
	private ImageView mBack;
	/**标题栏 标题*/ 
	private TextView mTitle;
	/**webview*/ 
	private WebView mWb;
	/**订单编号  物流名称*/ 
	private String orderName,type;
	private static MyProgressDialog progressDialog;// 进度条
	private String url;//url
	/**类型*/ 
	private int typeStatus = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wu_liu_web);
		mContext = this;
		progressDialog = MyProgressDialog.createDialog(this);
		initView();//初始化view
		initData();//初始化数据
		initWebView();//初始化webview
	}
	
	/**
	 * 初始化view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.wu_liu_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.wu_liu_title).findViewById(R.id.title);
		mWb = (WebView) findViewById(R.id.wu_liu_webview);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {		
		mBack.setOnClickListener(this);
		typeStatus = getIntent().getIntExtra("typeStatus", 0);
		if(typeStatus==1){//物流
			mTitle.setText("物流详情");
			orderName = getIntent().getStringExtra("orderName");
			type = getIntent().getStringExtra("type");
			url = "http://m.kuaidi100.com/index_all.html?type="+type+"&postid="+orderName;
		}else if(typeStatus==2){//退款状态
			mTitle.setText("退款详情");			
			url = getIntent().getStringExtra("refundUrl");
			url=url+"&types=1";
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
		setting.setDefaultFontSize(20);
		setting.setCacheMode(WebSettings.LOAD_DEFAULT);// 设置 缓存模式 
//		setting.setBuiltInZoomControls(true);// 设置支持缩放
		// 同一编码
		setting.setDefaultTextEncodingName("UTF-8");
		mWb.loadUrl(url);
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
			}
			
		});

	}


	@Override
	public void onClick(View v) {
		if(v == mBack){//返回上一层			
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

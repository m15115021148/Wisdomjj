package com.sitemap.wisdomjingjiang.activities;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.fragments.NewsFragment;
import com.sitemap.wisdomjingjiang.fragments.MerchantFragment;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.NewsFragmentActivity;

/**
 * com.sitemap.wisdomjingjiang.activities.NewsActivity
 * 
 * @author zhang create at 2016年5月5日 下午3:51:07
 */
public class NewsActivity extends NewsFragmentActivity implements
		OnClickListener {
	private NewsActivity context;// 本类
	private static MyProgressDialog progressDialog;// 进度条
	private HttpUtil http;// 网络请求
	private LinearLayout base_back_lay;// 回退
	private TextView back_tv;// 回退

	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		for (int i = 0; i < MyApplication.lnewsTypeModel.size(); i++) {
			tabs.add(new TabInfo(1, MyApplication.lnewsTypeModel.get(i)
					.getNewsTypeName(), new NewsFragment(
					MyApplication.lnewsTypeModel.get(i).getNewsTypeID())));
		}

		// tabs.add(new TabInfo(2, "靖江",
		// new NewsFragment("靖江")));
		// tabs.add(new TabInfo(3,"便民",
		// new NewsFragment("便民")));
		// tabs.add(new TabInfo(4, "生活",
		// new NewsFragment("生活")));
		// tabs.add(new TabInfo(5, "天下",
		// new NewsFragment("天下")));
		return 0;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();// 初始化view
		initData();// 初始化数据
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
			case HttpUtil.SUCCESS:// 成功
				if (msg.arg1 == RequestCode.REQUESTCODE) {

				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(context,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.LOGIN) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 初始化控件view
	 */
	public void initView() {
		back_tv = (TextView) findViewById(R.id.back_tv);
		back_tv.setOnClickListener(this);
		base_back_lay = (LinearLayout) findViewById(R.id.base_back_lay);
		base_back_lay.setOnClickListener(this);
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		context = this;
		http = new HttpUtil(handler);
		progressDialog = MyProgressDialog.createDialog(this);

	}

	@Override
	public void onClick(View v) {
		if (v == base_back_lay) {
			finish();
		}
		if (v == back_tv) {
			finish();
		}
	}
}

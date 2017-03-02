package com.sitemap.wisdomjingjiang.activities;


import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.BaoliaoListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.LookNewsModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

/**
 * com.sitemap.wisdomjingjiang.activities.AddressListActivity
 * @author zhang
 * create at 2016年5月13日 上午9:13:57
 */

public class BaoliaoListActivity extends BaseActivity implements OnClickListener{
	private ListView baoliao_list;//爆料列表
	private BaoliaoListViewAdapter baoliaoAdapter;//爆料适配器
	private BaoliaoListActivity context;//本类
	private LinearLayout base_back_lay;//返回
	private TextView back_tv;//返回按钮
	private TextView baoliao_add;//我要爆料
	
	private MyProgressDialog progress;// 进度条
	private HttpUtil http;// http对象
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baoliao_list);
		initView();// 初始化view
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		initData();// 初始化数据
	}


	/**
	 * 初始化控件view
	 */
	public void initView() {
		context=this;
		baoliao_list=(ListView)findViewById(R.id.baoliao_list);
		base_back_lay=(LinearLayout)findViewById(R.id.base_back_lay);
		base_back_lay.setOnClickListener(this);
		back_tv=(TextView)findViewById(R.id.back_tv);
		back_tv.setOnClickListener(this);
		baoliao_add=(TextView)findViewById(R.id.baoliao_add);
		baoliao_add.setOnClickListener(this);
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 关闭进度条
			if (progress != null && progress.isShowing()) {
				progress.dismiss();
			}
			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				if (msg.arg1 == RequestCode.BAOLIAOLIST) {
					List<LookNewsModel> list = JSON.parseArray(msg.obj.toString(),
							LookNewsModel.class);
					baoliaoAdapter=new BaoliaoListViewAdapter(context,list);
					baoliao_list.setAdapter(baoliaoAdapter);
				}
				break;
			case HttpUtil.FAILURE:// 失败
				if (msg.arg1 == RequestCode.BAOLIAOLIST) {
					ShowContentUtils.showShortToastMessage(context,
							RequestCode.ERRORINFO);
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 数据初始化
	 */
	public void initData() {
		if (!MyApplication.isLogin) {
			ShowContentUtils.showShortToastMessage(context, "您还没有登录！");
			return;
		}
		if (!MyApplication.getNetObject().isNetConnected()) {
			ShowContentUtils.showShortToastMessage(context, RequestCode.NOLOGIN);
			return;
		}
		if (progress == null) {
			progress = MyProgressDialog.createDialog(this);
		}
		progress.show();
		if (http == null) {
			http = new HttpUtil(handler);
		}
	 http.sendGet(RequestCode.BAOLIAOLIST,
				WebUrlConfig.lookNews(MyApplication.loginModel.getUserID()));
	}
	@Override
	public void onClick(View v) {
		if (v==base_back_lay) {
			finish();
		}
		if (v==back_tv) {
			finish();
		}
		if (v==baoliao_add) {
			Intent intent=new Intent(context,BaoliaoAddActivity.class);
			startActivity(intent);
		}
	}
}

package com.sitemap.wisdomjingjiang.activities;

import java.util.Timer;
import java.util.TimerTask;

import org.xutils.common.Callback.Cancelable;
import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.LoginModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.activities
 * 
 * @author chenmeng
 * @Description 用户注册页面 手机注册
 * @date create at 2016年5月3日 上午10:45:28
 */
public class RegisterPhoneActivity extends BaseActivity implements
		OnClickListener {
	private RegisterPhoneActivity context;// 本类
	private static MyProgressDialog progressDialog;// 进度条
	private HttpUtil http;// 网络请求
	private LinearLayout base_back_lay;// 回退
	private TextView back_tv;// 回退
	private EditText mPhoneNumber;// 手机号码
	private TextView mNextStep;// 下一步
	private String from="";//来源
	private TextView register_title;//标题
	private LinearLayout tishi;//提示布局
	/**协议*/ 
	private TextView mTreaty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_phone);
		from=getIntent().getStringExtra("to");
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
					Log.i("TAG", "request:" + msg.obj.toString());
					CodeModel code = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (code != null) {
						switch (code.getStatus()) {
						case 0:// 成功
							Intent intent = new Intent();
							intent.setClass(context, RegisterCodeActivity.class);
							intent.putExtra("phone", mPhoneNumber.getText()
									.toString().trim());
							intent.putExtra("to", from);
							startActivity(intent);
							finish();
							break;
						case 1:// 失败
							if (code.getErrorMsg() != null
									&& !code.getErrorMsg().equals("")) {
								ShowContentUtils.showLongToastMessage(context,
										code.getErrorMsg());
								return;
							}
							break;
						default:
							break;
						}
					} else {
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(context,
						"服务器君突然找不到了，请稍微再试！");
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
		mPhoneNumber = (EditText) findViewById(R.id.register_phone_num_ed);
		back_tv = (TextView) findViewById(R.id.back_tv);
		back_tv.setOnClickListener(this);
		mNextStep = (TextView) findViewById(R.id.register_next_step);
		mNextStep.setOnClickListener(this);
		base_back_lay = (LinearLayout) findViewById(R.id.base_back_lay);
		base_back_lay.setOnClickListener(this);
		register_title= (TextView) findViewById(R.id.register_title);
		tishi = (LinearLayout) findViewById(R.id.tishi);
		mTreaty = (TextView) findViewById(R.id.use_treaty);
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		mTreaty.setOnClickListener(this);
		http = new HttpUtil(handler);
		progressDialog = MyProgressDialog.createDialog(this);
		context = this;
		if (from.equals("register")) {
			register_title.setText("注册");
			tishi.setVisibility(View.VISIBLE);
		}else if(from.equals("findpassword")){
			register_title.setText("找回密码");
			tishi.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mNextStep) {// 下一步
			if (!MyApplication.isNull(mPhoneNumber)) {
				ShowContentUtils.showShortToastMessage(context, "手机号不能为空！");
				return;
			}
			if (!MyApplication.isMobileNO(mPhoneNumber.getText().toString()
					.trim())) {
				ShowContentUtils.showShortToastMessage(context, "请输入正确的手机号！");
				return;
			}
			if (MyApplication.getNetObject().isNetConnected()) {
				progressDialog = MyProgressDialog.createDialog(this);
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.setMessage("加载中...");
					progressDialog.show();
				}
				http.sendGet(
						RequestCode.REQUESTCODE,
						WebUrlConfig.requestCode(mPhoneNumber.getText()
								.toString().trim()));
			} else {
				ShowContentUtils.showShortToastMessage(context, "网络无法连接！");
			}

		}

		if (v == base_back_lay) {
			finish();
		}
		if (v == back_tv) {
			finish();
		}
		if(v == mTreaty){//协议
			Intent intent = new Intent(context,RegisterTreatyActivity.class);
			startActivity(intent);
		}

	}

}

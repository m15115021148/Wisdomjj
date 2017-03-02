package com.sitemap.wisdomjingjiang.activities;

import java.util.Timer;
import java.util.TimerTask;

import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 * com.sitemap.wisdomjingjiang.activities
 * @author chenmeng
 * @Description 注册页面  密码输入
 * @date create at  2016年5月3日 上午11:27:17
 */
public class RegisterPswActivity extends BaseActivity implements OnClickListener{
	private RegisterPswActivity context;//本类
	private static MyProgressDialog progressDialog;// 进度条
	private HttpUtil http;//网络请求
	private LinearLayout base_back_lay;//回退
	private TextView back_tv;//回退
	private EditText mPsw;//密码
	private EditText mPswSure;//确认密码
	private TextView mSubmit;//提交
	private String phone="";//用户电话
	private AlertDialog.Builder normalDia;//退出注册对话框
	private String from="";//来源
	private TextView register_title;//标题
	private LinearLayout tishi;//提示布局
	private String outcontext="";//退出内容
	private String success="";//成功提示
	/**协议*/ 
	private TextView mTreaty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		from=getIntent().getStringExtra("to");
		setContentView(R.layout.activity_register_psw);
		initView();
		initData();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mPsw = (EditText) findViewById(R.id.register_password_ed);
		mPswSure = (EditText) findViewById(R.id.register_password_sure_ed);
		mSubmit = (TextView) findViewById(R.id.register_submit);
		mSubmit.setOnClickListener(this);
		back_tv = (TextView) findViewById(R.id.back_tv);
		back_tv.setOnClickListener(this);
		base_back_lay=(LinearLayout)findViewById(R.id.base_back_lay);
		base_back_lay.setOnClickListener(this);
		register_title= (TextView) findViewById(R.id.register_title);
		tishi = (LinearLayout) findViewById(R.id.tishi);
		mTreaty = (TextView) findViewById(R.id.use_treaty);
	}
	
	/**
	 * 数据初始化
	 */
	public void initData() {
		context=this;
		mTreaty.setOnClickListener(this);
		http=new HttpUtil(handler);
		progressDialog = MyProgressDialog.createDialog(this);
		phone=getIntent().getStringExtra("phone");
		if (from.equals("register")) {
			register_title.setText("注册");
			tishi.setVisibility(View.VISIBLE);
			outcontext="是否退出注册？";
			success="注册成功,请登录!";
		}else if(from.equals("findpassword")){
			register_title.setText("找回密码");
			tishi.setVisibility(View.GONE);
			outcontext="是否退出找回密码？";
			success="找回密码成功！";
		}
		normalDia=MyApplication.myAlertDialog(context, true, "提示", outcontext, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.finish();
				dialog.dismiss();
			}
		});
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
				if (msg.arg1 == RequestCode.REGISTER) {
					Log.i("TAG", "request:" + msg.obj.toString());
					CodeModel code = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (code != null) {
						switch (code.getStatus()) {
						case 0:// 成功
							ShowContentUtils.showLongToastMessage(context,
									success);
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

	@Override
	public void onClick(View v) {
		if(v == mSubmit){//提交
			
			if (!MyApplication.isNull(mPsw)) {
				ShowContentUtils.showShortToastMessage(context, "密码不能为空！");
				return;
			}
			if (!MyApplication.isPWD(mPsw.getText()
					.toString().trim())) {
				ShowContentUtils.showShortToastMessage(context, RequestCode.REGISTERTOOT);
				return;
			}
			if (!MyApplication.isNull(mPswSure)) {
				ShowContentUtils.showShortToastMessage(context, "重复密码不能为空！");
				return;
			}
			if (!MyApplication.isPWD(mPswSure.getText().toString()
					.trim())) {
				ShowContentUtils.showShortToastMessage(context, RequestCode.REGISTERTOOT);
				return;
			}
			if (!mPswSure.getText().toString().trim()
					.equals(mPsw.getText().toString().trim())) {
				ShowContentUtils.showShortToastMessage(this, "两次输入密码不一致！");
				return;
			}
			if (MyApplication.getNetObject().isNetConnected()) {
				progressDialog = MyProgressDialog.createDialog(this);
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.setMessage("加载中...");
					progressDialog.show();
				}
				if (from.equals("register")) {
					RequestParams params = http.getParams(WebUrlConfig.register());
					params.addBodyParameter("phoneNumber", phone);
					params.addBodyParameter(
							"password",
							MyApplication.MD5(mPswSure.getText().toString()
									.trim()));
					params.addBodyParameter("phoneSystem", android.os.Build.VERSION.RELEASE);
					params.addBodyParameter("phoneBrand", android.os.Build.MODEL);
					
					http.sendPost(RequestCode.REGISTER, params);
				}else if(from.equals("findpassword")){
					RequestParams params = http.getParams(WebUrlConfig.fixPassword());
					params.addBodyParameter("phoneNumber", phone);
					params.addBodyParameter(
							"password",
							MyApplication.MD5(mPswSure.getText().toString()
									.trim()));
					http.sendPost(RequestCode.REGISTER, params);
				}
				
			} else {
				ShowContentUtils.showShortToastMessage(context, "网络无法连接！");
			}
			
			
			
		}
		
		if (v==base_back_lay) {
			normalDia.create().show();
		}
		if (v==back_tv) {
			normalDia.create().show();
		}
		if(v == mTreaty){//协议
			Intent intent = new Intent(context,RegisterTreatyActivity.class);
			startActivity(intent);
		}
	}	
	
	/**
	 * 返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			normalDia.create().show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

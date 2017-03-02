package com.sitemap.wisdomjingjiang.activities;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author 
 *	修改密码
 */
public class ChangePswActivity extends BaseActivity implements OnClickListener{
	private ChangePswActivity context;//本类
	private static MyProgressDialog progressDialog;// 进度条
	private HttpUtil http;//网络请求
	private LinearLayout base_back_lay;//回退
	private TextView back_tv;//回退
	private EditText changepwd_oldpwd;//旧密码密码
	private EditText mPsw;//密码
	private EditText mPswSure;//确认密码
	private TextView mSubmit;//提交

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_psw);
		initView();
		initData();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		changepwd_oldpwd = (EditText) findViewById(R.id.changepwd_oldpwd);
		mPsw = (EditText) findViewById(R.id.changepwd_newpwd);
		mPswSure = (EditText) findViewById(R.id.changepwd_repwd);
		mSubmit = (TextView) findViewById(R.id.register_submit);
		mSubmit.setOnClickListener(this);
		back_tv = (TextView) findViewById(R.id.back_tv);
		back_tv.setOnClickListener(this);
		base_back_lay=(LinearLayout)findViewById(R.id.base_back_lay);
		base_back_lay.setOnClickListener(this);
	}
	
	/**
	 * 数据初始化
	 */
	public void initData() {
		context=this;
		http=new HttpUtil(handler);
		progressDialog = MyProgressDialog.createDialog(this);
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
				if (msg.arg1 == RequestCode.UPDATEPASSWORD) {
					Log.i("TAG", "request:" + msg.obj.toString());
					CodeModel code = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (code != null) {
						switch (code.getStatus()) {
						case 0:// 成功
							ShowContentUtils.showLongToastMessage(context,
									"修改密码成功，请重新登录！");
							SharedPreferences preferences=context.getSharedPreferences("user",Context.MODE_PRIVATE);
							Editor editor=preferences.edit();
							editor.putString("pwd","");
							editor.commit();
							MyApplication.isLogin=false;
							MyApplication.loginModel=null;
							SetActivity.mContext.finish();
							Intent intent=new Intent(ChangePswActivity.this,LoginActivity.class);
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

	@Override
	public void onClick(View v) {
		if (v==back_tv) {
			finish();
		}
		if (v==base_back_lay) {
			finish();
		}
		if(v == mSubmit){//提交
			if (!MyApplication.isNull(changepwd_oldpwd)) {
				ShowContentUtils.showShortToastMessage(context, "旧密码密码不能为空！");
				return;
			}
			if (!MyApplication.isPWD(changepwd_oldpwd.getText()
					.toString().trim())) {
				ShowContentUtils.showShortToastMessage(context, "旧"+RequestCode.REGISTERTOOT);
				return;
			}
			if (!MyApplication.isNull(mPsw)) {
				ShowContentUtils.showShortToastMessage(context, "新密码不能为空！");
				return;
			}
			if (!MyApplication.isPWD(mPsw.getText()
					.toString().trim())) {
				ShowContentUtils.showShortToastMessage(context, "新"+RequestCode.REGISTERTOOT);
				return;
			}
			if (!MyApplication.isNull(mPswSure)) {
				ShowContentUtils.showShortToastMessage(context, "重复密码不能为空！");
				return;
			}
			if (!MyApplication.isPWD(mPswSure.getText().toString()
					.trim())) {
				ShowContentUtils.showShortToastMessage(context, "重复"+RequestCode.REGISTERTOOT);
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
					RequestParams params = http.getParams(WebUrlConfig.updatePassword());
					params.addBodyParameter("userID", MyApplication.loginModel.getUserID());
					params.addBodyParameter(
							"oldPassword",
							MyApplication.MD5(changepwd_oldpwd.getText().toString()
									.trim()));
					params.addBodyParameter(
							"password",
							MyApplication.MD5(mPswSure.getText().toString()
									.trim()));
					http.sendPost(RequestCode.UPDATEPASSWORD, params);
				
			} else {
				ShowContentUtils.showShortToastMessage(context, "网络无法连接！");
			}
		}
	}
}

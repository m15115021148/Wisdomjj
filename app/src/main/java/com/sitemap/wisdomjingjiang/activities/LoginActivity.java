package com.sitemap.wisdomjingjiang.activities;

import java.util.Set;

import org.xutils.x;
import org.xutils.http.RequestParams;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.LoginModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
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
 * @Description 登录页面
 * @date create at 2016年5月3日 上午9:56:40
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private LoginActivity context;// 本类
	private static MyProgressDialog progressDialog;// 进度条
	private HttpUtil http;// 网络请求
	private LinearLayout base_back_lay;// 回退
	private TextView back_tv;// 回退
	private TextView mRegister;// 注册
	private TextView mForGetPsw;// 找回密码
	private EditText login_phone_num_ed;// 账号
	private EditText login_pwd_num_ed;// 密码
	private TextView login;// 登录
	private static final int MSG_SET_ALIAS = 5001;//极光设置 别名
	/**登录标致*/ 
	private int loginType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();// 初始化view
		initData();
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
				if (msg.arg1 == RequestCode.LOGIN) {
					Log.i("TAG", "request:" + msg.obj.toString());
					MyApplication.loginModel = JSON.parseObject(
							msg.obj.toString(), LoginModel.class);
					if (MyApplication.loginModel.getErrorMsg() != null
							&& !MyApplication.loginModel.getErrorMsg().equals(
									"")) {
						ShowContentUtils.showLongToastMessage(context,
								MyApplication.loginModel.getErrorMsg());
						return;
					} else {
						ShowContentUtils.showLongToastMessage(context, "登录成功！");
//						Intent intent=new Intent(context,MainActivity.class);
//						startActivity(intent);
						MainActivity.mContext.setCheck(1);
						MyApplication.mViewPager.setCurrentItem(0);
						MyApplication.isLogin = true;
						MyApplication.userPhone=login_phone_num_ed.getText().toString().trim();
						SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
						Editor editor = preferences.edit();
						editor.putString("username", login_phone_num_ed.getText().toString().trim());
						editor.putString("pwd", MyApplication.MD5(login_pwd_num_ed.getText().toString()));
						editor.commit();
						JPushInterface.setAlias(getApplicationContext(), MyApplication.loginModel.getUserID(), mAliasCallback);
						context.finish();
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
			case MSG_SET_ALIAS:
//				JPushInterface.setAliasAndTags(getApplicationContext(),MyApplication.userModel.getUserID(), null, mAliasCallback);
				JPushInterface.setAlias(getApplicationContext(), MyApplication.loginModel.getUserID(), mAliasCallback);
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
		back_tv = (TextView) findViewById(R.id.back_tv);
		back_tv.setOnClickListener(this);
		base_back_lay = (LinearLayout) findViewById(R.id.base_back_lay);
		base_back_lay.setOnClickListener(this);
		mRegister = (TextView) findViewById(R.id.register_title);
		mRegister.setOnClickListener(this);
		mForGetPsw = (TextView) findViewById(R.id.forget_passward);
		mForGetPsw.setOnClickListener(this);
		login = (TextView) findViewById(R.id.login);
		login.setOnClickListener(this);
		login_phone_num_ed = (EditText) findViewById(R.id.login_phone_num_ed);
		login_pwd_num_ed = (EditText) findViewById(R.id.login_pwd_num_ed);
		// login_phone_num_ed.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		//
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// if
		// (login_pwd_num_ed.getText().toString().trim()!=null&&!login_pwd_num_ed.getText().toString().trim().equals(""))
		// {
		// if
		// (MyApplication.isPWD(login_pwd_num_ed.getText().toString().trim())&&s.length()==11)
		// {
		// if (MyApplication.isMobileNO(s.toString())) {
		// login.setBackgroundColor(Color.BLUE);
		// }else {
		// login.setBackgroundColor(Color.parseColor("#cccccc"));
		// }
		// }else {
		// login.setBackgroundColor(Color.parseColor("#cccccc"));
		// }
		// }else {
		// login.setBackgroundColor(Color.parseColor("#cccccc"));
		// }
		// }
		// });
		//
		// login_pwd_num_ed.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		//
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// if
		// (login_phone_num_ed.getText().toString().trim()!=null&&!login_phone_num_ed.getText().toString().trim().equals(""))
		// {
		// if
		// (MyApplication.isMobileNO(login_phone_num_ed.getText().toString().trim())&&s.length()>=6)
		// {
		// if (MyApplication.isPWD(s.toString())) {
		// login.setBackgroundColor(Color.BLUE);
		// }else {
		// login.setBackgroundColor(Color.parseColor("#cccccc"));
		// }
		// }else {
		// login.setBackgroundColor(Color.parseColor("#cccccc"));
		// }
		// }else {
		// login.setBackgroundColor(Color.parseColor("#cccccc"));
		// }
		// }
		// });
		loginType = getIntent().getIntExtra("loginType", 0);
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		http = new HttpUtil(handler);
		progressDialog = MyProgressDialog.createDialog(this);
		context = this;
	}

	@Override
	public void onClick(View v) {
		if (v == base_back_lay) {			
			onBackPressed();
			finish();
		}
		if (v == back_tv) {
			onBackPressed();
			finish();
		}
		if (v == mRegister) {// 注册
			Intent intent = new Intent();
			intent.setClass(this, RegisterPhoneActivity.class);
			intent.putExtra("to", "register");
			startActivity(intent);
		}
		if (v == mForGetPsw) {// 忘记密码
			Intent intent = new Intent();
			intent.setClass(this, RegisterPhoneActivity.class);
			intent.putExtra("to", "findpassword");
			startActivity(intent);
		}
		if (v == login) {
			if (!MyApplication.isNull(login_phone_num_ed)) {
				ShowContentUtils.showShortToastMessage(context, "用户名不能为空！");
				return;
			}
			if (!MyApplication.isMobileNO(login_phone_num_ed.getText()
					.toString().trim())) {
				ShowContentUtils.showShortToastMessage(context, "请输入正确的手机号！");
				return;
			}
			if (!MyApplication.isNull(login_pwd_num_ed)) {
				ShowContentUtils.showShortToastMessage(context, "密码不能为空！");
				return;
			}
//			if (!MyApplication.isPWD(login_pwd_num_ed.getText().toString()
//					.trim())) {
//				ShowContentUtils.showShortToastMessage(context, "输入的密码不符合规范！");
//				return;
//			}
			if (MyApplication.getNetObject().isNetConnected()) {
				progressDialog = MyProgressDialog.createDialog(this);
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.setMessage("登陆中...");
					progressDialog.show();
				}
				RequestParams params = http.getParams(WebUrlConfig.login());
				params.addBodyParameter("username", login_phone_num_ed
						.getText().toString().trim());
				params.addBodyParameter(
						"password",
//						login_pwd_num_ed.getText().toString().trim()
						MyApplication.MD5(login_pwd_num_ed.getText().toString()
								.trim())
						);
				http.sendPost(RequestCode.LOGIN, params);
			} else {
				ShowContentUtils.showShortToastMessage(context, "网络无法连接！");
			}
		}

	}
	/**
	 * 设置极光别名回调
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                Log.i("TAG", logs);
                Log.i("TAG", alias);
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.i("TAG", logs);
                if (MyApplication.getNetObject().isNetConnected()) {
                	handler.sendMessageDelayed(handler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
    			} else {
    				ShowContentUtils.showShortToastMessage(context, "网络无法连接！");
    			}
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
                Log.e("TAG", logs);
            }
            
//            CommonToast.showShortToastMessage(context, logs);
        }
	    
	};
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MainActivity.mContext.setCheck(1);
		MyApplication.mViewPager.setCurrentItem(0);
	}
}

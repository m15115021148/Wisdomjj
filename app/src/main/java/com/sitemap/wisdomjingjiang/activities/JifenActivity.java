package com.sitemap.wisdomjingjiang.activities;

import org.xutils.common.Callback.Cancelable;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.LoginModel;
import com.sitemap.wisdomjingjiang.models.SignModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

/**
 * com.sitemap.wisdomjingjiang.activities.JifenActivity
 * 
 * @author zhang create at 2016年5月13日 上午9:13:57
 */
@ContentView(R.layout.activity_jifen)
public class JifenActivity extends BaseActivity {
	@ViewInject(R.id.base_back_lay)
	private LinearLayout base_back_lay;// 返回
	@ViewInject(R.id.grade)
	private TextView mGrade;// 我的积分
	@ViewInject(R.id.sign)
	private TextView sign;// 进行签到

	private MyProgressDialog progress;// 进度条
	private HttpUtil http;// http对象
	private Cancelable cancelHttp;// http可中断对象
	// 网络请求顺序
	private final int FIRST = 0;

	private Context context;// 本类
	private SharedPreferences preferences;//储存器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();// 初始化数据
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		context = this;
		if (MyApplication.isLogin) {
			mGrade.setText(MyApplication.loginModel.getIntegral());
			
		} else {
			mGrade.setText("0");
		}
		http = new HttpUtil(handler);
		 preferences = getSharedPreferences("user",
					Context.MODE_PRIVATE);
		if (preferences.getString("pwd", "") != null
				&& !preferences.getString("pwd", "").equals("")) {
			
			RequestParams params = http.getParams(WebUrlConfig.login());
			params.addBodyParameter("username",preferences.getString("username", ""));
			params.addBodyParameter(
					"password",preferences.getString("pwd", ""));
			http.sendPost(RequestCode.LOGIN, params);
				
		}
		
		
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
				if (msg.arg1 == RequestCode.LOGIN) {
					Log.i("TAG", "request:" + msg.obj.toString());
					MyApplication.loginModel = JSON.parseObject(
							msg.obj.toString(), LoginModel.class);
					if (MyApplication.loginModel.getErrorMsg() != null
							&& !MyApplication.loginModel.getErrorMsg().equals(
									"")) {
//						ShowContentUtils.showLongToastMessage(mContext,
//								MyApplication.loginModel.getErrorMsg());
						return;
					} else {
					}
					if (MyApplication.loginModel!=null&&"1".equals(MyApplication.loginModel.getIsSign())) {
						sign.setBackgroundColor(context.getResources().getColor(R.color.bghui));
						sign.setClickable(false);
						sign.setText("已签到");
						
					} 
					if (MyApplication.isLogin) {
						mGrade.setText(MyApplication.loginModel.getIntegral());
						
					} else {
						mGrade.setText("0");
					}
				}
				if (msg.arg1 == FIRST) {
					SignModel model = JSON.parseObject(msg.obj.toString(),
							SignModel.class);
					if (TextUtils.isEmpty(model.getErrorMsg())) {
						mGrade.setText(model.getIntegral());
						MyApplication.loginModel.setIntegral(model.getIntegral());
						ShowContentUtils.showShortToastMessage(context, "签到成功！");
						sign.setBackgroundColor(context.getResources().getColor(R.color.bghui));
						sign.setClickable(false);
						sign.setText("已签到");
					} else {
						ShowContentUtils.showShortToastMessage(context,
								model.getErrorMsg());
					}

				}
				break;
			case HttpUtil.FAILURE:// 失败
				if (msg.arg1 == FIRST) {
					ShowContentUtils.showShortToastMessage(context,
							RequestCode.ERRORINFO);
				}
				break;
			default:
				break;
			}
		}

	};

	@Event(value = { R.id.base_back_lay, R.id.sign }, type = View.OnClickListener.class)
	private void viewClick(View v) {
		if (v == base_back_lay) {
			// 中断网络链接示例
			if (cancelHttp != null && !cancelHttp.isCancelled()) {
				cancelHttp.cancel();
			}
			// 关闭进度条
			if (progress != null && progress.isShowing()) {
				progress.dismiss();
			}

			finish();
		}
		// 进行签到
		if (v == sign) {
			if (!MyApplication.isLogin) {
				ShowContentUtils.showShortToastMessage(context, "您还没有登录！");
				Intent intent =new Intent(context,LoginActivity.class);
				startActivity(intent);
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
			cancelHttp = http.sendGet(FIRST,
					WebUrlConfig.sign(MyApplication.loginModel.getUserID()));
		}
	}
}

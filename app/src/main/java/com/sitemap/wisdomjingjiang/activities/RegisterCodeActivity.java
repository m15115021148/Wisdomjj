package com.sitemap.wisdomjingjiang.activities;

import java.util.Timer;
import java.util.TimerTask;

import org.xutils.common.Callback.Cancelable;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

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
 * 
 * @author zhang
 * @Description 用户注册页面 验证码页面
 * @date create at 2016年5月10日 上午10:45:28
 */
public class RegisterCodeActivity extends BaseActivity implements
		OnClickListener {
	private RegisterCodeActivity context;//本类
	private static MyProgressDialog progressDialog;// 进度条
	private HttpUtil http;//网络请求
	private LinearLayout base_back_lay;//回退
	private TextView back_tv;//回退
	private EditText mCodeNumber;// 验证码号码
	private TextView mNextStep;// 下一步
	private TextView send_code;//发送验证码
	private TextView use_phone;//用户电话
	private Timer mTimer1;//计时器
	private String phone="";//用户电话
	private AlertDialog.Builder normalDia;//退出注册对话框
	private String from="";//来源
	private TextView register_title;//标题
	private LinearLayout tishi;//提示布局
	private String outcontext="";//退出内容
	/**协议*/ 
	private TextView mTreaty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_code);
		from=getIntent().getStringExtra("to");
		initView();// 初始化view
		initData();// 初始化数据
	}

	/**
	 * 初始化控件view
	 */
	public void initView() {
		register_title= (TextView) findViewById(R.id.register_title);
		tishi = (LinearLayout) findViewById(R.id.tishi);
		mCodeNumber = (EditText) findViewById(R.id.register_phone_code_ed);
		back_tv = (TextView) findViewById(R.id.back_tv);
		back_tv.setOnClickListener(this);
		mNextStep = (TextView) findViewById(R.id.register_next_step);
		mNextStep.setOnClickListener(this);
		send_code = (TextView) findViewById(R.id.send_code);
		send_code.setOnClickListener(this);
		use_phone = (TextView) findViewById(R.id.use_phone);
		base_back_lay=(LinearLayout)findViewById(R.id.base_back_lay);
		base_back_lay.setOnClickListener(this);
		mTreaty = (TextView) findViewById(R.id.use_treaty);
		mTreaty.setOnClickListener(this);
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		context=this;
		http=new HttpUtil(handler);
		progressDialog = MyProgressDialog.createDialog(this);
		phone=getIntent().getStringExtra("phone");
		use_phone.setText(phone.substring(0,3)+"****"+phone.substring(7));
		if (from.equals("register")) {
			register_title.setText("注册");
			tishi.setVisibility(View.VISIBLE);
			outcontext="是否退出注册？";
		}else if(from.equals("findpassword")){
			register_title.setText("找回密码");
			tishi.setVisibility(View.GONE);
			outcontext="是否退出找回密码？";
		}
		send_code.setTextColor(Color.parseColor("#bcbcbc"));
		send_code.setText("已发送（"+MyApplication.codeNum+"）");
		send_code.setClickable(false);
				mTimer1= new Timer();
				mTimer1.schedule(new TimerTask() {
					@Override
					public void run() {
						handler.sendEmptyMessage(9);
					}							
				},10, 1000);
		normalDia=MyApplication.myAlertDialog(context, true, "提示",outcontext, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTimer1.cancel();
				MyApplication.codeNum=60;
				context.finish();
				dialog.dismiss();
			}
		});
		
	}
	
	//-----------------hander-----------------
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();// 关闭进度条
			}
			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				//对比验证码
				if (msg.arg1 == RequestCode.COMPARECODE) {
					Log.i("TAG", "request:" + msg.obj.toString());
					CodeModel code = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (code != null) {
						switch (code.getStatus()) {
						case 0:// 成功
							Intent intent = new Intent();
							intent.setClass(context, RegisterPswActivity.class);
							intent.putExtra("phone", phone);
							intent.putExtra("to", from);
							startActivity(intent);
							mTimer1.cancel();
							MyApplication.codeNum=60;
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
				//请求验证码
				if (msg.arg1 == RequestCode.REQUESTCODE) {
					Log.i("TAG", "request:" + msg.obj.toString());
					CodeModel code = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (code != null) {
						switch (code.getStatus()) {
						case 0:// 成功
							send_code.setTextColor(Color.parseColor("#bcbcbc"));
							send_code.setText("已发送（"+MyApplication.codeNum+"）");
							send_code.setClickable(false);
									mTimer1= new Timer();
									mTimer1.schedule(new TimerTask() {
										@Override
										public void run() {
											handler.sendEmptyMessage(9);
										}							
									},10, 1000);
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
			case 9:
				MyApplication.codeNum--;
				send_code.setText("已发送（"+MyApplication.codeNum+"）");
				if (MyApplication.codeNum==0) {
					send_code.setTextColor(Color.parseColor("#000000"));
					MyApplication.codeNum=60;
					send_code.setText("重新发送");
					send_code.setClickable(true);
					if(mTimer1!=null){
						
						mTimer1.cancel();
					}
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		if (v==base_back_lay) {
			normalDia.create().show();
		}
		if (v==back_tv) {
			normalDia.create().show();
		}
		if (v == send_code) {// 获取验证码
			
			if (MyApplication.getNetObject().isNetConnected()) {
				progressDialog = MyProgressDialog.createDialog(this);
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.setMessage("加载中...");
					progressDialog.show();
				}
				http.sendGet(
						RequestCode.REQUESTCODE,
						WebUrlConfig.requestCode(phone));
			} else {
				ShowContentUtils.showShortToastMessage(context, "网络无法连接！");
			}
			
		}
		if (v == mNextStep) {// 下一步
			if (!MyApplication.isNull(mCodeNumber)) {
				ShowContentUtils.showShortToastMessage(context, "验证码不能为空！");
				return;
			}
			if (mCodeNumber.getText().toString().trim().length()!=4) {
				ShowContentUtils.showShortToastMessage(context, "请输入4位验证码！");
				return;
			}
			
			if (MyApplication.getNetObject().isNetConnected()) {
				progressDialog = MyProgressDialog.createDialog(this);
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.setMessage("验证中...");
					progressDialog.show();
				}
				http.sendGet(
						RequestCode.COMPARECODE,
						WebUrlConfig.compareCode(phone, mCodeNumber.getText().toString().trim()));
			} else {
				ShowContentUtils.showShortToastMessage(context, "网络无法连接！");
			}
			
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

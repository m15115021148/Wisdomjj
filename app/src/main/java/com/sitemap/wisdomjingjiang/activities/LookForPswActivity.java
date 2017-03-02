package com.sitemap.wisdomjingjiang.activities;

import java.util.Timer;
import java.util.TimerTask;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.activities
 * 
 * @author chenmeng
 * @Description 找回密码页面
 * @date create at 2016年5月3日 下午1:27:41
 */
public class LookForPswActivity extends BaseActivity implements OnClickListener {

	private EditText mPhoneNumber;// 手机号码
	private EditText mCodeNumber;// 输入的验证码
	private TextView mSendCode;// 获取验证码
	private TextView mNextStep;// 下一步
	private Timer mTimer1;// 计时器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_for_psw);

		initData();// 初始化数据
		initView();// 初始化view
	}

	/**
	 * 初始化控件view
	 */
	public void initView() {
		mPhoneNumber = (EditText) findViewById(R.id.register_phone_num_ed);
		mCodeNumber = (EditText) findViewById(R.id.register_phone_code_ed);
		mSendCode = (TextView) findViewById(R.id.send_code);
		mSendCode.setOnClickListener(this);
		mNextStep = (TextView) findViewById(R.id.register_next_step);
		mNextStep.setOnClickListener(this);
	}

	public void initData() {

	}

	// -----------------hander-----------------
	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what == 9) {
				MyApplication.codeNum--;
				mSendCode.setText("已发送（" + MyApplication.codeNum + "）");
				if (MyApplication.codeNum == 0) {
					mSendCode.setBackgroundResource(R.color.huise);
					MyApplication.codeNum = 60;
					mSendCode.setText("发送验证码");
					mSendCode.setTextColor(Color.rgb(255, 255, 255));
					mSendCode.setClickable(true);
					if (mTimer1 != null) {
						mSendCode.setBackgroundResource(R.color.send_code);
						mSendCode.setTextColor(Color.rgb(0, 0, 0));
						mTimer1.cancel();						
					}
				}
			}
		};
	};

	@Override
	public void onClick(View v) {
		if (v == mSendCode) {// 获取验证码
			if (MyApplication.isMobileNO(mPhoneNumber.getText().toString().trim())) {
				if (mPhoneNumber.getText().toString().trim().equals("")
						|| mPhoneNumber.getText().toString().trim() == null) {
					ShowContentUtils.showShortToastMessage(this, "手机号不能为空！");
					return;
				} else {
					mSendCode.setBackgroundResource(R.color.huise);
					mSendCode.setText("已发送（"+MyApplication.codeNum+"）");
					mSendCode.setClickable(false);
					mTimer1= new Timer();
					mTimer1.schedule(new TimerTask() {
						@Override
						public void run() {
							handler.sendEmptyMessage(9);
						}							
					},10, 1000);
					//发送请求
					
				}
			}else {
				ShowContentUtils.showShortToastMessage(this, "请输入正确的手机号！");
				return;
			}
		}
		if (v == mNextStep) {// 下一步
			Intent intent = new Intent();
			intent.setClass(this, ChangePswActivity.class);
			startActivity(intent);
			finish();
		}

	}

}

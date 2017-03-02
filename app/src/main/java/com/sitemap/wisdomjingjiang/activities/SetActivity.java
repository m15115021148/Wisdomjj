package com.sitemap.wisdomjingjiang.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;


/**
 * com.sitemap.wisdomjingjiang.activities.SetActivity
 * @author zhang
 * create at 2016年5月13日 上午9:13:57
 */

public class SetActivity extends BaseActivity implements OnClickListener{
	public static SetActivity mContext;//本类
	private LinearLayout base_back_lay;//返回
	private TextView back_tv;//返回按钮
	/**修改密码*/ 
	private RelativeLayout mChangePsw,mAbout;
	private TextView userOut;// 退出按钮
	private CheckBox ispush;//
	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		initView();// 初始化view
		initData();// 初始化数据
		
	}
	/**
	 * 初始化控件view
	 */
	public void initView() {
		mContext=this;
		base_back_lay=(LinearLayout)findViewById(R.id.base_back_lay);
		base_back_lay.setOnClickListener(this);
		back_tv=(TextView)findViewById(R.id.back_tv);
		back_tv.setOnClickListener(this);
		mChangePsw = (RelativeLayout) findViewById(R.id.set_change_psw);
		mChangePsw.setOnClickListener(this);
		userOut = (TextView) findViewById(R.id.user_out_tv);
		userOut.setOnClickListener(this);
		ispush=(CheckBox) findViewById(R.id.ispush);
		mAbout  = (RelativeLayout) findViewById(R.id.set_about);
		mAbout.setOnClickListener(this);
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		preferences = mContext
				.getSharedPreferences("user",
						Context.MODE_PRIVATE);
		if (preferences.getBoolean("isPush", true)) {
			ispush.setChecked(true);
			JPushInterface.resumePush(getApplicationContext());
		}else {
			ispush.setChecked(false);
			JPushInterface.stopPush(getApplicationContext());
		}
		ispush.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					JPushInterface.resumePush(getApplicationContext());
					
					Editor editor = preferences.edit();
					editor.putBoolean("isPush", true);
					editor.commit();
				}else {
					JPushInterface.stopPush(getApplicationContext());
					Editor editor = preferences.edit();
					editor.putBoolean("isPush", false);
					editor.commit();
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		if (v==base_back_lay) {
			finish();
		}
		if (v==back_tv) {
			finish();
		}
		if(v == mChangePsw){//修改密码
			Intent intent = new Intent(mContext,ChangePswActivity.class);
			startActivity(intent);
		}
		if(v == mAbout){//关于
			Intent intent = new Intent(mContext,AboutActivity.class);
			startActivity(intent);
		}
		if (v == userOut) {
			MyApplication.myAlertDialog(mContext, true, "提示", "是否要注销账户？", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences preferences=mContext.getSharedPreferences("user",Context.MODE_PRIVATE);
					Editor editor=preferences.edit();
					editor.putString("pwd","");
					editor.commit();
					MyApplication.isLogin=false;
					MyApplication.loginModel=null;					
					MyApplication.mViewPager.setCurrentItem(0);
					Intent intent=new Intent(mContext,LoginActivity.class);
					startActivity(intent);
					mContext.finish();
					dialog.dismiss();
					
				}
			}).create().show();
		}
	}
}

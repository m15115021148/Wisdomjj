package com.sitemap.wisdomjingjiang.activities;


import java.io.File;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.utils.ImageUtil;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.PhotoCameraDialog;

/**
 * com.sitemap.wisdomjingjiang.activities.UserActivity
 * @author zhang
 * create at 2016年5月13日 下午3:13:57
 */

public class UserActivity extends BaseActivity implements OnClickListener{
	private UserActivity mContext;//本类
	private LinearLayout base_back_lay;//返回
	private TextView back_tv;//返回按钮
	/**昵称*/ 
	private RelativeLayout mUserNick;
	/**年龄 性别  职业*/ 
	private RelativeLayout mAge,mSex,mWork;
	/**用户昵称  职业 年龄 性别*/ 
	private TextView nick,work,age,sex;
	/**输入对话框的view*/ 
	private View mDialogView;
	/**头像点击事件*/ 
	private LinearLayout mAuth;
	private LinearLayout layout;//整个布局
	private PhotoCameraDialog photo;// 获取图片工具
	private ImageView touxiang;//头像
	private String path="";//路径
	private TextView username;//账号
	private MyProgressDialog progress;// 进度条
	private HttpUtil http;// http对象
	private String nikeName="";//昵称
	private String userSex="";//性别
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
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
//		mWork = (RelativeLayout) findViewById(R.id.my_work);
//		mAge = (RelativeLayout) findViewById(R.id.user_age);
		mSex = (RelativeLayout) findViewById(R.id.user_sex);
		mUserNick = (RelativeLayout) findViewById(R.id.user_name_lay);
		mUserNick.setOnClickListener(this);
		nick=(TextView)findViewById(R.id.user_name);
//		work = (TextView) findViewById(R.id.work);
//		age = (TextView) findViewById(R.id.age);
		sex = (TextView) findViewById(R.id.sex);
		mAuth = (LinearLayout) findViewById(R.id.user_author);
		layout= (LinearLayout) findViewById(R.id.layout);
		touxiang= (ImageView) findViewById(R.id.touxiang);
		username = (TextView) findViewById(R.id.username);
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
//		mWork.setOnClickListener(this);
//		mAge.setOnClickListener(this);
		mSex.setOnClickListener(this);
		mAuth.setOnClickListener(this);
		if (MyApplication.loginModel.getImg()!=null&&!MyApplication.loginModel.getImg().equals("")) {
			x.image().bind(touxiang,MyApplication.loginModel.getImg(),MyApplication.imageOptionsZ);
		}
		 sex.setText(MyApplication.loginModel.getSex());
		 username.setText(MyApplication.userPhone);
		 nick.setText(MyApplication.loginModel.getNickName());
		 if (progress == null) {
				progress = MyProgressDialog.createDialog(this);
			}
			if (http == null) {
				http = new HttpUtil(handler);
			}
	}
	
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				if (msg.arg1 == RequestCode.UPDATEHEADIMG) {
					// 关闭进度条
					if (progress != null && progress.isShowing()) {
						progress.dismiss();
					}

					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (0 == model.getStatus()) {
						ShowContentUtils
								.showShortToastMessage(mContext, "修改成功！");
						Log.e("result", path);
						MyApplication.loginModel.setImg(path);
					} else {
						ShowContentUtils.showShortToastMessage(mContext,
								model.getErrorMsg());
					}
				}
				if (msg.arg1 == RequestCode.UPDATENIKENAME) {
					// 关闭进度条
					if (progress != null && progress.isShowing()) {
						progress.dismiss();
					}

					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (0 == model.getStatus()) {
						ShowContentUtils
								.showShortToastMessage(mContext, "修改成功！");
						nick.setText(nikeName);
						MyApplication.loginModel.setNickName(nikeName);
					} else {
						ShowContentUtils.showShortToastMessage(mContext,
								model.getErrorMsg());
					}
				}
				if (msg.arg1 == RequestCode.UPDATESEX) {
					// 关闭进度条
					if (progress != null && progress.isShowing()) {
						progress.dismiss();
					}

					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (0 == model.getStatus()) {
						ShowContentUtils
								.showShortToastMessage(mContext, "修改成功！");
						 sex.setText(userSex);
						MyApplication.loginModel.setSex(userSex);
					} else {
						ShowContentUtils.showShortToastMessage(mContext,
								model.getErrorMsg());
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
				// 关闭进度条
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				if (msg.arg1 == RequestCode.UPDATEHEADIMG) {
					ShowContentUtils.showShortToastMessage(mContext,
							RequestCode.ERRORINFO);
				}
				break;
			case HttpUtil.LOADING:// 进度
				if (msg.arg1 == RequestCode.UPDATEHEADIMG) {
					progress.setMessage(msg.arg2 + "%");
				}
				break;
			default:
				break;
			}
		}

	};
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 相册返回调用
		if (requestCode == PhotoCameraDialog.IMAGE_CODE
				&& resultCode == Activity.RESULT_OK) {
			if (data != null) {
				Uri originalUri = data.getData(); // 获得图片缩略图的uri
				path = ImageUtil.setPhoto(mContext, originalUri, touxiang);
//				paths.add(path);
				uplodeTouxiang();
			}

		}

		// 相机返回调用
		else if (requestCode == PhotoCameraDialog.TAKE_PICTURE
				&& resultCode == Activity.RESULT_OK) {
			path = ImageUtil.setCameraPhoto(mContext, photo, touxiang);
			uplodeTouxiang();
		}
	}
	
	
	@Override
	public void onClick(View v) {
		if (v==base_back_lay) {
			finish();
		}
		if (v==back_tv) {
			finish();
		}
		if(v == mAuth){//头像
//			final String[] items = new String[]{"本地相册","照相"};
//			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//	        builder.setTitle("请选择");
//	        builder.setItems(items, new DialogInterface.OnClickListener() {
//	            @Override
//	            public void onClick(DialogInterface dialog, int which) {
//	                ShowContentUtils.showLongToastMessage(mContext, items[which]);
//	            }
//	        });
//	        builder.create().show();
			photo = new PhotoCameraDialog(this, layout);
		}
		if(v == mSex){//性别
			final String[] items = new String[]{"男","女"};
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	        builder.setTitle("请选择");
	        builder.setItems(items, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                userSex=items[which];
	                updateSex(userSex);
	            }
	        });
	        builder.create().show();
		}
//		if(v == mAge){//年龄
//			mDialogView = MyApplication.inputDialog(mContext,
//			new DialogInterface.OnClickListener() {//确定		
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					EditText et = (EditText) mDialogView
//							.findViewById(R.id.dialog_content);						
//					if (et.getText().toString().trim()
//							.equals("")
//							|| et.getText().toString().trim() == null) {
//						ShowContentUtils.showShortToastMessage(mContext,
//								"内容不能为空！");
//					} else {
//						age.setText(et.getText().toString());
//					}
//				}
//			}, 
//			new DialogInterface.OnClickListener() {//取消				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			});
//			TextView title = (TextView) mDialogView.findViewById(R.id.dialog_title);
//			title.setText("请输入你的年龄");
//			//只能输入数字
//			EditText input = (EditText) mDialogView
//					.findViewById(R.id.dialog_content);
//			input.setKeyListener(new DigitsKeyListener(false,true));
//		}
		if(v == mWork){//职业
			mDialogView = MyApplication.inputDialog(mContext,
			new DialogInterface.OnClickListener() {//确定		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					EditText diaInput = (EditText) mDialogView
							.findViewById(R.id.dialog_content);					
					if (diaInput.getText().toString().trim()
							.equals("")
							|| diaInput.getText().toString().trim() == null) {
						ShowContentUtils.showShortToastMessage(mContext,
								"内容不能为空！");
					} else {
						work.setText(diaInput.getText().toString());
					}
				}
			}, 
			new DialogInterface.OnClickListener() {//取消				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			TextView title = (TextView) mDialogView.findViewById(R.id.dialog_title);
			title.setText("请输入你的职业");
		}
		if (v==mUserNick) {
			AlertDialog.Builder customDia = new AlertDialog.Builder(
					mContext);
			final View viewDia = LayoutInflater.from(mContext)
					.inflate(R.layout.alertdialog_edittext, null);
			customDia.setView(viewDia);
			customDia.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							EditText diaInput = (EditText) viewDia
									.findViewById(R.id.dialog_content);

							if (diaInput.getText().toString().trim()
									.equals("")
									|| diaInput.getText().toString().trim() == null) {
								ShowContentUtils.showShortToastMessage(mContext,
										"修改的名字不能为空！");
							} else {
								nikeName=diaInput
										.getText().toString().trim();
								
								updateNikeName(nikeName);
							}
						}
					});
			customDia.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
						}
					});
			customDia.create().show();
		}
	}
	
	/**
	 * 修改头像
	 */
	public void uplodeTouxiang() {
	
		if (!MyApplication.getNetObject().isNetConnected()) {
			ShowContentUtils
					.showShortToastMessage(mContext, RequestCode.NOLOGIN);
			return;
		}
		
		progress.show();
		
		RequestParams params = http.getParams(WebUrlConfig.updateHeadImg());
		params.addParameter("userID", MyApplication.loginModel.getUserID());
		params.addParameter("img", new File(path));
		http.uploadFile(RequestCode.UPDATEHEADIMG, params);
	}

	/**
	 * 修改昵称
	 */
	public void updateNikeName(String nikeName) {
	
		if (!MyApplication.getNetObject().isNetConnected()) {
			ShowContentUtils
					.showShortToastMessage(mContext, RequestCode.NOLOGIN);
			return;
		}
		
		progress.show();
		
		http.sendGet(
				RequestCode.UPDATENIKENAME,
				WebUrlConfig.updateNickname(MyApplication.loginModel.getUserID(),nikeName));
	}
	
	/**
	 * 修改性别
	 */
	public void updateSex(String sex) {
	
		if (!MyApplication.getNetObject().isNetConnected()) {
			ShowContentUtils
					.showShortToastMessage(mContext, RequestCode.NOLOGIN);
			return;
		}
		
		progress.show();
		
		http.sendGet(
				RequestCode.UPDATESEX,
				WebUrlConfig.updateSex(MyApplication.loginModel.getUserID(),sex));
	}
}

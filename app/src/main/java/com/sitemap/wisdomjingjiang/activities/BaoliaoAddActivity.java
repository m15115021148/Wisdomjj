package com.sitemap.wisdomjingjiang.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sitemap.wisdomjingjiang.utils.TextviewDrawable;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.PhotoCameraDialog;

/**
 * com.sitemap.wisdomjingjiang.activities.BaoliaoAddActivity
 * 
 * @author zhang create at 2016年5月13日 下午3:13:57
 */
@ContentView(R.layout.activity_baoliao_add)
public class BaoliaoAddActivity extends BaseActivity {
	private BaoliaoAddActivity context;// 本类
	@ViewInject(R.id.base_back_lay)
	private LinearLayout base_back_lay;// 返回
	@ViewInject(R.id.back_tv)
	private TextView back_tv;// 返回按钮
	@ViewInject(R.id.pubulic)
	private TextView pubulic;// 公开
	@ViewInject(R.id.anonymity)
	private TextView anonymity;// 匿名
	@ViewInject(R.id.title)
	private EditText titleView;// 标题
	@ViewInject(R.id.content)
	private EditText contentView;// 内容
	@ViewInject(R.id.refund_sub)
	private TextView submit;// 提交
	@ViewInject(R.id.img1)
	private ImageView img1;// 图片
	@ViewInject(R.id.img2)
	private ImageView img2;// 图片
	@ViewInject(R.id.img3)
	private ImageView img3;// 图片
	@ViewInject(R.id.img4)
	private ImageView img4;// 图片
	@ViewInject(R.id.img5)
	private ImageView img5;// 图片
	@ViewInject(R.id.img6)
	private ImageView img6;// 图片
	@ViewInject(R.id.add_pic)
	private TextView add_pic;// 添加
	@ViewInject(R.id.layout)
	private LinearLayout layout;// 整个布局

	private TextviewDrawable textUtil;// 文字图片处理工具
	private MyProgressDialog progress;// 进度条
	private HttpUtil http;// http对象
	private String title;// 标题
	private String content;// 内容
	private int type = 2;// 爆料方式:是否匿名发布（1：是，2否）
	private PhotoCameraDialog photo;// 获取图片工具

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		context = this;
		initData();// 初始化数据

	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		textUtil = new TextviewDrawable(this);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				if (msg.arg1 == RequestCode.BAOLIAO) {
					// 关闭进度条
					if (progress != null && progress.isShowing()) {
						progress.dismiss();
					}

					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (0 == model.getStatus()) {
						ShowContentUtils
								.showShortToastMessage(context, "爆料成功！");
						context.finish();
					} else {
						ShowContentUtils.showShortToastMessage(context,
								model.getErrorMsg());
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
				// 关闭进度条
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				if (msg.arg1 == RequestCode.BAOLIAO) {
					ShowContentUtils.showShortToastMessage(context,
							RequestCode.ERRORINFO);
				}
				break;
			case HttpUtil.LOADING:// 进度
				if (msg.arg1 == RequestCode.BAOLIAO) {
					progress.setMessage(msg.arg2 + "%");
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 请求网络
	 */
	public void loadNet() {
		if (!MyApplication.isLogin) {
			ShowContentUtils.showShortToastMessage(context, "您还没有登录！");
			return;
		}
		if (!MyApplication.getNetObject().isNetConnected()) {
			ShowContentUtils
					.showShortToastMessage(context, RequestCode.NOLOGIN);
			return;
		}
		if (progress == null) {
			progress = MyProgressDialog.createDialog(this);
		}
		progress.show();
		if (http == null) {
			http = new HttpUtil(handler);
		}
		RequestParams params = http.getParams(WebUrlConfig.addNews());
		params.addParameter("userID", MyApplication.loginModel.getUserID());
		params.addParameter("title", title);
		params.addParameter("content", content);
		params.addParameter("isHide", type);
		for(int i=0;i<paths.size();i++){
			if(TextUtils.isEmpty(paths.get(i))){
				return;
			}
			params.addBodyParameter("img", new File(paths.get(i)));
		}
//		if(paths.size()==0){
//			params.addBodyParameter("img",new File());
//		}
		http.uploadFile(RequestCode.BAOLIAO, params);
	}

	private List<String> paths=new ArrayList<String>();// 返回的相片路径list
	private int number = 0;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String path=null;
		// 相册返回调用
		if (requestCode == PhotoCameraDialog.IMAGE_CODE
				&& resultCode == Activity.RESULT_OK) {
			if (data != null) {
				Uri originalUri = data.getData(); // 获得图片缩略图的uri
				switch (number) {
				case 0:
					path = ImageUtil.setPhoto(context, originalUri, img1);
					number++;
					img1.setTag(path);
					paths.add(path);
					break;
				case 1:
					path = ImageUtil.setPhoto(context, originalUri, img2);
					number++;
					img2.setTag(path);
					paths.add(path);
					break;
				case 2:
					path = ImageUtil.setPhoto(context, originalUri, img3);
					number++;
					img3.setTag(path);
					paths.add(path);
					break;
				case 3:
					path = ImageUtil.setPhoto(context, originalUri, img4);
					number++;
					img4.setTag(path);
					paths.add(path);
					break;
				case 4:
					path = ImageUtil.setPhoto(context, originalUri, img5);
					number++;
					img5.setTag(path);
					paths.add(path);
					break;
				case 5:
					path = ImageUtil.setPhoto(context, originalUri, img6);
					number++;
					img6.setTag(path);
					paths.add(path);
					break;
				default:
					break;
				}

			}

		}

		// 相机返回调用
		else if (requestCode == PhotoCameraDialog.TAKE_PICTURE
				&& resultCode == Activity.RESULT_OK) {
			switch (number) {
			case 0:
				path = ImageUtil.setCameraPhoto(context, photo, img1);
				number++;
				img1.setTag(path);
				paths.add(path);
				break;
			case 1:
				path = ImageUtil.setCameraPhoto(context, photo, img2);
				number++;
				img2.setTag(path);
				paths.add(path);
				break;
			case 2:
				path = ImageUtil.setCameraPhoto(context, photo, img3);
				number++;
				img3.setTag(path);
				paths.add(path);
				break;
			case 3:
				path = ImageUtil.setCameraPhoto(context, photo, img4);
				number++;
				img4.setTag(path);
				paths.add(path);
				break;
			case 4:
				path = ImageUtil.setCameraPhoto(context, photo, img5);
				number++;
				img5.setTag(path);
				paths.add(path);
				break;
			case 5:
				path = ImageUtil.setCameraPhoto(context, photo, img6);
				number++;
				img6.setTag(path);
				paths.add(path);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 查看图片
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		if(v.getTag()==null){
			return;
		}
		Intent it = new Intent(Intent.ACTION_VIEW);
		String path = v.getTag().toString();
		it.setDataAndType(Uri.fromFile(new File(path)), "image/*");
		startActivity(it);
	}

	@Event(value = { R.id.base_back_lay, R.id.back_tv, R.id.pubulic,
			R.id.anonymity, R.id.refund_sub, R.id.add_pic }, type = View.OnClickListener.class)
	private void viewClick(View v) {
		if (v == base_back_lay) {
			finish();
		}
		if (v == back_tv) {
			finish();
		}
		if (v == pubulic) {
			textUtil.setDrawable(pubulic, R.drawable.address_is_default_press,
					0);
			textUtil.setDrawable(anonymity,
					R.drawable.address_is_default_normal, 0);
			type = 2;
		}
		if (v == anonymity) {
			textUtil.setDrawable(pubulic, R.drawable.address_is_default_normal,
					0);
			textUtil.setDrawable(anonymity,
					R.drawable.address_is_default_press, 0);
			type = 1;
		}
		if (v == add_pic) {
			photo = new PhotoCameraDialog(this, layout);
		}
		if (v == submit) {
			title = titleView.getText().toString();
			content = contentView.getText().toString();
			if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
				ShowContentUtils.showShortToastMessage(this, "标题和内容不能为空！");
				return;
			}
			loadNet();
		}

	}
}

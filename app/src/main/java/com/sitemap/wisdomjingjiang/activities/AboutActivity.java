package com.sitemap.wisdomjingjiang.activities;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.R.layout;
import com.sitemap.wisdomjingjiang.R.menu;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.utils.ImageFactory;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 关于 页面
 * 
 * @author chenmeng
 * 
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
	/**本类*/ 
	private AboutActivity mContext;
	/** 返回上一层 */
	private ImageView mBack;
	/** 标题 */
	private TextView mTitle;
	/** 图片 */
	private ImageView mAboutImg;
	/** 版本 */
	private TextView mVersion;
	/**打电话*/ 
	private TextView mCallPhone;
	/**弹出的对话框*/ 
	private Builder mShowDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		mContext = this;
		initView();
		initData();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mTitle = (TextView) findViewById(R.id.about_tilte).findViewById(
				R.id.title);
		mBack = (ImageView) findViewById(R.id.about_tilte).findViewById(
				R.id.back);
		mAboutImg = (ImageView) findViewById(R.id.about_img);
		mVersion = (TextView) findViewById(R.id.about_card);
		mCallPhone =  (TextView) findViewById(R.id.about_call_phone);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mTitle.setText("关于");
		mBack.setOnClickListener(this);
		mCallPhone.setOnClickListener(this);
		Bitmap bt = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);
		Bitmap img = ImageFactory.toRoundCorner(bt, 100);
		mAboutImg.setImageBitmap(img);
			try {
				mVersion.setText("V "+ getVersionName());
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	@Override
	public void onClick(View v) {
		if (v == mBack) {
			onBackPressed();
			finish();
		}
		if(v == mCallPhone){
			mShowDialog = MyApplication.myAlertDialog(mContext, true,"提示", "你确定要向客服拨打电话吗?", new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent();
				    intent.setAction("android.intent.action.DIAL");
				    intent.setData(Uri.parse("tel:"+ MyApplication.WuQuanTel));
				    startActivity(intent);
					dialog.dismiss();
				}
			});
			mShowDialog.create().show();
		}
	}

	/**
	 * 获取当前应用的版本号：
	 * @return
	 * @throws Exception
	 */
	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
		String version = packInfo.versionName;
		return version;
	}

}

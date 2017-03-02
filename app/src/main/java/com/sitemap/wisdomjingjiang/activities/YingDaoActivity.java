package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.ViewPagerAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * com.sitemap.wisdomjingjiang.activities.YingDaoActivity
 * 
 * @author zhang 引导页 create at 2016年4月27日 上午11:30:09
 */
public class YingDaoActivity extends BaseActivity implements OnClickListener {
	// 定义ViewPager对象
	private ViewPager viewPager;
	// 定义ViewPager适配器
	private ViewPagerAdapter vpAdapter;
	// 定义一个ArrayList来存放View
	private ArrayList<View> views;
	// 引导图片资源
	private final int[] PICTURES = { R.drawable.yingdao01,
			R.drawable.yingdao02, R.drawable.yingdao03};
	private TextView[] points;// 页面底部小圆点
	private TextView tempPoint;// 选择的小圆点
	private LinearLayout pointsLayout, ad_layout;// 页面底部点的布局,广告的布局
	private ImageButton ad_image;// 广告图控件
	private long AD_TIME = 4000;// 4秒,广告显示时间
	private SharedPreferences preferences;// 系统参数配置文件
	private Button enter;// 进入主页面按钮
	private Runnable adBreak;// 进入主界面线程
	private Handler mHandler;// Handler控制器
	private PackageManager manager;// 应用程序包
	private PackageInfo info = null;// APP信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yingdao);
		preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		mHandler = new Handler();
		manager = this.getPackageManager();
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
			MyApplication.versionName = info.versionName;
		} catch (NameNotFoundException e) {

			e.printStackTrace();

		}
		initView();
		initData();
		// // 延迟后进入活动页面
		// new Handler().postDelayed(new Runnable() {
		//
		// public void run() {
		//
		// Intent intent = new Intent(YingDaoActivity.this,
		// MainActivity.class);
		// startActivity(intent);
		// finish();
		// }
		// }, 1000);
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		// 实例化ArrayList对象
		views = new ArrayList<View>();
		// 实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		// 实例化ViewPager适配器
		vpAdapter = new ViewPagerAdapter(views);
		pointsLayout = (LinearLayout) findViewById(R.id.viewpager_point);
		ad_layout = (LinearLayout) findViewById(R.id.ad_layout);
		ad_image = (ImageButton) findViewById(R.id.ad_image);
		enter = (Button) findViewById(R.id.enter);
		enter.setOnClickListener(this);
		if (!preferences.getString("versionName", "").equals(info.versionName)) {
			Editor editor = preferences.edit();
			editor.putBoolean("isfirst", false);
			editor.putBoolean("firstfriend", true);
			editor.commit();
		}

		if (preferences.getBoolean("isfirst", false)) {
			// 广告展示
			viewPager.setVisibility(View.GONE);
			pointsLayout.setVisibility(View.GONE);
			ad_layout.setVisibility(View.VISIBLE);
//			String ad_imageUrl = preferences.getString("ad_image", "");
//			if (ad_imageUrl != "") {
////				BitmapUtils util = new BitmapUtils(this);
//				x.image().bind(ad_image, ad_imageUrl);
////				util.display(ad_image, ad_imageUrl);
//				addAlphaAnimation(ad_image);
//			}else{
//				ad_image.setImageResource(R.drawable.guanggao);
//				addAlphaAnimation(ad_image);
//			}
//			// 延迟2秒后进入主页面
//			adBreak = new Runnable() {
//
//				public void run() {
					openHomePage();
//				}
//			};
//			mHandler.postDelayed(adBreak, AD_TIME);
		}
		// 进入首次介绍页
	}

	/**
	 * 初始化数据(viewPager)
	 */
	private void initData() {
		// 定义一个布局并设置参数
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		points = new TextView[PICTURES.length];
		// 初始化引导图片列表
		for (int i = 0; i < PICTURES.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			// 防止图片不能填满屏幕
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setImageResource(PICTURES[i]);
			views.add(iv);
			// 添加页面底部导航点
			points[i] = addPoints();
		}
		// 设置默认起始点(页面底部小圆点)
		tempPoint = points[0];
		tempPoint.setBackgroundResource(R.drawable.point_select);

		// 设置数据适配器
		viewPager.setAdapter(vpAdapter);
		// 设置监听
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	/**
	 * 
	 * @param view
	 *            ，为此控件添加渐变动画
	 */
	private void addAlphaAnimation(ImageButton view) {
		AlphaAnimation animation = new AlphaAnimation(0f, 1f);
		animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
		animation.setStartOffset(500);// 执行前的等待时间
		animation.setDuration(1500);
		view.startAnimation(animation);
	}

	/**
	 * 打开主页面
	 */
	private void openHomePage(int... a) {
		Intent intent = new Intent(YingDaoActivity.this, MainActivity.class);
		if (a.length > 0) {
			intent.putExtra("first", true);
		}
		startActivity(intent);
		finish();
	}

	/**
	 * 添加轮播图片导航点
	 */
	private TextView addPoints() {
		TextView point = new TextView(this);
		point.setBackgroundResource(R.drawable.point_normal);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(20, 20);
		param.leftMargin = 10;
		param.bottomMargin = 5;
		point.setLayoutParams(param);
		pointsLayout.addView(point);
		return point;
	}

	/**
	 * 切换轮播图片导航点
	 */
	private void transform(int position) {
		points[position].setBackgroundResource(R.drawable.point_select);
		tempPoint.setBackgroundResource(R.drawable.point_normal);
	}

	/**
	 * 
	 * 轮播图片切换监听
	 */
	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			transform(position);
			tempPoint = points[position];
			if (position == (PICTURES.length - 1)) {
				// 滑动到最后一张
				enter.setVisibility(View.VISIBLE);
			} else {
				enter.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.enter) {
			openHomePage(1);

		}
		/**
		 * 广告图片按钮
		 */
		if (v.getId() == R.id.ad_image) {
			// 进入广告页面
			String ad_link = preferences.getString("ad_link", "");
			if (ad_link != "") {
				// 中断原有线程
				mHandler.removeCallbacks(adBreak);
				// 友盟统计
				Intent intent = new Intent(this, WebViewActivity.class);
				intent.putExtra("url", ad_link);
				startActivity(intent);
//				友盟统计
//				MobclickAgent.onEvent(this, "ad_onclick");
				// 关闭原有界面
				this.finish();
			}
		}
	}

}

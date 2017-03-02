package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.Set;

import org.xutils.x;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.MyViewPagerAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.fragments.HomePageFragment;
import com.sitemap.wisdomjingjiang.fragments.MerchantFragment;
import com.sitemap.wisdomjingjiang.fragments.MySelfFragment;
import com.sitemap.wisdomjingjiang.fragments.ShoppingCartFragment;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.LoginModel;
import com.sitemap.wisdomjingjiang.models.UpdateModel;
import com.sitemap.wisdomjingjiang.utils.NoScrollViewPager;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 主类
 * Description: 
 * @author chenhao
 * @date   2016-5-21
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends FragmentActivity implements OnClickListener {
	/**本类*/ 
	public static MainActivity mContext;
//	@ViewInject(R.id.viewpager)
//	private NoScrollViewPager mViewPager;// ViewPager对象
	private HomePageFragment f1;// 第一个子页面 首页
	private MerchantFragment f2;// 第二个子页面 分类
	private ShoppingCartFragment f3;// 第三个子页面  购物车
	private MySelfFragment f4;// 第四个子页面  我的
	private ArrayList<Fragment> mMenuList;// 子页面列表
	@ViewInject(R.id.home_pager)
	private LinearLayout mHomePage; // 子页面按钮
	@ViewInject(R.id.merchant)
	private LinearLayout mMerchant;
	@ViewInject(R.id.shopping_cart)
	private LinearLayout mShopingCart;
	@ViewInject(R.id.my_self)
	private LinearLayout mMySelf;
	private long exitTime = 0;
	private SharedPreferences preferences;//储存器
	private HttpUtil http;// 网络请求
	private static final int MSG_SET_ALIAS = 5001;//极光设置 别名
	private UpdateModel um;//更新实体类
	private PackageManager manager;// 应用程序包
	private MyApplication app;// application对象
	private PackageInfo info = null;// APP信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		 x.view().inject(this);
		initview();
		initdate();

	}

	/**
	 * @Description 内容描述
	 * 初始化控件
	 */
	private void initview() {
		MyApplication.mViewPager=(NoScrollViewPager)findViewById(R.id.viewpager);
		f1 = new HomePageFragment();
		f2 = new MerchantFragment();
		f3 = new ShoppingCartFragment();
		f4 = new MySelfFragment();
		mMenuList = new ArrayList<Fragment>();
		mMenuList.add(f1);
		mMenuList.add(f2);
		mMenuList.add(f3);
		mMenuList.add(f4);

		mHomePage.setOnClickListener(this);
		mMerchant.setOnClickListener(this);
		mShopingCart.setOnClickListener(this);
		mMySelf.setOnClickListener(this);

		MyApplication.mViewPager.setAdapter(new MyViewPagerAdapter(
				getSupportFragmentManager(),mMenuList));
		mHomePage.setSelected(true);	
		app = (MyApplication) getApplication();
		try {
			manager = this.getPackageManager();
			info = manager.getPackageInfo(this.getPackageName(), 0);
			MyApplication.versionName = info.versionName;
		} catch (NameNotFoundException e) {

			e.printStackTrace();

		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
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
//						ShowContentUtils.showLongToastMessage(mContext,
//								MyApplication.loginModel.getErrorMsg());
						return;
					} else {
						MyApplication.isLogin = true;
						JPushInterface.setAlias(getApplicationContext(), MyApplication.loginModel.getUserID(), mAliasCallback);
						MyApplication.userPhone=preferences.getString("username", "");
					}
				}
				if (msg.arg1==RequestCode.UPDATE) {
					Log.i("TAG", "requestUpdate:" + msg.obj.toString());
					um=JSON.parseObject(
							msg.obj.toString(), UpdateModel.class);
					if (um!=null&&um.getVersion()!=null) {
						MyApplication.isupdate = false;
						// 是否需要更新，true是，false否。
						if (um.getVersion()!=null) {
							boolean isUpdate = (info.versionName).compareTo(um
									.getVersion()) < 0 ? true : false;
							if (!isUpdate) {
								return;
							} else {
								AlertDialog.Builder customDia = new AlertDialog.Builder(
										MainActivity.this);
								customDia.setCancelable(false);
								final View viewDia = LayoutInflater.from(
										MainActivity.this).inflate(
										R.layout.alertdialog_update, null);
								TextView update_con = (TextView) viewDia
										.findViewById(R.id.update_con);
								if (um.getContent() != null
										&& !um.getContent().equals("")) {
									String update_txt[] = um.getContent()
											.split(";");
									StringBuffer sb = new StringBuffer("");
									for (int i = 0; i < update_txt.length; i++) {
										sb.append(update_txt[i] + "\n");
									}
									update_con.setText(sb.toString());
								}
								if (um.getDownloadUrl()!=null&&um.getDownloadUrl()!="") {
									MyApplication.downUrl = um.getDownloadUrl();
								}
								if (um.getVersion()!=null&&um.getVersion()!="") {
									MyApplication.versionName = um.getVersion();
								}
								
							
								customDia.setView(viewDia);
								customDia.setPositiveButton("现在更新",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												
												Intent it = new Intent(
														MainActivity.this,
														NotificationUpdateActivity.class);
//												if (um.getIsForceUpdate()!=null&&um.getIsForceUpdate().equals("0")) {
//													it.putExtra("isforce", "0");//需要强制更新
//												}
												app.setDownload(true);
												startActivity(it);
												// startService(it);
												// bindService(it, conn,
												// Context.BIND_AUTO_CREATE);
											
												dialog.dismiss();
											}
										});
								//需要强制更新
								
//								if (um.getIsForceUpdate()!=null&&um.getIsForceUpdate().equals("0")) {
//									customDia.setNegativeButton("关闭程序",
//											new DialogInterface.OnClickListener() {
//												@Override
//												public void onClick(
//														DialogInterface dialog,
//														int which) {
//													exit();
//													dialog.dismiss();
//												}
//											});
//								}else {
									customDia.setNegativeButton("以后再说",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											});
//								}
								
								customDia.create().show();
							}
						}
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
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
	 * 数据初始化
	 */
	private void initdate(){
		 preferences = getSharedPreferences("user",
					Context.MODE_PRIVATE);
			
			if (getIntent().getBooleanExtra("first", false)) {
				Editor editor = preferences.edit();
				editor.putString("versionName", info.versionName);
				editor.putBoolean("isfirst", true);
				editor.commit();
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
		
		String update = WebUrlConfig.updateVersion();
		http.sendGet(RequestCode.UPDATE, update);
				
	}


	@Override
	public void onClick(View v) {
		resetBottomBg();
		if (v == mHomePage) {	
			mHomePage.setSelected(true);			
			MyApplication.mViewPager.setCurrentItem(0);
		}
		if (v == mMerchant) {
			mMerchant.setSelected(true);	
			MyApplication.mViewPager.setCurrentItem(1);
		}
		if (v == mShopingCart) {
			mShopingCart.setSelected(true);
			if (MyApplication.isLogin) {
				MyApplication.mViewPager.setCurrentItem(2);
			}else {
				Intent intent=new Intent(MainActivity.this,LoginActivity.class);
				startActivity(intent);
			}
			
		}
		if (v == mMySelf) {
			mMySelf.setSelected(true);
			if (MyApplication.isLogin) {
				MyApplication.mViewPager.setCurrentItem(3);
			}else {
				Intent intent=new Intent(MainActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		}
		  WindowManager windowManager = getWindowManager();    
	        Display display = windowManager.getDefaultDisplay();    
	        int screenWidth = screenWidth = display.getWidth();    
	        int screenHeight = screenHeight = display.getHeight();  
	        Log.i("TAG", "screenWidth:"+screenWidth);
	        Log.i("TAG", "screenHeight:"+screenHeight);
	}
	
	/**
	 * 重置底部按钮背景
	 * @param position
	 */
	private void  resetBottomBg(){
		mHomePage.setSelected(false);	
		mMerchant.setSelected(false);	
		mShopingCart.setSelected(false);	
		mMySelf.setSelected(false);	
	}
	
	
	/**
	 *	退出activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序!",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				//退出所有的activity
				Intent intent = new Intent();
				intent.setAction(BaseActivity.TAG_ESC_ACTIVITY);
				sendBroadcast(intent);
				System.exit(0);
				finish();
			}		
			return true;
		}
		return super.onKeyDown(keyCode, event);				
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
    				ShowContentUtils.showShortToastMessage(mContext, "网络无法连接！");
    			}
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
                Log.e("TAG", logs);
            }
            
//            CommonToast.showShortToastMessage(context, logs);
        }
	    
	};
	
	/**
	 *	设置选中商家 
	 */
	public  void setCheck(int...a){
		resetBottomBg();
		if (a.length>0) {
			mHomePage.setSelected(true);
		}else {
			mMerchant.setSelected(true);
		}
		
	}
	
	/**
	 * 设置选中购物车
	 */
	public void setShopCartCheck(){
		resetBottomBg();
		mShopingCart.setSelected(true);
		MyApplication.mViewPager.setCurrentItem(2);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(getIntent().getIntExtra("type", 0)==1){
			setShopCartCheck();
		}
	}
	
}

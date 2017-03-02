package com.sitemap.wisdomjingjiang.application;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xutils.BuildConfig;
import org.xutils.x;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.alipay.AliPayHandler;
import com.sitemap.wisdomjingjiang.alipay.AliPayHelper;
import com.sitemap.wisdomjingjiang.config.WebHostConfig;
import com.sitemap.wisdomjingjiang.db.DBCheckNews;
import com.sitemap.wisdomjingjiang.models.LoginModel;
import com.sitemap.wisdomjingjiang.models.NewsTypeModel;
import com.sitemap.wisdomjingjiang.utils.NetworkState;
import com.sitemap.wisdomjingjiang.utils.NoScrollViewPager;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.wxapi.WechatPayHandler;
import com.sitemap.wisdomjingjiang.wxapi.WechatPayHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * com.sitemap.wisdomjingjiang.application.MyApplication
 * @author zhang
 * 应用程序application类，配置一些全局变量或对象；
 * create at 2016年4月26日 上午9:40:04
 */
public class MyApplication extends Application {
	/**application对象*/ 
	private static MyApplication _instance;
	/**网络对象*/ 
	private static NetworkState netState;
	public static int codeNum = 60;//验证码
	public static String versionName;//版本号
	/**是否登录*/ 
	public static boolean isLogin=false;
	public static LoginModel loginModel;//用户登录资料
	public static List<NewsTypeModel> lnewsTypeModel=new ArrayList<NewsTypeModel>();//新闻类别列表
	/**纬度*/ 
	public static double lat;
	/**经度*/ 
	public static double lng;
	public static String userPhone="";//用户电话
	public static NoScrollViewPager mViewPager;// ViewPager对象
	public static String share=WebHostConfig.getHostAddress();//分享的地址
	private boolean isDownload;//是否在下载
	public static String downUrl="";//下载地址
	public static double length;//apk大小
	public final static String TUAN="团购"; 
	public static boolean isupdate=true;//是否请求更新
	public static String WuQuanTel = "18052633456";
	public static DBCheckNews db;//数据库
	
	public static MyApplication instance() {
		if (_instance != null) {
			return _instance;
		} else {
			return new MyApplication();
		}
	}
	
	
	
	public boolean isDownload() {
		return isDownload;
	}



	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}



	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("init_success", "<<<<<----------------初始化成功---------------->>>>>");
		SDKInitializer.initialize(getApplicationContext()); //百度地图初始化
//		//初始化极光推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		netState = new NetworkState(this.getApplicationContext());
		if(db==null){
			db = new DBCheckNews(this.getApplicationContext());
		}
		initXutils();
	}
	
	/**
	 * 初始化xutils框架
	 */
	private void initXutils() {
	    x.Ext.init(this);
	    x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
	}
	
	
	/**
	 * 必须是数字和字母组合
	 * 密码的设置规则
	 * @param pwd
	 * @return
	 */
	public static boolean isPWD(String pwd){
//		Pattern p = Pattern.compile("^[A-Za-z0-9|_]{6,16}$");
		Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}
	
	/**
	 * 验证手机号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|170|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	/**
	 * 重新计算ListView的高度， 解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
	 * 
	 * @param listView
	 */
	public static void setListViewHeight(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目			
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	/**
	 * 自定义系统提示框
	 * @param context 弹出的类
	 * @param ifcancle 是否有取消
	 * @param title 标题
	 * @param message 内容
	 * @param listen 确定监听
	 */
	public static AlertDialog.Builder myAlertDialog(Context context,boolean ifcancle,String title,String message,DialogInterface.OnClickListener listen){
		AlertDialog.Builder normalDia = new AlertDialog.Builder(
				context);
		normalDia.setTitle(title);
		normalDia.setMessage(message);

		normalDia.setPositiveButton("确定",listen);
		if (ifcancle) {
		
		normalDia.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		}
		return normalDia;
	}
	
	/**
	 * 验证输入框是否为空
	 * @param ed
	 * @return
	 */
	public static boolean isNull(EditText ed){
		if (ed.getText().toString().trim()==null||ed.getText().toString().trim().equals("")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取手机网络状态对象
	 * 
	 * @return
	 */
	public static NetworkState getNetObject() {
		if (netState != null) {
			return netState;
		} else {
			return new NetworkState(instance().getApplicationContext());
		}
	}
	
	
	/**
	 * MD5加密，32位
	 * @param str
	 * @return
	 */
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString().toUpperCase();
	}
	
	/**
	 * 自定义输入对话框
	 * @param context 所在的activity
	 * @param pListener 确定事件
	 * @param nListener 取消事件
	 * @return 返回输入对话框的view
	 */
	public static View inputDialog(Context context,
			DialogInterface.OnClickListener pListener,	
			DialogInterface.OnClickListener nListener){
		Builder buidler = new Builder(context);
		View view = LayoutInflater.from(context)
				.inflate(R.layout.alertdialog_edittext, null);
		buidler.setView(view);
		buidler.setPositiveButton("确定",pListener);
		buidler.setNegativeButton("取消",nListener);
		buidler.create().show();
		return view;
	}
	
	/**
	 * 自定义 提示对话框  （订单退款）
	 * @param context
	 * @param txt
	 * @return
	 */
	public static View showDialog(final Context context,
			DialogInterface.OnClickListener refund
			){
		AlertDialog.Builder customDia = new AlertDialog.Builder(context);
		customDia.setCancelable(false);
		View viewDia = LayoutInflater.from(context).inflate(R.layout.show_order_refund_item, null);
//		TextView breif = (TextView) viewDia.findViewById(R.id.show_txt);
//		breif.setText(txt);
		customDia.setView(viewDia);
		customDia.setPositiveButton("申请退款", refund);
		customDia.setNegativeButton("申请仲裁", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
			    intent.setAction("android.intent.action.DIAL");
			    intent.setData(Uri.parse("tel:"+ MyApplication.WuQuanTel));
			    context.startActivity(intent);
			    dialog.dismiss();
			}
		});
		customDia.create().show();
		return viewDia;
	}
	
	/**横向的图片*/ 
	public static ImageOptions imageOptions =new ImageOptions.Builder()
	        .setSize(-1, -1)//图片大小
//	        .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
//	        .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
	        .setImageScaleType(ImageView.ScaleType.FIT_XY)
	        .setLoadingDrawableId(R.drawable.tops_bg)//加载中默认显示图片
	        .setFailureDrawableId(R.drawable.tops_bg)//加载失败后默认显示图片
	        .build();
	
	/**等比例的图片*/ 
	public static ImageOptions imageOptionsZ =new ImageOptions.Builder()
	 .setSize(-1, -1)//图片大小
//	        .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
//	        .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
//	        .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
			.setImageScaleType(ImageView.ScaleType.FIT_XY)
	        .setLoadingDrawableId(R.drawable.tops_bg_2)//加载中默认显示图片
	        .setFailureDrawableId(R.drawable.tops_bg_2)//加载失败后默认显示图片
	        .build();
	
	/**竖向的显示图片*/ 
	public static ImageOptions imageOptionsV =new ImageOptions.Builder()
	 .setSize(-1, -1)//图片大小
//    .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
//    .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
    .setImageScaleType(ImageView.ScaleType.FIT_XY)
    .setLoadingDrawableId(R.drawable.tops_v_bg)//加载中默认显示图片
    .setFailureDrawableId(R.drawable.tops_v_bg)//加载失败后默认显示图片
    .build();
	
	/**评论头像*/ 
	public static ImageOptions imageComment =new ImageOptions.Builder()
	 .setSize(-1, -1)//图片大小
//	        .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
//	        .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
	        .setImageScaleType(ImageView.ScaleType.FIT_XY)
	        .setLoadingDrawableId(R.drawable.user_comment)//加载中默认显示图片
	        .setFailureDrawableId(R.drawable.user_comment)//加载失败后默认显示图片
	        .build();
	
	/**
	 * 画点
	 * @param mContext 	本类	
	 * @param layout	圆点的布局	
	 * @param list		圆点的集合
	 * @param len		圆点的数量
	 */
	public static void drawPoint(Context mContext,LinearLayout layout,List<View> list,int len){
		list.clear();
		layout.removeAllViews();		
		for (int i = 0; i < len; i++) {			
			ImageView dot = new ImageView(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 0, 5, 0);
			if (i == 0) {
				dot.setBackgroundResource(R.drawable.point_indictor_unselect);
			} else {
				dot.setBackgroundResource(R.drawable.point_indictor_select);
			}
			layout.addView(dot, params);
			list.add(dot);
		}
	}
	
	/**
	 * 支付方式
	 * @param orderID  订单id
	 * @param allMoney 价格
	 * @param express 运费
	 * @param payType 支付方式
	 * @param mContext 本类
	 */
	public static void payMoney(String orderID,String allMoney, String express,int payType,Activity mContext) {
		// 提交订单成功 支付
		if (payType == 1) {// 支付宝支付
			AliPayHelper aliPayHelper = null;// 付款工具
			AliPayHandler aliPayHandler = null;// 付款异步处理类 // 注意以下代码顺序不可变
			if (aliPayHandler == null) {
				aliPayHandler = new AliPayHandler(mContext);
			}
			if (aliPayHelper == null) {
				aliPayHelper = new AliPayHelper(mContext, aliPayHandler);
			}
			aliPayHelper.pay(orderID, "", "", allMoney, express);
		}
		if (payType == 2) {// 微信支付
			WechatPayHelper wechatPayHelper = null;// 付款工具
			WechatPayHandler wechatPayHandler = null;// 付款异步处理类
			MyProgressDialog progressDialog = null;
			// 注意以下代码顺序不可变
			if (wechatPayHandler == null) {
				progressDialog = MyProgressDialog.createDialog(mContext);
				wechatPayHandler = new WechatPayHandler(mContext,
						progressDialog);
			}
			if (wechatPayHelper == null) {
				wechatPayHelper = new WechatPayHelper(wechatPayHandler);
			}
			wechatPayHelper.pay(mContext,loginModel.getUserID(), allMoney, orderID, express,progressDialog);
		}
	}
	
	/**
	 * 判断支付方式客户端 是否存在
	 * @param payType
	 * @param mContext
	 * @return
	 */
	public static boolean isExistPackage(int payType,Activity mContext){
		if (payType == 1) {// 支付宝支付
			AliPayHelper aliPayHelper = null;// 付款工具
			AliPayHandler aliPayHandler = null;// 付款异步处理类 // 注意以下代码顺序不可变
			if (aliPayHandler == null) {
				aliPayHandler = new AliPayHandler(mContext);
			}
			if (aliPayHelper == null) {
				aliPayHelper = new AliPayHelper(mContext, aliPayHandler);
			}
			return aliPayHelper.checkBrowser();
		}
		if (payType == 2) {// 微信支付
//			WechatPayHelper wechatPayHelper = null;// 付款工具
			WechatPayHandler wechatPayHandler = null;// 付款异步处理类
			MyProgressDialog progressDialog = null;
			// 注意以下代码顺序不可变
			if (wechatPayHandler == null) {
				progressDialog = MyProgressDialog.createDialog(mContext);
				wechatPayHandler = new WechatPayHandler(mContext,
						progressDialog);
			}
//			if (wechatPayHelper == null) {
//				wechatPayHelper = new WechatPayHelper(wechatPayHandler);
//			}
			return wechatPayHandler.checkEnvironment();
		}
		return false;
	}
}

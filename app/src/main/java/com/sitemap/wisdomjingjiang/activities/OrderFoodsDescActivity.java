package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.OrderFoodsDescListAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.OrderFoodsModel;
import com.sitemap.wisdomjingjiang.utils.FileUtils;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author	chenmeng
 *	订单  团购详情页面
 */
public class OrderFoodsDescActivity extends BaseActivity implements OnClickListener{
	/**全局类*/ 
	private OrderFoodsDescActivity mContext;
	/**标题栏 返回上一层  */ 
	private ImageView mBack;
	/**标题栏 标题*/ 
	private TextView mTitle;
	/**付款*/ 
	private TextView mPay;
	/**取消订单*/ 
	private TextView mOrderCancel;
	/**商家名称*/ 
	private TextView mShopsName;
	/**团购列表 listview*/ 
	private ListView mFoodsLv;
	/**运费  总费用  创建事件 ,联系电话*/ 
	private TextView mOrderExpress,mOrderMoney,mOrderCreateTime,mShopTel;
	/**订单id*/ 
	private TextView mOrderID;
	/**商品的foodsinfo的实体类*/ 
	private CartFoodsInfoModel model;
	/**订单中的团购商品订单的返回实体类*/ 
	private OrderFoodsModel foodsModel;
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/**删除 时弹出的对话框*/ 
	private Builder mDeleteDialog;
	/** 订单类型(0：全部 1:待付款2:待使用 3:未评价)*/
	private int type;
	/**用户id*/ 
	private String userID = MyApplication.loginModel.getUserID();
	/**商家id*/ 
	private String shopID;
	/**是否显示  支付方式  以及 钱*/ 
	private LinearLayout mShowPay,mShowMoney;
	/**支付宝  微信*/ 
	private RelativeLayout mAlipy,mWxapi;
	/**支付方式  1、为支付宝  2、为微信支付*/ 
	private int payType = 1;
	/**总金额*/ 
	private TextView allMoney;
	/**总金额*/ 
	private String sunMoney = "0.01";
	/**运费*/ 
	private String sunExpress = "0.00";
	private boolean count=false;
	private int num=0;//支付后页面启动次数
	/**订单id*/ 
	private String orderID;
	/**打电话 按钮*/ 
	private RelativeLayout mShopPhone;
	/**团购券 验证码  截止时间*/ 
	private TextView mOrderCode,mReadTime;
	/**验证码是否显示 和截止时间*/ 
	private LinearLayout isShowCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_foods_desc);
		mContext = this;
		initView();//初始化view
		initData();//初始化数据
	}
	
	@Override
	public void onResume() {
		mAlipy.setSelected(true);
		super.onResume();
		if(orderID!=null){
			if(payType==1){
				if(count){
					num++;	
					if(num>1){
						try {
							Thread.currentThread().sleep(2000);
							http.sendGet(RequestCode.ORDERSTATUS, WebUrlConfig.getOrderStatus(orderID));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}					
					}
				}
			}
			if(payType==2){
				try {
					Thread.currentThread().sleep(2000);
					http.sendGet(RequestCode.ORDERSTATUS, WebUrlConfig.getOrderStatus(orderID));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
			}													
		}
	}
	
	/**
	 * 初始化view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.order_desc_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.order_desc_title).findViewById(R.id.title);
		mPay = (TextView) findViewById(R.id.order_desc_pay);
		mOrderCancel = (TextView) findViewById(R.id.order_desc_cancel);
		mShopsName = (TextView) findViewById(R.id.order_shops_name);
		mFoodsLv = (ListView) findViewById(R.id.order_foods_listview);
		mOrderExpress = (TextView) findViewById(R.id.order_shops_express);
		mOrderMoney = (TextView) findViewById(R.id.order_shops_money);
		mOrderID = (TextView) findViewById(R.id.order_shops_order_id);
		mOrderCreateTime = (TextView) findViewById(R.id.order_shops_create_time);
		mShowPay = (LinearLayout) findViewById(R.id.show_pay);
		mShowMoney = (LinearLayout) findViewById(R.id.re1);
		mAlipy = (RelativeLayout) findViewById(R.id.goods_pay_alipy);
		mWxapi = (RelativeLayout) findViewById(R.id.goods_pay_wechat);
		allMoney = (TextView) findViewById(R.id.order_all_money);
		mShopTel = (TextView) findViewById(R.id.order_shop_name_tel);
		mShopPhone = (RelativeLayout) findViewById(R.id.order_shop_tel);
		mOrderCode = (TextView) findViewById(R.id.order_foods_code);
		isShowCode = (LinearLayout) findViewById(R.id.is_show_code);
		mReadTime = (TextView) findViewById(R.id.order_foods_dead_time);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mTitle.setText(R.string.order_desc_title);
		mBack.setOnClickListener(this);
		mPay.setOnClickListener(this);
		mOrderCancel.setOnClickListener(this);
		mWxapi.setOnClickListener(this);
		mAlipy.setOnClickListener(this);
		mAlipy.setSelected(true);
		mShopPhone.setOnClickListener(this);
		
		if (http == null) {
			http = new HttpUtil(handler);
		}
		
		model = (CartFoodsInfoModel) getIntent().getSerializableExtra("CartFoodsInfoModel");
		foodsModel = (OrderFoodsModel) getIntent().getSerializableExtra("OrderFoodsModel");
		mOrderExpress.setText(model.getExpress());
		mShopsName.setText(foodsModel.getFoodShopName());		
		mOrderID.setText(foodsModel.getOrderName());
		mOrderCreateTime.setText(foodsModel.getCreateTime());
		mShopTel.setText(foodsModel.getShopMobile());
		
		if(!model.getCouponCode().equals("")){
			mOrderCode.setText("团购券验证码为："+model.getCouponCode());
		}
		if(!TextUtils.isEmpty(model.getDeadTime())){
			mReadTime.setText("团购券截止时间："+model.getDeadTime());
		}
		
		String foodsList = getIntent().getStringExtra("foodsList");
		List<CartFoodsInfoModel> list = JSON.parseArray(foodsList,CartFoodsInfoModel.class);
		showType();
		if(getIntent().getIntExtra("isShow", 0)==1){//子点击事件
			mPay.setVisibility(View.GONE);
			mOrderCancel.setVisibility(View.GONE);
			mShowPay.setVisibility(View.GONE);
			mShowMoney.setVisibility(View.GONE);
			list.clear();
			list  = new ArrayList<CartFoodsInfoModel>();
			list.add(model);
		}
		//初始化 listview		
		initFoodsListView(list);
		//计算总金额
		double money = 0;
		for(int i=0;i<list.size();i++){
			int num = 1;
			double price = 0;
			if(!model.getNumber().equals("")&&!model.getFoodsPrice().equals("")){
				num = Integer.parseInt(list.get(i).getNumber());
				price = Double.parseDouble(list.get(i).getFoodsPrice());
			}
			money = money+(num*price);
		}
		sunMoney = String.valueOf(FileUtils.roundMath(money));
		allMoney.setText("￥"+sunMoney);
		mOrderMoney.setText("￥"+sunMoney);
		mOrderExpress.setText("￥"+sunExpress);
				
	}
	
	/**
	 * 订单状态(1:待付款 2:待发货 3:待收货 4:未评价 5:已关闭 6:已完成 7:已退款)
	 */
	private void showType() {
		type = Integer.parseInt(foodsModel.getOrderType());	
		if(type==1){//待付款
			mShowPay.setVisibility(View.VISIBLE);
			mShowMoney.setVisibility(View.VISIBLE);
			isShowCode.setVisibility(View.GONE);
		}else
		if(type==2){//已付款
			int refund = Integer.parseInt(model.getIsRefund());
			if(refund==0){//未退款  待使用状态
				isShowCode.setVisibility(View.VISIBLE);
			}else
			if(refund==1){//退款中  退款状态
				isShowCode.setVisibility(View.VISIBLE);
			}else
			if(refund==2){//已退款  退款状态
				isShowCode.setVisibility(View.VISIBLE);
			}else
			if(refund==3){//不能退款  已使用状态
				isShowCode.setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * 初始化listview
	 */
	private void initFoodsListView(List<CartFoodsInfoModel> list){
		OrderFoodsDescListAdapter adapter = new OrderFoodsDescListAdapter(mContext,list);
		mFoodsLv.setAdapter(adapter);
		MyApplication.setListViewHeight(mFoodsLv);
	}
	
	/**
	 * 删除订单
	 */
	private void getDeleteData(String orderID){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String deleteOrder = WebUrlConfig.deleteOrder(orderID,String.valueOf(1));
			RequestParams params = http.getParams(deleteOrder);
			http.sendPost(RequestCode.DELETEORDER, params);
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	/**
	 * 查询库存量
	 */
	private void checkCount(String orderID){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			http.sendGet(RequestCode.GETISINCOUNT, WebUrlConfig.getIsInCount(orderID));
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();// 关闭进度条
			}
			super.handleMessage(msg);
			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				//删除订单
				if(msg.arg1 == RequestCode.DELETEORDER){
					CodeModel model = JSONObject.parseObject(msg.obj.toString(), CodeModel.class);
					if(model.getStatus() == 0){
						setResult(RequestCode.ADDORDER);
						onBackPressed();
						finish();
						ShowContentUtils.showLongToastMessage(mContext, "删除成功");
					}else{
						ShowContentUtils.showLongToastMessage(mContext, model.getErrorMsg());
					}					
				}
				//确认支付状态页面
				if(msg.arg1 == RequestCode.ORDERSTATUS){
					JSONObject ob = JSONObject.parseObject(msg.obj.toString());
					Intent intent = new Intent();
					intent.setClass(mContext, NewsDescActivity.class);
					intent.putExtra("orderUrl", ob.getString("url"));
					intent.putExtra("img", "");//图片
					intent.putExtra("title","支付结果");//标题
					intent.putExtra("type", 3);//订单
					startActivity(intent);
					finish();
				}
				//查询库存量
				if(msg.arg1 == RequestCode.GETISINCOUNT){
					mAlipy.setSelected(true);
					CodeModel model1 = JSONObject.parseObject(
							msg.obj.toString(), CodeModel.class);
					if (model1.getStatus() == 0) {//库存量足
						// 提交订单成功 支付
						orderID = foodsModel.getOrderID();
						MyApplication.payMoney(foodsModel.getOrderID(), 
								sunMoney, sunExpress, 
								payType, mContext);	
						setResult(RequestCode.DELETEORDER);
						count=true;						
					} else {
						ShowContentUtils.showLongToastMessage(mContext,
								"库存量不足，请重新下单!");
						setResult(RequestCode.ADDORDER);
						onBackPressed();
						finish();
//						getDeleteData(model.getOrderInfoID());
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.ADDFOODSORDER) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		mAlipy.setSelected(false);
		mWxapi.setSelected(false);
		if(v == mBack){//返回上一层
			if(orderID!=null){
				return;
			}
			onBackPressed();
			finish();
		}
		if(v == mPay){//付款
			if(type == 3){//待使用(退款)
				//计算退款金额
				String price = model.getFoodsPrice();
				String number = model.getNumber();
				if(price == null || price.equals("")){
					price = "1";
				}
				double money = Double.parseDouble(price)*Double.parseDouble(number);
				Intent intent = new Intent(mContext, RefundActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("OrderFoodsModel", foodsModel);
				intent.putExtras(b);
				intent.putExtra("type",String.valueOf(1));//团购商品
				intent.putExtra("money", money);
				startActivity(intent);
			}
			if(type == 4){//待评价
				Intent intent = new Intent(mContext, PingjiaActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("CartFoodsInfoModel", model);			
				intent.putExtras(b);
				intent.putExtra("type",String.valueOf(1));//团购商品
				startActivity(intent);
			}
			if(type == 1){//提交订单
				if(orderID!=null){
					return;
				}
				if(!MyApplication.isExistPackage(payType, mContext)){//判断有没有安装app
					ShowContentUtils.showLongToastMessage(mContext, "软件没有安装，无法支付");
					return;
				}
				//查询库存量
				checkCount(foodsModel.getOrderID());
//				// 提交订单成功 支付
//				orderID = foodsModel.getOrderID();
//				MyApplication.payMoney(foodsModel.getOrderID(), 
//						sunMoney, sunExpress, 
//						payType, mContext);	
//				setResult(RequestCode.DELETEORDER);
//				count=true;
			}
		}
		if(v == mOrderCancel){//取消订单
			switch (type) {
			case 1:// 删除
				mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定删除吗?", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getDeleteData(model.getOrderInfoID());
						dialog.dismiss();
					}
				});
				mDeleteDialog.create().show();
				break;
			case 3:// 
				break;
			case 4:// 删除
				mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定删除吗?", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getDeleteData(foodsModel.getOrderID());
						dialog.dismiss();
					}
				});
				mDeleteDialog.create().show();
				break;
			default:
				break;
			}
		}
		if(v == mAlipy){//支付宝
			if(orderID!=null){
				return;
			}
			mAlipy.setSelected(true);
			payType = 1;
		}
		if(v == mWxapi){//微信支付
			if(orderID!=null){
				return;
			}
			mWxapi.setSelected(true);
			payType = 2;
		}
		if(v == mShopPhone){//打电话
			if(orderID!=null){
				return;
			}
			if(foodsModel.getShopMobile() != null&&!foodsModel.getShopMobile().equals("")){
				mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"温馨提示", "您确定要给商家打电话吗?", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
					    intent.setAction("android.intent.action.DIAL");
					    intent.setData(Uri.parse("tel:"+foodsModel.getShopMobile()));
					    startActivity(intent);
						dialog.dismiss();
					}
				});
				mDeleteDialog.create().show();				
			}else{
				ShowContentUtils.showLongToastMessage(mContext, "商家暂无电话");
			}
		}
	}
	
}

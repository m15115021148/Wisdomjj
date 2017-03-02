package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.xutils.x;
import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.OrderGoodsDescListAdapter;
import com.sitemap.wisdomjingjiang.alipay.AliPayHandler;
import com.sitemap.wisdomjingjiang.alipay.AliPayHelper;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.AddressModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.OrderGoodsModel;
import com.sitemap.wisdomjingjiang.utils.FileUtils;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.wxapi.WechatPayHandler;
import com.sitemap.wisdomjingjiang.wxapi.WechatPayHelper;

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
 * @author chenmeng 购物订单详情 页面
 */
public class OrderGoodsDescActivity extends BaseActivity implements
		OnClickListener {
	/** 全局类 */
	private OrderGoodsDescActivity mContext;
	/** 标题栏 返回上一层 */
	private ImageView mBack;
	/** 标题栏 标题 */
	private TextView mTitle;
	/** 付款 */
	private TextView mPay;
	/** 取消订单 */
	private TextView mOrderCancel;
	/** 收货人 电话 地址 */
	private TextView mPreson, mPhone, mAddress;
	/** 商家名称 */
	private TextView mShopsName;
	/**商品详情列表 listview*/ 
	private ListView mGoodsLv;
	/** 运费 总费用 创建时间  商家联系电话*/
	private TextView mOrderExpress, mOrderMoney, mOrderCreateTime,mShopTel;
	/** 订单id */
	private TextView mOrderID;
	/** 商品的goodsinfo的实体类 */
	private CartGoodsInfoModel model;
	/** 订单中的购物商品订单的返回实体类 */
	private OrderGoodsModel goodsModel;
	/** 订单状态 1:待付款 2:待发货 3:待收货 4:未评价 5:已关闭 6:已完成 7:已退款 */
	private int type;
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/**删除 时弹出的对话框*/ 
	private Builder mDeleteDialog;
	/**用户id*/ 
	private String userID = MyApplication.loginModel.getUserID();
	/**商家id*/ 
	private String shopID;
	/**地址id*/ 
	private String addressID;
	/**是否显示  支付方式  以及  钱*/ 
	private LinearLayout mShowPay,mshowMoney;
	/**支付宝  微信*/ 
	private RelativeLayout mAlipy,mWxapi;
	/**支付方式  1、为支付宝  2、为微信支付*/ 
	private int payType = 1;
	/**总金额*/ 
	private TextView allMoney;
	/**总金额*/ 
	private String sunMoney = "0.01";
	/**运费*/ 
	private String sunExpress = "0.0";
	/**修改收货地址*/ 
	private RelativeLayout addressUpdate;
	
	private boolean count=false;
	private int num=0;//支付后页面启动次数
	/**订单id*/ 
	private String orderID;
	/**距离收货时间 查看物流*/ 
	private RelativeLayout mSureTime,mLookWuLiu;
	/**退款申请标题  时间*/ 
	private TextView mRefundTitle,mRefundTime;
	/**商家电话事件*/ 
	private RelativeLayout mTelPhone;
	/**物流单号  物流名称*/ 
	private TextView mWuLiuID,mWuLiuName;
	/**物流 进入*/ 
	private TextView mWuLiuIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_goods_desc);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据
	}
	
	@Override
	public void onResume() {
		Log.i("TAG", "onResume");
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
		mBack = (ImageView) findViewById(R.id.order_desc_title).findViewById(
				R.id.back);
		mTitle = (TextView) findViewById(R.id.order_desc_title).findViewById(
				R.id.title);
		mPay = (TextView) findViewById(R.id.order_desc_pay);
		mOrderCancel = (TextView) findViewById(R.id.order_desc_cancel);
		mPreson = (TextView) findViewById(R.id.order_shops_preson);
		mPhone = (TextView) findViewById(R.id.order_shops_phone);
		mAddress = (TextView) findViewById(R.id.order_shops_address);
		mShopsName = (TextView) findViewById(R.id.order_shops_name);
		mGoodsLv = (ListView) findViewById(R.id.order_goods_listview);
		mOrderExpress = (TextView) findViewById(R.id.order_shops_express);
		mOrderMoney = (TextView) findViewById(R.id.order_shops_money);
		mOrderID = (TextView) findViewById(R.id.order_shops_order_id);
		mOrderCreateTime = (TextView) findViewById(R.id.order_shops_create_time);
		mShowPay = (LinearLayout) findViewById(R.id.show_pay);
		mshowMoney = (LinearLayout) findViewById(R.id.re1);
		mAlipy = (RelativeLayout) findViewById(R.id.goods_pay_alipy);
		mWxapi = (RelativeLayout) findViewById(R.id.goods_pay_wechat);
		allMoney = (TextView) findViewById(R.id.order_all_money);
		addressUpdate = (RelativeLayout) findViewById(R.id.order_goods_update);
		mShopTel = (TextView) findViewById(R.id.order_shop_name_tel);
		mSureTime = (RelativeLayout) findViewById(R.id.order_sure_time);
		mLookWuLiu = (RelativeLayout) findViewById(R.id.order_wu_liu);
		mRefundTitle = (TextView) findViewById(R.id.tv_order);
		mRefundTime = (TextView) findViewById(R.id.order_refund_time);
		mTelPhone = (RelativeLayout) findViewById(R.id.order_shop_tel);
		mWuLiuID = (TextView) findViewById(R.id.wu_liu_id);
		mWuLiuName = (TextView) findViewById(R.id.wu_liu_name);
		mWuLiuIndex = (TextView) findViewById(R.id.order_index);
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
//		addressUpdate.setOnClickListener(this);
		mLookWuLiu.setOnClickListener(this);
		mTelPhone.setOnClickListener(this);

		if (http == null) {
			http = new HttpUtil(handler);
		}

		// 获取数据
		model = (CartGoodsInfoModel) getIntent().getSerializableExtra(
				"CartGoodsInfoModel");
		goodsModel = (OrderGoodsModel) getIntent().getSerializableExtra(
				"OrderGoodsModel");
		
		mPreson.setText(goodsModel.getLinkman());
		mPhone.setText(goodsModel.getLinkphone());
		mAddress.setText(goodsModel.getAddressInfo());
		mShopsName.setText(goodsModel.getGoodShopName());		
		mOrderID.setText(goodsModel.getOrderName());
		mOrderCreateTime.setText(goodsModel.getCreateTime());
		mShopTel.setText(goodsModel.getShopMobile());
		
		//获取listview的数据
		String goodsList = getIntent().getStringExtra("goodsList");
		List<CartGoodsInfoModel> list = JSON.parseArray(goodsList, CartGoodsInfoModel.class);
		showType();
		if(getIntent().getIntExtra("isShow", 0)==1){//子点击事件
			mPay.setVisibility(View.GONE);
			mOrderCancel.setVisibility(View.GONE);
			mShowPay.setVisibility(View.GONE);
			mshowMoney.setVisibility(View.GONE);
			list.clear();
			list = new ArrayList<CartGoodsInfoModel>();	
			list.add(model);
		}			
		initGoodsListView(list);
		//计算总金额
		double money = 0;
		String totalMoney = totalCount(list);
		String[] split = totalMoney.split(";");
		money = Double.parseDouble(split[0]);
		sunExpress = split[1];
		if(sunExpress.equals("0.001")){//已付款 后
			sunExpress = goodsModel.getExpressAll();
		}else{
			if(!sunExpress.equals("0.0")){
				if(Double.parseDouble(sunExpress)>Double.parseDouble(goodsModel.getExpressAll())){
					sunExpress = goodsModel.getExpressAll();
				}
			}			
		}
								
		mOrderMoney.setText("￥"+String.valueOf(FileUtils.roundMath(money)));
		mOrderExpress.setText("￥" + sunExpress);
		sunMoney = String.valueOf(FileUtils.roundMath(money+Double.parseDouble(sunExpress)));
		allMoney.setText("￥"+sunMoney);		
		
		if(!goodsModel.getExpressNo().equals("")&&!goodsModel.getExpressName().equals("")){
			mWuLiuID.setText("快递单号："+goodsModel.getExpressNo());
			mWuLiuName.setText("物流名称："+goodsModel.getExpressName());
		}else{
//			mWuLiuID.setText("快递单号：暂无信息");
//			mWuLiuName.setText("物流名称：暂无信息");
			mWuLiuID.setVisibility(View.GONE);
			mWuLiuName.setText("同城配送中");
			mWuLiuIndex.setVisibility(View.GONE);
		}
	}

	/**
	 * 订单状态(1:待付款 2:待发货 3:待收货 4:未评价 5:已关闭 6:已完成 7:已退款)
	 */
	private void showType() {
		//计算时间
		double refundTime = 0;
		int day = 0;
		int time = 0;
		type = Integer.parseInt(goodsModel.getOrderType());		
		int shopType = Integer.parseInt(model.getShopType());
		switch (type) {
		case 1:// 待付款
			mSureTime.setVisibility(View.GONE);
			mLookWuLiu.setVisibility(View.GONE);
			mShowPay.setVisibility(View.VISIBLE);
			mPay.setText("去付款");
			break;
		case 2://已付款
			if(shopType==2){
				mSureTime.setVisibility(View.VISIBLE);
				mLookWuLiu.setVisibility(View.GONE);
				mRefundTitle.setText("买家已付款");
				mPay.setText("可退款");
			}else
			if(shopType==3){
				//计算时间			
				if(goodsModel.getReceiptTime()!=null&&!goodsModel.getReceiptTime().equals("")){							
					refundTime = Double.parseDouble(goodsModel.getReceiptTime());
					day = (int) (refundTime/24);
					time = (int) (refundTime%24);
				}			
				mRefundTime.setText("还剩"+day+"天"+time+"时自动确认收货");
				mRefundTitle.setText("卖家已发货");
				mSureTime.setVisibility(View.VISIBLE);
				mLookWuLiu.setVisibility(View.VISIBLE);
				mOrderCancel.setVisibility(View.GONE);
				mOrderCancel.setText("查看物流");
				mPay.setText("可退款");
			}else
			if(shopType==4){
				mRefundTitle.setText("买家已收货");
				mRefundTime.setVisibility(View.GONE);
				mSureTime.setVisibility(View.VISIBLE);
				mLookWuLiu.setVisibility(View.VISIBLE);
				mPay.setText("待评价");
			}else
			if(shopType==6){
				mRefundTitle.setText("交易已完成");
//				mRefundTime.setText("还剩5天10时自动退款");
				mRefundTime.setVisibility(View.GONE);
				mSureTime.setVisibility(View.VISIBLE);
				mLookWuLiu.setVisibility(View.VISIBLE);
				mSureTime.setBackgroundResource(R.drawable.order_sure_time_bg);
			}else
			if(shopType==7){
				mSureTime.setVisibility(View.VISIBLE);
				mLookWuLiu.setVisibility(View.GONE);
				mRefundTitle.setText("卖家已退款");	
				mSureTime.setBackgroundResource(R.drawable.order_sure_time_bg);
			}else
			if(shopType==8){
				//计算时间		
				if(goodsModel.getRefundTime()!=null&&!goodsModel.getRefundTime().equals("")){							
					refundTime = Double.parseDouble(goodsModel.getRefundTime());
					day = (int) (refundTime/24);
					time = (int) (refundTime%24);
				}	
				mSureTime.setVisibility(View.VISIBLE);
				mLookWuLiu.setVisibility(View.GONE);
				mRefundTime.setVisibility(View.GONE);
//				mRefundTime.setText("还剩"+day+"天"+time+"时自动退款");
				mRefundTitle.setText("买家已申请退款");			
			}			
			break;
		
//		case 2:// 待发货
//			mSureTime.setVisibility(View.VISIBLE);
//			mLookWuLiu.setVisibility(View.GONE);
//			mRefundTitle.setText("买家已付款");
//			mPay.setText("可退款");
//			break;
//		case 3:// 待收货
//			//计算时间			
//			if(goodsModel.getReceiptTime()!=null&&!goodsModel.getReceiptTime().equals("")){							
//				refundTime = Double.parseDouble(goodsModel.getReceiptTime());
//				day = (int) (refundTime/24);
//				time = (int) (refundTime%24);
//			}			
//			mRefundTime.setText("还剩"+day+"天"+time+"时自动确认收货");
//			mRefundTitle.setText("卖家已发货");
//			mSureTime.setVisibility(View.VISIBLE);
//			mLookWuLiu.setVisibility(View.VISIBLE);
//			mOrderCancel.setVisibility(View.GONE);
//			mOrderCancel.setText("查看物流");
//			mPay.setText("可退款");
//			break;
//		case 4:// 未评价
//			mRefundTitle.setText("买家已收货");
//			mRefundTime.setVisibility(View.GONE);
//			mSureTime.setVisibility(View.VISIBLE);
//			mLookWuLiu.setVisibility(View.VISIBLE);
//			mPay.setText("待评价");
//			break;
//		case 6://已完成
//			mRefundTitle.setText("交易已完成");
////			mRefundTime.setText("还剩5天10时自动退款");
//			mRefundTime.setVisibility(View.GONE);
//			mSureTime.setVisibility(View.VISIBLE);
//			mLookWuLiu.setVisibility(View.VISIBLE);
//			mSureTime.setBackgroundResource(R.drawable.order_sure_time_bg);
//			break;
//		case 7:
//			mSureTime.setVisibility(View.GONE);
//			mLookWuLiu.setVisibility(View.GONE);
//			break;
//		case 8://退款中	
//			//计算时间		
//			if(goodsModel.getRefundTime()!=null&&!goodsModel.getRefundTime().equals("")){							
//				refundTime = Double.parseDouble(goodsModel.getRefundTime());
//				day = (int) (refundTime/24);
//				time = (int) (refundTime%24);
//			}	
//			mSureTime.setVisibility(View.VISIBLE);
//			mLookWuLiu.setVisibility(View.GONE);
//			mRefundTime.setVisibility(View.GONE);
////			mRefundTime.setText("还剩"+day+"天"+time+"时自动退款");
//			mRefundTitle.setText("买家已申请退款");			
//			break;
		default:
			break;
		}
	}
	
	/**
	 * 初始化listview
	 */
	private void initGoodsListView(List<CartGoodsInfoModel> list){
		OrderGoodsDescListAdapter adapter = new OrderGoodsDescListAdapter(mContext, list);
		mGoodsLv.setAdapter(adapter);
		MyApplication.setListViewHeight(mGoodsLv);
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

	/**
	 * 删除订单
	 */
	private void getDeleteData(String orderID) {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String deleteOrder = WebUrlConfig.deleteOrder(orderID,String.valueOf(2));
			RequestParams params = http.getParams(deleteOrder);
			http.sendPost(RequestCode.DELETEORDER, params);
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
				// 删除订单
				if (msg.arg1 == RequestCode.DELETEORDER) {
					CodeModel model = JSONObject.parseObject(
							msg.obj.toString(), CodeModel.class);
					if (model.getStatus() == 0) {
						setResult(RequestCode.DELETEORDER);
						onBackPressed();
						finish();
						ShowContentUtils.showLongToastMessage(mContext, "删除成功");
					} else {
						ShowContentUtils.showLongToastMessage(mContext,
								model.getErrorMsg());
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
						orderID = goodsModel.getOrderID();
						MyApplication.payMoney(goodsModel.getOrderID(), 
								sunMoney, sunExpress, 
								payType, mContext);	
						setResult(RequestCode.DELETEORDER);
						count=true;						
					} else {
						ShowContentUtils.showLongToastMessage(mContext,"库存量不足，请重新下单!");
						setResult(RequestCode.DELETEORDER);
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
				if (msg.arg2 == RequestCode.DELETEORDER) {
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
		if (v == mBack) {// 返回上一层
			if(orderID!=null){
				return;
			}
			onBackPressed();
			finish();
		}
		if (v == mPay) {// 付款
			switch (type) {
			case 1:// 待付款(提交订单)
				if(orderID!=null){
					return;
				}
				if(!MyApplication.isExistPackage(payType, mContext)){//判断有没有安装app
					ShowContentUtils.showLongToastMessage(mContext, "软件没有安装，无法支付");
					return;
				}
//				shopID = goodsModel.getGoodShopID();
//				addressID = goodsModel.getAddressID();				
//				getGoodsOrder(JSONObject.toJSONString(goodsModel.getGoodsInfo()));
				//查询库存量
				checkCount(goodsModel.getOrderID());
				// 提交订单成功 支付
//				orderID = goodsModel.getOrderID();
//				MyApplication.payMoney(goodsModel.getOrderID(), 
//						sunMoney, sunExpress, 
//						payType, mContext);	
//				setResult(RequestCode.DELETEORDER);
//				count=true;
				break;
			case 2:// 待发货(可退款)
				Intent intent2 = new Intent(mContext, RefundActivity.class);
				Bundle b2 = new Bundle();
				b2.putSerializable("OrderGoodsModel", goodsModel);
				intent2.putExtras(b2);
				startActivity(intent2);
				break;
			case 3:// 待收货(可退款)
				Intent intent3 = new Intent(mContext, RefundActivity.class);
				Bundle b3 = new Bundle();
				b3.putSerializable("OrderGoodsModel", goodsModel);
				intent3.putExtras(b3);
				startActivity(intent3);
				break;
			case 4:// 未评价
				Intent intent = new Intent(mContext, PingjiaActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("CartGoodsInfoModel", model);
				intent.putExtras(b);
				intent.putExtra("type", String.valueOf(2));// 购物商品
				startActivity(intent);
				break;
			default:
				break;
			}
		}
		if (v == mOrderCancel) {// 删除 查看物流
			switch (type) {
			case 1:// 删除
				mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定删除吗?", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getDeleteData(goodsModel.getOrderID());
						dialog.dismiss();
					}
				});
				mDeleteDialog.create().show();
				break;
			case 2:
				break;
			case 3:// 查看物流
				break;
			case 4:// 删除
				mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定删除吗?", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getDeleteData(goodsModel.getOrderID());
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
//		if(v == addressUpdate){//修改收货地址
//			Intent intent = new Intent(mContext,AddressListActivity.class);
//			intent.putExtra("type", 1);
//			startActivityForResult(intent, RequestCode.ADDADDRESS);
//		}
		if(v == mLookWuLiu){//查看物流
			if(orderID!=null){
				return;
			}
			if(!TextUtils.isEmpty(goodsModel.getExpressNo())){
				Intent intent = new Intent(mContext, WuLiuWebActivity.class);
				intent.putExtra("orderName", goodsModel.getExpressNo());
				intent.putExtra("type", goodsModel.getExpressName());
				intent.putExtra("typeStatus", 1);//物流标致
				startActivity(intent);
			}			
		}
		if(v == mTelPhone){//打电话
			if(orderID!=null){
				return;
			}
			if(goodsModel.getShopMobile() != null&&!goodsModel.getShopMobile().equals("")){
				mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"温馨提示", "您确定要给商家打电话吗?", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
					    intent.setAction("android.intent.action.DIAL");
					    intent.setData(Uri.parse("tel:"+goodsModel.getShopMobile()));
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
	
	/**
	 * 计算价格  运费
	 * @param list
	 * @return
	 */
	private String totalCount(List<CartGoodsInfoModel> list){
		Map<String, String> map = new HashMap<String, String>();
		double money = 0;
		double express = 0;
		for (int i = 0; i < list.size(); i++) {			
			CartGoodsInfoModel model = list.get(i);
			if(!model.getNumber().equals("")&&!model.getGoodsPrice().equals("")){							
				money = money + (Double.parseDouble(model.getGoodsPrice())*Integer.parseInt(model.getNumber()));				
			}
			if(!model.getExpress().equals("")){
//				express = express + Double.parseDouble(model.getExpress());
				map.put(model.getGoodsID(), model.getExpress());	
			}else{
				express = 0.001;
			}
		}
		//计算运费  同一件商品 不同颜色 尺寸 运费 只算一次
		if(map!=null){
			Set set = map.entrySet();
	        Iterator<Entry> it = set.iterator();
	        while (it.hasNext()){
	            Entry<String, String> entry = (Entry<String, String>)it.next();
	            express = express + Double.parseDouble(entry.getValue());
	        }
		}
		return String.valueOf(money)+";"+String.valueOf(express);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RequestCode.ADDADDRESS:
			AddressModel model = (AddressModel) data.getSerializableExtra("AddressModel");
			mPreson.setText(model.getLinkman());
			mPhone.setText(model.getLinkphone());
			mAddress.setText(model.getProvince()+model.getCity()+
					model.getArea()+model.getAddressInfo());
			addressID = model.getAddressID();
			break;
		default:
			break;
		}
	}

}

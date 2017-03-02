package com.sitemap.wisdomjingjiang.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.xutils.x;
import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.OrderFoodsDescListAdapter;
import com.sitemap.wisdomjingjiang.adapters.SubFoodsListViewAdapter;
import com.sitemap.wisdomjingjiang.alipay.AliPayHandler;
import com.sitemap.wisdomjingjiang.alipay.AliPayHelper;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartFoodsModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.utils.FileUtils;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.wxapi.WechatPayHandler;
import com.sitemap.wisdomjingjiang.wxapi.WechatPayHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.SumPathEffect;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author chemmeng 提交 团购商品 订单详情 页面
 * 
 */
public class SubFoodsOrderActivity extends BaseActivity implements
		OnClickListener {
	/** 全局类 */
	private SubFoodsOrderActivity mContext;
	/** 标题栏 返回上一层 */
	private ImageView mBack;
	/** 标题栏 标题 */
	private TextView mTitle;
	/** 付款 */
	private TextView mPay;
	/** 取消订单 */
	private TextView mCancelOrder;
	/** 取消订单 时弹出的对话框 */
	private Builder mBackDialog;
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/** 用户id */
	private String userID = MyApplication.loginModel.getUserID();
	/** 商家id */
	private String foodShopID;
	/** 商家名称 */
	private String shopName;
	/** 运费 */
	private String expressMoney = "0.00";
	/** 数量 */
	private String foodNum;
	/** 团购商家中的团购返回的实体类 */
	private FoodShopFoodsModel foodsModel;
	/** 商家名称 商品名称 商品数量 商品价格 */
	private TextView mShopName, mFoodsName, mShopNum, mShopPrice;
	/** 商品图片 */
	private ImageView mShopImg;
	/** 运费 实付款 */
	private TextView mExpressMoney, mTotalMoney;
	/** 支付宝 微信 */
	private RelativeLayout mAlipy, mWxapi;
	/** 支付方式 1、为支付宝 2、为微信支付 */
	private int payType = 1;
	/** 标致 1、表示购物车 2、表示 立即购买 */
	private int type;
	/** listview */
	private ListView mFoodsLv;
	/**商家名称*/ 
	private TextView mName;
	/** 是否显示 */
	private LinearLayout mShowOne, mShowTwo;
	/** 购物车 选择的list */
	private List<CartFoodsModel> payList;
	/**总金额*/ 
	private String sunMoney = "0.01";
	/**运费*/ 
	private String sunExpress = "0.00";
	private boolean count=false;//是否关闭
	private int num=0;//支付后页面启动次数
	/**结算窗口 点击的位置*/ 
	private int currPosition = 0;
	/**总金额*/ 
	private TextView allMoney;
	/**订单id*/ 
	private String orderID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_foods_order);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据
	}
	
	@Override
	public void onResume() {
		Log.i("TAG", "onResume");
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
		mBack = (ImageView) findViewById(R.id.goods_desc_title).findViewById(
				R.id.back);
		mTitle = (TextView) findViewById(R.id.goods_desc_title).findViewById(
				R.id.title);
		mPay = (TextView) findViewById(R.id.order_desc_pay);
		mCancelOrder = (TextView) findViewById(R.id.order_desc_cancel);
		mShopName = (TextView) findViewById(R.id.goods_shop_name);
		mFoodsName = (TextView) findViewById(R.id.goods_shop_breif);
		mShopNum = (TextView) findViewById(R.id.goods_shop_num);
		mShopPrice = (TextView) findViewById(R.id.goods_shop_price);
		mExpressMoney = (TextView) findViewById(R.id.goods_express_money);
		mTotalMoney = (TextView) findViewById(R.id.goods_total_money);
		mShopImg = (ImageView) findViewById(R.id.goods_shop_img);
		mAlipy = (RelativeLayout) findViewById(R.id.goods_pay_alipy);
		mWxapi = (RelativeLayout) findViewById(R.id.goods_pay_wechat);
		mFoodsLv = (ListView) findViewById(R.id.shopping_list);
		mShowOne = (LinearLayout) findViewById(R.id.foods_show);
		mShowTwo = (LinearLayout) findViewById(R.id.sub_foods_show_listview);
		mName = (TextView) findViewById(R.id.foods_shops_name);
		allMoney = (TextView) findViewById(R.id.order_all_money);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mTitle.setText("提交订单");
		mBack.setOnClickListener(this);
		mPay.setOnClickListener(this);
		mCancelOrder.setOnClickListener(this);
		mWxapi.setOnClickListener(this);
		mAlipy.setOnClickListener(this);
		mAlipy.setSelected(true);

		mBackDialog = MyApplication.myAlertDialog(mContext, true, "提示",
				"确定取消订单", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						dialog.dismiss();
					}
				});

		if (http == null) {
			http = new HttpUtil(handler);
		}

		// 获取商家团购的信息
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		if (type == 1) {// 购物车 进入or结算 窗口
			mShowOne.setVisibility(View.GONE);
			String foodsInfo = intent.getStringExtra("foodList");
			currPosition = intent.getIntExtra("position", 0);
			payList = JSONObject.parseArray(foodsInfo,CartFoodsModel.class);			
			mName.setText(payList.get(currPosition).getFoodShopName());
			//计算金额
			double sun = 0.00;
			for(int i=0;i<payList.get(currPosition).getFoodsInfo().size();i++){			
				CartFoodsInfoModel model = payList.get(currPosition).getFoodsInfo().get(i);
				int num = 1;
				double price = 0;
				if(!model.getNumber().equals("")&&!model.getFoodsPrice().equals("")){
					num = Integer.parseInt(model.getNumber());
					price = Double.parseDouble(model.getFoodsPrice());	
				}
				sun = sun + (num*price);
			}					
			sunMoney = String.valueOf(FileUtils.roundMath(sun));
			allMoney.setText("￥" + sunMoney);
			mTotalMoney.setText("￥" + sunMoney);
			mExpressMoney.setText("￥"+expressMoney);
			initListView(payList.get(currPosition).getFoodsInfo());
		} else {//立即购买
			foodShopID = getIntent().getStringExtra("foodShopID");
			shopName = getIntent().getStringExtra("shopName");
			foodNum = getIntent().getStringExtra("foodNum");
			mShopName.setText(shopName);
			foodsModel = (FoodShopFoodsModel) intent
					.getSerializableExtra("FoodShopFoodsModel");
			x.image().bind(mShopImg, foodsModel.getFoodImg(),
					MyApplication.imageOptionsZ);
			mFoodsName.setText(foodsModel.getFoodName());
			mShopNum.setText("x" + foodNum);
			mShopPrice.setText("￥" + foodsModel.getNowPrice());
//			mExpressMoney.setText("￥"+expressMoney);			
			// 计算总价
			if(foodsModel.getNowPrice().equals("")){
				foodsModel.setNowPrice("0.00");
			}
			double priceTotal = Double.parseDouble(foodsModel.getNowPrice())
					* Integer.parseInt(foodNum);
			double total = Double.parseDouble(expressMoney) + priceTotal;			
			sunMoney = String.valueOf(FileUtils.roundMath(total));
			allMoney.setText("￥" + sunMoney);
			mTotalMoney.setText("￥" + sunMoney);
			mExpressMoney.setText("￥"+expressMoney);
		}

	}

	/**
	 * 初始化listview
	 */
	private void initListView(List<CartFoodsInfoModel> list) {
		mShowTwo.setVisibility(View.VISIBLE);
		OrderFoodsDescListAdapter adapter = new OrderFoodsDescListAdapter(mContext,list);
		mFoodsLv.setAdapter(adapter);
		MyApplication.setListViewHeight(mFoodsLv);
	}

	/**
	 * 提交订单 团购
	 */
	private void getFoodsOrder(String type,String foodsInfo) {		
		if (MyApplication.getNetObject().isNetConnected()) {				
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String addOrder = WebUrlConfig.addFoodsOrder(userID, foodShopID,type,
					foodsInfo);
//			RequestParams params = http.getParams(addOrder);
			http.sendGet(RequestCode.ADDFOODSORDER, addOrder);
			Log.e("order", addOrder);
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
				// 提交订单
				if (msg.arg1 == RequestCode.ADDFOODSORDER) {
					Log.e("order", msg.obj.toString());
					CodeModel model = JSONObject.parseObject(
							msg.obj.toString(), CodeModel.class);
					if (model.getStatus() == 0) {
						// 提交订单成功 支付
						orderID = model.getOrderID();
						MyApplication.payMoney(model.getOrderID(), 
								sunMoney, sunExpress, 
								payType, mContext);
						count=true;
					} else {
						mAlipy.setSelected(true);
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
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg1 == RequestCode.ADDORDER) {
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
			mBackDialog.create().show();
		}
		if (v == mPay) {// 付款
			if(orderID!=null){
				return;
			}
			if(!MyApplication.isExistPackage(payType, mContext)){//判断有没有安装app
				ShowContentUtils.showLongToastMessage(mContext, "软件没有安装，无法支付");
				return;
			}
			if (type == 1) {				
				foodShopID = payList.get(currPosition).getFoodShopID();
				getFoodsOrder(String.valueOf(1),JSONObject.toJSONString(payList.get(currPosition).getFoodsInfo()));
			} else {
				List<CartFoodsInfoModel> list = new ArrayList<CartFoodsInfoModel>();
				CartFoodsInfoModel infoModel = new CartFoodsInfoModel();
				infoModel.setFoodsID(foodsModel.getFoodID());
				infoModel.setFoodsPrice(foodsModel.getNowPrice());
				infoModel.setNumber(foodNum);
				infoModel.setFoodsImg(foodsModel.getFoodImg());
				infoModel.setFoodsName(foodsModel.getFoodName());
				list.add(infoModel);
				getFoodsOrder(String.valueOf(2),JSONObject.toJSONString(list));
			}
		}
		if (v == mCancelOrder) {// 取消订单
			mBackDialog.create().show();
		}
		if (v == mAlipy) {// 支付宝
			if(orderID!=null){
				return;
			}
			mAlipy.setSelected(true);
			payType = 1;
		}
		if (v == mWxapi) {// 微信支付
			if(orderID!=null){
				return;
			}
			mWxapi.setSelected(true);
			payType = 2;
		}
	}
}

package com.sitemap.wisdomjingjiang.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.xutils.x;
import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.OrderGoodsDescListAdapter;
import com.sitemap.wisdomjingjiang.adapters.OrderGoodsListAdapter;
import com.sitemap.wisdomjingjiang.adapters.SubFoodsListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.SubGoodsListViewAdatper;
import com.sitemap.wisdomjingjiang.alipay.AliPayHandler;
import com.sitemap.wisdomjingjiang.alipay.AliPayHelper;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.AddressModel;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartFoodsModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.models.GoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.GoodsModel;
import com.sitemap.wisdomjingjiang.models.NewsListModel;
import com.sitemap.wisdomjingjiang.utils.FileUtils;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.wxapi.WechatPayHandler;
import com.sitemap.wisdomjingjiang.wxapi.WechatPayHelper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsStatus.NmeaListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author chenmeng 提交 购物商品 订单详情 页面
 */
public class SubGoodsOrderActivity extends BaseActivity implements
		OnClickListener {
	/** 全局类 */
	private SubGoodsOrderActivity mContext;
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
	private static MyProgressDialog progressDialog, progress;
	/** 用户id */
	private String userID = MyApplication.loginModel.getUserID();
	/** 商家id */
	private String shopID;
	/** 商家名称 */
	private String shopName;
	/** 运费 */
	private String expressMoney = "0.00";
	/** 数量 */
	private String foodNum;
	/** 商家名称 商品名称 商品数量 商品价格 */
	private TextView mShopName, mFoodsName, mShopNum, mShopPrice;
	/** 商品图片 */
	private ImageView mShopImg;
	/** 收货地址id */
	private String addressID;
	/** 运费 实付款 */
	private TextView mExpressMoney, mTotalMoney;
	/** 我的收货 地址 数据集合 */
	private List<AddressModel> mAddressList = null;
	/** 收货人 电话 详细地址 */
	private TextView mPerson, mPhone, mInfo;
	/**颜色 尺寸*/ 
	private TextView mColor,mSize;
	/** 支付宝 微信 */
	private RelativeLayout mAlipy, mWxapi;
	/** 支付方式 1、为支付宝 2、为微信支付 */
	private int payType = 1;
	/** 标致 1、表示购物车 2、表示 立即购买 */
	private int type;
	/** listview */
	private ListView mElv;
	/**商家名称*/ 
	private TextView mName;
	/** 是否显示 */
	private LinearLayout mShowOne, mShowTwo;
	/** 购物车 选择的list */
	private List<CartGoodsModel> payList;
	/** 购物车 当前选择list */
	private List<CartGoodsInfoModel> currList = new ArrayList<CartGoodsInfoModel>();;
	/**购物商品详情返回实体类*/ 
	private GoodsInfoModel goodsInfoModel;
	/**购物商品列表返回实体类*/ 
	private GoodsModel goodsModel;
	/**总金额*/ 
	private String sunMoney = "0.01";
	/**运费*/ 
	private String sunExpress = "0.00";
	/**颜色 尺寸 价格*/ 
	private String color,size,price;
	private boolean count=false;//是否关闭
	private int num=0;//支付后页面启动次数
	/**结算窗口 点击的位置*/ 
	private int currPosition;
	/**总金额*/ 
	private TextView allMoney;
	/**地址修改事件*/ 
	private RelativeLayout mAddressCheck;
	/**订单id*/ 
	private String orderID = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_goods_order);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据
	}
	
	@Override
	public void onResume() {
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
		mBack = (ImageView) findViewById(R.id.sub_goods_order_title)
				.findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.sub_goods_order_title)
				.findViewById(R.id.title);
		mPay = (TextView) findViewById(R.id.order_desc_pay);
		mCancelOrder = (TextView) findViewById(R.id.order_desc_cancel);
		mShopName = (TextView) findViewById(R.id.goods_shop_name);
		mFoodsName = (TextView) findViewById(R.id.goods_shop_breif);
		mShopNum = (TextView) findViewById(R.id.goods_shop_num);
		mShopPrice = (TextView) findViewById(R.id.goods_shop_price);
		mExpressMoney = (TextView) findViewById(R.id.goods_express_money);
		mTotalMoney = (TextView) findViewById(R.id.goods_total_money);
		mShopImg = (ImageView) findViewById(R.id.goods_shop_img);
		mPerson = (TextView) findViewById(R.id.order_desc_preson);
		mPhone = (TextView) findViewById(R.id.order_desc_phone);
		mInfo = (TextView) findViewById(R.id.order_desc_address);
		mAlipy = (RelativeLayout) findViewById(R.id.goods_pay_alipy);
		mWxapi = (RelativeLayout) findViewById(R.id.goods_pay_wechat);
		mElv = (ListView) findViewById(R.id.shopping_list);
		mShowOne = (LinearLayout) findViewById(R.id.goods_is_show);
		mShowTwo = (LinearLayout) findViewById(R.id.sub_foods_show_listview);
		mColor = (TextView) findViewById(R.id.sub_goods_color);
		mSize = (TextView) findViewById(R.id.sub_goods_size);
		mName = (TextView) findViewById(R.id.goods_shops_name);
		allMoney = (TextView) findViewById(R.id.order_all_money);
		mAddressCheck = (RelativeLayout) findViewById(R.id.goods_udpate_address);
	}
	
	
	@Override
	protected void onRestart() {
		Log.i("TAG", "onRestart");
		super.onRestart();
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
		mAddressCheck.setOnClickListener(this);

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
		getAddressDesc();// 获取我的收货地址
		// 获取商家团购的信息
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		if (type == 1) {// 购物车 进入
			mShowOne.setVisibility(View.GONE);
			String foodsInfo = intent.getStringExtra("goodList");
			currPosition = intent.getIntExtra("position", 0);
			payList = JSONObject.parseArray(foodsInfo,CartGoodsModel.class);	
			mName.setText(payList.get(currPosition).getGoodShopName());
			//计算金额
			String totalCount = totalCount(payList.get(currPosition).getGoodsInfo());
			String[] split = totalCount.split(";");
			double sun = Double.parseDouble(split[0]);
			sunExpress = split[1];			
			if(sunExpress.equals("0.001")){//运费为空  默认问0
				sunExpress = "0";
			}else{
				if(!sunExpress.equals("0.0")){//运费不为0  
					if(Double.parseDouble(sunExpress)>Double.parseDouble(payList.get(currPosition).getMaxExpress())){
						sunExpress = payList.get(currPosition).getMaxExpress();
					}
				}			
			}
			
			double total = sun + Double.parseDouble(sunExpress);
			
			sunMoney = String.valueOf(FileUtils.roundMath(total));
			mTotalMoney.setText("￥" + FileUtils.roundMath(sun));
			mExpressMoney.setText("￥" + FileUtils.roundMath(Double.parseDouble(sunExpress)));	
			allMoney.setText("￥" + sunMoney);	
			initListView(payList.get(currPosition).getGoodsInfo());
		} else {//立即购买 进入
			goodsInfoModel = (GoodsInfoModel) getIntent().getSerializableExtra("GoodsInfoModel");
			goodsModel = (GoodsModel) getIntent().getSerializableExtra("GoodsModel");
			shopID = goodsInfoModel.getGoodShopID();
			shopName = goodsInfoModel.getGoodShopName();
			foodNum = getIntent().getStringExtra("number");
			mShopName.setText(shopName);
			x.image().bind(mShopImg, goodsModel.getGoodsImg(),
					MyApplication.imageOptionsZ);
			mFoodsName.setText(goodsModel.getGoodsName());
			mShopNum.setText("x" + foodNum);
			
			color = getIntent().getStringExtra("color");
			size = getIntent().getStringExtra("size");
			mColor.setText(color);
			mSize.setText(size);
			price = getIntent().getStringExtra("price");
			// 计算总价
			if(goodsModel.getGoodsPrice().equals("")){
				goodsModel.setGoodsPrice("0.00");
			}else{
				goodsModel.setGoodsPrice(price);				
			}
			//运费
			if(goodsInfoModel.getExpress().equals("")){
				expressMoney = "0.00";
			}else{
				expressMoney = goodsInfoModel.getExpress();
			}
			
			mShopPrice.setText("￥" +goodsModel.getGoodsPrice());
			double priceTotal = Double.parseDouble(goodsModel.getGoodsPrice())
					* Integer.parseInt(foodNum);
			double total = Double.parseDouble(expressMoney) + priceTotal;			
			sunMoney = String.valueOf(FileUtils.roundMath(total));
			sunExpress = String.valueOf(FileUtils.roundMath(Double.parseDouble(expressMoney)));
			mTotalMoney.setText("￥" + FileUtils.roundMath(priceTotal));
			mExpressMoney.setText("￥" + sunExpress);	
			allMoney.setText("￥" + sunMoney);			
		}

	}

	/**
	 * 初始化listview
	 */
	private void initListView(List<CartGoodsInfoModel> list) {
		mShowTwo.setVisibility(View.VISIBLE);
		OrderGoodsDescListAdapter adapter = new OrderGoodsDescListAdapter(mContext, list);
		mElv.setAdapter(adapter);
		MyApplication.setListViewHeight(mElv);
	}

	/**
	 * 获取我的收货地址
	 */
	private void getAddressDesc() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progress = MyProgressDialog.createDialog(mContext);
			if (progress != null && !progress.isShowing()) {
				progress.setMessage("加载中...");
				progress.show();
			}
			String address = WebUrlConfig.getAddress(userID);
			http.sendGet(RequestCode.GETADDRESS, address);
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	/**
	 * 提交订单 购物
	 */
	private void getGoodsOrder(String type,String goodsInfo) {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String addOrder = WebUrlConfig.addOrder(userID, shopID, addressID,type,goodsInfo);
			RequestParams params = http.getParams(addOrder);
			http.sendPost(RequestCode.ADDORDER, params);
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
			if (progress != null && progress.isShowing()) {
				progress.dismiss();// 关闭进度条
			}
			super.handleMessage(msg);
			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				// 我的收货地址
				if (msg.arg1 == RequestCode.GETADDRESS) {
					mAddressList = JSONObject.parseArray(msg.obj.toString(),
							AddressModel.class);
					if (mAddressList.size() == 0) {
						Intent intent = new Intent(mContext, AddressAddActivity.class);
						startActivity(intent);
						mContext.finish();
					} else {					
						AddressModel model = checkAddressDefault();
						if (model != null) {
							mPerson.setText(model.getLinkman());
							mPhone.setText(model.getLinkphone());
							mInfo.setText(model.getProvince()+model.getCity()+
									model.getArea()+model.getAddressInfo());
						}
					}
				}
				// 提交订单
				if (msg.arg1 == RequestCode.ADDORDER) {
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
								"库存0件，无法购买");
					}
				}
				//确认支付状态页面
				if(msg.arg1 == RequestCode.ORDERSTATUS){
					Log.e("result", msg.obj.toString());
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
				if (msg.arg2 == RequestCode.ADDORDER) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}
	};		

	/**
	 * 查询我的收货地址 默认显示 系统中默认的地址
	 */
	private AddressModel checkAddressDefault() {
		AddressModel model = new AddressModel();
		for (int i = 0; i < mAddressList.size(); i++) {
			model = mAddressList.get(i);
			if (model.getIsDefault().equals("1")) {
				addressID = model.getAddressID();
				return model;
			}else{
				addressID = mAddressList.get(0).getAddressID();
			}
		}
		return mAddressList.get(0);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
			if(addressID != null){					
				if (type == 1) {					
					shopID = payList.get(currPosition).getGoodShopID();
					getGoodsOrder(String.valueOf(1),JSONObject.toJSONString(payList.get(currPosition).getGoodsInfo()));
				} else {
					List<CartGoodsInfoModel> list = new ArrayList<CartGoodsInfoModel>();
					CartGoodsInfoModel infoModel = new CartGoodsInfoModel();
					infoModel.setGoodsID(goodsModel.getGoodsID());
					infoModel.setGoodsPrice(goodsModel.getGoodsPlace());
					infoModel.setNumber(foodNum);
					infoModel.setGoodsPrice(goodsModel.getGoodsPrice());
					infoModel.setColor(color);
					infoModel.setSize(size);
					infoModel.setGoodsName(goodsModel.getGoodsName());
					infoModel.setGoodsImg(goodsModel.getGoodsImg());
					list.add(infoModel);
					getGoodsOrder(String.valueOf(2),JSONObject.toJSONString(list));
				}
//				mPay.setClickable(false);
//				mPay.setFocusable(false);
//				Timer timer = new Timer();  
//			    timer.schedule(new TimerTask() {  	           
//					public void run()   {  
//			               handler.sendEmptyMessage(RequestCode.PAYDELAY);
//			         }  			           
//			     },11000);
			}else{
				ShowContentUtils.showLongToastMessage(mContext, "请填写地址");
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
		if(v == mAddressCheck){//地址修改点击事件
			if(orderID!=null){
				return;
			}
			Intent intent = new Intent(mContext,AddressListActivity.class);
			intent.putExtra("type", 1);
			startActivityForResult(intent, RequestCode.ADDADDRESS);
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
				map.put(model.getGoodsID(), model.getExpress());	
			}else{//直接用总运费
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
			mPerson.setText(model.getLinkman());
			mPhone.setText(model.getLinkphone());
			mInfo.setText(model.getProvince()+model.getCity()+
					model.getArea()+model.getAddressInfo());
			addressID = model.getAddressID();
			mAlipy.setSelected(true);
			break;
		default:
			break;
		}
	}

}

package com.sitemap.wisdomjingjiang.activities;

import java.util.List;

import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.SubPayFoodsListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.SubPayFoodsListViewAdapter.CallBackSubPayFoods;
import com.sitemap.wisdomjingjiang.adapters.SubPayGoodsListViewAdapter.CallBackSubPayGoods;
import com.sitemap.wisdomjingjiang.adapters.SubPayGoodsListViewAdapter;
import com.sitemap.wisdomjingjiang.alipay.AliPayHandler;
import com.sitemap.wisdomjingjiang.alipay.AliPayHelper;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartFoodsModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.wxapi.WechatPayHandler;
import com.sitemap.wisdomjingjiang.wxapi.WechatPayHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author chenmeng 分开结算商品 页面
 */
public class SubPayActivity extends BaseActivity implements OnClickListener,
		CallBackSubPayFoods,CallBackSubPayGoods {
	/** 本类 */
	private SubPayActivity mContext;
	/** listview */
	private ListView mLv;
	/** 关闭 */
	private TextView mClose;
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/** 用户id */
	private String userID = MyApplication.loginModel.getUserID();
	/** 商家id */
	private String shopID;
	/** 支付方式 1、为支付宝 2、为微信支付 */
	private int payType = 1;
	/** 团购数据 */
	private List<CartFoodsModel> mFoodsList;
	/** 团购数据 */
	private List<CartGoodsModel> mGoodsList;
	/**类型  1表示购物  2团购*/ 
	private int type;
	/**地址id*/ 
	private String addressID;
	/**总金额*/ 
	private String sunMoney = "0.01";
	/**运费*/ 
	private String sunExpress = "0.00";
	private boolean count=false;//是否关闭
	private int num=0;//支付后页面启动次数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_pay);
		// 设置 窗口的显示位置
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.gravity = Gravity.BOTTOM;
		getWindow().setAttributes(lp);

		mContext = this;
		initView();
		initData();
		
	}
	
	@Override
	public void onResume() {
		Log.i("TAG", "onResume");
		super.onResume();
		if (count) {
			num++;
			if (num>1) {
				finish();
			}
		
		}
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mClose = (TextView) findViewById(R.id.sub_pay_close);
		mLv = (ListView) findViewById(R.id.sub_pay_listview);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mClose.setOnClickListener(this);
		
		type = getIntent().getIntExtra("type", 0);
		if(type==1){//购物
			String result = getIntent().getStringExtra("CartGoodsModel");
			mGoodsList = JSONObject.parseArray(result,CartGoodsModel.class);
			initGoodsListView();
		}else{//团购
			//数据
			String result = getIntent().getStringExtra("CartFoodsModel");
			mFoodsList = JSONObject.parseArray(result,CartFoodsModel.class);
			initFoodsListView();			
		}

	}
	
	/**
	 * 初始化listview  团购
	 */
	private void initFoodsListView() {
		if(mFoodsList != null){
			mLv.setAdapter(new SubPayFoodsListViewAdapter(mContext,this,mFoodsList));
		}
	}
	
	/**
	 * 初始化 购物 
	 */
	private void initGoodsListView() {
		if(mGoodsList != null){
			mLv.setAdapter(new SubPayGoodsListViewAdapter(mContext,this, mGoodsList));
		}
	}

	
	@Override
	public void onClick(View v) {
		if (v == mClose) {
			onBackPressed();
			finish();
		}
	}

	/**
	 * 结算按钮  团购
	 */
	@Override
	public void onClickPayFoodsSureListener(int position) {	
		Intent intent = new Intent(mContext,
				SubFoodsOrderActivity.class);
		intent.putExtra("foodList",JSONObject.toJSONString(mFoodsList));
		intent.putExtra("type", 1);// 从购物车 到提交订单 的标致
		intent.putExtra("position", position);//位置
		startActivity(intent);
		finish();
	}
	
	/**
	 * 购物 结算
	 */
	@Override
	public void onClickPayGoodsSureListener(int position) {
		Intent intent = new Intent(mContext,SubGoodsOrderActivity.class);
		intent.putExtra("goodList", JSONObject.toJSONString(mGoodsList));
		intent.putExtra("type", 1);// 从购物车 提交订单 的标致
		intent.putExtra("position", position);//位置
		startActivity(intent);
		finish();
	}


}

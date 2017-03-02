package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.HotleListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.MessageListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.models.GoodsModel;
import com.sitemap.wisdomjingjiang.models.MessageModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author chenmeng
 *	消息页面
 */
public class MessageActivity extends BaseActivity {
	private Context mContext;//全局类
	/**返回上一层*/ 
	private ImageView mBack;
	private TextView title;//标题
	/**消息的listview*/ 
	private ListView mLv;
	private HttpUtil http;// 网络请求
	private static MyProgressDialog progressDialog;// 进度条
	private List<MessageModel> lmessage=new ArrayList<MessageModel>();//消息列表
	private MessageListViewAdapter mAdapter;//消息适配器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		mContext = this;
		initView();//初始化view
//		initListView();//初始化listview
		initData();//初始化数据
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
				if (msg.arg1 == RequestCode.GETMESSAGE) {//第一次和刷新成功
					Log.i("result", msg.obj.toString());
					lmessage.clear();
					lmessage=JSONObject.parseArray(msg.obj.toString(),
							MessageModel.class);
					mAdapter=new MessageListViewAdapter(mContext, lmessage);
					mLv.setAdapter(mAdapter);
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.LOGIN) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}

	};


	/**
	 * 初始化view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.message_title).findViewById(R.id.back);
		title= (TextView) findViewById(R.id.message_title).findViewById(R.id.title);
		title.setText("消息");
		mLv = (ListView) findViewById(R.id.message_listview);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		//返回点击事件
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
				finish();
			}
		});
		http = new HttpUtil(handler);
		progressDialog = MyProgressDialog.createDialog(this);
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			if (MyApplication.isLogin) {
				http.sendGet(
						RequestCode.GETMESSAGE,
						WebUrlConfig.getMessage(MyApplication.loginModel.getUserID()));
			}else {
				Intent intent =new Intent(mContext,LoginActivity.class);
				startActivity(intent);
				finish();
			}
			
		} else {
			ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
		}
		initListView();
	}

	/**
	 * 初始化listview
	 */
	private void initListView() {
//		mLv.setAdapter(new MessageListViewAdapter(mContext));
		//点击事件
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				switch (Integer.parseInt(lmessage.get(position).getType())) {
				//（1：团购商家、2：购物商家，3：购物商品，4：团购商品，5物流信息.6新闻）
				case 1://团购商家
					FoodShopsModel model = new FoodShopsModel();	
					model.setArea("");
					model.setLat(lmessage.get(position).getLat());
					model.setLng(lmessage.get(position).getLng());
					model.setPreMoney(lmessage.get(position).getPreMoney());
					model.setShopGrade(lmessage.get(position).getGrade());
					model.setShopID(lmessage.get(position).getmID());
					model.setShopImg(lmessage.get(position).getImg());
					model.setShopName(lmessage.get(position).getName());
					model.setShopType(lmessage.get(position).getType());
					Intent intent = new Intent(mContext,FoodsShopActivity.class);
					//将商家id 传过去					
					Bundle b = new Bundle();
					b.putSerializable("FoodShopsModel", model);
					intent.putExtras(b);
					startActivity(intent);
					break;
				case 2://购物商家
					Intent intent1 = new Intent(mContext,ShoppingListActivity.class);
//					//将商家id 传过去
//					FoodShopsModel model1 = new FoodShopsModel();	
//					model1.setArea("");
//					model1.setLat(lmessage.get(position).getLat());
//					model1.setLng(lmessage.get(position).getLng());
//					model1.setPreMoney(lmessage.get(position).getPreMoney());
//					model1.setShopGrade(lmessage.get(position).getGrade());
//					model1.setShopID(lmessage.get(position).getmID());
//					model1.setShopImg(lmessage.get(position).getImg());
//					model1.setShopName(lmessage.get(position).getName());
//					model1.setShopType(lmessage.get(position).getType());
//					Bundle b1 = new Bundle();
//					b1.putSerializable("FoodShopsModel", model1);
//					intent1.putExtras(b1);
//					
					intent1.putExtra("shopID",lmessage.get(position).getmID());
					intent1.putExtra("shopName", lmessage.get(position).getName());
					intent1.putExtra("isColl", true);
					startActivity(intent1);
					break;
				case 3://购物商品
					Intent intent2 = new Intent(mContext, ShoppingDescActivity.class);
					Bundle b2 = new Bundle();
					GoodsModel mGood=new GoodsModel();
					mGood.setGoodsID(lmessage.get(position).getmID());
					mGood.setGoodsImg(lmessage.get(position).getImg());
					mGood.setGoodsName(lmessage.get(position).getName());
					mGood.setGoodsPlace("");
					mGood.setGoodsPrice(lmessage.get(position).getNowPrice());
					mGood.setSales(lmessage.get(position).getSales());
					b2.putSerializable("GoodsModel", mGood);
					intent2.putExtras(b2);
					intent2.putExtra("shopID", lmessage.get(position).getShopID());
					startActivity(intent2);
					break;
				case 4://团购商品
					FoodShopsModel model14 = new FoodShopsModel();	
					model14.setArea("");
					model14.setLat(lmessage.get(position).getLat());
					model14.setLng(lmessage.get(position).getLng());
					model14.setPreMoney(lmessage.get(position).getPreMoney());
					model14.setShopGrade(lmessage.get(position).getGrade());
					model14.setShopID(lmessage.get(position).getShopID());
					model14.setShopImg(lmessage.get(position).getImg());
					model14.setShopName(lmessage.get(position).getName());
					model14.setShopType(lmessage.get(position).getType());
					
					Intent intent3 = new Intent();
					intent3.setClass(mContext, FoodsImmediatelyBuyActivity.class);
					FoodShopFoodsModel model3 = new FoodShopFoodsModel();
					model3.setFoodID(lmessage.get(position).getmID());
					model3.setFoodImg(lmessage.get(position).getImg());
					model3.setFoodName(lmessage.get(position).getName());
					model3.setNowPrice(lmessage.get(position).getNowPrice());
					model3.setOldPrice(lmessage.get(position).getOldPrice());
					model3.setSales(lmessage.get(position).getSales());
					Bundle b3 = new Bundle();
					b3.putSerializable("FoodShopsModel", model14);
					b3.putSerializable("FoodShopFoodsModel", model3);
					intent3.putExtras(b3);
					intent3.putExtra("foodShopID", lmessage.get(position).getShopID());
					intent3.putExtra("foodShopName", lmessage.get(position).getShopName());
					Log.e("result","name:-->"+lmessage.get(position).getName());
					Log.e("result","shopname:-->"+lmessage.get(position).getShopName());
					startActivity(intent3);
					break;
				case 5://物流
					Intent intent4=new Intent(mContext,WuLiuWebActivity.class);
					intent4.putExtra("orderName", lmessage.get(position).getExpress());
					intent4.putExtra("type", lmessage.get(position).getExpressName());
					intent4.putExtra("typeStatus", 1);
					startActivity(intent4);
					break;
				case 6://新闻
					Intent intent5=new Intent(mContext,NewsDescActivity.class);
					intent5.putExtra("newsUrl", lmessage.get(position).getNewsUrl());
					intent5.putExtra("title", lmessage.get(position).getName());
					intent5.putExtra("img", lmessage.get(position).getImg());
					startActivity(intent5);
					break;
				case 7://团购券使用完毕
					Intent intent6=new Intent(mContext,NewsDescActivity.class);
					intent6.putExtra("newsUrl", lmessage.get(position).getNewsUrl());
					intent6.putExtra("img", lmessage.get(position).getImg());
					intent6.putExtra("type", 6);
					startActivity(intent6);
					break;
				default:
					break;
				}
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}

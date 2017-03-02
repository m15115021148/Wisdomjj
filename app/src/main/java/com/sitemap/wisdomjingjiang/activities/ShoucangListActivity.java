package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.AddressListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.FoodsShopListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.HotleListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.ShoucangListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.ShoucangShopsListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.CollectionThingsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.models.GoodsModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout.OnRefreshListener;

/**
 * com.sitemap.wisdomjingjiang.activities.ShoucangListActivity
 * 
 * @author zhang create at 2016年5月13日 上午9:13:57
 */

public class ShoucangListActivity extends BaseActivity implements
		OnClickListener {
	private ListView mList;// 收藏列表
	private ShoucangListViewAdapter mAdapter;// 收藏适配器
	private ShoucangShopsListViewAdapter mShopAdapter;// 收藏适配器
	private ShoucangListActivity mContext;// 本类
	private ImageView mBack;// 返回按钮
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/** 用户id */
	private String userID;
	/** 页码 */
	// private String page = "0";
	private PullToRefreshLayout ptrl;// 加载刷新
	private int page = 0;// 页数
	private List<CollectionThingsModel> lShoucangList = new ArrayList<CollectionThingsModel>();// 显示的列表
	private List<CollectionThingsModel> lShoucangListMore = new ArrayList<CollectionThingsModel>();// 加载更多回来的列表

	private List<FoodShopsModel> lShoucangShopsList = new ArrayList<FoodShopsModel>();// 显示的列表
	private List<FoodShopsModel> lShoucangShopsListMore = new ArrayList<FoodShopsModel>();// 加载更多回来的列表
	private RadioButton shopping;// 商家
	private RadioButton foods;// 商品
	private RadioGroup type;// 类别
	private int goodsType=1;// 类别（1、商品，2、商家）

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoucang_list);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据

	}

	/**
	 * 初始化控件view
	 */
	public void initView() {
		mList = (ListView) findViewById(R.id.shoucang_list);
		mBack = (ImageView) findViewById(R.id.collect_back);
		type = (RadioGroup) findViewById(R.id.type);
		foods = (RadioButton) findViewById(R.id.foods);
		shopping = (RadioButton) findViewById(R.id.shopping);
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		mBack.setOnClickListener(this);
		if (http == null) {
			http = new HttpUtil(handler);
		}
		progressDialog = MyProgressDialog.createDialog(this);
		ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
		ptrl.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				if (goodsType == 1) {
					if (MyApplication.getNetObject().isNetConnected()) {
						http.sendGet(RequestCode.GETSHOUCANGGOODSFIRST,
								WebUrlConfig.getCollectionThings(
										MyApplication.loginModel.getUserID(),
										String.valueOf(0)));
					} else {
						ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
						ShowContentUtils.showShortToastMessage(mContext,
								RequestCode.NOLOGIN);
					}
				} else {
					if (MyApplication.getNetObject().isNetConnected()) {
						http.sendGet(RequestCode.GETSHOUCANGSHOPSFIRST,
								WebUrlConfig.getCollectionShops(
										MyApplication.loginModel.getUserID(),
										String.valueOf(0)));
					} else {
						ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
						ShowContentUtils.showShortToastMessage(mContext,
								RequestCode.NOLOGIN);
					}
				}

			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				if (goodsType == 1) {
					if (MyApplication.getNetObject().isNetConnected()) {
						http.sendGet(RequestCode.GETSHOUCANGGOODSMORE,
								WebUrlConfig.getCollectionThings(
										MyApplication.loginModel.getUserID(),
										String.valueOf(page)));
					} else {
						ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
						ShowContentUtils.showShortToastMessage(mContext,
								RequestCode.NOLOGIN);
					}
				} else {
					if (MyApplication.getNetObject().isNetConnected()) {
						http.sendGet(RequestCode.GETSHOUCANGSHOPSMORE,
								WebUrlConfig.getCollectionShops(
										MyApplication.loginModel.getUserID(),
										String.valueOf(page)));
					} else {
						ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
						ShowContentUtils.showShortToastMessage(mContext,
								RequestCode.NOLOGIN);
					}
				}

			}
		});

		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			http.sendGet(
					RequestCode.GETSHOUCANGGOODSFIRST,
					WebUrlConfig.getCollectionThings(
							MyApplication.loginModel.getUserID(),
							String.valueOf(0)));
		} else {
			ShowContentUtils.showShortToastMessage(mContext,
					RequestCode.NOLOGIN);
		}

		type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == shopping.getId()) {
					goodsType = 2;
					if (progressDialog == null) {
						progressDialog = MyProgressDialog
								.createDialog(mContext);
					}
					progressDialog.show();
					if (http == null) {
						http = new HttpUtil(handler);
					}
					http.sendGet(RequestCode.GETSHOUCANGSHOPSFIRST,
							WebUrlConfig.getCollectionShops(
									MyApplication.loginModel.getUserID(),
									String.valueOf(0)));
				} else {
					goodsType = 1;
					if (progressDialog == null) {
						progressDialog = MyProgressDialog
								.createDialog(mContext);
					}
					progressDialog.show();
					if (http == null) {
						http = new HttpUtil(handler);
					}
					http.sendGet(RequestCode.GETSHOUCANGGOODSFIRST,
							WebUrlConfig.getCollectionThings(
									MyApplication.loginModel.getUserID(),
									String.valueOf(0)));
				}
			}
		});
		
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (goodsType==2) {//商家
					//（3:购物商家 4:团购商家 5:KTV商家，6酒店）
					switch (Integer.parseInt(lShoucangShopsList.get(position).getShopType())) {
					case 3:
						Intent intent = new Intent();
						intent.setClass(mContext, ShoppingListActivity.class);
						intent.putExtra("shopID", lShoucangShopsList.get(position).getShopID());
						intent.putExtra("shopName", lShoucangShopsList.get(position).getShopName());
						intent.putExtra("isColl", true);
						startActivity(intent);
						break;
					case 4:
						Intent intent1 = new Intent(mContext,FoodsShopActivity.class);
						//将商家id 传过去
						FoodShopsModel model = lShoucangShopsList.get(position);				
						Bundle b = new Bundle();
						b.putSerializable("FoodShopsModel", model);
						intent1.putExtras(b);
						startActivity(intent1);
						break;
					case 5:
						Intent intent2 = new Intent();
						intent2.setClass(mContext, KTVDescActivity.class);
						intent2.putExtra("hotel", lShoucangShopsList.get(position));
						startActivity(intent2);
						break;
					case 6:
						Intent intent3 = new Intent();
						intent3.setClass(mContext, HotleDescActivity.class);
						intent3.putExtra("hotel", lShoucangShopsList.get(position));
						startActivity(intent3);
						break;
					default:
						break;
					}
				}else {//商品
					//（1:团购商品 2:购物商品）
					
					switch (Integer.parseInt(lShoucangList.get(position).getThingType())) {
					case 1:
						FoodShopFoodsModel food=new FoodShopFoodsModel();
						food.setFoodID(lShoucangList.get(position).getThingID());
						food.setFoodName(lShoucangList.get(position).getThingName());
						food.setFoodImg(lShoucangList.get(position).getThingImg());
						food.setNowPrice(lShoucangList.get(position).getNowPrice());
						food.setOldPrice(lShoucangList.get(position).getOldPrice());
						food.setSales(lShoucangList.get(position).getSales());
						Intent intent = new Intent();
						intent.setClass(mContext, FoodsImmediatelyBuyActivity.class);
						Bundle b = new Bundle();
						b.putSerializable("FoodShopFoodsModel", food);
						FoodShopsModel foodShopModel=new FoodShopsModel();
						foodShopModel.setArea(lShoucangList.get(position).getArea());
						foodShopModel.setLat(lShoucangList.get(position).getLat());
						foodShopModel.setLng(lShoucangList.get(position).getLng());
						foodShopModel.setPreMoney(lShoucangList.get(position).getPreMoney());
						foodShopModel.setShopGrade(lShoucangList.get(position).getShopGrade());
						foodShopModel.setShopID(lShoucangList.get(position).getShopID());
						foodShopModel.setShopImg(lShoucangList.get(position).getShopImg());
						foodShopModel.setShopName(lShoucangList.get(position).getShopName());
						foodShopModel.setShopType("1");
						b.putSerializable("FoodShopsModel", foodShopModel);
						intent.putExtras(b);
						intent.putExtra("foodShopID", lShoucangList.get(position).getShopID());
						intent.putExtra("foodShopName", lShoucangList.get(position).getShopName());
						startActivity(intent);
						break;
					case 2:
						Intent intent1 = new Intent(mContext, ShoppingDescActivity.class);
						GoodsModel goodModel=new GoodsModel();
						goodModel.setGoodsID(lShoucangList.get(position).getThingID());
						goodModel.setGoodsImg(lShoucangList.get(position).getThingImg());
						goodModel.setGoodsName(lShoucangList.get(position).getThingName());
						goodModel.setGoodsPlace(lShoucangList.get(position).getThingPlace());
						goodModel.setGoodsPrice(lShoucangList.get(position).getNowPrice());
						goodModel.setSales(lShoucangList.get(position).getSales());
						Bundle b2 = new Bundle();
						b2.putSerializable("GoodsModel",goodModel);
						intent1.putExtras(b2);
						intent1.putExtra("shopID", lShoucangList.get(position).getShopID());
						startActivity(intent1);
						break;
					default:
						break;
					}
				}
			}
		});
		mList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				MyApplication.myAlertDialog(mContext, true, "提示", "是否删除该收藏？", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (progressDialog == null) {
							progressDialog = MyProgressDialog.createDialog(mContext);
						}
						progressDialog.setMessage("正在删除...");
						progressDialog.show();
						if (http == null) {
							http = new HttpUtil(handler);
						}
						
						if (goodsType==2) {
							http.sendGet(RequestCode.DELSHOUCANG,
									WebUrlConfig.deleteCollectionShops(MyApplication.loginModel.getUserID(),lShoucangShopsList.get(position).getShopID()));
						}else {
							http.sendGet(RequestCode.DELSHOUCANG,
									WebUrlConfig.deleteCollectionThings(MyApplication.loginModel.getUserID(),lShoucangList.get(position).getThingID(),lShoucangList.get(position).getThingType()));
						}
						dialog.dismiss();
					}
				}).create().show();
				return true;
			}
		});
	}

	// /**
	// * 初始化listview
	// */
	// private void initFoodsListViewData() {
	// mAdapter = new ShoucangListViewAdapter(mContext,null);
	// mList.setAdapter(mAdapter);
	// }

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
				if (msg.arg1 == RequestCode.GETSHOUCANGGOODSFIRST) {
					page = 0;
					Log.i("TAG", msg.obj.toString());
					lShoucangList.clear();
					lShoucangList = JSONObject.parseArray(msg.obj.toString(),
							CollectionThingsModel.class);
					mAdapter = new ShoucangListViewAdapter(mContext,
							lShoucangList, 1);
					mList.setAdapter(mAdapter);
					page += 1;
					ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
				if (msg.arg1 == RequestCode.GETSHOUCANGGOODSMORE) {// 加载更多
					page += 1;
					Log.i("TAG", "request:" + msg.obj.toString());
					lShoucangListMore.clear();
					lShoucangListMore = JSONObject.parseArray(
							msg.obj.toString(), CollectionThingsModel.class);
					for (int i = 0; i < lShoucangListMore.size(); i++) {
						lShoucangList.add(lShoucangListMore.get(i));
					}
					mAdapter = new ShoucangListViewAdapter(mContext,
							lShoucangList, 1);
					mList.setAdapter(mAdapter);
					// 千万别忘了告诉控件加载完毕了哦！
					ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				if (msg.arg1 == RequestCode.GETSHOUCANGSHOPSFIRST) {
					page = 0;
					Log.i("TAG", msg.obj.toString());
					lShoucangShopsList.clear();
					lShoucangShopsList = JSONObject.parseArray(
							msg.obj.toString(), FoodShopsModel.class);
					mShopAdapter = new ShoucangShopsListViewAdapter(mContext,
							lShoucangShopsList, 2);
					mList.setAdapter(mShopAdapter);
					page += 1;
					ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
				if (msg.arg1 == RequestCode.GETSHOUCANGSHOPSMORE) {// 加载更多
					page += 1;
					Log.i("TAG", "request:" + msg.obj.toString());
					lShoucangShopsListMore.clear();
					lShoucangShopsListMore = JSONObject.parseArray(
							msg.obj.toString(), FoodShopsModel.class);
					for (int i = 0; i < lShoucangShopsListMore.size(); i++) {
						lShoucangShopsList.add(lShoucangShopsListMore.get(i));
					}
					mShopAdapter = new ShoucangShopsListViewAdapter(mContext,
							lShoucangShopsList, 2);
					mList.setAdapter(mShopAdapter);
					// 千万别忘了告诉控件加载完毕了哦！
					ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				if (msg.arg1 == RequestCode.DELSHOUCANG) {

					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {
						ShowContentUtils.showLongToastMessage(mContext, "删除成功");
						if (goodsType==2) {
							if (progressDialog == null) {
								progressDialog = MyProgressDialog.createDialog(mContext);
							}
							progressDialog.setMessage("正在刷新列表...");
							progressDialog.show();
							if (http == null) {
								http = new HttpUtil(handler);
							}
							http.sendGet(RequestCode.GETSHOUCANGSHOPSFIRST,
									WebUrlConfig.getCollectionShops(
											MyApplication.loginModel.getUserID(),
											String.valueOf(0)));
						}else {
							if (progressDialog == null) {
								progressDialog = MyProgressDialog.createDialog(mContext);
							}
							progressDialog.setMessage("正在刷新列表...");
							progressDialog.show();
							if (http == null) {
								http = new HttpUtil(handler);
							}
							http.sendGet(RequestCode.GETSHOUCANGGOODSFIRST,
									WebUrlConfig.getCollectionThings(
											MyApplication.loginModel.getUserID(),
											String.valueOf(0)));
						}
					} else {
						ShowContentUtils.showLongToastMessage(mContext,
								model.getErrorMsg());
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				ptrl.loadmoreFinish(PullToRefreshLayout.FAIL);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.GETSHOUCANGGOODSFIRST) {
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
		if (v == mBack) {
			onBackPressed();
			finish();
		}
	}
}

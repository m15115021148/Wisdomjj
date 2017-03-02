package com.sitemap.wisdomjingjiang.activities;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.SearchListListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.SearchResultAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopInfoModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.models.GoodsModel;
import com.sitemap.wisdomjingjiang.models.SearchIndexModel;
import com.sitemap.wisdomjingjiang.models.SearchResultModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * @author chenmeng 搜索页面
 */
public class SearchActivity extends BaseActivity implements OnClickListener {
	/** 全局类 */
	private Context mContext;
	/** 搜索 列表 的listview 输入时显示 */
	private ListView mSearchIndexLv;
	/** 搜索结果 的listview 选择列表item时 显示 */
	private ListView mSearchResultLv;
	/** 输入的结果 */
	private EditText mSearch;
	/** 返回上一层 */
	private ImageView mBack;
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/** 搜索索引 数据 */
	private List<SearchIndexModel> mIndexList = null;
	/** 搜索结果 数据 */
	private List<SearchResultModel> mResultList = null;
	/** 经度 */
	private String lng = String.valueOf(MyApplication.lng);
	/** 纬度 */
	private String lat = String.valueOf(MyApplication.lat);
	/** 搜索索引 adapter */
	private SearchListListViewAdapter mIndexAdapter;
	/** 搜索结果 适配器 */
	private SearchResultAdapter mResultAdapter;
	/** 团购商家 的团购详情 数据 */
	private List<FoodShopFoodsModel> mFoodsList = null;
	/** 团购商家详细信息返回实体类 */
	private FoodShopInfoModel foodInfomodel;
	private FoodShopsModel foodModel1;//团购商品实体类
	private SearchResultModel model;//搜索的实体类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		mContext = this;
		initView();// 初始化 view
		initData();// 数据
		initSearchData();// 初始化搜索
	}

	/**
	 * 初始化 view
	 */
	private void initView() {
		mSearchIndexLv = (ListView) findViewById(R.id.search_index_listview);
		mSearch = (EditText) findViewById(R.id.search_result).findViewById(
				R.id.search_content);
		mBack = (ImageView) findViewById(R.id.search_result).findViewById(
				R.id.iv_back);
		mSearchResultLv = (ListView) findViewById(R.id.search_result_listview);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mBack.setOnClickListener(this);
		if (http == null) {
			http = new HttpUtil(handler);
		}
		//延时打开软键盘 保证数据加载完毕
		Timer timer = new Timer();  
	    timer.schedule(new TimerTask() {  	           
	         public void run()   {  
	             InputMethodManager inputManager =  
	                 (InputMethodManager)mSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
	             inputManager.showSoftInput(mSearch, 0);  
	         }  
	           
	     },  998);
	}

	// ---------------------------------搜索EditText----------------------------------
	/**
	 * 初始化搜索
	 */
	private void initSearchData() {
		// edittext 点击事件
		mSearch.addTextChangedListener(new TextWatcher() {
			int len = 0;//记录字符串被删除字符之前，字符串的长度
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(mSearch.length()==0){
					mSearchIndexLv.setVisibility(View.GONE);
					mSearchResultLv.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				 len = s.length();
			}

			@Override
			public void afterTextChanged(Editable s) {
//				if(s.toString().length()>len){
				if (s.length()>0) {
					getSearchIndex();
				}
				
//				}
			}
		});

	}

	// ---------------------------------搜索列表----------------------------------
	/**
	 * 初始化 搜索列表 的listview
	 */
	private void initListListView() {
		// 显示 搜索列表的listview
		mSearchIndexLv.setVisibility(View.VISIBLE);
		mIndexAdapter = new SearchListListViewAdapter(mContext, mIndexList);
		mSearchIndexLv.setAdapter(mIndexAdapter);
		// 点击事件
		mSearchIndexLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				getSearchResultData(mIndexList.get(position).getName());
			}
		});
	}

	// ---------------------------------搜索结果----------------------------------
	/**
	 * 初始化 搜索结果的 listview
	 */
	private void initResultListView() {
		// 隐藏搜索列表 listview
		mSearchIndexLv.setVisibility(View.GONE);
		// 显示搜索结果 的listview
		mSearchResultLv.setVisibility(View.VISIBLE);
		mResultAdapter = new SearchResultAdapter(mContext, mResultList, lng,
				lat);
		mSearchResultLv.setAdapter(mResultAdapter);
		mSearchResultLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 model = mResultList.get(position);
				FoodShopsModel hotel = new FoodShopsModel();
				Intent intent = new Intent();
				Bundle b = new Bundle();
				switch (Integer.parseInt(model.getType())) {
				case 1:// 团购商家
					FoodShopsModel foodModel = new FoodShopsModel();
					foodModel.setShopID(model.getrID());
					foodModel.setShopName(model.getName());
					foodModel.setShopGrade(model.getGrade());
					foodModel.setPreMoney(model.getPreMoney());
					foodModel.setLat(model.getLat());
					foodModel.setLng(model.getLng());
					foodModel.setShopImg(model.getImg());
					foodModel.setArea("");
					foodModel.setShopType(model.getType());
					
					intent.setClass(mContext, FoodsShopActivity.class);
					b.putSerializable("FoodShopsModel", foodModel);
					intent.putExtras(b);
					startActivity(intent);
					break;
				case 2:// 购物商家
					intent.setClass(mContext, ShoppingListActivity.class);
					intent.putExtra("shopID", model.getrID());
					intent.putExtra("shopName", model.getName());
					intent.putExtra("isColl", true);
					startActivity(intent);
					break;
				case 3:// 购物商品
					intent.setClass(mContext, ShoppingDescActivity.class);
					GoodsModel goodsModel = new GoodsModel();
					goodsModel.setGoodsID(model.getrID());
					goodsModel.setGoodsName(model.getName());
					goodsModel.setGoodsPrice(model.getPrice());
					goodsModel.setSales(model.getSales());
					goodsModel.setGoodsImg(model.getImg());
					goodsModel.setGoodsPlace("");
					b.putSerializable("GoodsModel", goodsModel);
					intent.putExtra("shopID", model.getShopID());
					intent.putExtras(b);
					startActivity(intent);
					break;
				case 4:// 团购商品
					 foodModel1 = new FoodShopsModel();
					foodModel1.setShopID(model.getShopID());
					foodModel1.setShopName(model.getName());
					foodModel1.setShopGrade(model.getGrade());
					foodModel1.setPreMoney(model.getPreMoney());
					foodModel1.setLat(model.getLat());
					foodModel1.setLng(model.getLng());
					foodModel1.setShopImg(model.getImg());
					foodModel1.setArea("");
					foodModel1.setShopType(model.getType());
					
					getDataFoodsDesc(model.getShopID());
					
					break;
				case 5:// KTV
					intent.setClass(mContext, KTVDescActivity.class);
					hotel.setShopName(model.getName());
					hotel.setShopGrade(model.getGrade());
					hotel.setShopID(model.getrID());
					hotel.setLat(model.getLat());
					hotel.setLng(model.getLng());
					hotel.setShopImg(model.getImg());
					hotel.setPreMoney(model.getPreMoney());
					hotel.setArea("");
					b.putSerializable("hotel", hotel);
					intent.putExtras(b);
					startActivity(intent);
					break;
				case 6:// 酒店
					intent.setClass(mContext, HotleDescActivity.class);
					hotel.setShopName(model.getName());
					hotel.setShopGrade(model.getGrade());
					hotel.setShopID(model.getrID());
					hotel.setLat(model.getLat());
					hotel.setLng(model.getLng());
					hotel.setShopImg(model.getImg());
					hotel.setPreMoney(model.getPreMoney());
					hotel.setArea("");
					b.putSerializable("hotel", hotel);
					intent.putExtras(b);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 搜索索引
	 */
	private void getSearchIndex() {
		if (MyApplication.getNetObject().isNetConnected()) {
//			progressDialog = MyProgressDialog.createDialog(mContext);
//			if (progressDialog != null && !progressDialog.isShowing()) {
//				progressDialog.setMessage("加载中...");
//				progressDialog.show();
//			}
			String searchIndex = WebUrlConfig.searchIndex(mSearch.getText()
					.toString());
			http.sendGet(RequestCode.SEARCHINDEX, searchIndex);
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	/**
	 * 搜索结果
	 */
	private void getSearchResultData(String index) {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String searchResult = WebUrlConfig.searchResult(index);
			http.sendGet(RequestCode.SEARCHRESULT, searchResult);
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
				if (msg.arg1 == RequestCode.SEARCHINDEX) {
					mIndexList = JSONObject.parseArray(msg.obj.toString(),
							SearchIndexModel.class);
					if(mIndexList.size()==0){
						mSearchIndexLv.setVisibility(View.GONE);
						mSearchResultLv.setVisibility(View.GONE);
						ShowContentUtils.showLongToastMessage(mContext,
								"暂无相关内容");
					}
					if (mIndexList!=null) {
						initListListView();
						mIndexAdapter.notifyDataSetChanged();
					}
				}
				if (msg.arg1 == RequestCode.SEARCHRESULT) {
					mResultList = JSONObject.parseArray(msg.obj.toString(),
							SearchResultModel.class);
					if (mResultList != null) {
						initResultListView();
						mResultAdapter.notifyDataSetChanged();
					}
				}
				// 团购详情
				if (msg.arg1 == RequestCode.GETFOODSHOPFOODS) {
					mFoodsList = JSONArray.parseArray(msg.obj.toString(),
							FoodShopFoodsModel.class);
					
				}
				// 商家信息 团购
				if (msg.arg1 == RequestCode.GETFOODSHOPINFO) {
					foodInfomodel = (FoodShopInfoModel) JSONObject.parseObject(
							msg.obj.toString(), FoodShopInfoModel.class);
					if (mFoodsList != null) {
						Intent intent = new Intent();
						Bundle b = new Bundle();
						intent.setClass(mContext, FoodsImmediatelyBuyActivity.class);
						FoodShopFoodsModel foodsModel = getFoodsModel(model);
						b.putSerializable("FoodShopFoodsModel", foodsModel);
						b.putSerializable("FoodShopsModel", foodModel1);
						intent.putExtras(b);
						intent.putExtra("foodShopID", model.getShopID());
						intent.putExtra("foodShopName",foodInfomodel.getShopName());
						startActivity(intent);
					} 
				}
				// 购物 信息
				// if (msg.arg1 == RequestCode.GOODSLIST) {
				// mGoodsList = JSON.parseArray(msg.obj.toString(),
				// GoodsModel.class);
				// }
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.SEARCHINDEX) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 获取团购详情信息
	 */
	private void getDataFoodsDesc(String foodShopID) {
		if (MyApplication.getNetObject().isNetConnected()) {
			String foodsDes = WebUrlConfig.getFoodShopFoods(foodShopID);
			String foods = WebUrlConfig.getFoodShopInfo(foodShopID);
			http.sendGet(RequestCode.GETFOODSHOPFOODS, foodsDes);
			http.sendGet(RequestCode.GETFOODSHOPINFO, foods);// 获得团购商家信息
		} else {// 无法连接网络
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	/**
	 * 通过商家id 查询商品信息 团购
	 * 
	 * @param model
	 * @return
	 */
	private FoodShopFoodsModel getFoodsModel(SearchResultModel model) {
		FoodShopFoodsModel foodsModel = new FoodShopFoodsModel();
		for (int i = 0; i < mFoodsList.size(); i++) {
			foodsModel = mFoodsList.get(i);
			if (foodsModel.getFoodID().equals(model.getrID())) {
				return mFoodsList.get(i);
			}
		}
		return foodsModel;
	}

	/**
	 * 获取购物商品 信息
	 * 
	 * @param foodShopID
	 */
	private void getDataGoodsDesc(String shopID, int pageNumber) {
		if (MyApplication.getNetObject().isNetConnected()) {
			http.sendGet(RequestCode.GOODSLIST,
					WebUrlConfig.getGoodShopInfo(shopID, pageNumber + ""));
		} else {// 无法连接网络
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mBack) {
			onBackPressed();
			finish();
		}
	}

}

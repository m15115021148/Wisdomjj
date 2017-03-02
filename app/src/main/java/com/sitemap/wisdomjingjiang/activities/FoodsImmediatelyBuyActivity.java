package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.FoodsListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.FoodsShopListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.FoodsTypeListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.FoodInfoModel;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsTypeFoodsModel;
import com.sitemap.wisdomjingjiang.share.UMShareUtil;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.CircleImageView;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author chenmeng 团购立即购买 显示 页面(加入购物车等) 团购详情
 */
public class FoodsImmediatelyBuyActivity extends BaseActivity implements
		OnClickListener {
	/** 全局类 */
	private Context mContext;
	/** 返回上一层 */
	private ImageView mBack;
	/** 标题栏 右侧 的更多 默认不显示 */
	private ImageView mMore;
	/** 标题栏 的标题 */
	private TextView mTitle;
	/** 获取图片 网络 */
	private static String[] foodImgs;
	/** 商品图片 展示 的viewpager */
	private ViewPager mVp;
	/** 商品图片 相对应的导航圈 */
	private LinearLayout dotLayout;
	/** 图片的数量以及导航圈的数量 */
	public static int NUMBER_MAX = 4;
	/** 轮播图片的点的集合 */
	private List<View> dotList = new ArrayList<View>();
	/** 图片 标签 */
	public static final String TAG = "img_flag";
	/** 立即购买 */
	private TextView mBuy;
	/** 加入购物车 */
	private TextView mAddShopCart;
	/** 标致 是点击加入购物车，还是立即购买 1、表示加入购物车 2、表示立即购买 */
	public static final String FOOD_WAY_FLAG = "food_way_flag";
	/** 更多评论 */
	private TextView mCommentMore;
	/** 店铺名 收藏 购物车  */
	private LinearLayout mMerchantName, mCollect, mShopCart;
	/**没有评论不显示*/ 
	private RelativeLayout commentShow;
	/** 商家详情 页面 中的团购详情 实体类 */
	private FoodShopFoodsModel foodsModel;
	/** 团购 文字描述 没有评论显示的内容 */
	private TextView mFoodsDesc, commentNo;
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/** 用户头像 */
	private CircleImageView userImg;
	/** 用户名,评论 */
	private TextView userName, userCommment,userTime;
	/** 商家地点 */
	private TextView cityName;
	/** 现价 门市价 */
	private TextView foodsNewPrice, foodsOldPrice;
	/** 月销售 */
	private TextView foodsSales;
	/** 数量的减 加 */
	private TextView foodsReduce, foodsAdd;
	/** 数量 */
	private TextView foodsNumber;
	/** 团购商品id */
	private String foodID;
	/** 商家id */
	private String foodShopID;
	/** 商家名称 */
	private String foodShopName;
	/** 用户id */
	private String userID;
	/** 默认数量 */
	private static int minNum = 1;
	/** 最大数量 */
	private static int maxNum = 99;
	/** 商品id 类别（1:团购商品 2:购物商品） */
	private String thingID, shopType;
	/** 跳转过来的类 */
	private FoodShopsModel foodShopModel;
	private FoodInfoModel model;// 商品详情
	/**库存量*/ 
	private TextView mInventory;
	
	private TextView youxiaoqi;//有效期
	/**团购商家 的团购详情 数据*/ 
	private List<FoodShopsTypeFoodsModel> mList = new ArrayList<FoodShopsTypeFoodsModel>();
	
	private FoodsTypeListViewAdapter mAdapter;//推荐的商品详情适配器
	private ListView mLv;//推荐的商品列表
	private LinearLayout tuijianlay;//团购推荐布局

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foods_immediately_buy);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据
		// initViewPager();// 初始化viewpager
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.foods_buy_title).findViewById(
				R.id.back);
		mMore = (ImageView) findViewById(R.id.foods_buy_title).findViewById(
				R.id.more);
		mTitle = (TextView) findViewById(R.id.foods_buy_title).findViewById(
				R.id.title);
		mVp = (ViewPager) findViewById(R.id.foods_buy_viewpager);
		dotLayout = (LinearLayout) findViewById(R.id.foods_buy_indictor);
		mBuy = (TextView) findViewById(R.id.foods_buy);
		mAddShopCart = (TextView) findViewById(R.id.add_shopping_cart);
		mCommentMore = (TextView) findViewById(R.id.foods_comment_more);
		mMerchantName = (LinearLayout) findViewById(R.id.foods_merchant_name);
		mCollect = (LinearLayout) findViewById(R.id.foods_collect);
		mFoodsDesc = (TextView) findViewById(R.id.foods_desc);
		userImg = (CircleImageView) findViewById(R.id.user_img);
		userName = (TextView) findViewById(R.id.user_name);
		userCommment = (TextView) findViewById(R.id.user_comment);
		foodsNewPrice = (TextView) findViewById(R.id.foods_now_price);
		foodsOldPrice = (TextView) findViewById(R.id.foods_old_price);
		foodsSales = (TextView) findViewById(R.id.foods_sales);
		foodsReduce = (TextView) findViewById(R.id.foods_reduce);
		foodsNumber = (TextView) findViewById(R.id.foods_number);
		foodsAdd = (TextView) findViewById(R.id.foods_add);
		cityName = (TextView) findViewById(R.id.foods_city_name);
		mShopCart = (LinearLayout) findViewById(R.id.shop_cart);
		commentShow = (RelativeLayout) findViewById(R.id.comment_show);
		commentNo = (TextView) findViewById(R.id.comment_nono);
		mInventory = (TextView) findViewById(R.id.foods_inventory);
		userTime = (TextView) findViewById(R.id.user_time);
		youxiaoqi= (TextView) findViewById(R.id.youxiaoqi);
		mLv=(ListView)findViewById(R.id.foods_listview);
		tuijianlay=(LinearLayout) findViewById(R.id.tuijianlay);
		mLv.setFocusable(false);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mBack.setOnClickListener(this);
		mMore.setOnClickListener(this);
		mAddShopCart.setOnClickListener(this);
		mCommentMore.setOnClickListener(this);
		mMerchantName.setOnClickListener(this);
		mCollect.setOnClickListener(this);
		mShopCart.setOnClickListener(this);
		foodsReduce.setOnClickListener(this);
		foodsAdd.setOnClickListener(this);
		// 显示
		mMore.setVisibility(View.VISIBLE);
		mBuy.setOnClickListener(this);
		foodsOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线

		if (http == null) {
			http = new HttpUtil(handler);
		}

		// 获取商家团购的信息
		Intent intent = getIntent();
		foodShopModel = (FoodShopsModel) intent
				.getSerializableExtra("FoodShopsModel");
		foodsModel = (FoodShopFoodsModel) intent
				.getSerializableExtra("FoodShopFoodsModel");
		mTitle.setText(foodsModel.getFoodName());
		foodsNewPrice.setText(foodsModel.getNowPrice());
		foodsOldPrice.setText("¥" + foodsModel.getOldPrice());
		foodsSales.setText("总销量" + foodsModel.getSales() + "笔");

		foodID = foodsModel.getFoodID();		
		foodShopID = intent.getStringExtra("foodShopID");
		foodShopName = intent.getStringExtra("foodShopName");

		loadNetData();
	}

	/**
	 * 加载网络数据
	 */
	private void loadNetData() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			// 获取是否收藏
			if (MyApplication.isLogin) {// 登录时 才有请求
				http.sendGet(
						RequestCode.GETISCOLLECTION,
						WebUrlConfig.getIsCollection(
								MyApplication.loginModel.getUserID(),
								String.valueOf(1), foodID));
			}
			// 获取团购详情
			http.sendGet(RequestCode.GETFOODINFO,
					WebUrlConfig.getFoodInfo(foodsModel.getFoodID()));
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	/**
	 * 添加我的收藏---商品
	 */
	private void addCollectFoodsData() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String addCollectionThings = WebUrlConfig.addCollectionThings(
					MyApplication.loginModel.getUserID(), foodID,
					String.valueOf(1));
			RequestParams params = http.getParams(addCollectionThings);
			http.sendPost(RequestCode.FOODSHOPCOLLECT, params);
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	/**
	 * 团购推荐
	 */
	private void initFoodsData() {
		String foodRecommend = WebUrlConfig.getTypeFoods(model.getFoodTypeID(),foodsModel.getFoodID());
		http.sendGet(RequestCode.GETFOODRECOMMEND, foodRecommend);		
	}

	/**
	 * 取消收藏
	 */
	private void cancelCollect() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			http.sendGet(
					RequestCode.CANCELCOLLECTION,
					WebUrlConfig.cancelCollection(
							MyApplication.loginModel.getUserID(),
							String.valueOf(1), foodID));
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
				// 获取团购详情
				if (msg.arg1 == RequestCode.GETFOODINFO) {
					Log.e("result", "库存量有没有啊:" + msg.obj.toString());
					model = JSONObject.parseObject(msg.obj.toString(),
							FoodInfoModel.class);
					// 这是数量的最小值
					if (!model.getInventory().equals("0")
							&& !model.getInventory().equals("")
							&& model.getInventory() != null) {
						foodsNumber.setText(String.valueOf(minNum));
						mInventory.setText("库存量:"+model.getInventory()+"件");
					} else {
						foodsNumber.setText(model.getInventory());
						mInventory.setText("库存量:已售罄");
					}
					mFoodsDesc.setText(model.getFoodBrief());
					userName.setText(model.getUserName());
					cityName.setText(model.getProvince() + model.getCity());
					if (!model.getUserImg().equals("")) {	
						x.image().bind(userImg, model.getUserImg(),MyApplication.imageComment);
					} else {						
						userImg.setBackgroundResource(R.drawable.user_comment);
					}
					userTime.setText(model.getCommentTime());
					userCommment.setText("\t\t"+model.getUserComment());
					if (model.getUserComment().equals("")
							|| model.getUserComment() == null) {
						// 没有评论
						commentShow.setVisibility(View.GONE);
						userCommment.setVisibility(View.GONE);
						commentNo.setVisibility(View.VISIBLE);
						mCommentMore.setVisibility(View.GONE);
					}
					if (model.getDeadTime()!=null&&!model.getDeadTime().equals("")) {
						youxiaoqi.setText("有效期至："+model.getDeadTime());
					}
					foodImgs = model.getFoodImg().split(";");
					
					NUMBER_MAX = foodImgs.length;
					if (NUMBER_MAX == 1) {
						dotLayout.setVisibility(View.GONE);
					}
					initFoodsData();//请求推荐列表
					initViewPager();// 初始化viewpager
				}
				// 是否已收藏
				if (msg.arg1 == RequestCode.GETISCOLLECTION) {
					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {// 已收藏
						mCollect.setSelected(true);
					} else {
						mCollect.setSelected(false);
					}
				}
				// 收藏成功
				if (msg.arg1 == RequestCode.FOODSHOPCOLLECT) {
					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {
						ShowContentUtils.showLongToastMessage(mContext, "收藏成功");
					}
				}
				// 取消收藏
				if (msg.arg1 == RequestCode.CANCELCOLLECTION) {
					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {
						ShowContentUtils.showLongToastMessage(mContext,
								"取消收藏成功");
					} else {
						ShowContentUtils.showLongToastMessage(mContext,
								model.getErrorMsg());
					}
				}
				// 加入购物车
				if (msg.arg1 == RequestCode.ADDFOOD) {
					CodeModel codeModel = JSONObject.parseObject(
							msg.obj.toString(), CodeModel.class);
					if (codeModel.getStatus() == 0) {// 返回状态标识符（0：成功，1：失败）
						ShowContentUtils.showLongToastMessage(mContext,
								"加入购物车成功");
					} else {
						ShowContentUtils.showLongToastMessage(mContext,
								codeModel.getErrorMsg());
					}
				}
				// 团购推荐
				if (msg.arg1 == RequestCode.GETFOODRECOMMEND) {
					Log.i("TAG", "团购推荐:"+msg.obj.toString());
					mList = JSONArray.parseArray(msg.obj.toString(),
							FoodShopsTypeFoodsModel.class);
					if (mList.size()<=0) {
						tuijianlay.setVisibility(View.GONE);
					}else {
						tuijianlay.setVisibility(View.VISIBLE);
						initListViewFoodsData();
					}
						
				
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.GETFOODINFO) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
			if (mList.size()<=0) {
				tuijianlay.setVisibility(View.GONE);
			}
		}
	};

	/**
	 * 初始化viewpager
	 */
	private void initViewPager() {
		// 画点
		MyApplication.drawPoint(mContext, dotLayout, dotList, NUMBER_MAX);
		;
		mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				MyFragment fragment = new MyFragment();
				Bundle b = new Bundle();
				b.putInt(TAG, position);
				fragment.setArguments(b);
				return fragment;
			}

			@Override
			public int getCount() {// 广告的页面数量
				return NUMBER_MAX;
			}
		});

		// 设置viewpager滑动事件
		mVp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < dotList.size(); i++) {
					if (i == position) {
						dotList.get(position).setBackgroundResource(
								R.drawable.point_indictor_unselect);
					} else {
						dotList.get(i).setBackgroundResource(
								R.drawable.point_indictor_select);
					}
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	/**
	 * 添加团购商品到购物车
	 */
	private void addShopCart() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			userID = MyApplication.loginModel.getUserID();
			String addFood = WebUrlConfig.addFood(userID, foodID, foodShopID,
					foodShopName, foodsNumber.getText().toString());
			RequestParams params = http.getParams(addFood);
			http.sendPost(RequestCode.ADDFOOD, params);
		} else {
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
		if (v == mMore) {// 分享
			if(model==null){
				return;
			}
			UMShareUtil util = new UMShareUtil(this);
			if(!model.getFoodImg().equals("")&&model.getFoodImg()!=null){
				util.shareNetImage(foodsModel.getFoodName(), model.getFoodBrief(),
						model.getFoodImg(), MyApplication.share);
			}else{//没有图片 默认分享
//				ShowContentUtils.showLongToastMessage(mContext, "缺少图片不能分享");				
				util.shareDrawableImage(foodsModel.getFoodName(), model.getFoodBrief(),
						R.drawable.icon, MyApplication.share);
			}
			

		}
		if (v == mBuy) {// 立即购买
			if(model==null){
				return;
			}
			// 判断用户是否登录
			if (MyApplication.isLogin) {
				// Intent intent = new
				// Intent(mContext,FoodsAddShopCartActivity.class);
				// intent.putExtra("foodShopID", foodShopID);
				// intent.putExtra("foodShopName", foodShopName);
				// intent.putExtra("foodNum", foodsNumber.getText().toString());
				// //将食商家中的团购返回的实体类 传递过去
				// Bundle b = new Bundle();
				// b.putSerializable("FoodShopFoodsModel", foodsModel);
				// intent.putExtras(b);
				// startActivity(intent);
				// overridePendingTransition(R.anim.shopping_buy_entry,
				// R.anim.shoping_desc_exit);
				if (!model.getInventory().equals("0")
						&& !model.getInventory().equals("")
						&& model.getInventory() != null) {// 库存量不为零
					Intent intent = new Intent(mContext,
							SubFoodsOrderActivity.class);
					intent.putExtra("foodShopID", foodShopID);
					intent.putExtra("shopName", foodShopName);
					intent.putExtra("foodNum", foodsNumber.getText().toString());
					// 将食商家中的团购返回的实体类 传递过去
					Bundle b = new Bundle();
					b.putSerializable("FoodShopFoodsModel", foodsModel);
					intent.putExtras(b);
					startActivity(intent);
				} else {
					ShowContentUtils.showLongToastMessage(mContext, "库存不足");
				}
			} else {
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
			}
		}
		if (v == mAddShopCart) {// 加入购物车
			if(model==null){
				return;
			}
			// 判断用户是否登录
			if (MyApplication.isLogin) {
				if (!model.getInventory().equals("0")
						&& !model.getInventory().equals("")
						&& model.getInventory() != null) {// 库存量不为零
					addShopCart();
				} else {
					ShowContentUtils.showLongToastMessage(mContext, "库存不足");
				}
			} else {
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
			}
		}
		if (v == mCommentMore) {// 更多评论
			if(model==null){
				return;
			}
			Intent intent = new Intent(mContext, CommentListActivity.class);
			intent.putExtra("foodID", foodID);
			startActivity(intent);
		}
		if (v == mMerchantName) {// 店铺
			if(model==null){
				return;
			}
			Intent intent = new Intent(mContext, FoodsShopActivity.class);
			// 将商家id 传过去
			Bundle b = new Bundle();
			b.putSerializable("FoodShopsModel", foodShopModel);
			Log.e("result", foodShopModel.getShopID());
			intent.putExtras(b);
			startActivity(intent);
			finish();
		}
		if (v == mCollect) {// 收藏
			if(model==null){
				return;
			}
			// 判断用户是否登录
			if (!MyApplication.isLogin) {
				mCollect.setSelected(false);
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
				return;
			}
			if (mCollect.isSelected()) {// 已收藏 点击取消
				cancelCollect();
				mCollect.setSelected(false);
			} else {// 未收藏 点击收藏
				addCollectFoodsData();
				mCollect.setSelected(true);
			}
		}
		if (v == foodsReduce) {// 数量的减
			if(model==null){
				return;
			}
			int num = Integer.parseInt(foodsNumber.getText().toString());
			if (num > minNum) {
				foodsNumber.setText(String.valueOf(num - 1));
			} else {
				if(num==0){
					ShowContentUtils.showLongToastMessage(mContext, "库存不足");	
				}
			}
		}
		if (v == foodsAdd) {// 数量的加
			if(model==null){
				return;
			}
			if (!model.getInventory().equals("0")
					&& !model.getInventory().equals("")
					&& model.getInventory() != null) {
				maxNum = Integer.parseInt(model.getInventory());
				int num = Integer.parseInt(foodsNumber.getText().toString());
				if (num < maxNum) {
					foodsNumber.setText(String.valueOf(num + 1));
				} else {
					ShowContentUtils.showLongToastMessage(mContext, "已达到最大数量");
				}
			} else {
				ShowContentUtils.showLongToastMessage(mContext, "库存不足");
			}

		}
		if (v == mShopCart) {// 购物车
			// 判断用户是否登录
			if (MyApplication.isLogin) {
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.putExtra("type", 1);
				startActivity(intent);
				MainActivity.mContext.setShopCartCheck();
			} else {
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
			}
		}
	}

	/**
	 * 图片viewpager页面中的子布局
	 */
	public static class MyFragment extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ImageView iv = new ImageView(inflater.getContext());
			int position = getArguments().getInt(TAG);
			iv.setScaleType(ScaleType.FIT_XY);
			x.image().bind(iv, foodImgs[position], MyApplication.imageOptions);
			return iv;
		}

	}
	
	/**
	 * 初始化 团购推荐
	 */
	private void initListViewFoodsData() {
		mAdapter = new FoodsTypeListViewAdapter(mContext,mList);
		mLv.setAdapter(mAdapter);
		MyApplication.setListViewHeight(mLv);
		
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(mContext, FoodsImmediatelyBuyActivity.class);
				FoodShopFoodsModel model =new FoodShopFoodsModel();
				model.setFoodID(mList.get(position).getFoodID());
				model.setFoodImg(mList.get(position).getFoodImg());
				model.setFoodName(mList.get(position).getFoodName());
				model.setNowPrice(mList.get(position).getNowPrice());
				model.setOldPrice(mList.get(position).getOldPrice());
				model.setSales(mList.get(position).getSales());
				FoodShopsModel foodShopModel=new FoodShopsModel();
				foodShopModel.setArea(mList.get(position).getArea());
				foodShopModel.setGoodsName(mList.get(position).getGoodsName());
				foodShopModel.setLat(mList.get(position).getLat());
				foodShopModel.setLng(mList.get(position).getLng());
				foodShopModel.setPreMoney(mList.get(position).getPreMoney());
				foodShopModel.setShopGrade(mList.get(position).getShopGrade());
				foodShopModel.setShopID(mList.get(position).getShopID());
				foodShopModel.setShopImg(mList.get(position).getShopImg());
				foodShopModel.setShopName(mList.get(position).getShopName());
				foodShopModel.setShopType(mList.get(position).getShopType());
				Bundle b = new Bundle();
				b.putSerializable("FoodShopFoodsModel", model);
				b.putSerializable("FoodShopsModel", foodShopModel);
				intent.putExtras(b);
				intent.putExtra("foodShopID", mList.get(position).getShopID());
				intent.putExtra("foodShopName", mList.get(position).getShopName());
				startActivity(intent);
			}
		});
		
	}

}

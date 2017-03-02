package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.FoodsListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopInfoModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.share.UMShareUtil;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

/**
 * com.sitemap.wisdomjingjiang.activities.FoodsShopActivity
 * @author zhang
 * create at 2016年5月12日 上午9:13:57
 * 商家详情 页面
 */
public class FoodsShopActivity extends BaseActivity implements OnClickListener{
	/** 商家图片 展示 的viewpager */
	private ViewPager mVp;
	/** 商家图片 相对应的导航圈 */
	private LinearLayout dotLayout;
	/** 图片的数量以及导航圈的数量 */
	public static int NUMBER_MAX = 4;
	/**轮播图片的点的集合*/ 
	private List<View> dotList = new ArrayList<View>(); 
	/** 图片 标签 */
	public static final String TAG = "img_flag";
	/**商家团购列表*/ 
	private ListView mLv;
	/**商家团购适配器*/ 
	private FoodsListViewAdapter mAdapter;
	/**本类*/ 
	private FoodsShopActivity mContext;
	/**返回上一层*/ 
	private ImageView mBack;
	/**收藏*/ 
	private CheckBox mCollect;
	/**标题栏 标题*/ 
	private TextView mTitle,mMore;
	/**网络请求*/ 
	private HttpUtil http;
	/**进度条*/ 
	private static MyProgressDialog progressDialog,progress;
	/**团购商家 的团购详情 数据*/ 
	private List<FoodShopFoodsModel> mList = null;
	/**商家id*/ 
	private String foodShopID;
	/**商家名称*/ 
	private String foodShopName;
	/**商家名称 地址，简介*/ 
	private TextView foodsName,foodsAddress,foodsBrif;	
	/**人均  评分等级*/ 
	private TextView foodsPerMoney,foodsGrade;
	/**等级星级数量*/ 
	private RatingBar foodsRb;
	/**商家团购商家详细信息 中的图片资源*/ 
	private static String[] shopImgs;
	/**点击 地址 进行定位*/ 
	private LinearLayout foodsLocation;
	/**打电话*/ 
	private LinearLayout foodsPhone;
	/**电话号码*/ 
	private String telephone;
	private LinearLayout foods_ding_wei;//地图页面
	private FoodShopsModel foodShopModel ;//跳转过来的类
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_shop);
		initView();// 初始化view
		initData();// 初始化数据
//		initViewPager();//初始化viewpager
//		initListView();//初始化listview
	}
	
	/**
	 * 初始化控件view
	 */
	public void initView() {
		mContext=this;
		mLv=(ListView)findViewById(R.id.foods_list);
		mVp = (ViewPager) findViewById(R.id.foods_shop_viewpager);
		dotLayout = (LinearLayout) findViewById(R.id.foods_shop_indictor);
		
		mBack = (ImageView) findViewById(R.id.merchant_desc_title).findViewById(R.id.back);
		mMore = (TextView) findViewById(R.id.merchant_desc_title).findViewById(R.id.share);
		mTitle = (TextView) findViewById(R.id.merchant_desc_title).findViewById(R.id.title);
		mCollect= (CheckBox) findViewById(R.id.merchant_desc_title).findViewById(R.id.collect);
		
		foodsName = (TextView) findViewById(R.id.foods_name);
		foodsAddress = (TextView) findViewById(R.id.foods_address);
		foodsBrif = (TextView) findViewById(R.id.foods_brief);
		foodsPerMoney = (TextView) findViewById(R.id.foods_ren_jun);
		foodsGrade = (TextView) findViewById(R.id.foods_grade);
		foodsRb = (RatingBar) findViewById(R.id.foods_lv);
		foodsLocation = (LinearLayout) findViewById(R.id.foods_ding_wei);
		foodsPhone = (LinearLayout) findViewById(R.id.foods_phone);
		foods_ding_wei= (LinearLayout) findViewById(R.id.foods_ding_wei);
		foods_ding_wei.setOnClickListener(this);
		
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		mBack.setOnClickListener(this);
		mMore.setVisibility(View.VISIBLE);
		mMore.setOnClickListener(this);
		foodsLocation.setOnClickListener(this);
		foodsPhone.setOnClickListener(this);
//		mCollect.setOnClickListener(this);
		mBack.setFocusable(true);
		mBack.setFocusableInTouchMode(true);
		mBack.requestFocus();
		mBack.requestFocusFromTouch();			
		
		Intent intent = getIntent();
		foodShopModel = (FoodShopsModel) intent.getSerializableExtra("FoodShopsModel");
		foodShopID = foodShopModel.getShopID();
		foodShopName = foodShopModel.getShopName();
		foodsName.setText(foodShopName);
		mTitle.setText(foodShopModel.getShopName());
		if(foodShopModel.getPreMoney().equals("")||foodShopModel.getPreMoney()==null){
			foodsPerMoney.setText("￥"+"0.00");
		}else{
			foodsPerMoney.setText("￥"+foodShopModel.getPreMoney());
		}
		
		if (!foodShopModel.getShopGrade().equals("")) {
			foodsRb.setRating(Float.parseFloat(foodShopModel.getShopGrade()));
			foodsGrade.setText(foodShopModel.getShopGrade()+"分");
		}else {
			foodsRb.setRating(0);
			foodsGrade.setText("暂无评分");
		}
		
		if(http == null){
			http = new HttpUtil(handler);
		}
						
		getNetData();
		getCollectStatus();
	}
	
	/**
	 * 获取网络数据
	 */
	private void getNetData(){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			//获取是否收藏
			if (MyApplication.isLogin) {//登录时 才有请求
				http.sendGet(RequestCode.GETISCOLLECTION, WebUrlConfig.getIsCollection(
						MyApplication.loginModel.getUserID(), 
						String.valueOf(4), 
						foodShopID));
			}
			//获得团购商家信息
			http.sendGet(RequestCode.GETFOODSHOPINFO, WebUrlConfig.getFoodShopInfo(foodShopID));
			//获取团购详情信息
			http.sendGet(RequestCode.GETFOODSHOPFOODS, WebUrlConfig.getFoodShopFoods(foodShopID));
		}else{//无法连接网络
			ShowContentUtils.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){								
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
			case HttpUtil.SUCCESS://成功	
				//商家信息 
				if(msg.arg1 == RequestCode.GETFOODSHOPINFO){
					FoodShopInfoModel model = (FoodShopInfoModel) JSONObject.parseObject(msg.obj.toString(),FoodShopInfoModel.class);					
					foodsAddress.setText(model.getAddress());
					foodsBrif.setText(model.getBrief());
					telephone = model.getPhoneNumber();
					shopImgs = model.getShopImg().split(";");
					NUMBER_MAX = shopImgs.length;
					if(NUMBER_MAX==1){
						dotLayout.setVisibility(View.GONE);
					}
					initViewPager();//初始化viewpager
				}
				//团购详情
				if(msg.arg1 == RequestCode.GETFOODSHOPFOODS){
					mList = JSONArray.parseArray(msg.obj.toString(), 
							FoodShopFoodsModel.class);
					initListView();
				}
				//是否已收藏
				if(msg.arg1 == RequestCode.GETISCOLLECTION){
					Log.e("result", msg.obj.toString());
					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {//已收藏
						mCollect.setChecked(true);
					} else {
						mCollect.setChecked(false);
					}
				}
				//收藏商家
				if (msg.arg1 == RequestCode.FOODSHOPCOLLECT) {

					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {
						ShowContentUtils.showLongToastMessage(mContext, "收藏成功");
					} 
				}
				//取消收藏
				if(msg.arg1 == RequestCode.CANCELCOLLECTION){
					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {
						ShowContentUtils.showLongToastMessage(mContext, "取消收藏成功");
					} else {
						ShowContentUtils.showLongToastMessage(mContext,
								model.getErrorMsg());
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.GETFOODSHOPINFO) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				if (msg.arg2 == RequestCode.GETFOODSHOPFOODS) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}
	};		

	/**
	 * 初始化viewpager
	 */
	private void initViewPager() {
		//画点
		MyApplication.drawPoint(mContext,dotLayout,dotList,NUMBER_MAX);
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
						dotList.get(position).setBackgroundResource(R.drawable.point_indictor_unselect);
					} else {
						dotList.get(i).setBackgroundResource(R.drawable.point_indictor_select);
					}
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}
	
	/**
	 * 初始化listview
	 */
	private void initListView() {
		mAdapter = new FoodsListViewAdapter(mContext,mList);
		mLv.setAdapter(mAdapter);
		MyApplication.setListViewHeight(mLv);
		
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(mContext, FoodsImmediatelyBuyActivity.class);
				FoodShopFoodsModel model = mList.get(position);
				Bundle b = new Bundle();
				b.putSerializable("FoodShopFoodsModel", model);
				b.putSerializable("FoodShopsModel", foodShopModel);
				intent.putExtras(b);
				intent.putExtra("foodShopID", foodShopID);
				intent.putExtra("foodShopName", foodShopName);
				startActivity(intent);
			}
		});
	}
	
	
	@Override
	public void onClick(View v) {
		if (v==mBack) {
			onBackPressed();
			finish();
		}
		if(v == mMore){//分享
			UMShareUtil util = new UMShareUtil(mContext);
			if(foodShopModel.getShopImg()==null||foodShopModel.getShopImg().equals("")){
//				ShowContentUtils.showLongToastMessage(mContext, "缺少图片不能分享");
//				return;
				util.shareDrawableImage(foodShopName,foodsBrif.getText().toString(),
						R.drawable.icon, MyApplication.share);
			}else
			if (foodsBrif.getText().toString()!=null) {
				util.shareNetImage(foodShopName,
						foodsBrif.getText().toString(), foodShopModel.getShopImg(),
						MyApplication.share 
						);
			}else {
//				UMShareUtil util = new UMShareUtil(mContext);
				util.shareNetImage(foodShopName,
						RequestCode.NAME, foodShopModel.getShopImg(),
						MyApplication.share 
						);
			}
			
		}
//		if (v==mCollect) {//收藏
//			if (!MyApplication.isLogin) {
//				ShowContentUtils.showShortToastMessage(mContext,"请先登录！");
//				Intent intent = new Intent(mContext,LoginActivity.class);
//			    startActivity(intent);
//			    return;
//			}
//			if (MyApplication.getNetObject().isNetConnected()) {
//				progressDialog = MyProgressDialog.createDialog(this);
//				if (progressDialog != null && !progressDialog.isShowing()) {
//					progressDialog.setMessage("加载中...");
//					progressDialog.show();
//				}
//				http.sendGet(
//						RequestCode.FOODSHOPCOLLECT,
//						WebUrlConfig.addCollectionShops(MyApplication.loginModel.getUserID(),foodShopID,"4"));
//			} else {
//				ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
//			}
//		}
		if (v==foods_ding_wei) {
			Intent intent=new Intent(mContext,MapActivity.class);
			if (foodShopModel.getLat()!=null&&!foodShopModel.getLat().equals("")) {
				intent.putExtra("lat", Double.parseDouble(foodShopModel.getLat()));
				intent.putExtra("lng", Double.parseDouble(foodShopModel.getLng()));
				intent.putExtra("adress", foodShopModel.getShopName());
				startActivity(intent);
			}else {
				ShowContentUtils.showLongToastMessage(mContext, "暂无商家具体位置");
			}
		}
		if(v == foodsPhone){//打电话
			if(telephone != null&&!telephone.equals("")){
				Intent intent = new Intent();
			    intent.setAction("android.intent.action.DIAL");
			    intent.setData(Uri.parse("tel:"+telephone));
			    startActivity(intent);
			}else{
				ShowContentUtils.showLongToastMessage(mContext, "商家暂无电话");
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
			x.image().bind(iv, shopImgs[position],MyApplication.imageOptions);			
			return iv;
		}				
	}

	/**
	 * 点击事件 收藏
	 */
	private void getCollectStatus(){
		mCollect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!MyApplication.isLogin){									
					mCollect.setChecked(false);
					ShowContentUtils.showShortToastMessage(mContext,"请先登录！");
					Intent intent = new Intent(mContext,LoginActivity.class);
				    startActivity(intent);
				    return;
				}
				if(isChecked){//没有收藏 点击就收藏
					if (MyApplication.getNetObject().isNetConnected()) {
						progressDialog = MyProgressDialog.createDialog(mContext);
						if (progressDialog != null && !progressDialog.isShowing()) {
							progressDialog.setMessage("加载中...");
							progressDialog.show();
						}
						http.sendGet(
								RequestCode.FOODSHOPCOLLECT,
								WebUrlConfig.addCollectionShops(MyApplication.loginModel.getUserID(),foodShopID,"4"));
					} else {
						ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
					}
				}else{//已收藏  点击取消收藏
					if (MyApplication.getNetObject().isNetConnected()) {
						progressDialog = MyProgressDialog.createDialog(mContext);
						if (progressDialog != null && !progressDialog.isShowing()) {
							progressDialog.setMessage("加载中...");
							progressDialog.show();
						}
						http.sendGet(
								RequestCode.CANCELCOLLECTION,
								WebUrlConfig.cancelCollection(MyApplication.loginModel.getUserID(),
										String.valueOf(4),foodShopID));
					} else {
						ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
					}
				}
			}
		});
	}
}	

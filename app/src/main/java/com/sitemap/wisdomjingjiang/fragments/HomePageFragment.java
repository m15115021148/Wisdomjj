package com.sitemap.wisdomjingjiang.fragments;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.activities.FoodsImmediatelyBuyActivity;
import com.sitemap.wisdomjingjiang.activities.FoodsShopActivity;
import com.sitemap.wisdomjingjiang.activities.FoodsShopListActivity;
import com.sitemap.wisdomjingjiang.activities.HotleActivity;
import com.sitemap.wisdomjingjiang.activities.JifenActivity;
import com.sitemap.wisdomjingjiang.activities.KTVActivity;
import com.sitemap.wisdomjingjiang.activities.LoginActivity;
import com.sitemap.wisdomjingjiang.activities.MainActivity;
import com.sitemap.wisdomjingjiang.activities.MessageActivity;
import com.sitemap.wisdomjingjiang.activities.NewsActivity;
import com.sitemap.wisdomjingjiang.activities.NewsDescActivity;
import com.sitemap.wisdomjingjiang.activities.SearchActivity;
import com.sitemap.wisdomjingjiang.activities.ShoppingDescActivity;
import com.sitemap.wisdomjingjiang.activities.ShoppingListActivity;
import com.sitemap.wisdomjingjiang.activities.WeatherActivity;
import com.sitemap.wisdomjingjiang.adapters.FoodsShopListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.HomeNewsListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.HomeShareAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.models.GoodsModel;
import com.sitemap.wisdomjingjiang.models.NewsListModel;
import com.sitemap.wisdomjingjiang.models.NewsTypeModel;
import com.sitemap.wisdomjingjiang.models.AreasModel;
import com.sitemap.wisdomjingjiang.models.TopsModel;
import com.sitemap.wisdomjingjiang.models.WeatherModel;
import com.sitemap.wisdomjingjiang.models.WeatherResultModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.ShopsDialog;
import com.sitemap.wisdomjingjiang.views.ShopsDialog.OnClickDialogListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.fragments.Fragment1 首页 页面
 * 
 * @author chenmeng create at 2016年4月27日 上午10:00:54
 */
@ContentView(R.layout.fragment_home_pager)
public class HomePageFragment extends BaseFragment {
	private Context mContext;// 全局类
	@ViewInject(R.id.viewpager)
	private ViewPager mTopsVp;// viewpager
	/**广告轮播 导航圈*/ 
	@ViewInject(R.id.indictor_view)
	private LinearLayout dotLayout;
	@ViewInject(R.id.indictor_grid_view)
	private LinearLayout dotLayout1;// 导航圈
	/**首页刷新*/ 
	@ViewInject(R.id.iv_refresh)
	private ImageView mRefresh;
	/** 广告 viewpager数量 */
	public static int TOPS_MAX_NUMBER = 1;
	/** 广告 轮回播放时间 */
	public static final long GUIDER_TIME_CHANGE = 4000;
	/** 广告hander 的what */
	public static final int GUIDER_WAHT = 1;
	/**轮播图片的点的集合*/ 
	private List<View> dotList = new ArrayList<View>(); 
	/**资讯的点的集合*/ 
	private List<View> dotList1 = new ArrayList<View>(); 
    /**自动轮播启用开关 */ 
    private static boolean isAutoPlay = true; 
	/** 广告 标签 */
	public static final String TAG = "topsModel";
	/** 广告页面中当前选择的页面 */
	private static int currPosition;
	/** 资讯 页面 的 gridview 中的viewpager */
	@ViewInject(R.id.vp_information)
	private ViewPager mVpInf;
	/** 资讯 页面的 gridview list的数量 */
	List<View> listGridView = new ArrayList<View>();
	/** gridview 的个数 也是viewpager的个数,indictor个数 */
	public static int NUMBER_MAX = 2;
	/** gridview 每一行的个数 */
	public static int GRID_VIEW_NUM = 4;
	/** 商圈的 图片点击事件 */
	@ViewInject(R.id.nearby_shopping_img)
	private ImageView mNearbyShopping;
	/** 最新资讯 的listview */
	@ViewInject(R.id.news_listview)
	private ListView mLvNews;
	/** 团购的listview */
	@ViewInject(R.id.foods_listview)
	private ListView mLvFoods;
	/** 搜索框 */
	@ViewInject(R.id.search_content)
	private EditText mSearch;
	/** 头部消息 按钮 */
	@ViewInject(R.id.iv_message)
	private ImageView mMessage;
	/** 天气页面 的显示 */
	@ViewInject(R.id.weather_click)
	private RelativeLayout mWeather;
	/** 签到页面的显示 */
	@ViewInject(R.id.qian_dao)
	private LinearLayout mQianDao;
	/** 最新资讯中的更多 团购推荐中的更多 */
	@ViewInject(R.id.news_more)
	private TextView mNewsMore;
	@ViewInject(R.id.food_more)
	private TextView mFoodsMore;
	@ViewInject(R.id.shopping_more)
	private TextView mShoppingMore;
	/**轮播图片背景*/ 
	@ViewInject(R.id.tops_img_bg)
	private RelativeLayout mTopsBg;
	/**当前温度*/ 
	@ViewInject(R.id.weather_day)
	private TextView mWeatherDay;
	/**范围*/ 
	@ViewInject(R.id.weather_fanwei)
	private TextView mWeatherInfo;
	/**详情*/ 
	@ViewInject(R.id.weather_info)
	private TextView mWeatherDesc;
	/**图片*/ 
	@ViewInject(R.id.weather_img)
	private ImageView mWeatherImg;
	// 获取商圈列表
	@ViewInject(R.id.city_name)
	private TextView city_name;
	private static MyProgressDialog progressDialog;// 进度条

	private MyProgressDialog progress;// 进度条
	private HttpUtil http;// http对象
	// 网络请求顺序
	private final int FIRST = 0;
	private String areaID;// 商圈id
	private ShopsDialog dialog;// 商圈列表dialog

	/** 团购推荐 数据 */
	private List<FoodShopsModel> foodsList = null;
	/** 经度 */
	private String lng = String.valueOf(MyApplication.lng);
	/** 纬度 */
	private String lat = String.valueOf(MyApplication.lat);
	/** 页码 */
	private String page = "0";
	/** 团购列表 适配器 */
	private FoodsShopListViewAdapter foodsAdapter;

	private LocationClient mLocationClient;//百度

	public MyLocationListener myListener = new MyLocationListener();//定位监听
	
	private String tempcoor = "bd09ll";// 数据格式
	
	private int time=60*60*24*1000;//定位间隔
	/**图片轮播 的数据 集合*/ 
	private static String[] topsImg = null;
	/**轮播信息 数据*/ 
	private static List<TopsModel> mTopsModelList = null;
	/**轮播图片适配器*/ 
	private FragmentPagerAdapter mTopsAdapter;
	/**数据*/ 
	private List<WeatherModel> mWeatherList;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		mContext = this.getActivity();
		initLocation();
		initData();// 初始化搜索		
		initInformationData();//初始化 资讯 团购 购物 等数据		
		mLvNews.setFocusable(false);
		mLvFoods.setFocusable(false);
	}
	
	/**
	 * 初始化定位
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		mLocationClient = new LocationClient(this.getActivity());
		mLocationClient.registerLocationListener(myListener);
		option.setOpenGps(true);// 打开gps
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setScanSpan(time);
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}
	
	@Override
	public void onDestroy() {
		mLocationClient.stop();
		super.onDestroy();
	}	
	
	@Override
	public void onResume() {
		super.onResume();
		// 发送消息 切换图片
		handler.sendEmptyMessage(GUIDER_WAHT);
	}

	@Override
	public void onPause() {
		super.onPause();
		// 移除 广告轮播what
		handler.removeMessages(GUIDER_WAHT);	
	}
	
	/**
	 * 初始化 搜索
	 */
	private void initData() {
		// 搜索点击事件
		mSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {// 此处为得到焦点时的处理内容
					Intent intent = new Intent();
					intent.setClass(mContext, SearchActivity.class);
					startActivity(intent);
					mSearch.clearFocus();// 失去焦点
				}
			}
		});
		//刷新
		mRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//判断是否有网络
				if (MyApplication.getNetObject().isNetConnected()) {
					progressDialog = MyProgressDialog.createDialog(mContext);
					if (progressDialog != null && !progressDialog.isShowing()) {
						progressDialog.setMessage("刷新中...");
						progressDialog.show();
					}
					//加载数据			
					getDataAD();// 获取轮播信息列表
					getNewsData();//最新资讯
					getWeatherData();//天气
					initFoodsData();// 初始化 团购商家类别 网络获取
					currPosition=0;
				} else {
					ShowContentUtils.showShortToastMessage(mContext,
							RequestCode.NOLOGIN);
				}
			}
		});
		
		//最新资讯  更多 点击事件
		mNewsMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initNewsType();
			}
		});

		if(http == null){
			http = new HttpUtil(handler);
		}
		
		//判断是否有网络
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this.getActivity());
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			//加载数据			
			getDataAD();// 获取轮播信息列表
			getNewsData();//最新资讯
			getWeatherData();//天气
			initFoodsData();// 初始化 团购商家类别 网络获取
			
		} else {
//			mIndictor.setVisibility(View.GONE);
			ShowContentUtils.showShortToastMessage(mContext,
					RequestCode.NOLOGIN);
		}
		
	}

	/**
	 * 新闻类型初始化
	 */
	private void initNewsType() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this.getActivity());
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			http.sendGet(RequestCode.GETNEWSTYPE, WebUrlConfig.getNewsType());
		} else {
			ShowContentUtils.showShortToastMessage(mContext,
					RequestCode.NOLOGIN);
		}
	}

	/**
	 * 团购推荐
	 */
	private void initFoodsData() {
		String foodRecommend = WebUrlConfig.getFoodRecommend();
		http.sendGet(RequestCode.GETFOODRECOMMEND, foodRecommend);		
	}

	/**
	 * 获取轮播信息列表
	 */
	private void getDataAD() {
		http.sendGet(RequestCode.GETTOPS, WebUrlConfig.getTops());		
	}
	
	/**
	 * 最新资讯
	 */
	private void getNewsData(){
		String recentNews = WebUrlConfig.getRecentNews();
		http.sendGet(RequestCode.GETRECENTNEWS, recentNews);		
	}

	/**
	 * 获取天气
	 */
	private void getWeatherData() {
		String weather = WebUrlConfig.getWeather();
		http.sendGet(RequestCode.WEATHER, weather);
	}
	
	
	/**
	 * 初始化广告 资讯 viewpager
	 * 轮播图片 
	 */
	private void initViewPagerData(List<TopsModel> list) {
		topsImg = new String[list.size()];
		for(int i=0;i<list.size();i++){
			topsImg[i] = list.get(i).getTopImg();
		}
		TOPS_MAX_NUMBER = topsImg.length;
		MyApplication.drawPoint(mContext,dotLayout,dotList,TOPS_MAX_NUMBER);		
		mTopsAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
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
				return TOPS_MAX_NUMBER;
			}
			
		};
		mTopsVp.setAdapter(mTopsAdapter);
		// 设置viewpager滑动事件
		mTopsVp.setOnPageChangeListener(new OnPageChangeListener() {
	
				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
				}
	
				@Override
				public void onPageSelected(int position) {
					currPosition = position;
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
					switch (state) {
					case 1:// 手势滑动，空闲中
						isAutoPlay = false;
						break;
					case 2:// 界面切换中
						isAutoPlay = true;
						break;
					case 0:// 滑动结束，即切换完毕或者加载完毕
							// 当前为最后一张，此时从右向左滑，则切换到第一张
						if (mTopsVp.getCurrentItem() == mTopsVp.getAdapter().getCount() - 1
								&& !isAutoPlay) {
							mTopsVp.setCurrentItem(0);
						}// 当前为第一张，此时从左向右滑，则切换到最后一张  
		                else if (mTopsVp.getCurrentItem() == 0 && !isAutoPlay) {  
		                	mTopsVp.setCurrentItem(mTopsVp.getAdapter().getCount() - 1);  
		                }
						break;
					}
				}
			});		
	}

	/**
	 * 初始化 资讯 团购 购物 等数据
	 */
	private void initInformationData(){
		// ------------------------资讯-------------------------		
		listGridView.clear();
		listGridView.add(getShareGridView(1));
		listGridView.add(getShareGridView(2));
		MyApplication.drawPoint(mContext,dotLayout1,dotList1, listGridView.size());						
		mVpInf.setAdapter(new PagerAdapter() {
			@Override
			public int getCount() {
				return NUMBER_MAX;// 资讯强制显示一页
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(listGridView.get(position));
				return listGridView.get(position);
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(listGridView.get(position));
			}
		});
		mVpInf.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < dotList1.size(); i++) {
					if (i == position) {
						dotList1.get(position).setBackgroundResource(R.drawable.point_indictor_unselect);
					} else {
						dotList1.get(i).setBackgroundResource(R.drawable.point_indictor_select);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {				
			}
		});
		// 点击事件
		GridView gv = (GridView) listGridView.get(0);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				switch (position) {
				case 0:// 资讯
					initNewsType();
					break;
				case 1:// 美食(团购)
					if (dialog != null) {
						areaID = dialog.getAreaID();// 获取商圈id
					}
					intent.setClass(mContext, FoodsShopListActivity.class);
					startActivity(intent);
					break;
				case 4:// 购物
					MainActivity.mContext.setCheck();
					MyApplication.mViewPager.setCurrentItem(1);
					break;
				case 2:// 酒店
					intent.setClass(mContext, HotleActivity.class);
					startActivity(intent);
					break;
				case 3:// ktv
					intent.setClass(mContext, KTVActivity.class);
					startActivity(intent);
					break;
				case 7://新靖江论坛
					intent.setClass(getActivity(), NewsDescActivity.class);
					intent.putExtra("jjUrl", "http://www.xjjbbs.com/forum.php");
					intent.putExtra("img", "");//广告图片
					intent.putExtra("title", "新靖江论坛");//广告标题
					intent.putExtra("type", 2);//广告标致
					startActivity(intent);
					break;
				default:
					ShowContentUtils.showShortToastMessage(mContext, "开发中");
					break;
				}
			}
		});
		// 点击事件
		GridView gv1 = (GridView) listGridView.get(1);
		gv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ShowContentUtils.showLongToastMessage(mContext, "开发中");
			}
		});
	}

	/**
	 * 资讯 页面中的 gridview
	 * 
	 * @return
	 */
	private View getShareGridView(int type) {
		GridView gridView = new GridView(mContext);
		gridView.setNumColumns(GRID_VIEW_NUM);
		if(type==1){//第一页
			gridView.setAdapter(new HomeShareAdapter(mContext,type));
		}else{//第二页
			gridView.setAdapter(new HomeShareAdapter(mContext,type));
		}
		return gridView;
	}

	// -----------------------------最新资讯----------------------------------

	/**
	 * 初始资讯 数据
	 */
	private void initListViewNewsData(final List<NewsListModel> list) {
		mLvNews.setAdapter(new HomeNewsListViewAdapter(mContext,list));
		MyApplication.setListViewHeight(mLvNews);
		// 点击事件
		mLvNews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(mContext, NewsDescActivity.class);
				NewsListModel model = list.get(position);
				intent.putExtra("newsUrl", model.getNewsUrl());
				intent.putExtra("img", model.getNewsImg());//图片
				intent.putExtra("title",model.getNewsTitle());//标题
				//本地缓存
				MyApplication.db.insert(model.getNewsID());
				TextView title = (TextView) view.findViewById(R.id.news_title);
				title.setTextColor(mContext.getResources().getColor(R.color.texthui));
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化天气
	 */
	private void initWeatherData(){
		if(mWeatherList != null){
			for (int i = 0; i < mWeatherList.size(); i++) {
				WeatherModel model = mWeatherList.get(i);
				if(i==0){
					int low = Integer.parseInt(model.getTemp_low());
					int high= Integer.parseInt(model.getTemp_high());
					mWeatherDay.setText((low+high)/2+"℃");
					mWeatherDesc.setText(model.getWeather());
					x.image().bind(mWeatherImg, model.getWeather_icon(),MyApplication.imageOptionsZ);
					mWeatherInfo.setText(model.getTemperature());
				}
			}
		}
	}
	
	// ------------------------------团购-----------------------------------------

	/**
	 * 初始化 团购推荐
	 */
	private void initListViewFoodsData() {
		Log.i("TAG","lng:"+MyApplication.lng+"，LAT:"+MyApplication.lat);
		foodsAdapter = new FoodsShopListViewAdapter(mContext, foodsList, 
				String.valueOf(MyApplication.lng),
				String.valueOf(MyApplication.lat));
		mLvFoods.setAdapter(foodsAdapter);
		MyApplication.setListViewHeight(mLvFoods);
		// 点击事件
		mLvFoods.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, FoodsShopActivity.class);
				// 将商家id 传过去
				FoodShopsModel model = foodsList.get(position);
				Bundle b = new Bundle();
				b.putSerializable("FoodShopsModel", model);
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}

	// ------------------------ScrollView--------------------------------------------

	/**
	 * 广告页面中的子布局
	 */
	public static class MyFragment extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ImageView iv = new ImageView(inflater.getContext());
			int pos = getArguments().getInt(TAG);		
//			iv.setScaleType(ScaleType.FIT_XY);
			x.image().bind(iv, topsImg[pos],MyApplication.imageOptions);			
			iv.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {					
					Intent intent = new Intent();
					switch (Integer.parseInt(mTopsModelList.get(currPosition).getTopType())) {
					case 1://新闻
						intent.setClass(getActivity(), NewsDescActivity.class);
						intent.putExtra("img",mTopsModelList.get(currPosition).getTopImg());//图片
						intent.putExtra("title",mTopsModelList.get(currPosition).getName());//标题
						intent.putExtra("newsUrl", mTopsModelList.get(currPosition).getNewsUrl());						
						startActivity(intent);
						break;
					case 2://团购商家
						FoodShopsModel model = new FoodShopsModel();	
						model.setArea("");
						model.setLat(mTopsModelList.get(currPosition).getLat());
						model.setLng(mTopsModelList.get(currPosition).getLng());
						model.setPreMoney(mTopsModelList.get(currPosition).getPreMoney());
						model.setShopGrade(mTopsModelList.get(currPosition).getGrade());
						model.setShopID(mTopsModelList.get(currPosition).getTopID());
						model.setShopImg(mTopsModelList.get(currPosition).getTopImg());
						model.setShopName(mTopsModelList.get(currPosition).getName());
						model.setShopType(mTopsModelList.get(currPosition).getTopType());
						intent.setClass(getActivity(), FoodsShopActivity.class);						
						Bundle b = new Bundle();
						b.putSerializable("FoodShopsModel", model);
						intent.putExtras(b);
						startActivity(intent);
						break;
					case 3://购物商家
						intent.setClass(getActivity(), ShoppingListActivity.class);
						intent.putExtra("shopID", mTopsModelList.get(currPosition).getTopID());
						intent.putExtra("shopName", mTopsModelList.get(currPosition).getName());
						intent.putExtra("isColl", true);
						startActivity(intent);
						break;
					case 4://购物商品
						intent.setClass(getActivity(), ShoppingDescActivity.class);
						Bundle b2 = new Bundle();
						GoodsModel mGood=new GoodsModel();
						mGood.setGoodsID(mTopsModelList.get(currPosition).getTopID());
						mGood.setGoodsImg(mTopsModelList.get(currPosition).getTopImg());
						mGood.setGoodsName(mTopsModelList.get(currPosition).getName());
						mGood.setGoodsPlace("");
						mGood.setGoodsPrice(mTopsModelList.get(currPosition).getNowPrice());
						mGood.setSales(mTopsModelList.get(currPosition).getSales());
						b2.putSerializable("GoodsModel", mGood);
						intent.putExtras(b2);
						intent.putExtra("shopID", mTopsModelList.get(currPosition).getShopID());
						startActivity(intent);
						break;
					case 5://团购商品
						intent.setClass(getActivity(), FoodsImmediatelyBuyActivity.class);
						FoodShopFoodsModel model3 = new FoodShopFoodsModel();
						model3.setFoodID(mTopsModelList.get(currPosition).getTopID());
						model3.setFoodImg(mTopsModelList.get(currPosition).getTopImg());
						model3.setFoodName(mTopsModelList.get(currPosition).getName());
						model3.setNowPrice(mTopsModelList.get(currPosition).getNowPrice());
						model3.setOldPrice(mTopsModelList.get(currPosition).getOldPrice());
						model3.setSales(mTopsModelList.get(currPosition).getSales());
						
						Bundle b3 = new Bundle();
						b3.putSerializable("FoodShopsModel", getFoodsModelData());
						b3.putSerializable("FoodShopFoodsModel", model3);
						intent.putExtras(b3);
						intent.putExtra("foodShopID", mTopsModelList.get(currPosition).getShopID());
						intent.putExtra("foodShopName", mTopsModelList.get(currPosition).getShopName());
						startActivity(intent);
						break;	
					case 6:
						intent.setClass(getActivity(), NewsDescActivity.class);
						intent.putExtra("adUrl", mTopsModelList.get(currPosition).getNewsUrl());
						intent.putExtra("img", mTopsModelList.get(currPosition).getTopImg());//广告图片
						intent.putExtra("title", mTopsModelList.get(currPosition).getName());//广告标题
						intent.putExtra("type", 1);//广告标致
						startActivity(intent);
						break;
					default:
						break;
					}
				}
			});			
			return iv;
		}
	}
	
	/**
	 * 获取团购商家 信息
	 * @return
	 */
	private static FoodShopsModel getFoodsModelData(){
		//将商家id 传过去
		FoodShopsModel model = new FoodShopsModel();	
		model.setArea("");
		model.setLat(mTopsModelList.get(currPosition).getLat());
		model.setLng(mTopsModelList.get(currPosition).getLng());
		model.setPreMoney(mTopsModelList.get(currPosition).getPreMoney());
		model.setShopGrade(mTopsModelList.get(currPosition).getGrade());
		model.setShopID(mTopsModelList.get(currPosition).getShopID());
		model.setShopImg(mTopsModelList.get(currPosition).getTopImg());
		model.setShopName(mTopsModelList.get(currPosition).getName());
		model.setShopType(mTopsModelList.get(currPosition).getTopType());
		return model;
	}

	// ------------------------handler-----------------------------

	/** handler 全局变量 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();// 关闭进度条
			}
			// 关闭进度条
			if (progress != null && progress.isShowing()) {
				progress.dismiss();
			}
			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				// 轮播信息列表
				if (msg.arg1 == RequestCode.GETTOPS) {
					mTopsModelList = JSONObject.parseArray(msg.obj.toString(), TopsModel.class);					
					initViewPagerData(mTopsModelList);					
					if(TOPS_MAX_NUMBER==1){
						dotList.get(0).setVisibility(View.GONE);
					}
				}				
				//资讯
				if (msg.arg1 == RequestCode.GETNEWSTYPE) {
//					Log.i("TAG", "咨询:"+msg.obj.toString());
					MyApplication.lnewsTypeModel = JSONObject.parseArray(
							msg.obj.toString(), NewsTypeModel.class);
					Intent intent = new Intent();
					intent.setClass(mContext, NewsActivity.class);
					startActivity(intent);
				}
				if (msg.arg1 == FIRST) {
//					Log.i("TAG", "商圈:"+msg.obj.toString());
					final List<AreasModel> list = JSON.parseArray(msg.obj.toString(),
							AreasModel.class);
					dialog = new ShopsDialog(mContext, list);
					dialog.setOnClickDialogListener(new OnClickDialogListener() {
						
						@Override
						public void setOnDialogClick(int position) {
							String name=list.get(position).getAreaName();
							if(name.equals("全城")){
								city_name.setText("靖江");
							}else{
								city_name.setText(name);
							}
						}

						@Override
						public void setOnDialogCityNameClick() {
							city_name.setText("靖江");
						}
					});
					dialog.show();
				}
				//最新资讯
				if(msg.arg1 == RequestCode.GETRECENTNEWS){
//					Log.i("TAG", "最新资讯:"+msg.obj.toString());
					List<NewsListModel> list = JSONObject.parseArray(msg.obj.toString(),
							NewsListModel.class);
//					if(list.size()==0){
//						
//					}else{
						initListViewNewsData(list);	
//					}
			
				}
				// 团购推荐
				if (msg.arg1 == RequestCode.GETFOODRECOMMEND) {
//					Log.i("TAG", "团购推荐:"+msg.obj.toString());
					foodsList = JSONArray.parseArray(msg.obj.toString(),
							FoodShopsModel.class);
//					if(foodsList.size()==0){
//						
//					}else{
					
						initListViewFoodsData();
//					}
				
				}
				//获取天气
				if (msg.arg1 == RequestCode.WEATHER) {
//					Log.i("TAG", "天气:"+msg.obj.toString());
					WeatherResultModel result = JSONObject.parseObject(msg.obj.toString(),WeatherResultModel.class);
					mWeatherList = JSONObject.parseArray(result.getResult(), WeatherModel.class);
					initWeatherData();
				}
				break;
			case HttpUtil.FAILURE:// 失败	
				if (msg.arg1 == RequestCode.WEATHER) {
					return;
				}
				if(msg.arg1 == RequestCode.GETTOPS){
					dotLayout.setVisibility(View.GONE);
					mTopsBg.setBackgroundResource(R.drawable.tops_bg);
				}				
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				progressDialog.dismiss();
				
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.LOGIN) {
				}
				break;
			case GUIDER_WAHT:// 轮播图片
				mTopsVp.setCurrentItem(currPosition++);
				if (currPosition == TOPS_MAX_NUMBER) {
					currPosition = 0;
				}
				handler.sendEmptyMessageDelayed(GUIDER_WAHT, GUIDER_TIME_CHANGE);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 控件的点击事件
	 * 
	 * @param v
	 */
	@Event(value = { R.id.nearby_shopping_img, R.id.iv_message,
			R.id.shopping_more, R.id.weather_click, R.id.qian_dao,
			R.id.news_more, R.id.food_more, R.id.city_name }, type = View.OnClickListener.class)
	private void viewClick(View v) {
		if (v == city_name) {// 商圈
			if(true){
				return;
			}
			if (!MyApplication.getNetObject().isNetConnected()) {
				ShowContentUtils.showShortToastMessage(mContext,
						RequestCode.NOLOGIN);
				return;
			}
			if (progress == null) {
				progress = MyProgressDialog.createDialog(mContext);
			}
			progress.setMessage("加载中...");
			progress.show();
			if (http == null) {
				http = new HttpUtil(handler);
			}
			http.sendCache(FIRST, WebUrlConfig.getAreas());
			return;
		}
		Intent intent = new Intent();
		if (v == mMessage) {// 消息 点击事件
			intent.setClass(mContext, MessageActivity.class);
		}
		if (v == mWeather) {// 天气
			intent.setClass(mContext, WeatherActivity.class);
			intent.putExtra("weather", JSON.toJSONString(mWeatherList));
		}
		if (v == mQianDao) {// 签到
			if(!MyApplication.isLogin){
				ShowContentUtils.showShortToastMessage(mContext, "您还没有登录！");
				Intent in =new Intent(mContext,LoginActivity.class);
				startActivity(in);
				return;
			}
			intent.setClass(mContext, JifenActivity.class);
		}
		if (v == mFoodsMore) {// 团购 更多
			intent.setClass(mContext, FoodsShopListActivity.class);
		}
		if (v == mShoppingMore) {// 附近商圈更多
			intent.setClass(mContext, FoodsShopListActivity.class);
		}
		if (v == mNearbyShopping) {// 附近商圈 图片点击
			intent.setClass(mContext, FoodsShopListActivity.class);
		}
//		if (v == mNewsMore) {// 新闻 更多
//			intent.setClass(mContext, NewsActivity.class);
////			initNewsType();
//		}
		startActivity(intent);
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// 设置自定义图标
			lat = location.getLatitude()+"";
			lng = location.getLongitude()+"";
			MyApplication.lat = location.getLatitude();
			MyApplication.lng = location.getLongitude();
			Log.i("TAG", lat + "--=====--"+lng);
			
//			MyApplication.city = location.getCity();
			System.out.println("jinrulis");
			// System.out.println(lat+"");
			// System.out.println(lng+"");
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// 单位度
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			}
			
			if (location.getLatitude()==0||location.getLongitude()==0) {
				time=30*1000;
				mLocationClient.requestLocation();
			}else {
				time=60*60*24*1000;
				
			}
		}

	}

}

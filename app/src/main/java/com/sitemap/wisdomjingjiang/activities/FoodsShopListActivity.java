package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.FoodsShopListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout.OnRefreshListener;

/**
 * com.sitemap.wisdomjingjiang.activities.FoodsActivity
 * @author zhang
 * create at 2016年5月12日 上午9:13:57
 * 团购商家列表
 */

public class FoodsShopListActivity extends BaseActivity implements OnClickListener{
	/**团购商家列表*/ 
	private ListView mLv;
	private FoodsShopListViewAdapter adapter;
	/**本类*/ 
	private FoodsShopListActivity mContext;
	/**返回上一层 搜索*/ 
	private ImageView mBack;
	/**标题栏 标题*/ 
	private TextView mTitle; 
	/**搜索*/ 
	private EditText mSearch;
	/**网络请求*/ 
	private HttpUtil http;
	/**进度条*/ 
	private static MyProgressDialog progressDialog;
	/**经度*/ 
	private String lng = String.valueOf(MyApplication.lng);
	/**纬度*/ 
	private String lat = String.valueOf(MyApplication.lat);
	/**页码*/ 
	private int page = 0;
	/**数据 第一次数据*/ 
	private List<FoodShopsModel> mList = new ArrayList<FoodShopsModel>();
	/**加载刷新*/ 
	private PullToRefreshLayout mPtrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_shop_list);
		initView();// 初始化view
		initData();// 初始化数据
//		initListView();//初始化listview
	}
	
	/**
	 * 初始化控件view
	 */
	public void initView() {
		mContext=this;
		mLv=(ListView)findViewById(R.id.foods_shop_list);
		mBack = (ImageView) findViewById(R.id.iv_back);
		mTitle = (TextView) findViewById(R.id.title);
		mPtrl = (PullToRefreshLayout)findViewById(R.id.refresh_view);
		mSearch = (EditText) findViewById(R.id.search_content);
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		mBack.setOnClickListener(this);
		mTitle.setText("团购");
		
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
		
		http = new HttpUtil(handler);		
		getDataFromServe();	//从服务器上获取数据列表
		
		/**下拉加载  上拉刷新*/
		mPtrl.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				
				if (MyApplication.getNetObject().isNetConnected()) {
					page+=1;
					String foodsList = WebUrlConfig.getFoodShops(lng, lat, String.valueOf(0));
					http.sendGet(RequestCode.GETFOODSHOPS, foodsList);
				} else {	
					mPtrl.refreshFinish(PullToRefreshLayout.FAIL);
					ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
				}
			}
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				if (MyApplication.getNetObject().isNetConnected()) {
					String foodsList = WebUrlConfig.getFoodShops(lng, lat, String.valueOf(page));
					http.sendGet(RequestCode.GETFOODSHOPFOODSMORE, foodsList);
				} else {
					mPtrl.loadmoreFinish(PullToRefreshLayout.FAIL);
					ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
				}
			}
		});
	}
	
	/**
	 * 初始化listview
	 */
	private void initListView(List<FoodShopsModel> list) {
		adapter=new FoodsShopListViewAdapter(mContext,list,lng,lat);
		mLv.setAdapter(adapter);
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext,FoodsShopActivity.class);
				//将商家id 传过去
				FoodShopsModel model = mList.get(position);				
				Bundle b = new Bundle();
				b.putSerializable("FoodShopsModel", model);
				intent.putExtras(b);
				startActivity(intent);
			}
		});	
	}
	
	/**
	 * 从服务器上获取数据列表
	 */
	private void getDataFromServe() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String foodsList = WebUrlConfig.getFoodShops(lng, lat, String.valueOf(page));
			http.sendGet(RequestCode.GETFOODSHOPS, foodsList);
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
			super.handleMessage(msg);
			switch (msg.what) {
			case HttpUtil.SUCCESS://成功		
				//第一次加载 和刷新
				if(msg.arg1 == RequestCode.GETFOODSHOPS){
					page = 0;
					mList.clear();
					mList = JSONArray.parseArray(msg.obj.toString(),
							FoodShopsModel.class);
					initListView(mList);
//					adapter.notifyDataSetChanged();
					//刷新成功
					page +=1;
//					adapter.notifyDataSetChanged();
					mPtrl.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
				//加载更多
				if(msg.arg1 == RequestCode.GETFOODSHOPFOODSMORE){
//					page += 1;
					List<FoodShopsModel> foodMoreList = JSONArray.parseArray(msg.obj.toString(),
							FoodShopsModel.class);
//					foodMoreList.clear();
					for(int i=0;i<foodMoreList.size();i++){
						mList.add(foodMoreList.get(i));
					}
//					initListView(mList);
					adapter.notifyDataSetChanged();
					mPtrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				break;
			case HttpUtil.FAILURE:// 失败
				// 千万别忘了告诉控件加载完毕了哦！
				mPtrl.loadmoreFinish(PullToRefreshLayout.FAIL);
				ShowContentUtils.showLongToastMessage(mContext,RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.GETFOODSHOPS) {
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
		if(v == mBack){
			onBackPressed();
			finish();
		}
		if(v == mSearch){//搜索
			
		}
	}
}

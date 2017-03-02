package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import u.aly.cm;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.ShoppingListRecyclerViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.ShoppingListRecyclerViewAdapter.OnClickItemsListener;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.GoodsModel;
import com.sitemap.wisdomjingjiang.share.UMShareUtil;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout.OnRefreshListener;
import com.sitemap.wisdomjingjiang.views.PullableRecyclerView;
import com.sitemap.wisdomjingjiang.views.SpacesItemDecoration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author chenmeng 商品列表页面
 */
@ContentView(R.layout.activity_shopping_list)
public class ShoppingListActivity extends BaseActivity implements
		OnClickItemsListener, OnRefreshListener,OnClickListener,OnCheckedChangeListener {
	/** 全局类 */
	private Context mContext;
	/** 商品列表 的 recycleview */
	@ViewInject(R.id.shopping_recyclerview)
	private RecyclerView mRv;
	/** 返回上一层 */
	@ViewInject(R.id.back)
	private ImageView mBack;
	/** 商品 标题栏的 名称 */
	@ViewInject(R.id.title)
	private TextView mTitleName;
	@ViewInject(R.id.refresh_view)
	private PullToRefreshLayout ptrl;// 加载刷新

	private MyProgressDialog progress;// 进度条
	private HttpUtil http;// http对象
	private int pageNumber = 0;// 页码
	private List<GoodsModel> mGoodsMoreList = new ArrayList<GoodsModel>();// 商品列表
	private ShoppingListRecyclerViewAdapter adapter = null;// 商品adapter
	private String typeID ;//商品类别id
	private String shopID ;//商家id
	/**点击收藏*/ 
	private CheckBox mCollect;
	private TextView share;//分享
	/**商品数据*/ 
	private List<GoodsModel> mGoodsList;
	private String shopName;//店铺名称
	/**屏幕的宽度*/ 
	private int mWidth;
	/**屏幕的高度*/ 
	private int mHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;	
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mWidth = wm.getDefaultDisplay().getWidth();
		mHeight = wm.getDefaultDisplay().getHeight();		
		initData();// 初始化 数据
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 关闭进度条
			if (progress != null && progress.isShowing()) {
				progress.dismiss();
			}
			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				//第一次加载
				if (msg.arg1 == RequestCode.GOODSLIST) {
					mGoodsList = JSON.parseArray(msg.obj.toString(), GoodsModel.class);
					initRecyclerView(mGoodsList);// 初始化 recyclerview							
					adapter.notifyDataSetChanged();;//刷新
				}
				//加载更多
				if(msg.arg1 == RequestCode.GOODSLISTMORE){
					mGoodsMoreList.clear();
					mGoodsMoreList = JSON.parseArray(msg.obj.toString(), GoodsModel.class);
					for(int i=0;i<mGoodsMoreList.size();i++){
						mGoodsList.add(mGoodsMoreList.get(i));
					}
					ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					adapter.changeData(mGoodsList);
					adapter.notifyDataSetChanged();//刷新
				}
				//是否已收藏
				if(msg.arg1 == RequestCode.GETISCOLLECTION){
					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {//已收藏
						mCollect.setChecked(true);
					} else {
						mCollect.setChecked(false);
					}
				}
				//收藏成功
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
				if(msg.arg1 == RequestCode.GOODSLIST || msg.arg1 == RequestCode.GOODSLISTMORE){
					ptrl.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
				ShowContentUtils.showShortToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			default:
				break;
			}
		}

	};
	

	/**
	 * 初始化 数据
	 */
	private void initData() {
		if (http == null) {
			http = new HttpUtil(handler);
		}
//		mCollect.setOnClickListener(this);
		Intent intent = getIntent();
		mCollect = (CheckBox) findViewById(R.id.shopping_title).findViewById(R.id.collect);
		share= (TextView) findViewById(R.id.shopping_title).findViewById(R.id.share);
		if (intent.getBooleanExtra("isColl", false)) {
			share.setOnClickListener(this);
//			mCollect.setOnClickListener(this);
			mCollect.setOnCheckedChangeListener(this);
		}else {
			mCollect.setVisibility(View.GONE);
			share.setVisibility(View.GONE);
		}
		String titleName = intent.getStringExtra("typeName");
		if(TextUtils.isEmpty(titleName)){
			 shopID = intent.getStringExtra("shopID");
			shopName = intent.getStringExtra("shopName");
			mTitleName.setText(shopName);
		}else{
			mTitleName.setText(titleName);
			typeID = intent.getStringExtra("typeID");
		}		
		ptrl.setOnRefreshListener(this); 
		loadNet();
	}
	
	/**
	 * 请求网络数据
	 * @param pageNumber
	 */
	private void loadNet(){		
		if (MyApplication.getNetObject().isNetConnected()) {
			progress = MyProgressDialog.createDialog(this);
			if (progress != null && !progress.isShowing()) {
				progress.setMessage("加载中...");
				progress.show();
			}			
			if(TextUtils.isEmpty(typeID)){
				//获取是否收藏
				if (MyApplication.isLogin) {//登录时 才有请求
					http.sendGet(RequestCode.GETISCOLLECTION, WebUrlConfig.getIsCollection(
							MyApplication.loginModel.getUserID(), 
							String.valueOf(3), 
							shopID));
				}
				//店铺信息列表
				http.sendGet(RequestCode.GOODSLIST,
						WebUrlConfig.getGoodShopInfo(shopID,String.valueOf(pageNumber)));			
			}else{
				http.sendGet(RequestCode.GOODSLIST,
						WebUrlConfig.getGoods(typeID,String.valueOf(pageNumber)));
			}	
		}else{//无法连接网络
			ShowContentUtils.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}			
	}

	@Event(value = {R.id.back,R.id.share}, type = View.OnClickListener.class)
	private void viewClick(View v) {
		if (v == mBack) {
			onBackPressed();
			finish();
		}
		if (v==share) {
//			if(imgs==null){
//				ShowContentUtils.showLongToastMessage(mContext, "缺少图片不能分享");
//				return;
//			}
			UMShareUtil util = new UMShareUtil(this);
			util.shareDrawableImage(shopName, RequestCode.NAME, R.drawable.icon, MyApplication.share);
		}
	}

	/**
	 * 初始化 recyclerview
	 */
	private void initRecyclerView(List<GoodsModel> mList) {
		//如果确定每个item的内容不会改变RecyclerView的大小，设置这个选项可以提高性能
		mRv.setHasFixedSize(true);
		mRv.setItemAnimator(new DefaultItemAnimator());
		// 设置瀑布流形式 布局
		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL);
				
		mRv.setLayoutManager(llm);
		adapter = new ShoppingListRecyclerViewAdapter(mContext, mList);
		mRv.setAdapter(adapter);
		// 设置item之间的间隔
		SpacesItemDecoration decoration = new SpacesItemDecoration(mWidth/7,mList.size());
		mRv.addItemDecoration(decoration);
		// 点击事件
		adapter.setOnClickItemsListener(this);
	}

	/**
	 * 页面点击跳转
	 */
	@Override
	public void setOnClick(int position) {
		if(mGoodsList!=null){					
			Intent intent = new Intent(mContext, ShoppingDescActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("GoodsModel", mGoodsList.get(position));
			intent.putExtras(b);
			intent.putExtra("shopID", shopID);
			startActivityForResult(intent, RequestCode.GOODSLISTMORE);
		}

	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		
	}

	/**
	 * 上拉加载更多
	 */
	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		pageNumber++;
		if (MyApplication.getNetObject().isNetConnected()) {
			progress = MyProgressDialog.createDialog(this);
			if (progress != null && !progress.isShowing()) {
				progress.setMessage("加载中...");
				progress.show(); 
			}			
			if(TextUtils.isEmpty(typeID)){
				http.sendGet(RequestCode.GOODSLISTMORE,
						WebUrlConfig.getGoodShopInfo(shopID,String.valueOf(pageNumber)));			
			}else{
				http.sendGet(RequestCode.GOODSLISTMORE,
						WebUrlConfig.getGoods(typeID,String.valueOf(pageNumber)));
			}
		}else{//无法连接网络
			ptrl.refreshFinish(PullToRefreshLayout.FAIL);
			ShowContentUtils.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	@Override
	public void onClick(View v) {
//		if (v==mCollect) {//收藏
//			if (!MyApplication.isLogin) {
//				ShowContentUtils.showShortToastMessage(mContext,"请先登录！");
//				Intent intent = new Intent(mContext,LoginActivity.class);
//			    startActivity(intent);
//			    return;
//			}
//			if (MyApplication.getNetObject().isNetConnected()) {
//				http.sendGet(
//						RequestCode.SHOPCOLLECT,
//						WebUrlConfig.addCollectionShops(MyApplication.loginModel.getUserID(),shopID,"3"));
//			} else {
//				ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
//			}
//		}
		if (v==share) {
			UMShareUtil util = new UMShareUtil(this);
			util.shareDrawableImage(shopName, RequestCode.NAME, R.drawable.icon, MyApplication.share);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (resultCode) {
//		case RequestCode.GOODSLISTMORE:
//			adapter.notifyDataSetChanged();;//刷新
//			break;
//		default:
//			break;
//		}
		adapter.notifyDataSetChanged();;//刷新
	}

	/**
	 * 	是否收藏
	 */
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
				progress = MyProgressDialog.createDialog(mContext);
				if (progress != null && !progress.isShowing()) {
					progress.setMessage("加载中...");
					progress.show();
				}
				http.sendGet(
						RequestCode.FOODSHOPCOLLECT,
						WebUrlConfig.addCollectionShops(MyApplication.loginModel.getUserID(),shopID,"3"));
			} else {
				ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
			}
		}else{//已收藏  点击取消收藏
			if (MyApplication.getNetObject().isNetConnected()) {
				progress = MyProgressDialog.createDialog(mContext);
				if (progress != null && !progress.isShowing()) {
					progress.setMessage("加载中...");
					progress.show();
				}
				http.sendGet(
						RequestCode.CANCELCOLLECTION,
						WebUrlConfig.cancelCollection(MyApplication.loginModel.getUserID(),
								String.valueOf(3),shopID));
			} else {
				ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
			}
		}		
	}
}

package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.HotleListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.KTVListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout.OnRefreshListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
 *  ktv页面 
 */
public class KTVActivity extends BaseActivity implements OnClickListener{
	/**全局类*/ 
	private Context mContext;
	/**返回上一层*/ 
	private ImageView mBack;
	/**标题栏 标题*/ 
	private TextView mTitle;
	/**酒店 listview*/ 
	private ListView mLv;
	private HttpUtil http;// 网络请求
	private static MyProgressDialog progressDialog;// 进度条
	private PullToRefreshLayout ptrl;//加载刷新
	private int page=0;//页数
	private HotleListViewAdapter hotleListAdapter;//列表适配器
	private List<FoodShopsModel> lhotelList=new ArrayList<FoodShopsModel>();//显示的列表
	private List<FoodShopsModel> lhotelListMore=new ArrayList<FoodShopsModel>();//加载更多回来的列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ktv);
		mContext = this;
		initView();//初始化view
		initData();//初始化数据
		initListView();//初始化listview
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
				if (msg.arg1 == RequestCode.GETHOTELLIST) {//第一次和刷新成功
					page=0;
					Log.i("TAG", "request:" + msg.obj.toString());
					lhotelList.clear();
					lhotelList=JSONObject.parseArray(msg.obj.toString(),
							FoodShopsModel.class);
					if(lhotelList.size()==0){
						ShowContentUtils.showLongToastMessage(mContext, "暂无数据");
					}
					hotleListAdapter=new HotleListViewAdapter(mContext,lhotelList);
					mLv.setAdapter(hotleListAdapter);
					// 千万别忘了告诉控件刷新完毕了哦！
					page+=1;
					ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
				if (msg.arg1 == RequestCode.GETHOTELLISTMORE) {//加载更多
					page+=1;
					Log.i("TAG", "request:" + msg.obj.toString());
					lhotelListMore.clear();
					lhotelListMore=JSONObject.parseArray(msg.obj.toString(),
							FoodShopsModel.class);
					for (int i = 0; i <lhotelListMore.size(); i++) {
						lhotelList.add(lhotelListMore.get(i));
					}
					hotleListAdapter=new HotleListViewAdapter(mContext,lhotelList);
					mLv.setAdapter(hotleListAdapter);
					// 千万别忘了告诉控件加载完毕了哦！
					ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				// 千万别忘了告诉控件加载完毕了哦！
				ptrl.loadmoreFinish(PullToRefreshLayout.FAIL);
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
		mBack = (ImageView) findViewById(R.id.ktv_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.ktv_title).findViewById(R.id.title);
		mLv = (ListView) findViewById(R.id.ktv_listview);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mBack.setOnClickListener(this);
		mTitle.setText("商家列表");
		http = new HttpUtil(handler);
		progressDialog = MyProgressDialog.createDialog(this);
		ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
		ptrl.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				
				if (MyApplication.getNetObject().isNetConnected()) {
					http.sendGet(
							RequestCode.GETHOTELLIST,
							WebUrlConfig.getKtvShops(String.valueOf(MyApplication.lng),String.valueOf(MyApplication.lat),String.valueOf(0)));
				} else {
					ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
					ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
				}
			}
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				if (MyApplication.getNetObject().isNetConnected()) {
					http.sendGet(
							RequestCode.GETHOTELLISTMORE,
							WebUrlConfig.getKtvShops(String.valueOf(MyApplication.lng),String.valueOf(MyApplication.lat),String.valueOf(page)));
				} else {
					ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
				}
			}
		});
		hotleListAdapter=new HotleListViewAdapter(mContext,lhotelList);
		mLv.setAdapter(hotleListAdapter);
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			http.sendGet(
					RequestCode.GETHOTELLIST,
					WebUrlConfig.getKtvShops(String.valueOf(MyApplication.lng),String.valueOf(MyApplication.lat),String.valueOf(0)));
		} else {
			ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	/**
	 * 初始化listview
	 */
	private void initListView() {
//		mLv.setAdapter(new KTVListViewAdapter(mContext));
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(mContext, KTVDescActivity.class);
				intent.putExtra("hotel", lhotelList.get(position));
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		if(v == mBack){
			onBackPressed();
			finish();
		}
	}

}

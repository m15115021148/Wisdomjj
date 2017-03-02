package com.sitemap.wisdomjingjiang.fragments;

import java.util.List;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.activities.SearchActivity;
import com.sitemap.wisdomjingjiang.activities.ShoppingListActivity;
import com.sitemap.wisdomjingjiang.adapters.MerchantGridViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.MerchantListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.GoodSmallTypesModel;
import com.sitemap.wisdomjingjiang.models.GoodsBigTypesModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.fragments.Fragment2
 * 
 * @author chenmeng 商家 页面 create at 2016年4月27日 上午10:00:54
 */
@ContentView(R.layout.fragment_merchant)
public class MerchantFragment extends BaseFragment {
	/** 左边的listview */
	@ViewInject(R.id.merchant_listview)
	private ListView mLv;
	/** 全局类 */
	private Context mContext;
	/** 搜索框 */
	@ViewInject(R.id.search_content)
	private EditText mSearch;
	/** 传值的标致 */
	public static final String MERCHANT_POSITION = "merchant_position";
	/** 数据来源 */
	private List<String> mList;
	/** 右侧 gridview */
	@ViewInject(R.id.merchant_gridview)
	private GridView mGv;
	/** gridview的适配器 */
	private MerchantGridViewAdapter mAdapter;
	private MerchantListViewAdapter bigAdapter;// 大类

	private MyProgressDialog progress;// 进度条
	private HttpUtil http;// http对象
	private List<GoodsBigTypesModel> bigList;// 大类列表
	private List<GoodSmallTypesModel> smallList;// 小类列表
	private boolean isShow = false;// 是否显示

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mContext = this.getActivity();
		initSearch();
		initListView();// 初始化listview
		initGridView();// 初始化gridview
		getData(RequestCode.GETBIGGOODS, WebUrlConfig.getGoodsBigTypes());
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		if (isShow) {
//			getData(RequestCode.GETBIGGOODS, WebUrlConfig.getGoodsBigTypes());
//		}
//	}
//
//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		isShow = isVisibleToUser;
//		super.setUserVisibleHint(isVisibleToUser);
//	}

	/**
	 * 初始化搜索框
	 */
	private void initSearch() {
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
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 != RequestCode.GETBIGGOODS) {
				// 关闭进度条
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
			}
			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				if (msg.arg1 == RequestCode.GETBIGGOODS) {// 大类别
					bigList = JSON.parseArray(msg.obj.toString(),
							GoodsBigTypesModel.class);
					bigAdapter = new MerchantListViewAdapter(mContext, bigList);
					mLv.setAdapter(bigAdapter);
					if (bigList.size() == 0) {
						return;
					}
					String typeId = bigList.get(0).getBigTypeID();
					getData(RequestCode.GETSMALLGOODS,
							WebUrlConfig.getGoodSmallTypes(typeId));
				}
				if (msg.arg1 == RequestCode.GETSMALLGOODS) {// 小类别
					smallList = JSON.parseArray(msg.obj.toString(),
							GoodSmallTypesModel.class);
					Log.i("jack", smallList.toString());
//					if (mAdapter != null) {
//						Log.i("jack", "not null");
////						mAdapter.getList().clear();
//						mAdapter.setList(smallList);
//						mAdapter.notifyDataSetChanged();
//					} else {
						Log.i("jack", "null");
						mAdapter = new MerchantGridViewAdapter(mContext,
								smallList);
						mGv.setAdapter(mAdapter);
//					}

				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showShortToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 加载网络数据
	 */
	private void getData(int order, String url) {
		if (!MyApplication.getNetObject().isNetConnected()) {
			ShowContentUtils.showShortToastMessage(mContext,
					RequestCode.NOLOGIN);
		} else {

			if (progress == null) {
				progress = MyProgressDialog.createDialog(mContext);
			}
			if (!progress.isShowing()) {
				progress.show();
			}
			if (http == null) {
				http = new HttpUtil(handler);
			}
			http.sendCache(order, url);
		}
	}

	/**
	 * 初始化listview
	 */
	private void initListView() {
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (bigList != null) {
					bigAdapter.setSelectPosition(position);
					bigAdapter.notifyDataSetChanged();
					String typeId = bigList.get(position).getBigTypeID();
					getData(RequestCode.GETSMALLGOODS,
							WebUrlConfig.getGoodSmallTypes(typeId));
				}else{
					ShowContentUtils.showLongToastMessage(mContext, "暂无商品信息");
				}
			}
		});

	}

	/**
	 * 初始化gridview
	 */
	private void initGridView() {

		mGv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("typeID", smallList.get(position)
						.getSmallTypeID());
				intent.putExtra("typeName", smallList.get(position)
						.getSmallTypeName());
				intent.setClass(mContext, ShoppingListActivity.class);
				intent.putExtra("isColl", false);
				startActivity(intent);
			}
		});
	}

}

package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.CommentListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.FoodCommentsModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author chenmeng 评论列表 页面
 */
public class CommentListActivity extends BaseActivity implements
		OnClickListener {
	/** 全局类 */
	private Context mContext;
	/** 标题栏 返回上一层 */
	private ImageView mBack;
	/** 标题栏 标题 */
	private TextView mTitle;
	/** listview */
	private ListView mLv;
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/** 评论的数据 */
	List<FoodCommentsModel> mList;
	/** 团购 id */
	private String foodID;
	private String goodsID;// 购物商品id
	private int page = 0;
	/**没有评论的时候不显示*/ 
	private LinearLayout isShowList;
	/**没有评论显示内容*/ 
	private TextView commentNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_list);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据
		// initListView();//初始化listview
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.comment_more_title).findViewById(
				R.id.back);
		mTitle = (TextView) findViewById(R.id.comment_more_title).findViewById(
				R.id.title);
		mLv = (ListView) findViewById(R.id.comment_listview);
		isShowList = (LinearLayout) findViewById(R.id.comment_show);
		commentNo = (TextView) findViewById(R.id.comment_nono);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mTitle.setText("评论列表");
		mBack.setOnClickListener(this);

		if (http == null) {
			http = new HttpUtil(handler);
		}

		foodID = getIntent().getStringExtra("foodID");
		goodsID = getIntent().getStringExtra("goodsID");
		getCommentData();
	}

	/**
	 * 获取团购评价
	 */
	private void getCommentData() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			if (TextUtils.isEmpty(foodID)) {
				http.sendGet(
						RequestCode.GETFOODCOMMENTS,
						WebUrlConfig.getGoodsComment(goodsID,
								String.valueOf(page)));
			} else {
				String foodComments = WebUrlConfig.getFoodComments(foodID,
						String.valueOf(page));
				http.sendGet(RequestCode.GETFOODCOMMENTS, foodComments);
			}
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	/**
	 * 初始化listview
	 */
	private void initListView() {
		mLv.setAdapter(new CommentListViewAdapter(mContext, mList));
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
				if (msg.arg1 == RequestCode.GETFOODCOMMENTS) {
					Log.e("result", msg.obj.toString());
					mList = JSONObject.parseArray(msg.obj.toString(),
							FoodCommentsModel.class);
					if(mList.size()>0){
						initListView();
					}else{//没有评论
						isShowList.setVisibility(View.GONE);
						commentNo.setVisibility(View.VISIBLE);
					}
					
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.GETFOODCOMMENTS) {
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
		if (v == mBack) {// 返回上一层
			onBackPressed();
			finish();
		}
	}

}

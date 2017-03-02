package com.sitemap.wisdomjingjiang.activities;

import java.util.List;

import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.AddressListViewAdapter;
import com.sitemap.wisdomjingjiang.adapters.AddressListViewAdapter.CallBackAddressList;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.AddressModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

/**
 * com.sitemap.wisdomjingjiang.activities.AddressListActivity
 * 
 * @author zhang create at 2016年5月13日 上午9:13:57
 */

public class AddressListActivity extends BaseActivity implements
		OnClickListener, CallBackAddressList {
	/** 收货地址列表 */
	private ListView mLv;
	/** 我的收货地址适配器 */
	private AddressListViewAdapter mAdapter;
	/** 本类 */
	private AddressListActivity mContext;
	/** 标题栏 标题 */
	private TextView mTitle;
	/** 返回上一层 */
	private ImageView mBack;
	/** 添加新地址按钮 */
	private TextView mAddAddress;
	/**网络请求*/ 
	private HttpUtil http;
	/**进度条*/ 
	private static MyProgressDialog progressDialog;	
	/**我的收货 地址  数据*/ 
	private List<AddressModel> mList = null;
	/**用户id*/ 
	private String userID;
	/**resultActivity 返回结果 量*/ 
	private static final int ADD_ADDRESS_RESULT = 0x1001;
	/**当前所选择的位置*/ 
	private int selectPosition;
	/**选择收货地址标致*/ 
	private int updatFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_list);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据
	}

	/**
	 * 初始化控件view
	 */
	public void initView() {
		mLv = (ListView) findViewById(R.id.address_list);
		mBack = (ImageView) findViewById(R.id.address_list_title).findViewById(
				R.id.back);
		mTitle = (TextView) findViewById(R.id.address_list_title).findViewById(
				R.id.title);
		mAddAddress = (TextView) findViewById(R.id.address_add);

	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		mTitle.setText(R.string.address_title);
		mBack.setOnClickListener(this);
		mAddAddress.setOnClickListener(this);
		
		updatFlag = getIntent().getIntExtra("type", 0);

		if(http == null){
			http = new HttpUtil(handler);
		}
		userID = MyApplication.loginModel.getUserID();
		getAddressData();//获取我的收获地址
	}

	/**
	 * 初始化listview
	 */
	private void initListView() {
		mAdapter = new AddressListViewAdapter(mContext,mList, this);
		mLv.setAdapter(mAdapter);
		if(updatFlag == 1){
			mLv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent();
					Bundle b = new Bundle();
					b.putSerializable("AddressModel", mList.get(position));
					intent.putExtras(b);
					setResult(RequestCode.ADDADDRESS, intent);
					mContext.finish();
				}
			});
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
				//获取我的收货地址
				if (msg.arg1 == RequestCode.GETADDRESS) {
					mList = JSONObject.parseArray(msg.obj.toString(),AddressModel.class);
					initListView();
					mAdapter.notifyDataSetChanged();
				}
				//删除地址
				if(msg.arg1 == RequestCode.DELETEADDRESS){
					CodeModel model = JSONObject.parseObject(msg.obj.toString(), CodeModel.class);
					if(model.getStatus() == 0){
						ShowContentUtils.showLongToastMessage(mContext, "删除成功");
						getAddressData();
					}else{
						ShowContentUtils.showLongToastMessage(mContext, model.getErrorMsg());
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.GETADDRESS) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}
	};	
	
	/**
	 * 获取我的收获地址
	 */
	private void getAddressData(){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String address = WebUrlConfig.getAddress(userID);
			http.sendGet(RequestCode.GETADDRESS, address);
		}else{
			ShowContentUtils.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mBack) {
			onBackPressed();
			finish();
		}
		if (v == mAddAddress) {//增加新的地址
			Intent intent = new Intent(mContext, AddressAddActivity.class);
			startActivityForResult(intent, ADD_ADDRESS_RESULT);
		}
	}

	/**
	 * 编辑点击事件
	 */
	@Override
	public void onClickEditListener(int position) {
		selectPosition = position;
		Intent intent = new Intent(mContext, AddressUpdateActivity.class);
		AddressModel model = mList.get(selectPosition);
		Bundle b = new Bundle();
		b.putSerializable("AddressModel", model);
		intent.putExtras(b);
		startActivityForResult(intent, ADD_ADDRESS_RESULT);
	}

	/**
	 * 删除事件
	 */
	@Override
	public void onClickDeleteListener(int position) {
		selectPosition = position;
		Builder dialog = MyApplication.myAlertDialog(mContext, true, "提示", "确定删除吗？", 
				new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteAddressData(mList.get(selectPosition).getAddressID());
				dialog.dismiss();
			}
		});	
		dialog.create().show();
	}
	
	/**
	 * 删除我的收获地址
	 */
	private void deleteAddressData(String addressID){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String deleteAddress = WebUrlConfig.deleteAddress(userID, addressID);
			RequestParams params = new RequestParams(deleteAddress);
			http.sendPost(RequestCode.DELETEADDRESS, params);
		}else{
			ShowContentUtils.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case ADD_ADDRESS_RESULT:
			getAddressData();//刷新
			break;
		default:
			break;
		}
	}
}

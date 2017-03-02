package com.sitemap.wisdomjingjiang.activities;

import java.util.List;

import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.OrderFoodsListAdapter;
import com.sitemap.wisdomjingjiang.adapters.OrderFoodsListAdapter.CallBackOrderFoods;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.OrderFoodsModel;
import com.sitemap.wisdomjingjiang.models.RefundStatusModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author chenmeng 团购订单 列表
 */
public class OrderFoodsListActivity extends BaseActivity implements
		OnClickListener, CallBackOrderFoods {
	/** 本类 */
	private OrderFoodsListActivity mContext;
	/** 订单列表适配器 */
	private OrderFoodsListAdapter mAdapter;
	/** 购物订单列表 */
	private ExpandableListView mLv;
	/** 返回上一层 */
	private ImageView mBack;
	/** 标题 */
	private TextView mTitle;
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/** 团购 订单数据 */
	private List<OrderFoodsModel> mFoodsList = null;
	/** radiogroup view */
	private RadioGroup mGroup;
	/** userid */
	private String userID = MyApplication.loginModel.getUserID();
	/** 订单状态 */
	private int orderType = 0;
	/**删除 时弹出的对话框*/ 
	private Builder mDeleteDialog;
	/** 商品类别（1：团购 2：购物商品） */
	private String type = "1";
	/**订单状态不存在时候*/ 
	private LinearLayout mOrderNoExit;
	/**返回的退款状态 实体类*/ 
	private RefundStatusModel refundModel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_foods_list);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据
		initRadioData();// 初始化 radio 数据
	}

	/**
	 * 初始化控件view
	 */
	public void initView() {
		mBack = (ImageView) findViewById(R.id.order_foods_title).findViewById(
				R.id.back);
		mTitle = (TextView) findViewById(R.id.order_foods_title).findViewById(
				R.id.title);
		mLv = (ExpandableListView) findViewById(R.id.order_foods_listview);
		mGroup = (RadioGroup) findViewById(R.id.order_foods_rg);
		mOrderNoExit = (LinearLayout) findViewById(R.id.order_no_exit);
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		mBack.setOnClickListener(this);
		mTitle.setText("团购订单");

		if (http == null) {
			http = new HttpUtil(handler);
		}
	}

	/**
	 * 初始化 radio 数据
	 */
	private void initRadioData() {
		getOrderFoodsData();
		RadioButton rb1 = (RadioButton) mGroup.getChildAt(0);
		rb1.setChecked(true);
		mGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				for (int i = 0; i < mGroup.getChildCount(); i++) {
					RadioButton rb = (RadioButton) mGroup.getChildAt(i);
					if (rb.getId() == checkedId) {
						orderType = i;
						break;
					}
				}
				getOrderFoodsData();
			}
		});
	}

	/**
	 * 初始化listview
	 */
	private void initListViewData() {
		if (mFoodsList != null) {
			mAdapter = new OrderFoodsListAdapter(mContext, mFoodsList, this);
			mLv.setAdapter(mAdapter);
			// 全部展开
			for (int i = 0; i < mFoodsList.size(); i++) {
				mLv.expandGroup(i);
			}
			//点击事件
			mLv.setOnChildClickListener(new OnChildClickListener() {
				
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					Intent intent = new Intent(mContext,OrderFoodsDescActivity.class);
					CartFoodsInfoModel model = mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition);
					OrderFoodsModel goodsModel = mFoodsList.get(groupPosition);
					Bundle b = new Bundle();					
					b.putSerializable("CartFoodsInfoModel",model);
					b.putSerializable("OrderFoodsModel", goodsModel);
					intent.putExtras(b);
					intent.putExtra("isShow", 1);
					intent.putExtra("foodsList", JSON.toJSONString(mFoodsList.get(groupPosition).getFoodsInfo()));
					startActivityForResult(intent, RequestCode.DELETEORDER);
					return true;
				}
			});
		}
	}

	/**
	 * 获取订单中的团购商品订单
	 */
	private void getOrderFoodsData() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String orderFoods = WebUrlConfig.getOrderFoods(userID,String.valueOf(orderType));
			http.sendGet(RequestCode.GETORDERFOODS, orderFoods);
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	/**
	 * 查询退款状态
	 * @param orderID
	 */
	private void checkRefundStatus(String orderID){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}			
			http.sendGet(RequestCode.CHECKREFUNDSTATUS, WebUrlConfig.checkRefundStatus(orderID,String.valueOf(1)));
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
				//获取订单
				if (msg.arg1 == RequestCode.GETORDERFOODS) {
					Log.e("result", msg.obj.toString());
					mFoodsList = JSONObject.parseArray(msg.obj.toString(),OrderFoodsModel.class);
					if(mFoodsList!=null){
						initListViewData();
						if(mFoodsList.size()>0){
							mOrderNoExit.setVisibility(View.GONE);								
						}else{
							mOrderNoExit.setVisibility(View.VISIBLE);	
						}
					}					
				}
				//删除订单
				if(msg.arg1 == RequestCode.DELETEORDER){
					CodeModel model = JSONObject.parseObject(msg.obj.toString(), CodeModel.class);
					if(model.getStatus() == 0){
						getOrderFoodsData();//刷新
						ShowContentUtils.showLongToastMessage(mContext, "删除成功");
					}else{
						ShowContentUtils.showLongToastMessage(mContext, model.getErrorMsg());
					}					
				}
				//查询退款订单状态
				if(msg.arg1 == RequestCode.CHECKREFUNDSTATUS){
					refundModel = JSON.parseObject(msg.obj.toString(),RefundStatusModel.class);
					if(refundModel!=null){
						Intent intent = new Intent(mContext, WuLiuWebActivity.class);
						intent.putExtra("refundUrl", refundModel.getRefundUrl());
						intent.putExtra("typeStatus", 2);//物流标致
						startActivity(intent);
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.GETORDERFOODS) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 删除订单
	 */
	private void getDeleteData(String orderID,int type){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String deleteOrder = WebUrlConfig.deleteOrder(orderID,String.valueOf(type));
			RequestParams params = http.getParams(deleteOrder);
			http.sendPost(RequestCode.DELETEORDER, params);
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
	}

	/**
	 *	删除
	 */
	@Override
	public void onClickDeleteOrderListener(int groupPosition, int childPosition) {
		int typeStatus = Integer.parseInt(mFoodsList.get(groupPosition).getOrderType());
		if(typeStatus==1){//待付款
			final CartFoodsInfoModel foodsModel = mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition);
			mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定删除吗?", new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getDeleteData(foodsModel.getOrderInfoID(),2);
					dialog.dismiss();
				}
			});
			mDeleteDialog.create().show();
		}else{//已付款
			final CartFoodsInfoModel foodsModel = mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition);		
			mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定删除吗?", new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getDeleteData(foodsModel.getOrderInfoID(),1);
					dialog.dismiss();
				}
			});
			mDeleteDialog.create().show();
		}
		
	}

	/**
	 *	确定 /退款 评论 付款 等等按钮
	 */
//	@Override
//	public void onClickSureListener(int groupPosition, int childPosition) {
//		int typeStatus = Integer.parseInt(mFoodsList.get(groupPosition).getOrderType());
//		if(typeStatus == 3){//待使用(退款)
			//计算退款金额
//			String price = mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition).getFoodsPrice();
//			String number = mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition).getNumber();
//			if(price == null || price.equals("")){
//				price = "1";
//			}
//			double money = Double.parseDouble(price)*Double.parseDouble(number);
//			Intent intent = new Intent(mContext, RefundActivity.class);
//			Bundle b = new Bundle();
//			b.putSerializable("OrderFoodsModel", mFoodsList.get(groupPosition));
//			intent.putExtras(b);
//			intent.putExtra("type",type);//团购商品
//			intent.putExtra("money", money);
//			startActivityForResult(intent,RequestCode.REFUND);
//		}
//		if(typeStatus == 4){//待评价
//			
//		}
//		if(typeStatus == 1){//待付款
//			Intent intent = new Intent(mContext,
//					OrderFoodsDescActivity.class);
//			CartFoodsInfoModel model = mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition);
//			OrderFoodsModel goodsModel = mFoodsList.get(groupPosition);
//			Bundle b = new Bundle();					
//			b.putSerializable("CartFoodsInfoModel",model);
//			b.putSerializable("OrderFoodsModel", goodsModel);
//			intent.putExtras(b);
//			intent.putExtra("foodsList", JSON.toJSONString(mFoodsList.get(groupPosition).getFoodsInfo()));
//			startActivityForResult(intent, RequestCode.ADDORDER);
//		}
//	}	
	
	/**
	 *	付款
	 */
	@Override
	public void onClickCommentListener(int groupPosition, int childPosition) {
		int typeStatus = Integer.parseInt(mFoodsList.get(groupPosition).getOrderType());
		if(typeStatus==1){//待付款
			Intent intent = new Intent(mContext,
					OrderFoodsDescActivity.class);
			CartFoodsInfoModel model = mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition);
			OrderFoodsModel goodsModel = mFoodsList.get(groupPosition);
			Bundle b = new Bundle();					
			b.putSerializable("CartFoodsInfoModel",model);
			b.putSerializable("OrderFoodsModel", goodsModel);
			intent.putExtras(b);
			intent.putExtra("foodsList", JSON.toJSONString(mFoodsList.get(groupPosition).getFoodsInfo()));
			startActivityForResult(intent, RequestCode.ADDORDER);
		}				
	}

	/**
	 *	退款
	 */
	@Override
	public void onClickRefundListener(int groupPosition, int childPosition) {
		int isRefund = Integer.parseInt(mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition).getIsRefund());
		if(isRefund==0){//退款
			Intent intent = new Intent(mContext, RefundActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("CartFoodsInfoModel", mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition));
			intent.putExtras(b);
			intent.putExtra("type",type);//团购商品
			startActivityForResult(intent,RequestCode.REFUND);
		}
		if(isRefund==1){//退款中
//			MyApplication.showDialog(mContext, "退款申请已提交，现已交由财务处理；如有疑问，请及时和后台管理员联系，电话："+MyApplication.WuQuanTel);
			if(refundModel!=null){
				refundModel = null;
			}
			checkRefundStatus(mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition).getOrderInfoID());
//			Intent intent = new Intent(mContext, WuLiuWebActivity.class);
//			intent.putExtra("refundUrl", refundModel.getRefundUrl());
//			intent.putExtra("typeStatus", 2);//物流标致
//			startActivity(intent);
		}		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RequestCode.DELETEORDER:
			getOrderFoodsData();//刷新
			break;
		case RequestCode.ADDORDER:
			getOrderFoodsData();
			break;
		case RequestCode.REFUND:
			mAdapter.changerOrderStatus(8);
			getOrderFoodsData();
			break;
		case RequestCode.ADDCOMMENT:
			mAdapter.changerOrderStatus(6);
			getOrderFoodsData();
			break;
		default:
			break;
		}
	}

	/**
	 *	评论
	 */
	@Override
	public void onClickStatusListener(int groupPosition, int childPosition) {
		int isComment = Integer.parseInt(mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition).getIsComment());
		int isRefund = Integer.parseInt(mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition).getIsRefund());
		if(isComment==2){
			Intent intent = new Intent(mContext, PingjiaActivity.class);
			CartFoodsInfoModel model = mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition);
			Bundle b = new Bundle();
			b.putSerializable("CartFoodsInfoModel", model);			
			intent.putExtras(b);
			intent.putExtra("type",type);//美食商品
			startActivityForResult(intent,RequestCode.ADDCOMMENT);
		}	
		if(isRefund==2){//已退款
//			MyApplication.showDialog(mContext, "退款已完成，如有疑问，请和后台管理员联系，电话："+MyApplication.WuQuanTel);
			if(refundModel!=null){
				refundModel = null;
			}
			checkRefundStatus(mFoodsList.get(groupPosition).getFoodsInfo().get(childPosition).getOrderInfoID());
//			Intent intent = new Intent(mContext, WuLiuWebActivity.class);
//			intent.putExtra("refundUrl", refundModel.getRefundUrl());
//			intent.putExtra("typeStatus", 2);//物流标致
//			startActivity(intent);
		}
	}

}

package com.sitemap.wisdomjingjiang.activities;

import java.util.List;

import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.OrderGoodsListAdapter;
import com.sitemap.wisdomjingjiang.adapters.OrderGoodsListAdapter.CallBackOrderGoods;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.OrderGoodsModel;
import com.sitemap.wisdomjingjiang.models.RefundStatusModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.OrderDialog;

/**
 * @author chenmeng	
 * 	购物商品 订单
 */
public class OrderShopsListActivity extends BaseActivity implements
		OnClickListener, CallBackOrderGoods {
	/** 本类 */
	private OrderShopsListActivity mContext;
	/** 订单列表适配器 */
	private OrderGoodsListAdapter mAdapter;
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
	/** radiogroup view */
	private RadioGroup mGroup;
	/** 订单数据 商品信息 */
	private List<OrderGoodsModel> mGoodsList = null;
	/** userid */
	private String userID = MyApplication.loginModel.getUserID();
	/** 订单状态 */
	private int orderType = 0;
	/**商品类别（1：团购 2：购物商品）*/ 
	private String type = "2";
	/**删除 时弹出的对话框*/ 
	private Builder mDeleteDialog;
	/**订单状态不存在时候*/ 
	private LinearLayout mOrderNoExit;
	/**返回的退款状态 实体类*/ 
	private RefundStatusModel refundModel = null;
	/**整个布局*/ 
	private LinearLayout mLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_shops_list);
		mContext = this; 
		initView();// 初始化view
		initData();// 初始化数据
		initRadioData();// 初始化 radio 数据
	}

	/**
	 * 初始化控件view
	 */
	public void initView() {
		mBack = (ImageView) findViewById(R.id.order_shops_title).findViewById(
				R.id.back);
		mTitle = (TextView) findViewById(R.id.order_shops_title).findViewById(
				R.id.title);
		mLv = (ExpandableListView) findViewById(R.id.order_shops_listview);
		mGroup = (RadioGroup) findViewById(R.id.order_shops_rg);
		mOrderNoExit = (LinearLayout) findViewById(R.id.order_no_exit);
		mLayout = (LinearLayout) findViewById(R.id.order_goods_layout);
	}

	/**
	 * 初始化 radio 数据
	 */
	private void initRadioData() {
		// 默认显示全部订单
		getOrderGoodsData();

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
				getOrderGoodsData();
			}
		});
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		mBack.setOnClickListener(this);
		mTitle.setText("购物订单");		

		if (http == null) {
			http = new HttpUtil(handler);
		}

	}

	/**
	 * 初始化listview
	 */
	private void initListViewData() {
		if (mGoodsList != null) {
			mAdapter = new OrderGoodsListAdapter(mContext, mGoodsList, this);
			mLv.setAdapter(mAdapter);
			// 全部展开
			for (int i = 0; i < mGoodsList.size(); i++) {
				mLv.expandGroup(i);
			}
			mLv.setOnChildClickListener(new OnChildClickListener() {				
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					Intent intent = new Intent(mContext,OrderGoodsDescActivity.class);
					CartGoodsInfoModel model = mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition);
					OrderGoodsModel goodsModel = mGoodsList.get(groupPosition);
					Bundle b = new Bundle();					
					b.putSerializable("CartGoodsInfoModel",model);
					b.putSerializable("OrderGoodsModel", goodsModel);
					intent.putExtra("isShow", 1);//是否显示
					intent.putExtras(b);		
					intent.putExtra("goodsList", JSON.toJSONString(mGoodsList.get(groupPosition).getGoodsInfo()));
					startActivity(intent);
					return true;
				}
			});
		}
	}

	/**
	 * 获取订单中的购物商品订单
	 */
	private void getOrderGoodsData() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String orderGoods = WebUrlConfig.getOrderGoods(userID,
					String.valueOf(orderType));
			http.sendGet(RequestCode.GETORDERGOODS, orderGoods);
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
				if (msg.arg1 == RequestCode.GETORDERGOODS) {
					Log.e("result", msg.obj.toString());
					mGoodsList = JSONObject.parseArray(msg.obj.toString(),
							OrderGoodsModel.class);					
					if(mGoodsList!=null){
						initListViewData();					
						if(mGoodsList.size()>0){
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
						getOrderGoodsData();
						ShowContentUtils.showLongToastMessage(mContext, "删除成功");
					}else{
						ShowContentUtils.showLongToastMessage(mContext, model.getErrorMsg());
					}					
				}
				//确认收货
				if(msg.arg1 == RequestCode.CONFIRMORDER){
					CodeModel model = JSONObject.parseObject(msg.obj.toString(), CodeModel.class);
					if(model.getStatus() == 0){
						getOrderGoodsData();
						ShowContentUtils.showLongToastMessage(mContext, "亲! 记得去评价呗!");
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
				//申请仲裁
				if(msg.arg1 == RequestCode.APPLYJUDGMENT){
					CodeModel model = JSONObject.parseObject(msg.obj.toString(), CodeModel.class);
					if(model.getStatus() == 0){						
						ShowContentUtils.showLongToastMessage(mContext, "申请仲裁成功，请等待处理");
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
				if (msg.arg2 == RequestCode.GETORDERGOODS) {
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
		if (v == mBack) {
			onBackPressed();
			finish();
		}
	}
	
	/**
	 * 确认收货 
	 * @param orderID
	 */
	private void confirmOrder(String orderID){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String confirmOrder = WebUrlConfig.confirmOrder(userID, orderID);
			RequestParams params = http.getParams(confirmOrder);
			http.sendPost(RequestCode.CONFIRMORDER, params);
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	/**
	 * 删除订单
	 */
	private void getDeleteData(String orderID){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			String deleteOrder = WebUrlConfig.deleteOrder(orderID,String.valueOf(2));
			RequestParams params = http.getParams(deleteOrder);
			http.sendPost(RequestCode.DELETEORDER, params);
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
			http.sendGet(RequestCode.CHECKREFUNDSTATUS, WebUrlConfig.checkRefundStatus(orderID,String.valueOf(2)));
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	/**
	 * 申请仲裁
	 * @param orderInfoID
	 */
	public void applyJudgment(String orderInfoID){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}			
			http.sendGet(RequestCode.APPLYJUDGMENT, WebUrlConfig.applyJudgment(orderInfoID));
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	/**
	 * 删除订单
	 */
	@Override
	public void onClickDeleteOrderListener(int groupPosition,int childPosition) {
		final CartGoodsInfoModel model = mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition);
		mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定删除吗?", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getDeleteData(model.getOrderInfoID());
				dialog.dismiss();
			}
		});
		mDeleteDialog.create().show();
	}

	/**
	 * 待付款  
	 * 已付款中的查看物流
	 */
	@Override
	public void onClickSureListener(int groupPosition,int childPosition) {
		int typeStatus = Integer.parseInt(mGoodsList.get(groupPosition).getOrderType());
//		int isNoRefund = Integer.parseInt(mGoodsList.get(groupPosition).getIsNoRefund());
//		if(typeStatus == 2){// 代发货(退款)	or (电话维权)	
//			if(isNoRefund == 1){//维权电话
//				if(TextUtils.isEmpty(mGoodsList.get(groupPosition).getSellerfeedback())){
//					mGoodsList.get(groupPosition).setSellerfeedback("原因不详");
//				}
//				MyApplication.showDialog(mContext, mGoodsList.get(groupPosition).getSellerfeedback());
//				if(refundModel!=null){
//					refundModel = null;
//				}
//				checkRefundStatus(mGoodsList.get(groupPosition).getOrderID());
//				Intent intent = new Intent(mContext, WuLiuWebActivity.class);
//				intent.putExtra("refundUrl", refundModel.getRefundUrl());
//				intent.putExtra("typeStatus", 2);//物流标致
//				startActivity(intent);
//			}else{//没有拒绝过  申请过退款
//				mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定打电话维权吗?", new DialogInterface.OnClickListener() {			
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Intent intent = new Intent();
//					    intent.setAction("android.intent.action.DIAL");
//					    intent.setData(Uri.parse("tel:"+ MyApplication.WuQuanTel));
//					    startActivity(intent);
//						dialog.dismiss();
//					}
//				});
//				mDeleteDialog.create().show();				
//				Intent intent = new Intent(mContext, RefundActivity.class);
//				Bundle b = new Bundle();
//				b.putSerializable("OrderGoodsModel", mGoodsList.get(groupPosition));
//				intent.putExtras(b);
//				intent.putExtra("type",type);//购物商品
//				startActivityForResult(intent,RequestCode.REFUND);	
//			}
//		}
//		if(typeStatus == 3){ //待收货(确认收货)
//			final OrderGoodsModel orderGoodsModel = mGoodsList.get(groupPosition);
//			mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定收货吗?", new DialogInterface.OnClickListener() {			
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					confirmOrder(orderGoodsModel.getOrderID());
//					dialog.dismiss();
//				}
//			});
//			mDeleteDialog.create().show();
//		}
//		if(typeStatus == 4){//待评价(查看物流)
//			if(!TextUtils.isEmpty(mGoodsList.get(groupPosition).getExpressNo())){//查看物流
//				Intent intent = new Intent(mContext, WuLiuWebActivity.class);
//				intent.putExtra("orderName", mGoodsList.get(groupPosition).getExpressNo());
//				intent.putExtra("type", mGoodsList.get(groupPosition).getExpressName());
//				intent.putExtra("typeStatus", 1);//物流标致
//				startActivity(intent);
//			}
//		}
//		if(typeStatus == 1){//待付款
//			Intent intent = new Intent(mContext,
//					OrderGoodsDescActivity.class);
//			CartGoodsInfoModel model = mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition);
//			OrderGoodsModel goodsModel = mGoodsList.get(groupPosition);
//			Bundle b = new Bundle();					
//			b.putSerializable("CartGoodsInfoModel",model);
//			b.putSerializable("OrderGoodsModel", goodsModel);
//			intent.putExtras(b);
//			intent.putExtra("goodsList", JSON.toJSONString(mGoodsList.get(groupPosition).getGoodsInfo()));
//			startActivityForResult(intent,RequestCode.ADDORDER);
//		}
//		if(typeStatus==7){
//			MyApplication.showDialog(mContext, "退款已完成，如有疑问，请和后台管理员联系，电话："+MyApplication.WuQuanTel);	
//			if(refundModel!=null){
//				refundModel = null;
//			}
//			checkRefundStatus(mGoodsList.get(groupPosition).getOrderID());
//			Intent intent = new Intent(mContext, WuLiuWebActivity.class);
//			intent.putExtra("refundUrl", refundModel.getRefundUrl());
//			intent.putExtra("typeStatus", 2);//物流标致
//			startActivity(intent);
//		}
//		if(typeStatus==8){
//			if(isNoRefund==0){//退款中
//				MyApplication.showDialog(mContext, "退款申请已提交，待商家处理；如有疑问，请和商家及时沟通，电话："+mGoodsList.get(groupPosition).getShopMobile());				
//			}else if(isNoRefund==2){//同意 退款
//				if(mGoodsList.get(groupPosition).getIsBackGoods().equals("1")){//收到退货
//					MyApplication.showDialog(mContext, "商家已收到您的退货，现已交由财务处理；如有疑问，请及时和后台管理员联系，电话："+MyApplication.WuQuanTel);				
//				}else{
//					MyApplication.showDialog(mContext, "商家已同意您的退款请求，请及时和商家电话（"+mGoodsList.get(groupPosition).getShopMobile()+"）沟通，进行退货等处理。");
//				}
//			}
//			if(refundModel!=null){
//				refundModel = null;
//			}
//			checkRefundStatus(mGoodsList.get(groupPosition).getOrderID());
//			Intent intent = new Intent(mContext, WuLiuWebActivity.class);
//			intent.putExtra("refundUrl", refundModel.getRefundUrl());
//			intent.putExtra("typeStatus", 2);//物流标致
//			startActivity(intent);
//		}
		
		if(typeStatus==1){//待付款
			Intent intent = new Intent(mContext,OrderGoodsDescActivity.class);
			CartGoodsInfoModel model = mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition);
			OrderGoodsModel goodsModel = mGoodsList.get(groupPosition);
			Bundle b = new Bundle();					
			b.putSerializable("CartGoodsInfoModel",model);
			b.putSerializable("OrderGoodsModel", goodsModel);
			intent.putExtras(b);
			intent.putExtra("goodsList", JSON.toJSONString(mGoodsList.get(groupPosition).getGoodsInfo()));
			startActivityForResult(intent,RequestCode.ADDORDER);
		}else
		if(typeStatus==2){//已付款   查看物流
			if(!TextUtils.isEmpty(mGoodsList.get(groupPosition).getExpressNo())){//查看物流
				Intent intent = new Intent(mContext, WuLiuWebActivity.class);
				intent.putExtra("orderName", mGoodsList.get(groupPosition).getExpressNo());
				intent.putExtra("type", mGoodsList.get(groupPosition).getExpressName());
				intent.putExtra("typeStatus", 1);//物流标致
				startActivity(intent);
			}
		}

	}	
	
	/**
	 *	评论
	 */
	@Override
	public void onClickCommentListener(final int groupPosition, final int childPosition) {		
		int shopType = Integer.parseInt(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getShopType());
		int isReject = Integer.parseInt(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getIsReject());
		if(shopType==2){//待发货(申请退款)
			if(isReject==1){//商家拒绝过 申请退款 				
				final OrderDialog dialog = new OrderDialog(mContext,
				new OnClickListener() {//申请退款					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, RefundActivity.class);
						Bundle b = new Bundle();
						b.putSerializable("CartGoodsInfoModel", mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition));
						intent.putExtras(b);
						intent.putExtra("type",type);//购物商品
						intent.putExtra("expressAll", mGoodsList.get(groupPosition).getExpressAll());
						startActivityForResult(intent,RequestCode.REFUND);						
					}
				}, 
				new OnClickListener() {//退款详情
					
					@Override
					public void onClick(View v) {
						if(refundModel!=null){
							refundModel = null;
						}
						checkRefundStatus(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getOrderInfoID());
					}
				}, mLayout);
				dialog.showAtLocation(mLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
			}else{//没有
				Intent intent = new Intent(mContext, RefundActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("CartGoodsInfoModel", mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition));
				intent.putExtras(b);
				intent.putExtra("type",type);//购物商品
				intent.putExtra("expressAll", mGoodsList.get(groupPosition).getExpressAll());
				startActivityForResult(intent,RequestCode.REFUND);
			}			
		}else
		if(shopType==3){//待收货（确认收货）
			final CartGoodsInfoModel model = mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition);
			mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定收货吗?", new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					confirmOrder(model.getOrderInfoID());
					dialog.dismiss();
				}
			});
			mDeleteDialog.create().show();
		}else
		if(shopType==4){//去评价
			Intent intent = new Intent(mContext, PingjiaActivity.class);
			CartGoodsInfoModel model = mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition);
			Bundle b = new Bundle();
			b.putSerializable("CartGoodsInfoModel", model);			
			intent.putExtras(b);
			intent.putExtra("type",type);//购物商品
			startActivityForResult(intent,RequestCode.ADDCOMMENT);
		}else
		if(shopType==7){//已退款
			if(refundModel!=null){
				refundModel = null;
			}
			checkRefundStatus(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getOrderInfoID());
		}else
		if(shopType==8){//退款中
			if(refundModel!=null){
				refundModel = null;
			}
			checkRefundStatus(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getOrderInfoID());
		}
		
	}

	/**
	 *	申请退款（确认收货）
	 */
	@Override
	public void onClickRefundListener(final int groupPosition, final int childPosition) {
//		int isNoRefund = Integer.parseInt(mGoodsList.get(groupPosition).getIsNoRefund());
//		if(isNoRefund == 1){//维权电话		
//			if(TextUtils.isEmpty(mGoodsList.get(groupPosition).getSellerfeedback())){
//				mGoodsList.get(groupPosition).setSellerfeedback("原因不详");
//			}
//			MyApplication.showDialog(mContext, mGoodsList.get(groupPosition).getSellerfeedback());
//			if(refundModel!=null){
//				refundModel = null;
//			}
//			checkRefundStatus(mGoodsList.get(groupPosition).getOrderID());
//			if(refundModel==null){
//				return;
//			}
//			Intent intent = new Intent(mContext, WuLiuWebActivity.class);
//			intent.putExtra("refundUrl", refundModel.getRefundUrl());
//			intent.putExtra("typeStatus", 2);//物流标致
//			startActivity(intent);
//		}else{//没有拒绝过  申请过退款			
//			Intent intent = new Intent(mContext, RefundActivity.class);
//			Bundle b = new Bundle();
//			b.putSerializable("OrderGoodsModel", mGoodsList.get(groupPosition));
//			intent.putExtras(b);
//			intent.putExtra("type",type);//购物商品
//			startActivityForResult(intent,RequestCode.REFUND);
//		}
		int shopType = Integer.parseInt(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getShopType());
		int isReject = Integer.parseInt(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getIsReject());
		if(shopType==2){//待发货(退款中)
//			if(refundModel!=null){
//				refundModel = null;
//			}
//			checkRefundStatus(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getOrderInfoID());
			mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定要申请仲裁吗?", new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					applyJudgment(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getOrderInfoID());	
					dialog.dismiss();
				}
			});
			mDeleteDialog.create().show();
		}else
		if(shopType==3){//待收货（申请退款）
			if(isReject==1){//商家拒绝过 申请退款 
				OrderDialog dialog = new OrderDialog(mContext,
				new OnClickListener() {//申请退款
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, RefundActivity.class);
						Bundle b = new Bundle();
						b.putSerializable("CartGoodsInfoModel", mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition));
						intent.putExtras(b);
						intent.putExtra("type",type);//购物商品
						intent.putExtra("expressAll", mGoodsList.get(groupPosition).getExpressAll());
						startActivityForResult(intent,RequestCode.REFUND);
					}
				}, 
				new OnClickListener() {//退款详情
					
					@Override
					public void onClick(View v) {
						if(refundModel!=null){
							refundModel = null;
						}
						checkRefundStatus(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getOrderInfoID());
					}
				}, mLayout);
				dialog.showAtLocation(mLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
			}else{//没有
				Intent intent = new Intent(mContext, RefundActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("CartGoodsInfoModel", mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition));
				intent.putExtras(b);
				intent.putExtra("type",type);//购物商品
				intent.putExtra("expressAll", mGoodsList.get(groupPosition).getExpressAll());
				startActivityForResult(intent,RequestCode.REFUND);
			}
		}
	}
	
	/**
	 *	待收货（是否 申请退款）
	 */
	@Override
	public void onClickStatusListener(final int groupPosition, final int childPosition) {
//		if(refundModel!=null){
//			refundModel = null;
//		}
//		checkRefundStatus(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getOrderInfoID());
		mDeleteDialog = MyApplication.myAlertDialog(mContext, true,"提示", "确定要申请仲裁吗?", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				applyJudgment(mGoodsList.get(groupPosition).getGoodsInfo().get(childPosition).getOrderInfoID());	
				dialog.dismiss();
			}
		});
		mDeleteDialog.create().show();
	}
	
	/**
	 *	返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RequestCode.DELETEORDER:
			getOrderGoodsData();//刷新
			break;
		case RequestCode.ADDORDER:
			getOrderGoodsData();
			break;
		case RequestCode.REFUND:
			mAdapter.changerOrderStatus(8);
			getOrderGoodsData();
			break;
		case RequestCode.ADDCOMMENT:
			mAdapter.changerOrderStatus(6);
			getOrderGoodsData();
			break;
		default:
			break;
		}
	}
	
}

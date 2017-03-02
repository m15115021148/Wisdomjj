package com.sitemap.wisdomjingjiang.fragments;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.activities.SubFoodsOrderActivity;
import com.sitemap.wisdomjingjiang.activities.SubGoodsOrderActivity;
import com.sitemap.wisdomjingjiang.activities.SubPayActivity;
import com.sitemap.wisdomjingjiang.adapters.ShoppingCartFoodsListAdapter;
import com.sitemap.wisdomjingjiang.adapters.ShoppingCartFoodsListAdapter.CallBackShoppingCartFoods;
import com.sitemap.wisdomjingjiang.adapters.ShoppingCartListAdapter;
import com.sitemap.wisdomjingjiang.adapters.ShoppingCartListAdapter.CallBackShoppingCart;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartFoodsModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.fragments.Fragment3
 * 
 * @author zhang create at 2016年4月27日 上午10:00:54
 */
@ContentView(R.layout.fragment_shopping_cart)
public class ShoppingCartFragment extends BaseFragment implements
		OnClickListener, CallBackShoppingCart, CallBackShoppingCartFoods {
	/** 全局类 */
	private Context mContext;
	/** 结算按钮 */
	@ViewInject(R.id.shopping_over)
	private TextView mShoppingOver;
	/** 总金额 */
	@ViewInject(R.id.shopping_money)
	private TextView shopping_money;
	/** 物品种类 */
	@ViewInject(R.id.type)
	private RadioGroup type;
	/** expandablelistview */
	@ViewInject(R.id.shopping_list)
	private ExpandableListView mElv;
	/** 全选按钮 */
	@ViewInject(R.id.shopping_list_cb)
	private CheckBox shopping_list_cb;
	/** 购物按钮 */
	@ViewInject(R.id.shopping)
	private RadioButton shopping;
	/** 团购按钮 */
	@ViewInject(R.id.foods)
	private RadioButton foods;
	private MyProgressDialog progress;// 进度条
	private HttpUtil http;// http对象
	private static List<CartGoodsModel> list = null;// 网络数据
	private static List<CartFoodsModel> foodList = null;// 网络数据
//	private boolean isShow = false;// 是否显示
	private int number;// 数量
	private ShoppingCartListAdapter adapter;// 购物车适配器
	private ShoppingCartFoodsListAdapter foodsAdapter;// 购物车适配器
	private double money = 0;// 金额
	/** 商品类型（2，商品 1，团购） */
	private int goodsType = 2;	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mContext = getActivity();
		// loadNet();
		// initData();// 初始化数据
	}

	//
	// @Override
	// public void setUserVisibleHint(boolean isVisibleToUser) {
	// isShow=isVisibleToUser;
	// super.setUserVisibleHint(isVisibleToUser);
	// }

	@Override
	public void onResume() {
		super.onResume();
		// if (isShow) {
		// mContext = getActivity();
		loadNet();
		initData();// 初始化数据

		// }

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
				if (msg.arg1 == RequestCode.GOODSCART) {
					Log.i("TAG", msg.obj.toString());
					list = JSON.parseArray(msg.obj.toString(),
							CartGoodsModel.class);
					if (list != null && list.size() > 0) {

						for (int i = 0; i < list.size(); i++) {
							for (int j = 0; j < list.get(i).getGoodsInfo()
									.size(); j++) {
								list.get(i).getGoodsInfo().get(j)
										.setChecked(true);
							}
							list.get(i).setChecked(true);
						}

					} else {
						list = new ArrayList<CartGoodsModel>();
					}
					initListView();
					money();
				}
				if (msg.arg1 == RequestCode.FOODSCART) {
					Log.i("TAG", msg.obj.toString());
					foodList = JSON.parseArray(msg.obj.toString(),
							CartFoodsModel.class);
					if (foodList != null && foodList.size() > 0) {

						for (int i = 0; i < foodList.size(); i++) {
							for (int j = 0; j < foodList.get(i).getFoodsInfo()
									.size(); j++) {
								foodList.get(i).getFoodsInfo().get(j)
										.setChecked(true);
							}
							foodList.get(i).setChecked(true);
						}

					} else {
						foodList = new ArrayList<CartFoodsModel>();
					}
					initListView(1);
					money();
				}
				if (msg.arg1 == RequestCode.DELGOODS) {

					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {
						ShowContentUtils.showLongToastMessage(mContext, "删除成功");
						if (goodsType == 2) {
							if (progress == null) {
								progress = MyProgressDialog
										.createDialog(mContext);
							}
							progress.setMessage("正在刷新列表...");
							progress.show();
							if (http == null) {
								http = new HttpUtil(handler);
							}
							http.sendGet(RequestCode.GOODSCART, WebUrlConfig
									.getCartGoods(MyApplication.loginModel
											.getUserID()));
						} else {
							if (progress == null) {
								progress = MyProgressDialog
										.createDialog(mContext);
							}
							progress.setMessage("正在刷新列表...");
							progress.show();
							if (http == null) {
								http = new HttpUtil(handler);
							}
							http.sendGet(RequestCode.FOODSCART, WebUrlConfig
									.getCartFoods(MyApplication.loginModel
											.getUserID()));
						}
					} else {
						ShowContentUtils.showLongToastMessage(mContext,
								model.getErrorMsg());
					}
				}

				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showShortToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 加载网络数据
	 */
	private void loadNet() {
		if (!MyApplication.isLogin) {
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
		progress.show();
		if (http == null) {
			http = new HttpUtil(handler);
		}
		shopping.setChecked(true);
		goodsType = 2;
		http.sendGet(RequestCode.GOODSCART,
				WebUrlConfig.getCartGoods(MyApplication.loginModel.getUserID()));

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mShoppingOver.setOnClickListener(this);
		shopping_list_cb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (goodsType == 2) {
							if (list.size() > 0) {

								if (isChecked) {
									for (int i = 0; i < list.size(); i++) {
										for (int j = 0; j < list.get(i).getGoodsInfo().size(); j++) {
											list.get(i).getGoodsInfo().get(j).setChecked(true);
										}
									}
									adapter.notifyDataSetChanged();
								} else {
									for (int i = 0; i < list.size(); i++) {
										for (int j = 0; j < list.get(i).getGoodsInfo().size(); j++) {
											list.get(i).getGoodsInfo().get(j).setChecked(false);
										}
									}									
									adapter.notifyDataSetChanged();
								}								
								money();
							}
						} else {
							if (foodList.size() > 0) {

								if (isChecked) {
									for (int i = 0; i < foodList.size(); i++) {
										for (int j = 0; j < foodList.get(i).getFoodsInfo().size(); j++) {
											foodList.get(i).getFoodsInfo().get(j).setChecked(true);
										}
									}
									foodsAdapter.notifyDataSetChanged();
								} else {
									for (int i = 0; i < foodList.size(); i++) {
										for (int j = 0; j < foodList.get(i).getFoodsInfo().size(); j++) {
											foodList.get(i).getFoodsInfo().get(j).setChecked(false);
										}
									}
									foodsAdapter.notifyDataSetChanged();
								}
								money();
							}
						}

					}
				});

		type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == shopping.getId()) {
					goodsType = 2;
					if (progress == null) {
						progress = MyProgressDialog.createDialog(mContext);
					}
					progress.show();
					if (http == null) {
						http = new HttpUtil(handler);
					}
					http.sendGet(RequestCode.GOODSCART, WebUrlConfig
							.getCartGoods(MyApplication.loginModel.getUserID()));
				} else {
					goodsType = 1;
					if (progress == null) {
						progress = MyProgressDialog.createDialog(mContext);
					}
					progress.show();
					if (http == null) {
						http = new HttpUtil(handler);
					}
					http.sendGet(RequestCode.FOODSCART, WebUrlConfig
							.getCartFoods(MyApplication.loginModel.getUserID()));
				}
				// Log.i("TAG", "checkedId:"+checkedId);
				// Log.i("TAG", "shopid:"+shopping.getId());
			}
		});
	}

	/**
	 * 初始化listview
	 */
	private void initListView(int... a) {
		if (a.length > 0) {
			foodsAdapter = new ShoppingCartFoodsListAdapter(mContext, foodList,
					this);
			mElv.setAdapter(foodsAdapter);
			foodsAdapter.notifyDataSetChanged();
			// 全部展开
			for (int i = 0; i < foodList.size(); i++) {

				mElv.expandGroup(i);
			}
		} else {
			adapter = new ShoppingCartListAdapter(mContext, list, this);
			mElv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			// 全部展开
			for (int i = 0; i < list.size(); i++) {

				mElv.expandGroup(i);
			}
		}

		// 子类点击事件
		mElv.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					final int groupPosition, final int childPosition, long id) {
				// Intent intent = new Intent(mContext,
				// ShoppingDescActivity.class);
				// startActivity(intent);
				// TextView reduces = (TextView) v.findViewById(R.id.reduce);
				// TextView add = (TextView) v.findViewById(R.id.add);
				// final TextView num = (TextView) v.findViewById(R.id.num);
				// number=Integer.parseInt(num.getText().toString());
				// reduces.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				//
				// if (number>2) {
				// number--;
				// num.setText(number+"");
				// list.get(groupPosition).getGoodsInfo().get(childPosition).setNumber(number+"");
				// adapter.notifyDataSetChanged();
				// }
				// }
				// });
				return true;

			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == mShoppingOver) {
			// 结算
			if (goodsType == 1) {// 团购
				List<CartFoodsModel> payList = new ArrayList<CartFoodsModel>();
				for (int i = 0; i < foodList.size(); i++) {
					CartFoodsModel foodsModel = foodList.get(i);
					List<CartFoodsInfoModel> currFoodsList = new ArrayList<CartFoodsInfoModel>();
					for (int j = 0; j < foodsModel.getFoodsInfo().size(); j++) {						
						if (foodsModel.getFoodsInfo().get(j).isChecked()) {// 子类选中
							currFoodsList.add(foodsModel.getFoodsInfo().get(j));
						}				
					}
					if (currFoodsList.size() > 0) {
						foodsModel.setFoodsInfo(currFoodsList);
						payList.add(foodsModel);
					}
				}
				if (payList.size() > 1) {//跳出结算页面
					Intent intent = new Intent(mContext,SubPayActivity.class);				
					intent.putExtra("type", 2);//从购物车 到  小窗口 结算 标致
					intent.putExtra("CartFoodsModel", JSONObject.toJSONString(payList));
					startActivity(intent);
				} else if(payList.size()==1){//订单页面
					Intent intent = new Intent(mContext,
							SubFoodsOrderActivity.class);
					intent.putExtra("foodList",JSONObject.toJSONString(payList));
					intent.putExtra("type", 1);// 从购物车 到提交订单 的标致
					startActivity(intent);
					payList.clear();
				}else{
					ShowContentUtils.showLongToastMessage(mContext, "请选择商品");
				}
			}
			if (goodsType == 2) {// 购物商品
				List<CartGoodsModel> payList = new ArrayList<CartGoodsModel>();
				for (int i = 0; i < list.size(); i++) {
					CartGoodsModel goodsModel = list.get(i);
					List<CartGoodsInfoModel> currGoodsList = new ArrayList<CartGoodsInfoModel>();
					for (int j = 0; j < goodsModel.getGoodsInfo().size(); j++) {						
						if (goodsModel.getGoodsInfo().get(j).isChecked()) {// 子类选中
							currGoodsList.add(goodsModel.getGoodsInfo().get(j));
						}				
					}
					if (currGoodsList.size() > 0) {
						goodsModel.setGoodsInfo(currGoodsList);
						payList.add(goodsModel);
					}
				}
				if (payList.size() > 1) {//跳出结算页面
					Intent intent = new Intent(mContext,SubPayActivity.class);
					intent.putExtra("type", 1);
					intent.putExtra("CartGoodsModel", JSONObject.toJSONString(payList));
					startActivity(intent);
				}else if(payList.size()==1){//订单页面
					Intent intent = new Intent(mContext,SubGoodsOrderActivity.class);
					intent.putExtra("goodList", JSONObject.toJSONString(payList));
					intent.putExtra("type", 1);// 从购物车 提交订单 的标致
					startActivity(intent);
				} else {
					ShowContentUtils.showLongToastMessage(mContext, "请选择商品");
				}
			}
		}		
	}

	@Override
	public void onClickDeleteListener(final int groupPosition,
			final int childPosition) {
		MyApplication
				.myAlertDialog(mContext, true, "提示", "是否将该商品从购物车删除？",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (progress == null) {
									progress = MyProgressDialog
											.createDialog(mContext);
								}
								progress.setMessage("正在删除...");
								progress.show();
								if (http == null) {
									http = new HttpUtil(handler);
								}

								if (goodsType == 2) {
									http.sendGet(RequestCode.DELGOODS,
											WebUrlConfig.deleteGoods(
													MyApplication.loginModel
															.getUserID(),
													String.valueOf(goodsType),
													list.get(groupPosition)
															.getGoodsInfo()
															.get(childPosition)
															.getItemID()));
								} else {
									http.sendGet(RequestCode.DELGOODS,
											WebUrlConfig.deleteGoods(
													MyApplication.loginModel
															.getUserID(),
													String.valueOf(goodsType),
													foodList.get(groupPosition)
															.getFoodsInfo()
															.get(childPosition)
															.getItemID()));
								}
								dialog.dismiss();
							}
						}).create().show();

	}

	@Override
	public void onClickAddListener(int groupPosition, int childPosition) {
		if (goodsType == 2) {
			number = Integer.parseInt(list.get(groupPosition).getGoodsInfo()
					.get(childPosition).getNumber());
			number++;
			list.get(groupPosition).getGoodsInfo().get(childPosition)
					.setNumber(number + "");
			adapter.notifyDataSetChanged();
			money();
		} else {
			number = Integer.parseInt(foodList.get(groupPosition)
					.getFoodsInfo().get(childPosition).getNumber());
			number++;
			foodList.get(groupPosition).getFoodsInfo().get(childPosition)
					.setNumber(number + "");
			foodsAdapter.notifyDataSetChanged();
			money();
		}

	}

	@Override
	public void onClickReducesListener(int groupPosition, int childPosition) {
		if (goodsType == 2) {
			number = Integer.parseInt(list.get(groupPosition).getGoodsInfo()
					.get(childPosition).getNumber());
			if (number==1) {
				ShowContentUtils.showShortToastMessage(mContext, "数量最少为1");
				return;
			}
			number--;
			list.get(groupPosition).getGoodsInfo().get(childPosition)
					.setNumber(number + "");
			adapter.notifyDataSetChanged();
			money();
		} else {
			number = Integer.parseInt(foodList.get(groupPosition)
					.getFoodsInfo().get(childPosition).getNumber());
			if (number==1) {
				ShowContentUtils.showShortToastMessage(mContext, "数量最少为1");
				return;
			}
			number--;
			foodList.get(groupPosition).getFoodsInfo().get(childPosition)
					.setNumber(number + "");
			foodsAdapter.notifyDataSetChanged();
			money();
		}

	}

	@Override
	public void onChildCheckChangeListener(int groupPosition,
			int childPosition, boolean isChildCheck) {
		if (goodsType == 2) {
			if (isChildCheck) {
				list.get(groupPosition).getGoodsInfo().get(childPosition).setChecked(true);
				adapter.notifyDataSetChanged();
			} else {
				list.get(groupPosition).getGoodsInfo().get(childPosition).setChecked(false);
				adapter.notifyDataSetChanged();
			}			
			money();
		} else {
			if (isChildCheck) {
				foodList.get(groupPosition).getFoodsInfo().get(childPosition).setChecked(true);
				foodsAdapter.notifyDataSetChanged();
			} else {
				foodList.get(groupPosition).getFoodsInfo().get(childPosition).setChecked(false);				
				foodsAdapter.notifyDataSetChanged();
			}
			money();
		}

	}

	@Override
	public void onGroupCheckChangeListener(int groupPosition,
			boolean isGroupCheck) {
		if (goodsType == 2) {
			if (isGroupCheck) {
				for (int i = 0; i < list.get(groupPosition).getGoodsInfo()
						.size(); i++) {
					list.get(groupPosition).getGoodsInfo().get(i).setChecked(true);
					adapter.notifyDataSetChanged();
				}
			} else {
				for (int i = 0; i < list.get(groupPosition).getGoodsInfo()
						.size(); i++) {
					list.get(groupPosition).getGoodsInfo().get(i).setChecked(false);
					adapter.notifyDataSetChanged();
				}
			}
			money();
		} else {
			if (isGroupCheck) {
				for (int i = 0; i < foodList.get(groupPosition).getFoodsInfo().size(); i++) {
					foodList.get(groupPosition).getFoodsInfo().get(i).setChecked(true);
					foodsAdapter.notifyDataSetChanged();
				}
			} else {
				for (int i = 0; i < foodList.get(groupPosition).getFoodsInfo().size(); i++) {
					foodList.get(groupPosition).getFoodsInfo().get(i).setChecked(false);
					foodsAdapter.notifyDataSetChanged();
				}
			}			
			money();
		}
		
//		int len = -1;
//		for (int i = 0; i < foodList.size(); i++) {
//			if(foodList.get(i).isChecked()){
//				len = i;
//			}
//		}
//		if(len == foodList.size()-1){
//			shopping_list_cb.setChecked(true);
//		}else{
//			shopping_list_cb.setChecked(false);
//		}

	}
	
	private void shoppCbState(){
		
	}

	/**
	 * 算钱
	 */
	private void money() {
		if (goodsType == 2) {
			money = 0;
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.get(i).getGoodsInfo().size(); j++) {
					if (list.get(i).getGoodsInfo().get(j).isChecked()) {
						String price = list.get(i).getGoodsInfo().get(j).getGoodsPrice();
						String number = list.get(i).getGoodsInfo().get(j).getNumber();
						if(!price.equals("") && !number.equals("")){				
							money += Double.parseDouble(price)* Integer.parseInt(number);
						}
					}
				}
			}
			DecimalFormat df = new java.text.DecimalFormat("#.00");
			money = Double.parseDouble(df.format(money));
			shopping_money.setText("￥" + money);
		} else {
			money = 0;
			for (int i = 0; i < foodList.size(); i++) {
				for (int j = 0; j < foodList.get(i).getFoodsInfo().size(); j++) {
					if (foodList.get(i).getFoodsInfo().get(j).isChecked()) {
						money += Double.parseDouble(foodList.get(i)
								.getFoodsInfo().get(j).getFoodsPrice())
								* Integer.parseInt(foodList.get(i)
										.getFoodsInfo().get(j).getNumber());
					}
				}
			}
			DecimalFormat df = new java.text.DecimalFormat("#.00");
			money = Double.parseDouble(df.format(money));
			shopping_money.setText("￥" + money);
		}

	}

}

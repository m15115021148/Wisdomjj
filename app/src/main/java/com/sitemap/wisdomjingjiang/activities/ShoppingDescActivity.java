package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.AddressModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.GoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.GoodsModel;
import com.sitemap.wisdomjingjiang.share.UMShareUtil;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.CircleImageView;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author chenmeng 商品详情 的页面
 */
@ContentView(R.layout.activity_shopping_desc)
public class ShoppingDescActivity extends BaseActivity implements
		OnClickListener {
	/** 全局类 */
	private Context mContext;
	/** 返回上一层 */
	@ViewInject(R.id.back)
	private ImageView mBack;
	/** 标题栏 右侧 的更多 默认不显示 */
	@ViewInject(R.id.more)
	private ImageView mMore;
	/** 标题栏 的标题 */
	@ViewInject(R.id.title)
	private TextView mTitle;
	/** 商品图片 展示 的viewpager */
	@ViewInject(R.id.shopping_viewpager)
	private ViewPager mVp;
	/** 商品图片 相对应的导航圈 */
	@ViewInject(R.id.shopping_indictor)
	private LinearLayout dotLayout;
	/** 图片的数量以及导航圈的数量 */
	public static  int NUMBER_MAX = 1;//默认一张
	/**轮播图片的点的集合*/ 
	private List<View> dotList = new ArrayList<View>();
	/** 图片 标签 */
	public static final String TAG = "img_flag";
	/** 立即购买 */
	@ViewInject(R.id.shopping_buy)
	private TextView mBuy;
	/** 选择 颜色 尺寸 数量 */
	@ViewInject(R.id.merchant_select)
	private RelativeLayout mMerchantSelect;
	/** 店铺名称 收藏 加入购物车 */
	@ViewInject(R.id.merchant_name)
	private LinearLayout mMerchantName;
	@ViewInject(R.id.merchant_collect)
	private LinearLayout mCollect;
	/**购物车*/ 
	@ViewInject(R.id.shop_cart)
	private LinearLayout mShopCart;
	@ViewInject(R.id.merchant_add_shop_cart)
	private TextView mAddShopCart;
	/**没有评论 不显示*/ 
	@ViewInject(R.id.comment_show)
	private RelativeLayout commentShow;
	/**没有评论显示*/ 
	@ViewInject(R.id.comment_nono)
	private TextView commentNo;
	/** 是否有 点击选择 颜色 尺寸 数量 在加入购物车之前 */
	private boolean isSelected = false;
	/** 评论 */
	@ViewInject(R.id.merchant_comment_more)
	private TextView mComment;
	/** 库存量 */
	@ViewInject(R.id.foods_inventory)
	private TextView mInventory;
	/** 商品简介 */
	@ViewInject(R.id.shopping_breif)
	private TextView mBrief;
	/** 评论时间 */
	@ViewInject(R.id.user_time)
	private TextView userTime;
	private MyProgressDialog progress;// 进度条
	private HttpUtil http;// http对象
	private GoodsInfoModel model;// 商品信息

	@ViewInject(R.id.shopping_desc)
	private TextView shopping_desc;// 商品描述
	@ViewInject(R.id.shopping_money)
	private TextView shopping_money;// 商品价格
	@ViewInject(R.id.shopping_pay)
	private TextView shopping_pay;// 商品门市价
	@ViewInject(R.id.shopping_expressage_money)
	private TextView shopping_expressage_money;// 商品快递费
	@ViewInject(R.id.shopping_sell_number)
	private TextView shopping_sell_number;// 商品销量
	@ViewInject(R.id.shopping_city_name)
	private TextView shopping_city_name;// 商品产地
	@ViewInject(R.id.user_img)
	private CircleImageView userImg;// 评论头像
	@ViewInject(R.id.shopping_user_name)
	private TextView shopping_user_name;// 评论人
	@ViewInject(R.id.shopping_comment_desc)
	private TextView shopping_comment_desc;// 评论内容
	@ViewInject(R.id.content)
	private TextView content;// 用户选择的颜色、尺寸、数量

	private String price;// 商品价格
	private String mColor;// 用户选择的颜色
	private String mSize;// 用户选择的尺寸
	private String mNumber = "1";// 用户选择的数量
	private String goodsID;// 商品id
	private FragmentPagerAdapter imgAdapter;//顶部图片适配器
	private static String[] imgs = null;
	/**购物商品列表返回实体类*/ 
	private GoodsModel goodsModel;
	/** 我的收货 地址 数据集合 */
	private List<AddressModel> mAddressList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initData();// 初始化数据
//		initViewPager();// 初始化viewpager
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mBack.setOnClickListener(this);
		mMore.setOnClickListener(this);
		// 显示
		mMore.setVisibility(View.VISIBLE);
		mBuy.setOnClickListener(this);
		mMerchantSelect.setOnClickListener(this);
		mMerchantName.setOnClickListener(this);
		mCollect.setOnClickListener(this);
		mAddShopCart.setOnClickListener(this);
		mComment.setOnClickListener(this);
		mShopCart.setOnClickListener(this);
		
		//获取数据
		Intent intent = getIntent();
		goodsModel = (GoodsModel) intent.getSerializableExtra("GoodsModel");
		price = goodsModel.getGoodsPrice();
		shopping_money.setText(price);
		if(goodsModel.getSales().equals("")){
			goodsModel.setSales("0");
		}
		shopping_sell_number.setText("总销量" + goodsModel.getSales()	+ "笔");
		shopping_city_name.setText(goodsModel.getGoodsPlace());
		mTitle.setText(goodsModel.getGoodsName());
		goodsID = goodsModel.getGoodsID();
		shopping_desc.setText(goodsModel.getGoodsName());

	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		loadNet(1);
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
				if (msg.arg1 == RequestCode.GOODSLIST) {
					model = JSON.parseObject(msg.obj.toString(),
							GoodsInfoModel.class);
					updateData();
				}
				//是否已收藏
				if(msg.arg1 == RequestCode.GETISCOLLECTION){
					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {//已收藏
						mCollect.setSelected(true);
					} else {
						mCollect.setSelected(false);
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
				if (msg.arg1 == RequestCode.GOODSCAR) {

					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (model.getStatus() == 0) {
						ShowContentUtils.showLongToastMessage(mContext,
								"添加购物车成功");
					} else {
						ShowContentUtils.showLongToastMessage(mContext,
								model.getErrorMsg());
					}
				}
				// 我的收货地址
				if (msg.arg1 == RequestCode.GETADDRESS) {
					mAddressList = JSONObject.parseArray(msg.obj.toString(),
							AddressModel.class);
					if (mAddressList.size() == 0) {
						Intent intent = new Intent(mContext, AddressAddActivity.class);
						startActivity(intent);
					} else{
						//跳转到提交 购物订单页面
						Intent intent = new Intent(mContext,SubGoodsOrderActivity.class);
						Bundle b = new Bundle();
						model.setNumberInfo(null);
						b.putSerializable("GoodsInfoModel", model);
						b.putSerializable("GoodsModel", goodsModel);
						intent.putExtras(b);
						intent.putExtra("color", mColor);
						intent.putExtra("size", mSize);
						intent.putExtra("number", mNumber);
						intent.putExtra("price", price);
						startActivity(intent);
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
	 * 更新页面数据
	 */
	private void updateData() {
		mBrief.setText(model.getGoodsBrief());
		shopping_pay.setText("¥" + model.getOldPrice());
		shopping_pay.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
		shopping_expressage_money.setText("¥" + model.getExpress());
		if(model.getUserImg().equals("")){
			userImg.setBackgroundResource(R.drawable.user_comment);
		}else{
			x.image().bind(userImg, model.getUserImg(),MyApplication.imageComment);
		}
		userTime.setText(model.getCommentTime());
		shopping_user_name.setText(model.getUserName());
		shopping_comment_desc.setText("\t\t"+model.getUserComment());
		if(model.getUserComment().equals("")||model.getUserComment()==null){
			//没有评论
			commentShow.setVisibility(View.GONE);
			shopping_comment_desc.setVisibility(View.GONE);
			commentNo.setVisibility(View.VISIBLE);
			mComment.setVisibility(View.GONE);
		}
		String imgStr=model.getGoodsImg();
		if(!TextUtils.isEmpty(imgStr)){
			imgs=imgStr.split(";");//获取多张图片
			NUMBER_MAX=imgs.length;
			if(NUMBER_MAX==1){
				dotLayout.setVisibility(View.GONE);
			}
		}
		if(model.getInventory()!=null&&!model.getInventory().equals("")){
			if(model.getInventory().equals("0")){//库存不足
				mInventory.setText("库存量:已售罄");
			}else{
				mInventory.setText("库存量:"+model.getInventory()+"件");
			}
		}
		initViewPager();
		
	}
	
	/**
	 * 获取我的收货地址
	 */
	private void getAddressDesc() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progress = MyProgressDialog.createDialog(mContext);
			if (progress != null && !progress.isShowing()) {
				progress.setMessage("加载中...");
				progress.show();
			}
			String address = WebUrlConfig.getAddress(MyApplication.loginModel.getUserID());
			http.sendGet(RequestCode.GETADDRESS, address);
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	/**
	 * 请求网络数据
	 * 
	 * @param goodsID
	 * @param type
	 *            1加载商品信息，2收藏,3加入购物车
	 */

	private void loadNet(int type) {
		// 进行网路请求
		if (!MyApplication.getNetObject().isNetConnected()) {
			ShowContentUtils.showShortToastMessage(this, RequestCode.NOLOGIN);
			return;
		}
		if (progress == null) {
			progress = MyProgressDialog.createDialog(this);
		}
		progress.show();
		if (http == null) {
			http = new HttpUtil(handler);
		}
		switch (type) {
		case 1:
			// 获取是否收藏
			if (MyApplication.isLogin) {// 登录时 才有请求
				http.sendGet(
						RequestCode.GETISCOLLECTION,
						WebUrlConfig.getIsCollection(
								MyApplication.loginModel.getUserID(),
								String.valueOf(2), goodsID));
			}
			http.sendGet(RequestCode.GOODSINFO,
					WebUrlConfig.getGoodsInfo(goodsID));
			break;
		case 2:
			http.sendGet(RequestCode.FOODSHOPCOLLECT, WebUrlConfig
					.addCollectionThings(
							MyApplication.loginModel.getUserID(), goodsID,
							2 + ""));
			break;
		case 3:
			if (MyApplication.isLogin) {

				http.sendGet(RequestCode.GOODSCAR, WebUrlConfig.addGoods(
						MyApplication.loginModel.getUserID(), goodsID,
						model.getGoodShopID(), model.getGoodShopName(), mColor,
						mSize, mNumber));
			} else {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}

			break;
		default:
			break;
		}

	}
	
	/**
	 * 取消收藏
	 */
	private void cancelCollect(){
		if (MyApplication.getNetObject().isNetConnected()) {
			progress = MyProgressDialog.createDialog(this);
			if (progress != null && !progress.isShowing()) {
				progress.setMessage("加载中...");
				progress.show();
			}
			http.sendGet(
					RequestCode.CANCELCOLLECTION,
					WebUrlConfig.cancelCollection(MyApplication.loginModel.getUserID(),
							String.valueOf(2),goodsID));
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	/**
	 * 初始化viewpager
	 */
	private void initViewPager() {
		//画点
		MyApplication.drawPoint(mContext,dotLayout,dotList,NUMBER_MAX);
		 imgAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
				return NUMBER_MAX;
			}
		};

		mVp.setAdapter(imgAdapter);

		// 设置viewpager滑动事件
		mVp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
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

			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == mBack) {
			onBackPressed();
			finish();
		}
		if (v == mMore) {
			if(imgs==null||imgs.length==0){
//				ShowContentUtils.showLongToastMessage(mContext, "缺少图片不能分享");
//				return;
				UMShareUtil util = new UMShareUtil(this);
				util.shareDrawableImage(model.getGoodShopName(),model.getGoodsBrief(),
						R.drawable.icon, MyApplication.share);
			}else{
				UMShareUtil util = new UMShareUtil(this);
				util.shareNetImage(
						model.getGoodShopName(),
						model.getGoodsBrief(),imgs[0],
						MyApplication.share
						);
			}
		}
		if (v == mBuy) {// 立即购买			
			if (isSelected) {// 已经选择了颜色 尺寸 数量
				if (MyApplication.isLogin) {
					getAddressDesc();
				}else {
					Intent intent = new Intent(this, LoginActivity.class);
					startActivity(intent);
				}				
			} else {
				selectInfo(2);
			}		
		}
		if (v == mMerchantSelect) {// 选择 颜色 尺寸 数量					
			selectInfo(0);				
		}
		if (v == mMerchantName) {// 店铺名称
			if(model==null){
				return;
			}
			Intent intent = new Intent();
			intent.setClass(mContext, ShoppingListActivity.class);
			intent.putExtra("shopID", model.getGoodShopID());
			intent.putExtra("shopName", model.getGoodShopName());
			intent.putExtra("number", mNumber);
			intent.putExtra("isColl", true);
			startActivity(intent);
		}
		if (v == mCollect) {// 收藏
			//判断用户是否登录
			if(!MyApplication.isLogin){
				mCollect.setSelected(false);
				Intent intent=new Intent(mContext,LoginActivity.class);
				startActivity(intent);
				return;
			}
			if(mCollect.isSelected()){//已收藏  点击取消
				cancelCollect();
				mCollect.setSelected(false);
			}else{//未收藏 点击收藏
				loadNet(2);
				mCollect.setSelected(true);
			}				
		}
		if (v == mAddShopCart) {// 加入购物车
			if (isSelected) {// 已经选择了颜色 尺寸 数量
				loadNet(3);
			} else {
				selectInfo(1);
			}
		}
		if (v == mComment) {// 更多评论
			if (!TextUtils.isEmpty(goodsID)) {
				Intent intent = new Intent();
				intent.putExtra("goodsID", goodsID);
				intent.setClass(mContext, CommentListActivity.class);
				startActivity(intent);
			}
		}
		if(v == mShopCart){//购物车
			//判断用户是否登录
			if (MyApplication.isLogin) {
				Intent intent = new Intent(mContext,MainActivity.class);
				intent.putExtra("type", 1);
				startActivity(intent);
				MainActivity.mContext.setShopCartCheck();
			}else {
				Intent intent=new Intent(mContext,LoginActivity.class);
				startActivity(intent);
			}			
		}
	}

	/**
	 * 选择颜色和尺寸
	 * @param select 是否第一选择
	 * @param type 1、加入购物车进去  2、立即购买进入
	 */
	private void selectInfo(int type) {
		if(model==null){
			return;
		}
		if(TextUtils.isEmpty(model.getInventory())){
			ShowContentUtils.showLongToastMessage(this, "库存不足");
			return;
		}
		if(model==null){
			ShowContentUtils.showShortToastMessage(this, "请选择颜色和尺寸！");
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("color", model.getColor());
		intent.putExtra("size", model.getSize());
		intent.putExtra("price", price);		
		intent.putExtra("number", model.getInventory());
		intent.putExtra("type", type);
		if(imgs!=null&&imgs.length>0){
			intent.putExtra("img", imgs[0]);
		}
		intent.putParcelableArrayListExtra("list", model.getNumberInfo());
		intent.setClass(mContext, ShoppingImmediatelyBuyActivity.class);
		startActivityForResult(intent, 0);
		overridePendingTransition(R.anim.shopping_buy_entry,
				R.anim.shoping_desc_exit);
	}

	/**
	 * 图片viewpager页面中的子布局
	 */
	public static class MyFragment extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ImageView iv = new ImageView(inflater.getContext());
			int position = getArguments().getInt(TAG);
			iv.setScaleType(ScaleType.FIT_XY);
			if(imgs!=null){
				x.image().bind(iv,imgs[position],MyApplication.imageOptions);
			}else{
				iv.setBackgroundResource(R.drawable.tops_bg);
			}
			return iv;
		}

	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(RequestCode.GOODSLISTMORE);		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RequestCode.GOODSINFO:
			Bundle bunde = data.getExtras();
			mColor = bunde.getString("color");// 用户选择的颜色
			mSize = bunde.getString("size");// 用户选择的尺寸
			mNumber = bunde.getString("number");// 用户选择的数量			
			price = bunde.getString("price");			
			content.setText(mColor + " " + mSize + " " + mNumber + "件"+" 单价"+price+"元");
			isSelected = true;
			int type = data.getIntExtra("type", 0);
			if(type==1){//购物车
				loadNet(3);
			}else if(type==2){//立即购买
				if (MyApplication.isLogin) {
					getAddressDesc();
				}else {
					Intent intent = new Intent(this, LoginActivity.class);
					startActivity(intent);
				}
			}
			break;
		default:
			break;
		}
	}
}

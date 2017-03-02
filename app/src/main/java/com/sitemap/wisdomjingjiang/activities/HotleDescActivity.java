package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.HotleDescListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.FoodShopInfoModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.models.HotelShopRoomModel;
import com.sitemap.wisdomjingjiang.share.UMShareUtil;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author chenmeng
 *	酒店详情 页面
 */
public class HotleDescActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener{
	/**全局类*/ 
	private Context mContext;
	/**返回上一层，*/ 
	private ImageView mBack;
	/**点击收藏*/ 
	private CheckBox mCollect;
	/**分享*/ 
	private TextView mShare;
	/** 酒店图片 展示 的viewpager */
	private ViewPager mVp;
	/** 酒店图片 相对应的导航圈 */
	private LinearLayout dotLayout;
	/** 图片的数量以及导航圈的数量 */
	public static  int NUMBER_MAX = 4;
	/**轮播图片的点的集合*/ 
	private List<View> dotList = new ArrayList<View>();
	/** 图片 标签 */
	public static final String TAG = "img_flag";
	/** 酒店图片的 播放的图片 */
	public static final int[] heads_res = null;
	/**酒店详情 listview*/ 
	private ListView mLv;
	/**电话 点击事件*/ 
	private TextView mCallPhone;
	private HttpUtil http;// 网络请求
	private static MyProgressDialog progressDialog;// 进度条
	private FoodShopsModel hotelModel;//传过来的房间
	private static String[] imgList;//图片地址
	private TextView hotle_name;//酒店名称
	private RatingBar hotle_lv;//酒店评分星星
	private TextView hotle_grade;//评分
	private TextView hotle_place;//酒店地址
	private LinearLayout hotle_phone;//酒店电话
	private LinearLayout foods_ding_wei;//地图页面
	private TextView hotel_brief;//酒店简介
	private List<HotelShopRoomModel> lroom=new ArrayList<HotelShopRoomModel>();//房间列表
	private HotleDescListViewAdapter roomAdapter;//房间列表适配器
	private FoodShopInfoModel shopInfoModel;//商家详情
	private TextView hotle_preMoney;//人均消费
	private TextView title;//标题

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotle_desc);
		mContext = this;
		initView();//初始化view
		initData();//初始化数据
		
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
				if (msg.arg1 == RequestCode.GETHOTELINFO) {//酒店信息
					Log.e("TAG", "request:" + msg.obj.toString());
					shopInfoModel=JSON.parseObject(
							msg.obj.toString(), FoodShopInfoModel.class);
					imgList=shopInfoModel.getShopImg().split(";");
					NUMBER_MAX=imgList.length;
					if(NUMBER_MAX==1){
						dotLayout.setVisibility(View.GONE);
					}
					initViewPager();//初始化viewpager
					hotel_brief.setText(shopInfoModel.getBrief());
					hotle_place.setText(shopInfoModel.getAddress());
					
				}
				if (msg.arg1 == RequestCode.GETHOTELROOM) {//加载更多
					Log.i("TAG", "request:" + msg.obj.toString());
					lroom.clear();
					lroom=JSONObject.parseArray(msg.obj.toString(),
							HotelShopRoomModel.class);
					roomAdapter=new HotleDescListViewAdapter(mContext,lroom);
					mLv.setAdapter(roomAdapter);
					MyApplication.setListViewHeight(mLv);
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
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
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
		mBack = (ImageView) findViewById(R.id.hotle_desc_title).findViewById(R.id.back);
		mCollect = (CheckBox) findViewById(R.id.hotle_desc_title).findViewById(R.id.collect);
		mShare = (TextView) findViewById(R.id.hotle_desc_title).findViewById(R.id.share);
		title= (TextView) findViewById(R.id.hotle_desc_title).findViewById(R.id.title);
		mVp = (ViewPager) findViewById(R.id.hotle_viewpager);
		dotLayout = (LinearLayout) findViewById(R.id.hotle_indictor);
		mLv = (ListView) findViewById(R.id.hotle_desc_listview);
		hotle_phone = (LinearLayout) findViewById(R.id.hotle_phone);
		hotle_phone.setOnClickListener(this);
		foods_ding_wei= (LinearLayout) findViewById(R.id.foods_ding_wei);
		foods_ding_wei.setOnClickListener(this);
		hotle_name= (TextView) findViewById(R.id.hotle_name);
		hotle_grade= (TextView) findViewById(R.id.hotle_grade);
		hotle_place= (TextView) findViewById(R.id.hotle_place);
		hotle_lv= (RatingBar) findViewById(R.id.hotle_lv);
		hotel_brief= (TextView) findViewById(R.id.hotel_brief);
		hotle_preMoney= (TextView) findViewById(R.id.hotle_preMoney);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		//获得焦点
		mBack.setFocusable(true);
		mBack.setFocusableInTouchMode(true);
		mBack.requestFocus();
		mBack.requestFocusFromTouch();
//		mCollect.setOnClickListener(this);
		mCollect.setOnCheckedChangeListener(this);
		mBack.setOnClickListener(this);
		mShare.setOnClickListener(this);
		hotelModel=(FoodShopsModel) getIntent().getSerializableExtra("hotel");
		hotle_name.setText(hotelModel.getShopName());
		if (hotelModel.getShopGrade()!=null&&!hotelModel.getShopGrade().equals("")) {
			hotle_lv.setRating(Float.parseFloat(hotelModel.getShopGrade()));
			hotle_grade.setText(hotelModel.getShopGrade()+"分");
		}else {
			hotle_lv.setRating(0);
			hotle_grade.setText("暂无平分");
		}
		if (hotelModel.getPreMoney()!=null&&!hotelModel.getPreMoney().equals("")) {
			hotle_preMoney.setText(hotelModel.getPreMoney()+"元");
		}else {
			hotle_preMoney.setText("0元");
		}
		title.setText(hotelModel.getShopName());
		http = new HttpUtil(handler);
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			//获取是否收藏
			if (MyApplication.isLogin) {//登录时 才有请求
				http.sendGet(RequestCode.GETISCOLLECTION, WebUrlConfig.getIsCollection(
						MyApplication.loginModel.getUserID(), 
						String.valueOf(6), 
						hotelModel.getShopID()));
			}
			http.sendGet(
					RequestCode.GETHOTELINFO,
					WebUrlConfig.getHotelShopInfo(hotelModel.getShopID()));
			http.sendGet(
					RequestCode.GETHOTELROOM,
					WebUrlConfig.getHotelShopRoom(hotelModel.getShopID()));
		} else {
			ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	/**
	 * 初始化viewpager
	 */
	private void initViewPager() {
		//画点
		MyApplication.drawPoint(mContext,dotLayout,dotList,NUMBER_MAX);
		mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
		});
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
		if(v == mBack){//上一层
			onBackPressed();
			finish();
		}
		if(v == mShare){//分享
			if(shopInfoModel.getShopImg().split(";").length==0){
//				ShowContentUtils.showLongToastMessage(mContext, "缺少图片不能分享");
//				return;
				UMShareUtil util = new UMShareUtil(this);
				util.shareDrawableImage(hotelModel.getShopName(),shopInfoModel.getBrief(),
						R.drawable.icon, MyApplication.share);
			}
			UMShareUtil util = new UMShareUtil(this);
			util.shareNetImage(hotelModel.getShopName(),
					shopInfoModel.getBrief(), shopInfoModel.getShopImg().split(";")[0],
					MyApplication.share 
					);
		}
//		if (v==mCollect) {//收藏
//			if (!MyApplication.isLogin) {
//				ShowContentUtils.showShortToastMessage(mContext,"请先登录！");
//				Intent intent = new Intent(mContext,LoginActivity.class);
//			    startActivity(intent);
//			    return;
//			}
//			if (MyApplication.getNetObject().isNetConnected()) {
//				progressDialog = MyProgressDialog.createDialog(this);
//				if (progressDialog != null && !progressDialog.isShowing()) {
//					progressDialog.setMessage("加载中...");
//					progressDialog.show();
//				}
//				http.sendGet(
//						RequestCode.HOTLECOLLECT,
//						WebUrlConfig.addCollectionShops(MyApplication.loginModel.getUserID(),hotelModel.getShopID(),"6"));
//			} else {
//				ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
//			}
//		}
		if(v == hotle_phone){//电话
			if(shopInfoModel.getPhoneNumber() != null&&!shopInfoModel.getPhoneNumber().equals("")){
				Intent intent = new Intent();
			    intent.setAction("android.intent.action.DIAL");
			    intent.setData(Uri.parse("tel:"+shopInfoModel.getPhoneNumber()));
			    startActivity(intent);
			}else{
				ShowContentUtils.showLongToastMessage(mContext, "商家暂无电话");
			}
		}
		if (v==foods_ding_wei) {
			Intent intent=new Intent(mContext,MapActivity.class);
			if (hotelModel.getLat()!=null&&!hotelModel.getLat().equals("")) {
				intent.putExtra("lat", Double.parseDouble(hotelModel.getLat()));
				intent.putExtra("lng", Double.parseDouble(hotelModel.getLng()));
				intent.putExtra("adress", hotelModel.getShopName());
				startActivity(intent);
			}else {
				ShowContentUtils.showLongToastMessage(mContext, "商家具体位置");
			}
		}
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
			x.image().bind(iv,imgList[position],MyApplication.imageOptions);			
			return iv;
		}				
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
				progressDialog = MyProgressDialog.createDialog(mContext);
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.setMessage("加载中...");
					progressDialog.show();
				}
				http.sendGet(
						RequestCode.FOODSHOPCOLLECT,
						WebUrlConfig.addCollectionShops(MyApplication.loginModel.getUserID(),hotelModel.getShopID(),"6"));
			} else {
				ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
			}
		}else{//已收藏  点击取消收藏
			if (MyApplication.getNetObject().isNetConnected()) {
				progressDialog = MyProgressDialog.createDialog(mContext);
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.setMessage("加载中...");
					progressDialog.show();
				}
				http.sendGet(
						RequestCode.CANCELCOLLECTION,
						WebUrlConfig.cancelCollection(MyApplication.loginModel.getUserID(),
								String.valueOf(6),hotelModel.getShopID()));
			} else {
				ShowContentUtils.showShortToastMessage(mContext, RequestCode.NOLOGIN);
			}
		}		
	}

}

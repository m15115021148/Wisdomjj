package com.sitemap.wisdomjingjiang.fragments;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.activities.AddressListActivity;
import com.sitemap.wisdomjingjiang.activities.BaoliaoListActivity;
import com.sitemap.wisdomjingjiang.activities.MainActivity;
import com.sitemap.wisdomjingjiang.activities.OrderFoodsListActivity;
import com.sitemap.wisdomjingjiang.activities.OrderShopsListActivity;
import com.sitemap.wisdomjingjiang.activities.JifenActivity;
import com.sitemap.wisdomjingjiang.activities.SetActivity;
import com.sitemap.wisdomjingjiang.activities.ShoucangListActivity;
import com.sitemap.wisdomjingjiang.activities.UserActivity;
import com.sitemap.wisdomjingjiang.application.MyApplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.fragments
 * @author chenmeng
 * @Description 我的 页面
 * @date create at  2016年4月29日 下午4:23:33
 */
public class MySelfFragment extends BaseFragment implements OnClickListener{
	private View rootView;
	private RelativeLayout user_address_lay;//我的收货地址按钮
	private RelativeLayout user_shoucang_lay;//我的收藏按钮
	private RelativeLayout user_jifen_lay;//我的积分按钮
	private RelativeLayout user_set_lay;//设置页面
	/**我的购物订单按钮*/ 
	private RelativeLayout mUserOrderShops;
	/**我的团购订单按钮*/ 
	private RelativeLayout mUserOrderFoods;
	private RelativeLayout user_baoliao_lay;//我的爆料
	private RelativeLayout user;//我
	
	private TextView user_name_tv;//个人资料按钮
	private TextView user_phone_tv;//个人资料按钮
	private ImageView user_head;//个人头像
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		rootView = inflater.inflate(R.layout.my_self_fragment, (ViewGroup)getActivity().findViewById(R.id.viewpager), false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initView();
		initDate();
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (MyApplication.loginModel!=null) {
			
		
		if (MyApplication.loginModel.getImg()!=null&&!MyApplication.loginModel.getImg().equals("")) {
			x.image().bind(user_head,MyApplication.loginModel.getImg(),MyApplication.imageOptionsZ);
			Log.e("result", MyApplication.loginModel.getImg());
		}
		user_name_tv.setText(MyApplication.loginModel.getNickName());
		}else {
			MyApplication.mViewPager.setCurrentItem(0);
			MainActivity.mContext.setCheck(1);
		}
	}

	/**
	 * 初始化控件view
	 */
	public void initView() {
		user_address_lay=(RelativeLayout)rootView.findViewById(R.id.user_address_lay);
		user_address_lay.setOnClickListener(this);
		user_shoucang_lay=(RelativeLayout)rootView.findViewById(R.id.user_shoucang_lay);
		user_shoucang_lay.setOnClickListener(this);
		user_jifen_lay=(RelativeLayout)rootView.findViewById(R.id.user_jifen_lay);
		user_jifen_lay.setOnClickListener(this);
		user_set_lay=(RelativeLayout)rootView.findViewById(R.id.user_set_lay);
		user_set_lay.setOnClickListener(this);
		mUserOrderShops=(RelativeLayout)rootView.findViewById(R.id.user_dingdan_lay);
		mUserOrderShops.setOnClickListener(this);
		mUserOrderFoods = (RelativeLayout) rootView.findViewById(R.id.user_order_shops);
		mUserOrderFoods.setOnClickListener(this);
		user_baoliao_lay=(RelativeLayout)rootView.findViewById(R.id.user_baoliao_lay);
		user_baoliao_lay.setOnClickListener(this);
		
		user=(RelativeLayout)rootView.findViewById(R.id.user);
		user.setOnClickListener(this);
		
		user_head=(ImageView)rootView.findViewById(R.id.user_head);
		user_head.setOnClickListener(this);
		user_name_tv=(TextView)rootView.findViewById(R.id.user_name_tv);
		user_name_tv.setOnClickListener(this);
		user_phone_tv=(TextView)rootView.findViewById(R.id.user_phone_tv);
		user_phone_tv.setOnClickListener(this);
	}
	
	/**
	 * 数据初始化
	 */
	private void initDate(){
		user_name_tv.setText(MyApplication.loginModel.getNickName());
		user_phone_tv.setText(MyApplication.userPhone);
	}
	
	@Override
	public void onClick(View v) {
		if (v==user) {
			Intent intent=new Intent(getActivity(),UserActivity.class);
			startActivity(intent);
		}
		
		if (v==user_address_lay) {
			Intent intent=new Intent(getActivity(),AddressListActivity.class);
			startActivity(intent);
		}
		if (v==user_shoucang_lay) {
			Intent intent=new Intent(getActivity(),ShoucangListActivity.class);
			startActivity(intent);
		}
		if (v==user_jifen_lay) {
			Intent intent=new Intent(getActivity(),JifenActivity.class);
			startActivity(intent);
		}
		if (v==user_set_lay) {
			Intent intent=new Intent(getActivity(),SetActivity.class);
			startActivity(intent);
		}
		if (v==mUserOrderShops) {
			Intent intent=new Intent(getActivity(),OrderShopsListActivity.class);
			startActivity(intent);
		}
		if (v==user_head) {
			Intent intent=new Intent(getActivity(),UserActivity.class);
			startActivity(intent);
		}
		if (v==user_name_tv) {
			Intent intent=new Intent(getActivity(),UserActivity.class);
			startActivity(intent);
		}
		if (v==user_phone_tv) {
			Intent intent=new Intent(getActivity(),UserActivity.class);
			startActivity(intent);
		}
		if (v==user_baoliao_lay) {
			Intent intent=new Intent(getActivity(),BaoliaoListActivity.class);
			startActivity(intent);
		}
		if(v == mUserOrderFoods){//团购订单
			Intent intent=new Intent(getActivity(),OrderFoodsListActivity.class);
			startActivity(intent);
		}
	}
}

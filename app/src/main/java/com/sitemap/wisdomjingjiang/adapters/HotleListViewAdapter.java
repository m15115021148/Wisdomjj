package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.utils.DistanceUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 酒店页面 listview 的适配器
 * @date create at  2016年5月16日 下午2:50:13
 */
public class HotleListViewAdapter extends BaseAdapter{
	Context mContext;
	private Holder holder;
	private List<FoodShopsModel> lhotelList;;//显示的列表
	
	public HotleListViewAdapter(Context context,List<FoodShopsModel> lhotelList){
		this.mContext = context;
		this.lhotelList=lhotelList;
	}

	@Override
	public int getCount() {		
		return lhotelList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_hotle_item, null);
			holder = new Holder();
			holder.hotleImg = (ImageView) convertView.findViewById(R.id.hotle_icon);
			holder.hotleName = (TextView) convertView.findViewById(R.id.hotle_name);
			holder.hotleDistance = (TextView) convertView.findViewById(R.id.hotle_distance);
			holder.rb = (RatingBar) convertView.findViewById(R.id.hotle_lv);
			holder.hotleGrade = (TextView) convertView.findViewById(R.id.hotle_grade);
			holder.hotlePlace = (TextView) convertView.findViewById(R.id.hotle_place);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.hotleName.setText(lhotelList.get(position).getShopName());
		if(lhotelList.get(position).getShopImg().equals("")){
			holder.hotleImg.setBackgroundResource(R.drawable.tops_bg_2);
		}else{
			x.image().bind(holder.hotleImg,lhotelList.get(position).getShopImg(),MyApplication.imageOptionsZ);
		}
		if (lhotelList.get(position).getShopGrade()!=null&&!lhotelList.get(position).getShopGrade().equals("")) {
			holder.rb.setRating(Float.parseFloat(lhotelList.get(position).getShopGrade()));
			holder.hotleGrade.setText("评分："+lhotelList.get(position).getShopGrade()+"分");
		}else {
			holder.rb.setRating(0);
			holder.hotleGrade.setText("暂无平分");
		}
		
		holder.hotlePlace.setText(lhotelList.get(position).getArea());
		if (lhotelList.get(position).getLng()!=null&&!lhotelList.get(position).getLng().equals("")&&lhotelList.get(position).getLat()!=null&&!lhotelList.get(position).getLat().equals("")) {
			String distance = DistanceUtils.getDistance(MyApplication.lng, MyApplication.lat,
					Double.parseDouble(lhotelList.get(position).getLng()), Double.parseDouble(lhotelList.get(position).getLat()));
			holder.hotleDistance.setText(distance+"km");
		}else {
			holder.hotleDistance.setText("0km");
		}
		
		return convertView;
	}
	
	private class Holder{
		ImageView hotleImg;//图片
		//酒店名称 距离，评分等级 ，酒店地址
		TextView hotleName,hotleDistance,hotleGrade,hotlePlace;
		RatingBar rb;//酒店等级 
	}

}

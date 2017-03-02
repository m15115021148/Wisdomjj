package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.utils.DistanceUtil;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.models.FoodsModels;
import com.sitemap.wisdomjingjiang.utils.DistanceUtils;
import com.sitemap.wisdomjingjiang.utils.FileUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.adapters
 * 
 * @author zhang
 * @Description
 * @date create at 2016年5月11日 下午15:59:14
 */
public class FoodsShopListViewAdapter extends BaseAdapter {
	Context mContext;
	List<FoodShopsModel> mList;
	private Holder holder;
	/** 当前 所在的经度 */
	private String mLng;
	/** 当前 所在的纬度 */
	private String mLat;

	public FoodsShopListViewAdapter(Context context, List<FoodShopsModel> list,
			String lng, String lat) {
		this.mContext = context;
		this.mList = list;
		this.mLng = lng;
		this.mLat = lat;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.lv_home_foods_item, null);
			holder = new Holder();
			holder.img = (ImageView) convertView.findViewById(R.id.foods_icon);
			holder.foodStore = (TextView) convertView
					.findViewById(R.id.foods_store);
			holder.foodLv = (RatingBar) convertView.findViewById(R.id.foods_lv);
			holder.foodGrade = (TextView) convertView
					.findViewById(R.id.foods_grade);
			holder.foodName = (TextView) convertView
					.findViewById(R.id.foods_name);
			holder.foodPlace = (TextView) convertView
					.findViewById(R.id.foods_place);
			holder.foodDistance = (TextView) convertView
					.findViewById(R.id.foods_distance);
			holder.tuijian= (TextView) convertView
					.findViewById(R.id.tuijian);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		FoodShopsModel model = mList.get(position);

		// 计算距离
		String lng = model.getLng();// 商圈的经度
		String lat = model.getLat();// 商圈的纬度
		
		if(lng.equals("") && lng==null){
			lng = "0";
		}
		if (lat.equals("") && lat==null) {			
			lat = "0";
		}

		String distance = DistanceUtils.getDistance(Double.parseDouble(mLng),
				Double.parseDouble(mLat), Double.parseDouble(lng),
				Double.parseDouble(lat));
		holder.foodStore.setText(model.getShopName());
		if(TextUtils.isEmpty(model.getLowPrice())){
			holder.foodName.setText("最低价：暂无信息");
		}else{
			holder.foodName.setText("最低价：￥"+model.getLowPrice());
		}
		holder.foodPlace.setText(model.getArea());
		holder.tuijian.setText(model.getGoodsName());
		if(mLng.equals("0.0")&&mLat.equals("0.0")){
			holder.foodDistance.setText("");
		}else{
			holder.foodDistance.setText(distance + "km");
		}
		
		
		if(model.getShopGrade().equals("")){
			holder.foodGrade.setText("暂无评分");
			holder.foodLv.setRating(0);
		}else{
			holder.foodGrade.setText("评分："+model.getShopGrade()+"分");
			float grade = Float.parseFloat(model.getShopGrade());
			holder.foodLv.setRating(grade);
		}
		
		if(model.getShopImg().equals("")){
			holder.img.setBackgroundResource(R.drawable.tops_bg_2);
		}else{
			x.image().bind(holder.img, model.getShopImg(),
					MyApplication.imageOptionsZ);
		}

		return convertView;
	}

	/**
	 * 优化类 com.sitemap.wisdomjingjiang.adapters.Holder
	 * 
	 * @author zhang create at 2016年5月12日 上午10:49:56
	 */
	private class Holder {
		ImageView img;// 团购 图片
		// 店名，团购名称，地点，距离
		TextView foodStore, foodName, foodPlace, foodDistance;
		TextView foodGrade,tuijian;// 评星等级
		RatingBar foodLv;// 星星数量
	}

}

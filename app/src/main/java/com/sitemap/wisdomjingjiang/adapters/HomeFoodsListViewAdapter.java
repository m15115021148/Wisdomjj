package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.models.FoodsModels;
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
 * @Description 团购listview 的适配器
 * @date create at  2016年5月10日 下午12:59:14
 */
public class HomeFoodsListViewAdapter extends BaseAdapter{
	Context mContext;
	/**团购的list集合*/ 
	List<FoodShopsModel> mList;
	/**当前 所在的经度*/ 
	private String mLng;
	/**当前 所在的纬度*/ 
	private String mLat;
	
	private Holder holder;
	
	public HomeFoodsListViewAdapter(Context context,
			List<FoodShopsModel> list,String lng,String lat){
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
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_home_foods_item, null);
			holder = new Holder();
			holder.img = (ImageView) convertView.findViewById(R.id.foods_icon);
			holder.foodStore = (TextView) convertView.findViewById(R.id.foods_store);
			holder.foodLv = (RatingBar) convertView.findViewById(R.id.foods_lv);
			holder.foodGrade = (TextView) convertView.findViewById(R.id.foods_grade);
			holder.foodName = (TextView) convertView.findViewById(R.id.foods_name);
			holder.foodPlace = (TextView) convertView.findViewById(R.id.foods_place);
			holder.foodDistance = (TextView) convertView.findViewById(R.id.foods_distance);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		FoodShopsModel model = mList.get(position);
		
		//计算距离		
		String lng = model.getLng();//商圈的经度
		String lat = model.getLat();//商圈的纬度
		if(lng.equals("") || lat.equals("")){
			lng = "0";
			lat = "120";
		}
		
		String distance = DistanceUtils.getDistance(Double.parseDouble(mLng), Double.parseDouble(mLat),
							Double.parseDouble(lng), Double.parseDouble(lat));
		holder.foodStore.setText(model.getShopName());
		holder.foodLv.setRating(5);
		holder.foodGrade.setText(model.getShopGrade());
		holder.foodName.setText(model.getShopType());
		holder.foodPlace.setText(model.getArea());
		holder.foodDistance.setText(distance+"km");
		String t = model.getShopImg();
		if(model.getShopImg().equals("")){
			holder.img.setBackgroundResource(R.drawable.tops_v_bg);
		}else{
			x.image().bind(holder.img, 
					model.getShopImg()
//					"http://192.168.1.107:8080/WisdomJingJiangAdmin//upload/business/6c1a5341927242fbaeeafcf4797263a7.jpg"
					);			
		}
		
		return convertView;
	}
	
	private class Holder{
		ImageView img;//团购 图片
		//店名，团购名称，地点，距离
		TextView foodStore,foodName,foodPlace,foodDistance;
		TextView foodGrade;//评星等级
		RatingBar foodLv;//星星数量
	}

}

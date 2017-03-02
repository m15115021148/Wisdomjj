package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.SearchResultModel;
import com.sitemap.wisdomjingjiang.utils.DistanceUtils;

/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 搜索结果  适配器
 * @date create at  2016年5月31日 下午4:37:12
 */
public class SearchResultAdapter extends BaseAdapter{
	Context mContext;
	List<SearchResultModel> mList;
	private Holder holder;
	/** 当前 所在的经度 */
	private String mLng;
	/** 当前 所在的纬度 */
	private String mLat;
	
	public SearchResultAdapter(Context context,
			List<SearchResultModel> list,
			String lng, String lat){
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_search_result_item, null);
			holder = new Holder();
			holder.img = (ImageView) convertView.findViewById(R.id.search_result_img);
			holder.type = (TextView) convertView.findViewById(R.id.search_result_type);
			holder.grade = (TextView) convertView.findViewById(R.id.search_result_grade);
			holder.name = (TextView) convertView.findViewById(R.id.search_result_name);
			holder.address = (TextView) convertView.findViewById(R.id.search_result_address);
			holder.distance = (TextView) convertView.findViewById(R.id.search_result_distance);
			holder.tb = (RatingBar) convertView.findViewById(R.id.search_result_lv);
			holder.tuijian= (TextView) convertView.findViewById(R.id.tuijian);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		SearchResultModel model = mList.get(position);
		x.image().bind(holder.img, model.getImg(),MyApplication.imageOptionsZ);
		if(model.getGrade().equals("")){
			holder.grade.setText("暂无评分");
			holder.tb.setRating(0);
		}else{
			holder.grade.setText("评分："+model.getGrade()+"分");
			holder.tb.setRating(Float.parseFloat(model.getGrade()));
		}
		
		holder.type.setText(model.getName());
		holder.address.setText(model.getAddress());
		
		// 计算距离
		String lng = model.getLng();// 商圈的经度
		String lat = model.getLat();// 商圈的纬度
		if (lng.equals("") || lat.equals("")) {
			lng = "0";
			lat = "120";
			holder.distance.setVisibility(View.GONE);
		}

		String distance = DistanceUtils.getDistance(Double.parseDouble(mLng),
				Double.parseDouble(mLat), Double.parseDouble(lng),
				Double.parseDouble(lat));
		holder.distance.setText(distance+"km");
		switch (Integer.parseInt(model.getType())) {
		case 1://团购商家
			holder.name.setText("团购商家");
			holder.tuijian.setText(model.getGoodsName());
			break;
		case 2://购物商家
			holder.name.setText("购物商家");		
			break;
		case 3://购物商品
			holder.name.setText("购物商品");
			break;
		case 4://团购商品
			holder.name.setText("团购商品");
			break;
		case 5://KTV
			holder.name.setText("KTV");
			break;
		case 6://酒店
			holder.name.setText("酒店");
			break;
		default:
			break;
		}
		
		return convertView;
	}
	
	private class Holder{
		/**图片*/ 
		ImageView img;
		/**类别 评分  名称  地址  距离*/ 
		TextView type,grade,name,address,distance,tuijian;
		RatingBar tb;
	}

}

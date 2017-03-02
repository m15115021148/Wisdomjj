package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.CollectionThingsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.models.FoodsModels;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.adapters
 * 
 * @author zhang
 * @Description
 * @date create at 2016年5月11日 下午9:59:14
 */
public class ShoucangShopsListViewAdapter extends BaseAdapter {
	Context mContext;
	private Holder holder;
	List<FoodShopsModel> mList;
	private int type;// 类型

	public ShoucangShopsListViewAdapter(Context context,
			List<FoodShopsModel> list, int type) {
		this.mContext = context;
		this.mList = list;
		this.type = type;
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
					R.layout.shoucang_list_item, null);
			holder = new Holder();
			holder.img = (ImageView) convertView
					.findViewById(R.id.collect_icon);
			holder.shops = (TextView) convertView
					.findViewById(R.id.collect_store);
			holder.grade = (TextView) convertView
					.findViewById(R.id.collect_grade);
			holder.foods = (TextView) convertView
					.findViewById(R.id.collect_name);
			holder.place = (TextView) convertView
					.findViewById(R.id.collect_place);
			holder.old_price = (TextView) convertView
					.findViewById(R.id.old_price);
			holder.new_price = (TextView) convertView
					.findViewById(R.id.new_price);
			holder.room_sales = (TextView) convertView
					.findViewById(R.id.room_sales);
			holder.shop_type = (TextView) convertView
					.findViewById(R.id.shop_type);
			holder.distance = (TextView) convertView
					.findViewById(R.id.collect_distance);
			holder.rb = (RatingBar) convertView.findViewById(R.id.collect_lv);
			holder.shop_mid = (RelativeLayout) convertView
					.findViewById(R.id.shop_mid);
			holder.l1 = (RelativeLayout) convertView.findViewById(R.id.l1);
			holder.l2 = (RelativeLayout) convertView.findViewById(R.id.l2);
			holder.goods_mid = (LinearLayout) convertView
					.findViewById(R.id.goods_mid);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		FoodShopsModel model = mList.get(position);
		x.image().bind(holder.img, model.getShopImg(),
				MyApplication.imageOptionsZ);
		holder.shops.setText(model.getShopName());
		if (type == 1) {
		} else {
			holder.place.setVisibility(View.GONE);
			holder.goods_mid.setVisibility(View.GONE);
			holder.l2.setVisibility(View.GONE);
			holder.l1.setVisibility(View.VISIBLE);
			holder.shop_mid.setVisibility(View.VISIBLE);
			if (model.getShopGrade() != null
					&& !model.getShopGrade().equals("")) {
				holder.rb.setRating(Float.parseFloat(model.getShopGrade()));
				holder.grade.setText("评分："+model.getShopGrade() + "分");
			} else {
				holder.rb.setRating(0);
				holder.grade.setText("暂无平分");
			}
			switch (Integer.parseInt(model.getShopType())) {
			case 3:
				holder.shop_type.setText("购物商家");
				break;
			case 4:
				holder.shop_type.setText("团购商家");
				break;
			case 5:
				holder.shop_type.setText("KTV商家");
				break;
			case 6:
				holder.shop_type.setText("酒店");
				break;
			default:
				break;
			}
			if (model.getPreMoney()!=null&&!model.getPreMoney().equals("")) {
				holder.foods.setText("人均消费："+model.getPreMoney()+"元");
			}else {
				holder.foods.setText("人均消费：0元");
			}
			holder.distance.setText(model.getArea());
		}
		// holder.grade.setText(model.get);

		return convertView;
	}

	/**
	 * 优化类 com.sitemap.wisdomjingjiang.adapters.Holder
	 * 
	 * @author zhang create at 2016年5月13日 上午9:59:56
	 */
	private class Holder {
		/** 图片 */
		ImageView img;
		/** 商家名称 评分 商品 地点 距离 */
		TextView shops, grade, foods, place, distance, old_price, new_price,
				room_sales, shop_type;
		/** 星级数量 */
		RatingBar rb;
		RelativeLayout shop_mid, l1, l2;// 中部和底部布局
		LinearLayout goods_mid;// 中部布局
	}

}

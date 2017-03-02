package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.CollectionThingsModel;
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
 * @author zhang
 * @Description 
 * @date create at  2016年5月11日 下午9:59:14
 */
public class ShoucangListViewAdapter extends BaseAdapter{
	Context mContext;
	private Holder holder;
	List<CollectionThingsModel> mList;
	private int type;//类型
	
	public ShoucangListViewAdapter(Context context,List<CollectionThingsModel> list,int type){
		this.mContext = context;
		this.mList = list;
		this.type=type;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.shoucang_list_item, null);
			holder = new Holder();
			holder.img = (ImageView) convertView.findViewById(R.id.collect_icon);
			holder.shops = (TextView) convertView.findViewById(R.id.collect_store);
			holder.grade = (TextView) convertView.findViewById(R.id.collect_grade);
			holder.foods = (TextView) convertView.findViewById(R.id.collect_name);
			holder.place = (TextView) convertView.findViewById(R.id.collect_place);
			holder.old_price = (TextView) convertView.findViewById(R.id.old_price);
			holder.new_price = (TextView) convertView.findViewById(R.id.new_price);
			holder.room_sales = (TextView) convertView.findViewById(R.id.room_sales);
			holder.goods_type = (TextView) convertView.findViewById(R.id.type);
			holder.distance = (TextView) convertView.findViewById(R.id.collect_distance);
			holder.rb = (RatingBar) convertView.findViewById(R.id.collect_lv);
			holder.shop_mid = (RelativeLayout) convertView.findViewById(R.id.shop_mid);
			holder.l1 = (RelativeLayout) convertView.findViewById(R.id.l1);
			holder.l2 = (RelativeLayout) convertView.findViewById(R.id.l2);
			holder.goods_mid = (LinearLayout) convertView.findViewById(R.id.goods_mid);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		CollectionThingsModel model = mList.get(position);
		x.image().bind(holder.img, model.getThingImg(),MyApplication.imageOptionsZ);
		holder.shops.setText(model.getThingName());
		if (type==1) {
			holder.goods_mid.setVisibility(View.VISIBLE);
			holder.l2.setVisibility(View.VISIBLE);
			holder.l1.setVisibility(View.GONE);
			holder.shop_mid.setVisibility(View.GONE);
			holder.old_price.setText("￥"+model.getOldPrice());
			holder.new_price.setText("￥"+model.getNowPrice());
			if (model.getSales()!=null&&!model.getSales().equals("")) {
				holder.room_sales.setText("销量："+model.getSales());
			}else {
				holder.room_sales.setText("销量：0");
			}
			if (model.getThingType().equals("1")) {
				holder.goods_type.setText("团购商品");
			}else {
				holder.goods_type.setText("购物商品");
			}
			holder.old_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
		}else {
			holder.goods_mid.setVisibility(View.GONE);
			holder.l2.setVisibility(View.GONE);
			holder.l1.setVisibility(View.VISIBLE);
			holder.shop_mid.setVisibility(View.VISIBLE);
		}
//		holder.grade.setText(model.get);
		
		return convertView;
	}
	/**
	 * 优化类
	 * com.sitemap.wisdomjingjiang.adapters.Holder
	 * @author zhang
	 * create at 2016年5月13日 上午9:59:56
	 */
	private class Holder{
		/**图片*/ 
		ImageView img;
		/**商家名称 评分  商品  地点  距离*/ 
		TextView shops,grade,foods,place,distance,old_price,new_price,room_sales,goods_type;
		/**星级数量*/ 
		RatingBar rb;
		RelativeLayout shop_mid,l1,l2;//中部和底部布局
		LinearLayout goods_mid;//中部布局
	}

}

package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopInfoModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsTypeFoodsModel;
import com.sitemap.wisdomjingjiang.models.FoodsModels;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author zhang
 * @Description 
 * @date create at  2016年5月11日 下午15:59:14
 */
public class FoodsTypeListViewAdapter extends BaseAdapter{
	Context mContext;
	List<FoodShopsTypeFoodsModel> mList;//团购详情里面的推荐商品
	private Holder holder;
	
	public FoodsTypeListViewAdapter(Context context,List<FoodShopsTypeFoodsModel> list){
		this.mContext = context;
		this.mList = list;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.food_list_item, null);
			holder = new Holder();
			holder.img = (ImageView) convertView.findViewById(R.id.foods_icon);
			holder.foodName = (TextView) convertView.findViewById(R.id.foods_name);
			holder.foodNowPrice = (TextView) convertView.findViewById(R.id.foods_now_price);
			holder.foodOldPrice = (TextView) convertView.findViewById(R.id.foods_old_price);
			holder.foodSales = (TextView) convertView.findViewById(R.id.foods_sales);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.foodName.setText(mList.get(position).getFoodName());
		holder.foodNowPrice.setText("￥"+mList.get(position).getNowPrice());
		holder.foodOldPrice.setText("￥"+mList.get(position).getOldPrice());
		holder.foodSales.setText("总销量"+mList.get(position).getSales()+"件");
		if(mList.get(position).getFoodImg().equals("")){
			holder.img.setBackgroundResource(R.drawable.tops_bg_2);
		}else{
			x.image().bind(holder.img, mList.get(position).getFoodImg(),MyApplication.imageOptionsZ);
		}
		holder.foodOldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
		
		return convertView;
	}
	
	/**
	 * 优化类
	 * com.sitemap.wisdomjingjiang.adapters.Holder
	 * @author zhang
	 * create at 2016年5月12日 上午10:49:56
	 */
	private class Holder{
		ImageView img;//团购 图片
		//店名，团购名称，地点，距离
		TextView foodName,foodNowPrice,foodOldPrice;
		TextView foodSales;//评星等级
	}

}

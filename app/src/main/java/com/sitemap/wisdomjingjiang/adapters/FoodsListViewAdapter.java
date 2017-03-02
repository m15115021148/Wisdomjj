package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopInfoModel;
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
public class FoodsListViewAdapter extends BaseAdapter{
	Context mContext;
	List<FoodShopFoodsModel> mList;
	private Holder holder;
	
	public FoodsListViewAdapter(Context context,List<FoodShopFoodsModel> list){
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
		FoodShopFoodsModel model = mList.get(position);
		
		holder.foodName.setText(model.getFoodName());
		holder.foodNowPrice.setText("￥"+model.getNowPrice());
		holder.foodOldPrice.setText("￥"+model.getOldPrice());
		holder.foodSales.setText("总销量"+model.getSales()+"件");
		if(model.getFoodImg().equals("")){
			holder.img.setBackgroundResource(R.drawable.tops_bg_2);
		}else{
			x.image().bind(holder.img, model.getFoodImg(),MyApplication.imageOptionsZ);
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

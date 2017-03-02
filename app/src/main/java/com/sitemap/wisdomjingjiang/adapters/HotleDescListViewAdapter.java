package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.HotelShopRoomModel;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 酒店详情 页面  listview 适配器
 * @date create at  2016年5月16日 下午4:23:30
 */
public class HotleDescListViewAdapter extends BaseAdapter{
	Context mContext;
	private Holder holder;
	private List<HotelShopRoomModel> lroom;//传来的集合
	
	public HotleDescListViewAdapter(Context context,List<HotelShopRoomModel> lroom){
		this.mContext = context;
		this.lroom=lroom;
	}

	@Override
	public int getCount() {
		return lroom.size();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_hotle_desc_item, null);
			holder = new Holder();
			holder.room_title = (TextView) convertView.findViewById(R.id.room_title);
			holder.old_price = (TextView) convertView.findViewById(R.id.old_price);
			holder.new_price = (TextView) convertView.findViewById(R.id.new_price);
			holder.room_sales = (TextView) convertView.findViewById(R.id.room_sales);
			holder.room_icon = (ImageView) convertView.findViewById(R.id.room_icon);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.room_title.setText(lroom.get(position).getRoomName());
		if (lroom.get(position).getOldPrice()!=null) {
			holder.old_price.setText("￥"+lroom.get(position).getOldPrice());
			
		}
		holder.old_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
		holder.new_price.setText("￥"+lroom.get(position).getNowPrice());
		holder.room_sales.setText("销量："+lroom.get(position).getSales());
		x.image().bind(holder.room_icon, lroom.get(position).getRoomImg(),MyApplication.imageOptions);
		return convertView;
	}
	
	private class Holder{
		/**房间名称 原价 现价 销量*/ 
		TextView room_title,old_price,new_price,room_sales;
		ImageView room_icon;
		
	}

}

package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 订单 购物详情 列表 适配器
 * @date create at  2016年6月14日 上午9:00:52
 */
public class OrderGoodsDescListAdapter extends BaseAdapter{
	Context mContext;
	List<CartGoodsInfoModel> mList;
	private Holder holder;
	
	public OrderGoodsDescListAdapter(Context context,
			List<CartGoodsInfoModel> list){
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
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.lv_order_goods_desc_items, null);
			holder = new Holder();
			holder.shopsName = (TextView) convertView.findViewById(R.id.order_shops_name);
			holder.shopsNum = (TextView) convertView.findViewById(R.id.order_number);
			holder.shopsColor = (TextView) convertView.findViewById(R.id.order_color);
			holder.shopsSize = (TextView) convertView.findViewById(R.id.order_size);
			holder.shopsPrice = (TextView) convertView.findViewById(R.id.order_price);
			holder.shopsImg = (ImageView) convertView.findViewById(R.id.order_img);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		CartGoodsInfoModel model = mList.get(position);
		x.image().bind(holder.shopsImg, model.getGoodsImg(),MyApplication.imageOptionsZ);
		holder.shopsName.setText(model.getGoodsName());
		holder.shopsNum.setText("X"+model.getNumber());
		holder.shopsColor.setText(model.getColor());
		holder.shopsSize.setText(model.getSize());
		if(model.getGoodsPrice().equals("")){
			holder.shopsPrice.setText("￥0.00");
		}else{
			holder.shopsPrice.setText("￥"+model.getGoodsPrice());
		}
		return convertView;
	}
	
	private class Holder{
		/**商品名称  数据  颜色  尺寸  价格*/ 
		TextView shopsName,shopsNum,shopsColor,shopsSize,shopsPrice;
		ImageView shopsImg;
	}

}

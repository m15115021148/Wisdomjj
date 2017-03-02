package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;

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
 * @Description 团购详情  列表 适配器
 * @date create at  2016年6月14日 上午9:50:53
 */
public class OrderFoodsDescListAdapter extends BaseAdapter{
	Context mContext; 
	List<CartFoodsInfoModel> mList;
	private Holder holder;

	public OrderFoodsDescListAdapter(Context context,
			List<CartFoodsInfoModel> list){
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
					R.layout.lv_order_foods_desc_items, null);
			holder = new Holder();
			holder.shopsName = (TextView) convertView.findViewById(R.id.order_shops_name);
			holder.shopsNum = (TextView) convertView.findViewById(R.id.order_number);
			holder.shopsPrice = (TextView) convertView.findViewById(R.id.order_price);
			holder.shopsImg = (ImageView) convertView.findViewById(R.id.order_img);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		CartFoodsInfoModel model = mList.get(position);
		x.image().bind(holder.shopsImg, model.getFoodsImg(),MyApplication.imageOptionsZ);
		holder.shopsName.setText(model.getFoodsName());
		holder.shopsNum.setText("X"+model.getNumber());

		if(model.getFoodsPrice().equals("")){
			holder.shopsPrice.setText("￥0.00");
		}else{
			holder.shopsPrice.setText("￥"+model.getFoodsPrice());
		}
		return convertView;
	}

	private class Holder{
		/**商品名称  数据  颜色  尺寸  价格*/ 
		TextView shopsName,shopsNum,shopsPrice;
		ImageView shopsImg;
	}
}

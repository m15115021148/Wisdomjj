package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import com.sitemap.wisdomjingjiang.R;

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
 * @Description 附近商圈 列表 页面 的listview 适配器
 * @date create at  2016年5月16日 下午1:38:47
 */
public class MerchantListListViewAdapter extends BaseAdapter{
	Context mContext;
	/**团购的list集合*/ 
	List<String> mList;
	private Holder holder;
	
	public MerchantListListViewAdapter(Context context){
		this.mContext = context;
		mList = new ArrayList<String>();
		for(int i=0;i<12;i++){
			mList.add("焖鲜汇(桂林路店"+i+")");
		}
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_merchant_list_item, null);
			holder = new Holder();
			holder.mcImg = (ImageView) convertView.findViewById(R.id.merchant_list_icon);
			holder.mcName = (TextView) convertView.findViewById(R.id.merchant_list_name);
			holder.mcDistance = (TextView) convertView.findViewById(R.id.merchant_list_distance);
			holder.rb = (RatingBar) convertView.findViewById(R.id.merchant_list_lv);
			holder.mcGrade = (TextView) convertView.findViewById(R.id.merchant_list_grade);
			holder.mcPlace = (TextView) convertView.findViewById(R.id.merchant_list_place);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		String str = mList.get(position);
		holder.mcName.setText(str);
		
		if(position/2==0){
			holder.mcGrade.setText("4.8分");
			holder.mcDistance.setText("2.6km");
			holder.rb.setRating(5);
		}
		
		return convertView;
	}
	
	private class Holder{
		ImageView mcImg;//图片
		//商家名称 距离，评分等级 ，商家地址
		TextView mcName,mcDistance,mcGrade,mcPlace;
		RatingBar rb;//商家等级 
	}

}

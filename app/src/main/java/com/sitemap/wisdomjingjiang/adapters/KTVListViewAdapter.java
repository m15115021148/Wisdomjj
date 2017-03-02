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
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description KTV页面 listview 适配器
 * @date create at  2016年5月16日 下午5:12:59
 */
public class KTVListViewAdapter extends BaseAdapter{
	Context mContext;
	/**团购的list集合*/ 
	List<String> mList;
	private Holder holder;
	
	public KTVListViewAdapter(Context context){
		this.mContext = context;
		mList = new ArrayList<String>();
		for(int i=0;i<12;i++){
			mList.add("天天KTV"+i);
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_ktv_item, null);
			holder = new Holder();
			holder.ktvImg = (ImageView) convertView.findViewById(R.id.ktv_icon);
			holder.ktvName = (TextView) convertView.findViewById(R.id.ktv_name);
			holder.ktvDistance = (TextView) convertView.findViewById(R.id.ktv_distance);
			holder.rb = (RatingBar) convertView.findViewById(R.id.ktv_lv);
			holder.ktvGrade = (TextView) convertView.findViewById(R.id.ktv_grade);
			holder.ktvPlace = (TextView) convertView.findViewById(R.id.ktv_place);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		String str = mList.get(position);
		holder.ktvName.setText(str);
		
		return convertView;
	}
	
	private class Holder{
		ImageView ktvImg;//图片
		//ktv名称 距离，评分等级 ，ktv地址
		TextView ktvName,ktvDistance,ktvGrade,ktvPlace;
		RatingBar rb;//ktv等级 
	}
}

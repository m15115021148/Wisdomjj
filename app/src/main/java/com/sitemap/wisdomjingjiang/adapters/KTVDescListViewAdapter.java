package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import com.sitemap.wisdomjingjiang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description ktv 详情页面 listview 适配器
 * @date create at  2016年5月16日 下午5:40:55
 */
public class KTVDescListViewAdapter extends BaseAdapter{
	Context mContext;
	/**数据*/ 
	List<String> mList;
	private Holder holder;
	
	public KTVDescListViewAdapter(Context context){
		this.mContext = context;
		mList = new ArrayList<String>();
		for(int i=0;i<6;i++){
			mList.add("ktv信息"+i);
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_ktv_desc_item, null);
			holder = new Holder();
			holder.time = (TextView) convertView.findViewById(R.id.ktv_time);
			holder.week = (TextView) convertView.findViewById(R.id.ktv_week);
			holder.money = (TextView) convertView.findViewById(R.id.ktv_money);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		String string = mList.get(position);
		holder.time.setText(string);
		
		return convertView;
	}
	
	private class Holder{
		/**信息 时间 钱*/ 
		TextView time,week,money;
	}


}

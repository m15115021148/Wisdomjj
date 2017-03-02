package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.models.AreaModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author 
 * @Description 
 * @date create at  2016年5月26日 下午3:07:51
 */
public class SpinnerAreaAdapter extends BaseAdapter{
	private Context context;
	private List<AreaModel> mList;
	private Holder holder;

	public SpinnerAreaAdapter(Context context, List<AreaModel> myList) {
		this.context = context;
		this.mList = myList;
	}
	
	/**
	 * 设置传参时  默认选择
	 * @param value
	 * @return
	 */
	public int selectPosition(String value){
		int position;
		for(int i=0;i<mList.size();i++){
	        if(value.equals(mList.get(i).getArea())){
	        	position = i;
	        	return position;
	        }
		}		
		return 0;
	}

	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.address_province_item, null);
			holder = new Holder();
			holder.name = (TextView) convertView.findViewById(R.id.province);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.name.setText(mList.get(position).getArea());
		
		return convertView;
	}

	private class Holder{
		TextView name;
	}

}

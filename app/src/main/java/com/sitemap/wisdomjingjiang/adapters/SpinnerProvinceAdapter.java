package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import com.sitemap.wisdomjingjiang.models.ProvinceModel;

import com.sitemap.wisdomjingjiang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.views
 * 
 * @author chenmeng
 * @Description 省 适配器
 * @date create at 2016年5月26日 下午2:33:15
 */
public class SpinnerProvinceAdapter extends BaseAdapter {
	private Context context;
	private List<ProvinceModel> mList;
	private Holder holder;

	public SpinnerProvinceAdapter(Context context, List<ProvinceModel> myList) {
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
	        if(value.equals(mList.get(i).getProvince())){
	        	position = i;
	        	return position;
	        }
		}		
		return 0;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.address_province_item, null);
			holder = new Holder();
			holder.name = (TextView) convertView.findViewById(R.id.province);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.name.setText(mList.get(position).getProvince());
		
		return convertView;
	}

	private class Holder{
		TextView name;
	}

}

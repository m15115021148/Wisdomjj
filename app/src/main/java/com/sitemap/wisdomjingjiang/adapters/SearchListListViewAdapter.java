package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.models.SearchIndexModel;

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
 * @author chenmeng
 * @Description 搜索列表 的listview 的适配器
 * @date create at  2016年5月11日 下午3:07:49
 */
public class SearchListListViewAdapter extends BaseAdapter {
	Context mContext;
	/**数据 的list*/ 
	List<SearchIndexModel> mList;
	private Holder holder;
	
	public SearchListListViewAdapter(Context context,List<SearchIndexModel> list){
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_search_list_item, null);
			holder = new Holder();
			holder.searchList = (TextView) convertView.findViewById(R.id.search_list);
			holder.searchResult = (TextView) convertView.findViewById(R.id.search_result);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.searchList.setText(mList.get(position).getName());
		holder.searchResult.setText("约"+mList.get(position).getNumber()+"个结果");
		
		return convertView;
	}
	
	private class Holder{
		//内容  结果数量
		TextView searchList,searchResult;
	}

}

package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.models.LookNewsModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author zhang
 * @Description 
 * @date create at  2016年5月11日 下午9:59:14
 */
public class BaoliaoListViewAdapter extends BaseAdapter{
	Context mContext;
	private Holder holder;
	private List<LookNewsModel> list;
	public BaoliaoListViewAdapter(Context context,List<LookNewsModel> list){
		this.mContext = context;
		this.list=list;
	}

	@Override
	public int getCount() {
		return list.size();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.baoliao_list_item, null);
			holder = new Holder();
			x.view().inject(holder, convertView);// 注解绑定
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.title.setText(list.get(position).getTitle());
		
		holder.time.setText(list.get(position).getCreateTime());
		String type=list.get(position).getStatus();
		if("1".equals(type)){
			holder.status.setText("审核中");
		}else if("2".equals(type)){
			holder.status.setText("审核成功");
		}else {
			holder.status.setText("审核失败");
		}
	
		return convertView;
	}
	/**
	 * 优化类
	 * com.sitemap.wisdomjingjiang.adapters.Holder
	 * @author zhang
	 * create at 2016年5月13日 上午9:59:56
	 */
	private class Holder{
		@ViewInject(R.id.title)
		TextView title;
		@ViewInject(R.id.time)
		TextView time;
		@ViewInject(R.id.status)
		TextView status;
	}

}

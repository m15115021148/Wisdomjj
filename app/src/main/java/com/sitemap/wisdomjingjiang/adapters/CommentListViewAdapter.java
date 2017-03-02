package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.FoodCommentsModel;
import com.sitemap.wisdomjingjiang.views.CircleImageView;

import android.content.Context;
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
 * @Description 评论页面 listview 适配器
 * @date create at  2016年5月19日 上午10:50:08
 */
public class CommentListViewAdapter extends BaseAdapter {
	Context mContext;
	List<FoodCommentsModel> mList;
	private Holder holder;
	
	public CommentListViewAdapter(Context context,List<FoodCommentsModel> list){
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_comment_item, null);
			holder = new Holder();
			holder.name = (TextView) convertView.findViewById(R.id.user_name);
			holder.img = (CircleImageView) convertView.findViewById(R.id.comment_img);
			holder.context = (TextView) convertView.findViewById(R.id.comment_desc);
			holder.time = (TextView) convertView.findViewById(R.id.user_time);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		FoodCommentsModel model = mList.get(position);
		holder.name.setText(model.getUserName());
		holder.time.setText(model.getCommentTime());
		if(model.getUserImg().equals("")){
			holder.img.setBackgroundResource(R.drawable.user_comment);				
		}else{
			x.image().bind(holder.img, model.getUserImg(),MyApplication.imageComment);		
		}
		holder.context.setText("\t\t"+model.getUserComment());
		
		return convertView;
	}
	
	private class Holder{
		TextView name,context;
		CircleImageView img;
		TextView time;
	}

}

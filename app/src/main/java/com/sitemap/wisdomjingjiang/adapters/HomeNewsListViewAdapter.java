package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.NewsListModel;
import com.sitemap.wisdomjingjiang.models.NewsModels;

import android.content.Context;
import android.database.Cursor;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 首页 最新资讯 listview 适配器
 * @date create at  2016年5月10日 上午9:51:11
 */
public class HomeNewsListViewAdapter extends BaseAdapter{
	Context mContext;
	/**新闻的list的集合*/ 
	List<NewsListModel> mList;
	private Holder holder;
	
	public HomeNewsListViewAdapter(Context context,List<NewsListModel> list){
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_home_news_item, null);
			holder = new Holder();
			holder.img = (ImageView) convertView.findViewById(R.id.news_icon);
			holder.title = (TextView) convertView.findViewById(R.id.news_title);
			holder.context = (TextView) convertView.findViewById(R.id.news_content);
			holder.day = (TextView) convertView.findViewById(R.id.news_day);
//			holder.video = (ImageView) convertView.findViewById(R.id.news_video);
			holder.message = (ImageView) convertView.findViewById(R.id.news_message);
			holder.comment = (TextView) convertView.findViewById(R.id.news_comment);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		NewsListModel model = mList.get(position);
		if(model.getNewsImg().equals("")){
			holder.img.setBackgroundResource(R.drawable.tops_bg_2);
		}else{
			x.image().bind(holder.img, model.getNewsImg(),MyApplication.imageOptionsZ);
		}
		holder.title.setText(model.getNewsTitle());
		holder.context.setText(model.getNewsBrief());
		holder.day.setText(model.getNewsTime());
		if(model.getClickNumber().equals("")){
			holder.comment.setText(String.valueOf("0"));
		} else {
			holder.comment.setText(model.getClickNumber());
		}
		
		Cursor cursor = MyApplication.db.queryDBCollectData();
		while(cursor.moveToNext()){   			
			String id = cursor.getString(1);
			if(id.equals(model.getNewsID())){				
				holder.title.setTextColor(mContext.getResources().getColor(R.color.texthui));
			}			  
		} 
		
		return convertView;
	}
	
	private class Holder{
		//视频，评论图片，新闻图片
		ImageView message,img;		
		//标题，新闻详情，时间，评论的人数
		TextView title,context,day,comment;
	}

}

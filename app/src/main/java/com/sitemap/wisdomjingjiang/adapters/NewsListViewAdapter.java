package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.db.DBCheckNews;
import com.sitemap.wisdomjingjiang.models.NewsListModel;
import com.sitemap.wisdomjingjiang.models.NewsModels;
import com.sitemap.wisdomjingjiang.utils.CacheUtils;

import android.content.Context;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
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
 * @Description 首页 最新资讯 listview 适配器
 * @date create at  2016年5月10日 上午9:51:11
 */
public class NewsListViewAdapter extends BaseAdapter{
	Context mContext;
	private Holder holder;
	private List<NewsListModel> lnewsList;//新闻列表
	
	public NewsListViewAdapter(Context context,List<NewsListModel> lnewsList){
		this.mContext = context;
		this.lnewsList=lnewsList;
	}

	@Override
	public int getCount() {
		return lnewsList.size();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.news_item, null);
			holder = new Holder();
			holder.img = (ImageView) convertView.findViewById(R.id.news_icon);
			holder.title = (TextView) convertView.findViewById(R.id.news_title);
			holder.context = (TextView) convertView.findViewById(R.id.news_content);
			holder.day = (TextView) convertView.findViewById(R.id.news_day);
			holder.message = (ImageView) convertView.findViewById(R.id.news_message);
			holder.comment = (TextView) convertView.findViewById(R.id.news_comment);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.title.setText(lnewsList.get(position).getNewsTitle());
		holder.context.setText(lnewsList.get(position).getNewsBrief());
		holder.day.setText(lnewsList.get(position).getNewsTime());
		holder.comment.setText(lnewsList.get(position).getClickNumber());
		x.image().bind(holder.img,lnewsList.get(position).getNewsImg(),MyApplication.imageOptionsZ);
		
//		String cache = CacheUtils.getCache(mContext);
//		if(!TextUtils.isEmpty(cache)){
//			if(cache.equals(lnewsList.get(position).getNewsID())){
//				holder.title.setTextColor(mContext.getResources().getColor(R.color.texthui));
//			}else{
//				holder.title.setTextColor(mContext.getResources().getColor(R.color.text));
//			}
//		}
		
		Cursor cursor = MyApplication.db.queryDBCollectData();
		while(cursor.moveToNext()){   			
			String id = cursor.getString(1);
			if(id.equals(lnewsList.get(position).getNewsID())){				
				holder.title.setTextColor(mContext.getResources().getColor(R.color.texthui));
			}			  
		} 
				
		
		return convertView;
	}
	/**
	 * 优化类
	 * com.sitemap.wisdomjingjiang.adapters.Holder
	 * @author zhang
	 * create at 2016年5月12日 上午10:49:56
	 */
	private class Holder{
		//视频，评论图片，新闻图片
		ImageView message,img;
		
		//标题，新闻详情，时间，评论的人数
		TextView title,context,day,comment;
	}

}

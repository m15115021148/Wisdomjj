package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.MessageModel;

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
 * @Description 消息页面 的listview 的适配器
 * @date create at  2016年5月11日 下午4:43:18
 */
public class MessageListViewAdapter extends BaseAdapter {
	Context mContext;
	/**数据 的list*/ 
	private Holder holder;
	private List<MessageModel> lmessage;//消息列表
	
	public MessageListViewAdapter(Context context,List<MessageModel> lmessage){
		this.mContext = context;
		this.lmessage=lmessage;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lmessage.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lmessage.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_message_list_item, null);
			holder = new Holder();
			holder.msgImg = (ImageView) convertView.findViewById(R.id.message_img);
			holder.msgName = (TextView) convertView.findViewById(R.id.message_name);
			holder.msgDesc = (TextView) convertView.findViewById(R.id.message_desc);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.msgName.setText(lmessage.get(position).getName());
		holder.msgDesc.setText(lmessage.get(position).getBrief());
		if(lmessage.get(position).getImg().equals("")){
			holder.msgImg.setBackgroundResource(R.drawable.tops_bg_2);
		}else{
			x.image().bind(holder.msgImg,lmessage.get(position).getImg(),MyApplication.imageOptionsZ);
		}
		return convertView;
	}
	
	private class Holder{
		ImageView msgImg;//消息 图片
		TextView msgName;//消息 标题
		TextView msgDesc;//消息 详情
	}

}

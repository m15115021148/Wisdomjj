package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.GoodSmallTypesModel;

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
 * @Description 商家 页面  的第一个gridview 适配器
 * @date create at  2016年5月10日 下午5:49:08
 */
public class MerchantGridViewAdapter extends BaseAdapter{
	private Context mContext;
	private List<GoodSmallTypesModel> list;
	private Holder holder;
	
	public MerchantGridViewAdapter(Context context,List<GoodSmallTypesModel> list){
		this.mContext = context;
		this.setList(list);
	}

	@Override
	public int getCount() {
		return getList().size();
	}

	@Override
	public Object getItem(int position) {
		return getList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gv_merchant_one_item, null);
			holder = new Holder();
			x.view().inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.content.setText(getList().get(position).getSmallTypeName());
		x.image().bind(holder.img, getList().get(position).getSmallTypeImg(),MyApplication.imageOptionsV);
		return convertView;
	}
	
	public List<GoodSmallTypesModel> getList() {
		return list;
	}

	public void setList(List<GoodSmallTypesModel> list) {
		this.list = list;
	}

	private class Holder{
		@ViewInject(R.id.merchant_img)
		ImageView img;//图片
		@ViewInject(R.id.merchant_content)
		TextView content;//描述
	}

}

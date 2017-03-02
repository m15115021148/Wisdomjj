package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.models.GoodsBigTypesModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 商品页面 的listview的适配器
 * @date create at  2016年5月10日 下午4:43:37
 */
public class MerchantListViewAdapter extends BaseAdapter{
	private Context mContext;
	/**数据的list*/ 
	private List<GoodsBigTypesModel> list;
	
	private int selectPosition=0;//选中的position
	
	public MerchantListViewAdapter(Context context,List<GoodsBigTypesModel> list){
		this.mContext = context;
		this.list=list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 Holder holder=null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.merchant_listview_item, null);
			holder = new Holder();
			x.view().inject(holder, convertView);// 注解绑定
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getBigTypeName());
		
		if(position==getSelectPosition()){
			convertView.setBackgroundResource(R.color.mygray);
			holder.name.setTextColor(mContext.getResources().getColor(R.color.light_red));
		}else{
			convertView.setBackgroundResource(R.color.white);
			holder.name.setTextColor(mContext.getResources().getColor(R.color.black));
		}
		
		return convertView;
	}	

	public int getSelectPosition() {
		return selectPosition;
	}

	public void setSelectPosition(int selectPosition) {
		this.selectPosition = selectPosition;
	}

	private class Holder{
		@ViewInject(R.id.tv_name)
		TextView name;//列表名称
	}

}

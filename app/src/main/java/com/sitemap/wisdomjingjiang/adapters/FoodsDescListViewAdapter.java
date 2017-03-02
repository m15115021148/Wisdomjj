package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import com.sitemap.wisdomjingjiang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 团购详情 中  商家信息 列表 listview 适配器
 * @date create at  2016年5月17日 下午2:44:49
 */
public class FoodsDescListViewAdapter extends BaseAdapter implements OnClickListener{
	Context mContext;
	/**数据*/ 
	List<String> mList;
	private Holder holder;
	
	/**回调*/ 
	private CallBackFoodsDesc mCallBack;
	
	/**
	 * 自定义内部 接口 
	 *	实现item点击事件
	 *	用于回调按钮点击事件到Activity
	 *	回调给团购详情 页面
	 */
	public interface CallBackFoodsDesc{
		void onClickPhone(View v);
	}

	public FoodsDescListViewAdapter(Context context,CallBackFoodsDesc callBack){
		this.mContext = context;
		this.mCallBack = callBack;
		mList = new ArrayList<String>();
		for(int i=0;i<5;i++){
			mList.add("蜀地冒菜"+i+"(虹槽南路店)");
		}
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_foods_desc_item, null);
			holder = new Holder();
			holder.foodName = (TextView) convertView.findViewById(R.id.foods_desc_name);
			holder.foodPlace = (TextView) convertView.findViewById(R.id.foods_desc_place);
			holder.foodDistance = (TextView) convertView.findViewById(R.id.foods_desc_distance);
			holder.foodPhone = (ImageView) convertView.findViewById(R.id.foods_desc_phone);
			holder.phone = (LinearLayout) convertView.findViewById(R.id.l1_phone);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		String string = mList.get(position);
		holder.foodName.setText(string);
		
		holder.phone.setOnClickListener(this);
		holder.phone.setTag(position);
		
		return convertView;
	}
	
	private class Holder{
		//商家名称 地址，距离
		TextView foodName,foodPlace,foodDistance;
		ImageView foodPhone; //电话图片
		LinearLayout phone;
	}

	@Override
	public void onClick(View v) {
		if(mCallBack !=null){
			mCallBack.onClickPhone(v);
		}
	}

}

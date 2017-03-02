package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.models.AreasModel;
import com.sitemap.wisdomjingjiang.views.ShopsDialog;
import com.sitemap.wisdomjingjiang.views.ShopsDialog.OnClickDialogListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 首页商圈dialog
 * 
 * @author chenhao
 * 
 */
public class DialogShopsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;// view解析对象
	private List<AreasModel> list;// 数据list
	private ShopsDialog dialog;
	
	OnClickDialogListener onClickDialogListener;

	private class Holder {
		@ViewInject(R.id.shop_name)
		TextView shop_name;
	}

	public DialogShopsAdapter(Context context, List<AreasModel> data,
			ShopsDialog dialog,OnClickDialogListener onClickDialogListener) {
		this.list = data;
		this.dialog = dialog;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.onClickDialogListener = onClickDialogListener;
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
		Holder holder = null;
		if (convertView != null) {
			holder = (Holder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.dialog_shops_item, null);
			holder = new Holder();
			x.view().inject(holder, convertView);// 注解绑定
			convertView.setTag(holder);
		}
		holder.shop_name.setText(list.get(position).getAreaName());
		holder.shop_name.setOnClickListener(new Shops(position));

		return convertView;
	}

/**
 * 点击事件
 * Description: 
 * @author chenhao
 * @date   2016-5-21
 */
	class Shops implements OnClickListener {
		int position;

		public Shops(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			String id = list.get(position).getAreaID();
			String name=list.get(position).getAreaName();
				dialog.setAreaName(name);
			
			dialog.setAreaID(id);
			if(onClickDialogListener!=null){
				onClickDialogListener.setOnDialogClick(position);
			}
			dialog.dismiss();

		}

	}

}
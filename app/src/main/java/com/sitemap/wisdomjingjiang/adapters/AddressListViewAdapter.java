package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.List;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.AddressModel;
import com.sitemap.wisdomjingjiang.models.FoodsModels;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.adapters
 * 
 * @author zhang
 * @Description
 * @date create at 2016年5月11日 下午9:59:14
 */
public class AddressListViewAdapter extends BaseAdapter{
	Context mContext;
	private Holder holder;
	private List<AddressModel> mList;
	
	/** 回调 */
	private CallBackAddressList mCallBack;

	/**
	 * 内部接口 传值给activity 收货地址修改页面
	 */
	public interface CallBackAddressList {
		void onClickEditListener(int position);
		void onClickDeleteListener(int position);
	}

	public AddressListViewAdapter(Context context, List<AddressModel> list,CallBackAddressList callBack) {
		this.mContext = context;
		this.mList = list;
		this.mCallBack = callBack;
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
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.address_list_item, null);
			holder = new Holder();
			holder.mEdit = (LinearLayout) convertView
					.findViewById(R.id.address_edit);
			holder.mDel = (LinearLayout) convertView.findViewById(R.id.address_del);
			holder.userName = (TextView) convertView.findViewById(R.id.address_user_name);
			holder.userPhone = (TextView) convertView.findViewById(R.id.address_user_phone);
			holder.userInfo = (TextView) convertView.findViewById(R.id.address_user_info);
			holder.userColor = (TextView) convertView.findViewById(R.id.address_mo_wen);
			holder.isDefault = (CheckBox) convertView.findViewById(R.id.address_is_default);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}	
		
		AddressModel model = mList.get(position);
		holder.userName.setText(model.getLinkman());
		holder.userPhone.setText(model.getLinkphone());
		holder.userInfo.setText(model.getProvince()+model.getCity()+model.getArea()+model.getAddressInfo());		
		
		holder.mEdit.setTag(position);
		holder.mDel.setTag(position);
		final int selectPosition = position;
		holder.mEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickEditListener(selectPosition);
				}
			}
		});		
		holder.mDel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickDeleteListener(selectPosition);
				}
			}
		});	
		
		if(model.getIsDefault().equals("1")){
			holder.isDefault.setChecked(true);
			holder.userColor.setTextColor(mContext.getResources().getColor(R.color.red));
		}else{
			holder.isDefault.setVisibility(View.GONE);
			holder.userColor.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	/**
	 * 优化类 com.sitemap.wisdomjingjiang.adapters.Holder
	 * 
	 * @author zhang create at 2016年5月13日 上午9:59:56
	 */
	private class Holder {
		/** 编辑 */
		LinearLayout mEdit;
		/** 删除 */
		LinearLayout mDel;
		TextView userName,userPhone,userInfo,userColor;
		CheckBox isDefault;
	}
}

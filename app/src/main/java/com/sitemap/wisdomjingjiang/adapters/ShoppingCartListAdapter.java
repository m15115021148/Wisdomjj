package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.AddressListViewAdapter.CallBackAddressList;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.CartGoodsModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.adapters
 * 
 * @author chenmeng
 * @Description 购物车 数量 列表 适配器
 * @date create at 2016年5月19日 下午3:47:35
 */
public class ShoppingCartListAdapter extends BaseExpandableListAdapter
		implements OnCheckedChangeListener, OnClickListener {
	Context mContext;
	/** 父类 holder */
	private GroupHolder groupHolder;
	/** 子类 holder */
	private ChildHolder childHolder;

	private List<CartGoodsModel> list;
	private int number = 0;// 商品数量

	/** 回调 */
	private CallBackShoppingCart mCallBack;
	private boolean isGroupCheck = true;// 父类是否全选

	/**
	 * 内部接口 传值给activity 购物车页面
	 */
	public interface CallBackShoppingCart {
		void onClickAddListener(int groupPosition, int childPosition);

		void onClickReducesListener(int groupPosition, int childPosition);

		void onChildCheckChangeListener(int groupPosition, int childPosition,
				boolean isChildCheck);

		void onClickDeleteListener(int groupPosition, int childPosition);

		void onGroupCheckChangeListener(int groupPosition, boolean isGroupCheck);
	}

	public ShoppingCartListAdapter(Context context, List<CartGoodsModel> list,
			CallBackShoppingCart mCallBack) {
		this.mContext = context;
		this.list = list;
		this.mCallBack = mCallBack;
	}

	/**
	 * 获取队长父类的数量
	 * 
	 * @return
	 */
	@Override
	public int getGroupCount() {
		return list.size();
	}

	/**
	 * 子类的数量
	 * 
	 * @param groupPosition
	 * @return
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return list.get(groupPosition).getGoodsInfo().size();
	}

	/**
	 * 获取每个父类的对象
	 * 
	 * @param groupPosition
	 * @return
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	/**
	 * 获取子类
	 * 
	 * @param groupPosition
	 * @param childPosition
	 * @return
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition).getGoodsInfo().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * 创建父类的视图
	 * 
	 * @param groupPosition
	 * @param isExpanded
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// if(convertView == null){
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.shopping_cart_lv_group_item, null);
		groupHolder = new GroupHolder();
		groupHolder.merchantCb = (CheckBox) convertView
				.findViewById(R.id.shopping_list_cb);
		groupHolder.merchantName = (TextView) convertView
				.findViewById(R.id.merchant_name);
		convertView.setTag(groupHolder);
		// }else{
		// groupHolder = (GroupHolder) convertView.getTag();
		// }
		String string = list.get(groupPosition).getGoodShopName();
		groupHolder.merchantName.setText(string);
		final int selectGroupPosition = groupPosition;
		groupHolder.merchantCb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (mCallBack != null) {
							mCallBack.onGroupCheckChangeListener(
									selectGroupPosition, isChecked);
						}
					}
				});
		for (int i = 0; i < list.get(groupPosition).getGoodsInfo().size(); i++) {
			if (!list.get(groupPosition).getGoodsInfo().get(i).isChecked()) {
				isGroupCheck = false;
			}
		}
		if (isGroupCheck) {
			groupHolder.merchantCb.setChecked(true);
			isGroupCheck = true;
		} else {
			groupHolder.merchantCb.setChecked(false);
			isGroupCheck = true;
		}

		return convertView;
	}

	/**
	 * 创建子类的视图
	 * 
	 * @param groupPosition
	 * @param childPosition
	 * @param isLastChild
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
//		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.shopping_cart_lv_child_item, null);
			childHolder = new ChildHolder();
			childHolder.cb = (CheckBox) convertView.findViewById(R.id.shopping_list_cb);
			childHolder.icon = (ImageView) convertView.findViewById(R.id.shopping_icon);
			childHolder.desc = (TextView) convertView.findViewById(R.id.shopping_desc);
			childHolder.reduces = (TextView) convertView.findViewById(R.id.reduce);
			childHolder.add = (TextView) convertView.findViewById(R.id.add);
			childHolder.num = (TextView) convertView.findViewById(R.id.num);
			childHolder.color = (TextView) convertView.findViewById(R.id.color);
			childHolder.size = (TextView) convertView.findViewById(R.id.size);
			childHolder.money = (TextView) convertView.findViewById(R.id.money);
			childHolder.del = (ImageView) convertView.findViewById(R.id.del);
			childHolder.isShow = (LinearLayout) convertView.findViewById(R.id.is_show);
			convertView.setTag(childHolder);
//		}else{
//			childHolder = (ChildHolder) convertView.getTag();
//		}
			final int selectGroupPosition = groupPosition;
			final int selectChildPosition = childPosition;
			if (list.get(groupPosition).getGoodsInfo().get(childPosition).isChecked()) {
				childHolder.cb.setChecked(true);
			}
			
			childHolder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if(mCallBack != null){
						mCallBack.onChildCheckChangeListener(selectGroupPosition,selectChildPosition,isChecked);
					}
				}
			});
			childHolder.del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mCallBack != null){
						mCallBack.onClickDeleteListener(selectGroupPosition,selectChildPosition);
					}
				}
			});
			x.image().bind(childHolder.icon, list.get(groupPosition).getGoodsInfo().get(childPosition).getGoodsImg(),MyApplication.imageOptionsZ);
			childHolder.desc .setText(list.get(groupPosition).getGoodsInfo().get(childPosition).getGoodsName());
				
			childHolder.reduces.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickReducesListener(selectGroupPosition,selectChildPosition);
				}
			}
			});
			childHolder.add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickAddListener(selectGroupPosition,selectChildPosition);
				}
			}
			});
			childHolder.num .setText(list.get(groupPosition).getGoodsInfo().get(childPosition).getNumber());
			childHolder.color .setText(list.get(groupPosition).getGoodsInfo().get(childPosition).getColor());
			childHolder.size.setText(list.get(groupPosition).getGoodsInfo().get(childPosition).getSize());
			if(list.get(groupPosition).getGoodsInfo().get(childPosition).getGoodsPrice().equals("")){
				childHolder.money.setText("￥0.00");
			}else{
				childHolder.money .setText("￥"+list.get(groupPosition).getGoodsInfo().get(childPosition).getGoodsPrice());
			}
//		childHolder.del ;
//		childHolder.isShow ;
		
		return convertView;
	}

	/** ExpandableListView 如果子条目需要响应click事件,必需返回true */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * 父类的 holder
	 * 
	 */
	private class GroupHolder {
		CheckBox merchantCb;// 是否全选
		TextView merchantName;// 商家名称
	}

	/**
	 * 子类的 holder
	 * 
	 */
	private class ChildHolder {
		CheckBox cb;// 是否有选择
		ImageView icon;// 图片
		TextView desc;// 商品名称
		TextView reduces, add, num;// 减 加 数量
		TextView color, size;// 颜色 尺寸
		TextView money;// 金额
		ImageView del;// 删除
		LinearLayout isShow;// 是否显示 颜色 尺寸 （美食中没有）
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {

	}

	/**
	 * 选中事件
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

}

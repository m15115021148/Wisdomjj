package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.OrderGoodsModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.adapters
 * 
 * @author zhang
 * @Description
 * @date create at 2016年5月13日 下午2:59:14
 */
public class DingdanListViewAdapter extends BaseExpandableListAdapter {
	Context mContext;
	/** 父类 holder */
	private GroupHolder groupHolder;
	/** 子类 holder */
	private ChildHolder childHolder;
	
	/**数据  父类*/ 
	private List<OrderGoodsModel> mGroupList;
	
	/**回调*/ 
	private CallBackOrderGoods mCallBack;
	
	/**
	 * 内部接口
	 *	传值给activity 查看物流
	 */
	public interface CallBackOrderGoods{
		void onClickLookForWuLiuListener(int position);
		void onClickDeleteOrderListener(int position);
		void onClickSureListener(int position);
	}
	
	public DingdanListViewAdapter(Context context ,
			List<OrderGoodsModel> list,
			CallBackOrderGoods callBack){
		this.mContext = context;
		this.mGroupList = list;
		this.mCallBack = callBack;
	}

	/**
	 * 获取队长父类的数量
	 * 
	 * @return
	 */
	@Override
	public int getGroupCount() {
		return mGroupList.size();
	}

	/**
	 * 子类的数量
	 * 
	 * @param groupPosition
	 * @return
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return mGroupList.get(groupPosition).getGoodsInfo().size();
	}

	/**
	 * 获取每个父类的对象
	 * 
	 * @param groupPosition
	 * @return
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return mGroupList.get(groupPosition);
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
		return mGroupList.get(groupPosition).getGoodsInfo().get(childPosition);
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
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.order_goods_list_group_item, null);
			groupHolder = new GroupHolder();
			groupHolder.name = (TextView) convertView
					.findViewById(R.id.name);
			convertView.setTag(groupHolder);
			//去抢父类的点击事件 无法响应点击
            convertView.setClickable(true);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		String string = mGroupList.get(groupPosition).getGoodShopName();
		groupHolder.name.setText(string);
		
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
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.order_goods_list_child_item, null);
			childHolder = new ChildHolder();
			childHolder.wuLiu = (TextView) convertView.findViewById(R.id.wu_liu);
			childHolder.shopsName = (TextView) convertView.findViewById(R.id.order_shops_name);
			childHolder.shopsNum = (TextView) convertView.findViewById(R.id.order_number);
			childHolder.shopsColor = (TextView) convertView.findViewById(R.id.order_color);
			childHolder.shopsSize = (TextView) convertView.findViewById(R.id.order_size);
			childHolder.shopsPrice = (TextView) convertView.findViewById(R.id.order_price);
			childHolder.shopsDel = (ImageView) convertView.findViewById(R.id.order_delete);
			childHolder.shopsImg = (ImageView) convertView.findViewById(R.id.order_img);
			childHolder.sure = (TextView) convertView.findViewById(R.id.order_sure);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		CartGoodsInfoModel model = mGroupList.get(groupPosition).getGoodsInfo().get(childPosition);
		x.image().bind(childHolder.shopsImg, model.getGoodsImg(),MyApplication.imageOptions);
		childHolder.shopsName.setText(model.getGoodsName());
		childHolder.shopsNum.setText("x"+model.getNumber());
		childHolder.shopsColor.setText(model.getColor());
		childHolder.shopsSize.setText(model.getSize());
		if(model.getGoodsPrice().equals("") && model.getGoodsPrice() == null){
			childHolder.shopsPrice.setText("￥0.00");
		}else{
			childHolder.shopsPrice.setText("￥"+model.getGoodsPrice());
		}
		OrderGoodsModel goodsModel = mGroupList.get(groupPosition);
		int type = Integer.parseInt(goodsModel.getOrderType());
		switch (type) {
		case 1://待付款
			childHolder.sure.setText("付款");
			break;
		case 2://待发货
			childHolder.sure.setText("退款");
			break;
		case 3://待收货
			childHolder.wuLiu.setVisibility(View.VISIBLE);
			childHolder.sure.setText("退款");
			break;
		case 4://未评价
			childHolder.sure.setText("评价");
			break;

		default:
			break;
		}
		
		final int currPosition = childPosition;		
		childHolder.wuLiu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickLookForWuLiuListener(currPosition);
				}
			}
		});
		childHolder.shopsDel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickDeleteOrderListener(currPosition);
				}
			}
		});
		childHolder.sure.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickSureListener(currPosition);
				}
			}
		});

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
		TextView name;// 商家名称
	}

	/**
	 * 子类的 holder
	 * 
	 */
	private class ChildHolder {
		TextView wuLiu,sure;//查看物流
		/**商品名称  数据  颜色  尺寸  价格*/ 
		TextView shopsName,shopsNum,shopsColor,shopsSize,shopsPrice;
		ImageView shopsImg;
		ImageView shopsDel;
	}

}

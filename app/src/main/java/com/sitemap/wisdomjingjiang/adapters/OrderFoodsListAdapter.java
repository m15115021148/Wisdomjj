package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.OrderGoodsListAdapter.CallBackOrderGoods;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.OrderFoodsModel;
import com.sitemap.wisdomjingjiang.models.OrderGoodsModel;
import com.sitemap.wisdomjingjiang.utils.FileUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 团购订单 适配器
 * @date create at  2016年5月31日 上午9:41:17
 */
public class OrderFoodsListAdapter extends BaseExpandableListAdapter{
	Context mContext;
	/** 父类 holder */
	private GroupHolder groupHolder;
	/** 子类 holder */
	private ChildHolder childHolder;
	
	/**数据  父类*/ 
	private List<OrderFoodsModel> mGroupList;
	
	/**回调*/ 
	private CallBackOrderFoods mCallBack;
	/**订单状态*/ 
	private int mType;
	/**是否评论*/ 
	private int mIsComment;
	/**是否退款*/ 
	private int mIsRefund;
	/**是否使用*/ 
	private int mIsUse;
	
	/**
	 * 内部接口
	 *	传值给activity 
	 */
	public interface CallBackOrderFoods{
		void onClickDeleteOrderListener(int groupPosition,int childPosition);
		void onClickRefundListener(int groupPosition,int childPosition);
		void onClickCommentListener(int groupPosition,int childPosition);
		void onClickStatusListener(int groupPosition,int childPosition);
	}
	
	public OrderFoodsListAdapter(Context context ,
			List<OrderFoodsModel> list,
			CallBackOrderFoods callBack){
		this.mContext = context;
		this.mGroupList = list;
		this.mCallBack = callBack;
	}
	
	/**
	 * 订单状态
	 */
	public void changerOrderStatus(int type){	
		this.mType = type;
		notifyDataSetChanged();
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
		return mGroupList.get(groupPosition).getFoodsInfo().size();
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
		return mGroupList.get(groupPosition).getFoodsInfo().get(childPosition);
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
			groupHolder.del = (ImageView) convertView.findViewById(R.id.order_delete);

			convertView.setTag(groupHolder);
			//去抢父类的点击事件 无法响应点击
            convertView.setClickable(true);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		String string = mGroupList.get(groupPosition).getFoodShopName();
		groupHolder.name.setText(string);
		
		mType = Integer.parseInt(mGroupList.get(groupPosition).getOrderType());
		if(mType==1){//待付款
			groupHolder.del.setVisibility(View.GONE);
		}else
		if(mType==2){//已付款
			groupHolder.del.setVisibility(View.GONE);
		}
		
		
//		if(mType==3){//待使用
//			groupHolder.del.setVisibility(View.GONE);
//		}else
//		if(mType==4){//未评价
//			groupHolder.del.setVisibility(View.GONE);
//		}else
//		if(mType==5){//已关闭
//			groupHolder.del.setVisibility(View.GONE);
//		}else
//		if(mType==6){//已完成
//			groupHolder.del.setVisibility(View.GONE);
//		}else
//		if(mType==7){//已退款
//			groupHolder.del.setVisibility(View.GONE);
//		}else
//		if(mType==8){//退款中
//			groupHolder.del.setVisibility(View.GONE);
//		}else
//		if(mType==9){//退款拒绝
//			groupHolder.del.setVisibility(View.GONE);
//		}
		
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
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.order_foods_items, null);
			childHolder = new ChildHolder();
			childHolder.shopsImg = (ImageView) convertView.findViewById(R.id.order_img);
			childHolder.name = (TextView) convertView.findViewById(R.id.order_shops_name);
			childHolder.price = (TextView) convertView.findViewById(R.id.order_price);
			childHolder.status = (TextView) convertView.findViewById(R.id.order_status);
			childHolder.refund = (TextView) convertView.findViewById(R.id.order_refund);
			childHolder.delete = (ImageView) convertView.findViewById(R.id.order_delete);
			childHolder.isUse = (ImageView) convertView.findViewById(R.id.order_is_use);
			childHolder.isShow = (RelativeLayout) convertView.findViewById(R.id.order_is_show);
			childHolder.comment = (TextView) convertView.findViewById(R.id.order_comment);
			childHolder.isBg = (TextView) convertView.findViewById(R.id.order_bg);
			childHolder.number = (TextView) convertView.findViewById(R.id.order_number);
			childHolder.isShowTotal = (RelativeLayout) convertView.findViewById(R.id.order_shop_desc);
			childHolder.total = (TextView) convertView.findViewById(R.id.order_total_money);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		
		CartFoodsInfoModel model = mGroupList.get(groupPosition).getFoodsInfo().get(childPosition);
		if(model.getFoodsImg().equals("") || model.getFoodsImg()==null){
			childHolder.shopsImg.setBackgroundResource(R.drawable.tops_bg_2);
		}else{
			x.image().bind(childHolder.shopsImg, model.getFoodsImg(),MyApplication.imageOptionsZ);
		}
		childHolder.name.setText(model.getFoodsName());
		if(model.getFoodsPrice().equals("")){
			childHolder.price.setText("￥0.00");
		}else{
			childHolder.price.setText("￥"+model.getFoodsPrice());
		}	
		
		OrderFoodsModel goodsModel = mGroupList.get(groupPosition);
		mType = Integer.parseInt(goodsModel.getOrderType());
		mIsComment = Integer.parseInt(model.getIsComment());
		mIsRefund = Integer.parseInt(model.getIsRefund());
		mIsUse = Integer.parseInt(model.getIsUse());
		if(mType==1){//待付款	
			childHolder.isUse.setVisibility(View.GONE);
			childHolder.delete.setVisibility(View.VISIBLE);
			childHolder.status.setVisibility(View.GONE);
			childHolder.refund.setVisibility(View.GONE);
			childHolder.isShow.setVisibility(View.VISIBLE);
			childHolder.comment.setText("付款");
			childHolder.number.setVisibility(View.VISIBLE);
			childHolder.number.setText("X"+model.getNumber());			
		}else
		if(mType==2){//已付款	
			childHolder.isShowTotal.setVisibility(View.GONE);
			childHolder.isUse.setVisibility(View.GONE);
			childHolder.number.setVisibility(View.GONE);
			childHolder.isShow.setVisibility(View.GONE);
			childHolder.delete.setVisibility(View.GONE);
			childHolder.status.setVisibility(View.GONE);
			childHolder.refund.setVisibility(View.GONE);
			
			if(mIsRefund==2){//已退款(退款状态)
				childHolder.isUse.setVisibility(View.GONE);
				childHolder.refund.setVisibility(View.GONE);
				childHolder.delete.setVisibility(View.VISIBLE);
				childHolder.status.setVisibility(View.VISIBLE);
				childHolder.status.setText("已退款");
				childHolder.status.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
			}else if(mIsRefund==0){//未退款(待使用状态)
				childHolder.isUse.setVisibility(View.GONE);
				childHolder.delete.setVisibility(View.GONE);
				childHolder.status.setVisibility(View.GONE);
				childHolder.refund.setVisibility(View.GONE);
				
				if(mIsUse==0){//未使用(待使用)
					childHolder.delete.setVisibility(View.GONE);
					childHolder.status.setVisibility(View.GONE);
					childHolder.refund.setVisibility(View.VISIBLE);
					childHolder.refund.setText("申请退款");				
				}else if(mIsUse==1){//已使用
					childHolder.isUse.setVisibility(View.VISIBLE);
					childHolder.refund.setVisibility(View.GONE);
					childHolder.delete.setVisibility(View.VISIBLE);
					childHolder.status.setVisibility(View.VISIBLE);
					childHolder.isUse.setBackgroundResource(R.drawable.order_user);
					if(mIsComment==1){//已评价
						childHolder.status.setText("已评论");
						childHolder.status.setBackgroundColor(mContext.getResources().getColor(R.color.bghui));
						childHolder.status.setClickable(false);
					}else if(mIsComment==2){//未评论
						childHolder.status.setText("去评论");
						childHolder.status.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
					}else if(mIsComment==3){//不能评论			
						childHolder.status.setText("已评论");
						childHolder.status.setBackgroundColor(mContext.getResources().getColor(R.color.bghui));
						childHolder.status.setClickable(false);
					}
				}else if(mIsUse==2){//已过期
					childHolder.isUse.setVisibility(View.VISIBLE);
					childHolder.refund.setVisibility(View.GONE);
					childHolder.delete.setVisibility(View.GONE);
					childHolder.status.setVisibility(View.GONE);
					childHolder.isUse.setBackgroundResource(R.drawable.order_overdue);
				}
				
			}else 
			if(mIsRefund==3){//不能退款(已使用状态)
				childHolder.isUse.setVisibility(View.GONE);
				childHolder.delete.setVisibility(View.GONE);
				childHolder.status.setVisibility(View.GONE);
				childHolder.refund.setVisibility(View.GONE);
				if(mIsUse==0){//未使用(待使用)
					childHolder.delete.setVisibility(View.GONE);
					childHolder.status.setVisibility(View.GONE);
					childHolder.refund.setVisibility(View.VISIBLE);
					childHolder.refund.setText("申请退款");				
				}else if(mIsUse==1){//已使用
					childHolder.isUse.setVisibility(View.VISIBLE);
					childHolder.refund.setVisibility(View.GONE);
					childHolder.delete.setVisibility(View.VISIBLE);
					childHolder.status.setVisibility(View.VISIBLE);
					childHolder.isUse.setBackgroundResource(R.drawable.order_user);
					if(mIsComment==1){//已评价
						childHolder.status.setText("已评论");
						childHolder.status.setBackgroundColor(mContext.getResources().getColor(R.color.bghui));
						childHolder.status.setClickable(false);
					}else if(mIsComment==2){//未评论
						childHolder.status.setText("去评论");
						childHolder.status.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
					}else if(mIsComment==3){//不能评论			
						childHolder.status.setText("已评论");
						childHolder.status.setBackgroundColor(mContext.getResources().getColor(R.color.bghui));
						childHolder.status.setClickable(false);
					}
				}else if(mIsUse==2){//已过期
					childHolder.isUse.setVisibility(View.VISIBLE);
					childHolder.refund.setVisibility(View.GONE);
					childHolder.delete.setVisibility(View.GONE);
					childHolder.status.setVisibility(View.GONE);
					childHolder.isUse.setBackgroundResource(R.drawable.order_overdue);
				}
			}else 
			if(mIsRefund==1){//退 款中(退款状态)
				childHolder.isUse.setVisibility(View.GONE);
				childHolder.delete.setVisibility(View.GONE);
				childHolder.status.setVisibility(View.GONE);
				childHolder.refund.setVisibility(View.VISIBLE);
				childHolder.refund.setText("退款详情");
			}	
		}	
		
		if(childPosition==mGroupList.get(groupPosition).getFoodsInfo().size()-1){	
			childHolder.isShowTotal.setVisibility(View.VISIBLE);
			String totalCount = totalCount(mGroupList.get(groupPosition).getFoodsInfo());
			String[] split = totalCount.split(";");			
			childHolder.total.setText("共"+split[0]+"件商品  合计：￥"+FileUtils.roundMath(Double.parseDouble(split[1])));
			if(mType==1){				
				childHolder.isShow.setVisibility(View.VISIBLE);
				childHolder.isBg.setVisibility(View.VISIBLE);				
			}else{
				childHolder.isShow.setVisibility(View.GONE);
				childHolder.isBg.setVisibility(View.GONE);
			}			
		}else{
			childHolder.isShowTotal.setVisibility(View.GONE);
			childHolder.isShow.setVisibility(View.GONE);
			childHolder.isBg.setVisibility(View.GONE);
		}
		final int currChildPosition = childPosition;
		final int currGroupPosition = groupPosition;		
		childHolder.comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickCommentListener(currGroupPosition,currChildPosition);
				}
			}
		});
		
		childHolder.refund.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack !=null){
					mCallBack.onClickRefundListener(currGroupPosition, currChildPosition);
				}
			}
		});
		
		childHolder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack!=null){
					mCallBack.onClickDeleteOrderListener(currGroupPosition, currChildPosition);
				}
				
			}
		});
		childHolder.status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack!=null){
					mCallBack.onClickStatusListener(currGroupPosition, currChildPosition);
				}
				
			}
		});
		groupHolder.del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack!=null){
					mCallBack.onClickDeleteOrderListener(currGroupPosition, currChildPosition);
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
		ImageView del;
	}

	/**
	 * 子类的 holder
	 * 
	 */
	private class ChildHolder {
		/**商品名称   价格 详情 退款  已退款*/ 
		TextView name,price,desc,refund,status;
		ImageView shopsImg;//图片
		TextView comment;//评论
		ImageView delete;//删除
		ImageView isUse;//是否使用
		/**是否显示  */ 
		RelativeLayout isShow,isShowTotal;
		TextView isBg;//是否显示 间距
		TextView number,total;//数量
	}
	
	/**
	 * 计算数量 价格  运费
	 * @param list
	 * @return
	 */
	private String totalCount(List<CartFoodsInfoModel> list){
		int number = 0;
		double money = 0;
		double express = 0;
		for (int i = 0; i < list.size(); i++) {			
			CartFoodsInfoModel model = list.get(i);
			if(!model.getNumber().equals("")&&!model.getFoodsPrice().equals("")){							
				number = number + Integer.parseInt(model.getNumber());
				money = money + (Double.parseDouble(model.getFoodsPrice())*Integer.parseInt(model.getNumber()));
			}
		}
		return String.valueOf(number)+";"+String.valueOf(money)+";"+String.valueOf(express);
	}

}

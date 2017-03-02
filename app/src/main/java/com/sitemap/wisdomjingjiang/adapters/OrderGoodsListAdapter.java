package com.sitemap.wisdomjingjiang.adapters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.OrderGoodsModel;
import com.sitemap.wisdomjingjiang.utils.FileUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.adapters
 * 
 * @author zhang
 * @Description
 * @date create at 2016年5月13日 下午2:59:14
 */
public class OrderGoodsListAdapter extends BaseExpandableListAdapter {
	Context mContext;
	/** 父类 holder */
	private GroupHolder groupHolder;
	/** 子类 holder */
	private ChildHolder childHolder;
	/**订单状态*/ 
	private int mType;
	/**商品状态*/ 
	private int mShopType;
	
	/**数据  父类*/ 
	private List<OrderGoodsModel> mGroupList;
	
	/**回调*/ 
	private CallBackOrderGoods mCallBack;
	
	/**
	 * 内部接口
	 *	传值给activity 查看物流
	 */
	public interface CallBackOrderGoods{
		void onClickDeleteOrderListener(int groupPosition,int childPosition);
		void onClickSureListener(int groupPosition,int childPosition);
		void onClickCommentListener(int groupPosition,int childPosition);
		void onClickRefundListener(int groupPosition,int childPosition);
		void onClickStatusListener(int groupPosition,int childPosition);
	}
	
	public OrderGoodsListAdapter(Context context ,
			List<OrderGoodsModel> list,
			CallBackOrderGoods callBack){
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
			groupHolder.del = (ImageView) convertView.findViewById(R.id.order_delete);
			convertView.setTag(groupHolder);
			//去抢父类的点击事件 无法响应点击
            convertView.setClickable(true);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		String string = mGroupList.get(groupPosition).getGoodShopName();
		groupHolder.name.setText(string);
		
		mType = Integer.parseInt(mGroupList.get(groupPosition).getOrderType());
		if(mType==1){//待付款
			groupHolder.del.setVisibility(View.GONE);
		}else
		if(mType==2){//待发货
			groupHolder.del.setVisibility(View.GONE);			
		}
//		else
//		if(mType==3){//待使用
//			groupHolder.del.setVisibility(View.GONE);
//		}else
//		if(mType==4){//未评价
//			groupHolder.del.setVisibility(View.GONE);
//		}else
//		if(mType==5){//已关闭
//			groupHolder.del.setVisibility(View.VISIBLE);
//		}else
//		if(mType==6){//已完成
//			groupHolder.del.setVisibility(View.VISIBLE);
//		}else
//		if(mType==7){//已退款
//			groupHolder.del.setVisibility(View.VISIBLE);
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
	@SuppressLint("NewApi")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.order_goods_list_child_item, null);
			childHolder = new ChildHolder();
			childHolder.status = (TextView) convertView.findViewById(R.id.order_status);
			childHolder.shopsName = (TextView) convertView.findViewById(R.id.order_shops_name);
			childHolder.shopsNum = (TextView) convertView.findViewById(R.id.order_number);
			childHolder.shopsColor = (TextView) convertView.findViewById(R.id.order_color);
			childHolder.shopsSize = (TextView) convertView.findViewById(R.id.order_size);
			childHolder.shopsPrice = (TextView) convertView.findViewById(R.id.order_price);
			childHolder.shopsImg = (ImageView) convertView.findViewById(R.id.order_img);
			childHolder.sure = (TextView) convertView.findViewById(R.id.order_sure);
			childHolder.isShow = (LinearLayout) convertView.findViewById(R.id.bt_show);
			childHolder.comment = (TextView) convertView.findViewById(R.id.order_comment);
			childHolder.refund = (TextView) convertView.findViewById(R.id.order_refund);
			childHolder.isShowTotal = (RelativeLayout) convertView.findViewById(R.id.order_shop_desc);
			childHolder.total = (TextView) convertView.findViewById(R.id.order_total_money);
			childHolder.delete = (ImageView) convertView.findViewById(R.id.order_delete);
			childHolder.orderShow = (LinearLayout) convertView.findViewById(R.id.order_show_status);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		CartGoodsInfoModel model = mGroupList.get(groupPosition).getGoodsInfo().get(childPosition);
		x.image().bind(childHolder.shopsImg, model.getGoodsImg(),MyApplication.imageOptionsZ);
		childHolder.shopsName.setText(model.getGoodsName());
		childHolder.shopsNum.setText("x"+model.getNumber());
		childHolder.shopsColor.setText(model.getColor());
		childHolder.shopsSize.setText(model.getSize());
		String totalCount = totalCount(mGroupList.get(groupPosition).getGoodsInfo());
		String[] split = totalCount.split(";");
		//运费
		if(!mGroupList.get(groupPosition).getExpressAll().equals("")){
			if(split[2].equals("0.001")){
				split[2] = mGroupList.get(groupPosition).getExpressAll();
			}else{
				if(!split[2].equals("0.0")){
					if(Double.parseDouble(split[2])>Double.parseDouble(mGroupList.get(groupPosition).getExpressAll())){
						split[2] = mGroupList.get(groupPosition).getExpressAll();
					}
				}					
			}			
		}		
		childHolder.total.setText("共"+split[0]+"件商品  合计：￥"+FileUtils.roundMath(Double.parseDouble(split[1])+Double.parseDouble(split[2]))+"(含运费￥"+split[2]+")");
		if(model.getGoodsPrice().equals("")){
			childHolder.shopsPrice.setText("￥0.00");
		}else{
			childHolder.shopsPrice.setText("￥"+model.getGoodsPrice());
		}
				
//		if(model.getIsComment().equals("1")){
//			childHolder.shopsComment.setText("已评论");			
//			childHolder.shopsComment.setTextColor(mContext.getResources().getColor(R.color.texthui));
//			childHolder.shopsComment.setBackground(mContext.getResources().getDrawable(R.color.bghui));
//			childHolder.shopsComment.setClickable(false);
//			childHolder.shopsComment.setEnabled(false);
//		}else{
//			childHolder.shopsComment.setText("去评价");
//			childHolder.shopsComment.setTextColor(mContext.getResources().getColor(R.color.red));
//			childHolder.shopsComment.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
//			childHolder.shopsComment.setClickable(true);
//			childHolder.shopsComment.setEnabled(true);
//		}

		OrderGoodsModel goodsModel = mGroupList.get(groupPosition);
		mType = Integer.parseInt(goodsModel.getOrderType());
		mShopType = Integer.parseInt(mGroupList.get(groupPosition).getGoodsInfo().get(childPosition).getShopType());
		if(mType==1){//待付款
			childHolder.orderShow.setVisibility(View.GONE);
			childHolder.delete.setVisibility(View.VISIBLE);
			childHolder.status.setVisibility(View.GONE);
			childHolder.comment.setVisibility(View.GONE);
			childHolder.refund.setVisibility(View.GONE);
			childHolder.sure.setVisibility(View.VISIBLE);
			childHolder.sure.setText("付款");
		}else		
		if(mType == 2){//已付款
			childHolder.orderShow.setVisibility(View.VISIBLE);
			childHolder.sure.setVisibility(View.GONE);
			if(mShopType==2){//待发货
				childHolder.delete.setVisibility(View.GONE);
				childHolder.status.setVisibility(View.GONE);
				childHolder.comment.setVisibility(View.VISIBLE);
				childHolder.refund.setVisibility(View.GONE);
				if(model.getIsRefund().equals("1")){//是      申请过退款
					childHolder.refund.setVisibility(View.VISIBLE);
//					childHolder.refund.setText("退款详情");
					childHolder.refund.setText("申请仲裁");
				}else{//否
					childHolder.refund.setVisibility(View.GONE);
				}
				childHolder.comment.setText("申请退款");
				childHolder.comment.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
				childHolder.sure.setVisibility(View.GONE);
			}else
			if(mShopType==3){//待收货
				childHolder.delete.setVisibility(View.GONE);
				childHolder.comment.setVisibility(View.VISIBLE);
				childHolder.refund.setVisibility(View.VISIBLE);
				childHolder.status.setVisibility(View.GONE);
				if(model.getIsRefund().equals("1")){//是      申请过退款
					childHolder.status.setVisibility(View.VISIBLE);
//					childHolder.status.setText("退款详情");
					childHolder.status.setText("申请仲裁");
				}else{//否
					childHolder.status.setVisibility(View.GONE);
				}
				childHolder.refund.setText("申请退款");
				childHolder.comment.setText("确认收货");
				childHolder.comment.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
				childHolder.sure.setVisibility(View.VISIBLE);
				if(TextUtils.isEmpty(goodsModel.getExpressNo())){
					childHolder.sure.setText("同城配送");
				}else{
					childHolder.sure.setText("查看物流");
				}
				
			}else
			if(mShopType==4){//未评价
				childHolder.delete.setVisibility(View.GONE);
				childHolder.comment.setVisibility(View.VISIBLE);
				childHolder.refund.setVisibility(View.GONE);
				childHolder.status.setVisibility(View.GONE);
				if(model.getIsComment().equals("1")){
					childHolder.comment.setText("已评论");			
					childHolder.comment.setTextColor(mContext.getResources().getColor(R.color.texthui));
					childHolder.comment.setBackground(mContext.getResources().getDrawable(R.color.bghui));
					childHolder.comment.setClickable(false);
					childHolder.comment.setEnabled(false);
				}else{
					childHolder.comment.setText("去评论");
					childHolder.comment.setTextColor(mContext.getResources().getColor(R.color.red));
					childHolder.comment.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
					childHolder.comment.setClickable(true);
					childHolder.comment.setEnabled(true);
				}
				childHolder.sure.setVisibility(View.VISIBLE);
				if(TextUtils.isEmpty(goodsModel.getExpressNo())){
					childHolder.sure.setText("同城配送");
				}else{
					childHolder.sure.setText("查看物流");
				}
			}else
			if(mShopType==6){//已完成 
				childHolder.delete.setVisibility(View.VISIBLE);
				childHolder.comment.setVisibility(View.VISIBLE);
				childHolder.refund.setVisibility(View.GONE);
				childHolder.status.setVisibility(View.GONE);
				childHolder.comment.setText("已完成");
				childHolder.comment.setBackgroundColor(mContext.getResources().getColor(R.color.bghui));
				childHolder.comment.setClickable(false);
				childHolder.sure.setVisibility(View.GONE);
			}else
			if(mShopType==7){//已退款
				childHolder.delete.setVisibility(View.VISIBLE);
				childHolder.comment.setVisibility(View.VISIBLE);
				childHolder.refund.setVisibility(View.GONE);
				childHolder.status.setVisibility(View.GONE);
				childHolder.comment.setText("已退款");
				childHolder.comment.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
//				childHolder.comment.setClickable(false);
				childHolder.sure.setVisibility(View.GONE);
			}else
			if(mShopType==8){//退款中
				childHolder.delete.setVisibility(View.GONE);
				childHolder.comment.setVisibility(View.VISIBLE);
				childHolder.refund.setVisibility(View.GONE);
				childHolder.status.setVisibility(View.GONE);
				childHolder.comment.setText("退款详情");
				childHolder.comment.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
				childHolder.sure.setVisibility(View.GONE);
			}
			
		}
			
			
//		if(mType==2){//待发货
//			childHolder.shopsComment.setVisibility(View.GONE);
//			childHolder.shopsRefund.setVisibility(View.GONE);
//			childHolder.sure.setVisibility(View.VISIBLE);
//			if(goodsModel.getIsNoRefund().equals("0")){//没有拒绝过申请过退款				
//				childHolder.sure.setText("申请退款");				
//			}else{
//				childHolder.sure.setText("退款中");
//			}
//			childHolder.sure.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
//		}else
//		if(mType==3){//待收货(退款 查看物流 确认收货)
//			childHolder.shopsComment.setVisibility(View.GONE);
//			childHolder.shopsRefund.setVisibility(View.VISIBLE);			
//			if(goodsModel.getIsNoRefund().equals("1")){//拒绝退款 退款原因显示		
//				childHolder.shopsRefund.setText("退款中");
//			}else{//没有拒绝  过申请过的退款	(两种状态0  2)		
//				childHolder.shopsRefund.setText("申请退款");
//			}
//			if(goodsModel.getExpressNo().equals("")||goodsModel.getExpressNo()==null){
//				childHolder.wuLiu.setText("同城配送");
//			}else{
//				childHolder.wuLiu.setText("查看物流");
//			}			
//			childHolder.sure.setVisibility(View.VISIBLE);
//			childHolder.sure.setText("确认收货");
//			childHolder.sure.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
//		}else
//		if(mType==4){//未评价(查看物流)
//			childHolder.shopsComment.setVisibility(View.VISIBLE);	
//			childHolder.shopsRefund.setVisibility(View.GONE);
////			childHolder.wuLiu.setText("退款");
//			if(goodsModel.getExpressNo().equals("")||goodsModel.getExpressNo()==null){
//				childHolder.sure.setText("同城配送");
//			}else{
//				childHolder.sure.setText("查看物流");
//			}
//			childHolder.sure.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
//		}else
//		if(mType==5){//已关闭
//			childHolder.shopsComment.setVisibility(View.GONE);
//			childHolder.shopsRefund.setVisibility(View.GONE);
//			childHolder.sure.setVisibility(View.VISIBLE);
//			childHolder.sure.setText("已关闭");
//			childHolder.sure.setBackgroundColor(mContext.getResources().getColor(R.color.bghui));
//			childHolder.sure.setClickable(false);
//		}else
//		if(mType==6){//已完成
//			childHolder.shopsComment.setVisibility(View.GONE);
//			childHolder.shopsRefund.setVisibility(View.GONE);
//			childHolder.sure.setVisibility(View.VISIBLE);
//			childHolder.sure.setText("已完成");
//			childHolder.sure.setBackgroundColor(mContext.getResources().getColor(R.color.bghui));
//			childHolder.sure.setClickable(false);
//		}else
//		if(mType==7){//已退款
//			childHolder.shopsComment.setVisibility(View.GONE);
//			childHolder.shopsRefund.setVisibility(View.GONE);
//			childHolder.sure.setVisibility(View.VISIBLE);
//			childHolder.sure.setText("已退款");
//			childHolder.sure.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
//			childHolder.sure.setClickable(false);
//		}else
//		if(mType==8){//退款中
//			childHolder.shopsComment.setVisibility(View.GONE);
//			childHolder.shopsRefund.setVisibility(View.GONE);
//			childHolder.sure.setVisibility(View.VISIBLE);
//			childHolder.sure.setBackground(mContext.getResources().getDrawable(R.drawable.bt_2));
//			childHolder.sure.setClickable(false);
//			
//			if(goodsModel.getIsNoRefund().equals("2")){//同意退款 也是退款中
//				if(goodsModel.getIsBackGoods().equals("1")){//收到退货				
//					childHolder.sure.setText("退款中");
//				}else{				
//					childHolder.sure.setText("退款中");
//				}
//			}else if(goodsModel.getIsNoRefund().equals("0")){//退款中
//				childHolder.sure.setText("退款中");
//			}			
//			
//			
//		}
		
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
		childHolder.sure.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickSureListener(currGroupPosition,currChildPosition);
				}
			}
		});
		childHolder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickDeleteOrderListener(currGroupPosition,currChildPosition);
				}
			}
		});		
		childHolder.refund.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack!=null){
					mCallBack.onClickRefundListener(currGroupPosition, currChildPosition);
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
		
		if(childPosition==mGroupList.get(groupPosition).getGoodsInfo().size()-1){
			childHolder.isShowTotal.setVisibility(View.VISIBLE);	
			childHolder.isShow.setVisibility(View.GONE);
			if(mType==1){
				childHolder.isShow.setVisibility(View.VISIBLE);
			}else
			if(mType==2){
				childHolder.isShow.setVisibility(View.GONE);
				if(mShopType==3){
					childHolder.isShow.setVisibility(View.VISIBLE);
				}else
				if(mShopType==4){
					childHolder.isShow.setVisibility(View.VISIBLE);
				}else{
					childHolder.isShow.setVisibility(View.GONE);
				}
			}
		}else{
			childHolder.isShowTotal.setVisibility(View.GONE);
			childHolder.isShow.setVisibility(View.GONE);
		}

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
		TextView status,sure;//查看物流
		/**商品名称  数据  颜色  尺寸  价格*/ 
		TextView shopsName,shopsNum,shopsColor,shopsSize,shopsPrice;
		ImageView shopsImg,delete;
		TextView comment,total;
		/**是否显示  */ 
		LinearLayout isShow ,orderShow;
		/**退款 （待收货中的 ）*/ 
		TextView refund;
		RelativeLayout isShowTotal;
	}
	
	/**
	 * 计算数量 价格  运费
	 * @param list
	 * @return
	 */
	private String totalCount(List<CartGoodsInfoModel> list){
		Map<String, String> map = new HashMap<String, String>();
		int number = 0;
		double money = 0;
		double express = 0;
		for (int i = 0; i < list.size(); i++) {			
			CartGoodsInfoModel model = list.get(i);
			if(!model.getNumber().equals("")&&!model.getGoodsPrice().equals("")){							
				number = number + Integer.parseInt(model.getNumber());
				money = money + (Double.parseDouble(model.getGoodsPrice())*Integer.parseInt(model.getNumber()));				
			}			
			if(!model.getExpress().equals("")){				
				map.put(model.getGoodsID(), model.getExpress());				
			}else{
				express = 0.001;
			}
		}
		//计算运费  同一件商品 不同颜色 尺寸 运费 只算一次
		if(map!=null){
			Set set = map.entrySet();
	        Iterator<Entry> it = set.iterator();
	        while (it.hasNext()){
	            Entry<String, String> entry = (Entry<String, String>)it.next();
	            express = express + Double.parseDouble(entry.getValue());
	        }
		}
		return String.valueOf(number)+";"+String.valueOf(money)+";"+String.valueOf(express);
	}

}

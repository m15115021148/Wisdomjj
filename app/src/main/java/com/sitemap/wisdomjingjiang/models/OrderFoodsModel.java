package com.sitemap.wisdomjingjiang.models;

import java.io.Serializable;
import java.util.List;

/**
 * com.sitemap.wisdomjingjiang.models.OrderFoodsModel
 * @author zhang
 * @category 订单中的美食商品订单的返回实体类
 * create at 2016年5月19日 上午10:38:43
 */
public class OrderFoodsModel implements Serializable{
	private String  foodShopID;//   商家id
	private String 	foodShopName;//  商家名称
	private String shopMobile;//商品电话
	private String 	orderID;//         订单id
	private String orderName;//订单号
	private String 	expressNo;//      快递单号
	private String 	expressName;//    快递名称
	private String createTime;//创建事件
	private String 	orderType;//       订单状态(1:待付款 2:待发货 3:待收货 4:未评价 5:已关闭 6:已完成 7:已退款)
    private List<CartFoodsInfoModel>  foodsInfo;//     商品信息（里面存储的是一个JSONArray对象）
	public String getFoodShopID() {
		return foodShopID;
	}
	public void setFoodShopID(String foodShopID) {
		this.foodShopID = foodShopID;
	}
	
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getFoodShopName() {
		return foodShopName;
	}
	public void setFoodShopName(String foodShopName) {
		this.foodShopName = foodShopName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public List<CartFoodsInfoModel> getFoodsInfo() {
		return foodsInfo;
	}
	public void setFoodsInfo(List<CartFoodsInfoModel> foodsInfo) {
		this.foodsInfo = foodsInfo;
	}
	public String getShopMobile() {
		return shopMobile;
	}
	public void setShopMobile(String shopMobile) {
		this.shopMobile = shopMobile;
	}
    
}

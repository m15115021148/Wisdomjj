package com.sitemap.wisdomjingjiang.models;

import java.io.Serializable;

/**
 * com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel
 * 
 * @author zhang
 * @category 购物车中美食商品的foodsinfo的实体类 create at 2016年5月19日 上午9:59:03
 */
public class CartFoodsInfoModel implements Serializable {
	private String foodsID;// 美食商品id
	private String foodsName;// 商品名称
	private String foodsImg;// 商品图片（一张）
	private String number;// 数量
	private String foodsPrice;// 商品单价
	private String express;// 快递费
	private String itemID;// 购物车条目id
	private boolean isChecked;// 是否选中
	private String orderInfoID;//订单详细项id
	private String isComment;//是否已评价 （1：已评价，2未评价）
	private String isRefund;//是否退款（1：已退款，2：未退款，3：不能退款，4，退 款中）
	private String couponCode;//团购卷的输入码（付款后都显示）
	private String isUse;//团购卷是否使用（0：未使用，1：已使用，2：已过期）
	private String couponDesc;//团购卷描述（是否可以叠加之类的）
	private String deadTime;//团购券截止日期
	
	
	public String getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(String deadTime) {
		this.deadTime = deadTime;
	}

	public String getCouponDesc() {
		return couponDesc;
	}

	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}

	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getOrderInfoID() {
		return orderInfoID;
	}

	public void setOrderInfoID(String orderInfoID) {
		this.orderInfoID = orderInfoID;
	}

	public String getIsComment() {
		return isComment;
	}

	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getFoodsID() {
		return foodsID;
	}

	public void setFoodsID(String foodsID) {
		this.foodsID = foodsID;
	}

	public String getFoodsName() {
		return foodsName;
	}

	public void setFoodsName(String foodsName) {
		this.foodsName = foodsName;
	}

	public String getFoodsImg() {
		return foodsImg;
	}

	public void setFoodsImg(String foodsImg) {
		this.foodsImg = foodsImg;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFoodsPrice() {
		return foodsPrice;
	}

	public void setFoodsPrice(String foodsPrice) {
		this.foodsPrice = foodsPrice;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

}

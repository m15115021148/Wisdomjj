package com.sitemap.wisdomjingjiang.models;

import java.io.Serializable;

/**
 * com.sitemap.wisdomjingjiang.models.ShoppingCartGoodsInfoModel
 * 
 * @author zhang
 * @category 购物车中购物商品的goodsinfo的实体类 create at 2016年5月19日 上午9:59:03
 */
public class CartGoodsInfoModel implements Serializable{
	private String goodsID;// 购物商品id
	private String goodsName;// 商品名称
	private String goodsImg;// 商品图片（一张）
	private String number;// 数量
	private String goodsPrice;// 商品单价
	private String color;// 颜色
	private String size;// 尺寸
	private String express;// 快递费
	private String itemID;// 购物车条目id
	private boolean isChecked;// 是否选中
	private String orderInfoID;//订单详细项id
	private String isComment;//是否已评价 （1：已评价，2未评价）
	private String shopType;//商品状态(2:待发货 3:待收货 4:未评价  6:已完成 7:已退款8退款中)
	private String isRefund;//是否 申请退款（1 是  2 否）
	private String isReject;//商家是否拒绝过 申请退款（1是  2 否）
		
	public String getIsReject() {
		return isReject;
	}

	public void setIsReject(String isReject) {
		this.isReject = isReject;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
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

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getGoodsID() {
		return goodsID;
	}

	public void setGoodsID(String goodsID) {
		this.goodsID = goodsID;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

}

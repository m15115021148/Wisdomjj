/**
 * 
 */
package com.sitemap.wisdomjingjiang.models;

import java.io.Serializable;

/**
 * com.sitemap.wisdomjingjiang.models.FoodShopsModel
 * @author zhang
 * @category 团购详情里面推荐的团购物品实体类
 * create at 2016年5月17日 下午3:26:56
 */
public class FoodShopsTypeFoodsModel implements Serializable {
	private String shopID;//        商家id
	private String shopName;//     商家名称
	private String shopGrade;//     商家评分
	private String shopImg;//       商家图片（一张）
	private String preMoney;//      人均消费
	private String shopType;//      商家类别（1：美食商家、2：购物商家，3：酒店，4：ktv）
	private String area;//           所属商圈
	private String lng;//             商家位置经度
	private String lat;//             商家位置纬度
	private String goodsName;//     最新的商品名称（3个、已“、”分割）
	
	private String foodID;//        美食id
	private String foodImg;//       美食图片（一张）
	private String foodName;//       美食名称
	private String nowPrice;//        现价
	private String oldPrice;//         门市价
	private String sales;//           销量
	public String getFoodID() {
		return foodID;
	}
	public void setFoodID(String foodID) {
		this.foodID = foodID;
	}
	public String getFoodImg() {
		return foodImg;
	}
	public void setFoodImg(String foodImg) {
		this.foodImg = foodImg;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public String getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}
	public String getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getShopID() {
		return shopID;
	}
	public void setShopID(String shopID) {
		this.shopID = shopID;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopGrade() {
		return shopGrade;
	}
	public void setShopGrade(String shopGrade) {
		this.shopGrade = shopGrade;
	}
	public String getShopImg() {
		return shopImg;
	}
	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}
	public String getPreMoney() {
		return preMoney;
	}
	public void setPreMoney(String preMoney) {
		this.preMoney = preMoney;
	}
	public String getShopType() {
		return shopType;
	}
	public void setShopType(String shopType) {
		this.shopType = shopType;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	
}

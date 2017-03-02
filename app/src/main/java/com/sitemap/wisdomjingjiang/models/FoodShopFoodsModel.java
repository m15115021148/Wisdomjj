/**
 * 
 */
package com.sitemap.wisdomjingjiang.models;

import java.io.Serializable;

/**
 * com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel
 * @author zhang
 * @category 美食商家中的美食返回的实体类
 * create at 2016年5月17日 下午4:07:47
 */
public class FoodShopFoodsModel implements Serializable {
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
	
}

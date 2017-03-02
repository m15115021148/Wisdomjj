package com.sitemap.wisdomjingjiang.models;

import java.util.List;

/**
 * com.sitemap.wisdomjingjiang.models.CartFoodsModel
 * 
 * @author zhang
 * @category 购物车中美食商品返回实体类 create at 2016年5月19日 上午10:00:56
 */
public class CartFoodsModel {
	private String foodShopID;// 商家id
	private String foodShopName;// 商家名称
	private List<CartFoodsInfoModel> foodsInfo;// 商品信息（里面存储的是一个JSONArray对象）
	private boolean isChecked;// 是否选中
	private String maxExpress;//最高运费
	
	public String getMaxExpress() {
		return maxExpress;
	}

	public void setMaxExpress(String maxExpress) {
		this.maxExpress = maxExpress;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getFoodShopID() {
		return foodShopID;
	}

	public void setFoodShopID(String foodShopID) {
		this.foodShopID = foodShopID;
	}

	public String getFoodShopName() {
		return foodShopName;
	}

	public void setFoodShopName(String foodShopName) {
		this.foodShopName = foodShopName;
	}

	public List<CartFoodsInfoModel> getFoodsInfo() {
		return foodsInfo;
	}

	public void setFoodsInfo(List<CartFoodsInfoModel> foodsInfo) {
		this.foodsInfo = foodsInfo;
	}

}

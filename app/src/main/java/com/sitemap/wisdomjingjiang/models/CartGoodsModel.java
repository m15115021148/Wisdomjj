package com.sitemap.wisdomjingjiang.models;

import java.util.List;

/**
 * com.sitemap.wisdomjingjiang.models.CartGoodsModel
 * @author zhang
 * @category 购物车中购物商品返回实体类
 * create at 2016年5月19日 上午10:00:56
 */
public class CartGoodsModel {
	private String goodShopID;//   商家id
	private String goodShopName;//  商家名称
	private List<CartGoodsInfoModel> goodsInfo;//     商品信息（里面存储的是一个JSONArray对象）
	private boolean isChecked;//是否选中
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
	public String getGoodShopID() {
		return goodShopID;
	}
	public void setGoodShopID(String goodShopID) {
		this.goodShopID = goodShopID;
	}
	public String getGoodShopName() {
		return goodShopName;
	}
	public void setGoodShopName(String goodShopName) {
		this.goodShopName = goodShopName;
	}
	public List<CartGoodsInfoModel> getGoodsInfo() {
		return goodsInfo;
	}
	public void setGoodsInfo(List<CartGoodsInfoModel> goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
	
}

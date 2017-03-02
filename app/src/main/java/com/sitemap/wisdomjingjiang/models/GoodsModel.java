package com.sitemap.wisdomjingjiang.models;

import java.io.Serializable;


/**
 * com.sitemap.wisdomjingjiang.models.GoodsModel
 * @author zhang
 * @category 获取购物商品列表返回实体类
 * create at 2016年5月17日 下午4:42:38
 */
public class GoodsModel implements Serializable{
	private String goodsID;//          购物商品id
	private String goodsName;//       商品名称
	private String goodsImg;//         商品图片
	private String goodsPlace;//        产地
	private String sales;//             销量
	private String goodsPrice;//        商品价格
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
	public String getGoodsPlace() {
		return goodsPlace;
	}
	public void setGoodsPlace(String goodsPlace) {
		this.goodsPlace = goodsPlace;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
}

package com.sitemap.wisdomjingjiang.models;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.models
 * @author chenmeng
 * @Description 搜索结果 实体类
 * @date create at  2016年5月31日 下午3:20:35
 */
public class SearchResultModel {
	private String rID;//信息id
	private String img;//图片
	private String type;//类别（1:美食商家2:购物商家3:购物商品4:美食商品5:KTV 6:酒店）
	private String name;//名称
	private String grade;//评分
	private String address;//地址
	private String lng;//商家位置经度
	private String lat;//商家位置纬度
	private String price;// 价格
	private String preMoney;//人均消费
	private String sales;//销量
	private String shopID;//商家id
	private String goodsName;//     最新的商品名称（3个、已“、”分割）（美食商家和商品要 返回这个字段）
	
	
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
	public String getrID() {
		return rID;
	}
	public void setrID(String rID) {
		this.rID = rID;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPreMoney() {
		return preMoney;
	}
	public void setPreMoney(String preMoney) {
		this.preMoney = preMoney;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}

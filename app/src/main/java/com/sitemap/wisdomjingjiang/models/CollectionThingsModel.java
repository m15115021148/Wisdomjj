package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.CollectionThingsModel
 * @author zhang
 * @category 获取我的收藏---商品
 * create at 2016年5月19日 下午1:44:32
 */
public class CollectionThingsModel {
	private String thingID;//        商品id
	private String thingName;//     商品名称
	private String thingImg;//        商品图片
	private String sales;//            销量
	private String nowPrice;//        现价
	private String oldPrice;//         门市价
	private String thingType;//    类别（1:美食商品 2:购物商品）
	private String shopID;//        商家id
	private String shopName;//     商家名称
	private String thingPlace;//商品所在地
	private String shopGrade;//		商家评价
	private String preMoney;//		人均消费
private String shopImg;//       商家图片（一张）
	private String shopType;//      商家类别（1：美食商家、2：购物商家，3：酒店，4：ktv）
	private String area;//           所属商圈
	private String lng;//             商家位置经度
	private String lat;//             商家位置纬度
	
	
	public String getShopImg() {
		return shopImg;
	}
	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
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
	public String getShopGrade() {
		return shopGrade;
	}
	public void setShopGrade(String shopGrade) {
		this.shopGrade = shopGrade;
	}
	public String getPreMoney() {
		return preMoney;
	}
	public void setPreMoney(String preMoney) {
		this.preMoney = preMoney;
	}
	public String getThingPlace() {
		return thingPlace;
	}
	public void setThingPlace(String thingPlace) {
		this.thingPlace = thingPlace;
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
	public String getThingID() {
		return thingID;
	}
	public void setThingID(String thingID) {
		this.thingID = thingID;
	}
	public String getThingName() {
		return thingName;
	}
	public void setThingName(String thingName) {
		this.thingName = thingName;
	}
	public String getThingImg() {
		return thingImg;
	}
	public void setThingImg(String thingImg) {
		this.thingImg = thingImg;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
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
	public String getThingType() {
		return thingType;
	}
	public void setThingType(String thingType) {
		this.thingType = thingType;
	}
	
}

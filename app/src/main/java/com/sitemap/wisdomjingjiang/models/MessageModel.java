package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.MessageModel
 * @author zhang
 * @category 消息列表返回类
 * create at 2016年6月3日 下午1:41:38
 */
public class MessageModel {
	private String mID;//     信息id
	private String shopID;//商品的店铺ID
	private String shopName;//商品的店铺名称
	private String img;//     图片
	private String type;//     类别（1：美食商家、2：购物商家，3：购物商品，4：美食商品，5物流信息。6新闻）
	private String name;//    标题
	private String brief;//     简介
	private String grade;//    评分
	private String address;//   地址
	private String lng;//              商家位置经度
	private String lat;//              商家位置纬度
	private String nowPrice;//        现价
	private String oldPrice;//         原价
	private String preMoney;//       人均消费
	private String sales;//            销量
	private String express;//          物流单号
	private String expressName;//      物流单位   
	private String newsUrl;//			新闻URL
	
	
	public String getNewsUrl() {
		return newsUrl;
	}
	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopID() {
		return shopID;
	}
	public void setShopID(String shopID) {
		this.shopID = shopID;
	}
	
	public String getmID() {
		return mID;
	}
	public void setmID(String mID) {
		this.mID = mID;
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
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
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
	public String getExpress() {
		return express;
	}
	public void setExpress(String express) {
		this.express = express;
	}
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	
}

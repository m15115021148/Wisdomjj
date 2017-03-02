package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.TopsModel
 * @author zhang
 * @category 轮播列表返回实体类
 * create at 2016年5月17日 下午2:38:08
 */
public class TopsModel {
	private String topID;//     轮播信息id
	private String topImg;//    图片
	private String topType;//    类别（1：新闻，2：美食商家、3：购物商家，4：购物商品，5：美食商品）
	private String shopID;//商品的店铺ID
	private String shopName;//商品的店铺名称
	private String name;//    标题
	private String brief;//     简介
	private String grade;//    评分
	private String address;//   地址
	private String lng;//              商家位置经度
	private String lat;//              商家位置纬度
	private String nowPrice;//        现价
	private String oldPrice;//         现价
	private String preMoney;//       人均消费
	private String sales;//            销量
	private String newsUrl;//       新闻网页链接
	

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
	public String getNewsUrl() {
		return newsUrl;
	}
	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}
	public String getTopID() {
		return topID;
	}
	public void setTopID(String topID) {
		this.topID = topID;
	}
	public String getTopImg() {
		return topImg;
	}
	public void setTopImg(String topImg) {
		this.topImg = topImg;
	}
	public String getTopType() {
		return topType;
	}
	public void setTopType(String topType) {
		this.topType = topType;
	}
	
}

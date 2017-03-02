package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.FoodShopInfoModel
 * @author zhang
 * @category 美食商家详细信息返回实体类
 * create at 2016年5月17日 下午3:41:54
 */
public class FoodShopInfoModel {
	private String shopImg;//       商家图片（多张，以“；”隔开）
	private String address;//         地址
	private String phoneNumber;//    电话号码
	private String brief;//             商家简介
	private String shopName;//商家名称
	
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopImg() {
		return shopImg;
	}
	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	
}

package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.FoodInfoModel
 * @author zhang
 * @category 美食详情返回实体类
 * create at 2016年5月17日 下午4:14:22
 */
public class FoodInfoModel {
	private String foodImg;//       美食图片（多张，以“；”隔开）
	private String foodBrief;//       美食简介
	private String integral;//         用户可获得的积分
	private String userName;//       用户名称(一条评论)
	private String userImg;//         用户头像
	private String userComment;//    用户评论
	private String commentTime;//评论时间
	private String province;
	private String city;
	private String area;
	/**库存量*/ 
	private String inventory;
	
	private String deadTime;//        商品有效期
	private String foodTypeID;//        商品类别id
	
	
	
	public String getDeadTime() {
		return deadTime;
	}
	public void setDeadTime(String deadTime) {
		this.deadTime = deadTime;
	}
	public String getFoodTypeID() {
		return foodTypeID;
	}
	public void setFoodTypeID(String foodTypeID) {
		this.foodTypeID = foodTypeID;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public String getInventory() {
		return inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getFoodImg() {
		return foodImg;
	}
	public void setFoodImg(String foodImg) {
		this.foodImg = foodImg;
	}
	public String getFoodBrief() {
		return foodBrief;
	}
	public void setFoodBrief(String foodBrief) {
		this.foodBrief = foodBrief;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getUserComment() {
		return userComment;
	}
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	
}

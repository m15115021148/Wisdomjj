package com.sitemap.wisdomjingjiang.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * com.sitemap.wisdomjingjiang.models.GoodsInfoModel
 * @author zhang
 * @category 购物商品详情返回实体类
 * create at 2016年5月17日 下午4:55:59
 */
public class GoodsInfoModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String goodsBrief;//       商品简介
	private String goodsImg;//         商品图片（多张，以“；”隔开）
	private String oldPrice;//          门市价
	private String express;//           快递费
	private String userName;//       用户名称（一条评论）
	private String userImg;//         用户头像
	private String userComment;//    用户评论
	private String commentTime;//评论时间
	private String goodShopID;//      购物商家id
	private String goodShopName;//    购物商家名称
	private String inventory;//         库存量
	private String integral;//         用户可获得的积分
	private String color;//              颜色（以”,”进行分割字符串）
	private String size;//               尺寸（以”,”进行分割字符串）
	private ArrayList<NumberModel> numberInfo     ;//数量列表
	
	
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getGoodShopName() {
		return goodShopName;
	}
	public void setGoodShopName(String goodShopName) {
		this.goodShopName = goodShopName;
	}
	public String getGoodsBrief() {
		return goodsBrief;
	}
	public void setGoodsBrief(String goodsBrief) {
		this.goodsBrief = goodsBrief;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public String getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}
	public String getExpress() {
		return express;
	}
	public void setExpress(String express) {
		this.express = express;
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
	public String getGoodShopID() {
		return goodShopID;
	}
	public void setGoodShopID(String goodShopID) {
		this.goodShopID = goodShopID;
	}
	public String getInventory() {
		return inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public ArrayList<NumberModel> getNumberInfo() {
		return numberInfo;
	}
	public void setNumberInfo(ArrayList<NumberModel> numberInfo) {
		this.numberInfo = numberInfo;
	}
	
}

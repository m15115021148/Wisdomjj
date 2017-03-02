/**
 * 
 */
package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.KtvShopRoomModel
 * @author zhang
 * @category ktv商家中的房间信息返回的实体类
 * create at 2016年5月19日 下午2:07:47
 */
public class KtvShopRoomModel {
	private String roodID;//        房间id
	private String roodImg;//       房间图片（一张）
	private String roodName;//       房间名称
	private String nowPrice;//        现价
	private String oldPrice;//         门市价
	private String sales;//           销量
	public String getRoodID() {
		return roodID;
	}
	public void setRoodID(String roodID) {
		this.roodID = roodID;
	}
	public String getRoodImg() {
		return roodImg;
	}
	public void setRoodImg(String roodImg) {
		this.roodImg = roodImg;
	}
	public String getRoodName() {
		return roodName;
	}
	public void setRoodName(String roodName) {
		this.roodName = roodName;
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
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	
}

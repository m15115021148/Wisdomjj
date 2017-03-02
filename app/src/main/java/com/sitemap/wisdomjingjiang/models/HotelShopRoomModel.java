/**
 * 
 */
package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.HotelShopRoomModel
 * @author zhang
 * @category 酒店商家中的房间信息返回的实体类
 * create at 2016年5月19日 下午2:07:47
 */
public class HotelShopRoomModel {
	private String roomID;//        房间id
	private String roomImg;//       房间图片（一张）
	private String roomName;//       房间名称
	private String nowPrice;//        现价
	private String oldPrice;//         门市价
	private String sales;//           销量
	public String getRoomID() {
		return roomID;
	}
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	public String getRoomImg() {
		return roomImg;
	}
	public void setRoomImg(String roomImg) {
		this.roomImg = roomImg;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
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

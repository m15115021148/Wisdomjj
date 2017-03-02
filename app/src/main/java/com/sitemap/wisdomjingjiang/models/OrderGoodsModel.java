package com.sitemap.wisdomjingjiang.models;

import java.io.Serializable;
import java.util.List;

/**
 * com.sitemap.wisdomjingjiang.models.OrderGoodsModel
 * @author zhang
 * @category 订单中的购物商品订单的返回实体类
 * create at 2016年5月19日 上午10:38:43
 */
public class OrderGoodsModel implements Serializable{
	private String  goodShopID;//   商家id
	private String 	goodShopName;//  商家名称
	private String shopMobile;//商家电话
	private String 	orderID;//         订单id
	private String orderName;//订单号
	private String 	expressNo;//      快递单号
	private String 	expressName;//    快递名称
	private String  expressAll;//	总运费
	private String addressID; // 地址id
	private String linkman;//联系人
	private String linkphone;//联系电话
	private String addressInfo;//地址具体信息
	private String createTime;//创建时间	
	private String 	orderType;//       订单状态(1:待付款 2:待发货 3:待收货 4:未评价 5:已关闭 6:已完成 7:已退款)
    private List<CartGoodsInfoModel>  goodsInfo;//     商品信息（里面存储的是一个JSONArray对象）
    private String isNoRefund;//是否拒绝过申请退款
    private String receiptTime;//自动确认收货时间
    private String refundTime;//自动退款时间
    private String backGoodsTime;//商家收到退货 倒计时时间
    private String isBackGoods = "3";//商家是否收到退货(1 是 2 否)(退款处理状态,时间是15天)
    private String sellerfeedback;//退款拒绝的理由      
   
	public String getSellerfeedback() {
		return sellerfeedback;
	}
	public void setSellerfeedback(String sellerfeedback) {
		this.sellerfeedback = sellerfeedback;
	}
	public String getBackGoodsTime() {
		return backGoodsTime;
	}
	public void setBackGoodsTime(String backGoodsTime) {
		this.backGoodsTime = backGoodsTime;
	}
	public String getIsBackGoods() {
		return isBackGoods;
	}
	public void setIsBackGoods(String isBackGoods) {
		this.isBackGoods = isBackGoods;
	}
	public String getIsNoRefund() {
		return isNoRefund;
	}
	public void setIsNoRefund(String isNoRefund) {
		this.isNoRefund = isNoRefund;
	}
	public String getReceiptTime() {
		return receiptTime;
	}
	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}
	public String getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}
	public String getGoodShopID() {
		return goodShopID;
	}
	public void setGoodShopID(String goodShopID) {
		this.goodShopID = goodShopID;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getGoodShopName() {
		return goodShopName;
	}
	public String getAddressID() {
		return addressID;
	}
	public void setAddressID(String addressID) {
		this.addressID = addressID;
	}
	public void setGoodShopName(String goodShopName) {
		this.goodShopName = goodShopName;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	public String getExpressAll() {
		return expressAll;
	}
	public void setExpressAll(String expressAll) {
		this.expressAll = expressAll;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getLinkphone() {
		return linkphone;
	}
	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}
	public String getAddressInfo() {
		return addressInfo;
	}
	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public List<CartGoodsInfoModel> getGoodsInfo() {
		return goodsInfo;
	}
	public void setGoodsInfo(List<CartGoodsInfoModel> goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
	public String getShopMobile() {
		return shopMobile;
	}
	public void setShopMobile(String shopMobile) {
		this.shopMobile = shopMobile;
	}
    
}

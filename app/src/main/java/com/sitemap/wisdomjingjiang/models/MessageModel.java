package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.MessageModel
 * @author zhang
 * @category ��Ϣ�б�����
 * create at 2016��6��3�� ����1:41:38
 */
public class MessageModel {
	private String mID;//     ��Ϣid
	private String shopID;//��Ʒ�ĵ���ID
	private String shopName;//��Ʒ�ĵ�������
	private String img;//     ͼƬ
	private String type;//     ���1����ʳ�̼ҡ�2�������̼ң�3��������Ʒ��4����ʳ��Ʒ��5������Ϣ��6���ţ�
	private String name;//    ����
	private String brief;//     ���
	private String grade;//    ����
	private String address;//   ��ַ
	private String lng;//              �̼�λ�þ���
	private String lat;//              �̼�λ��γ��
	private String nowPrice;//        �ּ�
	private String oldPrice;//         ԭ��
	private String preMoney;//       �˾�����
	private String sales;//            ����
	private String express;//          ��������
	private String expressName;//      ������λ   
	private String newsUrl;//			����URL
	
	
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

package com.sitemap.wisdomjingjiang.models;

import java.io.Serializable;

/**
 * com.sitemap.wisdomjingjiang.models.AddressModel
 * @author zhang
 * @category 我的收获地址的返回实体类
 * create at 2016年5月19日 下午2:56:29
 */
public class AddressModel implements Serializable{
	private String addressID;//    收获地址id
	private String linkman;//      联系人
	private String linkphone;//     联系电话
	private String province;//      省
	private String city;//          城市
	private String area;//          区
	private String addressInfo;//     地址具体信息
	private String isDefault;//      是否是默认收获地址（1是，2否）
	public String getAddressID() {
		return addressID;
	}
	public void setAddressID(String addressID) {
		this.addressID = addressID;
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
	public String getAddressInfo() {
		return addressInfo;
	}
	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
}

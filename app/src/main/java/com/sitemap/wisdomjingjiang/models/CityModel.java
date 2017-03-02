package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.CityModel
 * @author zhang
 * @category 获取市列表返回实体类
 * create at 2016年5月19日 下午3:11:43
 */
public class CityModel {
	private String cityID;//          城市id
	private String city;//            城市名称
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
}

package com.sitemap.wisdomjingjiang.models;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.models
 * @author chenmeng
 * @Description 搜索索引 实体类
 * @date create at  2016年5月31日 下午3:18:38
 */
public class SearchIndexModel implements Serializable{
	private String name;//名称
	private String number;//数量
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
}

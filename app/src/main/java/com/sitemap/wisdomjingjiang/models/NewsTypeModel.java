package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.NewsTypeModel
 * @author zhang
 * @category 新闻类别列表返回实体类
 * create at 2016年5月17日 下午2:43:30
 */
public class NewsTypeModel {
	private String newsTypeID;//     类别id
	private String newsTypeName;//   类别名称
	public String getNewsTypeID() {
		return newsTypeID;
	}
	public void setNewsTypeID(String newsTypeID) {
		this.newsTypeID = newsTypeID;
	}
	public String getNewsTypeName() {
		return newsTypeName;
	}
	public void setNewsTypeName(String newsTypeName) {
		this.newsTypeName = newsTypeName;
	}
	
}

/**
 * 
 */
package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.NewsListModel
 * @author zhang
 * @category 
 * create at 2016年5月17日 下午3:01:53
 */
public class NewsListModel {
	private String newsID;//     新闻id
	private String newsTitle;//    新闻标题
	private String newsBrief;//     新闻简介
	private String newsTime;//     新闻发布时间
	private String newsImg;//      新闻图片
	private String clickNumber;//   新闻点击量
	private String newsUrl;//       新闻网页链接
	public String getNewsID() {
		return newsID;
	}
	public void setNewsID(String newsID) {
		this.newsID = newsID;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public String getNewsBrief() {
		return newsBrief;
	}
	public void setNewsBrief(String newsBrief) {
		this.newsBrief = newsBrief;
	}
	public String getNewsTime() {
		return newsTime;
	}
	public void setNewsTime(String newsTime) {
		this.newsTime = newsTime;
	}
	public String getNewsImg() {
		return newsImg;
	}
	public void setNewsImg(String newsImg) {
		this.newsImg = newsImg;
	}
	public String getClickNumber() {
		return clickNumber;
	}
	public void setClickNumber(String clickNumber) {
		this.clickNumber = clickNumber;
	}
	public String getNewsUrl() {
		return newsUrl;
	}
	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}
	
}

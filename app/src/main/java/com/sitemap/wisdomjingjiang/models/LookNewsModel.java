package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.LookNewsModel
 * @author zhang
 * @category 查看爆料返回实体类
 * create at 2016年5月19日 下午3:19:28
 */
public class LookNewsModel {
	private String title;//           标题
	private String createTime;//     发布时间
	private String status;//        审核状态  （1：审核中，2：审核成功，3：审核失败）
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}

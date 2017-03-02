package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.FoodCommentsModel
 * @author zhang
 * @category 美食评价返回实体类
 * create at 2016年5月17日 下午4:19:33
 */
public class FoodCommentsModel {
	private String userName;//       用户名称
	private String userImg;//         用户头像
	private String userComment;//    用户评论
	private String commentTime;//评论时间
	
	
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getUserComment() {
		return userComment;
	}
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	
}

package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.LoginModel
 * @author zhang
 * @category 用户登录实体类
 * create at 2016年5月17日 上午10:44:41
 */
public class LoginModel {
	private String userID;//        用户id
	private String nickName;//       用户昵称
	private String img;//             头像 
	private String sex;//             性别
	private String integral;//         目前积分
	private String errorMsg;//        错误信息
	private String isSign;//           是否已签到（1：已签到，2：未签到）
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getIsSign() {
		return isSign;
	}
	public void setIsSign(String isSign) {
		this.isSign = isSign;
	}
	
}

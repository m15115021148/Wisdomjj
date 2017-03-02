package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.wisdomjingjiang.models.SignModel
 * @author zhang
 * @category 签到返回实体类
 * create at 2016年5月17日 下午2:25:03
 */
public class SignModel {
	private String integral;//      用户积分
	private String errorMsg;//     错误信息
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
	
}

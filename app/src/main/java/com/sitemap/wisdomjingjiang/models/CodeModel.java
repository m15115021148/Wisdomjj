package com.sitemap.wisdomjingjiang.models;

/**
 * 验证码请求类(所有的相同的返回都用这个实体类)
 * @author zhang
 * create at 2016年5月17日 下午1:05:52
 */
public class CodeModel {
	private int status;//请求状态
	private String errorMsg;//错误信息
	private String orderID;//订单id 
	
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}

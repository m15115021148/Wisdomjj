package com.sitemap.wisdomjingjiang.wxapi;

import com.sitemap.wisdomjingjiang.config.WebHostConfig;


/**
 * 
 * Description: 微信支付配置文件
 * 
 * @author chenhao
 * @date 2016-3-7
 */
public class WechatPayConfig {
	private static final String APP_ID = "wx7ace35557c293e46";//appid
	// 订单交易请求URL
	private static final String REQUEST_PAY_URL = WebHostConfig.getHostName()
			+ "userAction_sendWeChatPay.do?";

	public static String getAppId() {
		return APP_ID;
	}

	/**
	 * @param money
	 *            支付金额
	 * @param  userID  用户ID
	 * @param  orderID  订单ID 
	 * @param  express  运费 
	 * @return url
	 */
	public static String getRequestPayUrl(String money,String userID,String orderID ,String express) {
		return REQUEST_PAY_URL+"money="+money+"&platform=1&userID="+userID+"&orderID="+orderID+"&express="+express;
	}
}

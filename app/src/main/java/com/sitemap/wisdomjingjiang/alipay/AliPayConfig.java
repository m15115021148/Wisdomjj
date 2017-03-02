package com.sitemap.wisdomjingjiang.alipay;

import com.sitemap.wisdomjingjiang.config.WebHostConfig;


/**
 * 
 * Description: 支付宝配置文件
 * 
 * @author chenhao
 * @date 2015-12-31
 */
public class AliPayConfig {
	/*赛图支付宝账户*/
	// 商户PID 
	public static final String PARTNER = "2088011778363693";
//	 商户收款账号
	public static final String SELLER = "jstzgt@126.com";
//	 商户私钥，pkcs8格式
	public static String RSA_PRIVATE = "";
	// 订单交易成功，异步通知地址（赛图）
	public static final String NOTIFY_URL = WebHostConfig.getHostName()+"userAction_aliPay.do";

	public static String getPartner() {
		return PARTNER;
	}

	public static String getSeller() {
		return SELLER;
	}

	public static String getRSA_PRIVATE() {
		RSA_PRIVATE = "MIICXAIBAAKBgQDZduRcsV5MmPwPRa6ahBWUmqLIMRb2m34/pc2GJNnOXsjm6SBJ"
				+ "HX7LxnCXdF0HUg0SdzlZwcLlAevMiIdcwQYWoGsclPtbv4riFYBMrTYJLgCB6ePm"
				+ "rLdSrAaJFtqaWsWLQmB5yzWl7ShUUxBJeaTxosaUt66bUphSpFpH5PwGvwIDAQAB"
				+ "AoGAI2N2rCLtDpgCxNV5IYoHHQTBIsTiUMVjGSZ5OLA8hZnlJbagW2WiYbTsU6lL"
				+ "Dkrq96tROOsQNUZHwiQhqxV6UN1rh7OKgYqYvBV6oA4FY1XnR5DN4IxndCNIxhNF"
				+ "hr16ty9Jh6ZUqDG6mPYR8ub8fdTBWXlYPlZ/lHeau6tvoEkCQQD4RjWptNRzVEKq"
				+ "OOROSZH8ifjfX6v7Kt+nE+Olvulb5+5mCOtFUUtHmCT3zUHUf5M7ZUopkfJBy2QQ"
				+ "du6b1STlAkEA4Ds/IZEneObXiOHuWB64jGYCbRC2+myJCXyWUYE6GtfV/VR8WwOS"
				+ "A51rIkFNYKdgoVhCpCd4P4msnGeUL9hG0wJAMYFueeSA6G5IzMCDZcjtvzmT0pa1"
				+ "KGQoLJyerBIhXh4lP1tP7oaDSEdiN7dKBSFbLmDIJPnhIJV54n6UnaYmsQJBAIT1"
				+ "/Lhdxyck0teDCP7NsKbn8jZT+/xAkX3Dl6ra0uaJ54ThLSAW3OUx2g4k5MoUVfM2"
				+ "T8Xg9afPbkAMF03LlCsCQDNzhVZ0ia6ImYZ1GCSYdONEvs+nVzE7a2FN6KdXLy2H"
				+ "9xxdV8yTi74qxjtC26CdDl96etHgVqli5WF0wSWewDk=";
		return RSA_PRIVATE;
	}

	public static String getNotifyUrl() {
		return NOTIFY_URL;
	}
}

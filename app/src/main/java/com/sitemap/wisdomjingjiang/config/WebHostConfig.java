package com.sitemap.wisdomjingjiang.config;

/**
 * 
 * @ClassName:     WebHostConfig.java
 * @Description:   网络ip、port配置文件
 * @author         chenhao
 * @Date           2015-11-14
 */ 
 
public class WebHostConfig {
//	private static final String HOST_ADDRESS = "http://192.168.0.32:8080/";
//	 private static final String HOST_ADDRESS = "http://192.168.0.57:6060/";//内网
//	 private static final String HOST_ADDRESS = "http://218.202.235.66:8081/";//外网
//	 private static final String HOST_ADDRESS = "http://218.202.235.66:8081/";//外网
	private static final String HOST_ADDRESS="http://58.222.209.195:8083/";
//	private static final String HOST_ADDRESS = "http://wisejj.com/";
	
	private static final String HOST_NAME = HOST_ADDRESS + "ZhiHuiJingJiang/";
	
	public static String getHostAddress() {
		return HOST_ADDRESS; 
	}

	public static String getHostName() {
		return HOST_NAME;
	}
	
	
}

package com.sitemap.wisdomjingjiang.share;

import com.umeng.socialize.PlatformConfig;

/**
 * 
 * Description: 友盟分享配置类
 * @author chenhao
 * @date   2016-3-25
 */
public class UMShareConfig {

	/**
	 * 初始化，application中调用
	 */
	public static void init()
	// 各个平台的配置，建议放在全局Application或者程序入口
	{
		// 微信 
		PlatformConfig.setWeixin("wx7ace35557c293e46",
				"fb783e4065337e1b7dae10e28caf1102");
//		QQ和QQ空间
		PlatformConfig.setQQZone("1105385135",
				"n4WoMAdd79msZlTf");
	}
}

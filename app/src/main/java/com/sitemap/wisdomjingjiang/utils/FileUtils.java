package com.sitemap.wisdomjingjiang.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.os.Environment;

/**
 * @author Administrator
 *
 */
/**
 * com.sitemap.wisdomjingjiang.utils
 * 
 * @author chenmeng
 * @Description 文件util包
 * @date create at 2016年5月4日 上午9:58:21
 */
public class FileUtils {
	/**sdk 路径*/ 
	public static final String SDK_PATH = Environment.getExternalStorageDirectory().toString();
	
	/**
	 * 保留小点后两位
	 * @param value
	 * @return
	 */
	public static Double roundMath(double value){
		DecimalFormat df = new java.text.DecimalFormat("#.00");
		return Double.parseDouble(df.format(value));
	}

}

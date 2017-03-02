package com.sitemap.wisdomjingjiang.wxapi;

import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.app.Activity;
import android.os.Handler;

/**
 * 
 * Description: 微信支付帮助工具栏类
 * 
 * @author chenhao
 * @date 2016-3-7
 */
public class WechatPayHelper  {
	private Handler mHandler;// 调用此类的Activity中的Handler
	private final int ORDER=6;
	public WechatPayHelper() {

	}

	public WechatPayHelper(Handler mHandler) {
		this.mHandler = mHandler;
		
	}
	/**
	 * 
	 * @param context
	 * @param money 付款金额
	 * @param orderID  订单号
	 * @param progressDialog 进度条
	 */
	public void pay(Activity context,String userID,String money,String orderID,String express,MyProgressDialog progressDialog) {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.show();// 开启进度条
		}
		if (MyApplication.getNetObject().isNetConnected()) {
		HttpUtil httpUtil = new HttpUtil(
				mHandler);
		httpUtil.sendGet(ORDER,WechatPayConfig.getRequestPayUrl(money, userID, orderID, express));
		} else {
			ShowContentUtils.showShortToastMessage(context, "网络无法连接！");
		}
	}

}

package com.sitemap.wisdomjingjiang.wxapi;

import org.json.JSONObject;

import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * Description: 支付宝异步处理Handler
 * 
 * @author chenhao
 * @date 2016-1-7
 */
public class WechatPayHandler extends Handler {
	private Activity context;// 调用此类的Activity
	private IWXAPI api;// 微信支付sdk
	private  final String APPID = WechatPayConfig.getAppId();// appid
	private  MyProgressDialog progressDialog;// 进度条
//	支付异常tag
	public final static int WECHAT_EXCEPTION=101;
	

	public WechatPayHandler() {

	}

	public WechatPayHandler(Activity context,MyProgressDialog progressDialog) {
		this.context = context;
		api = WXAPIFactory.createWXAPI(context, APPID);// 注册appid
		api.registerApp(APPID);
		this.progressDialog=progressDialog;
	}

	/**
	 * 检查支付环境
	 * @return true正常，false 异常
	 */
	public boolean checkEnvironment() {
		if (!api.isWXAppInstalled()) {
//			new AlertDialog.Builder(context)
//					.setTitle("警告")
//					.setMessage("APP支付系统异常，没有安装微信客户端！")
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//								public void onClick(
//										DialogInterface dialoginterface, int i) {
//									dialoginterface.dismiss();
//								}
//							}).show();
			return  false;
		}
		if (!api.isWXAppSupportAPI()) {
			new AlertDialog.Builder(context)
					.setTitle("警告")
					.setMessage("APP支付系统异常，当前版本不支持支付功能！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									dialoginterface.dismiss();
								}
							}).show();
			return false;
		}
		return true;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();// 关闭进度条
		}
		if (msg.what == HttpUtil.SUCCESS) {
			// 获得余额
			if (msg.arg1 == 6) {
				if(!checkEnvironment()){
//					支付异常异常，通知充值页面
					Toast.makeText(context,
							"请更换支付方式！" ,
							Toast.LENGTH_SHORT).show();
					return;
				}; 
				try {
					String content = (String) msg.obj;
					if (content != null && content.length() > 0) {
						JSONObject json = new JSONObject(content);

						if (null != json && !json.has("retcode")) {
							PayReq req = new PayReq();
//							参与签名的字段名为appId，partnerId，prepayId，nonceStr，timeStamp，package。
							//注意：后台均小写，否则无法调起支付；如：appid、partnerid...
							req.appId = json.getString("appid");
							req.partnerId = json.getString("mch_id");
							req.prepayId = json.getString("prepay_id");// 获取预付prepayid
							req.nonceStr = json.getString("nonceStr");
							req.timeStamp = json.getString("timestamp");
							req.packageValue = json.getString("packages");
							req.sign = json.getString("sign");
//							 req.extData = json.getString("userID");
							// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
							api.sendReq(req);
						} else {
							Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
							Toast.makeText(context,
									"返回错误" + json.getString("retmsg"),
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Log.d("PAY_GET", "服务器请求错误");
						Toast.makeText(context, "服务器请求错误", Toast.LENGTH_SHORT)
								.show();
					}
				} catch (Exception e) {
					Log.e("PAY_GET", "异常：" + e.getMessage());
					Toast.makeText(context, "异常：" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

		} else if (msg.what == HttpUtil.FAILURE) {
			ShowContentUtils.showShortToastMessage(context, RequestCode.ERRORINFO);
			// 获取数据异常
		} 
	}


}

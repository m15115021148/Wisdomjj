package com.sitemap.wisdomjingjiang.alipay;

import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * 
 * Description: 支付宝异步处理Handler
 * 
 * @author chenhao
 * @date 2016-1-7
 */
public class AliPayHandler extends Handler {
	private Activity context;// 调用此类的Activity
	public AliPayHandler() {

	}

	public AliPayHandler(Activity context) {
		this.context = context;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case AliPayHelper.SDK_PAY_FLAG: {
			PayResult payResult = new PayResult((String) msg.obj);

			// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//			String resultInfo = payResult.getResult();

			String resultStatus = payResult.getResultStatus();

			// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
			if (TextUtils.equals(resultStatus, "9000")) {
				ShowContentUtils.showLongToastMessage(context, "支付成功！");
				
			} else {
				// 判断resultStatus 为非“9000”则代表可能支付失败
				// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				if (TextUtils.equals(resultStatus, "8000")) {
					ShowContentUtils.showLongToastMessage(context, "支付结果确认中！");
					
				} else {
					// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					ShowContentUtils.showLongToastMessage(context, "支付失败！");
				}
			}
			break;
		}
		case AliPayHelper.SDK_CHECK_FLAG: {
			ShowContentUtils.showLongToastMessage(context, "检查结果为："+ msg.obj);
			break;
		}
		default:
			break;
		}

	}


}

package com.sitemap.wisdomjingjiang.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.models.FoodShopFoodsModel;
import com.sitemap.wisdomjingjiang.models.FoodShopsModel;
import com.sitemap.wisdomjingjiang.models.GoodsModel;
import com.sitemap.wisdomjingjiang.models.LoginModel;
import com.sitemap.wisdomjingjiang.models.MessageModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 
 * com.sitemap.na2ne.activities.MyReceiver
 * @author zhang
 * 极光自定义广播
 * create at 2016年3月9日 下午5:13:05
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";//推送
	private Handler mhandler;//传过来的handler
	private final int JPUSH=1002;//极光handler
	public MyReceiver(Handler mhandler){
		this.mhandler=mhandler;
	}
	public MyReceiver(){
	}
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.i(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.i(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        	String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        	if (message!=null) {
        		Log.i(TAG,message );
			}
        	if (extras!=null) {
        		Log.i(TAG,extras );
			}
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.i(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver] 用户点击打开了通知");
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.e("result", extras);
			
        	if (extras!=null) {
				try {
					
					Intent in = new Intent();
					in.setAction("com.app2.activity");
					in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
					context.startActivity(in);
					
					JSONObject ob = new JSONObject(extras);
					MessageModel message=JSON.parseObject(
							ob.getString("msg"), MessageModel.class);
					Log.e("result", message.getmID());
					if (message!=null&&!message.getmID().equals("")&&message.getmID()!=null) {
						
						switch (Integer.parseInt(message.getType())) {
						//（1：团购商家、2：购物商家，3：购物商品，4：团购商品，5物流信息.6新闻）
						case 1://团购商家
							FoodShopsModel model = new FoodShopsModel();	
							model.setArea("");
							model.setLat(message.getLat());
							model.setLng(message.getLng());
							model.setPreMoney(message.getPreMoney());
							model.setShopGrade(message.getGrade());
							model.setShopID(message.getmID());
							model.setShopImg(message.getImg());
							model.setShopName(message.getName());
							model.setShopType(message.getType());
							Log.e("result", message.getType());
							Intent intent0 = new Intent(context,FoodsShopActivity.class);
							//将商家id 传过去					
							Bundle b = new Bundle();
							b.putSerializable("FoodShopsModel", model);
							intent0.putExtras(b);
							intent0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
							context.startActivity(intent0);
							break;
						case 2://购物商家
//							Intent intent1 = new Intent(context,FoodsShopActivity.class);
//							//将商家id 传过去
//							FoodShopsModel model1 = new FoodShopsModel();	
//							model1.setArea("");
//							model1.setLat(message.getLat());
//							model1.setLng(message.getLng());
//							model1.setPreMoney(message.getPreMoney());
//							model1.setShopGrade(message.getGrade());
//							model1.setShopID(message.getmID());
//							model1.setShopImg(message.getImg());
//							model1.setShopName(message.getName());
//							model1.setShopType(message.getType());
//							Bundle b1 = new Bundle();
//							b1.putSerializable("FoodShopsModel", model1);
//							intent1.putExtras(b1);
//							intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//							context.startActivity(intent1);
							Intent intent11 = new Intent(context,ShoppingListActivity.class);
							//将商家id 传过去
//							FoodShopsModel model11 = new FoodShopsModel();	
//							model1.setArea("");
//							model1.setLat(message.getLat());
//							model1.setLng(message.getLng());
//							model1.setPreMoney(message.getPreMoney());
//							model1.setShopGrade(message.getGrade());
//							model1.setShopID(message.getmID());
//							model1.setShopImg(message.getImg());
//							model1.setShopName(message.getName());
//							model1.setShopType(message.getType());
//							Bundle b11 = new Bundle();
//							b11.putSerializable("FoodShopsModel", model1);
//							intent11.putExtras(b11);
							
							intent11.putExtra("shopID",message.getmID());
							intent11.putExtra("shopName", message.getName());
							intent11.putExtra("isColl", true);
							intent11.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
							context.startActivity(intent11);
							break;
						case 3:	//购物商品						
							Intent intent2 = new Intent(context, ShoppingDescActivity.class);
							Bundle b2 = new Bundle();
						
							GoodsModel mGood=new GoodsModel();
							mGood.setGoodsID(message.getmID());
							mGood.setGoodsImg(message.getImg());
							mGood.setGoodsName(message.getName());
							mGood.setGoodsPlace("");
							mGood.setGoodsPrice(message.getNowPrice());
							mGood.setSales(message.getSales());
						
							b2.putSerializable("GoodsModel", mGood);
							intent2.putExtras(b2);
							intent2.putExtra("shopID", message.getShopID());
							intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
							context.startActivity(intent2);
							break;
						case 4://团购商品
							FoodShopsModel model14 = new FoodShopsModel();	
							model14.setArea("");
							model14.setLat(message.getLat());
							model14.setLng(message.getLng());
							model14.setPreMoney(message.getPreMoney());
							model14.setShopGrade(message.getGrade());
							model14.setShopID(message.getShopID());
							model14.setShopImg(message.getImg());
							model14.setShopName(message.getName());
							model14.setShopType(message.getType());
							Log.e("result", message.getType());
							
							Intent intent3 = new Intent();
							intent3.setClass(context, FoodsImmediatelyBuyActivity.class);
							FoodShopFoodsModel model3 = new FoodShopFoodsModel();
							model3.setFoodID(message.getmID());
							model3.setFoodImg(message.getImg());
							model3.setFoodName(message.getName());
							model3.setNowPrice(message.getNowPrice());
							model3.setOldPrice(message.getOldPrice());
							model3.setSales(message.getSales());
							Bundle b3 = new Bundle();
							b3.putSerializable("FoodShopsModel", model14);
							b3.putSerializable("FoodShopFoodsModel", model3);
							intent3.putExtras(b3);
							intent3.putExtra("foodShopID", message.getShopID());
							intent3.putExtra("foodShopName", message.getShopName());
							intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
							context.startActivity(intent3);
							break;
						case 5://物流
							Intent intent4=new Intent(context,WuLiuWebActivity.class);
							intent4.putExtra("orderName", message.getExpress());
							intent4.putExtra("type", message.getExpressName());
							intent4.putExtra("typeStatus", 1);
							intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
							context.startActivity(intent4);
							break;
						case 6://新闻
							Intent intent5=new Intent(context,NewsDescActivity.class);
							intent5.putExtra("newsUrl", message.getNewsUrl());
							intent5.putExtra("title", message.getName());
							intent5.putExtra("img",message.getImg());
							intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
							context.startActivity(intent5);
							break;
						case 7://团购券使用完毕
							Intent intent6=new Intent(context,NewsDescActivity.class);
							intent6.putExtra("newsUrl", message.getNewsUrl());
							intent6.putExtra("img", message.getImg());
							intent6.putExtra("type", 6);
							intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
							context.startActivity(intent6);
							break;
						default:
							break;
						}
					}else {
						ShowContentUtils.showShortToastMessage(context, "推送信息错误！");
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("TAG", e.toString());
				}
				
				//打开自定义的Activity
//	        	Intent i = new Intent(context, MainActivity.class);
//	        	i.putExtras(bundle);
//	        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//	        	context.startActivity(i);
				
		
			}
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.i(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.i(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	/**
	 * 打印所有的 intent extra 数据
	 * @param bundle
	 * @return
	 */
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.i(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	/**
	 * 发送数据到页面
	 * @param context
	 * @param bundle
	 */
	private void processCustomMessage(Context context, Bundle bundle) {
//		if (HomePageActivity.isForeground) {
		String message=bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(HomePageActivity.MESSAGE_RECEIVED_ACTION);
//			if (!message.equals("")&&message!=null) {
//				msgIntent.putExtra("jpushTitle", message);
//			}
//			if (!extras.equals("")&&extras!=null) {
//						msgIntent.putExtra("jpushMsg", extras);
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
	}
}

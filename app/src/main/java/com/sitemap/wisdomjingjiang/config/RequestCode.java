package com.sitemap.wisdomjingjiang.config;

/**
 * com.sitemap.wisdomjingjiang.config.RequestCode
 * @author zhang
 * 接口请求需要用的辨识常量
 * create at 2016年4月26日 上午9:30:35
 */
public class RequestCode {
	
	//	string类型常量
	public static final String ERRORINFO="服务器君突然找不到了，请稍后再试！";//网络连接错误信息
	public static final String NOLOGIN="网络无法连接！";//网络无法连接
	public static final String NAME="请点击查看详情";//app名称
	public static final String REFUNDTITLE = "退款详情";
	/**注册规则*/ 
	public static final String REGISTERTOOT = "密码长度应在6-16位，必须是字母跟数字组合";
	
//	int类型常量
	public static final int LOGIN=0x1001;//登录
	public static final int REQUESTCODE=0x1002;//请求验证码
	public static final int COMPARECODE=0x1003;//对比验证码
	public static final int REGISTER=0x1004;//注册
	public static final int UPDATEPASSWORD=0x1005;//修改密码
	public static final int GETNEWSTYPE=0x1006;//获得新闻类别
	public static final int GETNEWSLIST=0x1007;//获得新闻列表
	public static final int GETNEWSLISTMORE=0x1008;//新闻列表加载更多
	public static final int GETHOTELLIST=0x1009;//获得酒店列表
	public static final int GETHOTELLISTMORE=0x1010;//酒店列表加载更多
	public static final int GETHOTELINFO=0x1011;//获得酒店信息
	public static final int GETHOTELROOM=0x1012;//获得酒店房间
	public static final int GETKTVINFO=0x1013;//获得KTV信息
	public static final int GETKTVROOM=0x1014;//获得KTV房间
	public static final int GETSHOUCANGGOODSFIRST=0x1015;//获得收藏商品
	public static final int GETSHOUCANGGOODSMORE=0x1016;//获得收藏商品
	public static final int GETSHOUCANGSHOPSFIRST=0x1017;//获得收藏商品
	public static final int GETSHOUCANGSHOPSMORE=0x1018;//获得收藏商品
	public static final int DELSHOUCANG=0x1019;//删除收藏
	public static final int UPDATEHEADIMG=0x1020;//修改头像
	public static final int UPDATENIKENAME=0x1021;//修改昵称
	public static final int UPDATESEX=0x1022;//修改性别
	public static final int GETMESSAGE=0x1023;//获得消息列表
	public static final int KTVCOLLECT=0x1024;//收藏KTV商家
	public static final int HOTLECOLLECT=0x1025;//收藏酒店商家
	public static final int FOODSHOPCOLLECT=0x1026;//收藏美食商家
	public static final int SHOPCOLLECT=0x1027;//收藏购物商家
	public static final int UPDATE=0x1028;//更新
	
	/**获取美食商家列表*/ 
	public static final int GETFOODSHOPS = 0x2001;
	/**美食商家信息*/ 
	public static final int GETFOODSHOPINFO = 0x2002;
	/**美食商家  美食*/ 
	public static final int GETFOODSHOPFOODS = 0x2003;
	/**美食商家中 美食详情*/ 
	public static final int GETFOODINFO = 0x2004;	
	/**美食 详情 中 入购物车*/ 
	public static final int ADDFOOD = 0x2005;
	/**轮播信息列表*/ 
	public static final int GETTOPS = 0x2006;
	/**我的收货地址*/ 
	public static final int GETADDRESS = 0x2007;
	/**最新资讯*/ 
	public static final int GETRECENTNEWS = 0x2008;
	/**用户签到*/ 
	public static final int SIGN = 0x2009;
	/**美食推荐*/ 
	public static final int GETFOODRECOMMEND = 0x2010;
	/**美食商家  加载更多*/ 
	public static final int GETFOODSHOPFOODSMORE = 0x2011;
	/**获取省列表*/ 
	public static final int GETPROVINCE = 0x2012;
	/**获取市列表*/ 
	public static final int GETCITY = 0x2013;
	/**获取区列表*/ 
	public static final int GETAREA = 0x2014;
	/**获取美食评价*/ 
	public static final int GETFOODCOMMENTS = 0x2015;
	/**提交订单(购物订单)*/ 
	public static final int ADDORDER = 0x2016;
	/**添加我的收获地址*/ 
	public static final int ADDADDRESS = 0x2017;
	/**删除我的收获地址*/ 
	public static final int DELETEADDRESS = 0x2018;
	/**修改我的收货地址*/ 
	public static final int UPDATEADDRESS = 0x2019;
	/**添加我的收藏---商品*/ 
	public static final int ADDCOLLECTIONTHINGS = 0x2020;
	/**获取我的收藏---商品*/ 
	public static final int GETCOLLECTIONTHINGS = 0x2021;
	/**获取订单中的购物商品订单*/ 
	public static final int GETORDERGOODS = 0x2022;
	/**获取订单中的美食商品订单*/ 
	public static final int GETORDERFOODS = 0x2023;
	/**申请退款*/ 
	public static final int REFUND = 0x2024;
	/**搜索索引*/ 
	public static final int SEARCHINDEX = 0x2025;
	/**搜索结果*/ 
	public static final int SEARCHRESULT = 0x2026;
	/**商品评价*/ 
	public static final int ADDCOMMENT = 0x2027;
	/**删除订单*/ 
	public static final int DELETEORDER = 0x2028;
	/**提交美食订单*/ 
	public static final int ADDFOODSORDER = 0x2029;
	/**获取天气列表*/ 
	public static final int WEATHER = 0x2030;
	/**确认收货（美食/购物商品）*/ 
	public static final int CONFIRMORDER = 0x2031;
	/**确认支付状态页面*/ 
	public static final int ORDERSTATUS = 0x2032;
	/**是否收藏*/ 
	public static final int GETISCOLLECTION = 0x2033;
	/**取消收藏*/ 
	public static final int CANCELCOLLECTION = 0x2034;
	/**查询库存量*/ 
	public static final int GETISINCOUNT = 0x2035;
	/**付款点击 之后延时*/ 
	public static final int PAYDELAY = 0x2036;
	/**查询退款订单状态*/ 
	public static final int CHECKREFUNDSTATUS = 0x2037;
	/**申请仲裁*/ 
	public static final int APPLYJUDGMENT = 0x2038;
	
	public static final int GETBIGGOODS=0x10001;//购物商家大类
	public static final int GETSMALLGOODS=0x10002;//购物商家小类
	public static final int GOODSLIST=0x1003;//购物列表
	public static final int GOODSLISTMORE=0x1011;//购物列表更多
	public static final int GOODSINFO=0x1003;//购物商品信息
	public static final int GOODSSAVE=0x1004;//购物商品收藏
	public static final int GOODSCAR=0x1005;//加入购物车
	public static final int GOODSCART=0x1006;//购物车商品
	public static final int FOODSCART=0x1007;//购物车美食
	public static final int DELGOODS=0x1008;//删除购物车商品
	public static final int BAOLIAOLIST=0x1009;//爆料列表
	public static final int BAOLIAO=0x1010;//爆料
	public static final int DAOJISHI=0x3001;//倒计时
}

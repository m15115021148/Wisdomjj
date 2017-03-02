package com.sitemap.wisdomjingjiang.config;

import java.util.List;

import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;

/**
 * 
 * @ClassName:     WebUrlConfig.java
 * @Description:   网络url（接口）配置文件
 * @author         chenhao
 * @Date           2015-11-14
 */
 
public class WebUrlConfig {
	private static final String HOST_NAME = WebHostConfig.getHostName();
	private static final String LOGIN = HOST_NAME + "userAction_login.do";//登录（返回类LoginModel）
	private static final String REGISTER=HOST_NAME+"userAction_register.do";//注册（返回类CodeModel）
	private static final String REQUESTCODE=HOST_NAME+"userAction_requestCode.do?";//请求验证码（返回类CodeModel）
	private static final String COMPARECODE=HOST_NAME+"userAction_compareCode.do?";//对比验证码（返回类CodeModel）
	private static final String FIXPASSWORD=HOST_NAME+"userAction_fixPassword.do";//找回密码（返回类CodeModel）
	private static final String UPDATEPASSWORD=HOST_NAME+"userAction_updatePassword.do";//修改密码（返回类CodeModel）
	private static final String UPDATEVERSION=HOST_NAME+"userAction_updateVersion.do?";//更新接口（返回类UpdateModel）
	private static final String SIGN=HOST_NAME+"userAction_sign.do?";//签到（返回类SignModel）
	private static final String GETAREAS=HOST_NAME+"userAction_getAreas.do";//获取商圈列表（返回类AreasModel）
	private static final String GETTOPS=HOST_NAME+"userAction_getTops.do";//获取轮播信息列表（返回类TopsModel）
	private static final String GETNEWSTYPE=HOST_NAME+"userAction_getNewsType.do";//获取新闻类别列表（返回类NewsTypeModel）
	private static final String GETNEWSLIST=HOST_NAME+"userAction_getNewsList.do?";//获取新闻列表（返回类NewsListModel）
	private static final String GETRECENTNEWS=HOST_NAME+"userAction_getRecentNews.do";//最新资讯（返回类NewsListModel）
	private static final String GETFOODSHOPS=HOST_NAME+"userAction_getFoodShops.do?";//获取美食商家列表/附近商圈(返回类FoodShopsModel)
	private static final String GETFOODRECOMMEND=HOST_NAME+"userAction_getFoodRecommend.do";//美食推荐((返回类FoodShopsModel))
	private static final String GETFOODSHOPINFO=HOST_NAME+"userAction_getFoodShopInfo.do?";//美食商家详细信息(返回类FoodShopInfoModel)
	private static final String GETFOODSHOPFOODS=HOST_NAME+"userAction_getFoodShopFoods.do?";//美食商家中的美食（返回类FoodShopFoodsModel）
	private static final String GETFOODINFO=HOST_NAME+"userAction_getFoodInfo.do?";//美食详情	（返回类FoodInfoModel）
	private static final String GETFOODCOMMENTS=HOST_NAME+"userAction_getFoodComments.do?";//美食评价（返回类FoodCommentsModel）
	private static final String GETGOODSBIGTYPES=HOST_NAME+"userAction_getGoodsBigTypes.do";//获取购物商品大类别（返回类GoodsBigTypesModel）
	private static final String GOODSMALLTYPES=HOST_NAME+"userAction_getGoodSmallTypes.do?";//获取购物商品小类别（返回类GoodSmallTypesModel）	
	private static final String GETGOODS=HOST_NAME+"userAction_getGoods.do?";//获取购物商品列表（返回类GoodsModel）
	private static final String GETGOODSINFO=HOST_NAME+"userAction_getGoodsInfo.do?";//购物商品详情（返回类GoodsInfoModel）
	private static final String GETGOODSCOMMENT=HOST_NAME+"userAction_getGoodsComment.do?";//购物商品评价(返回类FoodCommentsModel)
	private static final String GETGOODSHOPINFO=HOST_NAME+"userAction_getGoodShopInfo.do?";//购物商家详细信息（返回类GoodShopInfoModel）
	private static final String ADDGOODS=HOST_NAME+"userAction_addGoods.do?";//添加购物商品到购物车（返回类CodeModel）
	private static final String ADDFOOD=HOST_NAME+"userAction_addFood.do?";//添加美食商品到购物车（返回类CodeModel）
	private static final String GETCARTGOODS=HOST_NAME+"userAction_getCartGoods.do?";//获取购物车中购物商品（返回类CartGoodsModel）
	private static final String GETCARTFOODS=HOST_NAME+"userAction_getCartFoods.do?";//获取购物车中美食商品（返回类CartFoodsModel）
	private static final String DELETEGOODS=HOST_NAME+"userAction_deleteGoods.do?";//删除购物车中的商品(美食/购物商品)（返回类CodeModel）	
	private static final String ADDORDER=HOST_NAME+"userAction_addOrder.do?";//提交订单 购物（返回类CodeModel）
	private static final String ADDOFOODSRDER=HOST_NAME+"userAction_addFoodOrder.do?";//提交订单  美食（返回类CodeModel）
	private static final String ADDCOMMENT=HOST_NAME+"userAction_addComment.do?";//商品评价（返回类CodeModel）
	private static final String GETORDERGOODS=HOST_NAME+"userAction_getOrderGoods.do?";//获取订单中的购物商品订单（返回类OrderGoodsModel）
	private static final String GETORDERFOODS=HOST_NAME+"userAction_getOrderFoods.do?";//获取订单中的美食商品订单（返回类OrderFoodsModel）	
	private static final String CONFIRMORDER=HOST_NAME+"userAction_confirmOrder.do?";//确认收货（美食/购物商品）（返回类CodeModel）
	private static final String GETKTVSHOPS=HOST_NAME+"userAction_getKtvShops.do?";//获取ktv商家列表(返回类FoodShopsModel)
	private static final String GETHOTELSHOPS=HOST_NAME+"userAction_getHotelShops.do?";//获取酒店商家列表(返回类FoodShopsModel)	
	private static final String GETKTVSHOPINFO=HOST_NAME+"userAction_getKtvShopInfo.do?";//ktv商家详细信息(返回类FoodShopInfoModel)
	private static final String GETKTVSHOPROOM=HOST_NAME+"userAction_getKtvShopRoom.do?";//ktv商家中的房间信息(返回类KtvShopRoomModel)
	private static final String GETHOTELSHOPINFO=HOST_NAME+"userAction_getHotelShopInfo.do?";//酒店商家详细信息(返回类FoodShopInfoModel)
	private static final String GETHOTELSHOPROOM=HOST_NAME+"userAction_getHotelShopRoom.do?";//酒店商家中的房间信息(返回类HotelShopRoomModel)
	private static final String GETCOLLECTIONSHOPS=HOST_NAME+"userAction_getCollectionShops.do?";//获取我的收藏---商家(返回类FoodShopsModel)	
	private static final String GETCOLLECTIONTHINGS=HOST_NAME+"userAction_getCollectionThings.do?";//获取我的收藏---商品(返回类CollectionThingsModel)	
	private static final String ADDCOLLECTIONSHOPS=HOST_NAME+"userAction_addCollectionShops.do?";//添加我的收藏---商家(返回类CodeModel)	
	private static final String ADDCOLLECTIONTHINGS=HOST_NAME+"userAction_addCollectionThings.do?";//添加我的收藏---商品(返回类CodeModel)
	private static final String DELETECOLLECTIONSHOPS=HOST_NAME+"userAction_deleteCollectionShops.do?";//删除我的收藏---商品(返回类CodeModel)	
	private static final String DELETECOLLECTIONTHINGS=HOST_NAME+"userAction_deleteCollectionThings.do?";//删除我的收藏---商品(返回类CodeModel)
	private static final String GETADDRESS=HOST_NAME+"userAction_getAddress.do?";//获取我的收获地址(返回类AddressModel)
	private static final String DELETEADDRESS=HOST_NAME+"userAction_deleteAddress.do?";//删除我的收获地址(返回类CodeModel)
	private static final String ADDADDRESS=HOST_NAME+"userAction_addAddress.do?";//添加我的收获地址(返回类CodeModel)
	private static final String UPDATEADDRESS=HOST_NAME+"userAction_updateAddress.do?";//修改我的收获地址(返回类CodeModel)
	private static final String GETPROVINCE=HOST_NAME+"userAction_getProvince.do?";//获取省列表(返回类ProvinceModel)
	private static final String GETCITY=HOST_NAME+"userAction_getCity.do?";//获取市列表(返回类CityModel)
	private static final String GETAREA=HOST_NAME+"userAction_getArea.do?";//获取区列表(返回类AreaModel)
	private static final String ADDNEWS=HOST_NAME+"userAction_addNews.do?";//新增爆料(返回类CodeModel)
	private static final String LOOKNEWS=HOST_NAME+"userAction_lookNews.do?";//查看爆料(返回类LookNewsModel)
	private static final String REFUND=HOST_NAME+"userAction_refund.do";//申请退款(返回类CodeModel)
	private static final String UPDATEHEADIMG=HOST_NAME+"userAction_updateHeadImg.do";//编辑头像(返回类CodeModel)
	private static final String UPDATENICKNAME=HOST_NAME+"userAction_updateNickname.do?";//编辑昵称(返回类CodeModel)
	private static final String UPDATESEX=HOST_NAME+"userAction_updateSex.do?";//编辑性别(返回类CodeModel)
	private static final String UPDATEBIRTHDAY=HOST_NAME+"userAction_updateBirthday.do?";//编辑出生日期(返回类CodeModel)
	private static final String UPDATEWORK=HOST_NAME+"userAction_updateWork.do?";//编辑职业(返回类CodeModel)
	private static final String SEARCHINDEX = HOST_NAME+"userAction_searchIndex.do?";//搜索索引(返回类SearchIndexModel)
	private static final String SEARCHRESULT = HOST_NAME+"userAction_searchResult.do?";//搜索结果(返回类SearchResultModel)
	private static final String DELETEORDER = HOST_NAME+"userAction_deleteOrder.do?";//删除订单(返回类CodeModel)
	private static final String GETMESSAGE = HOST_NAME+"userAction_getMessage.do?";//消息列表(返回类MessageModel)
	private static final String WEATHER = HOST_NAME+"userAction_weather.do";//天气列表(返回WeatherModel类)
	
	private static final String TOADVICE=HOST_NAME+"userAction_toAdvice";//用户反馈(问题和意见)
	private static final String ORDERSTATUS = HOST_NAME + "userAction_orderStatus.do?";//确认支付状态页面

	private static final String GETISCOLLECTION = HOST_NAME + "userAction_getIsCollection.do?";//获取是否收藏
	private static final String CANCELCOLLECTION = HOST_NAME + "userAction_cancelCollection.do?";//取消收藏
	private static final String GETISINCOUNT = HOST_NAME + "userAction_getIsInCount.do?";//查询是否超过库存
	private static final String CHECKREFUNDSTATUS = HOST_NAME + "userAction_checkRefundStatus.do?";//查询退款订单状态
	private static final String GETTYPEFOODS = HOST_NAME + "userAction_getTypeFoods.do?";//团购商品详情内的推荐
	private static final String APPLYJUDGMENT = HOST_NAME + "userAction_applyJudgment.do?";//申请仲裁

	/**
	 * 登陆操作
	 * 
	 * @param userID 用户名（手机号）
	 * @param password 用户登录密码 (md5加密)
	 * @return
	 */
	public static String login() {
		return LOGIN;
	}
	
	/**
	 * 签到
	 * 
	 * @param userID 用户名（手机号）
	 * @return
	 */
	public static String sign(String userID) {
		return SIGN + "userID=" + userID;
	}
	
	/**
	 * 团购商品详情内的推荐
	 * 
	 * @param foodTypeID 商品类别ID
	 * @return
	 */
	public static String getTypeFoods(String foodTypeID,String foodID) {
		return GETTYPEFOODS + "foodTypeID=" + foodTypeID+"&foodID="+foodID;
	}
	
	/**
	 * 获取商圈列表
	 * 
	 * @return
	 */
	public static String getAreas() {
		return GETAREAS;
	}
	
	
	/**
	 * 获取美食商家列表/附近商圈
	 * 
	 * @param lng 经度（用户当前位置）
	 * @param lat 纬度（用户当前位置）
	 * @param page 页码（用于分页查询，默认为0）
	 * @return
	 */
	public static String getFoodShops(String lng , String lat,String page) {
		return GETFOODSHOPS + "lng=" + lng  + "&lat=" + lat+"&page="+page;
	}
	
	/**
	 * 请求验证码
	 * @param phoneNumber 手机号
	 * @return
	 */
	public static String requestCode(String phoneNumber) {
		return REQUESTCODE + "phoneNumber=" + phoneNumber;
	}
	
	/**
	 * 注册
	 * @param phoneNumber 手机号
	 * @param password  密码（md5加密）
	 * @return
	 */
	public static String register() {
		return REGISTER;
	}
	
	/**
	 * 获取轮播信息列表
	 * @return
	 */
	public static String getTops() {
		return GETTOPS;
	}
	
	/**
	 * 美食推荐
	 * @return
	 */
	public static String getFoodRecommend() {
		return GETFOODRECOMMEND;
	}
	
	/**
	 * 美食商家详细信息
	 * @param shopID
	 * @return
	 */
	public static String getFoodShopInfo(String shopID) {
		return GETFOODSHOPINFO + "shopID=" + shopID;
	}
	
	/**
	 * 美食商家中的美食
	 * @param shopID 商家id
	 * @return
	 */
	public static String getFoodShopFoods(String shopID) {
		return GETFOODSHOPFOODS + "shopID=" + shopID;
	}
	
	/**
	 * 更新接口
	 * @param platform 请求平台（1：android，2：IOS）
	 * @return
	 */
	public static String updateVersion() {
		return UPDATEVERSION+ "platform=1";
	}
	
	/**
	 * 获取新闻类别列表
	 * @return
	 */
	public static String getNewsType() {
		return GETNEWSTYPE;
	}
	
	/**
	 * 获取新闻列表
	 * @param newsTypeID  新闻类别id
	 * @param page 页码（用于分页查询，默认为0）
	 * @return
	 */
	public static String getNewsList(String newsTypeID,String page) {
		return GETNEWSLIST + "newsTypeID=" + newsTypeID+"&page="+page;
	}
	
	/**
	 * 最新资讯
	 * 
	 * @return
	 */
	public static String getRecentNews() {
		return GETRECENTNEWS;
	}
	
	/**
	 * 美食详情
	 * @param foodID 美食id
	 * @return
	 */
	public static String getFoodInfo(String foodID) {
		return GETFOODINFO + "foodID=" + foodID;
	}
	
	
	/**
	 *  美食评价
	 * @param foodID 美食id
	 * @param page 页码（用于分页查询，默认为0）
	 * @return
	 */
	public static String getFoodComments(String foodID,String page) {
		return GETFOODCOMMENTS + "foodID=" + foodID+"&page="+page;
	}
	
	/**
	 * 获取购物商品大类别
	 * @return
	 */
	public static String getGoodsBigTypes() {
		return GETGOODSBIGTYPES;
	}
	
	/**
	 * 找回密码
	 * @param phoneNumber 手机号
	 * @param password 新密码
	 * @return
	 */
	public static String fixPassword() {
		return FIXPASSWORD;
	}
	
	/**
	 * 获取购物商品小类别
	 * @param bigTypeID 大类别id
	 * @return
	 */
	public static String getGoodSmallTypes(String bigTypeID) {
		return GOODSMALLTYPES + "bigTypeID=" + bigTypeID;
	}
	
	/**
	 * 对比验证码
	 * @param phoneNumber 手机号
	 * @param code 验证码
	 * @return
	 */
	public static String compareCode(String phoneNumber,String code) {
		return COMPARECODE + "phoneNumber=" + phoneNumber+"&code="+code;
	}
	
	/**
	 * 修改密码
	 * @param userID 用户id
	 * @param oldPassword 旧密码
	 * @param password 新密码
	 * @return
	 */
	public static String updatePassword() {
		return UPDATEPASSWORD;
	}
	
	
	/**
	 * 获取购物商品列表
	 * @param smallTypeID 小类别id
	 * @param page 页码（用于分页查询，默认为0）
	 * @return
	 */
	public static String getGoods(String smallTypeID,String page) {
		return GETGOODS + "smallTypeID=" + smallTypeID+"&page="+page;
	}
	
	
	/**
	 * 购物商品详情
	 * @param goodsID 购物商品id
	 * @return
	 */
	public static String getGoodsInfo(String goodsID) {
		return GETGOODSINFO + "goodsID=" + goodsID;
	}
	
	/**
	 * 购物商品评价
	 * @param goodsID 购物商品id
	 * @param page 页码（用于分页查询，默认为0）
	 * @return
	 */
	public static String getGoodsComment(String goodsID,String page) {
		return GETGOODSCOMMENT + "goodsID=" + goodsID+"&page="+page;
	}
	
	
	
	
	/**
	 * 购物商家详细信息
	 * @param goodShopID 购物商家id
	 * @param page 页码（用于分页查询，默认为0）
	 * @return
	 */
	public static String getGoodShopInfo(String goodShopID,String page) {
		return GETGOODSHOPINFO + "goodShopID=" + goodShopID+"&page="+page;
	}
	
	
	/**
	 * 添加购物商品到购物车
	 * @param userID 用户ID
	 * @param goodsID 购物商品id
	 * @param goodShopID       商家id
	 * @param goodShopName    商家名称
	 * @return
	 */
	public static String addGoods(String userID,String goodsID,String goodShopID,String goodShopName,String color,String size,String number) {
		return ADDGOODS + "userID=" + userID+"&goodsID=" + goodsID+"&goodShopID=" + goodShopID+"&goodShopName=" + goodShopName+"&color="+color+"&size="+size+"&number="+number;
	}
	
	/**
	 * 添加美食商品到购物车
	 * @param userID 用户ID
	 * @param foodID 美食商品id
	 * @param foodShopID       商家id
	 * @param foodShopName    商家名称
	 * @param number    数量
	 * @return
	 */
	public static String addFood(String userID,String foodID,String foodShopID,String foodShopName,String number) {
		return ADDFOOD + "userID=" + userID+ "&foodID=" + foodID+"&foodShopID=" + foodShopID+"&foodShopName=" + foodShopName+"&number=" + number;
	}
	/**
	 * 获取购物车中购物商品
	 * @param userID 用户id
	 * @return
	 */
	public static String getCartGoods(String userID) {
		return GETCARTGOODS+ "userID="+userID;
	}
	
	/**
	 * 获取购物车中美食商品
	 * @param userID
	 * @return
	 */
	public static String getCartFoods(String userID) {
		return GETCARTFOODS + "userID=" + userID;
	}
	
	
	/**
	 * 删除购物车中的商品(美食/购物商品)
	 * 
	 * @param userID         用户id
     * @param itemID         购物车条目id
     * @param type          商品类别（1：美食 2：购物商品）
	 * @return
	 */
	public static String deleteGoods(String userID,String type,String itemID) {
		return DELETEGOODS + "userID=" + userID+"&type="+type+"&itemID="+itemID;
	}
	
	/**
	 * 提交订单(购物商品)
	 * @param userID   用户id
	 * @param shopID   商家id
	 * @param addressID   地址id
	 * @param type 提交订单方式（1 购物车  2 立即购买）
	 * @param goodsInfo   商品信息（里面存储的是一个JSONArray对象）
	 * @return
	 */
	public static String addOrder(String userID,String shopID,String addressID,String type,String goodsInfo) {
		return ADDORDER + "userID=" + userID+"&shopID="+shopID+"&addressID="+addressID+"&type="+type+"&goodsInfo="+goodsInfo;
	}
	
	/**
	 * 商品评价
	 * @param userID         用户id
	 * @param thingID        商品id
	 * @param type          商品类别（1：美食 2：购物商品）
	 * @param content       评价内容
	 * @param grade         评分
	 * @param isHide        是否匿名评价  （1：是，2否）
	 * @return
	 */
	public static String addComment(String userID,String thingID,String type,String content,String grade,String isHide,String orderInfoID) {
		return ADDCOMMENT + "userID="+userID+"&thingID="+thingID+"&type="+type+"&content="+content+"&grade="+grade+"&isHide="+isHide+"&orderInfoID="+orderInfoID;
	}
	
	/**
	 * 获取订单中的购物商品订单
	 * @param userID:          用户id
	 * @param orderType       订单类型(0：全部 1:待付款 2:待发货 3:待收货 4:未评价)
	 * @return
	 */
	public static String getOrderGoods(String userID,String orderType) {
		return GETORDERGOODS + "userID="+userID+"&orderType="+orderType;
	}
	
	
	/**
	 * 用户反馈(问题和意见)
	 * @return
	 */
	public static String toAdvice() {
		return TOADVICE ;
	}
	
	
	/**
	 * 获取订单中的美食商品订单
	 * @param userID:          用户id
	 * @param orderType       订单类型(0：全部 1:待付款 2:待发货 3:待收货 4:未评价)
	 * @return
	 */
	public static String getOrderFoods(String userID,String orderType) {
		return GETORDERFOODS+ "userID="+userID+"&orderType="+orderType;
	}
	
	/**
	 * 确认收货（美食/购物商品）
	 * @param userID:          用户id
	 * @param orderID       订单id
	 * @return
	 */
	public static String confirmOrder(String userID,String orderID) {
		return CONFIRMORDER+ "userID="+userID+"&orderID="+orderID;
	}
	
	
	/**
	 * 获取ktv商家列表
	 * @param lng 经度（用户当前位置）
	 * @param lat 纬度（用户当前位置）
	 * @param page 页码（用于分页查询，默认为0）
	 * @return
	 */
	public static String getKtvShops(String lng , String lat,String page) {
		return GETKTVSHOPS + "lng=" + lng  + "&lat=" + lat+"&page="+page;
	}
	
	/**
	 * 获取酒店商家列表
	 * @param lng 经度（用户当前位置）
	 * @param lat 纬度（用户当前位置）
	 * @param page 页码（用于分页查询，默认为0）
	 * @return
	 */
	public static String getHotelShops(String lng , String lat,String page) {
		return GETHOTELSHOPS + "lng=" + lng  + "&lat=" + lat+"&page="+page;
	}
	
	/**
	 * ktv商家详细信息
	 * @param shopID
	 * @return
	 */
	public static String getKtvShopInfo(String shopID) {
		return GETKTVSHOPINFO + "shopID=" + shopID;
	}
	
	/**
	 * ktv商家中的房间信息
	 * @param shopID
	 * @return
	 */
	public static String getKtvShopRoom(String shopID) {
		return GETKTVSHOPROOM + "shopID=" + shopID;
	}
	
	/**
	 * 酒店商家详细信息
	 * @param shopID
	 * @return
	 */
	public static String getHotelShopInfo(String shopID) {
		return GETHOTELSHOPINFO + "shopID=" + shopID;
	}
	
	/**
	 * 酒店商家中的房间信息
	 * @param shopID
	 * @return
	 */
	public static String getHotelShopRoom(String shopID) {
		return GETHOTELSHOPROOM + "shopID=" + shopID;
	}
	
	/**
	 * 获取我的收藏---商家
	 * @param userID  用户id
	 * @param page 页码（用于分页查询，默认为0）
	 * @return
	 */
	public static String getCollectionShops(String userID,String page) {
		return GETCOLLECTIONSHOPS + "userID=" + userID+"&page="+page;
	}
	
	/**
	 * 获取我的收藏---商品
	 * @param userID  用户id
	 * @param page 页码（用于分页查询，默认为0）
	 * @return
	 */
	public static String getCollectionThings(String userID,String page) {
		return GETCOLLECTIONTHINGS + "userID=" + userID+"&page="+page;
	}
	
	/**
	 * 添加我的收藏---商家
	 * @param userID  用户id
	 * @param shopID 商家id
	 * @param shopType    （3:购物商家 4:美食商家 5:KTV商家，6酒店）
	 * @return
	 */
	public static String addCollectionShops(String userID,String shopID,String shopType) {
		return ADDCOLLECTIONSHOPS + "userID=" + userID+"&shopID="+shopID+"&shopType="+shopType;
	}
	
	/**
	 * 添加我的收藏---商品
	 * @param userID  用户id
	 * @param thingID 商品id
	 * @param thingType     类别（1:美食商品 2:购物商品）
	 * @return
	 */
	public static String addCollectionThings(String userID,String thingID,String shopType) {
		return ADDCOLLECTIONTHINGS + "userID=" + userID+"&thingID="+thingID+"&thingType="+shopType;
	}
	
	/**
	 * 删除我的收藏---商家
	 * @param userID  用户id
	 * @param shopID 商家id
	 * @return
	 */
	public static String deleteCollectionShops(String userID,String shopID) {
		return DELETECOLLECTIONSHOPS + "userID=" + userID+"&shopID="+shopID;
	}
	
	/**
	 * 删除我的收藏---商品
	 * @param userID  用户id
	 * @param thingID 商品id
	 * @return
	 */
	public static String deleteCollectionThings(String userID,String thingID,String thingType) {
		return DELETECOLLECTIONTHINGS + "userID=" + userID+"&thingID="+thingID+"&thingType="+thingType;
	}
	
	/**
	 * 获取我的收获地址
	 * @param userID 用户id
	 * @return
	 */
	public static String getAddress(String userID) {
		return GETADDRESS+ "userID="+userID;
	}
	
	/**
	 * 删除我的收获地址
	 * @param userID  用户id
	 * @param addressID 收获地址id
	 * @return
	 */
	public static String deleteAddress(String userID,String addressID) {
		return DELETEADDRESS + "userID=" + userID+"&addressID="+addressID;
	}
	
	/**
	 * 添加我的收获地址
	 * @param userID  用户id
	 * @param linkman      联系人
	 * @param linkphone     联系电话
	 * @param provinceID     省id
	 * @param cityID          城市id
	 * @param areaID         区id
	 * @param addressInfo     地址具体信息
	 * @param isDefault      是否设为默认收获地址（1是，2否）
	 * @return
	 */
	public static String addAddress(String userID,String linkman,String linkphone,String provinceID,String cityID,String areaID,String addressInfo,String isDefault) {
		return ADDADDRESS + "userID=" + userID+"&linkman="+linkman+"&linkphone="+linkphone+"&provinceID="+provinceID+"&cityID="+cityID+"&areaID="+areaID+"&addressInfo="+addressInfo+"&isDefault="+isDefault;
	}
	
	/**
	 * 修改我的收获地址
	 * @param userID  用户id
	 * @param linkman      联系人
	 * @param linkphone     联系电话
	 * @param provinceID     省id
	 * @param cityID          城市id
	 * @param areaID         区id
	 * @param addressInfo     地址具体信息
	 * @param isDefault      是否设为默认收获地址（1是，2否）
	 * @return
	 */
	public static String updateAddress(String userID,String linkman,String linkphone,String provinceID,String cityID,String areaID,String addressInfo,String isDefault,String addressID) {
		return UPDATEADDRESS + "userID=" + userID+"&linkman="+linkman+"&linkphone="+linkphone+"&provinceID="+provinceID+"&cityID="+cityID+"&areaID="+areaID+"&addressInfo="+addressInfo+"&isDefault="+isDefault+"&addressID="+addressID;
	}
	
	/**
	 * 获取省列表
	 * @return
	 */
	public static String getProvince() {
		return GETPROVINCE;
	}
	
	/**
	 * 获取市列表
	 * @param provinceID 省id
	 * @return
	 */
	public static String getCity(String provinceID) {
		return GETCITY+"provinceID="+provinceID;
	}
	
	/**
	 * 获取区列表
	 * @param cityID 城市id
	 * @return
	 */
	public static String getArea(String cityID) {
		return GETAREA+"cityID="+cityID;
	}
	
	/**
	 * 新增爆料
	 * @param userID  用户id
	 * @param title           标题
	 * @param content        内容描述
	 * @param isHide         是否匿名发布  （1：是，2否）
	 * @param img           图片
	 * @param voice          音频
	 * @param video          视频
	 * @return
	 */
	public static String addNews() {
		return WebHostConfig.getHostAddress()+"WisdomJingJiangAdmin/newstype/userAction_addNews.map";
	}
	
	/**
	 * 查看爆料
	 * @param userID 用户id
	 * @return
	 */
	public static String lookNews(String userID) {
		return LOOKNEWS+ "userID="+userID;
	}

	/**
	 * 申请退款
	 * @param userID 用户id
	 * @param orderID         订单id
	 * @param reason          退款原因
	 * @param info            退款说明
	 * @param img             图片
	 * @return
	 */
	public static String refund() {
		return REFUND;
	}
	
	/**
	 * 编辑头像
	 * @param userID 用户id
	 * @param img    图片
	 * @return
	 */
	public static String updateHeadImg() {
		return UPDATEHEADIMG;
	}
	
	/**
	 * 编辑昵称
	 * @param userID 用户id
	 * @param nickname       昵称
	 * @return
	 */
	public static String updateNickname(String userID,String nickname) {
		return UPDATENICKNAME+ "userID="+userID+"&nickname="+nickname;
	}
	
	/**
	 * 编辑性别
	 * @param userID 用户id
	 * @param sex            性别
	 * @return
	 */
	public static String updateSex(String userID,String sex) {
		return UPDATESEX+ "userID="+userID+"&sex="+sex;
	}
	
	/**
	 * 编辑出生日期
	 * @param userID 用户id
	 * @param birthday        出身日期
	 * @return
	 */
	public static String updateBirthday(String userID,String birthday) {
		return UPDATEBIRTHDAY+ "userID="+userID+"&birthday="+birthday;
	}
	
	/**
	 * 编辑职业
	 * @param userID 用户id
	 * @param work           职业
	 * @return
	 */
	public static String updateWork(String userID,String work) {
		return UPDATEWORK+ "userID="+userID+"&work="+work;
	}
	
	/**
	 * 搜索索引
	 * @param key 搜索词
	 * @return
	 */
	public static String searchIndex(String key){
		return SEARCHINDEX + "key="+key;
	}
	
	/**
	 * 搜索结果
	 * @param index 索引
	 * @return
	 */
	public static String searchResult(String index){
		return SEARCHRESULT + "index="+index;
	}
	
	/**
	 * 删除订单
	 * @param orderID 订单号
	 * @return
	 */
	public static String deleteOrder(String orderID,String type){
		return DELETEORDER + "orderID="+orderID + "&type="+type;
	}
	
	/**
	 * 提交美食订单
	 * @param userID  用户id
	 * @param shopID  商家id
	 * @param type 提交订单方式（1 购物车  2 立即购买）
	 * @param foodsInfo  商品信息（里面存储的是一个JSONArray对象）
	 * @return
	 */
	public static String addFoodsOrder(String userID,String shopID,String type,String foodsInfo){
		return ADDOFOODSRDER +"userID="+userID+"&shopID="+shopID+"&type="+type+"&foodsInfo="+foodsInfo;
	}
	
	/**
	 *消息列表
	 * 
	 * @param userID 用户名（手机号）
	 * @return
	 */
	public static String getMessage(String userID) {
		return GETMESSAGE + "userID=" + userID;
	}
	
	/**
	 * 获取天气
	 * 
	 * @return
	 */
	public static String getWeather() {
		return WEATHER;
	}
	
	/**
	 * 确认支付状态页面
	 * @param orderID 订单id
	 * @return
	 */
	public static String getOrderStatus(String orderID){
		return ORDERSTATUS + "orderID=" + orderID;
	}
	
	/**
	 * 获取是否收藏
	 * @param userID   用户id 
	 * @param type    类别（1:团购商品 2:购物商品 3:购物商家 4:美食商家 5:KTV商家，6酒店）
	 * @param collectionID  收藏的id（商家或者商品的ID）
	 * @return
	 */
	public static String getIsCollection(String userID,String type,String collectionID){
		return GETISCOLLECTION+"userID="+userID+"&type="+type+"&collectionID="+collectionID;
	}
	
	/**
	 * 取消收藏
	 * @param userID   用户id 
	 * @param type    类别（1:团购商品 2:购物商品 3:购物商家 4:美食商家 5:KTV商家，6酒店）
	 * @param collectionID  收藏的id（商家或者商品的ID）
	 * @return
	 */
	public static String cancelCollection(String userID,String type,String collectionID){
		return CANCELCOLLECTION+"userID="+userID+"&type="+type+"&collectionID="+collectionID;
	}
	
	/**
	 * 查询是否超过库存量
	 * @param orderID
	 * @return
	 */
	public static String getIsInCount(String orderID){
		return GETISINCOUNT + "orderID=" + orderID;
	}
	
	/**
	 * 查询退款订单状态
	 * @param orderID
	 * @return
	 */
	public static String checkRefundStatus(String orderID,String type){
		return CHECKREFUNDSTATUS + "orderID="+orderID +"&type="+type;
	}
	
	/**
	 * 申请仲裁
	 * @param orderInfoID  订单详细id
	 * @return
	 */
	public static String applyJudgment(String orderInfoID){
		return APPLYJUDGMENT + "orderInfoID=" + orderInfoID;
	}
}

	
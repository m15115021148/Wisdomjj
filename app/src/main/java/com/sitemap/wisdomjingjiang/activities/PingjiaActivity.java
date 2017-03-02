package com.sitemap.wisdomjingjiang.activities;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

/**
 * com.sitemap.wisdomjingjiang.activities.PingjiaActivity
 * @author zhang
 * create at 2016年5月13日 下午3:13:57
 * 评价页面
 */

public class PingjiaActivity extends BaseActivity implements OnClickListener{
	/**本类*/ 
	private PingjiaActivity mContext;
	/**返回按钮*/ 
	private ImageView mBack;
	/**标题*/ 
	private TextView mTitle;
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/**评论的内容*/ 
	private EditText mInfo;
	/**提交*/ 
	private TextView mSub;
	/**是否匿名评论*/ 
	private CheckBox mIsHide;
	/**userid*/ 
	private String userID = MyApplication.loginModel.getUserID();
	/**商品id*/ 
	private String thingID;
	/**商品类别（1：团购 2：购物商品）*/ 
	private String type;
	/**评分*/ 
	private String grade = "0.0";
	/**评价内容*/ 
	private String content;
	/**是否匿名评价（1：是，2否）*/ 
	private int isHide = 1;
	/**订单详细项id*/ 
	private String orderInfoID;
	/**购物商品的goodsinfo的实体类*/ 
	private CartGoodsInfoModel goodsModel;
	/**团购商品的foodsinfo的实体类*/ 
	private CartFoodsInfoModel foodsModel;
	/**商品图片*/ 
	private ImageView mImg;
	/**评分按钮*/ 
	private RatingBar mRb;
	/**评分*/ 
	private TextView mGrade;
	/**商品的名称  颜色  尺寸  价格*/ 
	private TextView mName,mColor,mSize,mPrice;
	/**是否显示*/ 
	private LinearLayout mIsShow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pingjia);
		mContext=this;
		initView();// 初始化view
		initData();// 初始化数据
		
	}
	/**
	 * 初始化控件view
	 */
	public void initView() {
		mBack = (ImageView) findViewById(R.id.ping_jia_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.ping_jia_title).findViewById(R.id.title);
		mInfo = (EditText) findViewById(R.id.comment_brief);
		mSub = (TextView) findViewById(R.id.comment_sub);
		mIsHide = (CheckBox) findViewById(R.id.comment_is_hide);
		mImg = (ImageView) findViewById(R.id.comment_img);
		mRb = (RatingBar) findViewById(R.id.foods_lv);
		mGrade = (TextView) findViewById(R.id.foods_grade);
		mName = (TextView) findViewById(R.id.comment_shop_name);
		mColor = (TextView) findViewById(R.id.comment_color);
		mSize = (TextView) findViewById(R.id.comment_size);
		mPrice = (TextView) findViewById(R.id.comment_price);
		mIsShow = (LinearLayout) findViewById(R.id.is_show);
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		mBack.setOnClickListener(this);
		mTitle.setText("发表评论");
		mSub.setOnClickListener(this);
		
		if(http == null){
			http = new HttpUtil(handler);
		}
		
		mGrade.setText("0.0分");
		type = getIntent().getStringExtra("type");
		if(type.equals("1")){//团购评价
			mIsShow.setVisibility(View.GONE);
			foodsModel = (CartFoodsInfoModel) getIntent().getSerializableExtra("CartFoodsInfoModel");
			thingID = foodsModel.getFoodsID();
			orderInfoID = foodsModel.getOrderInfoID();
			x.image().bind(mImg, foodsModel.getFoodsImg(),MyApplication.imageOptions);
			mName.setText(foodsModel.getFoodsName());
			mPrice.setText("￥"+foodsModel.getFoodsPrice());
		}else{//购物商品评价
			mIsShow.setVisibility(View.VISIBLE);
			goodsModel = (CartGoodsInfoModel) getIntent().getSerializableExtra("CartGoodsInfoModel");
			thingID = goodsModel.getGoodsID();
			orderInfoID = goodsModel.getOrderInfoID();
			x.image().bind(mImg, goodsModel.getGoodsImg(),MyApplication.imageOptions);
			mName.setText(goodsModel.getGoodsName());
			mColor.setText(goodsModel.getColor());
			mSize.setText(goodsModel.getSize());
			mPrice.setText("￥"+goodsModel.getGoodsPrice());
		}
		
		mRb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				grade = String.valueOf(ratingBar.getRating());
				mGrade.setText(grade+"分");
			}
		});
		
		mIsHide.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					isHide = 1;
				}else{
					isHide = 2;
				}
			}
		});
	}
	
	/**
	 * 商品评论
	 */
	private void sendCommentData(){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			content = mInfo.getText().toString();
			String addComment = WebUrlConfig.addComment(userID, thingID, type, content, 
					grade, String.valueOf(isHide),orderInfoID);
			RequestParams params = http.getParams(addComment);
			http.sendPost(RequestCode.ADDCOMMENT, params);
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();// 关闭进度条
			}
			super.handleMessage(msg);
			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				if (msg.arg1 == RequestCode.ADDCOMMENT) {
					CodeModel model = JSONObject.parseObject(msg.obj.toString(), CodeModel.class);
					if(model.getStatus() == 0){
						ShowContentUtils.showLongToastMessage(mContext, "感谢您的评价");
						setResult(RequestCode.ADDCOMMENT);
						finish();
					}else{
						ShowContentUtils.showLongToastMessage(mContext, model.getErrorMsg());
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.ADDCOMMENT) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}
	};	
	
	@Override
	public void onClick(View v) {
		if(v == mBack){
			onBackPressed();
			finish();
		}
		if(v == mSub){//提交
			if(!MyApplication.isNull(mInfo)){
				ShowContentUtils.showLongToastMessage(mContext, "内容不能为空");
				return ;
			}
			sendCommentData();
		}
	}
}

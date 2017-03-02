package com.sitemap.wisdomjingjiang.activities;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.alipay.Base64;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartGoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.OrderFoodsModel;
import com.sitemap.wisdomjingjiang.models.OrderGoodsModel;
import com.sitemap.wisdomjingjiang.utils.FileUtils;
import com.sitemap.wisdomjingjiang.utils.ImageFactory;
import com.sitemap.wisdomjingjiang.utils.ImageUtil;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;
import com.sitemap.wisdomjingjiang.views.PhotoCameraDialog;

/**
 * com.sitemap.wisdomjingjiang.activities.RefundActivity
 * 
 * @author zhang create at 2016年5月13日 下午3:13:57 申请退款页面
 */

public class RefundActivity extends BaseActivity implements OnClickListener {
	/** 本类 */
	private RefundActivity mContext;
	/** 返回按钮 */
	private ImageView mBack;
	/** 标题 */
	private TextView mTitle;
	/** 网络请求 */
	private HttpUtil http;
	/** 进度条 */
	private static MyProgressDialog progressDialog;
	/** 退款原因 spinner */
	private Spinner mSpinner;
	/** 退款原因 */
	private static final String result[] = { "请选择", "与卖家协商一致退款", "物流公司发货问题",
			"卖家虚假发货", "其他" };
	/** 上传图片的view */
	private ImageView pic1, pic2, pic3;
	/** 上传图片 */
	private TextView mAddImg;
	/** 第几张图片 */
	private static int pic = 1;
	/** 图片的url */
	private String url1, url2, url3;
	/** 图片 */
	private Bitmap bt1, bt2, bt3;
	/** 路径 */
	private static final String savePath = FileUtils.SDK_PATH + "/sitemap/";
	/**用户id*/ 
	private String userID;
	/**订单id or 订单详细项id(团购)*/ 
	private String orderID;
	/**退款原因*/ 
	private String reason;
	/**申请说明*/ 
	private String info;
	/**图片*/ 
	private String img;
	/**订单中的购物商品订单的返回实体类*/ 
	private CartGoodsInfoModel goodsModel;
	/**团购商品订单的返回实体类*/ 
	private CartFoodsInfoModel foodsModel;	
	/**提交退款*/ 
	private TextView mSure;
	/**退款金额 */ 
	private EditText mMoney;
	/**退款说明*/ 
	private EditText mInfo;
	/** 商品类别（1：团购 2：购物商品） */
	private String type;
	private List<String> paths=new ArrayList<String>();// 返回的相片路径list
	private int number = 0;
	private PhotoCameraDialog photo;// 获取图片工具
	private LinearLayout layout;// 整个布局
	private double money=0;//金额
	private double expressAll = 0;//运费
	/**运费是否显示*/ 
	private LinearLayout mShowExpress;
	/**运费*/ 
	private EditText mExpress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refund);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据

	}

	/**
	 * 初始化控件view
	 */
	public void initView() {
		mBack = (ImageView) findViewById(R.id.refund_title).findViewById(
				R.id.back);
		mTitle = (TextView) findViewById(R.id.refund_title).findViewById(
				R.id.title);
		mSpinner = (Spinner) findViewById(R.id.refund_result);
		pic1 = (ImageView) findViewById(R.id.pic1);
		pic2 = (ImageView) findViewById(R.id.pic2);
		pic3 = (ImageView) findViewById(R.id.pic3);
		mAddImg = (TextView) findViewById(R.id.add_pic);
		mSure = (TextView) findViewById(R.id.refund_sub);
		mMoney = (EditText) findViewById(R.id.refund_money);
		mInfo = (EditText) findViewById(R.id.refund_info);
		layout=(LinearLayout)findViewById(R.id.layout);
		mShowExpress = (LinearLayout) findViewById(R.id.li_express);
		mExpress = (EditText) findViewById(R.id.refund_express);
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		mBack.setOnClickListener(this);
		mTitle.setText("申请退款");
		mAddImg.setOnClickListener(this);
		mSure.setOnClickListener(this);

		if (http == null) {
			http = new HttpUtil(handler);
		}

		initSpinnerData();
		
		type = getIntent().getStringExtra("type");
		//获取数据
		if(type.equals("1")){//团购
			mMoney.setFocusable(false);
			mMoney.setClickable(false);
			mMoney.setEnabled(false);
			mExpress.setFocusable(false);
			mExpress.setClickable(false);
			mExpress.setEnabled(false);
			foodsModel = (CartFoodsInfoModel) getIntent().getSerializableExtra("CartFoodsInfoModel");
			int num = 1;
			double price = 0;
			if(!foodsModel.getNumber().equals("")&&!foodsModel.getFoodsPrice().equals("")){
				num = Integer.parseInt(foodsModel.getNumber());
				price = Double.parseDouble(foodsModel.getFoodsPrice());
			}			
			money = num*price;
			mMoney.setText(FileUtils.roundMath(money)+"");
			orderID = foodsModel.getOrderInfoID();
			mExpress.setText("0.0(该类订单没有运费)");
		}else{//购物
			mMoney.setFocusable(false);
			mMoney.setClickable(false);
			mMoney.setEnabled(false);
			goodsModel = (CartGoodsInfoModel) getIntent().getSerializableExtra("CartGoodsInfoModel");
			expressAll = Double.parseDouble(getIntent().getStringExtra("expressAll"));
			int num = 1;
			double price = 0;
			if(!goodsModel.getNumber().equals("")&&!goodsModel.getGoodsPrice().equals("")){
				num = Integer.parseInt(goodsModel.getNumber());
				price = Double.parseDouble(goodsModel.getGoodsPrice());
			}			
			money = num*price;
			mMoney.setText(FileUtils.roundMath(money)+"");
			orderID = goodsModel.getOrderInfoID();	
			mExpress.setHint("请输入退款运费金额");
		}

	}
	
	/**
	 * Spinner初始化
	 */
	private void initSpinnerData(){
		// 设置适配器
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, result);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		mSpinner.setSelection(0, true);
		reason = result[0];
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				reason = result[position];
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}

	/**
	 * 申请退款
	 */
	private void sendRefundData() {
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			info = mInfo.getText().toString();
//			img = changeByte(bt1);
//			Log.e("byte", img);
			RequestParams params = http.getParams(WebUrlConfig.refund());
			params.addParameter("userID", MyApplication.loginModel.getUserID());
			params.addParameter("orderID", orderID);
			params.addParameter("type", type);
			params.addParameter("money", String.valueOf(money));
			params.addParameter("reason", reason);
			params.addParameter("info", info);
			for(int i=0;i<paths.size();i++){
				if(TextUtils.isEmpty(paths.get(i))){
					return;
				}
				params.addBodyParameter("img", new File(paths.get(i)));
			}
			http.uploadFile(RequestCode.REFUND, params);
//			params.addParameter("img", new File(path));
		
//			String refund = WebUrlConfig.refund();
//			RequestParams params = http.getParams(refund);
//			http.sendPost(RequestCode.REFUND, params);
		} else {
			ShowContentUtils
					.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();// 关闭进度条
				}
				if (msg.arg1 == RequestCode.REFUND) {
					CodeModel model = JSON.parseObject(msg.obj.toString(),
							CodeModel.class);
					if (0 == model.getStatus()) {
						ShowContentUtils
								.showShortToastMessage(mContext, "申请退款成功！");
						setResult(RequestCode.REFUND);
						mContext.finish();
					} else {
						ShowContentUtils.showShortToastMessage(mContext,
								model.getErrorMsg());
					}
				}
				break;
			case HttpUtil.FAILURE:// 失败
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();// 关闭进度条
				}
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.REFUND) {
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
//		Intent it = new Intent(Intent.ACTION_VIEW);
//		String path = v.getTag().toString();
//		it.setDataAndType(Uri.fromFile(new File(path)), "image/*");
//		startActivity(it);
		if (v == mBack) {
			pic = 1;
			onBackPressed();
			finish();
		}
		if (v == mAddImg) {// 上传图片
			photo = new PhotoCameraDialog(this, layout);
//			if (pic == 4) {
//				ShowContentUtils.showLongToastMessage(mContext, "最多只能上传3张图片！");
//				return;
//			}
//			Intent intent = new Intent(
//					Intent.ACTION_PICK,
//					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//			startActivityForResult(intent, 0);
		}
		if(v == mSure){//提交
			if(reason.equals(result[0])){
				ShowContentUtils.showLongToastMessage(mContext, "请选择退款原因");
				return;
			}
			if(type.equals("1")){
				money = 0.0;
			}else{//购物
				if(TextUtils.isEmpty(mExpress.getText().toString().trim())){
					ShowContentUtils.showLongToastMessage(mContext, "运费金额不能为空");
					return;
				}				
				if(Double.parseDouble(mExpress.getText().toString().trim())>(expressAll)){
					ShowContentUtils.showLongToastMessage(mContext, "运费金额不能超出订单总运费");
					return;
				}
				money = Double.parseDouble(mExpress.getText().toString().trim());	
			}
			if(!MyApplication.isNull(mInfo)){
				ShowContentUtils.showLongToastMessage(mContext, "退款说明不能为空");
				return;
			}		
			sendRefundData();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String path=null;
		// 相册返回调用
		try {
			if (requestCode == PhotoCameraDialog.IMAGE_CODE
					&& resultCode == Activity.RESULT_OK) {
				if (data != null) {
					Uri originalUri = data.getData(); // 获得图片缩略图的uri
					switch (number) {
					case 0:
						path = ImageUtil.setPhoto(mContext, originalUri, pic1);
						number++;
						pic1.setTag(path);
						paths.add(path);
						break;
					case 1:
						path = ImageUtil.setPhoto(mContext, originalUri, pic2);
						number++;
						pic2.setTag(path);
						paths.add(path);
						break;
					case 2:
						path = ImageUtil.setPhoto(mContext, originalUri, pic3);
						number++;
						pic3.setTag(path);
						paths.add(path);
						mAddImg.setVisibility(View.GONE);
						break;
//					case 3:
//						path = ImageUtil.setPhoto(mContext, originalUri, img4);
//						number++;
//						img4.setTag(path);
//						paths.add(path);
//						break;
//					case 4:
//						path = ImageUtil.setPhoto(context, originalUri, img5);
//						number++;
//						img5.setTag(path);
//						paths.add(path);
//						break;
//					case 5:
//						path = ImageUtil.setPhoto(context, originalUri, img6);
//						number++;
//						img6.setTag(path);
//						paths.add(path);
//						break;
					default:
						break;
					}

				}

			}

			// 相机返回调用
			else if (requestCode == PhotoCameraDialog.TAKE_PICTURE
					&& resultCode == Activity.RESULT_OK) {
				switch (number) {
				case 0:
					path = ImageUtil.setCameraPhoto(mContext, photo, pic1);
					number++;
					pic1.setTag(path);
					paths.add(path);
					break;
				case 1:
					path = ImageUtil.setCameraPhoto(mContext, photo, pic2);
					number++;
					pic2.setTag(path);
					paths.add(path);
					break;
				case 2:
					path = ImageUtil.setCameraPhoto(mContext, photo, pic3);
					number++;
					pic3.setTag(path);
					paths.add(path);
					mAddImg.setVisibility(View.GONE);
					break;
//				case 3:
//					path = ImageUtil.setCameraPhoto(context, photo, img4);
//					number++;
//					img4.setTag(path);
//					paths.add(path);
//					break;
//				case 4:
//					path = ImageUtil.setCameraPhoto(context, photo, img5);
//					number++;
//					img5.setTag(path);
//					paths.add(path);
//					break;
//				case 5:
//					path = ImageUtil.setCameraPhoto(context, photo, img6);
//					number++;
//					img6.setTag(path);
//					paths.add(path);
//					break;
				default:
					break;
				}
			}
		} catch (Exception e) {			
			ShowContentUtils.showLongToastMessage(mContext, "无法获取此图片，请重新添加");
			e.printStackTrace();
		}
		
	}

	/**
	 * 解码图片
	 * 
	 * @param uri
	 */
	private void bitmapFactory(Uri uri, int pic) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 这将通知
		// BitmapFactory类只须返回该图像的范围,而无须尝试解码图像本身
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		int heightRadio = (int) Math.ceil(options.outHeight / (float) height);
		int widthRadio = (int) Math.ceil(options.outWidth / (float) width);
		if (heightRadio > 1 && widthRadio > 1) {
			if (heightRadio > widthRadio) {
				options.inSampleSize = heightRadio;
			} else {
				options.inSampleSize = widthRadio;
			}
		}
		// 真正解码图片
		options.inJustDecodeBounds = false;
		Bitmap b;
		try {
			b = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, options);
			switch (pic) {
			case 1:
				pic1.setVisibility(View.VISIBLE);
				bt1 = ImageFactory.ratio(b, 480f, 800f);
				pic1.setImageBitmap(bt1);
				saveBitmapFile(bt1, url1);
				RefundActivity.pic = 2;
				break;
			case 2:
				pic2.setVisibility(View.VISIBLE);
				bt2 = ImageFactory.ratio(b, 480f, 800f);
				pic2.setImageBitmap(bt2);
				saveBitmapFile(bt2, url2);
				RefundActivity.pic = 3;
				break;
			case 3:
				pic3.setVisibility(View.VISIBLE);
				bt3 = ImageFactory.ratio(b, 480f, 800f);
				pic3.setImageBitmap(bt3);
				saveBitmapFile(bt3, url3);
				RefundActivity.pic = 4;
				break;
			default:
				break;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}	

	/**
	 * 获得真实地址
	 * @param contentUri
	 * @return
	 */
	public String getRealPathFromURI(Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);
		if (cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

	/**
	 * 将bitmap转文件
	 * 
	 * @param bitmap
	 */
	public void saveBitmapFile(Bitmap bitmap, String url) {
		File file = new File(url);// 将要保存图片的路径
		if (!file.isFile()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 将Bitmap转换成String
	 * @param bm
	 * @return
	 */
	private String changeByte(Bitmap bm){
		ByteArrayOutputStream baos =new ByteArrayOutputStream(); 
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] byteArray = baos.toByteArray();	
		return Base64.encode(byteArray);
	}
}

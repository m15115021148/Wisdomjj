package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;




import com.alibaba.fastjson.JSON;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.models.NumberModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * @author chenmeng 商品 立即购买 页面
 */
@ContentView(R.layout.activity_shopping_immediately_buy)
public class ShoppingImmediatelyBuyActivity extends BaseActivity implements
		OnClickListener {
	/** 本类 */
	private ShoppingImmediatelyBuyActivity mContext;
	/** 确定 */
	private TextView mSure;
	/** finished掉 activity */
	private ImageView mClear;
	@ViewInject(R.id.shopping_money)
	private TextView shopping_money;// 价格

	@ViewInject(R.id.shopping_img)
	private ImageView shopping_img;// 商品图片
	@ViewInject(R.id.number)
	private TextView number;// 库存量
	@ViewInject(R.id.shop_reduce)
	private TextView shop_reduce;// 减
	@ViewInject(R.id.shop_add)
	private TextView shop_add;// 加
	@ViewInject(R.id.shop_number)
	private TextView shop_number;// 数量

	@ViewInject(R.id.sizes)
	private RadioGroup sizes;// 尺寸

	@ViewInject(R.id.colors)
	private RadioGroup colors;// 颜色
	private ArrayList<NumberModel> list;// 数量列表
	
	/**颜色 HorizontalScrollView*/ 
	@ViewInject(R.id.color_hsl)
	private HorizontalScrollView colorHsl;
	
	/**尺寸 HorizontalScrollView*/ 
	@ViewInject(R.id.size_hsl)
	private HorizontalScrollView sizeHsl;

	private String mColor;// 用户选择的颜色
	private String mSize;// 用户选择的尺寸

	private int allNumber;// 库存量
	/** 1、购物车进入 2、立即购买 */
	private int type;
	/** 颜色 数组集合 */
	private String colorSplit;
	/** 尺寸 数据集合 */
	private String sizeSplit;
	/** 尺寸集合 */
	private    List<RadioButton> mRb=new ArrayList<RadioButton>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置 窗口的显示位置
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.gravity = Gravity.BOTTOM;
		getWindow().setAttributes(lp);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mSure = (TextView) findViewById(R.id.shopping_sure);
		mClear = (ImageView) findViewById(R.id.shopping_clear);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mClear.setOnClickListener(this);
		mSure.setOnClickListener(this);
		Intent intent = getIntent();
		list=intent.getParcelableArrayListExtra("list");
		Log.e("result", JSON.toJSONString(list));
		allNumber = Integer.parseInt(intent.getStringExtra("number"));
		number.setText("总库存 " + allNumber + " 件");
		shopping_money.setText(intent.getStringExtra("price"));
		String imgUrl = intent.getStringExtra("img");
		x.image().bind(shopping_img, imgUrl, MyApplication.imageOptionsZ);
		colorSplit = intent.getStringExtra("color");
		sizeSplit = intent.getStringExtra("size");
		Log.e("result", JSON.toJSONString(colorSplit));
		Log.e("result", JSON.toJSONString(sizeSplit));
		type = intent.getIntExtra("type", 0);
		addData(true, colorSplit, colors);
		addData(false, sizeSplit, sizes);
		colors.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int length = 0;
                for(int i=0;i<group.getChildCount();i++){
                    RadioButton rb = (RadioButton) group.getChildAt(i);
                    if(i!=0){
                        length += rb.getWidth();
                    }
                    if(rb.getId()==checkedId){
                        length = length - (colorHsl.getWidth()-rb.getWidth())/2;
                        break;
                    }
                }
                colorHsl.scrollTo(length,0);
			}
		});
		sizes.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int length = 0;
                for(int i=0;i<group.getChildCount();i++){
                    RadioButton rb = (RadioButton) group.getChildAt(i);
                    if(i!=0){
                        length += rb.getWidth();
                    }
                    if(rb.getId()==checkedId){
                        length = length - (sizeHsl.getWidth()-rb.getWidth())/2;
                        break;
                    }
                }
                sizeHsl.scrollTo(length,0);
			}
		});
	}

	/**
	 * 动态添加颜色尺寸数据
	 * 
	 * @param type
	 *            true为颜色 ;false为尺寸
	 * @param msg
	 * @param group
	 * @param isChecked
	 *            尺寸是否能点击
	 */
	private void addData(boolean type, String msg, RadioGroup group) {
		if (!TextUtils.isEmpty(msg)) {
			group.removeAllViews();
			String[] sizeStr = msg.split(",");
			for (int i = 0; i < sizeStr.length; i++) {
				RadioButton but = (RadioButton) LayoutInflater.from(this)
						.inflate(R.layout.radiobutton, null);
				but.setText(sizeStr[i]);
				RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, 0, 10, 0); // 设置外边距
				but.setLayoutParams(lp);
				but.setClickable(true);
				but.setOnCheckedChangeListener(new CheckType(type));
				if (!type) {
					mRb.add(but);
				}
				group.addView(but);
			}

		}
	}

	/**
	 * 自定义RadioGroup 选中事件 Description:
	 * 
	 * @author chenhao
	 * @date 2016-5-25
	 */
	class CheckType implements
			android.widget.CompoundButton.OnCheckedChangeListener {
		boolean type;// ture颜色，false尺寸

		public CheckType(boolean type) {
			this.type = type;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			shop_number.setText(String.valueOf(1));			
			if (isChecked == false) {
				return;
			}
			if (type) {
				mColor = buttonView.getText().toString();
				checkNumber(mColor);
				getPrice(mColor, mSize);
			} else {
				mSize = buttonView.getText().toString();
				getPrice(mColor, mSize);
			}
		}

	}

	/**
	 * 获取相应的价格
	 * 
	 * @param color
	 * @param size
	 */
	private void getPrice(String color, String size) {
		if (TextUtils.isEmpty(size) || TextUtils.isEmpty(color)) {
			return;
		}
		for (NumberModel model : list) {
			if (model.getColor().equals(color)) {
				if (model.getSize().equals(size)) {
					number.setText("库存 " + model.getNumber() + " 件");
					allNumber = Integer.parseInt(model.getNumber());
					shopping_money.setText(model.getPrice());
					return;
				}
			}
		}
		;
		number.setText("库存 0 件");// 脏数据
		shopping_money.setText("0");
	}
	
	/**
	 * 根据颜色 查询相应尺寸下的 库存量
	 * 
	 * @param color
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void checkNumber(String color) {
		if (TextUtils.isEmpty(color)) {
			return;
		}
//		重置尺寸选择框
		mRb.clear();
		addData(false, sizeSplit, sizes);
//		标记可选按钮
		for (int i = 0; i < list.size(); i++) {
			if (color.equals(list.get(i).getColor())&&!"0".equals(list.get(i).getNumber())) {
				 String size=list.get(i).getSize();
				 
				for (int j = 0; j < mRb.size(); j++) {
					if (size.equals(mRb.get(j).getText().toString())) {
						mRb.get(j).setClickable(true);
						mRb.get(j).setBackground(mContext.getResources().getDrawable(R.drawable.shopping_desc_buy_tv));
						mRb.remove(j);				
					}
					
				}
			}
			
		}
//		标记不可选按钮
		for (int j = 0; j < mRb.size(); j++) {
			mRb.get(j).setChecked(false);
			mRb.get(j).setClickable(false);
			mRb.get(j).setBackground(mContext.getResources().getDrawable(R.drawable.bt_is_no_checked));
		}		
		
	}

	@Override
	public void onClick(View v) {
		if (v == mClear) {// 取消
			overridePendingTransition(R.anim.shopping_desc_entry,
					R.anim.shopping_buy_exit);
			finish();
		}
		if (v == shop_reduce) {// 减
			int num = Integer.parseInt(shop_number.getText().toString());
			if (num > 1) {
				shop_number.setText(String.valueOf(num - 1));
			}

		}
		if (v == shop_add) {// 加
			int num = Integer.parseInt(shop_number.getText().toString());
			if (num < allNumber) {
				shop_number.setText(String.valueOf(num + 1));
			} else {
				ShowContentUtils.showLongToastMessage(this, "已达到最大数量");
			}
		}
		if (v == mSure) {// 确定
			if (!TextUtils.isEmpty(mColor) && !TextUtils.isEmpty(mSize)
					&& !TextUtils.isEmpty(shop_number.getText().toString())) {
				if (number.getText().equals("库存 0 件")) {
					ShowContentUtils
							.showShortToastMessage(this, "库存 0 件,无法购买!");
					return;
				}
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("color", mColor);
				bundle.putString("size", mSize);
				bundle.putString("number", shop_number.getText().toString());
				bundle.putString("price", shopping_money.getText().toString());
				intent.putExtras(bundle);
				intent.putExtra("type", type);
				setResult(RequestCode.GOODSINFO, intent);
				overridePendingTransition(R.anim.shopping_desc_entry,
						R.anim.shopping_buy_exit);
				finish();
			} else {
				ShowContentUtils.showShortToastMessage(this, "请选择颜色和尺寸！");
			}
		}
	}

}

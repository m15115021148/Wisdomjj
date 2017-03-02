package com.sitemap.wisdomjingjiang.activities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.WeatherModel;
import com.sitemap.wisdomjingjiang.models.WeatherResultModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.activities
 * 
 * @author chenmeng
 * @Description 天气页面
 * @date create at 2016年5月3日 下午4:36:02
 */
@ContentView(R.layout.activity_weather)
public class WeatherActivity extends BaseActivity implements OnClickListener{
	/**本类*/ 
	private WeatherActivity mContext;
	/**天气的时间 view*/ 
	TextView[] mWeek = new TextView[7];
	/**天气详情 view*/ 
	TextView[] mDes = new TextView[7];
	/**天气图片 view*/ 
	ImageView[] mCityImg = new ImageView[7];	
	/**所选择的城市天气详情*/ 
	TextView mCity,mCityDes,mCityTem;
	/**返回上一层*/ 
	private ImageView mBack;
	/**标题栏 标题*/ 
	private TextView mTitle;
	/**数据*/ 
	private List<WeatherModel> mList;
	@ViewInject(R.id.titleID)
	private RelativeLayout titleID;//标题

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();// 初始化view
		initData();// 初始化数据
	}

	/**
	 * 初始化view
	 */
	private void initView() {
//		titleID.setBackgroundColor(getResources().getColor(R.color.white));
		mWeek[0] = (TextView) findViewById(R.id.weather_today).findViewById(R.id.weather_day);
		mWeek[1] = (TextView) findViewById(R.id.weather_tomorrow).findViewById(R.id.weather_day);
		mWeek[2] = (TextView) findViewById(R.id.weather_saturday).findViewById(R.id.weather_day);
		mWeek[3] = (TextView) findViewById(R.id.weather_sunday).findViewById(R.id.weather_day);
		mWeek[4] = (TextView) findViewById(R.id.weather_monday).findViewById(R.id.weather_day);
		mWeek[5] = (TextView) findViewById(R.id.weather_tuesday).findViewById(R.id.weather_day);
		mWeek[6] = (TextView) findViewById(R.id.weather_next).findViewById(R.id.weather_day);
		
		mDes[0] = (TextView) findViewById(R.id.weather_today).findViewById(R.id.weather_desc);
		mDes[1] = (TextView) findViewById(R.id.weather_tomorrow).findViewById(R.id.weather_desc);
		mDes[2] = (TextView) findViewById(R.id.weather_saturday).findViewById(R.id.weather_desc);
		mDes[3] = (TextView) findViewById(R.id.weather_sunday).findViewById(R.id.weather_desc);
		mDes[4] = (TextView) findViewById(R.id.weather_monday).findViewById(R.id.weather_desc);
		mDes[5] = (TextView) findViewById(R.id.weather_tuesday).findViewById(R.id.weather_desc);
		mDes[6] = (TextView) findViewById(R.id.weather_next).findViewById(R.id.weather_desc);
		
		mCityImg[0] = (ImageView) findViewById(R.id.weather_today).findViewById(R.id.weather_icon);
		mCityImg[1] = (ImageView) findViewById(R.id.weather_tomorrow).findViewById(R.id.weather_icon);
		mCityImg[2] = (ImageView) findViewById(R.id.weather_saturday).findViewById(R.id.weather_icon);
		mCityImg[3] = (ImageView) findViewById(R.id.weather_sunday).findViewById(R.id.weather_icon);
		mCityImg[4] = (ImageView) findViewById(R.id.weather_monday).findViewById(R.id.weather_icon);
		mCityImg[5] = (ImageView) findViewById(R.id.weather_tuesday).findViewById(R.id.weather_icon);
		mCityImg[6] = (ImageView) findViewById(R.id.weather_next).findViewById(R.id.weather_icon);
		
		mCity = (TextView) findViewById(R.id.city_name);
		mCityDes = (TextView) findViewById(R.id.city_des);
		mCityTem = (TextView) findViewById(R.id.city_temperature);
		
		mBack = (ImageView) findViewById(R.id.weather_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.weather_title).findViewById(R.id.title);
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {								
		mBack.setOnClickListener(this);
		mTitle.setText("天气详情");
		
		String extra = getIntent().getStringExtra("weather");
		mList = JSON.parseArray(extra,WeatherModel.class);
		
		showWeatherData();
	}	
	
	/**
	 * 匹配数据
	 */
	private void showWeatherData(){
		if(mList != null){
			for (int i = 0; i < mList.size(); i++) {
				WeatherModel model = mList.get(i);
				mWeek[i].setText(model.getWeek().replace("星期", "周"));
				x.image().bind(mCityImg[i], model.getWeather_icon(),MyApplication.imageOptionsZ);
				mDes[i].setText(model.getTemperature().replace("℃", "°"));
				if(i==0){
					mCity.setText(model.getCitynm());
					mCityDes.setText(model.getWeather());
					mCityTem.setText(model.getTemperature());
					mWeek[i].setText("今天");
				}
				if(i==1){
					mWeek[i].setText("明天");
				}
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v == mBack){
			onBackPressed();
			finish();
		}
	}
	
}

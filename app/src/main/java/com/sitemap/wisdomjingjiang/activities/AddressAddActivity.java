package com.sitemap.wisdomjingjiang.activities;

import java.util.List;

import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.SpinnerAreaAdapter;
import com.sitemap.wisdomjingjiang.adapters.SpinnerCityAdatper;
import com.sitemap.wisdomjingjiang.adapters.SpinnerProvinceAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.AreaModel;
import com.sitemap.wisdomjingjiang.models.CityModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.ProvinceModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

/**
 * com.sitemap.wisdomjingjiang.activities.AddressAddActivity
 * 
 * @author zhang create at 2016年5月13日 上午9:13:57
 */

public class AddressAddActivity extends BaseActivity implements 
	OnClickListener,OnItemSelectedListener{
	/** 本类 */
	private AddressAddActivity mContext;
	/** 返回按钮 */
	private ImageView back_tv;
	/**网络请求*/ 
	private HttpUtil http;
	/**进度条*/ 
	private static MyProgressDialog progressDialog;
	/** 省  市 区*/
	private Spinner mProvince,mCity,mArea;
	/**联系人  电话    详细地址*/ 
	private EditText mPerson,mPhone,mInfo;
	/**是否设为默认收获地址（1是，2否）*/ 
	private CheckBox mIsDefault; 
	/**保存*/ 
	private TextView mSave;
	/**用户id 联系人  联系电话  省id 城市id 区id 地址具体信息 */ 
	private String userID, linkman, linkphone, provinceID, cityID, areaID, addressInfo;
	/** 是否设为默认收获地址（1是，2否）*/
	private int isDefault;
	/**省 数据*/ 
	private List<ProvinceModel> provinceList;
	/**市 数据*/ 
	private List<CityModel> cityList;
	/**区 数据*/ 
	private List<AreaModel> areaList;
	/**resultActivity 返回结果 量*/ 
	private static final int ADD_ADDRESS_RESULT = 0x1001;
	/**是否第一次加载*/ 
	private boolean isFristP = true;
//	/**省*/ 
//	private String province;
//	/**城市*/ 
//	private String city;
//	/**县*/ 
//	private String area;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_add);
		mContext = this;
		initView();// 初始化view
		initData();// 初始化数据

	}

	/**
	 * 初始化控件view
	 */
	public void initView() {
		back_tv = (ImageView) findViewById(R.id.back_tv);
		back_tv.setOnClickListener(this);
		mProvince = (Spinner) findViewById(R.id.province);
		mCity = (Spinner) findViewById(R.id.city);
		mArea = (Spinner) findViewById(R.id.area);
		mPerson = (EditText) findViewById(R.id.address_preson);
		mPhone = (EditText) findViewById(R.id.address_phone);
		mInfo = (EditText) findViewById(R.id.address_info);
		mIsDefault = (CheckBox) findViewById(R.id.address_is_default);
		mSave = (TextView) findViewById(R.id.address_save);
		
	}

	/**
	 * 数据初始化
	 */
	public void initData() {
		if(http == null){
			http = new HttpUtil(handler);
		}		
		mSave.setOnClickListener(this);	
		userID = MyApplication.loginModel.getUserID();
		
		getProvinceList();		

	}
	
	/**
	 * 获取省数量列表
	 */
	private void getProvinceList(){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			http.sendGet(RequestCode.GETPROVINCE, WebUrlConfig.getProvince());			
		}else{
			ShowContentUtils.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	/**
	 * 获取市数量列表
	 */
	private void getCityList(String provinceID){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			http.sendGet(RequestCode.GETCITY, WebUrlConfig.getCity(provinceID));
		}else{
			ShowContentUtils.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	/**
	 * 获取区数量列表
	 */
	private void getAreaList(String cityID){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			http.sendGet(RequestCode.GETAREA, WebUrlConfig.getArea(cityID));
		}else{
			ShowContentUtils.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}
	
	/**
	 * 添加我的收获地址
	 */
	private void sendAddressData(){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(mContext);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			
			linkman = mPerson.getText().toString();
			linkphone = mPhone.getText().toString();
			addressInfo = mInfo.getText().toString();
			String addAddress = WebUrlConfig.addAddress(userID, linkman, linkphone, 
						provinceID, cityID, areaID, addressInfo, 
						String.valueOf(isDefault));
			http.sendGet(RequestCode.ADDADDRESS, addAddress);
		}else{
			ShowContentUtils.showLongToastMessage(mContext, RequestCode.NOLOGIN);
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
				//获取省列表
				if (msg.arg1 == RequestCode.GETPROVINCE) {
					provinceList = JSONObject.parseArray(msg.obj.toString(),ProvinceModel.class);
					SpinnerProvinceAdapter adapter = new SpinnerProvinceAdapter(mContext, provinceList);
					mProvince.setAdapter(adapter);
					mProvince.setSelection(0, true);
					if(isFristP){
						provinceID = provinceList.get(0).getProvinceID();
//						province = provinceList.get(0).getProvince()+"省";
						getCityList(provinceList.get(0).getProvinceID());
					}
					mProvince.setOnItemSelectedListener(AddressAddActivity.this);						
				}
				//获取市列表
				if (msg.arg1 == RequestCode.GETCITY) {
					cityList = JSONObject.parseArray(msg.obj.toString(),CityModel.class);
					SpinnerCityAdatper adapter = new SpinnerCityAdatper(mContext, cityList);
					mCity.setAdapter(adapter);
					mCity.setSelection(0, true);
					if(isFristP){						
						cityID = cityList.get(0).getCityID();
//						city = cityList.get(0).getCity();
						getAreaList(cityList.get(0).getCityID());
					}
					mCity.setOnItemSelectedListener(AddressAddActivity.this);
					
				}
				//获取区列表
				if (msg.arg1 == RequestCode.GETAREA) {
					areaList = JSONObject.parseArray(msg.obj.toString(),AreaModel.class);
					SpinnerAreaAdapter adapter = new SpinnerAreaAdapter(mContext, areaList);
					mArea.setAdapter(adapter);
					mArea.setSelection(0, true);
					if(isFristP){
						areaID = areaList.get(0).getAreaID();
//						area = areaList.get(0).getArea();
					}
					mArea.setOnItemSelectedListener(AddressAddActivity.this);
				}
//				if(isFristP){
//					mInfo.setText(province+city+area);
//				}
				//添加收货地址
				if(msg.arg1 == RequestCode.ADDADDRESS){					
					CodeModel model = JSONObject.parseObject(msg.obj.toString(), CodeModel.class);
					if(model.getStatus()==0){
						ShowContentUtils.showShortToastMessage(mContext, "添加成功");										
					}else{
						ShowContentUtils.showShortToastMessage(mContext, model.getErrorMsg());						
					}
					setResult(ADD_ADDRESS_RESULT);
					mContext.finish();
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(mContext,
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.GETPROVINCE) {
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
		if (v == back_tv) {
			onBackPressed();
			finish();
		}
		if(v == mSave){//保存
			if(!MyApplication.isNull(mPerson)){
				ShowContentUtils.showLongToastMessage(mContext, "联系人不能为空");
				return;
			}
			if(!MyApplication.isNull(mPhone)){
				ShowContentUtils.showLongToastMessage(mContext, "电话不能为空");
				return;
			}else{
				if(!MyApplication.isMobileNO(mPhone.getText().toString())){
					ShowContentUtils.showLongToastMessage(mContext, "电话格式不正确");
					return;
				}
			}
			
			if(mProvince.getSelectedItem().toString()==null){
				ShowContentUtils.showLongToastMessage(mContext, "请选择省份");
				return;
			}
			if(mProvince.getSelectedItem().toString().equals("") || mProvince.getSelectedItem().toString() == null){
				ShowContentUtils.showLongToastMessage(mContext, "请选择所在的市");
				return;
			}
			if(mProvince.getSelectedItem().equals("") || mProvince.getSelectedItem() == null){
				ShowContentUtils.showLongToastMessage(mContext, "请选择所在的区县");
				return;
			}
			
			if(!MyApplication.isNull(mInfo)){
				ShowContentUtils.showLongToastMessage(mContext, "详细地址不能为空");
				return;
			}
			
			if(mIsDefault.isChecked()){
				isDefault = 1;
			}else{
				isDefault = 2;
			}				
			sendAddressData();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		isFristP = false;
		if(parent==mProvince){	
			mInfo.setText("");
			provinceID = provinceList.get(position).getProvinceID();
			getCityList(provinceID);
//			province = provinceList.get(position).getProvince()+"省";
		} else if(parent==mCity){
			if(!TextUtils.isEmpty(provinceID)){
				cityID = cityList.get(position).getCityID();
				getAreaList(cityID);
//				city = cityList.get(position).getCity();
			}							
		} else if(parent==mArea){
			if(!TextUtils.isEmpty(provinceID)){
				areaID = areaList.get(position).getAreaID();
//				area = areaList.get(position).getArea();
			}
		}
//		if(province!=null){
//			mInfo.setText(province+city+area);
//		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {		
	}

}

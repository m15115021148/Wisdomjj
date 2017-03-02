package com.sitemap.wisdomjingjiang.activities;

import java.util.List;

import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.SpinnerAreaAdapter;
import com.sitemap.wisdomjingjiang.adapters.SpinnerCityAdatper;
import com.sitemap.wisdomjingjiang.adapters.SpinnerProvinceAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.AddressModel;
import com.sitemap.wisdomjingjiang.models.AreaModel;
import com.sitemap.wisdomjingjiang.models.CityModel;
import com.sitemap.wisdomjingjiang.models.CodeModel;
import com.sitemap.wisdomjingjiang.models.ProvinceModel;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.MyProgressDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


/**
 * @author chenmeng
 *	地址修改 页面
 */
public class AddressUpdateActivity extends BaseActivity implements 
	OnClickListener,OnItemSelectedListener{
	/**全局类*/ 
	private Context mContext;
	/**返回上一层*/ 
	private ImageView mBack;
	/**标题栏 标题*/ 
	private TextView mTitle;
	/**收货人，电话，详细地址*/ 
	private EditText mPreson,mPhone,mDesc;
	/**省份，城市，区县*/ 
	private Spinner mProvince,mCity,mArea;
	/**是否是默认收获地址（1是，2否）*/ 
	private CheckBox mIsDefault;
	/**网络请求*/ 
	private HttpUtil http;
	/**进度条*/ 
	private static MyProgressDialog progressDialog;
	/**我的收获地址的返回实体类*/ 
	private AddressModel addressModel;
	/**省 数据*/ 
	private List<ProvinceModel> provinceList;
	/**市 数据*/ 
	private List<CityModel> cityList;
	/**区 数据*/ 
	private List<AreaModel> areaList;
	/**用户id 联系人  联系电话  省id 城市id 区id 地址具体信息 */ 
	private String userID, linkman, linkphone, provinceID, cityID, areaID, addressInfo;
	/** 是否设为默认收获地址（1是，2否）*/
	private int isDefault;
	/**收货地址id*/ 
	private String addressID;
	/**省 适配器*/ 
	private SpinnerProvinceAdapter provinceAdapter;
	/**市 适配器*/ 
	private SpinnerCityAdatper cityAdapter;
	/**区  适配器*/ 
	private SpinnerAreaAdapter areaAdapter;
	/**是否是第一次选择*/ 
	private boolean isFrist = false;
	/**保存*/ 
	private TextView mSave;
	/**resultActivity 返回结果 量*/ 
	private static final int ADD_ADDRESS_RESULT = 0x1001;
//	/**省*/ 
//	private String province;
//	/**城市*/ 
//	private String city;
//	/**县*/ 
//	private String area;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_update);
		mContext = this;
		initView();//初始化view
		initData();//初始化数据
	}
	
	/**
	 * 初始化view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.address_update_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.address_update_title).findViewById(R.id.title);
		mSave = (TextView) findViewById(R.id.address_update_title).findViewById(R.id.save);
		mPreson = (EditText) findViewById(R.id.address_update_preson);
		mPhone = (EditText) findViewById(R.id.address_update_phone);
		mProvince = (Spinner) findViewById(R.id.address_update_province);
		mCity = (Spinner) findViewById(R.id.address_update_city);
		mArea = (Spinner) findViewById(R.id.address_update_area);
		mDesc = (EditText) findViewById(R.id.address_update_desc);
		mIsDefault = (CheckBox) findViewById(R.id.address_is_default);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mBack.setOnClickListener(this);
		mTitle.setText(R.string.address_update_title);
		mSave.setVisibility(View.VISIBLE);
		mSave.setOnClickListener(this);
		
		if(http == null){
			http = new HttpUtil(handler);
		}
		userID = MyApplication.loginModel.getUserID();
		
		addressModel = (AddressModel) getIntent().getSerializableExtra("AddressModel");
		mPreson.setText(addressModel.getLinkman());
		mPhone.setText(addressModel.getLinkphone());
		mDesc.setText(addressModel.getAddressInfo());
		if(addressModel.getIsDefault().equals("1")){
			mIsDefault.setChecked(true);
		}else{
			mIsDefault.setChecked(false);
		}
		addressID = addressModel.getAddressID();		
		
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
					provinceAdapter = new SpinnerProvinceAdapter(mContext, provinceList);
					mProvince.setAdapter(provinceAdapter);
					if(addressModel.getProvince()!=null && !isFrist){
						//所选中的位置
						int position = provinceAdapter.selectPosition(addressModel.getProvince());
						mProvince.setSelection(position, true);
						provinceID = provinceList.get(position).getProvinceID();
						getCityList(provinceList.get(position).getProvinceID());	
					}
					
					mProvince.setOnItemSelectedListener(AddressUpdateActivity.this);						
				}
				//获取市列表
				if (msg.arg1 == RequestCode.GETCITY) {
					cityList = JSONObject.parseArray(msg.obj.toString(),CityModel.class);
					cityAdapter = new SpinnerCityAdatper(mContext, cityList);
					mCity.setAdapter(cityAdapter);
					if(addressModel.getCity()!=null && !isFrist){
						//所选中的位置
						int position = cityAdapter.selectPosition(addressModel.getCity());
						mCity.setSelection(position, true);
						cityID = cityList.get(position).getCityID();
						getAreaList(cityList.get(position).getCityID());
					}
					mCity.setOnItemSelectedListener(AddressUpdateActivity.this);
					
				}
				//获取区列表
				if (msg.arg1 == RequestCode.GETAREA) {
					areaList = JSONObject.parseArray(msg.obj.toString(),AreaModel.class);
					areaAdapter = new SpinnerAreaAdapter(mContext, areaList);
					mArea.setAdapter(areaAdapter);
					if(addressModel.getArea()!=null && !isFrist){
						int position = areaAdapter.selectPosition(addressModel.getArea());
						mArea.setSelection(position, true);	
						areaID = areaList.get(position).getAreaID();
					}
					mArea.setOnItemSelectedListener(AddressUpdateActivity.this);
				}
				//修改我的收获地址
				if(msg.arg1 == RequestCode.UPDATEADDRESS){
					CodeModel model = JSONObject.parseObject(msg.obj.toString(), CodeModel.class);
					if(model.getStatus()==0){
						ShowContentUtils.showShortToastMessage(mContext, "修改成功");										
					}else{
						ShowContentUtils.showShortToastMessage(mContext, model.getErrorMsg());						
					}
					setResult(ADD_ADDRESS_RESULT);
					finish();
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
		if(v == mBack){
			onBackPressed();
			finish();
		}
		if(v == mSave){//保存
			if(!MyApplication.isNull(mPreson)){
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
			
			if(!MyApplication.isNull(mDesc)){
				ShowContentUtils.showLongToastMessage(mContext, "详细地址不能为空");
				return;
			}

			if(mIsDefault.isChecked()){
				isDefault = 1;
			}else{
				isDefault = 2;
			}			
			updateAddressData();
		}
	}
	
	/**
	 * 修改我的收获地址
	 */
	private void updateAddressData(){
		if (MyApplication.getNetObject().isNetConnected()) {
			progressDialog = MyProgressDialog.createDialog(this);
			if (progressDialog != null && !progressDialog.isShowing()) {
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
			linkman = mPreson.getText().toString();
			linkphone = mPhone.getText().toString();
			addressInfo = mDesc.getText().toString();
			String update = WebUrlConfig.updateAddress(userID, linkman, linkphone, 
							provinceID, cityID, areaID, 
							addressInfo, String.valueOf(isDefault), addressID);
			RequestParams params = http.getParams(update);
			http.sendPost(RequestCode.UPDATEADDRESS, params);
		}else{
			ShowContentUtils.showLongToastMessage(mContext, RequestCode.NOLOGIN);
		}
	}


	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		isFrist = true;
		if(parent == mProvince){
			mDesc.setText("");
			provinceID = provinceList.get(position).getProvinceID();
			getCityList(provinceID);
//			province = provinceList.get(position).getProvince()+"省";
		}
		
		if(parent == mCity){	
			cityID = cityList.get(position).getCityID();
			getAreaList(cityID);
//			city = cityList.get(position).getCity();
		}
		if(parent == mArea){
			areaID = areaList.get(position).getAreaID();
//			area = areaList.get(position).getArea();
		}
//		if(province!=null){
//			mDesc.setText(province+city+area);
//		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
}

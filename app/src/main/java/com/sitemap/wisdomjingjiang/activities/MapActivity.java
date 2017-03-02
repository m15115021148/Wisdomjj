package com.sitemap.wisdomjingjiang.activities;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.sitemap.wisdomjingjiang.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ZoomControls;

/**
 * com.sitemap.wisdomjingjiang.activities.MapActivity
 * @author zhang
 * @category 地图页面
 * create at 2016年6月3日 下午3:05:06
 */
public class MapActivity extends BaseActivity implements OnClickListener{
	/**全局类*/ 
	private Context mContext;
	/**返回上一层*/ 
	private ImageView mBack;
	/**标题栏 标题*/ 
	private TextView mTitle;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private double lat,lng;//经纬度
	private String adress;//地址
	private OnMarkerClickListener markClick;// 地图标注点击事件
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext()); //百度地图初始化
		setContentView(R.layout.activity_map);
		mContext = this;
		initView();//初始化view
		initData();//初始化数据
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.hotle_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.hotle_title).findViewById(R.id.title);
		mMapView = (MapView) findViewById(R.id.bmapView);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mBack .setOnClickListener(this);
		mTitle.setText("地图");
			mBaiduMap = mMapView.getMap();
			// 隐藏缩放控件
			hidezoomView();
			// 开启定位图层
			mBaiduMap.setMyLocationEnabled(true);
			mMapView.removeViewAt(1);
			mMapView.removeViewAt(2);
		lat=getIntent().getDoubleExtra("lat", 0);
		lng=getIntent().getDoubleExtra("lng", 0);
		adress=getIntent().getStringExtra("adress");
		setpoint();
	}

	@Override
	public void onClick(View v) {
		if(v == mBack){
			onBackPressed();
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();

	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();

	}
	
	/**
	 * 隐藏缩放控件
	 */
	private void hidezoomView() {
		final int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}

	}
	
	/**
	 * 地图标点
	 */
	private void setpoint(){
		// 定义Maker坐标点
		LatLng point= new LatLng(lat, lng);


				// 构建Marker图标
				BitmapDescriptor bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.point);
				// 构建MarkerOption，用于在地图上添加Marker
				OverlayOptions option = new MarkerOptions().position(point)
						.icon(bitmap).zIndex(1);
				View contentView = getLayoutInflater().inflate(R.layout.mapmark_layout,
						null);
				
				TextView location_address = (TextView) contentView
						.findViewById(R.id.location_address);
				location_address.setText(adress);
				location_address.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mBaiduMap.hideInfoWindow();
					}
				});
				// 构建对话框用于显示
				// 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
				final InfoWindow mInfoWindow = new InfoWindow(contentView, point, -60);

				// 在地图上添加Marker，并显示
				mBaiduMap.addOverlay(option);
				// 显示InfoWindow
				mBaiduMap.showInfoWindow(mInfoWindow);
				markClick = new OnMarkerClickListener() {

					@Override
					public boolean onMarkerClick(Marker arg0) {
						mBaiduMap.showInfoWindow(mInfoWindow);
						return false;
					}
				};
				mBaiduMap.setOnMarkerClickListener(markClick);
				// 正常显示
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//				CircleOptions circle=new CircleOptions();
//				circle.center(point);
//				circle.radius(500);
//				circle.fillColor(Color.parseColor("#3c1cace9"));
//				mBaiduMap.addOverlay(circle);
				// 定义地图状态
				updatestatus(point, 18);
	}
	
	/**
	 * update地图的状态与变化
	 * 
	 * @param point
	 * @param zoom
	 */
	private void updatestatus(LatLng point, int zoom) {
		MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(zoom)
				.build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate =

		MapStatusUpdateFactory.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}
}

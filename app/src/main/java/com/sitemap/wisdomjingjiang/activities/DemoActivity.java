package com.sitemap.wisdomjingjiang.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.fragments.HomePageFragment.MyFragment;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.TopsModel;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * 网络、注解、图片加载工具demo示例
 * @author chenhao
 * @date 2016-5-10
 */
@ContentView(R.layout.acitivity_demo)//布局反射注解  
public class DemoActivity extends BaseActivity{

	//控件反射注解
	@ViewInject(R.id.img)
	private ImageView img;
	
	//控件反射注解
	@ViewInject(R.id.viewpager)
	private ViewPager mVp;
	
	@ViewInject(R.id.r1)
	private LinearLayout dotLayout;
	
	private static List<TopsModel> mTopsModelList = null;

//	网络请求顺序
	private final int FIRST=0;
	private int flag = 0;
	
	private static String[] imgStr;
	
	//自动轮播启用开关  
    private static boolean isAutoPlay = true; 
	// 当前轮播页
	private int currentItem = 0;
	private static final int TOPSWHAT = 1;
	
	private List<ImageView> ImgList = new ArrayList<ImageView>();
	private List<View> dotList = new ArrayList<View>(); // 图片标题正文的那些点
	

	
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
//	        进行一些数据初始化操作（不必要再写setContentView()方法）;如果是fragment，则在onViewCreated（）方法中进行数据数据初始化
	        HttpUtil http = new HttpUtil(handler);
	        http.sendGet(1, WebUrlConfig.getTops());	
	    }
	  
	  @Override
	public void onResume() {
		  handler.sendEmptyMessage(TOPSWHAT);
		super.onResume();
	}
	  
	  @Override
	public void onPause() {
		// 移除 广告轮播what
			handler.removeMessages(TOPSWHAT);
		super.onPause();
	}
	  
	  
	  
	  private void initViewPagerImage(final ViewPager vp) {
			dotLayout.removeAllViews();
			for (int i = 0; i < imgStr.length; i++) {			
				// 点
				ImageView dot = new ImageView(this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(5, 0, 5, 0);
				if (i == 0) {
					dot.setBackgroundResource(R.drawable.point_indictor_unselect);
				} else {
					dot.setBackgroundResource(R.drawable.point_indictor_select);
				}
				dotLayout.addView(dot, params);
				dotList.add(dot);
			}

			vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
				
				@Override
				public Fragment getItem(int position) {
					MyFragment fragment = new MyFragment();
					Bundle b = new Bundle();
					b.putInt("tag", position);
					fragment.setArguments(b);
					return fragment;
				}

				@Override
				public int getCount() {
					return imgStr.length;
				}
			});
			
			vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
				}

				@Override
				public void onPageSelected(int position) {
					currentItem = position;
					for (int i = 0; i < dotList.size(); i++) {
						if (i == position) {
							dotList.get(position).setBackgroundResource(R.drawable.point_indictor_unselect);
						} else {
							dotList.get(i).setBackgroundResource(R.drawable.point_indictor_select);
						}
					}
//					ImgList.get(position).setOnClickListener(MainActivity.this);
				}

				@Override
				public void onPageScrollStateChanged(int state) {
					switch (state) {
					case 1:// 手势滑动，空闲中
						isAutoPlay = false;
						break;
					case 2:// 界面切换中
						isAutoPlay = true;
						break;
					case 0:// 滑动结束，即切换完毕或者加载完毕
							// 当前为最后一张，此时从右向左滑，则切换到第一张
						if (vp.getCurrentItem() == vp.getAdapter().getCount() - 1
								&& !isAutoPlay) {
							vp.setCurrentItem(0);
						}// 当前为第一张，此时从左向右滑，则切换到最后一张  
		                else if (vp.getCurrentItem() == 0 && !isAutoPlay) {  
		                    vp.setCurrentItem(vp.getAdapter().getCount() - 1);  
		                }
						break;
					}
				}
			});
		}

	  public static class MyFragment extends Fragment {
			public View onCreateView(LayoutInflater inflater, ViewGroup container,
					Bundle savedInstanceState) {
				ImageView iv = new ImageView(inflater.getContext());
				int pos = getArguments().getInt("tag");
				x.image().bind(iv, imgStr[pos]);
				return iv;
			}
	}

	/**
	 * 事件反射注解
	 * 
	 * 1. 方法必须私有限定, 2. 方法参数形式必须和type对应的Listener接口一致. 3. 注解参数value支持数组:
	 * value={id1, id2, id3}
	 **/
	@Event(value = R.id.r1, type = View.OnClickListener.class)//type可选参数, 默认是View.OnClickListener.class
															
	private void onTestClick(View view) {
		/*
		 * //网络框架示例post方式： 
		 * HttpUtil http=new HttpUtil(handler); 
		 * RequestParams params=http.getParams(
		 * "http://www.na2ne.com:20015/NaErNe/userAction_systemNotify");
		 * params.addBodyParameter("platform", "0");
		 * params.addBodyParameter("versionCode", "2.1.3"); http.sendPost(FIRST,
		 * params);
		 */

		/*
		 * //网络框架示例get方式： 
		 * HttpUtil http=new HttpUtil(handler); 
		 * Cancelable cancelHttp=http.sendGet(FIRST,
		 * "http://www.na2ne.com:20015/NaErNe/userAction_getShareInfo");
		 * //中断网络链接示例 
		 * // if(cancelHttp!=null&&!cancelHttp.isCancelled()){ //
		 * cancelHttp.cancel(); // }
		 */

		/*
		 * // 网络框架示例cache方式：
		 *  HttpUtil http = new HttpUtil(handler);
		 * http.sendCache(FIRST,
		 * "http://www.na2ne.com:20015/NaErNe/userAction_getShareInfo");
		 */

		// 上传文件示例：
		/*
		 * HttpUtil http = new HttpUtil(handler);
		 *  RequestParams params = http
		 * .getParams("http://192.168.0.56:1596/NaErNe/userAction_toAdvice");
		 * params.addBodyParameter("platform", "0");
		 * params.addBodyParameter("versionCode", "2.1.3");
		 * params.addBodyParameter("adviceTitle", "test");
		 * params.addBodyParameter("adviceInfo", "test");
		 * params.addBodyParameter("userID",
		 * "E6244438FA964B20AB16FD5B6F6D843D"); // 可上传多文件
		 * params.addBodyParameter("img2", new
		 * File(Environment.getExternalStorageDirectory() + "/b.jpg"));
		 * params.addBodyParameter("img1", new
		 * File(Environment.getExternalStorageDirectory() + "/a.jpg"));
		 * http.uploadFile(FIRST, params);
		 */

		// 下载文件
		
//		http.downloadFile(
//				FIRST,
//				"http://192.168.1.101/scan/girl.jpg",
//				Environment.getExternalStorageDirectory().getAbsolutePath()+"/girl.jpg");
//		String url = getWeather(et.getText().toString());
//		Log.e("result", url);
//		RequestParams params = http.getParams(url);
//		params.setCharset("GBK");
//		http.sendPost(1, params);
//		http.sendGet(1, url);
//		Log.e("result",System.getProperty("file.encoding"));
//		imgVp = new ImageCycleView(this, handler);
		
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HttpUtil.SUCCESS://成功
//				if(msg.arg1==FIRST){
//				x.image().bind(img,Environment.getExternalStorageDirectory().getAbsolutePath()+"/girl.jpg" );
//				}
				if(msg.arg1 ==1){
					Log.e("result", msg.obj.toString());
					mTopsModelList = JSONObject.parseArray(msg.obj.toString(), TopsModel.class);
					imgStr = new String[mTopsModelList.size()];
					for (int i = 0; i < mTopsModelList.size(); i++) {
						imgStr[i] = mTopsModelList.get(i).getTopImg();
					}
					initViewPagerImage(mVp);
				}
				break;
			case HttpUtil.FAILURE://失败
				if(msg.arg1==FIRST){
						Log.i("will", "数据加载失败！");
						}
				break;
			case HttpUtil.LOADING://更新进度条
				if(msg.arg1==FIRST){
					Log.i("will", "当前进度（0--100）： "+msg.obj.toString());
					}
				break;
			case TOPSWHAT:
				mVp.setCurrentItem(currentItem++);
				if(currentItem == ImgList.size()){
					currentItem = 0;
				}
				handler.sendEmptyMessageDelayed(TOPSWHAT, 3000);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
}

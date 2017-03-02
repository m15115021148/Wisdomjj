package com.sitemap.wisdomjingjiang.activities;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.MerchantListListViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author chenmeng
 *	附近商圈列表 页面
 */
public class MerchantListActivity extends BaseActivity implements OnClickListener{
	/**全局类*/ 
	private Context mContext;
	/**标题栏 返回上一层  右侧图片*/ 
	private ImageView mBack;
	/**标题栏 标题*/ 
	private TextView mTitle;
	/**listview*/ 
	private ListView mLv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant_list);
		mContext = this;
		initView();//初始化view
		initData();//初始化数据
		initListView();//初始化listview
	}
	
	/**
	 * 初始化view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.merchant_list_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.merchant_list_title).findViewById(R.id.title);
		mLv = (ListView) findViewById(R.id.merchant_list_listview);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mTitle.setText("附近商家");
		mBack.setOnClickListener(this);
		
	}
	
	/**
	 * 初始化listview
	 */
	private void initListView() {
		mLv.setAdapter(new MerchantListListViewAdapter(mContext));
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(mContext,FoodsShopActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v == mBack){//返回上一层
			onBackPressed();
			finish();
		}		
	}

}

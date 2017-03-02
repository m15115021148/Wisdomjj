package com.sitemap.wisdomjingjiang.activities;

import com.sitemap.wisdomjingjiang.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author chenmeng
 *	物流信息
 */
public class WuLiuActivity extends BaseActivity implements OnClickListener{
	/**全局类*/ 
	private Context mContext;
	/**返回上一层*/ 
	private ImageView mBack;
	/**标题栏 标题*/ 
	private TextView mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wu_liu);
		mContext = this;
		initView();//初始化view
		initData();//初始化数据
	}
	
	/**
	 * 初始化view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.wu_liu_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.wu_liu_title).findViewById(R.id.title);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mBack.setOnClickListener(this);
		mTitle.setText(R.string.wu_liu_title);
		
	}

	@Override
	public void onClick(View v) {
		if(v == mBack){
			onBackPressed();
			finish();
		}
	}

}

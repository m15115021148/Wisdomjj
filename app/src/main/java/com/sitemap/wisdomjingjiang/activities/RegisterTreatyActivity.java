package com.sitemap.wisdomjingjiang.activities;

import com.sitemap.wisdomjingjiang.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterTreatyActivity extends BaseActivity implements OnClickListener{
	/**返回上一层*/ 
	private ImageView mBack;
	/**标题*/ 
	private TextView mTitle;
	/**注册协议*/ 
	private TextView mTreaty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_treaty);
		initView();
		initData();
	}

	/**
	 * 初始化 view
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.register_treaty_title).findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.register_treaty_title).findViewById(R.id.title);
		mTreaty = (TextView) findViewById(R.id.treaty_txt);
	}

	/**
	 * 	初始化数据
	 */
	private void initData() {
		mBack.setOnClickListener(this);
		mTitle.setText("注册协议");
		String txt = "\t\t您在智慧靖江平台上使用智慧靖江服务过程中，您遵守以下约定：\n\n"
					+ "\t\t1、您在使用智慧靖江平台服务过程中实施的所有行为均遵守国家法律、法规等规范性文件及智慧靖"					
					+ "江各项规则的规定和要求，不违背社会公共利益或公共道德，不损害他人的合法权益，不违反本协"				
					+ "议及相关规则。\n"
					+ "\t\t2、您注册或绑定时所填写的手机号必须真实准确，以便您日后进行招车、买票等相关操作。\n"
					+ "\t\t3、您须自行负责对您的账号及密码保密，且须对您在该账号和密码下发生的所有活动（包括但不限"				
					+ "于信息披露、发布信息、网上购买服务等）承担责任。您同意：如发现任何人未经授权使用您的账"					
					+ "号和密码，或发生违反保密规定的任何其他情况，您会立即通知智慧靖江，并授权智慧靖江对该信"					
					+ "息进行相应处理。\n"
					+ "\t\t4、智慧靖江将为用户提供服务的过程中，竭尽可能地维护和完善智慧靖江提供的各种服务。用户在"				
					+ "使用智慧靖江过程中自行承担风险，智慧靖江不担保服务一定能满足用户的要求，也不担保服务不"					
					+ "会中断。智慧靖江同事不对用户所发布的信息的删除或存储失败负责。\n\n"					 
					+ "隐私政策：\n"					
					+ "\t\t智慧靖江非常重视对您隐私权的保护，您的注册信息及手机号等个人资料为您的重要隐私，智慧靖"					
					+ "江承诺不会将个人资料用作它途，承诺不会在未获得您许可的情况下擅自将您的个人资料信息出租"					
					+ "或出售给任何第三方，但以下情况除外：\n"
					+ "\t\t1、您同意让第三方共享资料；\n"
					+ "\t\t2、您同意公开其个人资料，享受为其提供的产品和服务；\n"
					+ "智慧靖江的隐私声明正在不断改进中，随着智慧靖江服务范围的扩大，会随时更新隐私声明。我们"					
					+ "欢迎您随时查看隐私声明。\n";
		mTreaty.setText(txt);
	}

	@Override
	public void onClick(View v) {
		if(v == mBack){
			onBackPressed();
			finish();
		}
	}

}

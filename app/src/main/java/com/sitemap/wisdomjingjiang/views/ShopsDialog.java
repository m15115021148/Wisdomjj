package com.sitemap.wisdomjingjiang.views;

import java.util.ArrayList;
import java.util.List;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.adapters.DialogShopsAdapter;
import com.sitemap.wisdomjingjiang.models.AreasModel;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
/**
 * 
 * Description: 首页商圈列表弹出框
 * @author chenhao
 * @date   2016-5-21
 */
public class ShopsDialog  extends Dialog{
	private Context context;
	private List<AreasModel> list;//商圈列表
    private String areaID;//商圈id
    private String areaName;//商圈名称
    private TextView cityName;//当前城市
	
	public GridView dialog_shops;//商圈
	public ShopsDialog(Context context) {
		super(context);
	}
	public ShopsDialog(Context context, List<AreasModel> list) {
		super(context);
		this.context = context;
		this.list = list;
	}
	
	/**
	 * 内部的接口 来实现点击事件
	 * 
	 */
	public interface OnClickDialogListener {
		void setOnDialogClick(int position);
		void setOnDialogCityNameClick();
	}

	public void setOnClickDialogListener(
			OnClickDialogListener onClickDialogListener) {
		this.onClickDialogListener = onClickDialogListener;
	}

	OnClickDialogListener onClickDialogListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_shops);
		dialog_shops=(GridView)findViewById(R.id.dialog_shops);
		cityName = (TextView) findViewById(R.id.dialog_city);
		cityName.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onClickDialogListener!=null){
					onClickDialogListener.setOnDialogCityNameClick();
					dismiss();
				}
			}
		});
		initInfo();
	}
	/**
	 * 初始化dialog界面信息
	 */
	private void initInfo() {
		if(list==null){
			list=new ArrayList<AreasModel> ();
			AreasModel model=new AreasModel();
			model.setAreaID("0");
			model.setAreaName("全城");
			list.add(model);
		}

		if (list != null) {
			AreasModel model=new AreasModel();
			model.setAreaID("0");
			model.setAreaName("全城");
			list.add(model);
			DialogShopsAdapter adapter=new DialogShopsAdapter(context,list,this,onClickDialogListener);
			dialog_shops.setAdapter(adapter);
		}
	}
	public String getAreaID() {
		return areaID;
	}
	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}

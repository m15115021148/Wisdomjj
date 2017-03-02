package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sitemap.wisdomjingjiang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 首页 资讯 的gridview 适配器
 * @date create at  2016年5月9日 下午3:18:59
 */
public class HomeShareAdapter extends BaseAdapter{
	Context mContext;
    /**资讯 的list的集合*/ 
    List<Map<String,Integer>> list1 = new ArrayList<Map<String, Integer>>();
    /**资讯 的list的集合*/ 
    List<Map<String,Integer>> list2 = new ArrayList<Map<String, Integer>>();
    /**名称*/ 
    public static final String[] Contents1 = {
        "资讯","团购","酒店","KTV","购物","租房","招聘","论坛"
    };
    /**名称*/ 
    public static final String[] Contents2 = {
        "水费","电费","煤气费","违章查询","自行车","汽车票","医院","二手"
    };
    /**对应的图片*/ 
    public static final int[] imgResId1 = {
    	R.drawable.home_news,R.drawable.home_foods,R.drawable.home_hotle,R.drawable.home_ktv,
    	R.drawable.home_shopping,R.drawable.home_fang,R.drawable.home_zhao,R.drawable.home_lun   	
    };
    /**对应的图片*/ 
    public static final int[] imgResId2 = {
    	R.drawable.home_1,R.drawable.home_2,R.drawable.home_3,R.drawable.home_4,
    	R.drawable.home_5,R.drawable.home_6,R.drawable.home_7,R.drawable.home_8   	
    };
	private Holder holder;
	/**类型*/ 
	private int mType;
    
    public HomeShareAdapter(Context context,int type){
    	this.mType = type;
        this.mContext = context;
        if(type == 1){
        	initData1();
        }else{
        	initData2();
        }
    }
    
    /**
     * 初始化数据
     */
    private void initData1(){
        Map<String,Integer> map = new HashMap<String, Integer>();
        for(int i=0;i<Contents1.length;i++){
            map.put(Contents1[i],imgResId1[i]);
            list1.add(map);
        }
    }
    
    /**
     * 初始化数据
     */
    private void initData2(){
        Map<String,Integer> map = new HashMap<String, Integer>();
        for(int i=0;i<Contents1.length;i++){
            map.put(Contents2[i],imgResId2[i]);
            list2.add(map);
        }
    }

	@Override
	public int getCount() {
		if(mType==1){
			return list1.size();
		}else{
			return list2.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gv_home_share_item, null);
			holder = new Holder();
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		if(mType == 1){
			holder.iv_icon.setImageResource(imgResId1[position]);
			holder.tv_content.setText(Contents1[position]);	
		}else{
			holder.iv_icon.setImageResource(imgResId2[position]);
			holder.tv_content.setText(Contents2[position]);	
		}
	
		return convertView;
	}
	
	private class Holder {
		private TextView tv_content;//图片的名称
		private ImageView iv_icon;//图片
	}

}

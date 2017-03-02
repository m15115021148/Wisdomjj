package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import com.sitemap.wisdomjingjiang.adapters.OrderFoodsListAdapter.CallBackOrderFoods;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartFoodsModel;
import com.sitemap.wisdomjingjiang.utils.FileUtils;
import com.sitemap.wisdomjingjiang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 分开结算  适配器
 * @date create at  2016年6月7日 下午1:59:54
 */
public class SubPayFoodsListViewAdapter extends BaseAdapter{
	Context mContext;
	List<CartFoodsModel> mList;
	private Holder holder;
	
	/**回调*/ 
	private CallBackSubPayFoods mCallBack;
	
	/**
	 * 内部接口
	 *	传值给activity 
	 */
	public interface CallBackSubPayFoods{
		void onClickPayFoodsSureListener(int position);
	}

	public SubPayFoodsListViewAdapter(Context context,
			CallBackSubPayFoods callBack,
			List<CartFoodsModel> list){
		this.mContext = context;
		this.mCallBack = callBack;
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_sub_pay_item, null);
			holder = new Holder();
			holder.name = (TextView) convertView.findViewById(R.id.sub_pay_name);
			holder.number = (TextView) convertView.findViewById(R.id.sub_pay_number);
			holder.money = (TextView) convertView.findViewById(R.id.sub_pay_money);
			holder.pay = (TextView) convertView.findViewById(R.id.sub_pay_sure);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		CartFoodsModel model = mList.get(position);
		holder.name.setText(model.getFoodShopName());
		int sunNum = 0;
		double sunMoney = 0;
		for (int i = 0; i < model.getFoodsInfo().size(); i++) {
			String number = model.getFoodsInfo().get(i).getNumber();
			String money = model.getFoodsInfo().get(i).getFoodsPrice();
			sunNum = sunNum+Integer.parseInt(number);
			if(number!=null && money != null){
				sunMoney = sunMoney+(Integer.parseInt(number)*Double.parseDouble(money));
			}
		}		
		
		holder.number.setText("共"+String.valueOf(sunNum)+"件");
		holder.money.setText("￥"+String.valueOf(FileUtils.roundMath(sunMoney)));
		final int currPosition = position;
		holder.pay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallBack != null){
					mCallBack.onClickPayFoodsSureListener(currPosition);
				}
			}
		});
		
		return convertView;
	}

	private class Holder{
		TextView number,money,name;
		TextView pay;
	}
	
}

package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.models.CartGoodsModel;
import com.sitemap.wisdomjingjiang.utils.FileUtils;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.adapters
 * @author chenmeng
 * @Description 购物 适配器
 * @date create at  2016年6月7日 下午3:58:05
 */
public class SubPayGoodsListViewAdapter extends BaseAdapter{
	Context mContext;
	List<CartGoodsModel> mList;
	private Holder holder;
	
	/**回调*/ 
	private CallBackSubPayGoods mCallBack;
	
	/**
	 * 内部接口
	 *	传值给activity 
	 */
	public interface CallBackSubPayGoods{
		void onClickPayGoodsSureListener(int position);
	}

	public SubPayGoodsListViewAdapter(Context context,
			CallBackSubPayGoods callBack,
			List<CartGoodsModel> list){
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
		CartGoodsModel model = mList.get(position);
		holder.name.setText(model.getGoodShopName());
		int sunNum = 0;
		double sunMoney = 0;
		for (int i = 0; i < model.getGoodsInfo().size(); i++) {
			String number = model.getGoodsInfo().get(i).getNumber();
			String money = model.getGoodsInfo().get(i).getGoodsPrice();
			sunNum = sunNum+Integer.parseInt(number);
			if(number!=null && money != null && !number.equals("") && !money.equals("")){
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
					mCallBack.onClickPayGoodsSureListener(currPosition);
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

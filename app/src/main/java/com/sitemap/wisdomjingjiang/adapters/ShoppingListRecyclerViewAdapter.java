package com.sitemap.wisdomjingjiang.adapters;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.GoodsModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
/**
 * com.sitemap.wisdomjingjiang.adapters
 * 
 * @author chenmeng
 * @Description 商品列表 的recyclerview 的适配器
 * @date create at 2016年5月12日 下午4:39:27
 */
public class ShoppingListRecyclerViewAdapter extends
		RecyclerView.Adapter<ViewHolder> {

	private Context mContext;
	/** 数据 */
	private List<GoodsModel> mList;		
	
	 private static final int TYPE_ITEM = 0;//主内容
	 private static final int TYPE_FOOTER = 1;//加载更多

	public ShoppingListRecyclerViewAdapter(Context context,
			List<GoodsModel> list) {
		this.mContext = context;
		this.mList = list;
	}

	/**
	 * 内部的接口 来实现点击事件
	 * 
	 */
	public interface OnClickItemsListener {
		void setOnClick(int position);
	}

	public void setOnClickItemsListener(
			OnClickItemsListener onClickItemsListener) {
		this.onClickItemsListener = onClickItemsListener;
	}

	OnClickItemsListener onClickItemsListener;
	
	/**
	 * 刷新数据
	 * @param list
	 */
	public void changeData(List<GoodsModel> list){
		this.mList = null;
		this.mList = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
//		return list.size() == 0 ? 0 : list.size() + 1;
		return mList.size();
	}
	
//	@Override
//    public int getItemViewType(int position) {
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
//    }
	
	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
//		if (holder instanceof MyHolder) {
			((MyHolder) holder).setData(position);		
			holder.itemView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (onClickItemsListener != null) {
						onClickItemsListener.setOnClick(holder.getLayoutPosition());
					}
				}
			});
			
//		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//		if(viewType==TYPE_ITEM){
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.rv_shopping_list_item, null);
			MyHolder holder = new MyHolder(view);
			return holder;
//		}else if(viewType==TYPE_FOOTER){
//			View view = LayoutInflater.from(mContext).inflate(R.layout.rv_shopping_foot_item, null);
//		/	return new FootViewHolder(view);
//		}
//		return null;
	}

//	/**
//	 * 绑定view
//	 */
//	@Override
//	public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//		if(viewType==TYPE_ITEM){
//			View view = LayoutInflater.from(mContext).inflate(
//					R.layout.rv_shopping_list_item, null);
//			MyHolder holder = new MyHolder(view);
//			return holder;
//		}else if(viewType==TYPE_FOOTER){
//			View view = LayoutInflater.from(mContext).inflate(R.layout.rv_shopping_foot_item, null);
//			return new FootViewHolder(view);
//		}
//		
//	}
//
//	/**
//	 * 绑定holder
//	 */
//	@Override
//	public void onBindViewHolder(final MyHolder holder, int position) {
//		holder.setData(position);		
//		holder.itemView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (onClickItemsListener != null) {
//					onClickItemsListener.setOnClick(holder.getLayoutPosition());
//				}
//			}
//		});
//	}

	/**
	 * 创建一个内部hodler 传数据
	 */
	class MyHolder extends RecyclerView.ViewHolder {
		View mItemView;
		TextView desc;
		TextView money;
		TextView number;
		TextView place;
		ImageView img;

		public MyHolder(View itemView) {
			super(itemView);			
			mItemView = itemView;
			desc = (TextView) mItemView.findViewById(R.id.shopping_desc);
			money = (TextView) mItemView.findViewById(R.id.shopping_money);
			number = (TextView) mItemView.findViewById(R.id.shopping_more);
			place = (TextView) mItemView.findViewById(R.id.shopping_city_name);
			img = (ImageView) mItemView.findViewById(R.id.shopping_img);
		}

		/**
		 * 设置 数据
		 */
		public void setData(int position) {						
			GoodsModel model = mList.get(position);			
			desc.setText(model.getGoodsName());	
						
			if(model.getGoodsImg().equals("")){
				img.setBackgroundResource(R.drawable.tops_v_bg);
			}else{
				x.image().bind(img, model.getGoodsImg(),MyApplication.imageOptionsZ);
			}	
			if(model.getGoodsPrice().equals("")){
				money.setText("￥0.00");	
			}else{
				money.setText("￥"+model.getGoodsPrice());	
			}
				
			if(model.getSales().equals("")){
				number.setText("总销量:0");	
			}else{
				number.setText("总销量:"+model.getSales());	
			}					
			place.setText(model.getGoodsPlace());
		}

	}  
	
	static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }



}

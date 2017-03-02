package com.sitemap.wisdomjingjiang.views;

import com.alibaba.fastjson.JSON;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PullableRecyclerView extends
		android.support.v7.widget.RecyclerView implements Pullable{
	public PullableRecyclerView(Context context) {
		super(context);
	}

	public PullableRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableRecyclerView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		return false;// 不需要下拉功能
	}

	@Override
	public boolean canPullUp() {
		LayoutManager layoutManager = getLayoutManager();
		if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
			// 获取布局管理器
			StaggeredGridLayoutManager layout = (StaggeredGridLayoutManager) layoutManager;
			// 用来记录lastItem的position
			// 由于瀑布流有多个列 所以此处用数组存储
			int column = layout.getColumnCountForAccessibility(null, null);
			int positions[] = new int[column];
			// 获取lastItem的positions
			int[] pos = layout.findLastVisibleItemPositions(positions);
			for (int i = 0; i < positions.length; i++) {
				/**
				 * 判断lastItem的底边到recyclerView顶部的距离 是否小于recyclerView的高度 如果小于或等于
				 * 说明滚动到了底部
				 */
				
				if(layout.findViewByPosition(positions[i])==null){
					return false;//页面内无数据
				}
				// 刚才忘了写判断是否是最后一个item了
				if (positions[i] >= (layout.getItemCount() - 1)
						&& layout.findViewByPosition(positions[i]).getBottom() <= getHeight()) {
					/**
					 * 此处实现你的业务逻辑
					 */
					 Log.e("result","true");
					return true;
					// break;
				}

			}
				return false;
		}
		else{
			return false;
		}
	}

}

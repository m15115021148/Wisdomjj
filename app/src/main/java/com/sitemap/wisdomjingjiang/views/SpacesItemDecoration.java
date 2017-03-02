package com.sitemap.wisdomjingjiang.views;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

/**
 * com.sitemap.wisdomjingjiang.views
 * 
 * @author chenmeng
 * @Description recyclerview中自定义 每个item的高度
 * @date create at 2016年5月13日 上午9:54:02
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
	/** 间距 */
	private int space;
	
	/**数量*/ 
	private int maxNum;

	public SpacesItemDecoration(int space,int max) {
		this.space = space;
		this.maxNum = max;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
			RecyclerView.State state) {
		outRect.bottom = space/4;
		if (parent.getChildAdapterPosition(view) == 0) {
			outRect.top = space/5;
		}
		if (parent.getChildAdapterPosition(view) == 1) {
			outRect.top = space;
		}
		if(parent.getChildAdapterPosition(view)%2==0){
			outRect.left=space/5;
			outRect.right=space/10;
		}
		if(parent.getChildAdapterPosition(view)%2==1){
			outRect.left=space/5;
			outRect.right=space/10;
		}
	}
	
}

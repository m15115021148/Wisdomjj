package com.sitemap.wisdomjingjiang.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * com.sitemap.wisdomjingjiang.utils.NoScrollViewPager
 * @author zhang
 * 禁止滚动的ViewPager
 * create at 2016年4月27日 下午4:24:35
 */
public class NoScrollViewPager extends ViewPager {

	  private boolean noScroll = true;//是否不滚动
	  
	    public NoScrollViewPager(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        // TODO Auto-generated constructor stub
	    }
	 
	    public NoScrollViewPager(Context context) {
	        super(context);
	    }
	 
	    public void setNoScroll(boolean noScroll) {
	        this.noScroll = noScroll;
	    }
	 
	    @Override
	    public void scrollTo(int x, int y) {
	        super.scrollTo(x, y);
	    }
	 
	    @Override
	    public boolean onTouchEvent(MotionEvent arg0) {
	        /* return false;//super.onTouchEvent(arg0); */
	        if (noScroll)
	            return false;
	        else
	            return super.onTouchEvent(arg0);
	    }
	 
	    @Override
	    public boolean onInterceptTouchEvent(MotionEvent arg0) {
	        if (noScroll)
	            return false;
	        else
	            return super.onInterceptTouchEvent(arg0);
	    }
	 
	    @Override
	    public void setCurrentItem(int item, boolean smoothScroll) {
	        super.setCurrentItem(item, smoothScroll);
	    }
	 
	    @Override
	    public void setCurrentItem(int item) {
	        super.setCurrentItem(item);
	    }
	 
	}
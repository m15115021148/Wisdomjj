package com.sitemap.wisdomjingjiang.views;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author Administrator
 *
 */
/** 
 * com.sitemap.wisdomjingjiang.views
 * @author chenmeng
 * @Description 弹出对话框
 * @date create at  2016年8月8日 下午3:09:08
 */
public class OrderDialog extends PopupWindow{
	private Context mContext;
	
	public OrderDialog(Context context,
			OnClickListener refundListener,
			OnClickListener rejectListener,
			final LinearLayout layout){
		this.mContext = context;
		final View view = LayoutInflater.from(mContext).inflate(
				R.layout.order_dialog, null);
		
		TextView refund = (TextView) view.findViewById(R.id.refund);
		TextView reject = (TextView) view.findViewById(R.id.reject);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);
		refund.setOnClickListener(refundListener);
		reject.setOnClickListener(rejectListener);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});		
		this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xB0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框        
        view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
			}
		});
	}	
	
}

package com.sitemap.wisdomjingjiang.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * 设置textview控件内部边缘的图片（比如： android:drawableTop="@drawable/zy_44"）
 * 
 * @author Administrator
 * 
 */
public class TextviewDrawable {
	public Context context;

	public TextviewDrawable(Context context) {
		this.context = context;
	}

	/**
	 * 
	 * @param view
	 * @param drawableId
	 * @param address
	 *            left:0,top:1,right:2,bottom:3
	 */
	public void setDrawable(TextView view, int drawableId, int address) {
		Drawable drawable = context.getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		switch (address) {
		case 0:
			view.setCompoundDrawables(drawable, null, null, null);
			break;
		case 1:
			view.setCompoundDrawables(null, drawable, null, null);
			break;
		case 2:
			view.setCompoundDrawables(null, null, drawable, null);
			break;
		case 3:
			view.setCompoundDrawables(null, null, null, drawable);
			break;
		default:
			break;
		}

	}
}
